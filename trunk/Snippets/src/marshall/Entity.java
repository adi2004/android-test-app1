package marshall;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entity")
public class Entity {

	private int age_ = 22;
	private String first_name = "Michael";
	private int pts[] = new int[4];

	public int getAge() {
		return age_;
	}

	public void setAge(int age) {
		this.age_ = age;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String firstname) {
		this.first_name = firstname;
	}
	
	@XmlElement(name="color")
	public int[] getPts1() {
		return pts;
	}

	public void setPts1(int[] pts) {
		this.pts = pts;
	}
}
