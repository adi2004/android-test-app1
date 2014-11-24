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

// this should be a very simple implementation of the LargestSquare interface
public class SimpleLargestSquare implements LargestSquare {
	// the points for the largest detected Square
	java.awt.Point[] point = new java.awt.Point[4000];
	ArrayList<java.awt.Point[]> squares = new ArrayList<java.awt.Point[]>();

	@Override
	public java.awt.Point[] getPoints() {
		return point;
	}

	@Override
	public java.awt.Point[] getPoints(BufferedImage originalImage) {
		Mat openCVImg = convertImage(originalImage);

		// blur
		Mat blurred = openCVImg.clone();
		Imgproc.medianBlur(openCVImg, blurred, 9);
		// convert to grayscale
		Mat grayscaleBlurred = new Mat(openCVImg.size(), CvType.CV_8U);
		Imgproc.cvtColor(blurred, grayscaleBlurred, Imgproc.COLOR_BGRA2GRAY);
		// apply canny
		Mat canny = new Mat(openCVImg.size(), CvType.CV_8U);
		Imgproc.Canny(grayscaleBlurred, canny, 18, 20);
		// dilate
		Mat dilate = new Mat(openCVImg.size(), CvType.CV_8U);
		Imgproc.dilate(canny, dilate, new Mat(), new Point(-1, -1), 1);
		// cut levels
		Mat levels = new Mat(openCVImg.size(), CvType.CV_8U);
		Imgproc.threshold(grayscaleBlurred, levels, 255, 255, Imgproc.THRESH_BINARY);

		// Find contours and store them in a list
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(dilate, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("Number of contours found " + contours.size());

		if (contours.size() < 1)
			return point;

		// return the largest contour
//		MatOfPoint largestSq = contours.get(0);
//		double largestSqArea = 0;
//		int i = 0;
//		for (MatOfPoint matOfPoint : contours) {
//			Rect rectangle = Imgproc.boundingRect(new MatOfPoint(matOfPoint));
//			if (rectangle.area() > largestSqArea) {
//				largestSqArea = rectangle.area();
//				matOfPoint.copyTo(largestSq);
//				System.out.println("Area " + largestSqArea);
//			}
//		}
//		for (int j = 0; j < contours.size(); j++) {
//			//if (contours.get(j).)
//			Rect rectangle = Imgproc.boundingRect(new MatOfPoint(contours.get(j)));
//			if (rectangle.area() > largestSqArea) {
//				largestSqArea = rectangle.area();
//				//squares.add(convertToJavaAwtPoint(contours.get(j)));
//				i = j;
//				System.out.println("Area " + largestSqArea + "; index " + i + "; points " + contours.get(i));
//			}
//		}
//		for (i = 0; i < contours.size(); i++) {
//			// approximate contour with accuracy proportional
//			// to the contour perimeter
//
//			MatOfPoint2f curve = new MatOfPoint2f();
//			contours.get(i).convertTo(curve, CvType.CV_32FC2);
//			MatOfPoint2f approxedCurve = new MatOfPoint2f();
//			MatOfPoint approxedCurveInt = new MatOfPoint();
//			Imgproc.approxPolyDP(curve, approxedCurve, Imgproc.arcLength(curve, true) * 0.02, true);
//
//			approxedCurve.convertTo(approxedCurveInt, CvType.CV_32S);
//
//			squares.add(convertToJavaAwtPoint(new MatOfPoint(approxedCurveInt)));
//		}
//		squares.add(convertToJavaAwtPoint(new MatOfPoint(contours.get(0))));
//		squares.add(convertToJavaAwtPoint(new MatOfPoint(contours.get(1))));
//		squares.add(convertToJavaAwtPoint(new MatOfPoint(contours.get(2))));
//		squares.add(convertToJavaAwtPoint(new MatOfPoint(contours.get(3))));
//		squares.add(convertToJavaAwtPoint(new MatOfPoint(contours.get(4))));
		// Test contours
		List<MatOfPoint> result = new ArrayList<MatOfPoint>();
		for (int i = 0; i < contours.size(); i++) {
			// approximate contour with accuracy proportional
			// to the contour perimeter

			MatOfPoint2f curve = new MatOfPoint2f();
			contours.get(i).convertTo(curve, CvType.CV_32FC2);
			MatOfPoint2f approxedCurve = new MatOfPoint2f();
			MatOfPoint approxedCurveInt = new MatOfPoint();
			Imgproc.approxPolyDP(curve, approxedCurve, Imgproc.arcLength(curve, true) * 0.02, true);

			approxedCurve.convertTo(approxedCurveInt, CvType.CV_32S);

			squares.add(convertToJavaAwtPoint(new MatOfPoint(approxedCurveInt)));
			result.add(new MatOfPoint(approxedCurveInt));
		}

		// get largest sq
		double largest_area = 0;
		int largest_square_index = 0;
		for (int i = 0; i < result.size(); i++) {
			// convert set of 4 unordered Points to OpenCVs Rect structure
			Rect rectangle = Imgproc.boundingRect(new MatOfPoint(result.get(i)));

			// store the index position of the largest square found
			if (rectangle.area() >= largest_area) {
				largest_area = rectangle.area();
				largest_square_index = i;
			}
		}
		return convertToJavaAwtPoint(result.get(largest_square_index));
	}

	private java.awt.Point[] convertToJavaAwtPoint(MatOfPoint largestSq) {
		java.awt.Point javaAwtPoint[] = new java.awt.Point[4000];
		int i = 0;
		for (Point p : largestSq.toArray()) {
			if (i < 4000)
				javaAwtPoint[i++] = new java.awt.Point((int) p.x, (int) p.y);
		}
		return javaAwtPoint;
	}

	private Mat convertImage(BufferedImage originalImage) {
		// getting byte[] of the original image
		byte[] colorChannelValues = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
		Mat imageMat = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC4);

		// converting image type (number of color channels) to one type, to make processing easier
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
		return imageMat;
	}

	@Override
	public ArrayList<java.awt.Point[]> getSquares() {
		return squares;
	}

	@Override
	public String toString() {
		return super.toString() + " basic, doesn't work";
	}
}
