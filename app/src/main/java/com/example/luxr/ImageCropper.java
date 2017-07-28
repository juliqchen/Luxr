package com.example.luxr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.grabCut;


/**
 * Created by juliachen on 7/10/17.
 */

class ImageCropper {

    Bitmap imgEdge;
    Bitmap orig;
    Bitmap copy;
    int height;
    int width;
    boolean[][] trackPix;
    Mat temp;
    Mat image;
    Rect rectang;

    public ImageCropper(Bitmap imgEdge, Bitmap originalImg) {
        this.imgEdge = imgEdge;
        this.orig = originalImg;
        this.copy = orig.copy(orig.getConfig(), true);
        this.height = imgEdge.getHeight();
        this.width = imgEdge.getWidth();
        System.out.println("about to transparent on this many pixels: " + imgEdge.getHeight() * imgEdge.getWidth());
        trackPix = new boolean[width][height];
//
//        PixelQueue pq = new PixelQueue();

//        whileCrop();

        recursiveCrop(0, 0);
        orig = copy;

        //imagePrinter(imgEdge);
    }

//    private void whileCrop() {
//        temp = new Mat();
//        image = new Mat();
//        copy.setDensity(DisplayMetrics.DENSITY_LOW);
//        Utils.bitmapToMat(copy, temp);
//        Imgproc.cvtColor(temp, image, Imgproc.COLOR_RGBA2RGB);
//        image.convertTo(image, CvType.CV_8UC3);
//
//        rectang = new Rect(100, 100, image.width() - 100, image.height() - 100);
//
//        Mat result = new Mat();
//        Mat bgModel = new Mat();
//        Mat fgModel = new Mat();
//        bgModel.setTo(new Scalar(255, 255, 255));
//        fgModel.setTo(new Scalar(255, 255, 255));
//        Mat source = new Mat(1, 1, CvType.CV_8UC3, new Scalar(3.0));
//
//        grabCut(image, result, rectang, bgModel, fgModel, 3);
//
//        Core.compare(result, source, result, Core.CMP_EQ);
//        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255,255,255));
//        image.copyTo(foreground, result);
//
//        Utils.matToBitmap(foreground, orig);
//    }


    private void recursiveCrop(int x, int y) {
        if (x >= 0 && x < imgEdge.getWidth() && y >= 0 && y < imgEdge.getHeight()
                && !trackPix[x][y] && imgEdge.getPixel(x, y) == Color.BLACK) {
            copy.setPixel(x, y, Color.TRANSPARENT);
            trackPix[x][y] = true;
            recursiveCrop(x + 1, y);
            recursiveCrop(x - 1, y);
            recursiveCrop(x, y + 1);
            recursiveCrop(x, y - 1);
        }
    }

//    private void imagePrinter(Bitmap imgEdge) {
//        for (int i = 0; i < imgEdge.getHeight(); i++) {
//            for (int j = 0; j < imgEdge.getWidth(); j++) {
//                int pixel = imgEdge.getPixel(j, i);
//                if (pixel == Color.BLACK) {
//                    copy.setPixel(j, i, Color.TRANSPARENT);
//                } else if (pixel == Color.WHITE){
//                    break;
//                }
//
//            }
//        }
//        for (int i = imgEdge.getHeight() - 1; i >= 0; i--) {
//            for (int j = imgEdge.getWidth() - 1; j >= 0; j--) {
//                int pixel = imgEdge.getPixel(j, i);
//                if (pixel == Color.BLACK) {
//                    copy.setPixel(j, i, Color.TRANSPARENT);
//                } else if (pixel == Color.WHITE){
//                    break;
//                }
//            }
//        }
//        for (int i = 0; i < imgEdge.getWidth(); i++) {
//            for (int j = 0; j < imgEdge.getWidth(); j++) {
//                int pixel = imgEdge.getPixel(i, j);
//                if (pixel == Color.BLACK) {
//                    copy.setPixel(i, j, Color.TRANSPARENT);
//                } else if (pixel == Color.WHITE){
//                    break;
//                }
//            }
//        }
//        for (int i = imgEdge.getWidth() - 1; i >= 0; i--) {
//            for (int j = imgEdge.getWidth() - 1; j >= 0; j--) {
//                int pixel = imgEdge.getPixel(i, j);
//                if (pixel == Color.BLACK) {
//                    copy.setPixel(i, j, Color.TRANSPARENT);
//                } else if (pixel == Color.WHITE){
//                    break;
//                }
//            }
//        }
//        orig = copy;
//    }
}