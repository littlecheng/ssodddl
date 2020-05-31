package com.gtzhou.sso.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gtzhou.sso.server.cache.JVMCache;


public class TicketServlet extends HttpServlet {
    private static final long serialVersionUID = 5964206637772848290L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ticket = request.getParameter("ticket");
        String username = JVMCache.TICKET_AND_NAME.get(ticket);
        JVMCache.TICKET_AND_NAME.remove(ticket);
        PrintWriter writer = response.getWriter();
        writer.write(username);
    }

}
