
public class Cow implements Vegetarian {

	@Override
	public void eatGrass() {
		System.out.println("[" + Cow.class + "]" + "Cow eating grass...");
	}

}
