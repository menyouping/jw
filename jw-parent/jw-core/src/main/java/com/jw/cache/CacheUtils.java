package com.jw.cache;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JointPointParameter;
import com.jw.cache.annotation.CacheKey;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;

public class CacheUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

    public static String getCacheKey(final JointPointParameter parameter, String[] cacheNames, String prefix,
            String join) {
        if (JwUtils.count(parameter.getArgs()) == 1) {
            return prefix + parameter.getArgs()[0].toString();
        } else {
            Map<String, Object> params = new LinkedHashMap<>();
            Annotation[][] methodParamAnnos = parameter.getMethod().getParameterAnnotations();
            if (!JwUtils.isEmpty(methodParamAnnos)) {
                Annotation[] paramAnnos;
                for (int i = 0; i < methodParamAnnos.length; i++) {
                    paramAnnos = methodParamAnnos[i];
                    for (Annotation ann : paramAnnos) {
                        if (ann.annotationType() != CacheKey.class) {
                            continue;
                        }
                        CacheKey cacheKey = (CacheKey) ann;
                        if (StringUtils.isEmpty(cacheKey.value())) {
                            String msg = "The value of CacheKey in method " + parameter.getMethod().getName()
                                    + " is empty.";
                            LOGGER.error(msg);
                            i = methodParamAnnos.length;
                            break;
                        }
                        params.put(cacheKey.value(), parameter.getArgs()[i]);
                        break;
                    }
                }
            }
            StringBuilder sb = new StringBuilder(prefix);
            boolean flag = false;
            Set<Entry<String, Object>> entries = params.entrySet();
            for (Entry<String, Object> entry : entries) {
                if (flag) {
                    sb.append(join);
                } else {
                    flag = true;
                }
                sb.append(entry.getValue());
            }
            return sb.toString();
        }

    }

}
