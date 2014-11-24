package com.ibm.opencv.guicomponents;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class CustomToolBar extends JToolBar {
	private static final long serialVersionUID = -7802152164516438638L;

	public CustomToolBar(String name) {
		super(name);
		// create buttons for navigation
		JButton previous = new JButton(new ImageIcon("gui-res\\Back24.gif"));
		add(previous);
		JButton next = new JButton(new ImageIcon("gui-res\\Forward24.gif"));
		add(next);
		JToggleButton showTestData = new JToggleButton(new ImageIcon("gui-res\\testData.gif"), true);
		showTestData.setToolTipText("Toogle test data (green square) on/off.");
		add(showTestData);
		JToggleButton showFoundPoints = new JToggleButton(new ImageIcon("gui-res\\foundPoints.gif"), true);
		showFoundPoints.setToolTipText("Toogle found points (blue square) on/off.");
		add(showFoundPoints);
		JToggleButton showAllSquares = new JToggleButton(new ImageIcon("gui-res\\allSquares.gif"), true);
		showAllSquares.setToolTipText("Toogle all found squares (blue squares) on/off.");
		add(showAllSquares);

		// add test methods
		//toolBar.add(createTestMethods());
		JButton runAllTestsButton = new JButton(new ImageIcon("gui-res\\play.gif"));
		runAllTestsButton.setToolTipText("Run test for all loaded files");
		add(runAllTestsButton);

//		// add listeners
//		next.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				currentImg = (currentImg + 1) % files.length;
//				fileList.setSelectedIndex(currentImg);
//			}
//		});
//
//		previous.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				currentImg = (files.length + currentImg - 1) % files.length;
//				fileList.setSelectedIndex(currentImg);
//			}
//		});
//
//		showTestData.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				editedImagePanel.displayTestData = !editedImagePanel.displayTestData;
//				editedImagePanel.repaint();
//			}
//		});
//
//		showFoundPoints.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				editedImagePanel.displayFoundPoints = !editedImagePanel.displayFoundPoints;
//				editedImagePanel.repaint();
//			}
//		});
//
//		showAllSquares.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				editedImagePanel.displaySquares = !editedImagePanel.displaySquares;
//				editedImagePanel.repaint();
//			}
//		});
//
//		runAllTestsButton.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				new Thread() {
//					@Override
//					public void run() {
//						listModel = new String[files.length];
//						for (int i = 0; i < files.length; i++) {
//							listModel[i] = "<html>" + files[i].getName() + " <font color=blue>laden...</font></html>";
//							fileList.setListData(listModel);
//							updateImage(files[i]);
//							int nrOfPointsFound = test();
//							if (nrOfPointsFound == 4) {
//								listModel[i] = "<html>" + files[i].getName()
//										+ " <font color=green>bestanden</font></html>";
//							} else {
//								listModel[i] = "<html>" + files[i].getName() + " <font color=red>gefallen "
//										+ nrOfPointsFound + "/4</font></html>";
//							}
//							fileList.setListData(listModel);
//						}
//					}
//				}.start();
//			}
//		});
	}
}
