package com.example.luxr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
    private String imageFileName;

    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;

    public FileHand(Bitmap bitmap, Context c) {
        this.c = c;
        createImageFile(bitmap);
    }

    private File createImageFile(Bitmap bitmap) {
        File root = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        myDir = new File(root.toString(), "Luxr_Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
            System.out.println("Directory Made: " + myDir.getAbsolutePath().toString());
        }
        System.out.println("YAHOO: " + myDir.exists());
        File pic = new File(myDir, "yellojello");

        System.out.println("YellowJello: " + pic.exists());

        File imgFile = new File(myDir, createFileName());
        try {
            imgFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("exists: " + imgFile.exists());
        System.out.println("directory: " + imgFile.isDirectory());
        System.out.println("read: " + imgFile.canRead());
        System.out.println("write: " + imgFile.canWrite());

        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imgFile.getAbsolutePath();
        FilePathStrings.add(mCurrentPhotoPath);

        System.out.println("mCPP: " + mCurrentPhotoPath);

        //MediaScanner
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(myDir.getAbsolutePath().toString());
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            c.sendBroadcast(mediaScanIntent);
        }
        else
        {
            c.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

        return imgFile;
    }

    private String createFileName() {
        //create an image file name with timestamp
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        imageFileName = "PlantPlacesImage" + timestamp + ".jpg";
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

    public static File[] getListFile() {
        return myDir.listFiles();
    }

}
