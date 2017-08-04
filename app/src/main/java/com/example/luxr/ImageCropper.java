package com.example.luxr;

import android.app.assist.AssistStructure;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

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
    PixelQueue pq;

    public ImageCropper(Bitmap edge, Bitmap originalImg) {
        this.imgEdge = edge;
        this.copy = originalImg;

        this.height = imgEdge.getHeight();
        this.width = imgEdge.getWidth();
        System.out.println("about to transparent on this many pixels: " + imgEdge.getHeight() * imgEdge.getWidth());

        orig = copy;


        //http://opencv-java-tutorials.readthedocs.io/en/latest/07-image-segmentation.html
//        frame = new Mat();
//        Utils.bitmapToMat(imgEdge, frame);
//        Mat hsvImg = new Mat();
//        hsvImg.create(frame.size(), CvType.CV_8U);
//        Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
//        List<Mat> hsvPlanes = null;
//        Core.split(hsvImg, hsvPlanes);
//
//        List<Mat> hue = null;
//        Mat hist_hue = new Mat();
//        MatOfInt histSize = null;
//        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));
//        double average = 0;
//        for (int h = 0; h < 180; h++)
//            average += (hist_hue.get(h, 0)[0] * h);
//        average = average / hsvImg.size().height / hsvImg.size().width;
//
//        Mat thresholdImg = ;
//        double threshValue;
//        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);
//
//        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));
//        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
//        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);
//
//        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);
//
//        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
//        frame.copyTo(foreground, thresholdImg);

        //  WORKING!!!
        int x = 0;
        int y = 0;
//
        pq = new PixelQueue();
        pq.enqueue(new HashObject(0, 0));

        while (pq.hasItems() && (x >= 0 && x < imgEdge.getWidth() &&
                y >= 0 && y < imgEdge.getHeight())) {
            HashObject current = pq.dequeue();
            x = current.x;
            y = current.y;
            if (imgEdge.getPixel(x, y) == Color.BLACK) {
                imgEdge.setPixel(x, y, Color.TRANSPARENT);
                copy.setPixel(x, y, Color.TRANSPARENT);
            }
            if (x + 1 >= 0 && x + 1 < imgEdge.getWidth() &&
                    y >= 0 && y < imgEdge.getHeight() &&
                    imgEdge.getPixel(x + 1, y) == Color.BLACK) {
                imgEdge.setPixel(x + 1, y, Color.TRANSPARENT);
                copy.setPixel(x + 1, y, Color.TRANSPARENT);
                pq.enqueue(new HashObject(x + 1, y));
            }
            if (x - 1 >= 0 && x - 1 < imgEdge.getWidth() &&
                    y >= 0 && y < imgEdge.getHeight() &&
                    imgEdge.getPixel(x - 1, y) == Color.BLACK) {
                imgEdge.setPixel(x - 1, y, Color.TRANSPARENT);
                copy.setPixel(x - 1, y, Color.TRANSPARENT);
                pq.enqueue(new HashObject(x - 1, y));
            }
            if (x >= 0 && x < imgEdge.getWidth() &&
                    y + 1 >= 0 && y + 1 < imgEdge.getHeight() &&
                    imgEdge.getPixel(x, y + 1) == Color.BLACK) {
                imgEdge.setPixel(x, y + 1, Color.TRANSPARENT);
                copy.setPixel(x, y + 1, Color.TRANSPARENT);
                pq.enqueue(new HashObject(x, y + 1));
            }
            if (x >= 0 && x < imgEdge.getWidth() &&
                    y - 1 >= 0 && y - 1 < imgEdge.getHeight() &&
                    imgEdge.getPixel(x, y - 1) == Color.BLACK) {
                imgEdge.setPixel(x, y - 1, Color.TRANSPARENT);
                copy.setPixel(x, y - 1, Color.TRANSPARENT);
                pq.enqueue(new HashObject(x, y - 1));
            }
        }
    }

}