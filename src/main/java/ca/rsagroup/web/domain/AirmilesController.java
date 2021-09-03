package ca.rsagroup.web.domain;

import java.nio.charset.Charset;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.webflow.execution.RequestContext;

import ca.rsagroup.airmiles.AirmilesRequest;
import ca.rsagroup.airmiles.AirmilesResponse;
import ca.rsagroup.airmiles.ErrorMessage;
import ca.rsagroup.commons.ConfigurationManager;
import ca.rsagroup.model.ValidResponse;
import ca.rsagroup.model.ValidResponses;
import ca.rsagroup.service.LookupManager;
import ca.rsagroup.web.util.MessageUtil;
import ca.rsagroup.websecurity.restful.SpringRestTemplate;

/**
 * Thin controller layer allowing you to do business validation and other
 * conditional checks that are easier to implement in Java than in webflow's xml
 * syntax.
 */
@Named

public class AirmilesController {
	private static final Logger log = Logger.getLogger(AirmilesController.class);

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
		if (date == null)
			return "";

		try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String formatDate(java.util.Date date) {
		if (date == null)
			return "";

		try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String formatDateShort(java.sql.Timestamp date) {
		if (date == null)
			return "";

		try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String formatDateShort(java.util.Date date) {
		if (date == null)
			return "";

		try {
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
			return df.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String formatBoolean(Boolean b) {
		if (b == null)
			return "";
		else if (b)
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
		return (int) (diffInMillis / (24 * 1000 * 60 * 60));
	}

	public void setLanguage(String language, Integer domainId) {
		String variant = domainId != null ? domainId.toString() : null;
		try {
			LocaleContextHolder.setLocale(new Locale(language, "CA", variant), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LocaleContextHolder.setLocale(Locale.ENGLISH);
//			e.printStackTrace();
		}
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
			localeResolver.setLocale(req, res, new Locale(language, "CA", variant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

//    	FacesContext.getCurrentInstance().getExternalContext()CurrentInstance().getViewRoot().setLocale( new Locale(language,"CA",variant));
	}

	public String setLanguageReverse(String language, Integer domainId, Integer fiId) {
		String variant = domainId != null ? domainId.toString() : null;

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
			localeResolver.setLocale(req, res, new Locale(language, "CA", variant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		if (language != null && language.equalsIgnoreCase("en"))
			language = "fr";
		else
			language = "en";
		return req.getRequestURI() + "?fi=" + fiId + "&lang=" + language;
	}

	public ValidResponses processRequest(AirmilesRequest airmilesRequest, boolean addAnother, ValidResponses resp,
			RequestContext context) {
		if (resp == null)
			resp = new ValidResponses();

		// resp.getResponses().add(new
		// ValidResponse(airmilesRequest.getPolicy(),airmilesRequest.getPolicyDate(),lookupManager.getBundle("airmiles.registeredStatus.txt")));

		if (airmilesRequest == null)
			return resp;
		if (addAnother)
			airmilesRequest.setActionSelected(configurationManager.getRegisterAndAddAnotherAction());
		else
			airmilesRequest.setActionSelected(configurationManager.getRegisterAction());

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
		Locale myLocale = localeResolver.resolveLocale(req);

		ObjectMapper mapper = new ObjectMapper();
		try {
			RestTemplate restTemplate = SpringRestTemplate.createRestTemplate(configurationManager.getTlsVersion());

			// Add the Jackson and String message converters
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			// the response to a String
			// try to read response
			log.warn(":::REQUEST:::" + mapper.writeValueAsString(airmilesRequest));

			HttpHeaders headers = new HttpHeaders();
			String securityToken = configurationManager.getBasicAuthUser() + ":"
					+ configurationManager.getBasicAuthPwd();
			byte[] encodedToken = Base64.encodeBase64(securityToken.getBytes(Charset.forName("US-ASCII")));
			headers.add("Authorization", "Basic " + new String(encodedToken));
			headers.setContentType(MediaType.APPLICATION_JSON);
			// create the HTTP Request object
			HttpEntity<AirmilesRequest> request = new HttpEntity<AirmilesRequest>(airmilesRequest, headers);

			// invoke the web service

			ResponseEntity<AirmilesResponse> response = restTemplate.exchange(configurationManager.getEsbUrl(),
					HttpMethod.POST, request, AirmilesResponse.class);
			AirmilesResponse saveResponse = response.getBody();

			// String response =
			// restTemplate.postForObject(configurationManager.getEsbUrl(),mapper.writeValueAsString(airmilesRequest),
			// String.class);
			log.warn(":::RESPONSE:::" + mapper.writeValueAsString(response));
			// AirmilesResponse saveResponse =
			// mapper.readValue(response,AirmilesResponse.class);
			if (saveResponse != null && saveResponse.getStatus().equalsIgnoreCase("OK")) {
				resp.getResponses()
						.add(new ValidResponse(airmilesRequest.getPolicy(), airmilesRequest.getPolicyDate(),
								lookupManager.getBundle("airmiles.registeredStatus.txt"),
								airmilesRequest.getAirmilesNumber(), airmilesRequest.getAirmilesName()));
				if (addAnother) {
					airmilesRequest.setPolicy(null);
					airmilesRequest.setPolicyDate(null);
				} else {
					airmilesRequest.setAirmilesName(null);
					airmilesRequest.setAirmilesNumber(null);
					airmilesRequest.setPhone(null);
					airmilesRequest.setEmail(null);
					airmilesRequest.setPolicy(null);
					airmilesRequest.setPolicyDate(null);
					airmilesRequest.setAcceptTerms(false);
				}
			} else if (saveResponse != null && saveResponse.getErrors() != null
					&& saveResponse.getErrors().size() > 0) {
				MessageContext messages = context.getMessageContext();
				for (ErrorMessage error : saveResponse.getErrors()) {
                    String errorMsg = error.getMessage();
                    if(errorMsg!=null)
                    	errorMsg = errorMsg.replaceAll("\\t", "");
					if (error.getCode() != null && error.getCode().equalsIgnoreCase("RSA"))
						MessageUtil.addGlobalMessage(context.getMessageContext(), errorMsg);
					else
						MessageUtil.addGlobalMessage(context.getMessageContext(),
								lookupManager.getBundle(error.getCode()));
				}
			} else {
				// default error
				MessageUtil.addGlobalMessage(context.getMessageContext(),
						lookupManager.getBundle("airmiles.esbError.txt"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("ESB Exception: ", e);
		}

		return resp;
	}

	public int getCurrentYear(int delta) {
		int result = 0;

		Calendar c = Calendar.getInstance();
		result = c.get(Calendar.YEAR);
		result = result - delta;
		return result;
	}

	public String getHomeLink(String lang) {
		if (lang != null && lang.equalsIgnoreCase("fr"))
			return "/../fr";
		return "/../";
	}

	public boolean enableGA() {

		return true;
	}

	public String formatLanguage(String preferedlang, String lang) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
		Locale myLocale = localeResolver.resolveLocale(req);
		lang = myLocale.getLanguage();
		System.out.println("LANG " + lang + preferedlang);
		if (lang.equalsIgnoreCase("fr")) {
			if (preferedlang.equalsIgnoreCase("French"))
				return "Francais";
			return "Anglais";
		}
		return preferedlang;
	}

	public String getDomain() {
		if (!configurationManager.isEnableDomains())
			return configurationManager.getDefaultDomain();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		String result = "";
		String reqUrl = request.getRequestURL().toString();
		try {
			result = reqUrl.substring(reqUrl.indexOf("//") + 2, reqUrl.indexOf("."));
		} catch (Exception e) {
			result = configurationManager.getDefaultDomain();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}