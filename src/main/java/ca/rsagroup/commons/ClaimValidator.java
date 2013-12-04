package ca.rsagroup.commons;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;


import ca.rsagroup.web.util.DatabaseDrivenMessageSource;


@Component
public class ClaimValidator {
	@Inject
	private ConfigurationManager configurationManager;	

	protected DatabaseDrivenMessageSource configurationSource;

}
