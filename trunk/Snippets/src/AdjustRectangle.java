import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AdjustRectangle extends JPanel {
	private static final long serialVersionUID = -6106328321233269965L;
	TestDataPanel tdP = null;
	JLayeredPane layers = null;
	int boxSize = 20;
	int clickedPoint;

	void initAndDisplay() {
		JFrame frame = new JFrame();

		// read an image
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("D:\\Workspaces\\ADTJunoWorkspace\\Snippets\\images\\deutsch-words.jpg"));
		} catch (Exception e) {
		}
		int w = img.getWidth(), h = img.getHeight();
		ImagePanel imgPanel = new ImagePanel(img);
		imgPanel.setBounds(0, 0, w, h);

		layers = new JLayeredPane();
		tdP = new TestDataPanel(new Dimension(w, h));
		layers.add(tdP);
		layers.add(imgPanel);
		layers.setPreferredSize(new Dimension(w, h));

		JScrollPane scrollPane = new JScrollPane(layers);
		frame.add(scrollPane);

		System.out.println(layers.getBounds());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		if (frame.getBounds().getWidth() < 200)
			frame.setBounds(100, 100, 200, 200);
		frame.setVisible(true);

		// add action listeners
		tdP.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				for (int i = 0; i < tdP.point.length; i++) {
					Rectangle pointBounds = new Rectangle((int) tdP.point[i].getX() - boxSize, (int) tdP.point[i]
							.getY() - boxSize, (int) tdP.point[i].getX() + boxSize, (int) tdP.point[i].getY() + boxSize);
					if (pointBounds.contains(e.getPoint())) {
						clickedPoint = i;
						return;
					}
				}
				clickedPoint = tdP.point.length;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickedPoint = tdP.point.length;
			}
		});
		tdP.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (clickedPoint < tdP.point.length) {
					tdP.point[clickedPoint] = new Point(e.getX(), e.getY());
					layers.repaint();
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				for (Point point : tdP.point) {
					Rectangle pointBounds = new Rectangle((int) point.getX() - boxSize, (int) point.getY() - boxSize,
							(int) point.getX() + boxSize, (int) point.getY() + boxSize);
					if (pointBounds.contains(e.getPoint())) {
						tdP.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						return;
					}
				}
				tdP.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				(new AdjustRectangle()).initAndDisplay();
			}
		});
	}
}
