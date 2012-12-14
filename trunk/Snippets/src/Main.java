// working with system properties

import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		//System.setProperty("HomeFolder", "D:\\");
//		Properties prop = System.getProperties();
//		System.out.println("Printing all System properties");
//		prop.list(System.out);
		Deer d = new Deer();
		Animal a = d;
		Animal a2 = new Animal();
		Vegetarian v = d;
		Vegetarian v2 = new Cow();
		d.eat();
		d.eatGrass();
		a.eat(); //Cow eating!
//		a.eatGrass();
		a2.eat();
		v.eatGrass(); //Deer
		v2.eatGrass(); //Cow
	}
}