package com.google.gwt.cs310project.crimemapper.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;

public final class FaqTabPanel extends StackPanel {

	private static StackPanel faqTabPanel = new StackPanel();

	private static final String WIDTH = "100%";
	private static final String HEIGHT = "100%";

	private FaqTabPanel() {
		// Do not allow instantiation of this class
	}

	public static StackPanel getFaqTabPanel(){

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
		String mischiefDiscription = "md";
		String theftFromAutoDiscription = "tfad";
		String theftOfAutoDiscription = "toad";
		String commercialBEDiscription = "cbed";

		faqTabPanel.setSize(WIDTH,HEIGHT);

		Label label;

		// Application Facts
		label = new Label(appFact1);
		faqTabPanel.add(label, "Comparing Crime Statistics", false);

		label = new Label(appFact2);
		faqTabPanel.add(label, "App Fact2", false);

		label = new Label(appFact3);
		faqTabPanel.add(label, "App Fact3", false);
		
		label = new Label(mischiefDiscription);
		faqTabPanel.add(label, "What is Mischief?", false);
		
		label = new Label(theftFromAutoDiscription);
		faqTabPanel.add(label, "What is Theft From Auto?", false);
		
		label = new Label(theftOfAutoDiscription);
		faqTabPanel.add(label, "What is Theft Of Auto?", false);
		
		label = new Label(commercialBEDiscription);
		faqTabPanel.add(label, "What is Commercial Break and Enter?", false);

		return faqTabPanel;
	}

}
