package com.xat;

import java.io.IOException;

/**
 * @author xuantao
 */
public interface MyHttpServlet {
    //定义静态常量
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    //定义抽象方法供子类重写

     void doGet(MyRequest request,MyResponse response) throws IOException;
     void doPost(MyRequest request,MyResponse response) throws IOException;

    /**
     * 根据请求方式来判断执行哪种方法
     * @param request
     * @param response
     */
     void service(MyRequest request,MyResponse response) throws IOException;
}
