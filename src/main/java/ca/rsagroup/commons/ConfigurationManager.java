package ca.rsagroup.commons;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@ManagedBean
@Scope(value="application")
@Component
public class ConfigurationManager {
		
	@Value("${quoteUrl}")
	private String quoteUrl;
	
	@Value("${enableGoogleAnalytics}")
	private String enableGoogleAnalytics;
	

	@Value("${mailSenderUrl}")
	private String mailSenderUrl;	
	@Value("${mailSenderFrom}")
	private String mailSenderFrom;
	@Value("${mailSenderReplyTo}")
	private String mailSenderReplyTo;
	
	@Value("${tempDir}")
	private String tempDir;
	
		

	public String getMailSenderUrl() {
		return mailSenderUrl;
	}

	public void setMailSenderUrl(String mailSenderUrl) {
		this.mailSenderUrl = mailSenderUrl;
	}

	public String getQuoteUrl() {
		return quoteUrl;
	}

	public void setQuoteUrl(String quoteUrl) {
		this.quoteUrl = quoteUrl;
	}


	public String getMailSenderFrom() {
		return mailSenderFrom;
	}

	public void setMailSenderFrom(String mailSenderFrom) {
		this.mailSenderFrom = mailSenderFrom;
	}

	public String getMailSenderReplyTo() {
		return mailSenderReplyTo;
	}

	public void setMailSenderReplyTo(String mailSenderReplyTo) {
		this.mailSenderReplyTo = mailSenderReplyTo;
	}
	
	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getEnableGoogleAnalytics() {
		return enableGoogleAnalytics;
	}

	public void setEnableGoogleAnalytics(String enableGoogleAnalytics) {
		this.enableGoogleAnalytics = enableGoogleAnalytics;
	}
	
}
