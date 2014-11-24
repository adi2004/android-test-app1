package gameoflife;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

// from the Day of Code Retreat, 15 Nov. 2014
// Restrictions:
// - use test driven development: unable to do it now
// - use small methods 3 lines
// - avoid ifs
// - use objects

// rules of the game
// a new entity is spawned when it has 3 neighbours
// a entity remains alive when it has 2 or 3 neighbours

// started at 9:40, in the train to Bucharest, on 22. Nov. 2014
/**
 * The story (because a well written code is like a story):
 * Once upon a time there was a world. And this world had only 2 dimensions and
 * was inhabited by pixel like creatures.
 * Each living creature was squared like, and could only have 8 neigbours. And
 * the timed passed, and as it went a cycle ended and another one beginned.
 * Whenever 3 creatures were together they gave birth to another one, but
 * whenever they were alone, or if they were too crowded, they died. And so the
 * game of life was...
 */
public class Game {
	Set<Point> livingEntities;

	public static void main(String[] args) {
		Game g = new Game();
		GameView v = new GameView(g);
		g.initWithRandom();
//		g.initWithLine();
//		g.initWithL();
		do {
			v.update();
		} while (g.nextStep());
	}

	private void initWithL() {
		livingEntities = new HashSet<Point>();
		livingEntities.add(new Point(0, 0));
		livingEntities.add(new Point(0, 1));
		livingEntities.add(new Point(0, 2));
		livingEntities.add(new Point(1, 2));
		livingEntities.add(new Point(2, 1));
	}

	private void initWithLine() {
		livingEntities = new HashSet<Point>();
		livingEntities.add(new Point(0, 1));
		livingEntities.add(new Point(1, 1));
		livingEntities.add(new Point(2, 1));
	}

	public boolean nextStep() {
		/**
		 * get offsprings
		 * for each dead cell and living entity
		 * if it will survive move to the next cycle
		 */
		if (livingEntities.size() == 0)
			return false;
		Set<Point> possibleOffsprings = getPossibleOffsprings();
		Set<Point> newWorld = new HashSet<Point>();
		for (Point o : possibleOffsprings) {
			if (willBeBorn(o)) {
				newWorld.add(o);
			}
		}
		if (newWorld.size() == 0)
			return false;
		for (Point living : livingEntities) {
			if (willSurvive(living)) {
				newWorld.add(living);
			}
		}
//		if (newWorld.size() == livingEntities.size())
//			return false;
		livingEntities = newWorld;
		return true;
	}

	private boolean willBeBorn(Point deadCell) {
		int aliveCount = 0;
		for (int x = -1; x < 2; x++)
			for (int y = -1; y < 2; y++) {
				Point p = new Point(deadCell.x + x, deadCell.y + y);
				if (livingEntities.contains(p))
					aliveCount++;
			}
		return aliveCount == 3;
	}

	private boolean willSurvive(Point aliveCell) {
		int aliveCount = 0;
		for (int x = -1; x < 2; x++)
			for (int y = -1; y < 2; y++) {
				Point p = new Point(aliveCell.x + x, aliveCell.y + y);
				if (livingEntities.contains(p))
					aliveCount++;
			}
		aliveCount--;
		return aliveCount == 2 || aliveCount == 3;
	}

	private Set<Point> getPossibleOffsprings() {
		Set<Point> deadCells = new HashSet<Point>();
		for (Point living : livingEntities)
			for (int x = -1; x < 2; x++)
				for (int y = -1; y < 2; y++) {
					Point p = new Point(living.x + x, living.y + y);
					if (!livingEntities.contains(p))
						deadCells.add(p);
				}
		return deadCells;
	}

	public void initWithRandom() {
		if (livingEntities == null) {
			livingEntities = new HashSet<Point>();
			for (int i = 0; i < 30; i++) {
				livingEntities.add(new Point((int) (5 - Math.random() * 10), (int) (5 - Math.random() * 10)));
			}
		}
	}

	public Set<Point> getCells() {
		return livingEntities;
	}
}
