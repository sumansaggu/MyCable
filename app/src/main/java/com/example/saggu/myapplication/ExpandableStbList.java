package com.example.saggu.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class ExpandableStbList extends BaseExpandableListAdapter {
    String TAG = "ExpandableSTBList";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    DbHendler dbHendler;
    Button copyFromGroupBtn;

    public ExpandableStbList(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
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

        TextView txtListPrice = convertView.findViewById(R.id.lblListPrice);
        txtListPrice.setText(dbHendler.getPackPrice(packOnstb));

      //  Log.d(TAG, "getChildView: header " + _listDataHeader.get(groupPosition));
        String serialno = _listDataHeader.get(groupPosition);
        Log.d(TAG, "getChildView: hash map "+ _listDataChild);
        ImageButton deletebtn = convertView.findViewById(R.id.deletePkg);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(_context, "Delete Clicked on " + _listDataHeader.get(groupPosition) + " " + packOnstb, Toast.LENGTH_SHORT).show();

                dbHendler.deletePackOnSTB(_listDataHeader.get(groupPosition), packOnstb);// sn of stb and pack name
            //    _listDataChild.remove(_listDataHeader.get(groupPosition),_listDataChild[childPosition]);
                Log.d(TAG, "onClick: _list data chile"+ _listDataChild  );


                notifyDataSetChanged();//not working

            }
        });
        if (isLastChild) { // exclude last row from adding x
            deletebtn.setVisibility(View.INVISIBLE);
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





