package ca.rsagroup.commons;

import java.util.Date;
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

import ca.rsagroup.airmiles.AirmilesRequest;
import ca.rsagroup.service.LookupManager;
import ca.rsagroup.web.util.MessageUtil;


@Component
public class AirmilesRequestValidator {
	@Inject
	private LookupManager lookupManager;		
	@Inject
	private ConfigurationManager configurationManager;

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public void validateStart(AirmilesRequest airmiles, ValidationContext context) {		
	        MessageContext messages = context.getMessageContext();
	        messages.clearMessages();
	        if(airmiles==null || airmiles.getAirmilesNumber()==null || !modulus11(airmiles.getAirmilesNumber())) {
		         messages.addMessage(new MessageBuilder().error()
						.source(null).defaultText(lookupManager.getBundle("airmiles.err.E4")).build());
//		         MessageUtil.addGlobalMessage(context.getMessageContext(),lookupManager.getBundle("airmiles.err.E4"));
	        }	 
	        
	        if(airmiles!=null && airmiles.getPolicyDate()!=null && new Date(airmiles.getPolicyDate()).after(new Date(System.currentTimeMillis()))) {
		         messages.addMessage(new MessageBuilder().error()
						.source(null).defaultText(lookupManager.getBundle("airmiles.err.E3")).build());
//		         MessageUtil.addGlobalMessage(context.getMessageContext(),lookupManager.getBundle("airmiles.err.E3"));
	        } 
	        if(airmiles==null || airmiles.getEmail()==null || !airmiles.getEmail().matches(EMAIL_PATTERN)) {
		         messages.addMessage(new MessageBuilder().error()
						.source(null).defaultText(lookupManager.getBundle("airmiles.email.label") +" : "+lookupManager.getBundle("airmiles.err.E5")).build());
//		         MessageUtil.addGlobalMessage(context.getMessageContext(),lookupManager.getBundle("airmiles.email.label") +" : "+lookupManager.getBundle("airmiles.err.E5"));
	        }	
	}
	
	private boolean modulus11(String airm) {
		boolean isValid = false;
        
        if (airm.length()!=11)
               return isValid;

        int[] mult = {6,5,4,3,8,7,6,5,4,3};
        int sum = 0;
        for (int i=0; i < 10; i++) {
               sum += Integer.parseInt(airm.substring(i,i+1))* mult[i];
        }

        int chkdigit = sum % 11;

        if (chkdigit < 2)
           chkdigit = 0;
        else        
           	chkdigit = 11 - chkdigit;

        if (chkdigit == Integer.parseInt(airm.substring(10))) {
               isValid = true;
        }

        return isValid; 	
	}
}
