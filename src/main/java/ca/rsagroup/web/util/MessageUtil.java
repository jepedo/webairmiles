package ca.rsagroup.web.util;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.webflow.execution.RequestContextHolder.getRequestContext;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;


/**
 * Convenient bean to create JSF info/warn/error messages from your flow.
 * Business exceptions can be mapped to user friendly messages inside the error(Exception e) method. 
 */
@Named
@Singleton
public class MessageUtil {

    private static final Logger log = Logger.getLogger(MessageUtil.class);

    @Inject
    private ResourcesUtil resourcesUtil;

    public static String toCssFriendly(Severity severity) {
        if (severity.equals(SEVERITY_INFO)) {
            return "info";
        } else if (severity.equals(SEVERITY_WARN)) {
            return "warn";
        } else if (severity.equals(SEVERITY_ERROR)) {
            return "error";
        } else if (severity.equals(SEVERITY_FATAL)) {
            return "fatal";
        }

        throw new IllegalStateException("Unexpected message severity: " + severity.toString());
    }

    // -- info

    public void info(String summaryKey, Object... args) {
//        addFacesMessageUsingKey(SEVERITY_INFO, summaryKey, args);
    }

    public void infoText(String summaryText) {
//        addFacesMessageUsingText(SEVERITY_INFO, summaryText);
    }

    /**
     * Use case for this method: Upon saveAndClose action, construct a FacesMessage, store it flow's conversationScope, when the
     * flows reach end-state add the message to facesContext using the addFacesesMessage method. This way we do not loose the
     * message upon redirections. Note: flash scope is not enough.
     */
    public FacesMessage newInfo(String summaryKey, Object... args) {
        return newFacesMessageUsingKey(SEVERITY_INFO, summaryKey, args);
    }

    // -- warning

    public void warning(String summaryKey, Object... args) {
//        addFacesMessageUsingKey(SEVERITY_WARN, summaryKey, args);
    }

    public void warningText(String summaryText) {
//        addFacesMessageUsingText(SEVERITY_WARN, summaryText);
    }

    public FacesMessage newWarning(String summaryKey, Object... args) {
        return newFacesMessageUsingKey(SEVERITY_WARN, summaryKey, args);
    }

    // -- error

    public void error(String summaryKey, Object... args) {
//        addFacesMessageUsingKey(SEVERITY_ERROR, summaryKey, args);
    }

    public void errorText(String summaryText) {
        addFacesMessageUsingText(SEVERITY_ERROR, summaryText);
    }

    public FacesMessage newError(String summaryKey, Object... args) {
        return newFacesMessageUsingKey(SEVERITY_ERROR, summaryKey, args);
    }

    /**
     * Map the passed exception to an error message.
     * Any potential exception (such as business exception) requiring a special message should be mapped here as well.
     */
    public void error(Exception e) {
        if (isCausedBy(e, DataIntegrityViolationException.class)) {
            error("error_unique_constraint_violation");
        } else if (isCausedBy(e, OptimisticLockingFailureException.class)) {
            error("error_concurrent_modification");
        } else if (isCausedBy(e, AccessDeniedException.class)) {
            // works only if the spring security filter is before the exception filter, 
            // that is if the exception filter handles the exception first.
            error("error_access_denied");
        } else {
            error("status_exception_ko", getMessage(e));
            log.error("====> !!ATTENTION!! DEVELOPERS should provide a less generic error message for the cause of this exception <====");
        }
    }

    // -- delayed messages support

    /**
     * It constructs a new info FacesMessage, store it flow's conversationScope but do not display it to the user. To trigger the message display, please invoke
     * displayDelayedMessages from your flow. This trick allows you to support multiple redirection (flash scope is not enough)
     */
    public void infoDelayed(String summaryKey, Object... args) {
        getDelayedMessages().add(newFacesMessageUsingKey(SEVERITY_INFO, summaryKey, args));
    }

    public void displayDelayedMessages() {
        List<FacesMessage> messages = getDelayedMessages();
        for (FacesMessage fm : messages) {
            addFacesMessage(fm);
        }
        messages.clear();
    }

    // -- impl details

    @SuppressWarnings("unchecked")
    private List<FacesMessage> getDelayedMessages() {
        List<FacesMessage> messages = (List<FacesMessage>) getRequestContext().getConversationScope().get("delayedMessages");
        if (messages == null) {
            messages = newArrayList();
            getRequestContext().getConversationScope().put("delayedMessages", messages);
        }
        return messages;
    }

    private void addFacesMessage(FacesMessage fm) {
        if (fm != null) {
            FacesContext.getCurrentInstance().addMessage(null, fm);
        }
    }

    private boolean isCausedBy(Throwable e, Class<?> cause) {
        Throwable current = e;
        while (current != null) {
            if (cause.isAssignableFrom(current.getClass())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private String getMessage(Throwable e) {
        String message = e.getClass().getCanonicalName();
        Throwable current = e;
        while (current != null) {
            if (current.getMessage() != null && !current.getMessage().trim().isEmpty()) {
                message = current.getMessage();
            }
            current = current.getCause();
        }
        return message;
    }

    private void addFacesMessageUsingKey(Severity severity, String summaryKey, Object[] args) {
//        addFacesMessage(newFacesMessageUsingKey(severity, summaryKey, args));
    }

    private void addFacesMessageUsingText(Severity severity, String text) {
//        addFacesMessage(newFacesMessageUsingText(severity, text));
    }

    private FacesMessage newFacesMessageUsingKey(Severity severity, String summaryKey, Object[] args) {
        return newFacesMessageUsingText(severity, resourcesUtil.getProperty(summaryKey, args));
    }

    private FacesMessage newFacesMessageUsingText(Severity severity, String text) {
        FacesMessage fm = new FacesMessage(text);
        fm.setSeverity(severity);
        return fm;
    }
    
	public static void addGlobalMessage(MessageContext messages, String summary) {
		
		messages.addMessage(new MessageBuilder()
		.error()
		.source(null)
		.defaultText(summary).build());

		FacesContext.getCurrentInstance().addMessage("errorCount", new FacesMessage(FacesMessage.SEVERITY_FATAL,  Integer.toString(FacesContext.getCurrentInstance().getMessageList().size()), "No. of Messages = " +  FacesContext.getCurrentInstance().getMessageList().size()));
	}
}