package com.example.luxr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by jenniferhu on 8/1/17.
 */

public class FavsGridAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> data;
    private ArrayList<String> name;

    private static LayoutInflater inflater = null;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
