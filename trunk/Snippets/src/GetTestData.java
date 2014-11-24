import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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

public class GetTestData {
	JFrame frame;
	ImagePanel imgPanel = null;
	BufferedImage img = null;
	JScrollPane scrollPane = null;
	JLayeredPane layeredPane = null;
	TestDataPanel testData = null;
	int currentImg = 0;
	int clickedPoint;
	final int boxSize = 20;
	JLabel info;
	public static final String IMG_PATH = "D:\\Workspaces\\ADTJunoWorkspace\\Snippets\\images\\";
	File[] files;

	public static void main(String[] args2) {
		System.out.println("test... showing image...");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					(new GetTestData()).initAndDisplay();
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

		// add a JLayeredPane to scrollPane, and add 2 layers to JLayeredPane
		layeredPane = new JLayeredPane();
		readImg(files[currentImg]);
		int w = img.getWidth(), h = img.getHeight();
		imgPanel = new ImagePanel(img);
		imgPanel.setBounds(0, 0, w, h);

		// create TestDataPanel
		testData = new TestDataPanel(new Dimension(w, h));

		layeredPane.add(testData);
		layeredPane.add(imgPanel);
		//layeredPane.setPreferredSize(new Dimension(w, h));

		// add to scrollPane
		scrollPane = new JScrollPane(layeredPane);
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

		// add components to the frame
		frame.setLayout(new BorderLayout());
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(buttonsPanel, BorderLayout.SOUTH);
		// --------------------------------
		// -preparing to display the frame-
		// --------------------------------
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocation(100, 100);
		frame.setTitle(IMG_PATH);
		// show it now
		frame.setVisible(true);

		// ------------------
		// -adding listeners-
		// ------------------
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (currentImg + 1) % files.length;
				displayImg(files[currentImg]);
			}
		});
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (files.length + currentImg - 1) % files.length;
				displayImg(files[currentImg]);
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("In frame: " + e);
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
				layeredPane.setPreferredSize(new Dimension((int) (img.getWidth() * imgPanel.finalScale),
						(int) (imgPanel.getHeight() * imgPanel.finalScale)));
				layeredPane.setBounds(0, 0, (int) (img.getWidth() * imgPanel.finalScale),
						(int) (imgPanel.getHeight() * imgPanel.finalScale));
				layeredPane.repaint();
				scrollPane.repaint();
			}
		});
		testData.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				for (int i = 0; i < testData.point.length; i++) {
					Rectangle pointBounds = new Rectangle((int) testData.point[i].getX() - boxSize,
							(int) testData.point[i].getY() - boxSize, (int) testData.point[i].getX() + boxSize,
							(int) testData.point[i].getY() + boxSize);
					if (pointBounds.contains(e.getPoint())) {
						clickedPoint = i;
						return;
					}
				}
				clickedPoint = testData.point.length;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickedPoint = testData.point.length;
			}
		});
		testData.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (clickedPoint < testData.point.length) {
					testData.point[clickedPoint] = new Point(e.getX(), e.getY());
					layeredPane.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				for (Point point : testData.point) {
					Rectangle pointBounds = new Rectangle((int) point.getX() - boxSize, (int) point.getY() - boxSize,
							(int) point.getX() + boxSize, (int) point.getY() + boxSize);
					if (pointBounds.contains(e.getPoint())) {
						testData.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						return;
					}
				}
				testData.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	private void readImg(File file) {
		try {
			img = ImageIO.read(file);
		} catch (Exception e) {
		}
	}

	private void displayImg(File file) {
		readImg(file);
		imgPanel.setImage(img);
		info.setText(files[currentImg].getName());
		layeredPane.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		testData.setSize(img.getWidth(), img.getHeight());
	}
}
