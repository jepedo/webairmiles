package ca.rsagroup.commons;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@ManagedBean
@Scope(value="application")
@Component
public class ConfigurationManager {
		
	@Value("${esbUrl}")
	private String esbUrl;
	
	@Value("${basicAuthUser}")
	private String basicAuthUser;
	
	@Value("${basicAuthPwd}")
	private String basicAuthPwd;
		
	@Value("${enableDomains}")
	private boolean enableDomains;
	
	@Value("${defaultDomain}")
	private String defaultDomain;

	@Value("${registerAction}")
	private String registerAction;
	
	@Value("${registerAndAddAnotherAction}")
	private String registerAndAddAnotherAction;
	
	@Value("${IncludeDTMScripts}")
	private boolean includeDTMScripts;
	
	@Value("${esb.tlsversion}")
	private String tlsVersion;
	
	public String getEsbUrl() {
		return esbUrl;
	}

	public void setEsbUrl(String esbUrl) {
		this.esbUrl = esbUrl;
	}


	public boolean isEnableDomains() {
		return enableDomains;
	}

	public void setEnableDomains(boolean enableDomains) {
		this.enableDomains = enableDomains;
	}

	public String getDefaultDomain() {
		return defaultDomain;
	}

	public void setDefaultDomain(String defaultDomain) {
		this.defaultDomain = defaultDomain;
	}

	public String getRegisterAction() {
		return registerAction;
	}

	public void setRegisterAction(String registerAction) {
		this.registerAction = registerAction;
	}

	public String getRegisterAndAddAnotherAction() {
		return registerAndAddAnotherAction;
	}

	public void setRegisterAndAddAnotherAction(String registerAndAddAnotherAction) {
		this.registerAndAddAnotherAction = registerAndAddAnotherAction;
	}

	public boolean getIncludeDTMScripts() {
		return includeDTMScripts;
	}

	public void setIncludeDTMScripts(boolean includeDTMScripts) {
		this.includeDTMScripts = includeDTMScripts;
	}

	public String getBasicAuthUser() {
		return basicAuthUser;
	}

	public void setBasicAuthUser(String basicAuthUser) {
		this.basicAuthUser = basicAuthUser;
	}

	public String getBasicAuthPwd() {
		return basicAuthPwd;
	}

	public void setBasicAuthPwd(String basicAuthPwd) {
		this.basicAuthPwd = basicAuthPwd;
	}

	public String getTlsVersion() {
		return tlsVersion;
	}

	public void setTlsVersion(String tlsVersion) {
		this.tlsVersion = tlsVersion;
	}
	
	
}
