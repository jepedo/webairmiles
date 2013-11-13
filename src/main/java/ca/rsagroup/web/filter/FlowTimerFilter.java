package ca.rsagroup.web.filter;

import static java.lang.String.format;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.View;

/**
 * Log current flow transition, state, etc.
 * You must also configure this class as an Execution Listener in Spring Web Flow conf.
 */
public final class FlowTimerFilter extends FlowExecutionListenerAdapter implements Filter {
    private static final Logger log = Logger.getLogger(FlowTimerFilter.class);
    private static final ThreadLocal<StringBuilder> message = new ThreadLocal<StringBuilder>();
    private static final String SEP = "\n\t";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // reset context.
        message.set(new StringBuilder(""));

        // set up log context for this thread so these information can be used by log4j
//        String username = UserContext.getUsername();
//        LogContext.setLogin(username != null ? username : "no username");
//        LogContext.setSessionId(httpRequest.getSession().getId());

        // TODO: move temp hack
        // (partial ajax response use it. Required for IE8.)
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }

        // timer
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - start;

        log.info("flow only duration: " + format("%5s", duration) + " ms - " + httpRequest.getRequestURI() + message.get());

        // reset context
//        LogContext.resetLogContext();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void viewRendered(RequestContext context, View view, StateDefinition viewState) {
        FlowDefinition activeFlow = context.getActiveFlow();
        StateDefinition currentState = context.getCurrentState();
        TransitionDefinition currentTransition = context.getCurrentTransition();
        message.get() //
                .append(SEP + "Active flow: ").append(activeFlow == null ? "N/A" : activeFlow.getId() + " (" + context.getFlowExecutionUrl() + ")") //
                .append(SEP + "Current State: ").append(currentState == null ? "N/A" : currentState.getId()) //
                .append(SEP + "Current Transition: ").append(currentTransition == null ? "N/A" : currentTransition.getId());
    }

    @Override
    public void eventSignaled(RequestContext context, Event event) {
        message.get().append(SEP + "eventSignaled: " + event.getId());
    }

    @Override
    public void transitionExecuting(RequestContext context, TransitionDefinition transition) {
        message.get().append(SEP + "transitionExecuting: on=" + transition.getId() + " to=" + transition.getTargetStateId());
    }

    @Override
    public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        req.getSession().setAttribute("lastException", exception);
        req.getSession().setAttribute("lastExceptionUniqueId", exception.hashCode());

        log.error("EXCEPTION unique id: " + exception.hashCode(), exception); // todo: better

        StringBuilder sb = message.get().append(SEP + "Exception Thrown: " + exception);
        if (exception.getCause() != null) {
            sb.append(SEP + "Exception Cause: " + exception.getCause());
        }
    }
}