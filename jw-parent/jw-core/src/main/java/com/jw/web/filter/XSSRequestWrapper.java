package com.jw.web.filter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.jw.util.JwUtils;
 
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    
    private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // lonely script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // onload(...)=...
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
 
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null || values.length == 0) {
            return values;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            if(JwUtils.isEditorField(parameter)){
                encodedValues[i] = stripXSSExceptEditor(values[i]);
            }else{
                encodedValues[i] = stripXSS(values[i]);
            }
        }
        return encodedValues;
    }
 
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if(JwUtils.isEditorField(parameter)){
            return stripXSSExceptEditor(value);
        }
        return stripXSS(value);
    }
 
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }
 
    /**
     * 针对编辑器的
     * @param value
     * @return
     */
    private String stripXSSExceptEditor(String value){
        if (value != null) {
            // Avoid null characters
            value = value.replaceAll("\0", "");
            // Remove all sections that match a pattern
            int len = patterns.length;
            for(int i=0;i<len;i++){
                if(i==1 || i==2){
                    continue;
                }else{
                    value = patterns[i].matcher(value).replaceAll("");
                }
            }
        }
        return value;
    }
    
    /**
     * 通用的
     * @param value
     * @return
     */
    private String stripXSS(String value) {
        if (value != null) {
            // Avoid null characters
            value = value.replaceAll("\0", "");
            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
