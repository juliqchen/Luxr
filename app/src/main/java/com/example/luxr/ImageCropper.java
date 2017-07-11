package com.example.luxr;

import android.graphics.Bitmap;

/**
 * Created by juliachen on 7/10/17.
 */

class ImageCropper {

    Bitmap imgEdge;


    public ImageCropper(Bitmap imgEdge) {
        this.imgEdge = imgEdge;
        imagePrinter(imgEdge);
    }

    private void imagePrinter(Bitmap imgEdge) {
        for (int i = 0; i < imgEdge.getWidth(); i++) {
            for (int j = 0; j < imgEdge.getHeight(); j++) {
                int pixel = imgEdge.getPixel(i, j);
                if (pixel == -1) {

                }

            }
        }
    }
}
