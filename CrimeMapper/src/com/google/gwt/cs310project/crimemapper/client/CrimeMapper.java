package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CrimeMapper implements EntryPoint {

	// Dynamic Panels
	private TabPanel tabPanel = new TabPanel();
	private StackPanel faqPanel = new StackPanel();
	
	// Static Panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel tableVPanel = new VerticalPanel(); // holds flex table, reset panel
	private VerticalPanel settingsVPanel = new VerticalPanel();
	private VerticalPanel mapsVPanel = new VerticalPanel();
	private HorizontalPanel clearTrendsButtonPanel = new HorizontalPanel();
	
	// Table features
	private FlexTable crimeFlexTable = new FlexTable();
	private Button clearTrendsButton = new Button("Clear Trends");
	private Label lastUploadedDateLabel = new Label();
	private final int COLUMN_COUNT = 8;
	private int selectedRow;
	
	private Button loadCrimeDataButton = new Button("Load Data");
	
	// Settings Text Box flags
	private TextBox newUrlTextBox = new TextBox();
	private final int NO_TABLE_SELECTION_FLAG = -1;
	private final int CLEAR_TEXT_BOX_FLAG = -1;
	private int selectedTextBox = CLEAR_TEXT_BOX_FLAG;
	
	//Crime Types
	private final String crime1 = "Mischief Under $5000";
	private final String crime2 = "Mischief Over $5000";
	private final String crime3 = "Theft From Auto Under $5000";
	private final String crime4 = "Theft From Auto Over $5000";
	private final String crime5 = "Theft Of Auto Under $5000";
	private final String crime6 = "Theft Of Auto Over $5000";
	private final String crime7 = "Commercial Break and Enter";

	//CrimeData RPC fields
	private ArrayList<CrimeData> crimes = new ArrayList<CrimeData>();
	private CrimeDataServiceAsync crimeDataSvc = GWT.create(CrimeDataService.class);
	
	
	//Login Fields
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Account to access the CrimeMapper.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	/**
	 * Entry point method.
	 */

	public void onModuleLoad() {
		
		// Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          loadMainPanel();
	        } else {
	          loadLogin();
	        }
	      }
	    });
	}
	
	
	private void loadMainPanel(){
		
		signOutLink.setHref(loginInfo.getLogoutUrl());
		applicationHandlers();
		// Associate the Main panel with the HTML host page
		RootPanel.get("crimeList").add(buildMainPanel());
	}
	
	private void loadLogin(){
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    RootPanel.get("crimeList").add(loginPanel);
	}

	private void applicationHandlers(){
		// Clear Text box when mouse places icon
		newUrlTextBox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(selectedTextBox == CLEAR_TEXT_BOX_FLAG){
					newUrlTextBox.setText("");
					selectedTextBox = 0;
					} 
			}
		});

		// Listen for mouse events on the Add button.
		loadCrimeDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String crimeURL = newUrlTextBox.getText();
				refreshCrimeList(crimeURL);
				newUrlTextBox.setText("Paste Crime URL here");
				selectedTextBox = CLEAR_TEXT_BOX_FLAG;
			}
		});

		// Listen for mouse click on the Rows in table and Highlight row.
		crimeFlexTable.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Cell cell = crimeFlexTable.getCellForEvent(event);
				int receiverRowIndex = cell.getRowIndex();
				if (receiverRowIndex == selectedRow)
				{
					crimeFlexTable.getRowFormatter().setStyleName(receiverRowIndex, "rowUnselectedShadow");
					selectedRow = NO_TABLE_SELECTION_FLAG;
				} else {
					crimeFlexTable.getRowFormatter().setStyleName(receiverRowIndex, "rowSelectedShadow");
					// TODO Trends method
					updateTableTrends(receiverRowIndex);
					selectedRow = receiverRowIndex;
				}
			}
		});
	}
	
	/**
	 * Update table view with trends labels
	 * @param receiverRowIndex
	 */
	protected void updateTableTrends(int receiverRowIndex) {
		// TODO Auto-generated method stub
		
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

		tabPanel.setAnimationEnabled(true);

		//Create titles for tabs
		String tab1Title = "Trends";
		String tab2Title = "Map";
		String tab3Title = "Settings";
		String tab4Title = "FAQ";

		//Create Custom FlowPanels to add to TabPanel
		FlowPanel flowpanel;
		// Create tab to hold Table, Map and Settings
		// Assemble mainPanel
		flowpanel = new FlowPanel();
		flowpanel.add(buildTableTabPanel());
		tabPanel.add(flowpanel, tab1Title);

		flowpanel = new FlowPanel();
		flowpanel.add(buildMapTabPanel());
		tabPanel.add(flowpanel, tab2Title);

		flowpanel = new FlowPanel();
		flowpanel.add(buildSettingsTabPanel());
		tabPanel.add(flowpanel, tab3Title);

		flowpanel = new FlowPanel();
		flowpanel.add(buildFaqTabPanel());
		tabPanel.add(flowpanel, tab4Title);

		// first tab upon load
		tabPanel.selectTab(0);
		tabPanel.setSize("1200px", "1200px");


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
		crimeFlexTable.setText(1, 1, crime1);
		crimeFlexTable.setText(1, 2, crime2);
		crimeFlexTable.setText(1, 3, crime3);
		crimeFlexTable.setText(1, 4, crime4);
		crimeFlexTable.setText(1, 5, crime5);
		crimeFlexTable.setText(1, 6, crime6);
		crimeFlexTable.setText(1, 7, crime7);

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
		while(i < COLUMN_COUNT){
			// TODO Possibly refactor to get rid of magic number and
			// use the size of the enum of crime types
			crimeFlexTable.getCellFormatter().addStyleName(1, i, "crimeTypeHeaderTitles");
			i++;
		}
		crimeFlexTable.setCellPadding(20);


		// Assemble resetPanel.
		clearTrendsButtonPanel.add(clearTrendsButton);
		

		// Date label
		lastUploadedDateLabel.setText("MOST RECENT UPDATE DATE GOES HERE");


		tableVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		// Assemble Table Panel to insert in Tab1 of Tab Panel
		tableVPanel.add(crimeFlexTable);
		tableVPanel.add(clearTrendsButtonPanel);
		tableVPanel.add(lastUploadedDateLabel);
		tableVPanel.add(signOutLink);


		// return table constructed panel
		return tableVPanel;
	}

	/**
	 *  Method for Constructing Map tab panel
	 */
	private Panel buildMapTabPanel(){
		// Assemble elements for Map Panel
		Label mapLabel = new Label("MAP WILL GO HERE");
		Image dummyMap = new Image("images/vancouver-dummy-map.jpg");
		
		mapsVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// Assemble Map Panel to insert map label/image
		mapsVPanel.add(mapLabel);
		mapsVPanel.add(dummyMap);
		
		return mapsVPanel;
	}

	/**
	 * Method for Constructing Settings tab panel
	 */
	private Panel buildSettingsTabPanel(){
		// Assemble elements for Settings Panel
		Label settingsLabel = new Label("SETTINGS WILL GO HERE");

		// Assemble Settings Panel to insert Settings 
		settingsVPanel.add(settingsLabel);
		newUrlTextBox.setText("Paste Crime URL here");
		settingsVPanel.add(newUrlTextBox);
		settingsVPanel.add(loadCrimeDataButton);

		return settingsVPanel;
	}

	private Panel buildFaqTabPanel(){
		// Application facts
		String appFact1 = "Fact 1";
		String appFact2 = "Fact 2";
		String appFact3 = "Fact 3";

		// Crime Types
		String cr1 = "Explanation 1";
		String cr2 = "Explanation 2";
		String cr3 = "Explanation 3";
		String cr4 = "Explanation 4";
		String cr5 = "Explanation 5";
		String cr6 = "Explanation 6";
		String cr7 = "Explanation 7";

		Label label;

		//Application Facts
		label = new Label(appFact1);
		faqPanel.add(label, "App Fact1", false);

		label = new Label(appFact2);
		faqPanel.add(label, "App Fact2", false);

		label = new Label(appFact3);
		faqPanel.add(label, "App Fact3", false);

		//Crime facts
		label = new Label(cr1);
		faqPanel.add(label, crime1, false);

		label = new Label(cr2);
		faqPanel.add(label, crime2, false);

		label = new Label(cr3);
		faqPanel.add(label, crime3, false);

		label = new Label(cr4);
		faqPanel.add(label, crime4, false);

		label = new Label(cr5);
		faqPanel.add(label, crime5, false);

		label = new Label(cr6);
		faqPanel.add(label, crime6, false);

		label = new Label(cr7);
		faqPanel.add(label, crime7, false);

		faqPanel.setSize("100%","100%");
		return faqPanel;
	}

	/**
	 * Add crimedata to FlexTable 
	 * Added when admin clicks add new data
	 * 
	 */
	private void refreshCrimeList(String crimeURL){
		//Initialize the service proxy.
		if(crimeDataSvc == null){
			crimeDataSvc = GWT.create(CrimeDataService.class);
		}
		
				
		// Set up the callback object.
		// Make the call to the crime data service.
		AsyncCallback<ArrayList<CrimeData>> callback = new AsyncCallback<ArrayList<CrimeData>>(){
			public void onFailure(Throwable caught){
				//TODO: Do something with errors.
			}
			
			public void onSuccess(ArrayList<CrimeData> result){
				loadCrimeDataSet(result);
			}
		}; 
		
		crimeDataSvc.getCrimeData(crimeURL, callback);
	}

	/**
	 *  Fill table with crime data
	 * @param crimes
	 */
	
	private void loadCrimeDataSet(ArrayList<CrimeData> crimes){
		// Crime ListData
		ArrayList<CrimeData> crime1List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime2List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime3List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime4List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime5List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime6List = new ArrayList<CrimeData>();
		ArrayList<CrimeData> crime7List = new ArrayList<CrimeData>();
		
		ArrayList<ArrayList<CrimeData>> crimeList = new ArrayList<ArrayList<CrimeData>>();
		int year = crimes.get(0).getYear();
		
		for (CrimeData crime: crimes){
			
			String crimeType = crime.getType();
	        switch (crimeType) {
	            case crime1:  crime1List.add(crime);
	            case crime2:  crime2List.add(crime);
	            case crime3:  crime3List.add(crime);
	            case crime4:  crime4List.add(crime);
	            case crime5:  crime5List.add(crime);
	            case crime6:  crime6List.add(crime);
	            case crime7:  crime7List.add(crime);
	                     break;
	            default: break;
	        }
		}
		
		crimeList.add(crime1List);
		crimeList.add(crime2List);
		crimeList.add(crime3List);
		crimeList.add(crime4List);
		crimeList.add(crime5List);
		crimeList.add(crime6List);
		crimeList.add(crime7List);
		
		updateTableView(crimeList, year);
	}

	// Inserting Data into table
	private void updateTableView(ArrayList<ArrayList<CrimeData>> crimeList, int yr) {
		String year = "" + yr;
		int row = crimeFlexTable.getRowCount();
		crimeFlexTable.setText(row, 0, year);
		int i = 1;
		while(i < COLUMN_COUNT){
			crimeFlexTable.setText(row, i, "" + crimeList.get(i).size());
			i++;
		}
		
	}
}


