package com.gtzhou.sso.server.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		    logger.warn("time="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		 	HttpServletRequest request = (HttpServletRequest) servletRequest;
	        HttpServletResponse response = (HttpServletResponse) servletResponse;
	        String service = request.getParameter("service");
	        String ticket = request.getParameter("ticket");
	        Cookie[] cookies = request.getCookies();
	        String username = "";
			logger.warn("service="+service+",ticket="+ticket+",cookies="+cookies);
	        if (null != cookies) {
				logger.info("cookies="+cookies.length);
	            for (Cookie cookie : cookies) {
	                if ("sso".equals(cookie.getName())) {
	                    username = cookie.getValue();
	                    break;
	                }
	            }
	        }
		   logger.warn("service="+service+",ticket="+ticket+",username="+username);
	        if (null == service && null != ticket) {
				logger.warn("SSOServerFilter before doFilter");
	            filterChain.doFilter(servletRequest, servletResponse);
				logger.warn("SSOServerFilter after doFilter");
	            return;
	        }

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
	            url.append("ticket=").append(timeString).append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
				logger.warn("send url="+url);
	            response.sendRedirect(url.toString());
				logger.warn("after url="+url);
	        } else {
				logger.warn(" doFilter");
	            filterChain.doFilter(servletRequest, servletResponse);
				logger.warn(" doFilter");
	        }
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
