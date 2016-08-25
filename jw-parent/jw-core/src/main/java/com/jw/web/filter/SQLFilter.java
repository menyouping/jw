package com.jw.web.filter;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.jw.util.JwUtils;

public class SQLFilter implements Filter {

    private static final Pattern PATTERN = Pattern.compile("('|and|exec|insert|select|delete|update|count|\\*|%|chr|mid|master|truncate|char|declare|;|or|-|\\+|,)");

    public void init(FilterConfig config) throws ServletException {
    }

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Set<Entry<String, String[]>> entries = req.getParameterMap().entrySet();
        for (Entry<String, String[]> entry : entries) {
            if (JwUtils.isEditorField(entry.getKey()))
                continue;
            safe(entry.getValue());
        }
        chain.doFilter(request, response);
    }

    public void safe(String[] values) {
        if (values == null)
            return;
        int len = values.length;
        
        Matcher m;
        for (int i = 0; i < len; i++) {
            m = PATTERN.matcher(values[i]);
            if (!m.find())
                continue;
            
            StringBuilder sb = new StringBuilder(values[i]);
            int start = 0, end = 0;
            int offset = 0;
            while (m.find(start)) {
                start = m.start();
                end = m.end();
                if ("'".equals(m.group(1))) {
                    sb.replace(start + offset, end + offset, "");
                    offset--;
                } else {
                    sb.insert(start + offset, "&nbsp;");
                    offset += 6;
                }
                start = end;
            }
            values[i] = sb.toString();
        }
    }

    @Override
    public void destroy() {

    }

}
