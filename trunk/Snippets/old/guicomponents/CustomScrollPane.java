package com.ibm.opencv.guicomponents;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JScrollPane;

public class CustomScrollPane extends JScrollPane implements MouseWheelListener, ComponentListener {

	public CustomScrollPane(ImageComponent image) {
		super(image);
		addMouseWheelListener(this);
		addComponentListener(this);
	}

	private static final long serialVersionUID = -7913584227243274363L;

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		revalidate();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		System.out.println("Viewport bounds: " + getViewport().getBounds());
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
