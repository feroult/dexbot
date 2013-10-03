package dexbot.templates;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TemplateParser {

	private String template;

	private TemplateParser(String template) {
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

	public static TemplateParser fromResource(String name) {
		try {
			String template = FileUtils.readFileToString(FileUtils.toFile(TemplateParser.class.getResource(name)));
			return new TemplateParser(template);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static TemplateParser fromString(String template) {
		return new TemplateParser(template);
	}
}
