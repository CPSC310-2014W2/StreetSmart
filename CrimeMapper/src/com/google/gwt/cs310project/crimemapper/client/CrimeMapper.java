package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.client.ui.FlexTable;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.ui.TabLayoutPanel;

import com.google.gwt.user.client.ui.VerticalPanel;





/**

 * Entry point classes define <code>onModuleLoad()</code>.

 */

public class CrimeMapper implements EntryPoint {



	// Create a tab panel with three tabs, each of which displays a different piece of text

	private TabLayoutPanel mainPanel = new TabLayoutPanel(4, Unit.EM);

	private VerticalPanel tableVPanel = new VerticalPanel(); //holds flex table, reset panel

	private FlexTable crimeFlexTable = new FlexTable();

	private HorizontalPanel resetPanel = new HorizontalPanel();

	private Label lastUploadedDateLabel = new Label();

	private Button clearTrendsButton = new Button("Clear Trends");


	/**

	 * Entry point method.

	 */

	public void onModuleLoad() {


		// Create table for crime data.

		crimeFlexTable.setText(0, 0, "Crime Type");

		crimeFlexTable.setText(0, 1, "Mischief under $5000");

		crimeFlexTable.setText(0, 2, "Mischief over $5000");

		crimeFlexTable.setText(0, 3, "Theft from auto under $5000");

		crimeFlexTable.setText(0, 4, "Theft from auto over $5000");

		crimeFlexTable.setText(0, 5, "Theft of auto under $5000");

		crimeFlexTable.setText(0, 6, "Theft of auto over $5000");

		crimeFlexTable.setText(0, 7, "Commercial break and enter");


		// Assemble resetPanel.

		resetPanel.add(clearTrendsButton);


		// Create tab to hold Table, Map and Settings

		mainPanel.add(new HTML("Table"), "[Table]");

		mainPanel.add(new HTML("Map"), "[Map]");

		mainPanel.add(new HTML("Settings"), "[Settings]");


		// Assemble Table Panel

		tableVPanel.add(crimeFlexTable);

		tableVPanel.add(resetPanel);

		tableVPanel.add(lastUploadedDateLabel);



		// Assemble Main panel
		// TODO Needs proper implementation
		
		//mainPanel.add(tableVPanel, mainPanel.getTabWidget(0));
		
		// Associate the Main panel with the HTML host page

		RootPanel.get("crimeList").add(tableVPanel);

	}


}


