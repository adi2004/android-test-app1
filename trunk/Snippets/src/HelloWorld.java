import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HelloWorld {

	static final Logger logger = Logger.getLogger(HelloWorld.class);

	public static void main(String[] args) {
//		BasicConfigurator.configure();
		PropertyConfigurator.configure("conf/log4j.properties");
		logger.debug("Hello World!");
	}
}