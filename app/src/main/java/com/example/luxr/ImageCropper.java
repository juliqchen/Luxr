package com.example.luxr;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by juliachen on 7/10/17.
 */

class ImageCropper {

    Bitmap imgEdge;
    Bitmap orig;
    Bitmap copy;


    public ImageCropper(Bitmap imgEdge, Bitmap originalImg) {
        this.imgEdge = imgEdge;
        this.orig = originalImg;
        copy = orig.copy(orig.getConfig(), true);
        System.out.println("about to transparent on this many pixels: " + imgEdge.getHeight() * imgEdge.getWidth());

        imagePrinter(imgEdge);
    }

    private void imagePrinter(Bitmap imgEdge) {
        for (int i = 0; i < imgEdge.getHeight(); i++) {
            for (int j = 0; j < imgEdge.getWidth(); j++) {
                int pixel = imgEdge.getPixel(j, i);
                if (pixel == Color.BLACK) {
                    copy.setPixel(j, i, Color.TRANSPARENT);
                } else if (pixel == Color.WHITE){
                    break;
                }

            }
        }
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
//        for (int i = imgEdge.getWidth() + 1; i >= 0; i--) {
//            for (int j = imgEdge.getWidth() - 1; j >= 0; j--) {
//                int pixel = imgEdge.getPixel(i, j);
//                if (pixel == Color.BLACK) {
//                    copy.setPixel(i, j, Color.TRANSPARENT);
//                } else if (pixel == Color.WHITE){
//                    break;
//                }
//            }
//        }
        orig = copy;
    }
}
