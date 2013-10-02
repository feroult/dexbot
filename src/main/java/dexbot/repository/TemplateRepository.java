package dexbot.repository;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.ez.DJ;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dexbot.domain.Template;
import dexbot.utils.URLUtils;

public class TemplateRepository {

	public void save(Template template) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		Entity entity = null;

		if (template.getKey() == null) {
			entity = new Entity("template");
		} else {
			entity = new Entity(template.getKey());
		}

		entity.setProperty("service_url", template.getServiceUrl());
		entity.setProperty("template", template.getTemplate());

		Key key = datastoreService.put(entity);
		template.setKey(key);
	}

	public Template findByKey(Key key) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity entity = datastoreService.get(key);
			Template template = entityToTemplate(entity);
			return template;
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public List<Template> listAll() {
		List<Template> templates = new ArrayList<Template>();

		DatastoreService service = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("template");

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
		Key key = entity.getKey();

		Template template = new Template();
		template.setKey(key);
		template.setServiceUrl(serviceUrl);
		template.setTemplate(templateStr);
		return template;
	}

	public void saveBase(String base) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		Key key = KeyFactory.createKey("template_base", "BASE");
		Entity entity = new Entity(key);
		entity.setProperty("base", base);
		datastoreService.put(entity);
	}

	public String findBase() {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("template_base", "BASE");

		try {
			Entity entity = datastoreService.get(key);
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

}
