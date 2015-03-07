package com.google.gwt.cs310project.crimemapper.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CrimeMapper implements EntryPoint {

	// Constants
	private static final int YEAR_COLUMN = 0;
	private static final int COLUMN_COUNT = 8;
	private static final int START_OF_DATA_ROWS = 2;
	private static final int NO_TABLE_SELECTION_FLAG = -1;
	
	// Dynamic Panels
	private TabPanel tabPanel = new TabPanel();
	private StackPanel faqPanel = new StackPanel();

	// Static Panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel tableVPanel = new VerticalPanel();
	private VerticalPanel settingsVPanel = new VerticalPanel();
	private VerticalPanel mapsVPanel = new VerticalPanel();
	private HorizontalPanel clearTrendsButtonPanel = new HorizontalPanel();

	// Dimensions and Spacing
	private final String width = "100%";
	private final String height = "100%";
	private final int spacing = 20;

	// Table Tab elements
	private FlexTable crimeFlexTable = new FlexTable();
	private Button clearTrendsButton = new Button("Clear Trends");
	private Label lastUploadedDateLabel = new Label();
	private Label selectedYearLabel = new Label();
	private int selectedRow;
	

	// Settings Tab elements
	private Button loadCrimeDataButton = new Button("Load Data");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox newUrlTextBox = new SuggestBox(oracle);
	private final int CLEAR_TEXT_BOX_FLAG = -1;
	private int selectedTextBox = CLEAR_TEXT_BOX_FLAG;
	private Label settingsLabel = new Label("");
	
	// CrimeData RPC fields
	private CrimeDataServiceAsync crimeDataSvc = GWT.create(CrimeDataService.class);

	// Login Fields
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Account to access the CrimeMapper.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	// Databases 
	private TreeMap<Integer, CrimeDataByYear> crimeDataMap = new TreeMap<Integer, CrimeDataByYear>();

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
		newUrlTextBox.getValueBox().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(selectedTextBox == CLEAR_TEXT_BOX_FLAG){
					settingsLabel.setText("");
					newUrlTextBox.setText("");
					selectedTextBox = 0;
				} 
			}
		});

		// Clear Text box when mouse places icon
		newUrlTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					loadCrime();
				}
			}
		});

		// Listen for mouse events on Load btn
		loadCrimeDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadCrime();
			}
		});

		// Listen for mouse click on the Rows in table and Highlight row.
		crimeFlexTable.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Cell cell = crimeFlexTable.getCellForEvent(event);
				int rowIndex = cell.getRowIndex();
				selectRow(rowIndex);
			}
		});

	}

	@SuppressWarnings("deprecation")
	private void loadCrime(){
		String crimeURL = newUrlTextBox.getText().trim();
		oracle.add(crimeURL);
		refreshCrimeList(crimeURL);
		newUrlTextBox.setText("Paste Crime URL here");
		selectedTextBox = CLEAR_TEXT_BOX_FLAG;
		lastUploadedDateLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}

	private void selectRow(int rowIndex){
		if (rowIndex == selectedRow)
		{
			crimeFlexTable.getRowFormatter().setStyleName(rowIndex, "rowUnselectedShadow");
			selectedYearLabel.setText("");
			selectedRow = NO_TABLE_SELECTION_FLAG;
		} else {
			int row = crimeFlexTable.getRowCount();
			int i = START_OF_DATA_ROWS;
			while(i < row){
				if(i == rowIndex){
					crimeFlexTable.getRowFormatter().setStyleName(rowIndex, "rowSelectedShadow");
					selectedYearLabel.setText(""+getYearFromTable(rowIndex));
				} else {
					crimeFlexTable.getRowFormatter().setStyleName(i, "rowUnselectedShadow");
				}
				i++;
			}
			// TODO Trends method
			updateTableTrends(rowIndex);
			selectedRow = rowIndex;
		}
	}
	/**
	 * Method for constructing Main Panel
	 */
	private Panel buildMainPanel(){

		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSize(width, height);
		mainPanel.add(buildTabPanel());

		return mainPanel;
	}

	/**
	 * Method for constructing Tab Panel
	 */
	private TabPanel buildTabPanel(){

		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize(width, height);

		//Create titles for tabs
		String tab1Title = "Trends";
		String tab2Title = "Map";
		String tab3Title = "FAQ";
		String tab4Title = "Admin";


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
		flowpanel.add(buildFaqTabPanel());
		tabPanel.add(flowpanel, tab3Title);

		flowpanel = new FlowPanel();
		flowpanel.add(buildSettingsTabPanel());
		tabPanel.add(flowpanel, tab4Title);

		// first tab upon load
		tabPanel.selectTab(0);
		return tabPanel;
	}

	/**
	 * Method for constructing Table Tab Panel 
	 *  - style elements for table
	 */
	@SuppressWarnings("deprecation")
	private Panel buildTableTabPanel(){
		tableVPanel.setSize(width, height);
		tableVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		tableVPanel.setSpacing(spacing);

		// Create table and table headers for crime data.
		crimeFlexTable.setText(1, 0, "Year");
		crimeFlexTable.setText(0, 1, "Crime Type");
		for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
			crimeFlexTable.setText(1, i + 1, CrimeTypes.getType(i));
		}

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
		lastUploadedDateLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

		// Assemble Table Panel to insert in Tab1 of Tab Panel
		tableVPanel.add(selectedYearLabel);
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
		mapsVPanel.setSize(width, height);
		mapsVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mapsVPanel.setSpacing(spacing);
		// Assemble elements for Map Panel
		Label mapLabel = new Label("MAP WILL GO HERE");
		Image dummyMap = new Image("images/vancouver-dummy-map.jpg");

		// Assemble Map Panel to insert map label/image
		mapsVPanel.add(mapLabel);
		mapsVPanel.add(dummyMap);

		return mapsVPanel;
	}

	/**
	 * Method for Constructing Settings tab panel
	 */
	private Panel buildSettingsTabPanel(){

		settingsVPanel.setSize(width, height);
		settingsVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		settingsVPanel.setSpacing(spacing);
		// Assemble elements for Settings Panel
		

		// Assemble Settings Panel to insert Settings 
		settingsVPanel.add(settingsLabel);
		newUrlTextBox.setText("Paste Crime URL here");
		settingsVPanel.add(newUrlTextBox);
		settingsVPanel.add(loadCrimeDataButton);

		return settingsVPanel;
	}

	private Panel buildFaqTabPanel(){
		// Application facts
		String appFact1 = "The Vancouver Police Department (VPD) has changed the way in "
				+ "which it reports its crime statistics. Historically, it reported data "
				+ "based on Statistics Canada reporting requirements, which meant that "
				+ "only the most serious offence per incident was counted. Now, the all "
				+ "violations method is used. Other policing agencies like Edmonton, "
				+ "Toronto, Ottawa and Calgary also present their crime statistics using "
				+ "the all violations method. It is important to note these differences "
				+ "in reporting when comparing our crime statistics to other Police "
				+ "Agencies and Statistics Canada.";
		String appFact2 = "Fact 2";
		String appFact3 = "Fact 3";

		// Crime Types
		ArrayList<String> explanations = new ArrayList<String>();
		explanations.add("Explanation 1");
		explanations.add("Explanation 2");
		explanations.add("Explanation 3");
		explanations.add("Explanation 4");
		explanations.add("Explanation 5");
		explanations.add("Explanation 6");
		explanations.add("Explanation 7");

		faqPanel.setSize(width,height);

		Label label;

		// Application Facts
		label = new Label(appFact1);
		faqPanel.add(label, "Comparing Crime Statistics", false);

		label = new Label(appFact2);
		faqPanel.add(label, "App Fact2", false);

		label = new Label(appFact3);
		faqPanel.add(label, "App Fact3", false);

		String whatIs = "What is ";

		// Crime facts
		for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
			label = new Label(explanations.get(i));
			faqPanel.add(label, whatIs + CrimeTypes.getType(i), false);
		}

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
		AsyncCallback<CrimeDataByYear> callback = new AsyncCallback<CrimeDataByYear>(){
			public void onFailure(Throwable caught){
				//TODO: Do something with errors.
			}

			public void onSuccess(CrimeDataByYear result) {
				if(!(result.getYear() == 0)){
					settingsLabel.setText("Data Loaded Successfully");
					addCrimeDataSet(result);
				} else {
					settingsLabel.setText("Seems Like an Error Loading Data");
				}
			}
		}; 

		// Make the call to the crime data service.
		crimeDataSvc.getCrimeDataByYear(crimeURL, callback);
	}

	private void addCrimeDataSet(CrimeDataByYear result) {
		// TODO Insert Persistent Method for DataStore
		crimeDataMap.put(result.getYear(), result);
		updateTableView(crimeDataMap);
	}

	private void updateTableView(TreeMap<Integer, CrimeDataByYear> crimeDataMap2) {

		// remove rows to reload new data
		refreshRows(crimeFlexTable.getRowCount());

		for (Map.Entry<Integer, CrimeDataByYear> entry: crimeDataMap2.entrySet()){
			int row = crimeFlexTable.getRowCount();
			CrimeDataByYear value = entry.getValue(); 
			crimeFlexTable.setText(row, 0, value.yearToString());
			int i = 1;
			while(i < COLUMN_COUNT){
				int crimeOccurences = value.getNumberOfCrimeTypeOccurrences(CrimeTypes.getType(i-1));
				crimeFlexTable.setText(row, i, ""+crimeOccurences);
				i++;
			}
		}
	}

	/**
	 * 
	 * @param rowCount
	 */
	private void refreshRows(int rowCount) {
		int n = START_OF_DATA_ROWS;
		while(rowCount > n){
			crimeFlexTable.removeRow(rowCount-1);
			rowCount--;
		}
	}

	private int getYearFromTable(int index){
		int year = 0;
		year = Integer.parseInt(crimeFlexTable.getText(index, YEAR_COLUMN));
		return year;
	}
	
	/**
	 * Update table view with trends labels
	 * @param receiverRowIndex
	 */
	private void updateTableTrends(int rowIndex) {

	}

}


