package dexbot;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TemplateParser {

	private String template;

	public TemplateParser(String template) {
		this.template = template;
	}

	public String parse() {
		return template;
	}

	public String parse(String context) {
		return template.replaceAll("\\$\\{hello\\}", context);
	}

	public String parse(Object o) {
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("bean", o);

		StringWriter writer = new StringWriter();

		Velocity.evaluate(velocityContext, writer, "dexbot", template);

		return writer.toString();
	}

}
