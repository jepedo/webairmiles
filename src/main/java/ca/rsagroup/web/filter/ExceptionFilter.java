package ca.rsagroup.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.rsagroup.commons.ConfigurationManager;


/**
 * In case of exception, this filter redirects the user to a view that depends on the type of request (ajax or not) and the kind of exception (flow exception or not).
 * In addition, this filter intercepts Ajax request corresponding to an expired session and redirects the user to the login page.
 * _HACK_ as we had to implement it ourselves.
 */
public final class ExceptionFilter implements Filter {
	   private static final Logger log = Logger.getLogger(ExceptionFilter.class);
	   @Inject
	   private ConfigurationManager configurationManager;

	   public void setConfigurationManager(ConfigurationManager configurationManager) {
	      this.configurationManager = configurationManager;
	   }

	   WebApplicationContext springContext;

	   @Override
	   public void init(FilterConfig config) throws ServletException {
	      springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
	   }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

    	String redirectionUrl = "/../";
		try {
			// chain...
			HttpServletRequest request = (HttpServletRequest) req;
			String reqUrl = request.getRequestURL().toString();
			if (!reqUrl.contains("javax.faces.resource")) {

//				String variant = configurationManager.getDefaultDomain();	
//		    	if(!configurationManager.isEnableDomains())
//		    		variant= configurationManager.getDefaultDomain();	
//		    	else {
//					try {
//						variant = reqUrl.substring(reqUrl.indexOf("//") + 2,
//								reqUrl.indexOf("."));
//					} catch (Exception e) {
//						variant = configurationManager.getDefaultDomain();
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		    	}
				String lang = request.getParameter("lang");

				if (lang != null) {
					LocaleContextHolder.setLocale(new Locale(lang, "CA", ""));
				} else {
					LocaleContextHolder.setLocale(new Locale(LocaleContextHolder.getLocale()
							.getLanguage(), "CA", ""));
				}

	            if (!isAjax(request) && request.getRequestedSessionId() != null
	                    && (request.getSession(false) == null || !request.isRequestedSessionIdValid()) && lang == null) {
	                 // Session is invalid or has expired - create a new one and redirect to the root
	                 HttpSession session = request.getSession();
	                 log.warn(this.getClass().toString() + " : doFilter() - Session timed out - redirecting to " + redirectionUrl);
	                 // KnockoutException.createKnockOutException(
	                 //	"kickout.timeout.error", null, null);
	                 HttpServletResponse response = (HttpServletResponse)resp;
	                 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	                 response.setHeader("Pragma", "no-cache");
	                 response.setDateHeader("Expires", 0);
	                 response.sendRedirect(request.getContextPath() + redirectionUrl);
	              }
	              else if (isAjax(request) && !request.isRequestedSessionIdValid()) {
	                 log.warn(this.getClass().toString() + " : doFilter() - Session expiration during ajax request, partial redirect to " + redirectionUrl);
	                 HttpServletResponse response = (HttpServletResponse)resp;
	                 response.setContentType("text/xml");
	                 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	                 response.setHeader("Pragma", "no-cache");
	                 response.setDateHeader("Expires", 0);
	                 response.getWriter().print(xmlPartialRedirectToPage(request, redirectionUrl));
	                 response.flushBuffer();
	                 return;
	              }

			}
			
			chain.doFilter(req, resp);  
		} catch (Exception e) {
			// redirect to error page
			HttpServletRequest request = (HttpServletRequest) req;
			log.error("EXCEPTION unique id: " + e.hashCode(), e);
			log.error("EXCEPTION stacktrace: " + e.getMessage(), e);
			e.printStackTrace();
			HttpServletResponse response = (HttpServletResponse) resp;
	         if (response != null && request != null) {
	             response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	             response.setHeader("Pragma", "no-cache");
	             response.setDateHeader("Expires", 0);
	             response.sendRedirect(request.getContextPath() + redirectionUrl);
	             return;
	          }
		}
    }	

    private String xmlPartialRedirectToPage(HttpServletRequest request, String page) {
        return "<?xml version='1.0' encoding='UTF-8'?>" //
              + "<partial-response>" //
              + "<redirect url=\"" + request.getContextPath() + page + "\">" + "</redirect>" + "</partial-response>";
     }

     private boolean isAjax(HttpServletRequest request) {
        // return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        return "partial/ajax".equals(request.getHeader("Faces-Request"));
     }

     @Override
     public void destroy() {
     }
}