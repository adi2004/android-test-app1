
public class Deer extends Animal implements Vegetarian {

	@Override
	public void eatGrass() {
		System.out.println("[" + Deer.class + "]" + "Deer eating grass...");
	}

	public void eat(){
		System.out.println("[" + Deer.class + "]" + "Deer eating...");
	}
}
