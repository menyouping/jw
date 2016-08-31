package com.jw.db;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jw.domain.annotation.Id;
import com.jw.domain.annotation.Table;
import com.jw.util.JwUtils;
import com.jw.util.Pair;
import com.jw.util.StringUtils;

public class EntityUtils {

    private EntityUtils() {
    }

    public static Map<String, Object> toMap(Object entity) {
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
                e.printStackTrace();
            }
        }

        return map;
    }

    public static final String INSERT_SQL = "INSERT INTO %s(%s) VALUES(%s)";

    public static void save(Object entity) {
        Map<String, Object> map = toMap(entity);

        String sql = String.format(INSERT_SQL, getTableName(entity), JwUtils.join(map.keySet()),
                JwUtils.repeat('?', ',', map.size()));
        SQLUtils.executeUpdate(sql, map.values().toArray());
    }

    public static final String DELETE_SQL = "DELETE FROM %s WHERE %s = ?";

    public static void delete(Object entity) {
        Pair pair = getPrimaryKey(entity);
        String sql = String.format(DELETE_SQL, getTableName(entity), pair.getKey());
        SQLUtils.executeUpdate(sql, new Object[] { pair.getValue() });
    }

    public void delete(Class<?> claze, int id) {
        String sql = String.format(DELETE_SQL, getTableName(claze), "id");
        SQLUtils.executeUpdate(sql, new Object[] { id });
    }

    public void delete(Class<?> claze, String uuid) {
        String sql = String.format(DELETE_SQL, getTableName(claze), "uuid");
        SQLUtils.executeUpdate(sql, new Object[] { uuid });
    }

    public static final String UPDATE_SQL = "UPDATE %s SET %s WHERE %s = ?";

    public void update(Object entity) {
        Pair pair = getPrimaryKey(entity);

        Map<String, Object> map = toMap(entity);
        map.remove(pair.getKey());

        String keys = JwUtils.join(map.keySet(), "=?,") + "=?";
        String sql = String.format(UPDATE_SQL, getTableName(entity), keys, pair.getKey());

        List<Object> values = new ArrayList<Object>(map.values());
        values.add(pair.getValue());
        SQLUtils.executeUpdate(sql, values.toArray());
    }

    public static final String FETCH_SQL = "SELECT * FROM %s WHERE %s = ?";

    public static <T> T load(Class<T> claze, int id) {
        String sql = String.format(FETCH_SQL, getTableName(claze), "id");
        ResultSet rs = SQLUtils.executeQuery(sql, new Object[] { id });
        List<T> list = toList(rs, claze);

        return list.isEmpty() ? null : list.get(0);
    }

    public static <T> T load(Class<T> claze, String uuid) {
        String sql = String.format(FETCH_SQL, getTableName(claze), "uuid");
        ResultSet rs = SQLUtils.executeQuery(sql, new Object[] { uuid });
        List<T> list = toList(rs, claze);

        return list.isEmpty() ? null : list.get(0);
    }

    public static final String COUNT_SQL = "SELECT COUNT(*) FROM %s";

    public static <T> int count(Class<T> claze) {
        String sql = String.format(COUNT_SQL, getTableName(claze));
        ResultSet rs = SQLUtils.executeQuery(sql, new Object[0]);
        int count = 0;
        try {
            if (rs.next()) {
                count = rs.getInt(1);
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static <T> List<T> toList(ResultSet rs, Class<T> claze) {
        List<T> list = new ArrayList<T>();

        try {
            if (Map.class.isAssignableFrom(claze)) {
                T t = claze.newInstance();

                ResultSetMetaData metaData = rs.getMetaData();
                int count = metaData.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= count; i++) {
                        ((Map) t).put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    list.add(t);
                }
            } else {
                Field[] fields = claze.getDeclaredFields();

                while (rs.next()) {
                    T t = claze.newInstance();
                    Transient tr;
                    for (Field field : fields) {
                        tr = field.getAnnotation(Transient.class);
                        if (tr == null || !tr.value()) {
                            field.setAccessible(true);
                            field.set(t, rs.getObject(StringUtils.toColumnName(field.getName())));
                        }
                    }

                    list.add(t);
                }
            }

            SQLUtils.release(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static final String FETCH_ALL_SQL = "SELECT * FROM %s";

    public static <T> List<T> findAll(Class<T> claze) {
        String sql = String.format(FETCH_ALL_SQL, getTableName(claze));
        ResultSet rs = SQLUtils.executeQuery(sql, new Object[0]);
        return toList(rs, claze);
    }

    public static <T> List<T> find(Class<T> claze, String sql, Object... params) {
        ResultSet rs = SQLUtils.executeQuery(sql, params);
        return toList(rs, claze);
    }

    public static <T> List<T> findByPage(Class<T> claze, String sql, int pageNo, int pageSize, Object... params) {
        sql = sql + " LIMIT " + (pageNo - 1) * pageSize + "," + pageSize;
        ResultSet rs = SQLUtils.executeQuery(sql, params);
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
        if (fields == null || fields.length == 0)
            return null;

        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    return StringUtils.toColumnName(field.getName());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static Pair getPrimaryKey(Object entity) {
        Class<?> claze = entity.getClass();

        Field[] fields = claze.getFields();
        if (fields == null || fields.length == 0)
            return null;

        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.setAccessible(true);
                    return new Pair(StringUtils.toColumnName(field.getName()), field.get(entity));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
