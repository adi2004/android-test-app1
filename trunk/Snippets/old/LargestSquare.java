package com.ibm.opencv.test;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface LargestSquare {
	Point[] getPoints();

	Point[] getPoints(BufferedImage originalImage);

	ArrayList<Point[]> getSquares();
	
//	BufferedImage getEditedImage();
}
