package marshall;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main {
	public static void main(String[] args) throws JAXBException {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH'h'mm'm'");

		System.out.println(df.format(date));
		JAXBContext jc = JAXBContext.newInstance(Entity.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		Entity en = new Entity();
		en.setAge(24);
		en.setFirstName("</entity>");
		m.marshal(en, System.out);
		m.marshal(en, new File("m_out.xml"));

		Unmarshaller um = jc.createUnmarshaller();
		Entity en_read = (Entity) um.unmarshal(new File("m_out.xml"));
		System.out.println("Age = " + en_read.getAge() + " Name = " + en_read.getFirstName());
	}
}