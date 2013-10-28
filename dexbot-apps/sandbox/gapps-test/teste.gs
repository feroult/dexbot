function doGetX() {
	return HtmlService.createTemplateFromFile('template').evaluate();
}

function doGet() {
	var chart = getChart();
	var image = getImage();
	
	sendMail(image);

	var uiApp = UiApp.createApplication().setTitle("My Chart");
	uiApp.add(chart);
	return uiApp;

}

function getImage() {
	return UrlFetchApp.fetch("https://docs.google.com/a/dextra-sw.com/spreadsheet/oimg?key=0AiEZezeUULf3dGp4NUlnRnp3RVh6dEdpX3cxTFFFN1E&oid=1&zx=efkcv480nfy0").getBlob();
}

function getChartFromSpreadsheet() {
   var sheet = SpreadsheetApp.openById(
			'0AiEZezeUULf3dGp4NUlnRnp3RVh6dEdpX3cxTFFFN1E').getSheetByName('x').getName();
  
	var charts = SpreadsheetApp.openById(
			'0AiEZezeUULf3dGp4NUlnRnp3RVh6dEdpX3cxTFFFN1E').getSheetByName('x')
			.getCharts();

	return charts[0];
}

function sendMail(blob) {
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

function getChart() {
	var data = Charts.newDataTable().addColumn(Charts.ColumnType.STRING,
			"Month").addColumn(Charts.ColumnType.NUMBER, "In Store").addColumn(
			Charts.ColumnType.NUMBER, "Online").addRow([ "January", 10, 1 ])
			.addRow([ "February", 12, 1 ]).addRow([ "March", 20, 2 ]).addRow(
					[ "April", 25, 3 ]).addRow([ "May", 30, 4 ]).build();

	var chart = Charts.newAreaChart().setDataTable(data).setStacked().setRange(
			0, 40).setTitle("Sales per Month").build();
	return chart;
}
