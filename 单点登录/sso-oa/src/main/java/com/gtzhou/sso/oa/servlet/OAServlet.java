package com.gtzhou.sso.oa.servlet;

import com.gtzhou.sso.oa.filter.SSOClientFilter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAServlet extends HttpServlet {
    private static final long serialVersionUID = 3615122544373006252L;

    Logger logger = Logger.getLogger(OAServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.warn("OAServlet doGet()");
        request.getSession().setAttribute("time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) );
        request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
        logger.warn("OAServlet 跳转后");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
}
