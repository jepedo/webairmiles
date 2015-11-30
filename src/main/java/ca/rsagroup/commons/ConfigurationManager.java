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


	
}
