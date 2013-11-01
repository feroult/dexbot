var Letters = {

	init : function() {
		for (var i = 0; i < arguments.length; i++) {
			Blobs.register(arguments[i]);
		}
	},

	send : function(to, subject, templateUrl) {
		var template = Templates.parse(templateUrl);

		MailApp.sendEmail({
			to : to,
			subject : subject,
			htmlBody : template.parsed,
			inlineImages : template.blobs
		});

	}
}

function testSimpleLetter() {
	Letters.init(mockSimpleBlob);
	Letters.send('fernando@dextra-sw.com', 'Test Mail', 'docs://1QIKt9i8br8_UFwt19YpCBuwh_XgB9fXWGcCl8rH1OF8');
}

function testPersistLetter() {
	
}