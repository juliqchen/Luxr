package com.example.luxr;

import android.graphics.Bitmap;


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

    public ImageCropper(Bitmap imgEdge, Bitmap originalImg) {
        this.imgEdge = imgEdge;
        this.orig = originalImg;
        this.copy = orig.copy(orig.getConfig(), true);
        this.height = imgEdge.getHeight();
        this.width = imgEdge.getWidth();
        System.out.println("about to transparent on this many pixels: " + imgEdge.getHeight() * imgEdge.getWidth());
        trackPix = new boolean[width][height];

        PixelQueue pq = new PixelQueue();

        whileCrop(1, 1);
//        recursiveCrop(1, 1);
//        for (double a : trackPix) {
//            System.out.println(a);
//        }
        orig = copy;

        //imagePrinter(imgEdge);
    }

    private void whileCrop(int x, int y) {
        //if ();
    }

//    private void recursiveCrop(int x, int y) {
//        if (x >= 0 && x < imgEdge.getWidth() && y >= 0 && y < imgEdge.getHeight()
//                && !trackPix[x][y] && imgEdge.getPixel(x, y) == Color.BLACK) {
//            copy.setPixel(x, y, Color.TRANSPARENT);
//            trackPix[x][y] = true;
//            recursiveCrop(x + 1, y);
//            recursiveCrop(x - 1, y);
//            recursiveCrop(x, y + 1);
//            recursiveCrop(x, y - 1);
//        }
//    }

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