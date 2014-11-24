package gameoflife;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Set;

public class GameView {
	Game g;
	int cycle;

	public GameView(Game g) {
		this.g = g;
	}

	public void update() {
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e1) {
			System.out.println("Error with clear...");
			e1.printStackTrace();
		}

		System.out.println("=== Cycle " + cycle++ + " ===");

		Set<Point> aliveCells = g.getCells();
		//displayFixedMat(aliveCells);
		//displayFixedMatUsintStringBuilder(aliveCells);
		//displayVariableSizeMat(aliveCells);
		displayBounds(aliveCells);

//		try {
//			Thread.sleep(25);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	private void displayBounds(Set<Point> aliveCells) {
		System.out.println(getBounds(aliveCells));
	}

	private void displayVariableSizeMat(Set<Point> aliveCells) {
		Rectangle rect = getBounds(aliveCells);
		CharType displayMat[][] = new CharType[rect.width + 1][rect.height + 1];
		drawOXLine(rect, displayMat);
		drawOYLine(rect, displayMat);
		drawCells(aliveCells, rect, displayMat);
		//displayMatWithString(rect, displayMat);
		displayMatWithStringBuilder(rect, displayMat);
	}

	private void displayMatWithStringBuilder(Rectangle rect, CharType[][] displayMat) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y <= rect.height; y++) {
			for (int x = 0; x <= rect.width; x++) {
				switch (displayMat[x][y]) {
				case CELL:
					sb.append("o ");
					break;
				case VLINE:
					sb.append("| ");
					break;
				case OLINE:
					sb.append("--");
					break;
				case ORIGIN:
					sb.append("X ");
					break;
				default:
					sb.append("  ");
					break;
				}
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

	private void displayMatWithString(Rectangle rect, CharType[][] displayMat) {
		for (int y = 0; y <= rect.height; y++) {
			for (int x = 0; x <= rect.width; x++) {
				switch (displayMat[x][y]) {
				case CELL:
					System.out.print("o ");
					break;
				case VLINE:
					System.out.print("| ");
					break;
				case OLINE:
					System.out.print("--");
					break;
				case ORIGIN:
					System.out.print("X ");
					break;
				default:
					System.out.print("  ");
					break;
				}
			}
			System.out.println();
		}
	}

	private void drawCells(Set<Point> aliveCells, Rectangle rect, CharType[][] displayMat) {
		for (int y = 0; y <= rect.height; y++)
			for (int x = 0; x <= rect.width; x++)
				if (aliveCells.contains(new Point(rect.x + x, rect.y + y)))
					displayMat[x][y] = CharType.CELL;
				else if (displayMat[x][y] == null)
					displayMat[x][y] = CharType.EMPTY;
	}

	private void drawOXLine(Rectangle rect, CharType[][] displayMat) {
		// get intersection with oX
		int x1 = rect.x, x2 = rect.x + rect.width;
		if (x1 <= 0 || 0 <= x2) {
			for (int x = 0; x <= rect.width; x++) {
				displayMat[x][-rect.y] = CharType.OLINE;
			}
		}
	}

	private void drawOYLine(Rectangle rect, CharType[][] displayMat) {
		// get intersection with oY
		int y1 = rect.y, y2 = rect.y + rect.height;
		if (y1 <= 0 || 0 <= y2) {
			for (int y = 0; y <= rect.height; y++) {
				displayMat[-rect.x][y] = CharType.VLINE;
			}
		}
	}

	private void displayFixedMat(Set<Point> aliveCells) {
		for (int y = -20; y <= 20; y++) {
			for (int x = -20; x <= 20; x++) {
				if (aliveCells.contains(new Point(x, y))) {
					System.out.print("o ");
				} else {
					System.out.print(". ");
				}
			}
			System.out.println();
		}
	}
	
	private void displayFixedMatUsintStringBuilder(Set<Point> aliveCells) {
		StringBuilder sb = new StringBuilder();
		for (int y = -20; y <= 20; y++) {
			for (int x = -20; x <= 20; x++) {
				if (aliveCells.contains(new Point(x, y))) {
					sb.append("o ");
				} else {
					sb.append(". ");
				}
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}
	private Rectangle getBounds(Set<Point> aliveCells) {
		int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		for (Point p : aliveCells) {
			if (p.x < x1)
				x1 = p.x;
			if (p.y < y1)
				y1 = p.y;
			if (p.x > x2)
				x2 = p.x;
			if (p.y > y2)
				y2 = p.y;
		}
		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}
}
