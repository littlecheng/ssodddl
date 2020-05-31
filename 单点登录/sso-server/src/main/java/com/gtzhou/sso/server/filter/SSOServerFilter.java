package com.gtzhou.sso.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gtzhou.sso.server.cache.JVMCache;
import org.apache.log4j.Logger;


public class SSOServerFilter implements Filter{

	Logger logger = Logger.getLogger(SSOServerFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		 	HttpServletRequest request = (HttpServletRequest) servletRequest;
	        HttpServletResponse response = (HttpServletResponse) servletResponse;
	        String service = request.getParameter("service");
	        String ticket = request.getParameter("ticket");
	        Cookie[] cookies = request.getCookies();
	        String username = "";
			logger.info("service="+service+",ticket="+ticket);
			logger.info("cookies="+cookies);
	        if (null != cookies) {
				logger.info("cookies="+cookies.length);
	            for (Cookie cookie : cookies) {
	                if ("sso".equals(cookie.getName())) {
	                    username = cookie.getValue();
	                    break;
	                }
	            }
	        }
		   logger.info("service="+service+",ticket="+ticket);
	        if (null == service && null != ticket) {
				logger.info("do doFilter");
	            filterChain.doFilter(servletRequest, servletResponse);
	            return;
	        }

			logger.info("username="+username);
	        if (null != username && !"".equals(username)) {
	            long time = System.currentTimeMillis();
	            String timeString = username + time;
	            JVMCache.TICKET_AND_NAME.put(timeString, username);
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
				logger.info(" doFilter");
	            filterChain.doFilter(servletRequest, servletResponse);
	        }
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
