function doGet(request) {	
	var dash = request.parameters.dash;
	
	if(dash == null) {
		return ContentService.createTextOutput('Use: ?dash=dashboard_id');
	}
	
    return HtmlService
           .createTemplateFromFile(dash + '_template')
           .evaluate();
}