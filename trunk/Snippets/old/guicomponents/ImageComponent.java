package com.ibm.opencv.guicomponents;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageComponent extends JComponent implements MouseWheelListener, ImageChangeObserver {
	private static final long serialVersionUID = 1983419615008268646L;

	BufferedImage img;
	float scale = .2f;

	public ImageComponent(String imgPath) {
		img = readFromFile(new File(imgPath));
		addMouseWheelListener(this);
	}

	private BufferedImage readFromFile(File file) {
		try {
			BufferedImage img = ImageIO.read(file);
			return img;
		} catch (Exception e) {
			System.out.println("File not found at location " + file);
			return null;
		}
	}

	public ImageComponent(BufferedImage img) {
		this.img = img;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (img == null) {
			g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
			g2d.drawString("Image does not exist", 10, 100);
			return;
		}

		AffineTransform affineTrans = new AffineTransform();
		if (scale != 0) {
			affineTrans.scale(scale, scale);
		}
		g2d.drawRenderedImage(img, affineTrans);
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		if (img != null)
			return new Dimension((int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
		else
			return new Dimension(200, 200);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() > 0) {
			scale *= .9;
		} else {
			scale *= 1.1;
		}

		// trigger the component change event
		setSize(new Dimension(400, 400));
	}

	@Override
	public void imageChanged(File file) {
		img = readFromFile(file);
		
		setSize(new Dimension(400, 400));
	}
}
