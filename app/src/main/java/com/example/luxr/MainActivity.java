package com.example.luxr;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 10;
    private ImageView imgPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fabClicked(view);
            }
        });

        imgPic =(ImageView) findViewById(R.id.imgPic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //fabClicked v1.0.0
//    public void fabClicked(View v) {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureName = getPictureName();
//        File imageFile = new File(pictureDirectory, pictureName);
//        Uri pictureUri = Uri.fromFile(imageFile);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//    }

    //fabClicked v1.0.1
    public void fabClicked(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //finds the path and then makes a new folder called Luxr
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        System.out.println(path);
        File dir = new File(path, "Luxr");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        //adds the picture to this new directory
        String pictureName = getPictureName();
        File imageFile = new File(dir, pictureName);
        Uri pictureUri = Uri.fromFile(imageFile);

        //??String imagePath = imageFile.getAbsolutePath();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "PlantPlacesImage" + timestamp + ".jpg";
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

    //public void saveClicked(){
    //    View v = new View(this);
    //
    //}

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
        Intent intent = new Intent(v.getContext(), SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user chooses OK, the code inside braces will execute
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
//                //we are hearing back from the camera
//                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
//                //at this point, we have image from camera
//                imgPic.setImageBitmap(cameraImage);
            }
        }
    }
}
