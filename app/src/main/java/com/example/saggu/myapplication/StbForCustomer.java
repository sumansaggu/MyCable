package com.example.saggu.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StbForCustomer extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DbHendler dbHendler;
    int custId;
    String TAG = "StbForCustomerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stb_for_customer);
        dbHendler = new DbHendler(this, null, null, 1);
        Bundle bundle = getIntent().getExtras();
        custId = bundle.getInt("ID");
        prepareListData();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandable_list_stb);
        // preparing list data

        listAdapter = new ExpandableStbList(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("STBs");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_audiotrack_dark));
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        // Adding header data
        listDataHeader = new ArrayList<String>();
        Log.d(TAG, "customer id " + custId);
        listDataHeader = dbHendler.getSTBsForCustomer(custId);
        listDataChild = new HashMap<String, List<String>>();
        Log.d(TAG, "prepareListData: listdataHeader "+listDataHeader);
        Log.d(TAG, "prepareListData: length"+ listDataHeader.size());
        Log.d(TAG, "prepareListData: "+listDataHeader.get(0));
        int i;
        for (i=0;i<listDataHeader.size();i++){
            List<String>  pkgChild = new ArrayList<String>();
            pkgChild =dbHendler.getPackListOnSTB(listDataHeader.get(i));
            listDataChild.put(listDataHeader.get(i),pkgChild);
        }

        // Adding child data


       /* List<String> top250 = new ArrayList<String>();
       // top250 = dbHendler.getPackListOnSTB(1);
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

      //  listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
      //  listDataChild.put(listDataHeader.get(1), nowShowing);
      //  listDataChild.put(listDataHeader.get(2), comingSoon);*/
        Log.d(TAG, "prepareListData: "+listDataChild);
    }

}
