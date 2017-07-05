package com.example.luxr;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by juliachen on 6/26/17.
 */

public class EdgeDetector {

    Bitmap currentImage;
    Mat rgba;

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
    }


}
