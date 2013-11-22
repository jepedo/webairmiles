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


@Component
public class AirmilesRequestValidator {
	@Inject
	private LookupManager lookupManager;		

	public void validateStart(AirmilesRequest airmiles, ValidationContext context) {		
	        MessageContext messages = context.getMessageContext();
	        if(airmiles==null || airmiles.getAirmilesNumber()==null || !modulus11(airmiles.getAirmilesNumber())) {
		         messages.addMessage(new MessageBuilder().error()
						.source(null).defaultText(lookupManager.getBundle("airmiles.err.E4")).build());
	        }	 
	        
	        if(airmiles!=null && airmiles.getPolicyDate()!=null && new Date(airmiles.getPolicyDate()).after(new Date(System.currentTimeMillis()))) {
		         messages.addMessage(new MessageBuilder().error()
						.source(null).defaultText(lookupManager.getBundle("airmiles.err.E3")).build());
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
