package ca.rsagroup.web.filter;

import java.io.IOException;
import java.util.Locale;

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


/**
 * In case of exception, this filter redirects the user to a view that depends on the type of request (ajax or not) and the kind of exception (flow exception or not).
 * In addition, this filter intercepts Ajax request corresponding to an expired session and redirects the user to the login page.
 * _HACK_ as we had to implement it ourselves.
 */
public final class ExceptionFilter implements Filter {
    private static final Logger log = Logger.getLogger(ExceptionFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

    	String redirectionUrl = "/../";
		try {
			// chain...
			HttpServletRequest request = (HttpServletRequest) req;
			String reqUrl = request.getRequestURL().toString();
			if (!reqUrl.contains("javax.faces.resource")) {

				String variant = "d";
				try {
					variant = reqUrl.substring(reqUrl.indexOf("//") + 2,
							reqUrl.indexOf("."));
				} catch (Exception e) {
					variant = "www";
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String lang = request.getParameter("lang");

				if (lang != null) {
					LocaleContextHolder.setLocale(new Locale(lang, "CA", variant));
				} else {
					LocaleContextHolder.setLocale(new Locale(LocaleContextHolder.getLocale()
							.getLanguage(), "CA", variant));
				}

				if (!isAjax(request)
						&& request.getRequestedSessionId() != null
						&& (request.getSession(false) == null || !request
								.isRequestedSessionIdValid()) && lang == null) {
					// Session is expired

					((HttpServletResponse) resp).sendRedirect(request
							.getContextPath() + redirectionUrl);
				}
				// else if (isAjax(request) &&
				// !request.isRequestedSessionIdValid()) {
				// log.warn("Session expiration during ajax request, partial redirect to login page");
				// HttpServletResponse response = (HttpServletResponse) resp;
				// response.getWriter().print(xmlPartialRedirectToPage(request,
				// redirectionUrl));
				// response.flushBuffer();
				// }

			}
			
			chain.doFilter(req, resp);  
		} catch (Exception e) {
			// redirect to error page
			HttpServletRequest request = (HttpServletRequest) req;
			request.getSession().setAttribute("lastException", e);
			request.getSession().setAttribute("lastExceptionUniqueId",
					e.hashCode());

			log.error("EXCEPTION unique id: " + e.hashCode(), e);
			log.error("EXCEPTION stacktrace: " + e.getMessage(), e);
			e.printStackTrace();
			HttpServletResponse response = (HttpServletResponse) resp;

			response.sendRedirect(request.getContextPath() + redirectionUrl);
		}

		
    	/**	
    	String redirectionUrl = "/../";
        try {
            // chain...
            HttpServletRequest request = (HttpServletRequest) req;
            String fi = request.getParameter("fi");
            if (!isAjax(request) && request.getRequestedSessionId() != null && request.getSession(false) == null
            		&& fi==null) {
                // Session is expired
            	
            	 ( (HttpServletResponse)resp).sendRedirect(request.getContextPath() + redirectionUrl);
            }
            else if (isAjax(request) && !request.isRequestedSessionIdValid()) {
                log.warn("Session expiration during ajax request, partial redirect to login page");
                HttpServletResponse response = (HttpServletResponse) resp;
                response.getWriter().print(xmlPartialRedirectToPage(request, redirectionUrl));
                response.flushBuffer();
            } 
            else {
                chain.doFilter(req, resp);
            }

        } catch (Exception e) {
            // redirect to error page
            HttpServletRequest request = (HttpServletRequest) req;
            request.getSession().setAttribute("lastException", e);
            request.getSession().setAttribute("lastExceptionUniqueId", e.hashCode());

            log.error("EXCEPTION unique id: " + e.hashCode(), e);
            log.error("EXCEPTION stacktrace: " + e.getMessage(), e);
            e.printStackTrace();
            HttpServletResponse response = (HttpServletResponse) resp;

            response.sendRedirect(request.getContextPath()  + redirectionUrl);
        }
        */
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