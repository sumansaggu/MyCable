package com.example.saggu.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Saggu on 2/25/2017.
 */

public class CursorAdaptor extends SimpleCursorAdapter {
    public CursorAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
