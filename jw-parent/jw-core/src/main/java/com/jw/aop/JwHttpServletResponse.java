package com.jw.aop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jw.util.SessionContext;

public class JwHttpServletResponse implements HttpServletResponse {

    @Override
    public String getCharacterEncoding() {
        return SessionContext.getResponse().getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return SessionContext.getResponse().getContentType();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return SessionContext.getResponse().getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return SessionContext.getResponse().getWriter();
    }

    @Override
    public void setCharacterEncoding(String charset) {
        SessionContext.getResponse().setCharacterEncoding(charset);
    }

    @Override
    public void setContentLength(int len) {
        SessionContext.getResponse().setContentLength(len);
    }

    @Override
    public void setContentType(String type) {
        SessionContext.getResponse().setContentType(type);
    }

    @Override
    public void setBufferSize(int size) {
        SessionContext.getResponse().setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
        return SessionContext.getResponse().getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        SessionContext.getResponse().flushBuffer();
    }

    @Override
    public void resetBuffer() {
        SessionContext.getResponse().resetBuffer();
    }

    @Override
    public boolean isCommitted() {
        return SessionContext.getResponse().isCommitted();
    }

    @Override
    public void reset() {
        SessionContext.getResponse().reset();
    }

    @Override
    public void setLocale(Locale loc) {
        SessionContext.getResponse().setLocale(loc);
    }

    @Override
    public Locale getLocale() {
        return SessionContext.getResponse().getLocale();
    }

    @Override
    public void addCookie(Cookie cookie) {
        SessionContext.getResponse().addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return SessionContext.getResponse().containsHeader(name);
    }

    @Override
    public String encodeURL(String url) {
        return SessionContext.getResponse().encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return SessionContext.getResponse().encodeRedirectURL(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeUrl(String url) {
        return SessionContext.getResponse().encodeUrl(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeRedirectUrl(String url) {
        return SessionContext.getResponse().encodeRedirectUrl(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        SessionContext.getResponse().sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
        SessionContext.getResponse().sendError(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        SessionContext.getResponse().sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        SessionContext.getResponse().setDateHeader(name, date);
    }

    @Override
    public void addDateHeader(String name, long date) {
        SessionContext.getResponse().addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        SessionContext.getResponse().setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        SessionContext.getResponse().addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        SessionContext.getResponse().setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        SessionContext.getResponse().addIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        SessionContext.getResponse().setStatus(sc);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String sm) {
        SessionContext.getResponse().setStatus(sc, sm);
    }

}
