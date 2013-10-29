var Tests = (function() {

	function xpto() {
		var data = Charts.newDataTable().addColumn(Charts.ColumnType.STRING,
				"Month").addColumn(Charts.ColumnType.NUMBER, "In Store")
				.addColumn(Charts.ColumnType.NUMBER, "Online").addRow(
						[ "January", 10, 1 ]).addRow([ "February", 12, 1 ])
				.addRow([ "March", 20, 2 ]).addRow([ "April", 25, 3 ]).addRow(
						[ "May", 30, 4 ]).build();

		var chart = Charts.newAreaChart().setDataTable(data).setStacked()
				.setRange(0, 40).setTitle("Sales per Month").build();
		return chart;
	}

	return {
		testSimpleLetter : function() {
			Letters.init(xpto);
			Letters.send('fernando@dextra-sw.com', 'xpto.html');
		}
	};

})();

function run() {
	Tests.testSimpleLetter();
}