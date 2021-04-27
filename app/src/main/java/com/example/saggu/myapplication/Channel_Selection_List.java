package com.example.saggu.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Channel_Selection_List extends AppCompatActivity implements MyInterface {
    String sn;
    int stbId;
    RecyclerView recyclerView;
    AdapterChannelRecyclerView adapter;
    ArrayList<ChannelObject> mylist = new ArrayList<>();
    String TAG = "CHANNEL SELECTION LIST";
    DbHendler dbHendler;
    private List<ChannelObject> channellistAll;
    private ArrayList<Integer> selected_ch_id;
    TextView selected_ch, rent, txtdiffirence, total;
    Button ok;
    ArrayList<String> added_ch = new ArrayList<>();
    String oldrent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel__selection__list);
        ok = findViewById(R.id.btn_add_selected_ch);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHendler.setPkgToStb(stbId,selected_ch_id);
                ok.setEnabled(false);


            }
        });


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            sn = bundle.getString("sn");
            Toast.makeText(this, "" + sn, Toast.LENGTH_SHORT).show();
            dbHendler = new DbHendler(this, null, null, 1);

            added_ch = dbHendler.getPackListOnSTB(sn);
            stbId = dbHendler.getSTBIdWithSn(sn);
            Toast.makeText(this, ""+ stbId, Toast.LENGTH_SHORT).show();


            channellistAll = new ArrayList<>();

            Cursor cursor = dbHendler.getchannelListToAdd();
            if (cursor != null) {
                cursor.moveToFirst();
                Log.d(TAG, "getchannelListToAdd: " + cursor.getCount());
                Log.d(TAG, "onCreate: " + cursor.getString(cursor.getColumnIndex("packtype")));
                do {

                    channellistAll.add(new ChannelObject(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("pkgid"))),
                            cursor.getString(cursor.getColumnIndex("pkgname")),
                            cursor.getFloat(cursor.getColumnIndex("pkgprice")),
                            cursor.getString(cursor.getColumnIndex("packtype"))


                    ));
                }
                while (cursor.moveToNext());

            }


            recyclerView = (RecyclerView) findViewById(R.id.recyclerChannelList);
            selected_ch = (TextView) findViewById(R.id.selected_ch);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AdapterChannelRecyclerView(this, channellistAll, added_ch, this);
            recyclerView.setAdapter(adapter);

            rent = findViewById(R.id.ch_selecton_rent);

            oldrent = added_ch.get(added_ch.size() - 1); // get last element of arrya list this is rent

            rent.setText(oldrent);

            txtdiffirence = findViewById(R.id.ch_selecton_rent_diff);
            total = findViewById(R.id.ch_selecton_rentTotal);
            try {
                total.setText(String.valueOf(Float.parseFloat(oldrent)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void respond(ArrayList<Integer> selected_ch_id, ArrayList<String> ch_name, float difference) {
        this.selected_ch_id=selected_ch_id;

        String str = TextUtils.join(" || ", ch_name); // make string from array
        selected_ch.setText(str);
        txtdiffirence.setText(String.valueOf(difference));

        try {
            total.setText(String.valueOf(difference + Float.parseFloat(oldrent)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}