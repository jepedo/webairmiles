package ca.rsagroup.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.rsagroup.domain.ResourceBundles;

import com.mysql.jdbc.StringUtils;

@Named
public class LookupManager {

	@PersistenceContext
	private EntityManager em;

	public LookupManager() {

	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}

	public void commit() {
		getEntityManager().getTransaction().commit();
	}

	public Query createQuery(String query) {
		return getEntityManager().createQuery(query);
	}

	public void save(Object transientInstance) {

		try {
			beginTransaction();
			getEntityManager().persist(transientInstance);
			commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Object persistentInstance) {
		try {
			beginTransaction();
			getEntityManager().remove(persistentInstance);
			commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Object update(Object detachedInstance) {
		try {
			beginTransaction();
			Object result = getEntityManager().merge(detachedInstance);
			commit();
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> findByProperty(String ObjName, String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from " + ObjName
					+ " model where model." + propertyName + "= :propertyValue";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> findByProperties(String objName,
			Map<String, Object> props) {
		try {
			String queryString = "select model from " + objName
					+ " model where ";

			HashMap params = new HashMap();
			int ix = 0;
			for (Iterator iterator = props.keySet().iterator(); iterator
					.hasNext();) {
				String propertyName = (String) iterator.next();
				Object value = props.get(propertyName);
				if (ix == 0)
					queryString += " model." + propertyName + "= :p" + ix;
				else
					queryString += " and model." + propertyName + "= :p" + ix;
				params.put("p" + ix, value);
				ix++;
			}
			Query query = getEntityManager().createQuery(queryString);

			for (Iterator iterator = params.keySet().iterator(); iterator
					.hasNext();) {
				String propertyName = (String) iterator.next();
				Object value = props.get(propertyName);
				query.setParameter(propertyName, value);
			}

			return query.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	public List getUICodes(String type) {

		String q = "select model from ca.rsagroup.domain.CodeUiLabel model where  model.id.type= :p0  order by model.id.value";
		Query query = getEntityManager().createQuery(q);
		query.setParameter("p0", type);

		List res = query.getResultList();

		return res != null ? res : new Vector();
	}

	public List getUICodes(String type, String lang) {

		String q = "select model from ca.rsagroup.domain.CodeUiLabel model where  model.id.type= :p0 and model.id.language= :p1 order by model.value";
		Query query = getEntityManager().createQuery(q);
		query.setParameter("p0", type);
		query.setParameter("p1", lang);

		List res = query.getResultList();

		return res != null ? res : new Vector();
	}

	public List getQueryResult(String q) {
		Query query = getEntityManager().createNamedQuery(q);
		List res = query.getResultList();

		return res != null ? res : new Vector();
	}

	public String getBundle(String key) {
		String res = key;
		String lang = "en";
		Locale locale = LocaleContextHolder.getLocale();
		try {
			lang = locale.getLanguage();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String variant = null;
		try {
			variant = locale.getVariant();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String q = "select model from ca.rsagroup.domain.ResourceBundles model where  model.stringId= :p0 and model.languageCode= :p1";
		if (!StringUtils.isNullOrEmpty(variant)) {
			q += (" and model.variant=:p2");
		}
		Query query = getEntityManager().createQuery(q);
		query.setParameter("p0", key);
		query.setParameter("p1", lang);
		if (!StringUtils.isNullOrEmpty(variant)) {
			query.setParameter("p2", variant);
		}
		List<ResourceBundles> res1 = query.getResultList();
		if (res1 != null && res1.size() > 0) {
			if (!StringUtils.isNullOrEmpty(variant)) {
				String s = null;
				for (Iterator iterator = res1.iterator(); iterator.hasNext();) {
					ResourceBundles resourceBundles = (ResourceBundles) iterator
							.next();
					if (resourceBundles.getVariant().equalsIgnoreCase(variant)) {
						s = resourceBundles.getValue();
						break;
					}
				}
				if (s == null) {
					for (Iterator iterator = res1.iterator(); iterator
							.hasNext();) {
						ResourceBundles resourceBundles = (ResourceBundles) iterator
								.next();
						if (resourceBundles.getVariant()
								.equalsIgnoreCase("www")) {
							res = resourceBundles.getValue();
							break;
						}
					}
				} else {
					res = s;
				}
			} else {
				res = res1.get(0).getValue();
			}

		}

		return res;
	}

}
