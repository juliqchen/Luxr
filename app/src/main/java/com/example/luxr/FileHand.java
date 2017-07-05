package com.example.luxr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by juliachen on 7/5/17.
 */

public class FileHand {

    private static ArrayList<String> FilePathStrings = new ArrayList<>();
    private static ArrayList<String> FileNameStrings = new ArrayList<>();
    private static File myDir;
    private Context c;
    private File[] mediaDirs;

    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;

    public FileHand(Bitmap bitmap, Context c) {
        this.c = c;
        createImageFile();
        addImageFile(bitmap);
    }

    private File createImageFile() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString().trim();
        System.out.println("Media Store: " + c.getExternalMediaDirs().toString());
        mediaDirs = c.getExternalMediaDirs();

        for (File a : mediaDirs) {
            System.out.println("MSTORE: " + a.getPath());
            System.out.println("AbsPath: " + a.getAbsolutePath());
        }
        if (myDir == null) {
            myDir = new File(root + "/Luxr_images");
        }

        if (!myDir.exists()) {
            myDir.mkdirs();
            System.out.println("Directory Made: " + myDir.getAbsolutePath().toString());
        }
        return myDir;
    }

    private File addImageFile(Bitmap bitmap) {

        //create an image file name with timestamp
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PlantPlacesImage" + timestamp + ".jpg";
        mCurrentPhotoName = imageFileName.trim();
        FileNameStrings.add(mCurrentPhotoName);

        //put image in file
        File file = new File(myDir, imageFileName);
        System.out.println("exists: " + file.exists());
        System.out.println("directory: " + file.isDirectory());
        System.out.println("read: " + file.canRead());

        System.out.println(file.getAbsoluteFile().toString());

        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            myDir.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = file.getAbsolutePath();
        FilePathStrings.add(mCurrentPhotoPath);

        System.out.println(mCurrentPhotoPath);
        return file;
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

    public static File[] getListFile() {
        return myDir.listFiles();
    }

}
