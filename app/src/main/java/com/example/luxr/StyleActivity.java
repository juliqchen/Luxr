package com.example.luxr;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
import java.util.ArrayList;

/**
 * Created by jenniferhu on 6/30/17.
 */

public class StyleActivity extends AppCompatActivity {
    File file;
    ArrayList<String> FilePathStrings;
    ArrayList<String> FileNameStrings;
    File[] listFile;
    GridView grid;
    StyleGridAdapter adapter;
    ImageView items;
    String msg;
    RelativeLayout layout;
    private android.widget.RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        if (FileHand.getMyDir() != null) {
            //populating gridview with gallery images
            file = FileHand.getMyDir();
            FilePathStrings = FileHand.getFilePathStrings();
            FileNameStrings = FileHand.getFileNameStrings();
            listFile = file.listFiles();

            grid = (GridView) findViewById(R.id.styleGrid);
            adapter = new StyleGridAdapter(this, FilePathStrings, FileNameStrings);
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

                    System.out.println(parent.getItemAtPosition(i));

                    ImageView newView = new ImageView(StyleActivity.this);
                    newView.setImageBitmap(bm);
                    RelativeLayout layout = (RelativeLayout) StyleActivity.this.findViewById(R.id.styleFrame);
                    layout.addView(newView);
                    attachDragDropListen(newView);
                }
            });

            layout = (RelativeLayout) findViewById(R.id.styleFrame);

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


    //drag and drop from gridview onto imageview
//            grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long l) {
//                    ClipData.Item item = (ClipData.Item) parent.getItemAtPosition(pos);
//                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//                    ClipData dragData = new ClipData(view.getTag().toString(), mimeTypes, item);
//
//                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder();
//                    view.startDrag(dragData,myShadow,null,0);
//                    return true;
//                }
//            });



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
