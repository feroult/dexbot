package dexbot.repository;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import dexbot.domain.Base;
import dexbot.domain.Template;

public class RepositoryTest {

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
		Repository repository = new Repository();
		Base base = new Base();
		base.setBase("<html></html>");
		base.setDesc("Some base");
		repository.saveBase(base);
		
		Template template = new Template();
		template.setServiceUrl("http://someservice.com");
		template.setTemplate("<engine selector='.header'><h1>{{=it.header}}</h1><engine>");
		template.setBaseKey(base.getKey().getId());

		repository.saveTemplate(template);

		template = repository.findTemplateByKey(template.getKey());
		assertEquals("http://someservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.header'><h1>{{=it.header}}</h1><engine>", template.getTemplate());

		template.setServiceUrl("http://someotherservice.com");
		template.setTemplate("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>");
		repository.saveTemplate(template);

		template = repository.findTemplateByKey(template.getKey());
		assertEquals("http://someotherservice.com", template.getServiceUrl());
		assertEquals("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>", template.getTemplate());
	}

	@Test
	public void testSearchTemplates() {
		Repository repository = new Repository();

		Base base = new Base();
		base.setBase("<html></html>");
		base.setDesc("Some base");
		repository.saveBase(base);

		Template template = new Template();
		template.setServiceUrl("http://someservice.com");
		template.setTemplate("<engine selector='.header'><h1>{{=it.header}}</h1><engine>");
		template.setBaseKey(base.getKey().getId());

		repository.saveTemplate(template);

		template = new Template();
		template.setServiceUrl("http://someotherservice.com");
		template.setTemplate("<engine selector='.footer'><h1>{{=it.header}}</h1><engine>");
		template.setBaseKey(base.getKey().getId());

		repository = new Repository();
		repository.saveTemplate(template);

		List<Template> result = repository.listAll(base.getKey().getId());
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
		Repository repository = new Repository();
		Base base = new Base();
		base.setBase("<html><head><body>Hello dexbot!</body></head></html>");
		base.setDesc("Some base");
		repository.saveBase(base);

		String baseStr = repository.findBase(base.getKey());
		assertEquals("<html><head><body>Hello dexbot!</body></head></html>", baseStr);
	}

	@Test
	public void testTemplateMerge() throws URISyntaxException {
		Repository repository = new Repository();

		Base base = new Base();
		base.setBase("<html><head><body>Hello dexbot!</body></head></html>");
		base.setDesc("Some base");
		repository.saveBase(base);

		Template template = new Template();
		template.setServiceUrl(RepositoryTest.class.getClassLoader().getResource("json").toURI().toString());
		template.setTemplate("<engine selector='.header'><h1>Great Scott - by: {{=it.name}}</h1><engine>");
		template.setBaseKey(base.getKey().getId());
		
		String merged = repository.mergeTemplate(template);
		assertEquals("<engine selector='.header'><h1>Great Scott - by: Emmett Brown</h1><engine>", merged);
	}

	@Test
	public void testMergedEmail() throws URISyntaxException {
		Repository repository = new Repository();

		Base base = new Base();
		base.setBase("<html><head><body><div class='first'></div><div class='second'></div></body></head></html>");
		base.setDesc("Some base");
		repository.saveBase(base);

		Template template = new Template();
		template.setServiceUrl(RepositoryTest.class.getClassLoader().getResource("json").toURI().toString());
		template.setTemplate("<engine selector='.first'><h1>Great Scott - by: {{=it.name}}</h1></engine>");
		template.setBaseKey(base.getKey().getId());
		repository.saveTemplate(template);

		template = new Template();
		template.setServiceUrl(RepositoryTest.class.getClassLoader().getResource("json").toURI().toString());
		template.setTemplate("<engine selector='.second'><h2>Great Scott - by: {{=it.name}}</h2></engine>");
		template.setBaseKey(base.getKey().getId());
		repository.saveTemplate(template);

		String email = repository.mergeEmail(base.getKey());
		
		Document document = Jsoup.parse(email);

		Elements elements = document.select(".first");
		assertEquals(1, elements.size());
		Element child = elements.first().child(0);
		assertEquals("h1", child.nodeName());
		assertEquals("Great Scott - by: Emmett Brown", child.text());
		
		elements = document.select(".second");
		assertEquals(1, elements.size());
		child = elements.first().child(0);
		assertEquals("h2", child.nodeName());
		assertEquals("Great Scott - by: Emmett Brown", child.text());
	}
}
