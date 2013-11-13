package ca.rsagroup.web.interceptor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerInterceptor;

import ca.rsagroup.web.flow.FlowsMenuHandler;
import ca.rsagroup.web.util.LocaleUtil;

/**
 * Expose the 'flowsMenuHandler' beans to non webflow views, that is to {@link org.springframework.faces.mvc.JsfView} such as
 * homepage, error page etc. Otherwise this bean is not visible from the view and we cannot display the menu listing the active flows.
 * <p>
 * Note: It should not intercept webflow pages, as webflow is handling all the resource bindings itself.
 */
@Named
@Singleton
public class ExposeBeansToNonWebflowViewsInterceptor implements HandlerInterceptor {

    @Inject
    private FlowsMenuHandler flowsMenuHandler;
    @Inject
    private LocaleUtil localeUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        request.setAttribute("flowsMenuHandler", flowsMenuHandler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}