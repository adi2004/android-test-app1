import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.*;

// How to use log4j
public class TestLogging {

	// Initialize a logging category. Here, we get THE ROOT CATEGORY
	// static Category cat = Category.getRoot();
	// Or, get a custom category
	// static Category cat = Category.getInstance(TestLogging.class.getName());

	// From here on, log away! Methods are: cat.debug(your_message_string),
	// cat.info(...), cat.warn(...), cat.error(...), cat.fatal(...)

	// Different way of doing stuff
	static Logger cat = Logger.getLogger(TestLogging.class.getName());

	public static void main(String args[]) {
		// Quickly configure the package
//		BasicConfigurator.configure();
//		PropertyConfigurator.configure("conf/log4j.properties");
		DOMConfigurator.configure("conf/example.xml");

		// Try a few logging methods
		cat.debug("Start of main()");
		cat.info("Just testing a log message with priority set to INFO");
		cat.warn("Just testing a log message with priority set to WARN");
		cat.error("Just testing a log message with priority set to ERROR");
		cat.fatal("Just testing a log message with priority set to FATAL");

		// Alternate but INCONVENIENT form
//		cat.log(Priority.DEBUG, "Calling init()");

		new TestLogging().init();
	}

	public void init() {
		java.util.Properties prop = System.getProperties();
		java.util.Enumeration enumr = prop.propertyNames();

		cat.info("***System Environment As Seen By Java***");
		cat.debug("***Format: PROPERTY = VALUE***");

		while (enumr.hasMoreElements()) {
			String key = (String) enumr.nextElement();
			cat.info(key + " = " + System.getProperty(key));
		}
	}

}