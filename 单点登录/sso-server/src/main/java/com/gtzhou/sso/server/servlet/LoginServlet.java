package com.gtzhou.sso.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gtzhou.sso.server.cache.JVMCache;
import com.gtzhou.sso.server.filter.SSOServerFilter;
import org.apache.log4j.Logger;


public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = -3170191388656385924L;

    Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String service = request.getParameter("service");
        logger.info("username="+username+",password="+password+",service="+service);
        if ("cloud".equals(username) && "cloud".equals(password)) {
            Cookie cookie = new Cookie("sso", username);
            cookie.setPath("/");
            response.addCookie(cookie);

            long time = System.currentTimeMillis();
            String timeString = username + time;
            JVMCache.TICKET_AND_NAME.put(timeString, username);

            logger.info("service="+service);
            if (null != service) {
                StringBuilder url = new StringBuilder();
                url.append(service);
                if (0 <= service.indexOf("?")) {
                    url.append("&");
                } else {
                    url.append("?");
                }
                url.append("ticket=").append(timeString);
                logger.info("url="+url);
                response.sendRedirect(url.toString());
            } else {
                response.sendRedirect("/sso/index.jsp");
            }
        } else {
            response.sendRedirect("/sso/index.jsp?service=" + service);
        }
    }

}