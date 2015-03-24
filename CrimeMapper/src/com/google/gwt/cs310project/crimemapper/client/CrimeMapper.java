package com.google.gwt.cs310project.crimemapper.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.PieDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;
import org.xml.sax.SAXException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CrimeMapper implements EntryPoint {

	// Constants
	private static final int YEAR_COLUMN = 0;
	private static final int COLUMN_COUNT = 8;
	private static final int START_OF_DATA_ROWS = 2;
	private static final int START_OF_DATA_COLUMNS = 1;
	private static final int NO_TABLE_SELECTION_FLAG = -1;
	private static final int BASE_YEAR = 2009;
	private static final int NUM_YEARS = 6;
	private static final int PADDING = 7;

	// Dynamic Panels
	private TabPanel tabPanel = new TabPanel();

	// Static Panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel tableVPanel = new VerticalPanel();
	private VerticalPanel settingsVPanel = new VerticalPanel();
	private VerticalPanel mapsVPanel = new VerticalPanel();
	private HorizontalPanel clearTrendsButtonPanel = new HorizontalPanel();
	private VerticalPanel accountVPanel = new VerticalPanel();
	private HorizontalPanel trendsHPanel1 = new HorizontalPanel();
	private VerticalPanel trendsHPanel2 = new VerticalPanel();
	private HorizontalPanel pieChartPanel = new HorizontalPanel();
	private VerticalPanel colChartPanel = new VerticalPanel();
	private VerticalPanel mainTrendsPanel = new VerticalPanel();

	// Data Visualization
	private Chart pieChart = new Chart();



	// Dimensions and Spacing
	private final String WIDTH = "100%";
	private final String HEIGHT = "100%";
	private final int SPACING = 15;

	// Table Tab elements
	private FlexTable crimeFlexTable = new FlexTable();
	private Button clearTrendsButton = new Button("Clear Trends");
	private Label lastUploadedDateLabel = new Label();
	private Label selectedYearLabel = new Label();
	private int selectedRow;
	private int userSelectedRow = NO_TABLE_SELECTION_FLAG;


	// Settings Tab elements
	private Button loadCrimeDataButton = new Button("Load Data");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox newUrlTextBox = new SuggestBox(oracle);
	private final int CLEAR_TEXT_BOX_FLAG = -1;
	private int selectedTextBox = CLEAR_TEXT_BOX_FLAG;
	private Label settingsLabel = new Label("");
	private VerticalPanel localBackupPanel = new VerticalPanel();
	private ListBox localBackupListBox = new ListBox();
	private Label localBackupLabel = new Label("Please choose a file to load from local backup:");
	private Button localBackupAddButton = new Button("Add");
	private Button localBackupRemoveButton = new Button("Remove");
	private Button localBackupCancelButton = new Button("Cancel");

	// CrimeData RPC fields
	private CrimeDataServiceAsync crimeDataSvc = GWT.create(CrimeDataService.class);

	// UserSettings RPC fields
	private UserSettingsServiceAsync userSettingsSvc = GWT.create(UserSettingsService.class);

	// Login Fields
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Account to access the CrimeMapper.");
	private Label failedToRetrieveDataLabel = new Label(
			"Failed to retrieve data from the server. Please try logging in again later.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	LoginServiceAsync loginService = null;

	//Admin Account
	private ListBox localAccountListBox = new ListBox();
	private Button localAccountAddButton = new Button("Add");
	private Button localAccountDelButton = new Button("Delete");
	private List<String> lst;
	private boolean isAdmin = false;
	private TextBox adminTextBox = new TextBox();
	private Label adminLabel = new Label("Admin Account List");


	// Databases 
	private TreeMap<Integer, CrimeDataByYear> crimeDataMap;
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		// Check login status using login service.
		loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {

					lst = loginInfo.getAccountList();
					for (int i = 0; i < lst.size(); i++) {
						if(loginInfo.getEmailAddress().toLowerCase() == (lst.get(i)).toLowerCase()) {
							isAdmin = true;
							break;
						}					
					}

					loadMainPanel();
				} else {
					loadLogin();
				}
			}
		});
	}

	private void loadCrimeDataMap() {
		//Initialize the service proxy.
		if (crimeDataSvc == null) {
			crimeDataSvc = GWT.create(CrimeDataService.class);
		}
		// Set up the callback object.
		AsyncCallback<TreeMap<Integer, CrimeDataByYear>> callback = new AsyncCallback<TreeMap<Integer, CrimeDataByYear>>(){
			public void onFailure(Throwable caught){
				throw new FailedToRetrieveDataException();
			}

			public void onSuccess(TreeMap<Integer, CrimeDataByYear> result) {
				crimeDataMap = result;
				updateTableView(crimeDataMap);
				updateChartViewDStore();
			}
		}; 

		// Make the call to the crime data service.
		crimeDataSvc.getCrimeDataMap(callback);
	}

	private void updateChartViewDStore() {

		Chart colChart = buildYearlyColChart(crimeDataMap);
		colChartPanel.setPixelSize(1600, 400);
		colChartPanel.add(colChart);	
	}

	private void updateCrimeDataMap() {
		//Initialize the service proxy.
		if (crimeDataSvc == null) {
			crimeDataSvc = GWT.create(CrimeDataService.class);
		}
		// Set up the callback object.
		AsyncCallback<Void> callback = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught){
				throw new FailedToRetrieveDataException();
			}

			public void onSuccess(Void result) {}
		}; 

		// Make the call to the crime data service.
		crimeDataSvc.setCrimeDataMap(crimeDataMap, callback);
	}

	private void loadUserSelectedRow() {
		//Initialize the service proxy.
		if (userSettingsSvc == null) {
			userSettingsSvc = GWT.create(UserSettingsService.class);
		}
		// Set up the callback object.
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>(){
			public void onFailure(Throwable caught){
				throw new FailedToRetrieveDataException();
			}

			public void onSuccess(Integer result) {
				userSelectedRow = result;
				if (userSelectedRow != NO_TABLE_SELECTION_FLAG) {
					selectRow(userSelectedRow);
				}
			}
		}; 

		// Make the call to the user settings service.
		userSettingsSvc.getSelectedRow(callback);
	}

	private void updateUserSelectedRow() {
		//Initialize the service proxy.
		if (userSettingsSvc == null) {
			userSettingsSvc = GWT.create(UserSettingsService.class);
		}
		// Set up the callback object.
		AsyncCallback<Void> callback = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught){
				throw new FailedToRetrieveDataException();
			}

			public void onSuccess(Void result) {
				userSelectedRow = selectedRow;
			}
		}; 

		// Make the call to the user settings service.
		userSettingsSvc.setSelectedRow(selectedRow, callback);
	}

	// ===================================================================================== //
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
	// ===================================================================================== //
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

		// Listen for mouse events on Clear Trends btn
		clearTrendsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearTrends();
				selectedYearLabel.setText("");
				selectedYearLabel.setStyleName("UnSelectedYearLabelStyle");
				selectedRow = NO_TABLE_SELECTION_FLAG;
				int row = crimeFlexTable.getRowCount();
				int i = START_OF_DATA_ROWS;
				while(i < row){
					crimeFlexTable.getRowFormatter().setStyleName(i, "rowUnselectedShadow");
					i++;
				}
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

		// Listen for mouse events on local backup Add button
		localBackupAddButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int year = BASE_YEAR + localBackupListBox.getSelectedIndex();

				String filePath = "http://1-dot-ddwaychen.appspot.com/data/crime_" + year + ".csv";


				//String filePath = "http://1-dot-crimemapper310.appspot.com/data/crime_" + year + ".csv";
				refreshCrimeList(filePath);
			}
		});

		// Listen for mouse events on local backup Add button
		localBackupRemoveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int year = BASE_YEAR + localBackupListBox.getSelectedIndex();
				removeFromCrimeList(year);
			}
		});

		// Listen for mouse events on local backup Cancel button
		localBackupCancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				localBackupPanel.setVisible(false);
				settingsLabel.setText("");
			}
		});

		// Listen for mouse events on local Account Add button
		localAccountAddButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(adminTextBox.getText() == "")
					Window.alert("Please input new account!!!");
				else{
					//add new account
					String str = adminTextBox.getText();
					for(int i=0; i<lst.size(); i++){
						if(str.toLowerCase() == lst.get(i).toLowerCase()){
							Window.alert("This account already exists!!!");
							return;
						}
					}

					lst.add(str);
					localAccountListBox.addItem(str);


					/*loginInfo.setAccountList(lst);


							if(loginService == null)
								loginService = GWT.create(LoginService.class);

							loginService.addAccount(str, new AsyncCallback<Void>() {
								public void onFailure(Throwable error) {

									Window.alert(error.toString());
								}

								public void onSuccess(Void result) {
								}
							});*/
				}
			}
		});

	}

	// ===================================================================================== //

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
		if (rowIndex == selectedRow) {
			crimeFlexTable.getRowFormatter().setStyleName(rowIndex, "rowUnselectedShadow");
			clearTrends();
			selectedYearLabel.setText("");
			selectedYearLabel.setStyleName("UnSelectedYearLabelStyle");
			selectedRow = NO_TABLE_SELECTION_FLAG;
			try {
				updateUserSelectedRow();
			} catch (FailedToRetrieveDataException e) {
				// TODO Add the reload data panel
			}
		} else {
			int row = crimeFlexTable.getRowCount();
			int i = START_OF_DATA_ROWS;

			selectedRow = rowIndex;
			if (rowIndex != userSelectedRow) {
				try {
					updateUserSelectedRow();
				} catch (FailedToRetrieveDataException e) {
					// TODO Add the reload data panel
				}
			}
			
			ArrayList<ArrayList<Double>> trends = getTrends(rowIndex);

			updateTableTrends(trends);

			while(i < row){
				if(i == rowIndex){
					crimeFlexTable.getRowFormatter().setStyleName(rowIndex, "rowSelectedShadow");
					selectedYearLabel.setText("Base Year: "+getYearFromTable(rowIndex));
					selectedYearLabel.setStyleName("selectedYearLabelStyle");

				} else {
					crimeFlexTable.getRowFormatter().setStyleName(i, "rowUnselectedShadow");
				}
				i++;
			}
		}
	}

	// ===================================================================================== //
	/**
	 * Method for constructing Main Panel
	 * @throws Exception 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private Panel buildMainPanel(){

		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSize(WIDTH, HEIGHT);
		mainPanel.add(buildTabPanel());

		return mainPanel;
	}

	/**
	 * Method for constructing Tab Panel
	 * @throws Exception 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private TabPanel buildTabPanel(){

		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize(WIDTH, HEIGHT);

		//Create titles for tabs
		String tab1Title = "Trends";
		String tab2Title = "Map";
		String tab3Title = "FAQ";
		String tab4Title = "Admin";
		String tab5Title = "Account";


		//Create Custom FlowPanels to add to TabPanel
		FlowPanel flowpanel;
		// Create tab to hold Table, Map and Settings
		// Assemble mainPanel
		flowpanel = new FlowPanel();
		flowpanel.add(buildTrendsTabPanel());
		tabPanel.add(flowpanel, tab1Title);

		flowpanel = new FlowPanel();
		flowpanel.add(buildMapTabPanel());
		tabPanel.add(flowpanel, tab2Title);

		flowpanel = new FlowPanel();
		flowpanel.add(FaqTabPanel.getFaqTabPanel());
		tabPanel.add(flowpanel, tab3Title);

		


		if(isAdmin){
			flowpanel = new FlowPanel();
			flowpanel.add(buildSettingsTabPanel());
			tabPanel.add(flowpanel, tab4Title);
			
			flowpanel = new FlowPanel();
			flowpanel.add(buildAccountTabPanel());
			tabPanel.add(flowpanel, tab5Title);
		}

		// first tab upon load
		tabPanel.selectTab(0);
		return tabPanel;
	}
	/**
	 * Build trend tab
	 */

	private Panel buildTrendsTabPanel(){


		mainTrendsPanel.setWidth(WIDTH);
		mainTrendsPanel.setHeight(HEIGHT);
		mainTrendsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		trendsHPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		trendsHPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		trendsHPanel1.setSpacing(SPACING);
		trendsHPanel1.add(buildTableVPanel());
		trendsHPanel1.add(pieChartPanel);
		trendsHPanel2.add(colChartPanel);
		trendsHPanel2.add(lastUploadedDateLabel);
		mainTrendsPanel.add(trendsHPanel1);
		mainTrendsPanel.add(trendsHPanel2);


		return mainTrendsPanel;
	}


	private Chart buildYearlyPieChart(int year){
		// TODO: Finish for Sprint 2
		pieChart.setType(Series.Type.PIE)  
		.setChartTitleText("Year Added: "+ year)
		.setPlotBackgroundColor((String) null)  
		.setPlotBorderWidth(null)  
		.setPlotShadow(true)  
		.setPiePlotOptions(new PiePlotOptions()  
		.setAllowPointSelect(true)  
		.setCursor(PlotOptions.Cursor.POINTER)  
		.setPieDataLabels(new PieDataLabels()   
		.setEnabled(false)  
				)
				.setShowInLegend(true)
				)
				.setToolTip(new ToolTip()  
				.setFormatter(new ToolTipFormatter() {  
					public String format(ToolTipData toolTipData) {  
						return "<b>" + toolTipData.getPointName() + "</b>: " + toolTipData.getYAsDouble() + " %";  
					}  
				})  
						);  
		CrimeDataByYear cdby = crimeDataMap.get(year);
		int i = 0;
		double totalNumberCrimes = 0;
		while(i < CrimeTypes.getNumberOfTypes()){
			totalNumberCrimes = totalNumberCrimes + cdby.getNumberOfCrimeTypeOccurrences(CrimeTypes.getType(i));
			i++;
		}
		pieChart.addSeries(pieChart.createSeries()  
				.setName("Crime Distribution")  
				.setPoints(new Point[]{  
						new Point(CrimeTypes.getType(0), (cdby.getNumberOfCrimeTypeOccurrences
								(CrimeTypes.getType(0))/totalNumberCrimes)*100),  
								new Point(CrimeTypes.getType(1), (cdby.getNumberOfCrimeTypeOccurrences
										(CrimeTypes.getType(1))/totalNumberCrimes)*100),  
										new Point(CrimeTypes.getType(2), (cdby.getNumberOfCrimeTypeOccurrences
												(CrimeTypes.getType(2))/totalNumberCrimes)*100)  
						.setSliced(true)  
						.setSelected(true),  
						new Point(CrimeTypes.getType(3), (cdby.getNumberOfCrimeTypeOccurrences
								(CrimeTypes.getType(3))/totalNumberCrimes)*100),  
								new Point(CrimeTypes.getType(4), (cdby.getNumberOfCrimeTypeOccurrences
										(CrimeTypes.getType(4))/totalNumberCrimes)*100),  
										new Point(CrimeTypes.getType(5), (cdby.getNumberOfCrimeTypeOccurrences
												(CrimeTypes.getType(5))/totalNumberCrimes)*100),
												new Point(CrimeTypes.getType(6), (cdby.getNumberOfCrimeTypeOccurrences
														(CrimeTypes.getType(6))/totalNumberCrimes)*100)
				})  
				); 
		//pieChart.setVisible(true);
		return pieChart;
	}

	private Chart buildYearlyColChart(TreeMap<Integer, CrimeDataByYear> crimeDataMap2){
		Chart colChart = new Chart();
		colChart.setType(Series.Type.COLUMN)  
		.setChartTitleText("Yearly Comparison of Crime Types")    
		.setColumnPlotOptions(new ColumnPlotOptions()  
		.setPointPadding(0.2)  
		.setBorderWidth(0)  
				)  
				.setLegend(new Legend()  
				.setLayout(Legend.Layout.HORIZONTAL)  
				.setAlign(Legend.Align.LEFT)  
				.setVerticalAlign(Legend.VerticalAlign.TOP)  
				.setX(195)  
				.setY(40)  
				.setFloating(true)  
				.setBackgroundColor("#FFFFFF")  
				.setShadow(true)  
						)  
						.setToolTip(new ToolTip()  
						.setFormatter(new ToolTipFormatter() {  
							public String format(ToolTipData toolTipData) {  
								return toolTipData.getXAsString() + ": " + toolTipData.getYAsLong() 
										+ " " + toolTipData.getSeriesName();  
							}  
						})  
								);  

		CrimeDataByYear[] cdby = crimeDataMap2.values().toArray(new CrimeDataByYear[crimeDataMap2.size()]);
		String[] years = new String[cdby.length];
		ArrayList<String> yearsA = new ArrayList<String>();
		for (int i = 0; i < years.length; i++){
			yearsA.add(cdby[i].yearToString());
		}

		// selectedYearLabel.setText(yearsA.toString());
		colChart.getXAxis()
		.setCategories(yearsA.toArray(new String[0]));
		colChart.getXAxis().setAxisTitleText("Years");

		colChart.getYAxis().setAxisTitleText("Number of Occurrences").setMin(0).setMax(18000);

		Number[] crimes = new Number[years.length];
		for(int k = 0; k < CrimeTypes.getNumberOfTypes(); k++){
			for (int i = 0; i < cdby.length; i++){
				crimes[i] = cdby[i].getNumberOfCrimeTypeOccurrences(CrimeTypes.getType(k));
			}
			colChart.addSeries(colChart.createSeries()
					.setName(CrimeTypes.getType(k))
					.setPoints(crimes)
					);   
		}
		colChart.setWidth("100%");
		return colChart;  
	}

	/**
	 * Method for constructing Table Tab Panel 
	 *  - style elements for table
	 */
	@SuppressWarnings("deprecation")
	private Panel buildTableVPanel(){
		tableVPanel.setSize(WIDTH, HEIGHT);
		tableVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		tableVPanel.setSpacing(SPACING);

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
			crimeFlexTable.getCellFormatter().addStyleName(1, i, "crimeTypeHeaderTitles");
			i++;
		}
		crimeFlexTable.setCellPadding(PADDING);
		try {
			loadCrimeDataMap();
			loadUserSelectedRow();
		} catch (FailedToRetrieveDataException e) {
			// TODO Add the reload data panel
		}
		crimeFlexTable.getCellFormatter().addStyleName(1, 1, "mischiefUnder");
		crimeFlexTable.getCellFormatter().addStyleName(1, 2, "mischiefOver");
		crimeFlexTable.getCellFormatter().addStyleName(1, 3, "fromAutoUnder");
		crimeFlexTable.getCellFormatter().addStyleName(1, 4, "fromAutoOver");
		crimeFlexTable.getCellFormatter().addStyleName(1, 5, "ofAutoUnder");
		crimeFlexTable.getCellFormatter().addStyleName(1, 6, "ofAutoOver");
		crimeFlexTable.getCellFormatter().addStyleName(1, 7, "commercialBE");

		// Assemble resetPanel.
		clearTrendsButtonPanel.setSpacing(SPACING);
		clearTrendsButton.setStyleName("clearButtonStyle");
		clearTrendsButtonPanel.add(clearTrendsButton);
		clearTrendsButtonPanel.add(selectedYearLabel);
		// Date label
		lastUploadedDateLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

		// Assemble Table Panel to insert in Tab1 of Tab Panel
		//tableVPanel.add(selectedYearLabel);
		tableVPanel.add(crimeFlexTable);
		tableVPanel.add(clearTrendsButtonPanel);
		tableVPanel.add(signOutLink);
		tableVPanel.add(lastUploadedDateLabel);


		// return table constructed panel
		return tableVPanel;
	}

	/**
	 *  Method for Constructing Map tab panel
	 */
	private Panel buildMapTabPanel(){
		mapsVPanel.setSize(WIDTH, HEIGHT);
		mapsVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mapsVPanel.setSpacing(SPACING);
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

		settingsVPanel.setSize(WIDTH, HEIGHT);
		settingsVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		settingsVPanel.setSpacing(SPACING);
		// Assemble elements for Settings Panel


		// Assemble Settings Panel to insert Settings 
		settingsVPanel.add(settingsLabel);
		newUrlTextBox.setText("Paste Crime URL here");
		settingsVPanel.add(newUrlTextBox);
		settingsVPanel.add(loadCrimeDataButton);

		// Assemble the listbox that loads backup data from local
		localBackupPanel.setVisible(false);
		localBackupPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		localBackupPanel.add(localBackupLabel);
		localBackupPanel.add(localBackupListBox);
		localBackupLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		settingsVPanel.add(localBackupPanel);
		for (int i = 0; i < NUM_YEARS; i++) {
			localBackupListBox.addItem(Integer.toString(BASE_YEAR + i));
		}
		localBackupPanel.add(localBackupAddButton);
		localBackupPanel.add(localBackupCancelButton);

		return settingsVPanel;
	}

	private Panel buildAccountTabPanel(){
		accountVPanel.add(adminLabel);
		accountVPanel.add(localAccountListBox);
		//accountVPanel.add(localAccountDelButton);

		//accountVPanel.add(adminTextBox);
		//accountVPanel.add(localAccountAddButton);
		for (int i = 0; i < lst.size(); i++) {
			localAccountListBox.addItem(lst.get(i));
		}

		return accountVPanel;
	}

	// ===================================================================================== //
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
					localBackupPanel.setVisible(false);
				} else {
					settingsLabel.setText("Error Loading Data");
					localBackupPanel.setVisible(true);
				}
			}
		}; 

		// Make the call to the crime data service.
		crimeDataSvc.getCrimeDataByYear(crimeURL, callback);
	}
	// ===================================================================================== //
	private void removeFromCrimeList(int year) {
		// TODO Implement Remove from list functionality in Sprint 2
	}
	// ===================================================================================== //
	private void addCrimeDataSet(CrimeDataByYear result) {
		crimeDataMap.put(result.getYear(), result);
		try {
			updateCrimeDataMap();
		} catch (FailedToRetrieveDataException e) {
			// TODO Add the reload data panel
			e.printStackTrace();
		}
		updateTableView(crimeDataMap);
		updateColChartView(crimeDataMap);
	}

	private void updateColChartView(TreeMap<Integer, CrimeDataByYear> crimeDataMap2){

		colChartPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		colChartPanel.add(buildYearlyColChart(crimeDataMap2));
		// Removing previous elements from panel
		int i = colChartPanel.getWidgetCount();
		int n = 0;
		if((i != 1) && (i != 0)){
			while (n < i-1){
				colChartPanel.remove(n);
				n++;
			}
		}
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
				crimeFlexTable.setText(row, i, ""+crimeOccurences+"");
				i++;
			}
		}
		int i = 1;
		while(i < crimeFlexTable.getRowCount()){
			// TODO Possibly refactor to get rid of magic number and
			// use the size of the enum of crime types
			crimeFlexTable.getCellFormatter().addStyleName(i, 0, "crimeTypeHeader");
			i++;
		}
	}
	// ===================================================================================== //
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
	// ===================================================================================== //
	private ArrayList<ArrayList<Double>> getTrends(int index) {
		ArrayList<ArrayList<Double>> trendsByYear = new ArrayList<ArrayList<Double>>();
		if (index<START_OF_DATA_ROWS) {
			return null;
		}
		int baseYear = getYearFromTable(index);
		CrimeDataByYear baseYearCrimeData = crimeDataMap.get(baseYear);
		for (Map.Entry<Integer, CrimeDataByYear> otherYear: crimeDataMap.entrySet()){
			CrimeDataByYear otherYearCrimeData = otherYear.getValue();
			ArrayList<Double> trendsByType = new ArrayList<>();
			for (int i = 0; i < CrimeTypes.getNumberOfTypes(); i++) {
				String type = CrimeTypes.getType(i);
				double base = baseYearCrimeData.getNumberOfCrimeTypeOccurrences(type);
				double other = otherYearCrimeData.getNumberOfCrimeTypeOccurrences(type);
				double percentChange = (((other - base) / base) * 100);
				// Round to two decimal places
				percentChange = Math.floor(percentChange*100)/100;
				trendsByType.add(percentChange);
			}
			trendsByYear.add(trendsByType);
		}
		return trendsByYear;
	}

	/**
	 * Update table view with trends labels
	 * @param receiverRowIndex
	 */
	private void updateTableTrends(ArrayList<ArrayList<Double>> trendsByRow) {
		updateTableView(crimeDataMap);
		int row = crimeFlexTable.getRowCount();
		int r = START_OF_DATA_ROWS;
		clearTrends();
		while (r < row){
			for (int i=START_OF_DATA_COLUMNS; i < COLUMN_COUNT; i++){
				String cellText = crimeFlexTable.getText(r, i);
				double percentage = trendsByRow.get(r-START_OF_DATA_ROWS).get(i-START_OF_DATA_COLUMNS);
				String trendsText = " (" + percentage + "%)";
				if (r == selectedRow){
					crimeFlexTable.setText(r, i, cellText);
				}
				else {
					crimeFlexTable.setText(r, i, cellText + trendsText);
				}
			}
			r++;
		}
	}
	private void clearTrends(){
		int row = crimeFlexTable.getRowCount();
		int r = START_OF_DATA_ROWS;
		while (r < row){
			for (int i=START_OF_DATA_COLUMNS; i < COLUMN_COUNT; i++){
				String cellText = crimeFlexTable.getText(r, i);
				if (cellText.contains("(")){
					int cutoff = ((cellText.indexOf("("))-1);
					String newCellText = cellText.substring(0, cutoff);
					crimeFlexTable.setText(r, i, newCellText);}
			}
			r++;
		}
	}
}
// ===================================================================================== //

