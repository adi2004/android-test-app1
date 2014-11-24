import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DisplayImg {
	JFrame frame;
	ImagePanel imgPanel = null;
	BufferedImage img = null;
	JScrollPane scrollPane = null;
	JLayeredPane layeredPane = null;
	int currentImg = 0;
	JLabel info;
	public static final String IMG_PATH = "D:\\Workspaces\\ADTJunoWorkspace\\Snippets\\images\\";
	File[] files;

	// public static final String IMG_PATH =
	// "D:\\Workspaces\\ADTJunoWorkspace\\Snippets\\images\\img1.jpg";

	public static void main(String[] args2) {
		System.out.println("test... showing image...");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					(new DisplayImg()).initAndDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void initAndDisplay() {
		// read and save all images from the IMG_PATH directory
		File fileNames = new File(IMG_PATH);
		for (File f : fileNames.listFiles()) {
			System.out.println(f);
		}
		files = fileNames.listFiles();

		frame = new JFrame();

		// method 1: just display the img
		// a JFrame contains a JScrollPane which contains a ImagePanel

		// read the image & create the ImagePanel
		readImg(files[currentImg]);
		imgPanel = new ImagePanel(img);

		// create scroll pane
		scrollPane = new JScrollPane(imgPanel);
		scrollPane.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		imgPanel.setScrollPane(scrollPane);

		// create buttons for navigation
		JButton previous = new JButton("<");
		JButton next = new JButton(">");
		info = new JLabel("Loaded " + files.length + " images.");
		JPanel buttonsPanel = new JPanel();
		Box buttonsBox = Box.createHorizontalBox();
		buttonsPanel.setLayout(new BorderLayout());
		buttonsBox.add(previous);
		buttonsBox.add(next);
		buttonsPanel.add(buttonsBox, BorderLayout.EAST);
		buttonsPanel.add(info, BorderLayout.WEST);

		// create TestDataPanel
		// TestDataPanel testData = new TestDataPanel(new Dimension(300, 300));
		// testData.setPreferredSize(new Dimension(300, 300));
		// scrollPane.add(testData);

		// add components to the frame
		frame.setLayout(new BorderLayout());
		//
		
		frame.add(scrollPane, BorderLayout.CENTER);
		//frame.add(layeredPane, BorderLayout.CENTER);
		// frame.add(testData, BorderLayout.CENTER);
		frame.add(buttonsPanel, BorderLayout.SOUTH);

		// ------------------
		// -adding listeners-
		// ------------------
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (currentImg + 1) % files.length;
				readImg(files[currentImg]);
				imgPanel.setImage(img);
				info.setText(files[currentImg].getName());
			}
		});
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (files.length + currentImg - 1) % files.length;
				readImg(files[currentImg]);
				imgPanel.setImage(img);
				info.setText(files[currentImg].getName());
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("In frame: " + e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
			}
		});
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println(frame.getSize());
			}
		});
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// System.out.println("Mouse wheel moved: " + e);
				if (e.getPreciseWheelRotation() > 0) {
					imgPanel.reScale(imgPanel.scale * 1.1);
				} else {
					imgPanel.reScale(imgPanel.scale * 0.9);
				}
				// scrollPane.setLocation(new Point(30, 30));

			}
		});
		scrollPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// Point coordsInImg = new Point(
				// (int) ((e.getX() +
				// scrollPane.getViewport().getViewPosition().getX()) /
				// imgPanel.finalScale),
				// (int) ((e.getY() +
				// scrollPane.getViewport().getViewPosition().getY()) /
				// imgPanel.finalScale));
				// System.out.println("Coords in img: " + coordsInImg);
				// imgPanel.addPoint(coordsInImg);
				// scrollPane.repaint();
				// Graphics2D g2d = (Graphics2D) scrollPane.getGraphics();
				// g2d.drawLine(10, 10, 200, 200);
				// g2d.dispose();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// System.out.println("Size: "
				// + scrollPane.getViewport().getViewSize());
				Point coordsInImg = new Point(
						(int) ((e.getX() + scrollPane.getViewport().getViewPosition().getX()) / imgPanel.finalScale),
						(int) ((e.getY() + scrollPane.getViewport().getViewPosition().getY()) / imgPanel.finalScale));
				System.out.println("Coords in img: " + coordsInImg);
				imgPanel.addPoint(coordsInImg);
				scrollPane.repaint();
			};
		});

		// --------------------------------
		// -preparing to display the frame-
		// --------------------------------
		// frame.setBounds(50, 50, 200, 200);
		// frame.setMinimumSize(new Dimension(200, 200));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocation(100, 100);
		frame.setTitle(IMG_PATH);
		// show it now
		frame.setVisible(true);
	}

	private void readImg(File file) {
		try {
			img = ImageIO.read(file);
		} catch (Exception e) {
		}
	}
}
