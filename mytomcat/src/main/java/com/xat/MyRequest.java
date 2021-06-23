package com.xat;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 淡漠
 */
public class MyRequest {
    //请求方法
    private String requestMethod;
    //请求地址
    private String requestUrl;

    public MyRequest(InputStream inputStream) throws IOException {
        //定义缓冲区
        byte[] buffer = new byte[1024];
        //定义读取长度
        int lenth = 0;
        //定义读取数据字符串
        String str = "";
        if((lenth = inputStream.read(buffer))>0){
            str = new String(buffer,0,lenth);
            System.out.println("请求数据为：\n"+str);
        }
        //GET / HTTP/1.0
        String data = str.split("\n")[0];
        String[] params = data.split(" ");
        this.requestMethod = params[0];
        this.requestUrl = params[1];

    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
