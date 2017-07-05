package com.example.luxr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.OnClickListener;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 10;
    private static final String TAG = "Luxr::MainActivity";
    private ImageView imgPic;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;

    private static File myDir;

    public ArrayList<String> FilePathStrings;
    public ArrayList<String> FileNameStrings;
    public File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgPic =(ImageView) findViewById(R.id.imgPic);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fabClicked(view);
            }
        });

        createImageFile();
        FilePathStrings = new ArrayList<String>();
        FileNameStrings = new ArrayList<String>();

//        TabLayout bottTab = (TabLayout) findViewById(R.id.bottomTab);
//        bottTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        })

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //fabClicked v1.0.0
    public void fabClicked(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user chooses OK, the code inside braces will execute
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                //we are hearing back from the camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                Bitmap imgEdge = detectEdges(cameraImage);
                imgPic.setImageBitmap(imgEdge);
                File savedImage = addImageFile(imgEdge);

                listFile = myDir.listFiles();
                FilePathStrings.add(mCurrentPhotoPath);
                FileNameStrings.add(mCurrentPhotoName);
            }
        }
    }

    private File createImageFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/Luxr_images");
        if (!myDir.isDirectory()) {
            myDir.mkdirs();
        }
        return myDir;
    }

    private File addImageFile(Bitmap bitmap) {
        //create an image file name with timestamp
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PlantPlacesImage" + timestamp + ".jpg";
        mCurrentPhotoName = imageFileName;

        //put image in file
        File file = new File(myDir, imageFileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("File could not be saved as JPEG");
        }

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = file.getAbsolutePath();
        System.out.println(mCurrentPhotoPath);
        return file;
    }

    public static File getMyDir() {
        return myDir;
    }

    private Bitmap detectEdges(Bitmap bitmap) {
        EdgeDetector detector = new EdgeDetector(bitmap);
        return detector.currentImage;
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                    Log.i(TAG, "OpenCV loading failed");
                } break;
            }
        }
    };
    
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
        } /*else if (id == R.id.action_settings) {
            settingsClicked();
            return true;
        }*/ else if (id == R.id.action_home) {
            homeClicked();
            return true;
        } else if (id == R.id.action_gallery){
            galleryClicked();
            return true;
        } else if (id == R.id.action_style){
            styleClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void styleClicked(){
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), StyleActivity.class);
        startActivity(intent);
    }

    public void galleryClicked(){
        View v = new View(this);
        Intent intent = new Intent(v.getContext(),GalleryActivity.class);
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

    /*public void settingsClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), StyleActivity.class);
        startActivity(intent);
    }*/

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }
}
