package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Saggu on 9/1/2017.
 */

public class IDPWFragment extends DialogFragment {

    String code,id, pw;
    EditText codetxt,idtxt, pwtxt;
    Button okbtn;
    DbHendler dbHendler;
    int extra_id = 0;
    String TAG ="IDPW DIALOG FRAG";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.id_pw_add_edit_fragment, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        codetxt = view.findViewById(R.id.code);
        idtxt = (EditText) view.findViewById(R.id.etxtID);
        pwtxt = (EditText) view.findViewById(R.id.etxtPW);
        okbtn = (Button) view.findViewById(R.id.btnIDPWFrag);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okbtn.getText().equals("Change")) {
                    updateIDPW();
                } else
                    setIDPW();
            }
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            extra_id = bundle.getInt("ID");
            if (extra_id > 0) {
                Toast.makeText(getActivity(), " " + extra_id, Toast.LENGTH_SHORT).show();
                Cursor cursor = dbHendler.getIDPWforEdit(extra_id);
                if (cursor.moveToFirst()) {
                    do {
                        codetxt.setText(cursor.getString(cursor.getColumnIndex(dbHendler.KEY_SERVERID)));
                        idtxt.setText(cursor.getString(cursor.getColumnIndex(dbHendler.KEY_USERID)));
                        pwtxt.setText(cursor.getString(cursor.getColumnIndex(dbHendler.KEY_USERPASSWORD)));
                        // Log.d(TAG, "getIDPWforEdit: "+list);

                    } while (cursor.moveToNext());
                    okbtn.setText("Change");

                }
            }

        }
        return view;
    }

    private void updateIDPW() {
        id = idtxt.getText().toString();
        pw = pwtxt.getText().toString();
        if (id.equals("") || pw.equals("")) {
            Toast.makeText(getActivity(), "Enter proper ID and Password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            dbHendler.updateIDPW(extra_id, id, pw);
            IDPWActivity activity = (IDPWActivity) getActivity();
            activity.cursorswap();
            dismiss();
        }
    }



    private void setIDPW() {
        code = codetxt.getText().toString();
        id = idtxt.getText().toString();
        pw = pwtxt.getText().toString();
        if (code.equals("")|| id.equals("") || pw.equals("")) {
            Toast.makeText(getActivity(), "Enter proper ID and Password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            dbHendler.setIDPW(code,id, pw);
            Toast.makeText(getActivity(), "button clicked" + id + " " + pw, Toast.LENGTH_SHORT).show();
            IDPWActivity activity = (IDPWActivity) getActivity();
            activity.createList();
            dismiss();
        }

    }


}
