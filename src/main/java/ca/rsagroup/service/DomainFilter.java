package ca.rsagroup.service;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.rsagroup.commons.ConfigurationManager;
import ca.rsagroup.service.LookupManager;


/**
 * In case of exception, this filter redirects the user to a view that depends on the type of request (ajax or not) and the kind of exception (flow exception or not).
 * In addition, this filter intercepts Ajax request corresponding to an expired session and redirects the user to the login page.
 * _HACK_ as we had to implement it ourselves.
 */

@Component
public final class DomainFilter implements Filter {
    private static final Logger log = Logger.getLogger(DomainFilter.class);
    
	@Inject
	protected LookupManager lookupManager;

	WebApplicationContext springContext;
	
    @Override
    public void init(FilterConfig config) throws ServletException {
    	springContext =  WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());    	
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

    	((HttpServletRequest) req).getSession();
    	
    	String redirectionUrl = "/../";
		try {
			// chain...
			HttpServletRequest request = (HttpServletRequest) req;
			String reqUrl = request.getRequestURL().toString();
			// check if urq is in the domains list
			if(lookupManager ==null)
				lookupManager = (LookupManager) springContext.getBean("lookupManager");
			String[] domains = lookupManager.getBundle("valid.domains").split(",");
			String mydomain= reqUrl.substring(reqUrl.indexOf("//") + 2);
			int sc = mydomain.indexOf(":");
			int ss = mydomain.indexOf("/");
			mydomain = (sc<ss && sc>0)?mydomain.substring(0,sc):mydomain.substring(0,ss);
			log.info("myURL - "+mydomain);
			boolean validDomain = false;
			for (int i = 0; i < domains.length; i++) {
				if(domains[i].equalsIgnoreCase(mydomain)) {
					validDomain = true;
					break;
				}
			}
			
			if(!validDomain) {
				HttpServletResponse response = (HttpServletResponse) resp;
				response.sendError(403);
				
			}
			else 
				chain.doFilter(req, resp);  
			} catch (Exception e) {
				// redirect to error page
				HttpServletRequest request = (HttpServletRequest) req;
				log.error("EXCEPTION unique id: " + e.hashCode(), e);
				log.error("EXCEPTION stacktrace: " + e.getMessage(), e);
				e.printStackTrace();
				HttpServletResponse response = (HttpServletResponse) resp;
				response.sendRedirect(request.getContextPath() + redirectionUrl);
			}
		
    }	

    private String xmlPartialRedirectToPage(HttpServletRequest request, String page) {
        return "<?xml version='1.0' encoding='UTF-8'?>" //
                + "<partial-response>" //
                + "<redirect url=\"" + request.getContextPath() + request.getServletPath() + page + "\"/>" //
                + "</partial-response>";
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    @Override
    public void destroy() {
    }
}