var Templates = (function() {

	function Template(body) {
		this.unparsed = body;
		this.parsed = null;
		this.blobs = {};

		this.parse = function() {
			var tempFn = doT.template(this.unparsed);
			this.parsed = tempFn(this);			
		}
		
		this.fn = function(blobFunction) {
			return this.addBlob(Blobs.fromFunction(blobFunction));
		}
		
		this.url = function(url) {
			return this.addBlob(Blobs.fromUrl(url));
		}
		
		this.addBlob = function(blob) {
			var nextId = 'blob' + (Object.keys(this.blobs).length + 1);
			this.blobs[nextId] = blob;
			return 'cid:' + nextId;
		} 
	}

	function fetch(body) {
		if (body.toLowerCase().indexOf("http") == 0) {
			return UrlFetchApp.fetch(body).toString();
		}

		if (body.toLowerCase().indexOf("docs://") == 0) {
			var key = body.substring(7);
			return DocumentApp.openById(key).getBody().getText();
		}

		return body;
	}

	function parse(body) {
		var template = new Template(fetch(body));
		template.parse();
		return template;
	}

	return {
		parse : parse
	}

})();

function testAll() {
	testFunctionBlob();
	testUrlBlob();
	testDocsTemplate();
	testHttpTemplate();
}

function testFunctionBlob() {
	Blobs.register(mockSimpleBlob);

	var template = Templates.parse('<img src="{{= it.fn(\'mockSimpleBlob\') }}" />');

	GSUnit.assertEquals('<img src="cid:blob1" />', template.parsed);
	GSUnit.assertNotNull(template.blobs['blob1']);
}

function testUrlBlob() {
	var template = Templates.parse('<img src="{{= it.url(\'https://raw.github.com/feroult/dexbot/master/dexbot-apps/letters/test/bumblebee.jpg\') }}" />');

	GSUnit.assertEquals('<img src="cid:blob1" />', template.parsed);
	GSUnit.assertNotNull(template.blobs['blob1']);	
}

function testDocsTemplate() {
	Blobs.register(mockSimpleBlob);
	assertTemplate(Templates.parse('docs://1QIKt9i8br8_UFwt19YpCBuwh_XgB9fXWGcCl8rH1OF8'));
}

function testHttpTemplate() {
	Blobs.register(mockSimpleBlob);
	assertTemplate(Templates.parse('https://raw.github.com/feroult/dexbot/master/dexbot-apps/letters/test/template.html'));
}

function assertTemplate(template) {
	Logger.log(template.parsed);
	Logger.log(template.blobs);
	GSUnit.assertNotNull(template.blobs['blob1']);
	GSUnit.assertNotNull(template.blobs['blob2']);
	GSUnit.assertTrue(template.parsed.indexOf('cid:blob1') != -1);
	GSUnit.assertTrue(template.parsed.indexOf('cid:blob2') != -1);
}