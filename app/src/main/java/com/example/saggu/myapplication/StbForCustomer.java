package com.example.saggu.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    Button deleteBtn;


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "on redume called", Toast.LENGTH_SHORT).show();
        prepareListData();
        // get the listview

        registerForContextMenu(expListView);
        // preparing list data

        listAdapter = new expandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stb_for_customer);
        dbHendler = new DbHendler(this, null, null, 1);
        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            custId = bundle.getInt("ID");
        }
        expListView = (ExpandableListView) findViewById(R.id.expandable_list_stb);

        Toolbar toolbar = findViewById(R.id.toolbarchannellist);
        toolbar.setTitle("STBs");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_audiotrack_dark));
        setSupportActionBar(toolbar);



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        menu.setHeaderTitle("Options");
        menu.add("MSO Server");
        menu.add("Add Channel");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        String groupItem = listDataHeader.get(group);
        Log.d(TAG, "onCreateContextMenu: ho ho ho " + groupItem);
        if (item.getTitle().equals("MSO Server")) {
            Intent intent = new Intent(this, MQWebViewActivity.class);
            intent.putExtra("CALLINGACTIVITY", "StbForCustomer");
            intent.putExtra("SN", groupItem);
            intent.putExtra("ID",custId);
            startActivity(intent);
        }
        if(item.getTitle().equals("Add Channel")){

            Intent intent = new Intent(this,Channel_Selection_List.class);
            intent.putExtra("sn",groupItem);

            startActivity(intent);


        }


        return super.onContextItemSelected(item);
    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        // Adding header data
        listDataHeader = new ArrayList<String>();
      //    Log.d(TAG, "customer id " + custId);
        listDataHeader = dbHendler.getSTBsForCustomer(custId);
        listDataChild = new HashMap<String, List<String>>();
   //        Log.d(TAG, "prepareListData: listdataHeader " + listDataHeader);
   //       Log.d(TAG, "prepareListData: length" + listDataHeader.size());
   //        Log.d(TAG, "prepareListData: " + listDataHeader.get(0));
        int i;
        for (i = 0; i < listDataHeader.size(); i++) {
            List<String> pkgChild = new ArrayList<String>();
            pkgChild = dbHendler.getPackListOnSTB(listDataHeader.get(i));
            listDataChild.put(listDataHeader.get(i), pkgChild);
        }

        Log.d(TAG, "prepareListData: " + listDataChild);
    }

    public void refreshList() {

    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ViewAll.class));
        finish();
    }*/


    private class expandListAdapter extends BaseExpandableListAdapter {

        String TAG = "ExpandableSTBList";

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;
        DbHendler dbHendler;
        Button copyFromGroupBtn;

        public expandListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
            this._context = _context;
            this._listDataHeader = _listDataHeader;
            this._listDataChild = _listDataChild;
            dbHendler = new DbHendler(this._context, null, null, 1);

        }

        @Override
        public int getGroupCount() {
            //     Log.d("BAse adaptoer", "getGroupCount: "+this._listDataHeader);
            return this._listDataHeader.size();
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            //   Log.d(TAG, "getGroupView: "+groupPosition);
            final String headerTitle = (String) getGroup(groupPosition);


            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.stb_group_layout, null);

                TextView lblListHeaderSN = (TextView) convertView.findViewById(R.id.stb_group_stb_no);
                lblListHeaderSN.setTypeface(null, Typeface.BOLD);
                lblListHeaderSN.setText(headerTitle);

                copyFromGroupBtn = convertView.findViewById(R.id.copyFromGroupBtn);

                copyFromGroupBtn.setFocusable(false);
                copyFromGroupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Gets a handle to the clipboard service.
                        ClipboardManager clipboard = (ClipboardManager) _context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Creates a new text clip to put on the clipboard
                        ClipData clip = ClipData.newPlainText("simple text", headerTitle);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(_context, "Copied " + headerTitle, Toast.LENGTH_SHORT).show();
                    }
                });

                // we can fill second text view from datadase here but

                String vcMac = dbHendler.getVcMacNoWithSerialNo(headerTitle);

                TextView lblListHeaderVCMac = (TextView) convertView.findViewById(R.id.stb_group_stb_vcMac);
                lblListHeaderVCMac.setTypeface(null, Typeface.BOLD);
                lblListHeaderVCMac.setText(vcMac);
            }
            return convertView;
        }

        @Override
        public Object getGroup(int groupPosition) {
            //   Log.d(TAG, "getGroup: "+groupPosition);
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            //  Log.d(TAG, "getGroupId: ");
            return groupPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String packOnstb = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.stb_pkg_child_layout, null);
            }
            TextView txtListPack = (TextView) convertView.findViewById(R.id.lblListPack);
            txtListPack.setText(packOnstb);
            final TextView txtListPrice = convertView.findViewById(R.id.lblListPrice);
            String price = dbHendler.getPackPrice(packOnstb);
            txtListPrice.setText(price);
            //  Log.d(TAG, "getChildView: header " + _listDataHeader.get(groupPosition));
            String serialno = _listDataHeader.get(groupPosition);
            Log.d(TAG, "getChildView: hash map " + _listDataChild);

         final    float total =0.0f;
            ImageButton deletebtn = convertView.findViewById(R.id.deletePkg);
            //region Button On Click Listener
            deletebtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setMessage("Remove " + packOnstb + " ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHendler.deletePackOnSTB(_listDataHeader.get(groupPosition), packOnstb);// sn of stb and pack name
                            List<String> child = _listDataChild.get(_listDataHeader.get(groupPosition));
                            Log.d(TAG, "onClick: child" + child);
                            child.remove(childPosition);
                            txtListPrice.setText(dbHendler.getPackPrice(packOnstb));
                            List<String> rent = _listDataChild.get(_listDataHeader.get(groupPosition));


                            Log.d(TAG, "onClick: " + copyFromGroupBtn.getText());


                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Toast.makeText(_context, "Delete Clicked on " + _listDataHeader.get(groupPosition) + " " + packOnstb, Toast.LENGTH_SHORT).show();

                }

            });
            //endregion

            if (isLastChild) { // exclude last row from adding x
             //   deletebtn.setVisibility(View.INVISIBLE);
                String fees = dbHendler.getFeesForSTB(_listDataHeader.get(groupPosition));
                TextView textView= convertView.findViewById(R.id.lblListPack);

                textView.setText("Rent: "+fees);
                Log.d(TAG, "getChildView last child position: " + childPosition);

            }

            return convertView;
        }


        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }


        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
