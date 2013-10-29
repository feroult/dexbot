var Letters = {

	init : function() {
		for (var i = 0; i < arguments.length; i++) {
			Blobs.register(arguments[i]);
		}
	},

	send : function(to, templateUrl) {
		var template = Templates.parse(templateUrl);

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
	//Letters.send('fernando@dextra-sw.com', '<img src="$blob(\'mockSimpleBlob\')" />');
	Letters.send('fernando@dextra-sw.com', 'docs://1QIKt9i8br8_UFwt19YpCBuwh_XgB9fXWGcCl8rH1OF8');
	
}