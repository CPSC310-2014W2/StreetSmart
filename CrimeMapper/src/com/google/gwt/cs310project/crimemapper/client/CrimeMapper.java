package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CrimeMapper implements EntryPoint {

	// Create a tab panel with three tabs, each of which displays a different piece of text
	private TabPanel mainPanel = new TabPanel();
	private VerticalPanel tableVPanel = new VerticalPanel(); // holds flex table, reset panel
	private VerticalPanel settingsVPanel = new VerticalPanel();
	private VerticalPanel mapsVPanel = new VerticalPanel();
	private FlexTable crimeFlexTable = new FlexTable();
	private HorizontalPanel clearTrendsButtonPanel = new HorizontalPanel();
	private Label lastUploadedDateLabel = new Label();
	private Button clearTrendsButton = new Button("Clear Trends");
	private Button loadCrimeDataButton = new Button("Load Data");
	
	/**
	 * Entry point method.
	 */
	
	public void onModuleLoad() {
		
		// Associate the Main panel with the HTML host page
		RootPanel.get("crimeList").add(buildMainTabPanel());
	}
	
	/**
	 * Method for constructing Main Panel
	 */
	private TabPanel buildMainTabPanel(){
		
		//Create titles for tabs
		String tab1Title = "Trends";
		String tab2Title = "Map";
		String tab3Title = "Settings";

		// Create tab to hold Table, Map and Settings
		// Assemble mainPanel
		mainPanel.add(buildTableTabPanel(), tab1Title);
		mainPanel.add(buildMapTabPanel(), tab2Title);
		mainPanel.add(buildSettingsTabPanel(), tab3Title);

		// first tab upon load
		mainPanel.selectTab(0);

		// set width of mainPanel
		mainPanel.setWidth("800");
		mainPanel.setHeight("800");


		return mainPanel;
	}
	
	/**
	 * Method for constructing Table Tab Panel
	 * 
	 */
	private Panel buildTableTabPanel(){
		
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
		clearTrendsButtonPanel.add(clearTrendsButton);

		// Date label
		lastUploadedDateLabel.setText("DATE GOES HERE");

		// Assemble Table Panel to insert in Tab1 of Tab Panel
		tableVPanel.add(crimeFlexTable);
		tableVPanel.add(clearTrendsButtonPanel);
		tableVPanel.add(lastUploadedDateLabel);
		
		// return table constructed panel
		return tableVPanel;

	}
	
	/**
	 *  Method for Constructing Map tab panel
	 */
	private Panel buildMapTabPanel(){
		// Assemble elements for Map Panel
		Label mapLabel = new Label("MAP WILL GO HERE");
		mapLabel.setWidth("800");

		// Assemble Map Panel to insert map label
		mapsVPanel.add(mapLabel);

		return mapsVPanel;
	}
	
	/**
	 * Method for Constructing Settings tab panel
	 */
	private Panel buildSettingsTabPanel(){
		// Assemble elements for Settings Panel
		Label settingsLabel = new Label("SETTINGS WILL GO HERE");
		settingsLabel.setWidth("800");
		
		// Assemble Settings Panel to insert Settings 
		settingsVPanel.add(settingsLabel);
		settingsVPanel.add(loadCrimeDataButton);
		
		return settingsVPanel;
	}
	
	/**
	 * Add crimedata to FlexTable, should be added when admin clicks add new data
	 * 
	 */
}


