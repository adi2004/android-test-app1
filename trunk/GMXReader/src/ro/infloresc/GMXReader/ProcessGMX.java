package ro.infloresc.GMXReader;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class ProcessGMX extends JFrame
{
	private static String ROOT_ELEM = "gpx";
	private static String SEGMENTS_ELEM = "trkseg";
	private static String POINT_ELEM = "trkpt";
	private static String LAT_ATRIBUTE = "lat";
	private static String LON_ATRIBUTE = "lon";
    private JMapViewer treeMap = null;

	private LatLong coords[];

	public ProcessGMX() {
		setTitle("GMX points on OSM");
		setSize(1000,500); // default size is 0,0
		setLocation(100,100); // default is 0,0 (top left corner)
		treeMap = new JMapViewer();

		add(treeMap, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
		ProcessGMX app = new ProcessGMX();
		//app.readXML("Spre Telmap.gpx");
		app.readXML("C tre 1 Decembrie  i înapoi..gpx");
		System.out.printf("Distance traked: %d meters ", app.getDistance());
		app.plotCoords();
		app.setVisible(true);
	}

	public void plotCoords() {
		List<LatLong> listCoords = new ArrayList<LatLong>();

		for(LatLong coord:coords) {
			listCoords.add(coord);
			treeMap.addMapMarker( new MapMarkerDot(coord.getLat(), coord.getLon()) );

			//treeMap.addMapPolygon(new MapPolygonImpl(points))
		}

		treeMap.addMapPolygon(new MapPolygonImpl(listCoords));
	}

	public int getDistance()
	{
		float distance = 0;
		LatLong coordA = coords[0];
		for(LatLong coord:coords)
		{
			distance += distFrom(coordA, coord);
			coordA = coord;
		}
		return (int)distance;
	}
	public boolean readXML(String xml) {
		// rolev = new ArrayList<String>();
		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the
			// XML file
			dom = db.parse(xml);

			Element doc = dom.getDocumentElement();

			NodeList coordsList = doc.getElementsByTagName(POINT_ELEM);

			coords = new LatLong[coordsList.getLength()];

			for (int i = 0, len = coordsList.getLength(); i < len; i++) {
				coords[i] = new LatLong();
				coords[i].setLat( Float.parseFloat(((Element) coordsList.item(i)).getAttribute(LAT_ATRIBUTE)) );
				coords[i].setLon(Float.parseFloat(((Element) coordsList.item(i)).getAttribute(LON_ATRIBUTE)));
				System.out.println(i + ": lat/long is " + coords[i]);
			}

			System.out.println("Nr of coordonates is " + coordsList.getLength());

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		return false;
	}

	public float distFrom(LatLong a1, LatLong b) {
		double lat1 = a1.getLat();
		double lng1 = a1.getLon();
		double lat2 = b.getLat();
		double lng2 = b.getLon();
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return new Float(dist * meterConversion).floatValue();
	}

	class LatLong implements ICoordinate
	{
		private double lat;
		private double lng;

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lng;
		}

		public void setLon(double lng) {
			this.lng = lng;
		}

		@Override
		public String toString() {
			return Double.toString(lat) + ", " + Double.toString(lng);
		}
	}
}