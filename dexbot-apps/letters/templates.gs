var Templates = (function() {

	var base_url = 'https://raw.github.com/feroult/dexbot/master/dexbot-apps/letters/templates/';

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

	function fetch(url) {
		var template = new Template(UrlFetchApp.fetch(base_url + url).toString());
		parseBlobs(template);
		return template;
	}

	return {
		fetch : fetch
	}

})();

function test() {
	var template = Templates.fetch('xpto.html');
	
	Logger.log(template.body);
	Logger.log(template.blobs);
	
	GSUnit.assertNotUndefined(template.blobs['gravatar']);
	GSUnit.assertNotUndefined(template.blobs['xpto']);	
	GSUnit.assertTrue(template.body.indexOf('cid:gravatar') != -1);
	GSUnit.assertTrue(template.body.indexOf('cid:xpto') != -1);
}
