/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to purchase Celerio ? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Template pack-javaee-jpa-backend:src/main/java/context/LocaleHolder.p.vm.java
 */
package ca.rsagroup.web.util;

import java.io.Serializable;
import java.util.Locale;



import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named
public class LocaleHolder implements Serializable {
	private Locale locale;

	public Locale getLocale() {
		return locale != null ? locale : Locale.getDefault();
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}