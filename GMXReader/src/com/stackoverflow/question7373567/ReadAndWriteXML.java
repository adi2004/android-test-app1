package com.stackoverflow.question7373567;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

class ReadAndWriteXML
{
	private String role1 = null;
	private String role2 = null;
	private String role3 = null;
	private String role4 = null;
	private ArrayList<String> rolev;

	public static void main(String[] args) {
		ReadAndWriteXML app = new ReadAndWriteXML();

		app.readXML("string.xml");
		app.printValues();
		//app.saveToXML("output.xml");
		app.saveToXML();
	}

	private void printValues() {
		System.out.println("role1 = " + role1);
		System.out.println("role2 = " + role2);
		System.out.println("role3 = " + role3);
		System.out.println("role4 = " + role4);
	}

	public boolean readXML(String xml) {
		rolev = new ArrayList<String>();
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

			role1 = getTextValue(role1, doc, "role1");
			if (role1 != null) {
				if (!role1.isEmpty())
					rolev.add(role1);
			}
			role2 = getTextValue(role2, doc, "role2");
			if (role2 != null) {
				if (!role2.isEmpty())
					rolev.add(role2);
			}
			role3 = getTextValue(role3, doc, "role3");
			if (role3 != null) {
				if (!role3.isEmpty())
					rolev.add(role3);
			}
			role4 = getTextValue(role4, doc, "role4");
			if (role4 != null) {
				if (!role4.isEmpty())
					rolev.add(role4);
			}
			return true;

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		return false;
	}

	public void saveToXML() {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("company");
			doc.appendChild(rootElement);

			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);

			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);

			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);

			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public void saveToXML(String xml) {
		Document dom;
		Element e = null;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			// create the root element
			Element rootEle = dom.createElement("roles");

			// create data elements and place them under root
			e = dom.createElement("role1");
			e.appendChild(dom.createTextNode(role1));
			rootEle.appendChild(e);

			e = dom.createElement("role2");
			e.appendChild(dom.createTextNode(role2));
			rootEle.appendChild(e);

			e = dom.createElement("role3");
			e.appendChild(dom.createTextNode(role3));
			rootEle.appendChild(e);

			e = dom.createElement("role4");
			e.appendChild(dom.createTextNode(role4));
			rootEle.appendChild(e);

			dom.appendChild(rootEle);

			try {
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(dom);
				// StreamResult result = new StreamResult(new File("file.xml"));

				// Output to console for testing
				StreamResult result = new StreamResult(System.out);

				transformer.transform(source, result);

				System.out.println("File saved!");
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}
			// try {
			// Transformer tr =
			// TransformerFactory.newInstance().newTransformer();
			// tr.setOutputProperty(OutputKeys.INDENT, "yes");
			// tr.setOutputProperty(OutputKeys.METHOD, "xml");
			// tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			// tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
			// "4");
			//
			// // send DOM to file
			// tr.transform(new DOMSource(dom), new StreamResult(new
			// FileOutputStream(xml)));
			//
			// } catch (TransformerException te) {
			// System.out.println(te.getMessage());
			// te.printStackTrace();
			// } catch (IOException ioe) {
			// System.out.println(ioe.getMessage());
			// ioe.printStackTrace();
			// }
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
			pce.printStackTrace();
		}
	}

	private String getTextValue(String def, Element doc, String tag) {
		String value = def;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			value = nl.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}
}