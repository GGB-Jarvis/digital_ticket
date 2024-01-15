package com.num.digital_ticket.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebUtils {

    public static String rederString(HttpServletResponse response, Object content) {
        PrintWriter writer = null;
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            writer.print(content);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            writer.flush();
            writer.close();
        }
        return null;
    }
}
