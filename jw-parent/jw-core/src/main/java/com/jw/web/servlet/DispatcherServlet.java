package com.jw.web.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.ui.JwModel;
import com.jw.ui.Model;
import com.jw.util.ConfigUtils;
import com.jw.util.FileUtils;
import com.jw.util.JwUtils;
import com.jw.util.MimeUtils;
import com.jw.util.SessionContext;
import com.jw.util.StringUtils;
import com.jw.validation.ConstraintValidatorManager;
import com.jw.validation.ValidErrors;
import com.jw.web.bind.annotation.CookieValue;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestHeader;
import com.jw.web.bind.annotation.RequestMethod;
import com.jw.web.bind.annotation.RequestParam;
import com.jw.web.bind.annotation.ResponseBody;
import com.jw.web.bind.annotation.Valid;
import com.jw.web.context.AppContext;
import com.jw.web.context.UrlMapping;
import com.jw.web.context.UrlMappingRegistry;

public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = -3874308705324703315L;

    private static final String PAGE_FOLDER = "WEB-INF/" + ConfigUtils.getProperty("web.page.folder") + "/";

    private static final String RESOURCE_FOLDER = ConfigUtils.getProperty("web.resources.folder") + "/";

    private static final String RESOURCES_EXTENSION = ConfigUtils.getProperty("web.resources.extension");

    private static final String PAGE_DEFAULT_EXTENSION = ConfigUtils.getProperty("web.page.default.extension");

    public DispatcherServlet() {
    }

    public void init() {
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String appName = request.getSession().getServletContext().getContextPath();
        request.setAttribute("root", appName);

        String path = request.getRequestURI().substring(appName.length());
        // handle static resource, e.g. css, js, html...
        if (isStaticResource(response, path))
            return;
        // remove the last /
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        // find matched method
        UrlMapping urlMapping = UrlMappingRegistry.match(request.getMethod(), path);
        if (urlMapping == null) {
            showError(request, response, HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        LOGGER.info("Request {} is matched:{}", request.getRequestURI(), urlMapping.getUrl());
        // build context
        SessionContext.buildContext().set(SessionContext.REQUEST, request).set(SessionContext.RESPONSE, response)
                .set(SessionContext.SESSION, request.getSession()).set("requestUrl", path);
        // upload file
        if (ServletFileUpload.isMultipartContent(request)) {
            SessionContext.getContext().set(SessionContext.FILE_UPLOAD_PARAMETERS,
                    FileUtils.uploadFiles(this, request));
        }
        // invork method
        Method method = urlMapping.getMethod();
        Object[] paras = null;
        try {
            Object controller = AppContext.getBean(urlMapping.getClaze());

            List<Method> modelAttributeMethods = JwUtils.findMethods(urlMapping.getClaze(), ModelAttribute.class);
            if (!JwUtils.isEmpty(modelAttributeMethods)) {
                for (Method modelAttributeMethod : modelAttributeMethods) {
                    paras = autowireParameters(null, modelAttributeMethod);
                    modelAttributeMethod.invoke(controller, paras);
                }
            }
            paras = autowireParameters(urlMapping, urlMapping.getMethod());
            if (JwUtils.isAnnotated(method, ResponseBody.class)) {
                Object result = method.invoke(controller, paras);
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(result));
            } else if (String.class.equals(method.getReturnType())) {
                String returnUrl = (String) method.invoke(controller, paras);
                if (!StringUtils.isEmpty(returnUrl)) {
                    if (returnUrl.split(":")[0].equals("redirect")) {
                        response.sendRedirect(returnUrl.split(":")[1]);
                    } else {
                        // pass model to jsp page
                        Model model = SessionContext.getModel();
                        if (model != null) {
                            Map<String, Object> map = SessionContext.getModel().asMap();
                            for (Entry<String, Object> entry : map.entrySet()) {
                                request.setAttribute(entry.getKey(), entry.getValue());
                            }
                        }
                        RequestDispatcher dispatcher = request
                                .getRequestDispatcher("/" + getPage(returnUrl + "." + PAGE_DEFAULT_EXTENSION));
                        dispatcher.forward(request, response);
                    }
                }
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
        return PAGE_FOLDER + (fileName.startsWith("/") ? fileName.substring(1) : fileName);
    }

    public static String getResource(String fileName) {
        return RESOURCE_FOLDER + (fileName.startsWith("/") ? fileName.substring(1) : fileName);
    }

    protected void showError(HttpServletRequest request, HttpServletResponse response, int status)
            throws ServletException, IOException {
        response.setStatus(status);
        if ("jsp".equals(PAGE_DEFAULT_EXTENSION)) {
            String page = "/" + getPage(status + "." + PAGE_DEFAULT_EXTENSION);
            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } else {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String page = this.getServletContext().getRealPath(getPage(status + "." + PAGE_DEFAULT_EXTENSION));
            FileUtils.copy(page, response.getOutputStream());
        }
    }

    protected boolean isStaticResource(HttpServletResponse response, String path) throws IOException {
        int index = path.lastIndexOf(".");
        if (index > -1 && index < path.length() - 1) {
            String ext = path.substring(index + 1).toLowerCase();
            if (RESOURCES_EXTENSION.contains(";" + ext + ";")) {
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

    protected static Object[] autowireParameters(UrlMapping urlMapping, Method method) throws Exception {
        Class<?>[] paramClazes = method.getParameterTypes();
        Annotation[][] paramAnnos = method.getParameterAnnotations();
        int parameterCount = paramClazes.length;

        Object[] paras = new Object[parameterCount];

        for (int p = 0; p < parameterCount; p++) {
            paras[p] = autowireParameter(urlMapping, paramClazes[p], paramAnnos[p]);
        }
        return paras;
    }

    @SuppressWarnings("unchecked")
    protected static Object autowireParameter(UrlMapping urlMapping, Class<?> paramClaze, Annotation[] paramAnnos)
            throws Exception {
        if (HttpServletRequest.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getRequest();
        }
        if (HttpServletResponse.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getResponse();
        }
        if (HttpSession.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getSession();
        }
        if (Model.class.isAssignableFrom(paramClaze)) {
            if (!SessionContext.getContext().containsKey(SessionContext.MODEL)) {
                SessionContext.getContext().put(SessionContext.MODEL, new JwModel());
            }
            return SessionContext.getModel();
        }
        if (ValidErrors.class.isAssignableFrom(paramClaze)) {
            return SessionContext.getContext().get(SessionContext.VALID_ERRORS);
        }

        String name = null, value = null;
        for (Annotation anno : paramAnnos) {
            if (PathVariable.class.equals(anno.annotationType())) {
                name = ((PathVariable) anno).value();
                if (name.isEmpty())
                    continue;
                String pathVariable = urlMapping
                        .getPathVariable(SessionContext.getContext().getString(SessionContext.REQUEST_URL), name);
                Object targetValue = JwUtils.convert(pathVariable, paramClaze);
                return targetValue;
            }
            if (RequestParam.class.equals(anno.annotationType())) {
                name = ((RequestParam) anno).value();
                if (name.isEmpty())
                    continue;
                value = SessionContext.getRequest().getParameter(name);
                SessionContext.getRequest().setAttribute(name, value);
                return value;
            }
            if (RequestHeader.class.equals(anno.annotationType())) {
                name = ((RequestHeader) anno).value();
                if (name.isEmpty())
                    continue;
                value = SessionContext.getRequest().getHeader(name);
                SessionContext.getRequest().setAttribute(name, value);
                return value;
            }
            if (CookieValue.class.equals(anno.annotationType())) {
                name = ((CookieValue) anno).value();
                if (name.isEmpty())
                    continue;
                Cookie[] cookies = SessionContext.getRequest().getCookies();
                if (!JwUtils.isEmpty(cookies)) {
                    for (Cookie cookie : cookies) {
                        if (name.equals(cookie.getName())) {
                            value = cookie.getValue();
                            SessionContext.getRequest().setAttribute(name, value);
                            return value;
                        }
                    }
                }
                return value;
            }
            if (ModelAttribute.class.equals(anno.annotationType())) {
                HttpServletRequest request = SessionContext.getRequest();
                Object dto = null;
                if (RequestMethod.GET.name().equals(request.getMethod())
                        || "application/x-www-form-urlencoded".equals(request.getContentType())) {
                    JSONObject json = new JSONObject();
                    json.putAll(request.getParameterMap());
                    String[] values = null;
                    Field field = null;
                    for (Entry<String, Object> entry : json.entrySet()) {
                        values = (String[]) entry.getValue();
                        if (values != null && values.length == 1) {
                            field = paramClaze.getDeclaredField(entry.getKey());
                            if (field != null) {
                                if (field.getType().isArray()) {
                                    // pass
                                } else if (Collection.class.isAssignableFrom(field.getType())) {
                                    entry.setValue(Arrays.asList(values));
                                } else {
                                    entry.setValue(values[0]);
                                }
                            } else {
                                entry.setValue(values[0]);
                            }
                        }
                    }
                    dto = json.toJavaObject(paramClaze);
                    // validation
                    if (JwUtils.contains(paramAnnos, Valid.class)) {
                        ValidErrors errors = ConstraintValidatorManager.validate(dto);
                        if (errors != null) {
                            SessionContext.getContext().set(SessionContext.VALID_ERRORS, errors);
                        }
                    }
                    return dto;
                } else if ("application/json".equals(request.getContentType())) {
                    String content = readText(request);
                    if (!StringUtils.isEmpty(content)) {
                        dto = JSON.parseObject(content).toJavaObject(paramClaze);
                    }
                    return dto;
                } else if (ServletFileUpload.isMultipartContent(request)) {
                    Map<String, Object> map = (Map<String, Object>) SessionContext.getContext()
                            .get(SessionContext.FILE_UPLOAD_PARAMETERS);
                    JSONObject json = new JSONObject();
                    json.putAll(map);
                    dto = json.toJavaObject(paramClaze);
                    return dto;
                } else {
                    LOGGER.error("Not meet the required condition for @ModelAttribute");
                }
            }
        }

        return null;
    }

    protected static String readText(HttpServletRequest request) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(request.getInputStream(), "UTF-8");
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[256];
            int read = 0;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
            LOGGER.error("Error raised when parse the post body to dto.", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.error("Error raised when release the reader.", e);
            }
        }
        return null;
    }

}
