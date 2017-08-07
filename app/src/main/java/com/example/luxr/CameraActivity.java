package com.example.luxr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.view.View.OnClickListener;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imgPic;
    private ImageView contourImg;
    private Bitmap contourBm;
    public static MakeHash makeHash = new MakeHash();
    private int sampleSize = 4;
    private String mCurrentPhotoPath;
    private File photoFile;
    public final String APP_TAG = "ClosetSpace";
    private final String fileName = "TEMP.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        onCreateViewSetup();
    }

    //populate the Main Camera View
    public void onCreateViewSetup() {
        imgPic = (ImageView) findViewById(R.id.imgPic);
        contourImg = (ImageView) findViewById(R.id.contourImg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.upload);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                uploadClicked(view);
            }
        });

        FloatingActionButton confirm = (FloatingActionButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmClicked();
            }
        });
    }

    private void confirmClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), ConfirmPhotoActivity.class);
        startActivity(intent);
    }

    //uploadClicked v1.0.0
    public void uploadClicked(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(fileName));
        System.out.println(getPhotoFileUri(fileName));
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            System.out.println(file.getPath().toString());
            return FileProvider.getUriForFile(CameraActivity.this, "com.example.luxr.fileprovider", file);
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if the user chooses OK, the code inside braces will execute
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                //we are hearing back from the camera
                Bitmap bm;
                System.out.println("hearing back");
                Uri takenPhotoUri = getPhotoFileUri(fileName);
                System.out.println("Actual: " + takenPhotoUri);
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    //opt.inSampleSize = 6;
                    opt.inMutable = true;
                    opt.inDensity = DisplayMetrics.DENSITY_LOW;
                    bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(takenPhotoUri), null, opt);
                    Bitmap edges = detectEdges(bm);
                    edges.setHasAlpha(true);
                    //top
                    contourImg.setImageBitmap(edges);
                    Bitmap edge2 = edges.copy(edges.getConfig(), true);
                    Bitmap cropped = imageCropping(edge2, bm);
                    cropped.setHasAlpha(true);
                    //imgPic is the one on bottom
                    imgPic.setImageBitmap(cropped);

                    FileHand fileHand = new FileHand(bm, this.getApplicationContext());
                    //get the current image's path
                    mCurrentPhotoPath = fileHand.getPhotoPath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private Bitmap imageCropping(Bitmap imgEdge, Bitmap orig) {
        ImageCropper imgCropper = new ImageCropper(imgEdge, orig);
        return imgCropper.orig;
    }

    private Bitmap detectEdges(Bitmap bitmap) {
        EdgeDetector detector = new EdgeDetector(bitmap);
        contourBm = detector.contourImage;
        return detector.currentImage;
    }

    //menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    public void favsClicked() {
        View v = new View(this);
        Intent intent = new Intent(v.getContext(), FavsActivity.class);
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

}
