package dexbot.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dexbot.templates.TemplateParser;

public class TemplateParserTest {

	public class Bean {

		private String hello;

		public Bean(String hello) {
			this.hello = hello;
		}

		public String getHello() {
			return hello;
		}
	}

	@Test
	public void testTemplateWithoutPlaceholders() {
		TemplateParser template = TemplateParser.fromString("<tag>hello</tag>");
		assertEquals("<tag>hello</tag>", template.parse());
	}

	@Test
	public void testTemplateWithBeanProperty() {
		TemplateParser template = TemplateParser.fromString("<tag>$bean.hello</tag>");

		assertEquals("<tag>olah</tag>", template.parse(new Bean("olah")));
		assertEquals("<tag>ciao</tag>", template.parse(new Bean("ciao")));
	}
	
	@Test
	public void testTemplateFromResource() {
		TemplateParser template = TemplateParser.fromResource("/dexbot/templates/hello.html");
		
		assertEquals("<tag>olah</tag>", template.parse(new Bean("olah")));
		assertEquals("<tag>ciao</tag>", template.parse(new Bean("ciao")));
	}
}
