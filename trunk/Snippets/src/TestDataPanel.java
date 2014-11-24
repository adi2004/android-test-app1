import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.Transient;

import javax.swing.JComponent;

public class TestDataPanel extends JComponent {
	private static final long serialVersionUID = 3293852529472837227L;
	Dimension size;
	Point point[] = new Point[4];

	public TestDataPanel(Dimension size) {
		/* ******
		 * *0**1* 
		 * ****** 
		 * *2**3* 
		 * ******
		 */
		this.size = size;
		setPreferredSize(size);
		int xOffset = (int) (size.getWidth() * .1);
		int yOffset = (int) (size.getHeight() * .1);
		point[0] = new Point(xOffset, yOffset);
		point[1] = new Point((int) size.getWidth() - xOffset, yOffset);
		point[2] = new Point((int) size.getWidth() - xOffset, (int) size.getHeight() - yOffset);
		point[3] = new Point(xOffset, (int) size.getHeight() - yOffset);
		
		setBounds(new Rectangle(size));
		//setPreferredSize(new Dimension(size));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(new Color(0xFF00));

		Point startPoint = point[0];
		for (int i = 1; i <= 4; i++) {
			g2d.drawLine(startPoint.x, startPoint.y, point[i%4].x, point[i%4].y);
			startPoint = point[i%4];
		}
		//setPreferredSize(new Dim);
	}
	
	@Override
	@Transient
	public Dimension getPreferredSize() {
		return size;
	}
}
