package com.google.gwt.cs310project.crimemapper.server;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.cs310project.crimemapper.client.CrimeData;
import com.google.gwt.cs310project.crimemapper.client.LatLon;
import com.google.gwt.cs310project.crimemapper.client.MapDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.xml.client.XMLParser;


@SuppressWarnings("serial")
public class MapDataServiceImpl extends RemoteServiceServlet implements
MapDataService {



	private static final String GEOCODE_API_BASE = "http://geocoder.cit.api.here.com/6.2/geocode.xml";

	/**
	 * Here Maps Keys
	 */
	private final static String APP_ID = "DemoAppId01082013GAL";
	private final static String APP_CODE = "AJKnXv84fjrb0KIHawS0Tg";
	private final static String GEN = "8";
	private final static String CITY = "Vancouver";

	@SuppressWarnings("null")
	@Override
	public TreeMap<LatLon, Integer> getMapData(ArrayList<CrimeData> crimeDataList) {
		TreeMap<LatLon, Integer> mapData = null;
		for(CrimeData cd: crimeDataList){
			LatLon latlon = getLatLon(cd.getLocation());
			mapData.put(latlon, cd.getMonth());
		}
		return mapData;
	}
	
	private static String urlMapBuilder(String str){
		StringBuilder sb = new StringBuilder(GEOCODE_API_BASE);
		sb.append("?app_id="+APP_ID);
		sb.append("?app_code="+APP_CODE);
		sb.append("gen="+GEN);
		sb.append("&searchtext=" + str +"+,");
		sb.append("+"+CITY);
		
		return sb.toString();
	}
	private static LatLon getLatLon(String location) {
		LatLon latlon = null;
		HttpURLConnection conn = null;
		try {

			URL url = new URL(urlMapBuilder(location));
			conn = (HttpURLConnection) url.openConnection();

			latlon = readLatLon(url);

		} catch (MalformedURLException e) {
			return latlon;

		} catch (IOException e) {
			return latlon;

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return latlon;
	}

	private static LatLon readLatLon(URL url){
		double latitude = 0.0;
		double longitude = 0.0;

		try{

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(url.openStream());

			NodeList nodeList = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;

					// Get the value of all sub-elements.
					String latStr = elem.getElementsByTagName("Latitude")
							.item(0).getChildNodes().item(0).getNodeValue();
					latitude = Double.parseDouble(latStr);
					String longStr = elem.getElementsByTagName("Longitude")
							.item(0).getChildNodes().item(0).getNodeValue();
					latitude = Double.parseDouble(longStr);


				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return new LatLon(latitude, longitude); 
	}
}
