var Letters = {

	init : function() {
		for (var i = 0; i < arguments.length; i++) {
			Blobs.register(arguments[i]);
		}
	},

	send : function(to, templateUrl) {
		var template = Templates.fetch(templateUrl);

		MailApp.sendEmail({
			to : to,
			subject : "Test Mail",
			htmlBody : template.body,
			inlineImages : template.blobs
		});

	}
}

function testSimpleLetter() {
	Letters.init(mockSimpleBlob);
	Letters.send('fernando@dextra-sw.com', 'xpto.html');
}