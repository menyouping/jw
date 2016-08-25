package com.jw.util;

import javax.activation.MimetypesFileTypeMap;

public class MimeUtils {

    private static final MimetypesFileTypeMap MIMETYPE_MAP = new MimetypesFileTypeMap();
    
    public static String getMimeType(String extension) {
        if ("css".equals(extension))
            return "text/css";
        return MIMETYPE_MAP.getContentType("x." + extension);
    }
}
