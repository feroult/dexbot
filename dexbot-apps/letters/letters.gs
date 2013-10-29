var Letters = {

	init : function() {
		for (var i = 0; i < arguments.length; i++) {
			Blobs.register(arguments[i].name, arguments[i]);
		}
	},

	send : function(to, templateUrl) {

		var template = Templates.fetch(templateUrl);
		
		
		
		MailApp
		.sendEmail({
			to : "fernando@dextra-sw.com",
			subject : "Test Mail",
			htmlBody : "inline Google Logo<img src='cid:chartImage'> images! <br>"
					+ "inline YouTube Logo <img src='cid:chartImage'> <br>"
					+ "xxxx <img src='http://www.gravatar.com/avatar/3b174eda6e08157adaa9794294e53702.png?s=200' />",
			inlineImages : {
				chartImage : blob
			}
		});		
		
	}

}
