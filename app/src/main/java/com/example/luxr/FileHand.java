package com.example.luxr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by juliachen on 7/5/17.
 */

public class FileHand extends AppCompatActivity{

    private static ArrayList<String> FilePathStrings = new ArrayList<>();
    private static ArrayList<String> FileNameStrings = new ArrayList<>();
    private static File myDir;
    private Context c;
    private File[] mediaDirs;
    private String imageFileName;


    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;

    public static MakeHash makeHash = new MakeHash();

    public FileHand(Bitmap bitmap, Context c) {
        this.c = c;
        createImageFile(bitmap);
        System.out.println(MakeHash.closet.containsKey("Tops"));
    }

    private File createImageFile(Bitmap bitmap) {
        File root = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        myDir = new File(root.toString(), "Luxe_Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
            System.out.println("Directory Made: " + myDir.getAbsolutePath().toString());
        }

        File imgFile = new File(myDir, createFileName());
        try {
            imgFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            System.out.println("File saved as PNG");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imgFile.getAbsolutePath();
        FilePathStrings.add(mCurrentPhotoPath);

        System.out.println("mCPP: " + mCurrentPhotoPath);

        for (String path: FilePathStrings) {
            scanMedia(path);
            System.out.println("File Scanned");
        }

        return imgFile;
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        c.sendBroadcast(scanFileIntent);
    }

    private String createFileName() {
        //create an image file name with timestamp
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        imageFileName = "Luxe" + timestamp + ".png";
        mCurrentPhotoName = imageFileName.trim();
        FileNameStrings.add(mCurrentPhotoName);
        return imageFileName;
    }

    public static File getMyDir() {
        return myDir;
    }

    public static ArrayList<String> getFilePathStrings() {
        return FilePathStrings;
    }

    public static ArrayList<String> getFileNameStrings() {
        return FileNameStrings;
    }

}
