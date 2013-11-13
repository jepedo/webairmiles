/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-primefaces:src/main/java/component/MessagesHelper.p.vm.java
 */
package ca.rsagroup.web.component;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import ca.rsagroup.web.util.ResourcesUtil;
import ca.rsagroup.web.util.MessageUtil;

/**
 * Helper used from the appcc:messages composite component.
 */
@Named
@Singleton
public class MessagesHelper {

    @Inject
    ResourcesUtil resourcesUtil;

    public String getMaxSeverity() {
        Severity maxSeverity = null;
        Iterator<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessages();

        while (msgs.hasNext()) {
            if (maxSeverity == null) {
                maxSeverity = FacesMessage.SEVERITY_INFO;
            }

            Severity currentSeverity = msgs.next().getSeverity();
            if (currentSeverity.getOrdinal() > maxSeverity.getOrdinal()) {
                maxSeverity = currentSeverity;
            }
        }

        if (maxSeverity != null) {
            return MessageUtil.toCssFriendly(maxSeverity);
        }

        return "";
    }

    public Message[] getGlobalMessages() {
        List<Message> res = newArrayList();
        Iterator<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessages(null);
        while (msgs.hasNext()) {
            res.add(new Message(null, msgs.next()));
        }
        return toArray(res, Message.class);
    }

    public Message[] getNonGlobalMessages() {
        List<Message> res = newArrayList();

        Iterator<String> ids = FacesContext.getCurrentInstance().getClientIdsWithMessages();
        while (ids.hasNext()) {
            String clientId = ids.next();
            if (clientId != null && !clientId.equals("null")) { /* the 'null' string is pretty disturbing */
                Iterator<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessages(clientId);
                while (msgs.hasNext()) {
                    res.add(new Message(clientId, msgs.next()));
                }
            }
        }

        return toArray(res, Message.class);
    }

    public boolean hasGlobalMessages() {
        return FacesContext.getCurrentInstance().getMessages(null).hasNext();
    }

    public boolean hasNonGlobalMessages() {
        Iterator<String> ids = FacesContext.getCurrentInstance().getClientIdsWithMessages();
        while (ids.hasNext()) {
            String clientId = ids.next();
            if (clientId != null && !clientId.equals("null")) { /* the 'null' string is pretty disturbing */
                return true;
            }
        }
        return false;
    }

    public boolean hasNoMessages() {
        return !(hasGlobalMessages() || hasNonGlobalMessages());
    }

    public boolean hasOnlyGlobalMessages() {
        return hasGlobalMessages() && !hasNonGlobalMessages();
    }

    public String getNonGlobalMessagesIntro() {
        return resourcesUtil.getPluralableProperty("form_error_status", nonGlobalMessagesCount());
    }

    public int nonGlobalMessagesCount() {
        int count = 0;
        Iterator<String> ids = FacesContext.getCurrentInstance().getClientIdsWithMessages();
        while (ids.hasNext()) {
            String clientId = ids.next();
            if (clientId != null && !clientId.equals("null")) { /* the 'null' string is pretty disturbing */
                count += FacesContext.getCurrentInstance().getMessageList(clientId).size();
            }
        }
        return count;
    }
}