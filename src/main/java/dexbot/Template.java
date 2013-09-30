package dexbot;

public class Template {

	private String template;

	public Template(String template) {
		this.template = template;
	}

	public String parse() {		
		return template;
	}

	public String parse(String context) {
		return template.replaceAll("\\$\\{hello\\}", context);
	}

}
