package com.jw.aop;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.jw.util.SessionContext;

@SuppressWarnings("deprecation")
public class JwHttpSession implements HttpSession {

    @Override
    public long getCreationTime() {
        return SessionContext.getSession().getCreationTime();
    }

    @Override
    public String getId() {
        return SessionContext.getSession().getId();
    }

    @Override
    public long getLastAccessedTime() {
        return SessionContext.getSession().getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return SessionContext.getSession().getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        SessionContext.getSession().setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return SessionContext.getSession().getMaxInactiveInterval();
    }

    @SuppressWarnings("deprecation")
    @Override
    public HttpSessionContext getSessionContext() {
        return SessionContext.getSession().getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return SessionContext.getSession().getAttribute(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object getValue(String name) {
        return SessionContext.getSession().getValue(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getAttributeNames() {
        return SessionContext.getSession().getAttributeNames();
    }

    @SuppressWarnings("deprecation")
    @Override
    public String[] getValueNames() {
        return SessionContext.getSession().getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        SessionContext.getSession().setAttribute(name, value);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void putValue(String name, Object value) {
        SessionContext.getSession().putValue(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        SessionContext.getSession().removeAttribute(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeValue(String name) {
        SessionContext.getSession().removeValue(name);
    }

    @Override
    public void invalidate() {
        SessionContext.getSession().invalidate();
    }

    @Override
    public boolean isNew() {
        return SessionContext.getSession().isNew();
    }

}
