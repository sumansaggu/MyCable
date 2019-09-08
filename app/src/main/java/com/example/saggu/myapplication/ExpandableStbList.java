package com.example.saggu.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableStbList extends BaseExpandableListAdapter {
    String TAG="ExpandableSTBList";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    DbHendler dbHendler;

    public ExpandableStbList(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        dbHendler = new DbHendler(this._context,null,null,1);
    }

    @Override
    public int getGroupCount() {
   //     Log.d("BAse adaptoer", "getGroupCount: "+this._listDataHeader);
        return this._listDataHeader.size();
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
     //   Log.d(TAG, "getGroupView: "+groupPosition);
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stb_group_layout, null);

               TextView lblListHeaderSN = (TextView) convertView.findViewById(R.id.stb_group_stb_no);
               lblListHeaderSN.setTypeface(null, Typeface.BOLD);
               lblListHeaderSN.setText(headerTitle);

             // we can fill second text view from datadase here but

           String vcMac= dbHendler.getVcMacNoWithSerialNo(headerTitle);

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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childListPack = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stb_pkg_child_layout, null);
        }
        TextView txtListPack = (TextView) convertView.findViewById(R.id.lblListPack);
        txtListPack.setText(childListPack);


        TextView txtListPrice = convertView.findViewById(R.id.lblListPrice);
        txtListPrice.setText(dbHendler.getPackPrice(childListPack));

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
