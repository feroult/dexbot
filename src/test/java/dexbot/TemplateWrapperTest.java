package dexbot;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemplateWrapperTest {

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
		TemplateWrapper template = new TemplateWrapper("<tag>hello</tag>");
		assertEquals("<tag>hello</tag>", template.parse());
	}

	@Test
	public void testTemplateWithBeanProperty() {
		TemplateWrapper template = new TemplateWrapper("<tag>$bean.hello</tag>");

		assertEquals("<tag>olah</tag>", template.parse(new Bean("olah")));
		assertEquals("<tag>ciao</tag>", template.parse(new Bean("ciao")));
	}

}
