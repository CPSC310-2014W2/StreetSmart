package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CrimeMapper implements EntryPoint {

	// Create a tab panel with three tabs, each of which displays a different piece of text
	private TabPanel mainPanel = new TabPanel();
	private VerticalPanel tableVPanel = new VerticalPanel(); //holds flex table, reset panel
	private FlexTable crimeFlexTable = new FlexTable();
	private HorizontalPanel resetPanel = new HorizontalPanel();
	private Label lastUploadedDateLabel = new Label();
	private Button clearTrendsButton = new Button("Clear Trends");
	
	/**
	 * Entry point method.
	 */
	
	public void onModuleLoad() {

		// Create table and table headers for crime data.
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
		
		// Assemble Table Panel to insert in Tab1 of Tab Panel
		tableVPanel.add(crimeFlexTable);
		tableVPanel.add(resetPanel);
		tableVPanel.add(lastUploadedDateLabel);
		
		// Assemble temporary labels for Map and Settings tab
		Label mapLabel = new Label("Map will go here");
		Label settingsLabel = new Label("Settings will go here");
		
		// Label holders for settings and maps panel
		mapLabel.setHeight("200");
		settingsLabel.setHeight("200");
		
		//Create titles for tabs
		  String tab1 = "Tabel";
	      String tab2 = "Map";
	      String tab3 = "Settings";
	      
		// Create tab to hold Table, Map and Settings
	    // Assemble mainPanel
		mainPanel.add(tableVPanel, tab1);
		mainPanel.add(mapLabel, tab2);
		mainPanel.add(settingsLabel, tab3);

		// first tab upon load
		mainPanel.selectTab(0);
		
		// set width of mainPanel
		mainPanel.setWidth("800");
		mainPanel.setHeight("800");
		
		// Associate the Main panel with the HTML host page
		RootPanel.get("crimeList").add(mainPanel);
	}
}


