package com.jw.db;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.db.transformer.ResultSetTransformer;
import com.jw.db.transformer.ResultSetTransformerFactory;
import com.jw.domain.annotation.Id;
import com.jw.domain.annotation.PostPersist;
import com.jw.domain.annotation.PostRemove;
import com.jw.domain.annotation.PostUpdate;
import com.jw.domain.annotation.PrePersist;
import com.jw.domain.annotation.PreRemove;
import com.jw.domain.annotation.PreUpdate;
import com.jw.domain.annotation.Table;
import com.jw.util.JwUtils;
import com.jw.util.Pair;
import com.jw.util.StringUtils;

public class EntityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);

    private EntityUtils() {
    }

    public static final String INSERT_SQL = "INSERT INTO %s(%s) VALUES(%s)";

    public static Object create(Object entity) {
        Class<?> claze = entity.getClass();
        JwUtils.runMethodWithAnnotation(claze, entity, PrePersist.class);

        Map<String, Object> map = convert2Map(entity);
        create(claze, map);

        JwUtils.runMethodWithAnnotation(claze, entity, PostPersist.class);
        return entity;
    }

    public static void create(Class<?> claze, Map<String, Object> params) {
        String sql = String.format(INSERT_SQL, getTableName(claze), JwUtils.join(params.keySet()),
                JwUtils.repeat('?', ',', params.size()));
        SQLUtils.update(sql, params.values().toArray());
    }

    public static final String DELETE_SQL = "DELETE FROM %s WHERE %s = ?";

    public static void delete(Object entity) {
        Class<?> claze = entity.getClass();
        JwUtils.runMethodWithAnnotation(claze, entity, PreRemove.class);

        Pair pair = getPrimaryKey(entity);
        String sql = String.format(DELETE_SQL, getTableName(entity), pair.getKey());
        SQLUtils.update(sql, new Object[] { pair.getValue() });

        JwUtils.runMethodWithAnnotation(claze, entity, PostRemove.class);
    }

    public static void delete(Class<?> claze, Map<String, Object> params) {
        if (JwUtils.isEmpty(params))
            return;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(getTableName(claze)).append(" WHERE ");

        String where = JwUtils.join(params.keySet(), "=? AND ") + "=?";
        sb.append(where);
        SQLUtils.update(sb.toString(), params.values().toArray());
    }

    public void delete(Class<?> claze, int id) {
        String sql = String.format(DELETE_SQL, getTableName(claze), "id");
        SQLUtils.update(sql, new Object[] { id });
    }

    public void delete(Class<?> claze, String uuid) {
        String sql = String.format(DELETE_SQL, getTableName(claze), "uuid");
        SQLUtils.update(sql, new Object[] { uuid });
    }

    public static final String UPDATE_SQL = "UPDATE %s SET %s WHERE %s = ?";

    public void update(Object entity) {
        Class<?> claze = entity.getClass();
        JwUtils.runMethodWithAnnotation(claze, entity, PreUpdate.class);

        Map<String, Object> map = convert2Map(entity);
        update(entity.getClass(), map);

        JwUtils.runMethodWithAnnotation(claze, entity, PostUpdate.class);
    }

    public void update(Class<?> claze, Map<String, Object> params) {
        if (JwUtils.isEmpty(params))
            return;
        String primaryKey = getPrimaryKey(claze);
        Object primaryVal = params.remove(primaryKey);

        String keys = JwUtils.join(params.keySet(), "=?,") + "=?";
        String sql = String.format(UPDATE_SQL, getTableName(claze), keys, primaryKey);

        List<Object> values = new ArrayList<Object>(params.values());
        values.add(primaryVal);
        SQLUtils.update(sql, values.toArray());
    }

    public static final String FETCH_SQL = "SELECT * FROM %s WHERE %s = ?";

    public static <T> T find(Class<T> claze, int id) {
        String sql = String.format(FETCH_SQL, getTableName(claze), "id");
        ResultSet rs = SQLUtils.query(sql, new Object[] { id });
        List<T> list = toList(rs, claze);

        return list.isEmpty() ? null : list.get(0);
    }

    public static <T> T find(Class<T> claze, String uuid) {
        String sql = String.format(FETCH_SQL, getTableName(claze), "uuid");
        ResultSet rs = SQLUtils.query(sql, new Object[] { uuid });
        List<T> list = toList(rs, claze);

        return list.isEmpty() ? null : list.get(0);
    }

    public static <T> T find(Class<T> claze, Map<String, Object> params) {
        if (JwUtils.isEmpty(params))
            return null;
        List<T> list = findList(claze, params);
        return list.isEmpty() ? null : list.get(0);
    }

    public static final String COUNT_SQL = "SELECT COUNT(*) FROM %s";

    public static <T> int count(Class<T> claze) {
        String sql = String.format(COUNT_SQL, getTableName(claze));
        ResultSet rs = SQLUtils.query(sql, new Object[0]);
        int count = 0;
        try {
            if (rs.next()) {
                count = rs.getInt(1);
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            LOGGER.error("Error raised when statistic the count of " + claze.getName(), e);
        }

        return count;
    }

    public static final String FETCH_ALL_SQL = "SELECT * FROM %s";

    public static <T> List<T> findAll(Class<T> claze) {
        String sql = String.format(FETCH_ALL_SQL, getTableName(claze));
        ResultSet rs = SQLUtils.query(sql, new Object[0]);
        return toList(rs, claze);
    }

    /**
     * 
     * @param dtoClaze
     * @param tableNameOrSql
     *            sys_user or select id, name from sys_user
     * @param params
     * @return
     */
    public static <T> List<T> findDtos(Class<T> dtoClaze, String tableNameOrSql, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (tableNameOrSql.length() <= 6 || !"SELECT".equals(tableNameOrSql.substring(0, 6).toUpperCase())) {
            List<String> fields = getColumnNames(dtoClaze);
            sb.append("SELECT ").append(JwUtils.join(fields)).append(" FROM ").append(tableNameOrSql);
        } else {
            sb.append(tableNameOrSql);
        }

        String where = JwUtils.join(params.keySet(), "=? AND ") + "=?";
        sb.append(" WHERE ").append(where);
        ResultSet rs = SQLUtils.query(sb.toString(), params.values().toArray());
        return toList(rs, dtoClaze);
    }

    public static List<Map<String, Object>> findMaps(String tableName, String[] fields, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(JwUtils.join(fields)).append(" FROM ").append(tableName);

        return findMaps(sb.toString(), params);
    }

    /**
     * @param sql
     *            select id, name from sys_user
     * @param params
     *            where condition
     * @return
     */
    public static List<Map<String, Object>> findMaps(String sql, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(sql).append(" WHERE ");

        String where = JwUtils.join(params.keySet(), "=? AND ") + "=?";
        sb.append(where);
        ResultSet rs = SQLUtils.query(sql, params);

        return toMaps(rs);
    }

    /**
     * @param claze
     * @param sql
     *            select * from sys_user where age < ? and sex = ?
     * @param params
     * @return
     */
    public static <T> List<T> findList(Class<T> claze, String sql, Object[] params) {
        ResultSet rs = SQLUtils.query(sql, params);
        return toList(rs, claze);
    }

    public static <T> List<T> findList(Class<T> claze, Map<String, Object> params) {
        if (JwUtils.isEmpty(params))
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(getTableName(claze)).append(" WHERE ");

        String where = JwUtils.join(params.keySet(), "=? AND ") + "=?";
        sb.append(where);
        ResultSet rs = SQLUtils.query(sb.toString(), params.values().toArray());
        return toList(rs, claze);
    }

    public static <T> List<T> findByPage(Class<T> claze, String sql, int pageNo, int pageSize, Object[] params) {
        sql = sql + " LIMIT " + (pageNo - 1) * pageSize + "," + pageSize;
        ResultSet rs = SQLUtils.query(sql, params);
        return toList(rs, claze);
    }

    public static String getTableName(Object entity) {
        return getTableName(entity.getClass());
    }

    public static String getTableName(Class<?> claze) {
        Table ann = claze.getAnnotation(Table.class);
        return ann == null ? StringUtils.toColumnName(claze.getSimpleName()) : ann.name();
    }

    public static <T> String getPrimaryKey(Class<T> claze) {
        Field[] fields = claze.getFields();
        if (JwUtils.isEmpty(fields))
            return null;

        for (Field field : fields) {
            if (!JwUtils.isAnnotated(field, Id.class))
                continue;
            try {
                return StringUtils.toColumnName(field.getName());
            } catch (IllegalArgumentException e) {
                LOGGER.error("Error raised in getPrimaryKey method", e);
            }
        }

        return null;
    }

    public static Pair getPrimaryKey(Object entity) {
        Class<?> claze = entity.getClass();

        Field[] fields = claze.getFields();
        if (JwUtils.isEmpty(fields))
            return null;

        for (Field field : fields) {
            if (!JwUtils.isAnnotated(field, Id.class))
                continue;
            try {
                field.setAccessible(true);
                return new Pair(StringUtils.toColumnName(field.getName()), field.get(entity));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                LOGGER.error("Error raised in getPrimaryKey method", e);
            }
        }

        return null;
    }

    public static <T> List<String> getColumnNames(Class<T> claze) {
        List<String> colNames = JwUtils.newLinkedList();

        Field[] fields = claze.getFields();
        if (JwUtils.isEmpty(fields))
            return null;

        for (Field field : fields) {
            if (!JwUtils.isAnnotated(field, Id.class))
                continue;
            colNames.add(StringUtils.toColumnName(field.getName()));
        }

        return colNames;
    }

    public static List<Map<String, Object>> toMaps(ResultSet rs) {
        List<Map<String, Object>> list = JwUtils.newLinkedList();

        try {

            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();

            Map<String, Object> t = null;
            while (rs.next()) {
                t = JwUtils.newHashMap(count);
                for (int i = 1; i <= count; i++) {
                    t.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                list.add(t);
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            LOGGER.error("Error raised in toMaps method", e);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(ResultSet rs, Class<T> claze) {
        if (String.class.equals(claze)) {
            return (List<T>) toStrings(rs);
        }
        if (Integer.TYPE.equals(claze) || Integer.class.equals(claze)) {
            return (List<T>) toInts(rs);
        }
        if (Long.TYPE.equals(claze) || Long.class.equals(claze)) {
            return (List<T>) toLongs(rs);
        }
        if (Double.TYPE.equals(claze) || Double.class.equals(claze)) {
            return (List<T>) toDoubles(rs);
        }
        if (Float.TYPE.equals(claze) || Float.class.equals(claze)) {
            return (List<T>) toFloats(rs);
        }
        if (Date.class.equals(claze)) {
            return (List<T>) toDates(rs);
        }
        List<T> list = new ArrayList<T>();

        try {
            Field[] fields = claze.getDeclaredFields();

            Transient tr = null;
            while (rs.next()) {
                T t = claze.newInstance();
                for (Field field : fields) {
                    tr = field.getAnnotation(Transient.class);
                    if (tr == null || !tr.value()) {
                        field.setAccessible(true);
                        field.set(t, rs.getObject(StringUtils.toColumnName(field.getName())));
                    }
                }

                list.add(t);
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            LOGGER.error("Error raised in toList method", e);
        }

        return list;
    }

    public static List<String> toStrings(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getStringTransformer());
    }

    public static List<Integer> toInts(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getIntegerTransformer());
    }

    public static List<Long> toLongs(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getLongTransformer());
    }

    public static List<Double> toDoubles(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getDoubleTransformer());
    }

    public static List<Float> toFloats(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getFloatTransformer());
    }

    public static List<Date> toDates(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getDateTransformer());
    }

    public static List<Date> toBools(ResultSet rs) {
        return toList(rs, ResultSetTransformerFactory.getBoolTransformer());
    }

    public static <T> List<T> toList(ResultSet rs, ResultSetTransformer transformer) {
        List<T> list = new ArrayList<T>();

        try {
            while (rs.next()) {
                T t = transformer.transform(rs);
                list.add(t);
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            LOGGER.error("Error raised in toList method", e);
        }

        return list;
    }

    public static Map<String, Object> convert2Map(Object entity) {
        Class<?> claze = entity.getClass();
        Field[] fields = claze.getDeclaredFields();

        Map<String, Object> map = JwUtils.newHashMap(fields.length);
        Transient ann = null;
        for (Field field : fields) {
            try {
                ann = field.getAnnotation(Transient.class);
                if (ann == null || !ann.value()) {
                    field.setAccessible(true);
                    map.put(StringUtils.toColumnName(field.getName()), field.get(entity));
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error raised in convert2Map method", e);
            }
        }

        return map;
    }

}
