package com.google.gwt.cs310project.crimemapper.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.LatLon;
import com.google.gwt.cs310project.crimemapper.client.MapDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;




@SuppressWarnings("serial")
public class MapDataServiceImpl extends RemoteServiceServlet implements
MapDataService {

	private static final String GEOCODE_API_BASE = "http://geocoder.cit.api.here.com/6.2/geocode.xml";
	/**
	 * Here Maps Keys
	 */
	private final static String APP_ID = "Removed";
	private final static String APP_CODE = "Removed";
	private final static String GEN = "8";
	private final static String CITY = "Vancouver";

	@Override
	public ArrayList<LatLon> getHereMapData(ArrayList<CrimeData> crimeDataList) {
		ArrayList<LatLon> latlonList = new ArrayList<LatLon>();
		for(CrimeData cd: crimeDataList){

			LatLon latlon = getLatLon(cd.getLocation());
			latlonList.add(latlon);

		}
		return latlonList;
	}

	private static String uriBuilder(String str){
		StringBuilder sb = new StringBuilder(GEOCODE_API_BASE);
		sb.append("?app_id="+APP_ID);
		sb.append("&app_code="+APP_CODE);
		sb.append("&gen="+GEN);
		sb.append("&searchtext=" + str +"+,");
		sb.append("+"+CITY);

		return sb.toString();
	}

	private static LatLon getLatLon(String location) {
		LatLon latlon = null;
		try {
			URL url = new URL(uriBuilder(location));
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;

			while((line = reader.readLine()) != null){
				latlon = readLatLon(line);
			}


		} catch (MalformedURLException e) {
		
		} catch (IOException e){
		
		}
		return latlon;
	}

	private static LatLon readLatLon(String hereXML){
		double lat = 0.0;
		double lon = 0.0;
		String latStr = "";
		String lonStr = "";
		try{

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new ByteArrayInputStream(hereXML.getBytes("utf-8"))));

			NodeList nodeList = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;

					// Get the value of all sub-elements.
					latStr = elem.getElementsByTagName("Latitude")
							.item(0).getChildNodes().item(0).getNodeValue();
					lonStr = elem.getElementsByTagName("Longitude")
							.item(0).getChildNodes().item(0).getNodeValue();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

		lat = Double.parseDouble(latStr);
		lon = Double.parseDouble(lonStr);
		return new LatLon(lat, lon);
	}
}
