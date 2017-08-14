package com.example.luxr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jenniferhu on 6/30/17.
 */

public class StyleActivity extends AppCompatActivity {
    File file;
    private static ArrayList<String> StyleFilePathStrings = new ArrayList<>();
    private static ArrayList<String> StyleFileNameStrings = new ArrayList<>();
    private static ArrayList<String> outfitFilePathStrings = new ArrayList<>();
    private static ArrayList<String> outfitFileNameStrings = new ArrayList<>();
    File[] listFile;
    GridView grid;
    StyleGridAdapter adapter;
    ImageView items;
    String msg;
    RelativeLayout layout;
    public static File myStyleDir;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    String mCurrentPhotoPath;
    private String imageFileName;
    private String mCurrentPhotoName;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        if (FileHand.getMyDir() != null) {
            //populating gridview with gallery images
            file = FileHand.getMyDir();
            StyleFilePathStrings = FileHand.getFilePathStrings();
            StyleFileNameStrings = FileHand.getFileNameStrings();
            listFile = file.listFiles();

            grid = (GridView) findViewById(R.id.styleGrid);
            adapter = new StyleGridAdapter(this, StyleFilePathStrings, StyleFileNameStrings);
            grid.setAdapter(adapter);

            //spinner for style activity
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.style_spinner, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            //save button
            Button save = (Button) findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new intent goes to faves/saved looks page
                    Bitmap outfit = layout.getDrawingCache();
                    createImageFile(outfit);

                    Intent intent = new Intent(view.getContext(), FavsActivity.class);
                    startActivity(intent);
                }
            });

            //add image to the left space
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                    Toast.makeText(StyleActivity.this, "Photo Selected", Toast.LENGTH_SHORT).show();
                    Bitmap bm = (Bitmap) grid.getAdapter().getItem(i);
                    ImageView newView = new ImageView(StyleActivity.this);
                    newView.setImageBitmap(bm);
                    RelativeLayout layout = (RelativeLayout) StyleActivity.this.findViewById(R.id.styleFrame);
                    layout.addView(newView);
                    attachDragDropListen(newView);
                }
            });

            layout = (RelativeLayout) findViewById(R.id.styleFrame);
            layout.setDrawingCacheEnabled(true);

            //dragging
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    layout.bringChildToFront(v);
                    switch(event.getAction()){
                        case DragEvent.ACTION_DRAG_STARTED:
                            layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            //do nothing
                            break;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            int x_cord = (int) event.getX();
                            int y_cord = (int) event.getY();
                            break;

                        case DragEvent.ACTION_DRAG_EXITED :
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            layoutParams.leftMargin = x_cord;
                            layoutParams.topMargin = y_cord;
                            v.setLayoutParams(layoutParams);
                            break;

                        case DragEvent.ACTION_DRAG_LOCATION  :
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            break;

                        case DragEvent.ACTION_DRAG_ENDED   :
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                            // Do nothing
                            break;

                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");
                            // Do nothing
                            break;

                        default: break;
                    }
                    return true;
                }
            });
        } else {
            Toast.makeText(StyleActivity.this, "You have no photos :(", Toast.LENGTH_SHORT).show();
        }
    }

    float x_cord;
    float y_cord;
    float gapX, gapY;

    private void attachDragDropListen(final ImageView img) {
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x_cord = event.getRawX();
                        y_cord = event.getRawY();
                        gapX = x_cord - img.getX();
                        gapY = y_cord - img.getY();

                        swapDepth(img);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        x_cord = event.getRawX();
                        y_cord = event.getRawY();

                        if (x_cord < 0+gapX) {
                            x_cord = 0+gapX;
                        }
                        if (y_cord < 0+gapY) {
                            y_cord = 0+gapY;
                        }

                        img.setX(x_cord-gapX);
                        img.setY(y_cord-gapY);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void swapDepth(ImageView img) {
    }


    //menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            accountClicked();
            return true;
        } else if (id == R.id.action_upload) {
            uploadClicked();
            return true;
        } else if (id == R.id.action_home) {
            homeClicked();
            return true;
        } else if (id == R.id.action_gallery){
            galleryClicked();
            return true;
        } else if (id == R.id.action_style){
            styleClicked();
            return true;
        } else if (id == R.id.action_favs){
            favsClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void favsClicked(){
        View v = new View(this);
        Intent intent = new Intent(v.getContext(),FavsActivity.class);
        startActivity(intent);
    }

    private File createImageFile(Bitmap bitmap) {
        File root = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        myStyleDir = new File(root.toString(), "Luxe_Outfits");
        if (!myStyleDir.exists()) {
            myStyleDir.mkdirs();
            System.out.println("Directory Made: " + myStyleDir.getAbsolutePath().toString());
        }

        File imgFile = new File(myStyleDir, createFileName());
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
        outfitFilePathStrings.add(mCurrentPhotoPath);

        System.out.println("mCPP: " + mCurrentPhotoPath);

        for (String path: outfitFilePathStrings) {
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
        sendBroadcast(scanFileIntent);
    }

    private String createFileName() {
        //create an image file name with timestamp
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        imageFileName = "LuxeOutfit" + timestamp + ".png";
        mCurrentPhotoName = imageFileName.trim();
        outfitFileNameStrings.add(mCurrentPhotoName);
        return imageFileName;
    }

    public static File getMyStyleDir() {
        return myStyleDir;
    }

    public static ArrayList<String> getOutfitFilePathStrings() {
        return outfitFilePathStrings;
    }

    public static ArrayList<String> getOutfitFileNameStrings() {
        return outfitFileNameStrings;
    }

    public void styleClicked(){
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), StyleActivity.class);
        startActivity(intent);
    }

    public void galleryClicked(){
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), GalleryActivity.class);
        startActivity(intent);
    }

    public void homeClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void accountClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), AccountActivity.class);
        startActivity(intent);
    }

    public void uploadClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), CameraActivity.class);
        startActivity(intent);
    }
}
