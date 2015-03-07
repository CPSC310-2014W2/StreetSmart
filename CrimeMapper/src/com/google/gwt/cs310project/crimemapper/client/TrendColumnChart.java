package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.ColumnChart;
import com.googlecode.gwt.charts.client.corechart.ColumnChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;

public class TrendColumnChart extends DockLayoutPanel {
	private ColumnChart chart;

	public TrendColumnChart() {
		super(Unit.PX);
		initialize();
	}
	
	private void initialize(){
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		//chartLoader.loadApi(new Runnable(){});
	}

}