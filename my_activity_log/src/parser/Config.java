package parser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** read and store the configuration from the xml file */
public class Config {
	private static String FILE_ELEMENT = "file";
	private static String ACTIVITY_ELEMENT = "activity";
	private static String ACTIVITY_NAME = "name";
	private static String SPLIT_STRING = ";";
	private static String DATE_FORMAT_ELEMENT = "date_format";

	String file;
	String dateFormat;
	Activity[] activ;

	public Config(String path) {
		init(path);
	}

	public void init(String path) {
		Log.i("Starting to read file ...");
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(path));

			// normalize text representation
			doc.getDocumentElement().normalize();

			// assume that there is only one file element, and only one;
			file = doc.getElementsByTagName(FILE_ELEMENT).item(0).getTextContent();

			// read the date format
			dateFormat = doc.getElementsByTagName(DATE_FORMAT_ELEMENT).item(0).getTextContent();

			// read the activities that can be in a file
			setActiv(doc);
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		Log.i("finished reading file.");
	}

	private void setActiv(Document doc) {
		NodeList activities = doc.getElementsByTagName(ACTIVITY_ELEMENT);
		Log.i("found activities: " + activities.getLength());
		activ = new Activity[activities.getLength()];
		for (int idx = 0; idx < activities.getLength(); idx++) {
			Node tempNode = activities.item(idx);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap attr = tempNode.getAttributes();
				String activName = attr.getNamedItem(ACTIVITY_NAME).getTextContent();
				String activKeywords = tempNode.getTextContent();
				activ[idx] = new Activity(activName, activKeywords);
				Log.i("added activity " + activ[idx].toString());
			}
		}
	}

	public String getFile() {
		return file;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(file + "; ");
		output.append(dateFormat);
		return output.toString();
	}

	private class Activity {
		public String name;
		public String[] words;

		public Activity(String listName, String concatenatedList) {
			name = listName;
			if (concatenatedList != null)
				words = concatenatedList.split(SPLIT_STRING);
		}

		@Override
		public String toString() {
			if (words == null) {
				return name;
			}
			StringBuilder wrds = new StringBuilder();
			for(String word:words) {
				wrds.append(word + " ");
			}
			return name + " = " + wrds.toString();
		}
	}
}
