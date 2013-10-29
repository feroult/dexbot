var Letters = {

	init : function() {
		for (var i = 0; i < arguments.length; i++) {
			Blobs.register(arguments[i].name, arguments[i]);
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
