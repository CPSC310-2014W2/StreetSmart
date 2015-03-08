package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;
import java.util.TreeMap;

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
	private TreeMap<Integer, CrimeDataByYear> crimeDataMap;

	public TrendColumnChart(TreeMap<Integer, CrimeDataByYear> crimeDataMap) {
		super(Unit.PX);
		this.crimeDataMap = crimeDataMap;
		initialize();
	}

	private void initialize(){
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartLoader.loadApi(new Runnable(){

			@Override
			public void run() {
				// Create and attach the chart for Crime Data
				chart = new ColumnChart();
				add(chart);
				draw();

			}});	
	}

	private void draw(){
		// crime type legend
		String[] crimeTypes = new String[CrimeTypes.getNumberOfTypes()];
		int numOfCrimeTypes = 0;
		for (; numOfCrimeTypes < CrimeTypes.getNumberOfTypes(); numOfCrimeTypes++){
			crimeTypes[numOfCrimeTypes] = CrimeTypes.getType(numOfCrimeTypes);
		}

		// Year Axis
		int numOfYears = 0;
		int[] years = new int[crimeDataMap.size()];
		for (Integer key: crimeDataMap.keySet()){
			years[numOfYears] = key;
			numOfYears++;
		}

		int[][] values = new int[numOfYears][CrimeTypes.getNumberOfTypes()];
		ArrayList<CrimeDataByYear> crimesArr = new ArrayList<CrimeDataByYear>();

		for (CrimeDataByYear crimes: crimeDataMap.values()){
			crimesArr.add(crimes);
		}

		for (int r = 0; r < numOfYears; r++){
			for (int c = 0; c < CrimeTypes.getNumberOfTypes(); c++){
				values[r][c] = crimesArr.get(r).getNumberOfCrimeTypeOccurrences(CrimeTypes.getType(c));
			}
		}

		// Prepare crime data for columns
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Year");
		for (int i = 0; i < crimeTypes.length; i++){
			dataTable.addColumn(ColumnType.NUMBER, crimeTypes[i]);
		}

		dataTable.addRows(years.length);
		for (int i = 0; i < years.length; i++){
			dataTable.setValue(i, 0, String.valueOf(years[i]));
		}

		for (int col = 0; col < values.length; col++){
			for (int row = 0; row < values[col].length; row++){
				dataTable.setValue(row, col + 1, values[col][row]);
			}
		}

		// Options
		ColumnChartOptions options = ColumnChartOptions.create();
		options.setFontName("Tahoma");
		options.setTitle("Yearly Occurrences of Crime by Crime Type");
		options.setHAxis(HAxis.create("Year"));
		options.setVAxis(VAxis.create("Number of Crimes"));

		// Draw this chart
		chart.draw(dataTable, options);
	}
}