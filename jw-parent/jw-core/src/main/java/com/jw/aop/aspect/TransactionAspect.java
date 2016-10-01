package com.jw.aop.aspect;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JoinPoint;
import com.jw.aop.JointPointParameter;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Aspect;
import com.jw.aop.annotation.Pointcut;
import com.jw.aop.annotation.Transaction;
import com.jw.db.DbManagerFactory;
import com.jw.db.JwCallable;
import com.jw.db.JwConnection;
import com.jw.db.JwRunnable;
import com.jw.db.JwTx;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;

@Aspect
public class TransactionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionAspect.class);

    @Pointcut("@annotation(com.jw.aop.annotation.Transaction)")
    public void txPointcut() {

    }

    @Around("txPointcut()")
    public Object aroundTxHold(JoinPoint jp, final JointPointParameter parameter) {
        String dbName = parameter.getMethod().getAnnotation(Transaction.class).value();
        if (StringUtils.isEmpty(dbName)) {
            dbName = DbManagerFactory.getManager().getDefaultDBName();
        }

        if (parameter.getMethod().getReturnType() == Void.TYPE) {
            JwTx.run(dbName, new JwRunnable() {

                @Override
                public void run(Connection connection) throws Exception {
                    setConnection(parameter, connection);
                    try {
                        parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
                    } catch (Throwable e) {
                        LOGGER.error("Error raised when invorked method " + parameter, e);
                        throw new Exception(e);
                    }
                }
            });
            return Void.TYPE;
        } else {
            return JwTx.call(dbName, new JwCallable<Object>() {

                @Override
                public Object call(Connection connection) throws Exception {
                    setConnection(parameter, connection);
                    try {
                        return parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
                    } catch (Throwable e) {
                        LOGGER.error("Error raised when invorked method " + parameter, e);
                        throw new Exception(e);
                    }
                }
            });
        }
    }

    private void setConnection(JointPointParameter parameter, Connection connection) {
        Object[] args = parameter.getArgs();
        if (!JwUtils.isEmpty(args)) {
            Object arg = null;
            for (int i = 0; i < args.length; i++) {
                arg = args[i];
                if (arg != null && arg instanceof JwConnection) {
                    ((JwConnection) arg).setConnection(connection);
                    break;
                }
            }
        }
    }

}
