package ca.rsagroup.web.util;

import java.util.Locale;

import javax.el.ELContext;
import javax.el.ELException;
import javax.faces.context.FacesContext;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

public class RsaSpringBeanFacesELResolver extends SpringBeanFacesELResolver {

	//see also : http://forum.springsource.org/showthread.php?t=31015&highlight=messages+database&page=2
	//http://forum.springsource.org/showthread.php?t=58825
	@Override
	public Object getValue(ELContext elContext, Object base, Object property)
			throws ELException {
		Locale myLocale = LocaleContextHolder.getLocale();
		if (base instanceof MessageSource && property instanceof String) {
			String result = ((MessageSource) base).getMessage(
					(String) property, null, getLocale());

			if (null != result) {
				elContext.setPropertyResolved(true);
			}

			return result;
		}

		return super.getValue(elContext, base, property);
	}

	private Locale getLocale() {
		return LocaleContextHolder.getLocale();
//		FacesContext context = FacesContext.getCurrentInstance();
//		return context.getExternalContext().getRequestLocale();
	}
}