package com.example.saggu.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Channel_Selection_List extends AppCompatActivity {
    String sn;
    RecyclerView recyclerView;
    AdapterChannelRecyclerView adapter;
    ArrayList<ChannelObject> mylist = new ArrayList<>();
    String TAG = "CHANNEL SELECTION LIST";
    DbHendler dbHendler;
    private List<ChannelObject> channellist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel__selection__list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sn = bundle.getString("sn");
            Toast.makeText(this, "" + sn, Toast.LENGTH_SHORT).show();
            dbHendler = new DbHendler(this,null,null,1);
            ArrayList<String> added_ch =new ArrayList<>();
            added_ch = dbHendler.getPackListOnSTB(sn);





            channellist = new ArrayList<>();

           Cursor cursor = dbHendler.getchannelListToAdd();
            if(cursor!=null ){
                cursor.moveToFirst();
                Log.d(TAG, "getchannelListToAdd: "+cursor.getCount());
               Log.d(TAG, "onCreate: "+cursor.getString(cursor.getColumnIndex("packtype")));
                do{
                     channellist.add(new ChannelObject(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("pkgid"))),
                             cursor.getString(cursor.getColumnIndex("pkgname")),
                             cursor.getFloat(cursor.getColumnIndex("pkgprice")),
                             cursor.getString(cursor.getColumnIndex("packtype"))



                             ));
                }
                 while (cursor.moveToNext());

            }


            //       mylist = new ArrayList<>();


            //        Log.d(TAG, "onCreate: "+mylist.get(1));


            recyclerView = (RecyclerView) findViewById(R.id.recyclerChannelList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


              adapter =new AdapterChannelRecyclerView(this,channellist,added_ch);
            recyclerView.setAdapter(adapter);


        }
    }
}