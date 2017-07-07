package com.example.luxr;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juliachen on 6/26/17.
 */

public class EdgeDetector {

    Bitmap currentImage;
    Bitmap contourImage;
    Mat rgba;
    Mat hierarchy;
    Mat secondMat;
    List<MatOfPoint> contours;

    EdgeDetector(Bitmap bitmap) {
        rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);
        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        //BitmapHelper.showBitmap(this, bitmap, imgPic);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
        //BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
        currentImage = resultBitmap;

        //drawContours init
        contours = new ArrayList<>();
        hierarchy = new Mat();

        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        //drawContours init

        Mat drawMat = new Mat(edges.size(), CvType.CV_8UC1);

        //if any contour exists
        if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
            System.out.println("Found Contour");
            //for each contour, display it in blue
            for (int i = 0; i >= 0; i = (int) hierarchy.get(0, i)[0]) {
                Imgproc.drawContours(drawMat, contours, i, new Scalar(250, 0, 0));
            }
        }

        //Bitmap
        resultBitmap = Bitmap.createBitmap(drawMat.cols(), drawMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(drawMat, resultBitmap);
        contourImage = resultBitmap;

    }


}
