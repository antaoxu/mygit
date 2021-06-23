package com.xat;

import java.io.IOException;

/**
 * @author xuantao
 */
public  abstract class MyHttpServlet {
    //定义静态常量
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    //定义抽象方法供子类重写
    public abstract void doGet(MyRequest request,MyResponse response) throws IOException;
    public abstract void doPost(MyRequest request,MyResponse response) throws IOException;

    /**
     * 根据请求方式来判断执行哪种方法
     * @param request
     * @param response
     */
    public void service(MyRequest request,MyResponse response) throws IOException {
        if(METHOD_GET.equals(request.getRequestMethod())){
            doGet(request,response);
        }else if(METHOD_POST.equals(request.getRequestMethod())){
            doPost(request,response);
        }
    }
}
