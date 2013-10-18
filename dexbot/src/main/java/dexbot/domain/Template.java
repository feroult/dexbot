package dexbot.domain;

import com.google.appengine.api.datastore.Key;

public class Template {
	private Key key;
	private String serviceUrl;
	private String template;
	private Long baseKey;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Long getBaseKey() {
		return baseKey;
	}

	public void setBaseKey(Long baseKey) {
		this.baseKey = baseKey;
	}

}
