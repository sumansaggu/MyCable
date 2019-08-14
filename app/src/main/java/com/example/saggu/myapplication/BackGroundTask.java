package com.example.saggu.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


public class BackGroundTask extends AsyncTask<String[], Integer, Void> {

    Context context;
    DbHendler dbHendler;
    String TAG = "AsyncTask ";
    // ArrayList<STB> uploadSTB;
    private ProgressDialog progressDialog;
    int rowcount = 0;

    public BackGroundTask(Context context) {
        this.context = context;
        dbHendler = new DbHendler(context, null, null, 1);

        // uploadCust = new ArrayList<>();
        // uploadSTB = new ArrayList<>();

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Wait..");
        //progressDialog.setMessage("Wait...Reading Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String[]... params) {
        String[] rows = params[0]; //get passed array from params
        rowcount = rows.length;

        //get the rows form array
        for (int i = 0; i < rows.length; i++) {
            //split the columns of rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String sn = columns[0];
                String vc_mac = columns[1];
                String status = "ACTIVE";
                String cellInfo = "Rows.." + i + " | " + sn + " | " + vc_mac + " | ";
                Log.d(TAG, "parseStringBuilderCust: data from row: " + cellInfo);

                //    add the data to ArrayList only for log purpose
                //   uploadSTB.add(new STB(sn, vc_mac));
                dbHendler.AddNewStb(new STB(sn, vc_mac, status));

                // Publish the async task progress
                // Added 1, because index start from 0
                publishProgress(i);
                if (i > rowcount - 10) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            } catch (NumberFormatException ex) {
                Log.e(TAG, "parseStringBuilder STB: NUMBERFORMATEXCEPTION " + ex.getMessage());
            } catch (SQLiteConstraintException ex) {
                Log.d(TAG, "doInBackground: " + ex.toString());
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.d(TAG, "onProgressUpdate: called");
        super.onProgressUpdate(progress);
        progressDialog.setMessage("Inserting " + progress[0] + " of " + rowcount);
        //progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        Toast.makeText(context, rowcount + " Records Inserted", Toast.LENGTH_SHORT).show();

    }

}
