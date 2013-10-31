function doGet(request) {
	var dash = request.parameters.dash;

	if (dash == null) {
		return ContentService.createTextOutput('Use: ?dash=dashboard_id');
	}

	return HtmlService.createTemplateFromFile(dash + '_template').evaluate();
}

function entressafras() {
	var sheet = SpreadsheetApp.openById('0AiEZezeUULf3dFJ6cnEtN3NTYjhIVGgtQW9PdHByVlE').getSheetByName('equipe');	
	var range = sheet.getRange(2, 1, sheet.getLastRow()-1, 2);
	return DexbotUtils.shuffle(range.getValues());	
}