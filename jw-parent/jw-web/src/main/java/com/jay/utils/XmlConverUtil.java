package com.jay.utils;

import net.sf.json.xml.XMLSerializer;

public class XmlConverUtil {
    public static String xmltoJson(String xml) {  
        XMLSerializer xmlSerializer = new XMLSerializer();  
        return xmlSerializer.read(xml).toString();  
    }
}
