package com.example.luxr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by juliachen on 7/11/17.
 */

public class ConfirmPhotoActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    String color;
    String type;

    View lastColor;
    View lastType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_confirm);



        //next button --> gallery activity
        Button next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                startActivity(intent);

                //toast to show which color and type was selected
                Toast.makeText(getApplicationContext(), color + " and " + type + " were selected.", Toast.LENGTH_LONG).show();
            }
        });

        //finds the expListView and populates with children
        expandableListView = (ExpandableListView) findViewById(R.id.expListView);
        expandableListDetail = ExpandableListViewData.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(),
                        //expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        //closes lists onClick
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(),
                        //expandableListTitle.get(groupPosition) + " List Collapsed.",
                        //Toast.LENGTH_SHORT).show();
            }
        });

        //opens lists onClick
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " -> " +
//                                expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition),
//                        Toast.LENGTH_SHORT).show();

                if (expandableListTitle.get(groupPosition).toLowerCase().equals("colors")) {
                    color = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                    TextView colorSelection = (TextView)findViewById(R.id.colorselection);
                    colorSelection.setText(color);
                    //Toast.makeText(getApplicationContext(), color + " was chosen.", Toast.LENGTH_SHORT).show();
                }

                if (expandableListTitle.get(groupPosition).toLowerCase().equals("types")) {
                    type = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                    TextView typeSelection = (TextView)findViewById(R.id.typeselection);
                    typeSelection.setText(type);
                    //Toast.makeText(getApplicationContext(), type + " was chosen.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}

