package com.example.luxr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by jenniferhu on 8/1/17.
 */

public class FavsGridAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> data;
    private ArrayList<String> name;

    private static LayoutInflater inflater = null;

    public FavsGridAdapter(Activity galleryActivity, ArrayList<String> FilePathStrings, ArrayList<String> FileNameStrings) {
        this.activity = galleryActivity;
        this.data = FilePathStrings;
        this.name = FileNameStrings;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(activity.getApplicationContext());
            imageView.setLayoutParams(new GridView.LayoutParams(280, 280));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(Color.WHITE);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(data.get(i));
        imageView.setImageBitmap(myBitmap);
        return imageView;
    }
}
