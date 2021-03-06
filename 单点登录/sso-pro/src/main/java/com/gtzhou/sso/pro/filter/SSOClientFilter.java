package com.gtzhou.sso.pro.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class SSOClientFilter implements Filter{
	Logger logger = Logger.getLogger(SSOClientFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		 	HttpServletRequest request = (HttpServletRequest) servletRequest;
	        HttpServletResponse response = (HttpServletResponse) servletResponse;
	        HttpSession session = request.getSession();
	        String username = (String) session.getAttribute("username");
	        String ticket = request.getParameter("ticket");
	        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
			logger.info("username="+username+" ticket="+ticket +" url"+ URLDecoder.decode(url,"UTF-8"));
	        if (null == username) {
	            if (null != ticket && !"".equals(ticket)) {
	                PostMethod postMethod = new PostMethod("http://localhost:8081/sso/ticket");
	                postMethod.addParameter("ticket", ticket);
	                HttpClient httpClient = new HttpClient();
	                try {
	                    httpClient.executeMethod(postMethod);
	                    username = postMethod.getResponseBodyAsString();
	                    postMethod.releaseConnection();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
					logger.info("username="+username);
	                if (null != username && !"".equals(username)) {
	                    session.setAttribute("username", username);
						logger.info("doFilter...");
	                    filterChain.doFilter(request, response);
	                } else {
						logger.info("url="+URLDecoder.decode(url,"UTF-8"));
	                    response.sendRedirect("http://localhost:8081/sso/index.jsp?service=" + url);
	                }
	            } else {
					logger.info("url="+URLDecoder.decode(url,"UTF-8"));
	                response.sendRedirect("http://localhost:8081/sso/index.jsp?service=" + url);
	            }
	        } else {
				logger.info("doFilter");
	            filterChain.doFilter(request, response);
	        }
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
