package dexbot.repository;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.ez.DJ;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dexbot.domain.Base;
import dexbot.domain.Template;
import dexbot.utils.URLUtils;

public class Repository {

	public void saveTemplate(Template template) {
		if (template.getBaseKey() == null) {
			throw new RuntimeException("Base key is required");
		}
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		Entity entity = null;

		if (template.getKey() == null) {
			entity = new Entity("template");
		} else {
			entity = new Entity(template.getKey());
		}

		entity.setProperty("base_key", template.getBaseKey());
		entity.setProperty("service_url", template.getServiceUrl());
		entity.setProperty("template", template.getTemplate());

		Key key = datastoreService.put(entity);
		template.setKey(key);
	}

	public Template findTemplateByKey(Key key) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = datastoreService.get(key);
			Template template = entityToTemplate(entity);
			return template;
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public List<Template> listAll(long baseKey) {
		List<Template> templates = new ArrayList<Template>();

		DatastoreService service = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("template").setFilter(new FilterPredicate("base_key", FilterOperator.EQUAL, baseKey));

		Iterable<Entity> iterable = service.prepare(query).asIterable();
		Iterator<Entity> iterator = iterable.iterator();

		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			Template template = entityToTemplate(entity);
			templates.add(template);
		}

		return templates;
	}

	private Template entityToTemplate(Entity entity) {
		String serviceUrl = (String) entity.getProperty("service_url");
		String templateStr = (String) entity.getProperty("template");
		long baseKey = (long) entity.getProperty("base_key");
		Key key = entity.getKey();

		Template template = new Template();
		template.setKey(key);
		template.setServiceUrl(serviceUrl);
		template.setTemplate(templateStr);
		template.setBaseKey(baseKey);
		return template;
	}

	public void saveBase(Base base) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		Entity entity = null;
		if (base.getKey() == null) {
			entity = new Entity("template_base");
		} else {
			entity = new Entity(base.getKey());
		}
		
		entity.setProperty("base", base.getBase());
		entity.setProperty("desc", base.getDesc());
		Key key = datastoreService.put(entity);
		base.setKey(key);
	}

	public String findBase(Key baseKey) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = datastoreService.get(baseKey);
			return (String) entity.getProperty("base");
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public String mergeTemplate(Template template) {

		try {
			URL url = new URL(template.getServiceUrl());
			String jsonString = URLUtils.readAsString(url);

			JsonElement jsonElement = new JsonParser().parse(jsonString);

			return new DJ().template(template.getTemplate()).context(jsonElement).result();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String mergeEmail(Key baseKey) {
		
		String base = findBase(baseKey);
		
		Document document = Jsoup.parse(base);

		List<Template> list = listAll(baseKey.getId());
		
		for (Template template : list) {
			String mergedTemplate = mergeTemplate(template);
			
			Document templateDoc = Jsoup.parse(mergedTemplate);
			Elements elements = templateDoc.select("engine");

			for (Element element : elements) {
				String selector = element.attr("selector");
				
				Elements docElements = document.select(selector);
				for (Element docElement : docElements) {
					Elements children = element.children();
					
					for (Element child : children) {
						docElement.appendChild(child);
					}
				}
			}
		}

		return document.toString();
	}

}
