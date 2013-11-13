package ca.rsagroup.web.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.binding.message.MessageContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.webflow.execution.RequestContext;

import ca.rsagroup.commons.ConfigurationManager;
import ca.rsagroup.service.LookupManager;
import ca.rsagroup.web.util.DatabaseDrivenMessageSource;

/**
 * Thin controller layer allowing you to do business validation and other conditional
 * checks that are easier to implement in Java than in webflow's xml syntax.
 */
@Named

public class AirmilesController  {
	
   ;
	@Inject
	protected DatabaseDrivenMessageSource configurationSource;
	

	protected LookupManager lookupManager;
	
	@Inject
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	
	@Inject
	private ConfigurationManager configurationManager;
	
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
   
    public String formatDate(java.sql.Timestamp date) {
    	if(date==null)
    		return  "";
    	
    	try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm") ;
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
    
    public String formatDate(java.util.Date date) {
    	if(date==null)
    		return  "";
    	
    	try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm") ;
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
    
    
    public String formatDateShort(java.sql.Timestamp date) {
    	if(date==null)
    		return  "";
    	
    	try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd") ;
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
    
    public String formatDateShort(java.util.Date date) {
    	if(date==null)
    		return  "";
    	
    	try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd") ;
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
    
    public String formatBoolean(Boolean b){
    	if(b==null)
    		return "";
    	else if(b)
    		return "Yes";
    	else
    		return "No";
    }
   
    
    private static int determineDifferenceInDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        return (int) (diffInMillis / (24* 1000 * 60 * 60));
    }
        
    
    public void setLanguage(String language, Integer domainId) {
    	String variant = domainId!=null?domainId.toString():null;
    	try {
			LocaleContextHolder.setLocale(new Locale(language,"CA",variant), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LocaleContextHolder.setLocale(Locale.ENGLISH);
//			e.printStackTrace();
		} 
    	HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse res = (HttpServletResponse)  FacesContext.getCurrentInstance().getExternalContext().getResponse();

        try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
			localeResolver.setLocale(req, res, new Locale(language,"CA",variant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
       
//    	FacesContext.getCurrentInstance().getExternalContext()CurrentInstance().getViewRoot().setLocale( new Locale(language,"CA",variant));
    }
    
    public String setLanguageReverse(String language, Integer domainId, Integer fiId) {
    	String variant = domainId!=null?domainId.toString():null;


    	HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse res = (HttpServletResponse)  FacesContext.getCurrentInstance().getExternalContext().getResponse();

        try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
			localeResolver.setLocale(req, res, new Locale(language,"CA",variant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
    	if(language!=null && language.equalsIgnoreCase("en"))
    		language = "fr";
    	else
    		language = "en";
        return req.getRequestURI()+"?fi="+fiId+"&lang="+language;
    }
    
    public void processRequest(Object airmilesRequest) {  
    	String a = "ss";
		 /*if(aClaim==null)
			 return ;
		 HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
         LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
         Locale myLocale = localeResolver.resolveLocale(req);
	        
		 ObjectMapper mapper = new ObjectMapper();
		 MailData request = new MailData();
		 request.setReplyTo(configurationManager.getMailSenderReplyTo());
		 request.setFrom(configurationManager.getMailSenderFrom());
		 request.setContentType("text/html; charset=UTF-8");
		 
		 if(EMAIL_THANKYOU.equalsIgnoreCase(emailType)) {
			 request.setTo(aClaim.getEmail());	
			 
			 if(aClaim.getPreferredLanguage().toLowerCase().startsWith("fr"))
				 myLocale =  new Locale("fr","CA",myLocale.getVariant());
			 else
				 myLocale =  new Locale("en","CA",myLocale.getVariant());
			 
			 String body = configurationSource.getText("email.thankyou.body.start", myLocale);
			 body+=getDocumentsBodyByLossType(aClaim,"email.thankyou.body",myLocale);
			 body += configurationSource.getText("email.thankyou.body.end", myLocale);
			 
			 request.setBody(body);
			 request.setSubject(configurationSource.getText("email.thankyou.subject", myLocale));
		 }
		 else if(EMAIL_NEW_CLAIM.equalsIgnoreCase(emailType)) {
			 myLocale =  new Locale("en","CA",myLocale.getVariant());
			 request.setTo(configurationManager.getMailSenderTo());	
			 String body = configurationSource.getText("email.newClaims.body.title", myLocale);
			 body += configurationSource.getText("email.newClaims.body.refnumber.title", myLocale)+ aClaim.getId();
			 body += configurationSource.getText("email.newClaims.body.dateSubmitted.title", myLocale)+ formatDate(new Date(System.currentTimeMillis()));
			 body += configurationSource.getText("email.newClaims.body.cardholderLastname.title", myLocale)+ aClaim.getLastName();
			 body += configurationSource.getText("email.newClaims.body.activityType.title", myLocale)+ configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale);
			 body += configurationSource.getText("email.newClaims.body.body.end.text", myLocale);
			 request.setBody(body);
			 request.setSubject(configurationSource.getText("email.newClaim.subject",myLocale) + " - "+configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale) + " - " + aClaim.getId()+" - "+aClaim.getLastName());			 
		 }
		 else if(EMAIL_NEW_DOCS.equalsIgnoreCase(emailType)) {
			 myLocale =  new Locale("en","CA",myLocale.getVariant());
			 request.setTo(configurationManager.getMailSenderTo());		
			 String body = configurationSource.getText("email.newClaimDocs.body.title", myLocale);
			 body += configurationSource.getText("email.newClaimDocs.body.refnumber.title", myLocale)+ aClaim.getId();
			 body += configurationSource.getText("email.newClaimDocs.body.dateSubmitted.title", myLocale)+ formatDate(aClaim.getDateCreated());
			 body += configurationSource.getText("email.newClaimDocs.body.cardholderLastname.title", myLocale)+ aClaim.getLastName();
			 body += configurationSource.getText("email.newClaimDocs.body.activityType.title", myLocale)+ configurationSource.getText("email.newClaimDocs.subject", myLocale) + " - "+configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale);
			 body += configurationSource.getText("email.newClaimDocs.body.body.end.text", myLocale);		 
			 request.setBody(body);
			 request.setSubject(configurationSource.getText("email.newClaimDocs.subject", myLocale) + " - "+configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale) + " - " + aClaim.getId()+" - "+aClaim.getLastName());			 
		 }
		 else if(EMAIL_NEW_NOTE.equalsIgnoreCase(emailType)) {
			 myLocale =  new Locale("en","CA",myLocale.getVariant());
			 request.setTo(configurationManager.getMailSenderTo());
			 String body = configurationSource.getText("email.newClaimNote.body.title", myLocale);
			 body += configurationSource.getText("email.newClaimNote.body.refnumber.title", myLocale)+ aClaim.getId();
			 body += configurationSource.getText("email.newClaimNote.body.dateSubmitted.title", myLocale)+ formatDate(aClaim.getDateCreated());
			 body += configurationSource.getText("email.newClaimNote.body.cardholderLastname.title", myLocale)+ aClaim.getLastName();
			 body += configurationSource.getText("email.newClaimNote.body.activityType.title", myLocale)+ configurationSource.getText("email.newClaimNote.subject", myLocale) + " - "+configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale);
			 body += configurationSource.getText("email.newClaimNote.body.body.end.text", myLocale);				 
			 request.setBody(body);
			 request.setSubject(configurationSource.getText("email.newClaimNote.subject", myLocale) + " - "+configurationSource.getText("email.claimForm."+aClaim.getClaimFormName()+".label",myLocale) + " - " + aClaim.getId()+" - "+aClaim.getLastName());			 
		 }		 
		 try {
				RestTemplate restTemplate = new RestTemplate();
				
				// Add the Jackson and String message converters
				restTemplate.getMessageConverters().add(
						new MappingJacksonHttpMessageConverter());
				restTemplate.getMessageConverters().add(
						new StringHttpMessageConverter());

				// the response to a String
				System.out.println("JSON REQ:"
						+ mapper.writeValueAsString(request));
				// try to read response 

				try {
					String response = restTemplate.postForObject(
							configurationManager.getMailSenderUrl(),
							mapper.writeValueAsString(request), String.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		 return ;*/
   }
  
    public int getCurrentYear(int delta) {
    	int result = 0;

    	Calendar c = Calendar.getInstance();
    	result = c.get(Calendar.YEAR);
    	result = result- delta;
    	return result;
    }
    
    public String getHomeLink(String lang) {
    	if(lang!=null && lang.equalsIgnoreCase("fr"))
    		return "/../fr";
    	return "/../";
    }
    
    public boolean enableGA() {
    	try {
			if("true".equalsIgnoreCase(configurationManager.getEnableGoogleAnalytics()))
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    public String formatLanguage(String preferedlang, String lang){
		 HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
         LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
         Locale myLocale = localeResolver.resolveLocale(req);
         lang = myLocale.getLanguage();
    	System.out.println("LANG " + lang + preferedlang);
    	if(lang.equalsIgnoreCase("fr")) {
    		if(preferedlang.equalsIgnoreCase("French"))
    			return "Francais";
    		return "Anglais";
    	}
    	return preferedlang;
    }  
    
}