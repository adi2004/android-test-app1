import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

// custom class for showing and resizing images easily
class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private AffineTransform affineTrans;
	private JScrollPane scrollPane;
	private RenderedImage image;
	// private JButton
	private int originX = 0, originY = 0;
	public double scale = 1, dependentScale = 1;
	public double finalScale;
	private boolean paintOnResize = true;
	private List<Point> pointList = new ArrayList<Point>();
	public Point testPoint[] = new Point[4];

	public ImagePanel(RenderedImage image) {
		super();
		setLayout(null);
		setImage(image);
	}

	// called from outer class to rescale the image
	public void reScale(double scaleFactor) {
		if (image == null) {
			return;
		}
		if (scaleFactor != 0) {
			scale = scaleFactor;
		}
		setPreferredSize(new Dimension((int) (image.getWidth() * dependentScale * scale), (int) (image.getHeight()
				* dependentScale * scale)));
		repaint();
		if (scrollPane != null)
			scrollPane.revalidate();
		else
			revalidate();
	}

	void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public void setImage(RenderedImage im) {
		image = im;
		Dimension size = new Dimension(im.getWidth(), im.getHeight());
		int xOffset = (int) (size.getWidth() * .1);
		int yOffset = (int) (size.getHeight() * .1);
		testPoint[0] = new Point(xOffset, yOffset);
		testPoint[1] = new Point((int) size.getWidth() - xOffset, yOffset);
		testPoint[2] = new Point((int) size.getWidth() - xOffset, (int) size.getHeight() - yOffset);
		testPoint[3] = new Point(xOffset, (int) size.getHeight() - yOffset);

		repaint();
	}

	private RenderedImage getImage() {
		return image;
	}

	// overwrite of the paint method to do transforming, rescaling and error
	// handling
	@Override
	public synchronized void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (image == null) {
			g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
			g2d.drawString("Image does not exist", 100, 100);
			return;
		}

		if (scrollPane != null) {
			dependentScale = getContainerDependingScaleFactor(image.getWidth(), image.getHeight(), scrollPane.getSize());
		}
		finalScale = dependentScale * scale;

		if (paintOnResize) {
			paintOnResize = false;
			setPreferredSize(new Dimension((int) (image.getWidth() * dependentScale * scale), (int) (image.getHeight()
					* dependentScale * scale)));
		}

		Insets insets = getInsets();
		int tx = insets.left + originX;
		int ty = insets.top + originY;

		Rectangle clipBounds = g2d.getClipBounds();
		g2d.setColor(getBackground());
		g2d.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);

		affineTrans = new AffineTransform();
		affineTrans.setTransform(AffineTransform.getTranslateInstance(tx, ty));
		if (finalScale != 0) {
			affineTrans.scale(finalScale, finalScale);
		}
		g2d.drawRenderedImage(image, affineTrans);

		// draw the lines
		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(Color.green);

		Point startPoint = testPoint[0];
		for (int i = 1; i <= 4; i++) {
			g2d.drawLine((int) ((startPoint.x + .5) * finalScale), (int) ((startPoint.y + .5) * finalScale),
					(int) ((testPoint[i % 4].x + .5) * finalScale), (int) ((testPoint[i % 4].y + .5) * finalScale));
			startPoint = testPoint[i % 4];
		}
		// Point first = null;
		// g2d.setStroke(new BasicStroke(5));
		// g2d.setColor(Color.GREEN);
		// for (Point p : pointList) {
		// if (first == null)
		// first = p;
		// g2d.drawLine((int) ((first.x + .5) * finalScale), (int) ((first.y +
		// .5) * finalScale),
		// (int) ((p.x + .5) * finalScale), (int) ((p.y + .5) * finalScale));
		// first = p;
		// }
	}

	// gets the scale factor to alwys show the whole picture in the image
	// panel
	private double getContainerDependingScaleFactor(int originalWidth, int originalHeight, Dimension requiredSize) {
		double factorWidth = (double) (requiredSize.width - 5) / (double) originalWidth;
		double factorHeight = (double) (requiredSize.height - 5) / (double) originalHeight;
		return Math.min(factorWidth, factorHeight);
	}

	public void addPoint(Point point) {
		pointList.add(point);
	}
}