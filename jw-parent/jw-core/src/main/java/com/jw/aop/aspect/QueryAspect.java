package com.jw.aop.aspect;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JoinPoint;
import com.jw.aop.JointPointParameter;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Aspect;
import com.jw.aop.annotation.Pointcut;
import com.jw.aop.annotation.Query;
import com.jw.db.DbManagerFactory;
import com.jw.db.EntityUtils;
import com.jw.db.JwCallable;
import com.jw.db.JwRunnable;
import com.jw.db.JwTx;
import com.jw.db.SQLUtils;
import com.jw.util.CollectionUtils;
import com.jw.util.StringUtils;

@Aspect
public class QueryAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryAspect.class);

    @Pointcut("@annotation(com.jw.aop.annotation.Query)")
    public void queryPointcut() {

    }

    @Around("queryPointcut()")
    public Object aroundQueryHold(JoinPoint jp, final JointPointParameter parameter) {
        Query ann = parameter.getMethod().getAnnotation(Query.class);
        final String sql = ann.value();
        String dbName = ann.db();
        if (StringUtils.isEmpty(dbName)) {
            dbName = DbManagerFactory.getManager().getDefaultDBName();
        }

        if (parameter.getMethod().getReturnType() == Void.TYPE) {
            if (StringUtils.isEmpty(sql))
                return Void.TYPE;

            LOGGER.info("Sql execute: {}", sql);
            JwTx.run(dbName, new JwRunnable() {

                @Override
                public void run(Connection connection) throws Exception {
                    if (!"SELECT".equalsIgnoreCase(sql.trim().substring(0, "SELECT".length()))) {
                        SQLUtils.update(connection, sql, parameter.getArgs());
                    }
                }
            });
            return Void.TYPE;
        } else {
            if (StringUtils.isEmpty(sql))
                return null;
            LOGGER.info("Sql query: {}", sql);
            return JwTx.call(dbName, new JwCallable<Object>() {

                @Override
                public Object call(Connection connection) throws Exception {
                    if ("SELECT".equalsIgnoreCase(sql.trim().substring(0, "SELECT".length()))) {
                        ResultSet rs = SQLUtils.query(connection, sql, parameter.getArgs());
                        Class<?> targetClaze = parameter.getMethod().getReturnType();
                        if (targetClaze == ResultSet.class)
                            return rs;
                        if (targetClaze.isAssignableFrom(List.class)) {
                            ParameterizedType parameterizedType = (ParameterizedType) parameter.getMethod()
                                    .getGenericReturnType();
                            Class<?> modelClaze = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                            if (modelClaze.isAssignableFrom(Map.class)) {
                                return EntityUtils.toMaps(rs);
                            }
                            return EntityUtils.toList(rs, modelClaze);
                        }
                        if (targetClaze.isAssignableFrom(Map.class)) {
                            return CollectionUtils.first(EntityUtils.toMaps(rs));
                        }
                        return CollectionUtils.first(EntityUtils.toList(rs, targetClaze));
                    }
                    return null;
                }
            });
        }
    }

}
