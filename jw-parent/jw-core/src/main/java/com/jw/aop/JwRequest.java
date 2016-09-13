package com.jw.aop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jw.util.SessionContext;

public class JwRequest implements HttpServletRequest {

    public JwRequest() {
    }

    @Override
    public Object getAttribute(String name) {
        return SessionContext.getRequest().getAttribute(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getAttributeNames() {
        return SessionContext.getRequest().getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return SessionContext.getRequest().getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        SessionContext.getRequest().setCharacterEncoding(env);
    }

    @Override
    public int getContentLength() {
        return SessionContext.getRequest().getContentLength();
    }

    @Override
    public String getContentType() {
        return SessionContext.getRequest().getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return SessionContext.getRequest().getInputStream();
    }

    @Override
    public String getParameter(String name) {
        return SessionContext.getRequest().getParameter(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getParameterNames() {
        return SessionContext.getRequest().getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return SessionContext.getRequest().getParameterValues(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getParameterMap() {
        return SessionContext.getRequest().getParameterMap();
    }

    @Override
    public String getProtocol() {
        return SessionContext.getRequest().getProtocol();
    }

    @Override
    public String getScheme() {
        return SessionContext.getRequest().getScheme();
    }

    @Override
    public String getServerName() {
        return SessionContext.getRequest().getServerName();
    }

    @Override
    public int getServerPort() {
        return SessionContext.getRequest().getServerPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return SessionContext.getRequest().getReader();
    }

    @Override
    public String getRemoteAddr() {
        return SessionContext.getRequest().getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return SessionContext.getRequest().getRemoteHost();
    }

    @Override
    public void setAttribute(String name, Object o) {
        SessionContext.getRequest().setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        SessionContext.getRequest().removeAttribute(name);
    }

    @Override
    public Locale getLocale() {
        return SessionContext.getRequest().getLocale();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getLocales() {
        return SessionContext.getRequest().getLocales();
    }

    @Override
    public boolean isSecure() {
        return SessionContext.getRequest().isSecure();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return SessionContext.getRequest().getRequestDispatcher(path);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getRealPath(String path) {
        return SessionContext.getRequest().getRealPath(path);
    }

    @Override
    public int getRemotePort() {
        return SessionContext.getRequest().getRemotePort();
    }

    @Override
    public String getLocalName() {
        return SessionContext.getRequest().getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return SessionContext.getRequest().getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return SessionContext.getRequest().getLocalPort();
    }

    @Override
    public String getAuthType() {
        return SessionContext.getRequest().getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return SessionContext.getRequest().getCookies();
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return SessionContext.getRequest().getHeader(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getHeaders(String name) {
        return SessionContext.getRequest().getHeaders(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getHeaderNames() {
        return SessionContext.getRequest().getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return SessionContext.getRequest().getIntHeader(name);
    }

    @Override
    public String getMethod() {
        return SessionContext.getRequest().getMethod();
    }

    @Override
    public String getPathInfo() {
        return SessionContext.getRequest().getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return SessionContext.getRequest().getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return SessionContext.getRequest().getContextPath();
    }

    @Override
    public String getQueryString() {
        return SessionContext.getRequest().getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return SessionContext.getRequest().getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return SessionContext.getRequest().isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return SessionContext.getRequest().getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return SessionContext.getRequest().getRequestedSessionId();
    }

    @Override
    public String getRequestURI() {
        return SessionContext.getRequest().getRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return SessionContext.getRequest().getRequestURL();
    }

    @Override
    public String getServletPath() {
        return SessionContext.getRequest().getServletPath();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return SessionContext.getRequest().getSession(create);
    }

    @Override
    public HttpSession getSession() {
        return SessionContext.getRequest().getSession();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return SessionContext.getRequest().isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return SessionContext.getRequest().isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return SessionContext.getRequest().isRequestedSessionIdFromURL();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return SessionContext.getRequest().isRequestedSessionIdFromUrl();
    }

}
