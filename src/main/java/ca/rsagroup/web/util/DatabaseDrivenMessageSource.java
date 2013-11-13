package ca.rsagroup.web.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

public class DatabaseDrivenMessageSource extends ReloadableResourceBundleMessageSource {

    private Map<String, ResourceBundle> properties = new HashMap<String, ResourceBundle>();
	private DataSource dataSource;
	private long createTimestamp = System.currentTimeMillis();
	private long cacheMillis = -1;
	private String bundleName;
	private String tableName;
	
//	public DatabaseDrivenMessageSource () {
//		
//	}
//	public DatabaseDrivenMessageSource (String bundleName, String tableName,DataSource dataSource) {
//		this.bundleName = bundleName;
//		this.tableName = tableName;
//		this.dataSource = dataSource;
//	}
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getText(code, locale);
        MessageFormat result = createMessageFormat(msg, locale);
        return result;
    }
    @Override
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheMillis = (cacheSeconds * 1000);
	}
	
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return getText(code, locale);
    }

    public String getText(String code, Locale locale) {
    	// check expiration time   	
    	if(createTimestamp < System.currentTimeMillis() - this.cacheMillis) {    		
    		properties = new HashMap<String, ResourceBundle>(); 
    		createTimestamp = System.currentTimeMillis();
    	}
//    	locale =FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	
    	ResourceBundle bundle = properties.get(locale.getLanguage());
    	if(bundle==null) {
    		DBResourceBundleControl control =  new DBResourceBundleControl(dataSource,tableName);
    		try {
				bundle =control.newBundle(bundleName, locale, DBResourceBundleControl.FORMAT_DB, null, true);	
				properties.put(locale.getLanguage(),bundle);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(bundle==null) {
    		// no language defined in DB for this locale...
    		DBResourceBundleControl control =  new DBResourceBundleControl(dataSource,tableName);
			try {
				bundle =control.newBundle(bundleName, Locale.ENGLISH, DBResourceBundleControl.FORMAT_DB, null, true);			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			properties.put(locale.getLanguage(),bundle);
    	}  	    	
    	
        try {
			return bundle!=null ?bundle.getString(code):"";
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			if(code.startsWith("javax.faces."))
				return "";
			return code;
		}

    }

    public void reload() {
        properties.clear();        
    }

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public String getBundleName() {
		return bundleName;
	}
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}