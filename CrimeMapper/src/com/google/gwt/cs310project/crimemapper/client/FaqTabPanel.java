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

		String appFact1 = "Fact 1";
		String appFact2 = "Fact 2";
		String appFact3 = "The Vancouver Police Department (VPD) has changed the way in "
				+ "which it reports its crime statistics. Historically, it reported data "
				+ "based on Statistics Canada reporting requirements, which meant that "
				+ "only the most serious offence per incident was counted. Now, the all "
				+ "violations method is used. Other policing agencies like Edmonton, "
				+ "Toronto, Ottawa and Calgary also present their crime statistics using "
				+ "the all violations method. It is important to note these differences "
				+ "in reporting when comparing our crime statistics to other Police "
				+ "Agencies and Statistics Canada.";

		// Crime Types
		String mischiefDiscription = "Mischief is the offence of damaging another person’s property without intending to steal"
				+ " it.  A person commits mischief if they intentionally destroy or damage property, render "

				+ "property dangerous, useless, inoperative or ineffective, or interfere with another person’s "

				+ "use, enjoyment or operation of the property.”";
		String theftFromAutoDiscription = "The unlawful taking of motor vehicle contents or parts.";
		String theftOfAutoDiscription = "Theft of Auto is an offence where a motor vehicle is entered, with an attack of the steering column or ignition and the vehicle was moved.";
		String commercialBEDiscription = "Commercial Break and Enter is an offence where the perpetrator enters a commercial business or financial institution without permission, and commits another criminal offence. The additional offence most often refers to the theft of valuables. This category includes break and enter to compounds on a commercial property.";

		faqTabPanel.setSize(WIDTH,HEIGHT);

		Label label;

		// Application Facts
		label = new Label(appFact1);
		faqTabPanel.add(label, "App Fact1", false);
		
		label = new Label(appFact2);
		faqTabPanel.add(label, "App Fact2", false);

		label = new Label(appFact3);
		faqTabPanel.add(label, "How does The Vancouver Police Department report its crime statistics?", false);
		
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
