package com.xat;

import sun.net.www.http.HttpClient;

import java.io.IOException;

/**
 * @author xuantao
 */
public class MyServlet implements MyHttpServlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) throws IOException {
        response.write("GET tomcat");
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) throws IOException {
        response.write("POST tomcat");
    }

    /**
     * 根据请求方式来判断执行哪种方法
     *
     * @param request
     * @param response
     */
    @Override
    public void service(MyRequest request, MyResponse response) throws IOException {
        if (METHOD_GET.equals(request.getRequestMethod())) {
            doGet(request, response);
        } else if (METHOD_POST.equals(request.getRequestMethod())) {
            doPost(request, response);
        }
    }

}
