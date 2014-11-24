package com.ibm.opencv.test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * author: Carlo Morgenstern - Carlo Morgenstern/Germany/IBM version: 1.1 date:
 * 02.04.2014
 */
public class TestMethodNo2 implements LargestSquare {
	// the points for the largest detected Square
	java.awt.Point[] point = new java.awt.Point[4];

	// the currently used edge detection algorithm
	public Mat editImage(Mat imageMat) {
		// gets all quadrilaterals found in the picture
		List<MatOfPoint> squares = find_squares(imageMat);

		// gets the largest one of them
		MatOfPoint largest_square = find_largest_square(squares);
		if (largest_square == null) {
			return imageMat;
		}

		Point[] pointsOfSquare = largest_square.toArray();
		int i = 0;
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
				Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1);
			} else {
				Imgproc.threshold(grayscaleBlurred, gray, (l + 1) * 255 / threshold_level, 255, Imgproc.THRESH_BINARY);
			}

			// Find contours and store them in a list
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

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

				// Note: absolute value of an area is used because
				// area may be positive or negative - in accordance with the
				// contour orientation
				if (approxedCurve.total() == 4 && Math.abs(Imgproc.contourArea(approxedCurve)) > 1000
						&& Imgproc.isContourConvex(new MatOfPoint(approxedCurveInt))) {
					double maxCosine = 0;

					for (int j = 2; j < 5; j++) {
						Point[] approxPoints = approxedCurve.toArray();
						double cosine = Math.abs(angle(approxPoints[j % 4], approxPoints[j - 2], approxPoints[j - 1]));
						maxCosine = Math.max(maxCosine, cosine);
					}

					if (maxCosine < 1) {
						result.add(new MatOfPoint(approxedCurveInt));
					} else {
						// System.out.println(maxCosine);
					}
				}
			}
		}
		return result;
	}

	private double angle(Point pt1, Point pt2, Point pt0) {
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
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
		byte[] colorChannelValues = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
		Mat imageMat = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC4);

		// converting image type (number of color channels) to one type, to
		// make processing easier
		switch (originalImage.getType()) {
		case BufferedImage.TYPE_BYTE_BINARY:
			System.out.println("Image type: binary - binary support is not implemented");

			byte[] newPixels = new byte[colorChannelValues.length * 8];
			int index = 0;
			for (byte b : colorChannelValues) {
				for (int i = 0; i <= 7; i++) {
					if ((b & (1 << i)) != 0) {
						newPixels[index] = -1;
					} else {
						newPixels[index] = 0;
					}
					index++;
				}
			}
			colorChannelValues = newPixels;
		case BufferedImage.TYPE_BYTE_GRAY:
			Mat tmpMat = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC1);
			tmpMat.put(0, 0, colorChannelValues);
			Imgproc.cvtColor(tmpMat, imageMat, Imgproc.COLOR_GRAY2BGRA);
			break;
		case BufferedImage.TYPE_3BYTE_BGR:
			Mat tempMat = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC3);
			tempMat.put(0, 0, colorChannelValues);
			Imgproc.cvtColor(tempMat, imageMat, Imgproc.COLOR_BGR2BGRA);
			break;
		case BufferedImage.TYPE_4BYTE_ABGR:
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
			imageMat.put(0, 0, colorChannelValues);
			break;
		default:
			new Exception("Custom: BufferedImage.getType() not supported - cannot work with this image type: "
					+ originalImage.getType()).printStackTrace();
		}

		// processing image with OpenCVEditor.editImage method and saving in
		// the dedicated imagePanel
		if (imageMat != null) {
			imageMat = editImage(imageMat);

			Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGRA2RGBA);

			byte[] imageData = new byte[imageMat.cols() * imageMat.rows() * (int) imageMat.elemSize()];
			imageMat.get(0, 0, imageData);

			BufferedImage editedImage = new BufferedImage(imageMat.cols(), imageMat.rows(),
					BufferedImage.TYPE_4BYTE_ABGR);
			editedImage.getRaster().setDataElements(0, 0, imageMat.cols(), imageMat.rows(), imageData);
		}

		return point;
	}

	@Override
	public ArrayList<java.awt.Point[]> getSquares() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + " works, no fail, now editing";
	}
}
