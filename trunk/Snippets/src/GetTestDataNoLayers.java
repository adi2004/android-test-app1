import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GetTestDataNoLayers {
	public static final String IMG_PATH = "D:\\Workspaces\\ADTJunoWorkspace\\Snippets\\images\\";

	public static void main(String[] args2) {
		System.out.println("test... showing image...");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					(new GetTestDataNoLayers()).initAndDisplay();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	int boxSize = 20;
	int clickedPoint;
	int currentImg = 0;
	File[] files;
	JFrame frame;
	BufferedImage img = null;
	ImagePanel imgPanel = null;
	JLabel info;
	JLayeredPane layeredPane = null;
	JScrollPane scrollPane = null;
	String imgExtensions[] = { "jpg", "jpeg", "png", "bmp" };

	void initAndDisplay() {
		// read and save all images from the IMG_PATH directory
		File fileNames = new File(IMG_PATH);
		ArrayList<File> imgFiles = new ArrayList<File>();
		for (File f : fileNames.listFiles()) {
			System.out.println(f);
			for (String ext : imgExtensions) {
				if (ext.equalsIgnoreCase(f.getName().substring(f.getName().length() - 3, f.getName().length()))) {
					imgFiles.add(f);
				}
			}
		}
		Object[] objArray = imgFiles.toArray();
		files = Arrays.copyOf(objArray, objArray.length, File[].class);
		// (File[]) (imgFiles.toArray());

		frame = new JFrame();

		// method 1: just display the img
		// a JFrame contains a JScrollPane which contains a ImagePanel

		// read the image & create the ImagePanel
		readImg(files[currentImg]);
		imgPanel = new ImagePanel(img);
		loadImg(files[0]);

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

		// add components to the frame
		frame.setLayout(new BorderLayout());
		//

		frame.add(scrollPane, BorderLayout.CENTER);
		// frame.add(layeredPane, BorderLayout.CENTER);
		// frame.add(testData, BorderLayout.CENTER);
		frame.add(buttonsPanel, BorderLayout.SOUTH);

		// ------------------
		// -adding listeners-
		// ------------------
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (currentImg + 1) % files.length;
				loadImg(files[currentImg]);
			}
		});
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentImg = (files.length + currentImg - 1) % files.length;
				loadImg(files[currentImg]);
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("In frame: " + e);
			}
		});
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getPreciseWheelRotation() > 0) {
					imgPanel.reScale(imgPanel.scale * 1.1);
				} else {
					imgPanel.reScale(imgPanel.scale * 0.9);
				}
			}
		});

		scrollPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (clickedPoint < imgPanel.testPoint.length) {
					Point coordsInImg = convertCoords(e.getPoint());
					imgPanel.testPoint[clickedPoint] = coordsInImg;
					scrollPane.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				Point coordsInImg = convertCoords(e.getPoint());
				for (Point point : imgPanel.testPoint) {
					Rectangle pointBounds = new Rectangle((int) point.getX() - boxSize, (int) point.getY() - boxSize,
							2 * boxSize, 2 * boxSize);
					if (pointBounds.contains(coordsInImg)) {
						imgPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						return;
					}
				}
				imgPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point coordsInImg = convertCoords(e.getPoint());
				for (int i = 0; i < imgPanel.testPoint.length; i++) {
					Rectangle pointBounds = new Rectangle((int) imgPanel.testPoint[i].getX() - boxSize,
							(int) imgPanel.testPoint[i].getY() - boxSize, 2 * boxSize, 2 * boxSize);
					if (pointBounds.contains(coordsInImg)) {
						clickedPoint = i;
						return;
					}
				}
				clickedPoint = imgPanel.testPoint.length;
			};

			@Override
			public void mouseReleased(MouseEvent e) {
				clickedPoint = imgPanel.testPoint.length;
				System.out.print("Point list: ");
				for (Point p : imgPanel.testPoint) {
					System.out.print(p + " ");
				}
				System.out.println();
				saveToFile(IMG_PATH
						+ files[currentImg].getName().substring(0, files[currentImg].getName().length() - 4) + ".txt");
			}
		});

		// --------------------------------
		// -preparing to display the frame-
		// --------------------------------
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

	private Point convertCoords(Point e) {
		return new Point((int) ((e.getX() + scrollPane.getViewport().getViewPosition().getX()) / imgPanel.finalScale),
				(int) ((e.getY() + scrollPane.getViewport().getViewPosition().getY()) / imgPanel.finalScale));
	}

	private void saveToFile(String name) {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(imgPanel.testPoint);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadImg(File file) {
		readImg(file);
		imgPanel.setImage(img);
		if (info != null)
			info.setText(file.getName());

		// open the file <img_name>.txt
		try {
			FileInputStream fis = new FileInputStream(IMG_PATH
					+ file.getName().substring(0, file.getName().length() - 4) + ".txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			imgPanel.testPoint = (Point[]) ois.readObject();
			ois.close();
		} catch (Exception e) {
		}
	}
}
