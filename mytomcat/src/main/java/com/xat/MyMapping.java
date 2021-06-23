package com.xat;

import java.util.HashMap;

/**
 * @author xuantao
 */
public class MyMapping {
    public static HashMap<String, String> mapping = new HashMap<>();

    static {
        mapping.put("/tomcat","com.xat.MyServlet");
    }

    public HashMap<String, String> getMapping(){
        return mapping;
    }

}
