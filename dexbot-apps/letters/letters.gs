var TemplateApp = (function() {

	var base_url = 'https://raw.github.com/feroult/dexbot/master/dexbot-apps/letters/templates/';

	function Template(body) {
		this.body = body;
		this.blobs = {};
		this.parseBlobs = function() {

			var lines = body.split(/\r?\n/);

			var regexpBlobFunction = /.*\$blob\(\s*(\'[^\']*\')\s*\,\s*(\'[^\']*\')\s*\).*/g;
			var regexpBlobUrl = /.*\$blob\(\s*(\'[^\']*\')\s*\).*/g;

			for (var i = 0; i < lines.length; i++) {

				var result = lines[i].match(regexpBlobFunction);
				if (result != null) {
					var blob = result[0];
					blobs[blob] = blob;
					continue;
				}

				result = lines[i].match(regexpBlobUrl);
				if (result != null) {
					var blob = result[0];
					var url = result[1];
					blobs[blob] = url;
					continue;
				}
			}
		};
	}

	return {
		fetch : function(url) {
			var template = new Template(UrlFetchApp.fetch(base_url + url).toString());
			template.parseBlobs();
			return template;
		}
	};
})();

var Letters = {

	send : function(template) {

	}

};

function test() {
	Logger.log(TemplateApp.fetch('xpto.html'));
}
