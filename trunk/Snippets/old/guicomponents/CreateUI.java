package com.ibm.opencv.guicomponents;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class CreateUI {
	private static final String PATH = "testimages-res\\";

	CreateUI() {
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());

		// add components to frame
		frame.add(createToolbar(), BorderLayout.PAGE_START);
		
		// img
		ImageComponent image = new ImageComponent("");
		CustomScrollPane scrollPane = new CustomScrollPane(image);
		frame.add(scrollPane, BorderLayout.CENTER);
		
		// files
		CustomFileList fileList = new CustomFileList(PATH);
		fileList.addObserver(image);
		JScrollPane fileListScrollPanel = new JScrollPane(fileList);
		frame.add(fileListScrollPanel, BorderLayout.WEST);

		// preparing to display the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		System.out.println("Bounds: " + frame.getBounds());
		System.out.println("Preferred size: " + frame.getPreferredSize());
		frame.setLocation(300, 300);
		frame.setTitle("Hello observers!");
		// show it now
		frame.setVisible(true);
	}

	private Component createFileList() {
		CustomFileList fileList = new CustomFileList(PATH);
		JScrollPane fileListScrollPanel = new JScrollPane(fileList);

		return fileListScrollPanel;
	}

	private Component createResizableImagePanel() {
		ImageComponent image = new ImageComponent("testimages-res\\test_squares1.jpg");
		CustomScrollPane scrollPane = new CustomScrollPane(image);

		return scrollPane;
	}

	private JToolBar createToolbar() {
		// create a tool bar
		CustomToolBar toolBar = new CustomToolBar("Actions");

		return toolBar;
	}

	public static void main(String... string) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CreateUI();
			}
		});
	}
}
