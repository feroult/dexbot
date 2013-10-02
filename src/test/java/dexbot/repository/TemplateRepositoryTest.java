package dexbot.repository;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import dexbot.domain.Template;

public class TemplateRepositoryTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalUserServiceTestConfig(),
			new LocalDatastoreServiceTestConfig(), new LocalSearchServiceTestConfig()).setEnvIsLoggedIn(true).setEnvAuthDomain("localhost")
			.setEnvEmail("test@localhost");

	@Before
	public void setup() {
		helper.setUp();
	}

	@After
	public void tearDownHelper() {
		helper.tearDown();
	}

	@Test
	public void testSaveAndFindTemplate() {
		Template template = new Template();
		template.setServiceUrl("http://someservice.com");
		template.setTemplate("<engine selector='.header'><h1>{{=it.header}}</h1><engine>");

		TemplateRepository repository = new TemplateRepository();
		repository.save(template);

		template = repository.findByKey(template.getKey());
		assertEquals("http://someservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.header'><h1>{{=it.header}}</h1><engine>", template.getTemplate());

		template.setServiceUrl("http://someotherservice.com");
		template.setTemplate("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>");
		repository.save(template);

		template = repository.findByKey(template.getKey());
		assertEquals("http://someotherservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>", template.getTemplate());
	}

	@Test
	public void testSearchTemplates() {
		TemplateRepository repository = new TemplateRepository();
		Template template = new Template();
		template.setServiceUrl("http://someservice.com");
		template.setTemplate("<engine selector='.header'><h1>{{=it.header}}</h1><engine>");

		repository.save(template);

		template = new Template();
		template.setServiceUrl("http://someotherservice.com");
		template.setTemplate("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>");

		repository = new TemplateRepository();
		repository.save(template);

		List<Template> result = repository.listAll();
		assertEquals(2, result.size());

		template = result.get(0);
		assertEquals("http://someservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.header'><h1>{{=it.header}}</h1><engine>", template.getTemplate());

		template = result.get(1);
		assertEquals("http://someotherservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>", template.getTemplate());
	}

	@Test
	public void testSaveAndFindBase() {
		TemplateRepository repository = new TemplateRepository();
		repository.saveBase("<html><head><body>Hello dexbot!</body></head></html>");

		String base = repository.findBase();
		assertEquals("<html><head><body>Hello dexbot!</body></head></html>", base);
	}

	@Test
	public void testTemplateMerge() throws URISyntaxException {
		TemplateRepository repository = new TemplateRepository();

		Template template = new Template();
		template.setServiceUrl(TemplateRepositoryTest.class.getClassLoader().getResource("json").toURI().toString());
		template.setTemplate("<engine selector='.header'><h1>Great Scott - by: {{=it.name}}</h1><engine>");
		
		String merged = repository.mergeTemplate(template);
		assertEquals("<engine selector='.header'><h1>Great Scott - by: Emmett Brown</h1><engine>", merged);

	}
}
