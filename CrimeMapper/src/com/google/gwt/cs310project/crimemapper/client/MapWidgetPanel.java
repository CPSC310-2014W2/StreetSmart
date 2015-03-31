package com.google.gwt.cs310project.crimemapper.client;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.format.KML;
import org.gwtopenmaps.openlayers.client.layer.Bing;
import org.gwtopenmaps.openlayers.client.layer.BingOptions;
import org.gwtopenmaps.openlayers.client.layer.BingType;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.protocol.HTTPProtocol;
import org.gwtopenmaps.openlayers.client.protocol.HTTPProtocolOptions;
import org.gwtopenmaps.openlayers.client.protocol.Protocol;
import org.gwtopenmaps.openlayers.client.protocol.ProtocolType;
import org.gwtopenmaps.openlayers.client.strategy.FixedStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;


public class MapWidgetPanel {

	//private static final String DOMAIN_NAME = CrimeMapper.DOMAIN_NAME; //add your own domain here
	private static final String DOMAIN_NAME = CrimeMapper.DOMAIN_NAME;
	private static final double VAN_LON = -123.116226;
	private static final double VAN_LAT = 49.246292;
	private static final String MAP_WIDTH = "1250px";
	private static final String MAP_HEIGHT = "550px";

	private static final String BING_API = "Apd8EWF9Ls5tXmyHr22OuL1ay4HRJtI4JG4jgluTDVaJdUXZV6lpSBpX-TwnoRDG";
	private static MapOptions defaultMapOptions = new MapOptions();

	private MapWidgetPanel() {
		// Do not allow instantiation of this class
	}

	public static MapWidget getMapWidget(){
		MapWidget mapWidget = new MapWidget(MAP_WIDTH, MAP_HEIGHT, defaultMapOptions);
		// Bing Map Layer
		//Create some Bing layers
		final String key = BING_API;
		// configuring road options
		BingOptions bingOptionRoad = new BingOptions("Bing Road Layer", key,
				BingType.ROAD);
		bingOptionRoad.setProtocol(ProtocolType.HTTP);
		Bing road = new Bing(bingOptionRoad);

		// configuring hybrid options
		BingOptions bingOptionHybrid = new BingOptions("Bing Hybrid Layer", key,
				BingType.HYBRID);
		bingOptionRoad.setProtocol(ProtocolType.HTTP);
		Bing hybrid = new Bing(bingOptionHybrid);

		// configuring aerial options
		BingOptions bingOptionAerial = new BingOptions("Bing Aerial Layer", key,
				BingType.AERIAL);
		bingOptionRoad.setProtocol(ProtocolType.HTTP);
		Bing aerial = new Bing(bingOptionAerial);

		//Add layers to map
		mapWidget.getMap().addLayer(road);
		mapWidget.getMap().addLayer(hybrid);
		mapWidget.getMap().addLayer(aerial);

		//Map Controls
		//mapWidget.getMap().addControl(new LayerSwitcher()); //+ sign in the upperright corner to display the layer switcher
		mapWidget.getMap().addControl(new OverviewMap()); //+ sign in the lowerright to display the overviewmap
		mapWidget.getMap().addControl(new ScaleLine()); //Display the scaleline

		// Vancouver coordinates
		LonLat lonLat = new LonLat(VAN_LON, VAN_LAT);
		lonLat.transform("EPSG:4326", mapWidget.getMap().getProjection());
		mapWidget.getMap().setCenter(lonLat, 12);

		//Create a KML layer using Vancouver's boundary kml file
		VectorOptions kmlOptions = new VectorOptions();
		kmlOptions.setStrategies(new Strategy[]{new FixedStrategy()});
		HTTPProtocolOptions protocolOptions = new HTTPProtocolOptions();
		protocolOptions.setUrl(DOMAIN_NAME+"/data/cov_localareas.kml");
		KML kml = new KML();
		kml.setExtractStyles(true);
		kml.setExtractAttributes(true);
		kml.setMaxDepth(2);
		protocolOptions.setFormat(kml);
		Protocol protocol = new HTTPProtocol(protocolOptions);
		kmlOptions.setProtocol(protocol);
		Vector kmlLayer = new Vector("KML", kmlOptions);
		kmlLayer.setOpacity(1);
		mapWidget.getMap().addLayer(kmlLayer);
		return mapWidget;
		
		}
}
