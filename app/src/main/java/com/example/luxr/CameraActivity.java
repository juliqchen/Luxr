package com.example.luxr;

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
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.view.View.OnClickListener;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 10;
    private ImageView imgPic;
    private ImageView contourImg;
    private Bitmap contourBm;
    public static MakeHash makeHash = new MakeHash();
    private Uri imageToUpload;
    private int sampleSize = 6;

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

        File root = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File myDir = new File(root.toString(), "Luxe_Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
            System.out.println("Directory Made: " + myDir.getAbsolutePath().toString());
        }

        File imgFile = new File(myDir, "TEMP_IMAGE.png");
        try {
            imgFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        imageToUpload = Uri.fromFile(imgFile);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user chooses OK, the code inside braces will execute
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            if (imageToUpload != null) {
                //we are hearing back from the camera
                System.out.println("hearing back");

                //Bitmap option setting
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = sampleSize;
                //bitmapOptions.inDensity = DisplayMetrics.DENSITY_DEVICE_STABLE;
                bitmapOptions.inMutable = true;

                Bitmap cameraImage = BitmapFactory.decodeFile(imageToUpload.getPath(), bitmapOptions);
//                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                Bitmap imgEdge = detectEdges(cameraImage);
                imgPic.setImageBitmap(imgEdge);
                contourImg.setImageBitmap(contourBm);

                Bitmap croppedImage = imageCropping(contourBm, cameraImage);
                imgPic.setImageBitmap(croppedImage);
                imgPic.setBackgroundColor(Color.WHITE);
                System.out.println("File saved");
                FileHand fileHand = new FileHand(croppedImage, this.getApplicationContext());

            } else {
                Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG);
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
