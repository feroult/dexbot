function doGet(request) {
	var dash = request.parameters.dash;

	if (dash == null) {
		return ContentService.createTextOutput('Use: ?dash=dashboard_id');
	}

	var template = HtmlService.createTemplateFromFile(dash + '_template');
	return template.evaluate().setSandboxMode(HtmlService.SandboxMode.NATIVE); 
}

function entressafras() {
	var sheet = SpreadsheetApp.openById('0AiEZezeUULf3dFJ6cnEtN3NTYjhIVGgtQW9PdHByVlE').getSheetByName('equipe');
	var range = sheet.getRange(2, 1, sheet.getLastRow() - 1, 2);
	return DexbotUtils.shuffle(range.getValues());
}

function includeJS(output) {	
	output.append('window.x = function() { window.alert(\'oi\'); }');
}