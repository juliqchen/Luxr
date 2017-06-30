package com.example.luxr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Mat rgba;
    private static final int CAMERA_REQUEST = 10;
    private static final String TAG = "Luxr::MainActivity";
    private ImageView imgPic;
    private static String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout bottomBar = (TabLayout) findViewById(R.id.bottomTab);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        imgPic =(ImageView) findViewById(R.id.imgPic);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fabClicked(view);
            }
        });

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

        //not working from StackOverFlow
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureName = getPictureName();
//        File imageFile = new File(pictureDirectory, pictureName);
//        Uri pictureUri = Uri.fromFile(imageFile);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

//        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            //create the file where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                System.out.println("Error: Image file was not created");
//            }
//
//            //continue iff the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(photoFile);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        }

        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user chooses OK, the code inside braces will execute
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                //we are hearing back from the camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                Bitmap imgEdge = detectEdges(cameraImage);
//                //at this point, we have image from camera
                imgPic.setImageBitmap(imgEdge);

            }
        }
    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PlantPlacesImage" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println(mCurrentPhotoPath);
        return image;
    }

//    private String getPictureName() {
//        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
//        return "PlantPlacesImage" + timestamp + ".jpg";
//    }

    private Bitmap detectEdges(Bitmap bitmap) {

        rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

//        BitmapHelper.showBitmap(this, bitmap, imgPic);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
//        BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
        return resultBitmap;
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
        } else if (id == R.id.action_settings) {
            settingsClicked();
            return true;
        } else if (id == R.id.action_home) {
            homeClicked();
            return true;
        } //else if (id == R.id.save_button){
        //saveClicked();
        //return true;
        //}
        return super.onOptionsItemSelected(item);
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

    public void settingsClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), GalleryActivity.class);
        startActivity(intent);
    }

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }
}
