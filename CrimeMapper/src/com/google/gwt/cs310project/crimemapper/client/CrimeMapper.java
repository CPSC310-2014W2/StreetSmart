package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
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
	private VerticalPanel mainPanel = new VerticalPanel();
	private TabPanel tabPanel = new TabPanel();
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
		RootPanel.get("crimeList").add(buildMainPanel());
	}
	
	/**
	 * Method for constructing Main Panel
	 */
	private Panel buildMainPanel(){
		mainPanel.add(buildTabPanel());
		
		return mainPanel;
	}
	
	/**
	 * Method for constructing Tab Panel
	 */
	private TabPanel buildTabPanel(){
		
		//Create titles for tabs
		String tab1Title = "Trends";
		String tab2Title = "Map";
		String tab3Title = "Settings";

		// Create tab to hold Table, Map and Settings
		// Assemble mainPanel
		tabPanel.add(buildTableTabPanel(), tab1Title);
		tabPanel.add(buildMapTabPanel(), tab2Title);
		tabPanel.add(buildSettingsTabPanel(), tab3Title);
		
		// first tab upon load
		tabPanel.selectTab(0);

		// set width of mainPanel
		tabPanel.setWidth("1600");
		tabPanel.setHeight("1600");
		// TODO Refactor to get rid of magic number

		return tabPanel;
	}
	
	/**
	 * Method for constructing Table Tab Panel 
	 *  - style elements for table
	 */
	private Panel buildTableTabPanel(){
		
		// Create table and table headers for crime data.
		crimeFlexTable.setText(1, 0, "Year");
		crimeFlexTable.setText(0, 1, "Crime Type");
		crimeFlexTable.setText(1, 1, "Mischief under $5000");
		crimeFlexTable.setText(1, 2, "Mischief over $5000");
		crimeFlexTable.setText(1, 3, "Theft from auto under $5000");
		crimeFlexTable.setText(1, 4, "Theft from auto over $5000");
		crimeFlexTable.setText(1, 5, "Theft of auto under $5000");
		crimeFlexTable.setText(1, 6, "Theft of auto over $5000");
		crimeFlexTable.setText(1, 7, "Commercial break and enter");
		// TODO Possibly use enum to denote crime types and
		// a loop to automatically add all crime types to table
		
		// Merging Crime Type to be over the Crime Types
		FlexCellFormatter crimeTypeCellFormatter = crimeFlexTable.getFlexCellFormatter();
		crimeTypeCellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		crimeTypeCellFormatter.setColSpan(0, 1, 7);
		
		// Add styles to elements in the crime type table
		crimeFlexTable.addStyleName("crimeData");
		crimeFlexTable.getCellFormatter().addStyleName(0, 0, "crimeTypeHeader");
		crimeFlexTable.getCellFormatter().addStyleName(0, 1, "crimeTypeHeader");
		crimeFlexTable.getCellFormatter().addStyleName(1, 0, "crimeTypeHeader");
		int i = 1;
		while(i < 8){
			// TODO Possibly refactor to get rid of magic number and
			// use the size of the enum of crime types
			crimeFlexTable.getCellFormatter().addStyleName(1, i, "crimeTypeHeaderTitles");
			i++;
		}
		crimeFlexTable.setCellPadding(10);

		
		// Assemble resetPanel.
		clearTrendsButtonPanel.add(clearTrendsButton);

		// Date label
		lastUploadedDateLabel.setText("MOST RECENT UPDATE DATE GOES HERE");
		
		
		tableVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

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
		// TODO Refactor to get rid of magic number

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
		// TODO Refactor to get rid of magic number
		
		// Assemble Settings Panel to insert Settings 
		settingsVPanel.add(settingsLabel);
		settingsVPanel.add(loadCrimeDataButton);
		
		return settingsVPanel;
	}
	
	/**
	 * Add crimedata to FlexTable 
	 * Added when admin clicks add new data
	 * 
	 */
}


