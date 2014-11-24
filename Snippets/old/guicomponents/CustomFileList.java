package com.ibm.opencv.guicomponents;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CustomFileList extends JList<String> {
	private static final long serialVersionUID = -6472293795698279359L;
	private static final String imgExtensions[] = { "jpg", "jpeg", "png", "bmp", "wbmp", "tiff", "gif" };
	
	String path;
	ArrayList<ImageChangeObserver> observers = new ArrayList<ImageChangeObserver>();
	String[] listModel;
	File[] files;

	public CustomFileList(String path) {
		super();
		this.path = path;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setMinimumSize(new Dimension(150, 100));

		createAndReadImagesList();

		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					notifyObservers(files[getSelectedIndex()]);
				}
			}
		});
	}

	protected void notifyObservers(File file) {
		for (ImageChangeObserver observer : observers) {
			observer.imageChanged(file);
		}
	}

	private void createAndReadImagesList() {
		// read and save all images from the imgPath directory
		File fileNames = new File(path);
		ArrayList<String> imageListName = new ArrayList<String>();
		ArrayList<File> imgFiles = new ArrayList<File>();
		for (File f : fileNames.listFiles()) {
			System.out.println(f);
			// check to see if the extension is supported
			for (String ext : imgExtensions) {
				String imageExtension = f.getName().substring(f.getName().lastIndexOf('.') + 1, f.getName().length());
				if (ext.equalsIgnoreCase(imageExtension)) {
					imgFiles.add(f);
					imageListName.add("<html><font color=blue>" + f.getName() + "</font></html>");
				}
			}
		}
		
		Object[] objArray = imgFiles.toArray();
		files = Arrays.copyOf(objArray, objArray.length, File[].class);
		objArray = imageListName.toArray();
		listModel = Arrays.copyOf(objArray, objArray.length, String[].class);

		// set the fileList to display the files
		setListData(listModel);
		// setSelectedIndex(currentImg);
	}

	void addObserver(ImageChangeObserver observer) {
		observers.add(observer);
	}
}
