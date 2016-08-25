package com.jw.web.servlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jw.util.ConfigUtils;
import com.jw.util.FileUtils;
import com.jw.util.JwUtils;
import com.jw.util.MimeUtils;
import com.jw.util.SessionContext;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;
import com.jw.web.bind.annotation.RequestParam;
import com.jw.web.bind.annotation.ResponseBody;
import com.jw.web.context.AppContext;
import com.jw.web.context.UrlMapping;
import com.jw.web.context.UrlMappingRegistry;

public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = -3874308705324703315L;

    private static final String PAGES = "WEB-INF/" + ConfigUtils.getProperty("web.page.folder") + "/";

    private static final String RESOURCES = ConfigUtils.getProperty("web.resources.folder") + "/";

    private static final String RESOURCE_EXTS = ConfigUtils.getProperty("web.resources.ext");
    
    private static final String DEFAULT_EXTENSION = ConfigUtils.getProperty("web.page.default.extension");

    public DispatcherServlet() {
    }

    public void init() {

    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StringBuffer url = request.getRequestURL();
        String path = request.getRequestURI();

        if (url.indexOf("localhost") > -1 || url.indexOf("127.0.0.1") > -1 || url.indexOf("0.0.0.0") > -1) {
            path = path.substring(path.indexOf("/", 1));
        }

        if (isStaticResource(response, path))
            return;


        UrlMapping urlMapping = UrlMappingRegistry.match(path);
        if (urlMapping == null) {
            showError(request, response, HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        LOGGER.info("Request " + request.getRequestURI() + " is mathched:" + urlMapping);

        Method method = urlMapping.getMethod();

        RequestMethod[] allowActions = method.getAnnotation(RequestMapping.class).method();
        if (allowActions.length > 0) {
            boolean isAllowVisit = false;
            String action = request.getMethod();
            for (RequestMethod allowAction : allowActions) {
                if (allowAction.name().equals(action)) {
                    isAllowVisit = true;
                    break;
                }
            }
            if (!isAllowVisit) {
                showError(request, response, HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
        }

        SessionContext.buildContext().set(SessionContext.REQUEST, request)
                .set(SessionContext.RESPONSE, response)
                .set(SessionContext.SESSION, request.getSession());

        try {
            Object[] paras = autowireParameters(urlMapping);
            Object controller = AppContext.getBean(urlMapping.getClaze());
            if (method.getReturnType().equals(String.class)) {
                String returnUrl = (String) method.invoke(controller, paras);
                if (!StringUtils.isEmpty(returnUrl)) {
                    if (returnUrl.split(":")[0].equals("redirect")) {
                        response.sendRedirect(returnUrl.split(":")[1]);
                    } else {
                        RequestDispatcher dispatcher = request.getRequestDispatcher(getPage(returnUrl + DEFAULT_EXTENSION));
                        dispatcher.forward(request, response);
                    }
                }
            } else if (JwUtils.isAnnotated(method, ResponseBody.class)) {
                Object result = method.invoke(controller, paras);
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(result));
            } else {
                method.invoke(controller, paras);
            }
        } catch (Exception e) {
            LOGGER.error("Error raised in DispatcherServlet.", e);
            showError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    public static String getPage(String fileName) {
        return PAGES + (fileName.startsWith("/") ? fileName.substring(1) : fileName);
    }

    public static String getResource(String fileName) {
        return RESOURCES + (fileName.startsWith("/") ? fileName.substring(1) : fileName);
    }

    protected void showError(HttpServletRequest request, HttpServletResponse response, int status)
            throws ServletException, IOException {
        response.setStatus(status);
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        String page = this.getServletContext().getRealPath(getPage(status + ".html"));
        FileUtils.copy(page, response.getOutputStream());
    }

    protected boolean isStaticResource(HttpServletResponse response, String path) throws IOException {
        int index = path.lastIndexOf(".");
        if (index > -1 || index < path.length() - 1) {
            String ext = path.substring(index + 1).toLowerCase();
            if (RESOURCE_EXTS.contains(ext)) {
                response.setHeader("Content-type", MimeUtils.getMimeType(ext) + ";charset=UTF-8");
                String page = this.getServletContext().getRealPath(getResource(path));
                FileUtils.copy(page, response.getOutputStream());
                return true;
            }

            if ("html".equals(ext)) {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                String page = this.getServletContext().getRealPath(getPage(path));
                FileUtils.copy(page, response.getOutputStream());
                return true;
            }
        }
        return false;
    }

    protected static Object[] autowireParameters(UrlMapping urlMapping)
            throws InstantiationException, IllegalAccessException {
        Class<?>[] paramClazes = urlMapping.getMethod().getParameterTypes();
        Annotation[][] paramAnnos = urlMapping.getMethod().getParameterAnnotations();
        int parameterCount = paramClazes.length;

        Object[] paras = new Object[parameterCount];

        for (int p = 0; p < parameterCount; p++) {
            paras[p] = autowireParameter(paramClazes[p], paramAnnos[p]);
        }
        return paras;
    }

    protected static Object autowireParameter(Class<?> paramClaze, Annotation[] paramAnnos)
            throws InstantiationException, IllegalAccessException {
        if (HttpServletRequest.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getRequest();
        }
        if (HttpServletResponse.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getResponse();
        }
        if (HttpSession.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getSession();
        }

        for (Annotation anno : paramAnnos) {
            if (RequestParam.class.isAssignableFrom(anno.annotationType())) {
                RequestParam requestParam = (RequestParam) anno;
                String paramValue = SessionContext.getRequest().getParameter(requestParam.value());
                SessionContext.getRequest().setAttribute(requestParam.value(), paramValue);
                return paramValue;
            }
        }

        HttpServletRequest request = SessionContext.getRequest();
        Object dto = paramClaze.newInstance();
        Field[] fields = paramClaze.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.getType().equals(String.class)) {
                field.set(dto, request.getParameter(fieldName));
                continue;
            }
            if (field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)) {
                if (request.getParameter(fieldName) == null) {
                    field.set(dto, 0);
                } else {
                    field.set(dto, Integer.valueOf(request.getParameter(fieldName)));
                }
            } else if (field.getType().equals(Long.TYPE) || field.getType().equals(Long.class)) {
                if (request.getParameter(fieldName) == null) {
                    field.set(dto, 0L);
                } else {
                    field.set(dto, Long.valueOf(request.getParameter(fieldName)));
                }
            } else if (field.getType().equals(Double.TYPE) || field.getType().equals(Double.class)) {
                if (request.getParameter(fieldName) == null) {
                    field.set(dto, 0.0);
                } else {
                    field.set(dto, Double.valueOf(request.getParameter(fieldName)));
                }
            }
        }
        return dto;
    }
}
