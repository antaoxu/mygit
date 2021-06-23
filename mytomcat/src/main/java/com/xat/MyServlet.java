package com.xat;

import java.io.IOException;

/**
 * @author xuantao
 */
public class MyServlet extends MyHttpServlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) throws IOException {
        response.write("GET tomcat");
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) throws IOException {
        response.write("POST tomcat");
    }
}
