var Templates = (function() {

	function Template(body) {
		this.body = body;
		this.blobs = {};
	}

	function loadBlob(template, line) {
		var regexpBlobUrl = /.*\$blob\(\s*\'([^\']*)\'\s*\,\s*\'([^\']*)\'\s*\).*/;
		var regexpBlobFunction = /.*\$blob\(\s*\'([^\']*)\'\s*\).*/;

		var result = line.match(regexpBlobFunction);
		if (result != null) {
			var blobKey = result[1];
			template.blobs[blobKey] = Blobs.fromFunction(blobKey);
			return blobKey;
		}

		result = line.match(regexpBlobUrl);
		if (result != null) {
			var blobKey = result[1];
			var url = result[2];
			template.blobs[blobKey] = Blobs.fromUrl(url);
			return blobKey;
		}

		return null;
	}

	function parseBlobs(template) {
		var parsedBody = [];
		var lines = template.body.split(/\r?\n/);

		for (var i = 0; i < lines.length; i++) {
			var line = lines[i];

			var blobKey = loadBlob(template, line);

			if (blobKey) {
				parsedBody.push(line.replace(/\$blob\(.*\)/g, 'cid:' + blobKey));
				continue;
			}
			parsedBody.push(line);
		}

		template.body = parsedBody.join('\n');
	}

	function fetch(body) {
		if(body.toLowerCase().indexOf("http") == 0) {
			return UrlFetchApp.fetch(body).toString();
		}
		
		if(body.toLowerCase().indexOf("docs://") == 0) {
			var key = body.substring(7);			
			return DocumentApp.openById(key).getBody().getText();
		}
		
		return body;
	}
	
	function parse(body) {
		var template = new Template(fetch(body));
		parseBlobs(template);
		return template;
	}

	return {
		parse : parse
	}

})();

function testStringTemplate() {
	Blobs.register(mockSimpleBlob);
	
	var template = Templates.parse('<div><img src="$blob(\'mockSimpleBlob\')" />');
	
	Logger.log(template.body);
	Logger.log(template.blobs);
	
	GSUnit.assertNotNull(template.blobs['mockSimpleBlob']);
	GSUnit.assertTrue(template.body.indexOf('cid:mockSimpleBlob') != -1);
}


function testDocsTemplate() {
	Blobs.register(mockSimpleBlob);
	
	var template = Templates.parse('docs://1QIKt9i8br8_UFwt19YpCBuwh_XgB9fXWGcCl8rH1OF8');
	
	Logger.log(template.body);
	Logger.log(template.blobs);
	
	GSUnit.assertNotNull(template.blobs['mockSimpleBlob']);
	GSUnit.assertTrue(template.body.indexOf('cid:mockSimpleBlob') != -1);
}

