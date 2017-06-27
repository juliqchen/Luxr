package com.example.luxr;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by juliachen on 6/26/17.
 */

public class EdgeDetection {

    public static void main(String[] args) {
        //loading library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat color = Imgcodecs.imread(MainActivity.getmCurrentPhotoPath());

        Mat gray = new Mat();
        Mat draw = new Mat();
        Mat wide = new Mat();

        Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, wide, 50, 150, 3, false);
        wide.convertTo(draw, CvType.CV_8U);

        if (Imgcodecs.imwrite(MainActivity.getmCurrentPhotoPath() +  "edge.jpg", draw)) {
            System.out.println("Edge is detected");
        }
    }

}
