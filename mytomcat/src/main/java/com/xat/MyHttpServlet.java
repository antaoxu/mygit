package com.xat;

import java.io.IOException;

/**
 * @author xuantao
 */
public interface MyHttpServlet {
    //定义静态常量
    String METHOD_GET = "GET";
    String METHOD_POST = "POST";

    //定义抽象方法供子类重写

    void doGet(MyRequest request, MyResponse response) throws IOException;

    void doPost(MyRequest request, MyResponse response) throws IOException;

    /**
     * 根据请求方式来判断执行哪种方法
     *
     * @param request
     * @param response
     * @throws
     */
    void service(MyRequest request, MyResponse response) throws IOException;
}
