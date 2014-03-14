package parser;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Config
{
	// private static String ROOT_ELEMENT = "config";
	private static String FILE_ELEMENT = "file";
	private static String IDLE_ELEMENT = "idle_words";
	private static String WORK_ELEMENT = "work_words";
	private static String WORDS_ELEMENT = "words";
	private static String PROCRASTINATING_ELEMENT = "procrastinating_words";

	private static String SPLIT_STRING = ";";

	String[] work;
	String[] idle;
	String[] procrastinating;

	String file;

	WordListConfig[] list;

	public Config() {
		init();
	}

	public void init() {
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("config.xml"));

			// normalize text representation
			doc.getDocumentElement().normalize();
			file = doc.getElementsByTagName(FILE_ELEMENT).item(0).getTextContent();
			work = doc.getElementsByTagName(WORK_ELEMENT).item(0).getTextContent().split(SPLIT_STRING);
			idle = doc.getElementsByTagName(IDLE_ELEMENT).item(0).getTextContent().split(SPLIT_STRING);
			procrastinating = doc.getElementsByTagName(PROCRASTINATING_ELEMENT).item(0).getTextContent().split(SPLIT_STRING);

			NodeList nlist = doc.getElementsByTagName("list");
			list = new WordListConfig[nlist.getLength()];
			for (int i = 0; i <= nlist.getLength(); i++) {
				Node words = nlist.item(i);
				list[i] = new WordListConfig(words.getAttributes().getNamedItem("name").getNodeValue(), words.getNodeValue());
			}
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String[] getWork() {
		return work;
	}

	public String[] getIdle() {
		return idle;
	}

	public String[] getProcrastinating() {
		return procrastinating;
	}

	public String getFile() {
		return file;
	}

	private class WordListConfig
	{
		public WordListConfig(String nodeValue, String nodeValue2) {
			name=nodeValue;
			words=nodeValue2.split(SPLIT_STRING);
		}
		public String name;
		public String[] words;
	}
}
