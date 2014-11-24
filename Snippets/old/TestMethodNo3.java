package com.ibm.opencv.test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * author: Carlo Morgenstern - Carlo Morgenstern/Germany/IBM version: 1.1 date:
 * 02.04.2014
 */
public class TestMethodNo3 implements LargestSquare {
	// the points for the largest detected Square
	java.awt.Point[] point = new java.awt.Point[4];
	ArrayList<java.awt.Point[]> squares = new ArrayList<>(0);// = new java.awt.Point[4];

	public TestMethodNo3() {
		// including OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	// the currently used edge detection algorithm
	public Mat editImage(Mat imageMat) {
		// gets all quadrilaterals found in the picture
		List<MatOfPoint> squares = find_squares(imageMat);

		// save the squares
		int i = 0;
		System.out.println("Printing " + squares.size() + " SQUARES");
		this.squares = new ArrayList<java.awt.Point[]>(squares.size());
		for (MatOfPoint sq : squares) {
			System.out.print("Square no. " + i++ + ": ");
			java.awt.Point[] points = new java.awt.Point[sq.toArray().length];
			int j = 0;
			for (Point p : sq.toArray()) {
				System.out.print(p);
				points[j++] = new java.awt.Point((int) p.x, (int) p.y);
			}
			this.squares.add(points);
			System.out.println();
		}

		// gets the largest one of them
		MatOfPoint largest_square = find_largest_square(squares);
		if (largest_square == null) {
			return imageMat;
		}

		Point[] pointsOfSquare = largest_square.toArray();
		i = 0;
		for (Point p : pointsOfSquare) {
			point[i++] = new java.awt.Point((int) p.x, (int) p.y);
		}

		return imageMat;
	}

	// simpler version of the edge detection algorithm above
	// does not go through each color plane, but combines them into a grayscale
	// image
	// is way faster and seems to be as accurate as the algorithm above
	// TODO simple improvement of edge detection algorithm
	private List<MatOfPoint> find_squares(Mat image) {
		List<MatOfPoint> result = new ArrayList<MatOfPoint>();

		// blur will enhance edge detection
		Mat blurred = image.clone();
		Imgproc.medianBlur(image, blurred, 9);
		Mat grayscaleBlurred = new Mat(blurred.size(), CvType.CV_8U);
		Imgproc.cvtColor(blurred, grayscaleBlurred, Imgproc.COLOR_BGRA2GRAY);

		Mat gray = new Mat(blurred.size(), CvType.CV_8U);

		int threshold_level = 2;
		for (int l = 0; l < threshold_level; l++) {
			// Use Canny instead of zero threshold level!
			// Canny helps to catch squares with gradient shading
			if (l == 0) {
				Imgproc.Canny(grayscaleBlurred, gray, 18, 20);

				// Dilate helps to remove potential holes between edge segments
				//Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1);
			} else {
				//Imgproc.threshold(grayscaleBlurred, gray, (l + 1) * 255 / threshold_level, 255, Imgproc.THRESH_BINARY);
			}

			// Find contours and store them in a list
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

			if (true)
				return contours;

			// Test contours
			for (int i = 0; i < contours.size(); i++) {
				// approximate contour with accuracy proportional
				// to the contour perimeter

				MatOfPoint2f curve = new MatOfPoint2f();
				contours.get(i).convertTo(curve, CvType.CV_32FC2);
				MatOfPoint2f approxedCurve = new MatOfPoint2f();
				MatOfPoint approxedCurveInt = new MatOfPoint();
				Imgproc.approxPolyDP(curve, approxedCurve, Imgproc.arcLength(curve, true) * 0.02, true);

				approxedCurve.convertTo(approxedCurveInt, CvType.CV_32S);

				result.add(new MatOfPoint(approxedCurveInt));
			}
		}
		return result;
	}

	// reiterate through squares to find the largest one
	private MatOfPoint find_largest_square(List<MatOfPoint> squares) {
		if (squares.size() == 0) {
			// no squares detected
			return null;
		}

		double largest_area = 0;
		int largest_square_index = 0;

		for (int i = 0; i < squares.size(); i++) {
			// convert set of 4 unordered Points to OpenCVs Rect structure
			Rect rectangle = Imgproc.boundingRect(new MatOfPoint(squares.get(i)));

			// store the index position of the largest square found
			if (rectangle.area() >= largest_area) {
				largest_area = rectangle.area();
				largest_square_index = i;
			}
		}
		return squares.get(largest_square_index);
	}

	@Override
	public java.awt.Point[] getPoints() {
		// check to see if we have any points set up
		if (point[0] == null) {
			// editImage(imageMat);
		}
		return point;
	}

	@Override
	public java.awt.Point[] getPoints(BufferedImage originalImage) {
		// mark the point list as clear
		point[0] = null;

		// getting byte[] of the original image
		Mat imageMat = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC4);

		// processing image with OpenCVEditor.editImage method and saving in
		// the dedicated imagePanel
		if (imageMat != null) {
			imageMat = editImage(imageMat);
		}

		return point;
	}

	@Override
	public ArrayList<java.awt.Point[]> getSquares() {
		return squares;
	}
	
	@Override
	public String toString() {
		return super.toString() + " doesn't find any squares";
	}
}
