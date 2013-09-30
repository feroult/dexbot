package dexbot;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemplateTest {

	@Test
	public void testSimpleTemplate() {
		Template template = new Template("<tag>hello</tag>");				
		assertEquals("<tag>hello</tag>", template.parse());
	}

	@Test
	public void testTemplateOptions() {
		Template template = new Template("<tag>${hello}</tag>");				
		assertEquals("<tag>custom hello</tag>", template.parse("custom hello"));
	}
	
}
