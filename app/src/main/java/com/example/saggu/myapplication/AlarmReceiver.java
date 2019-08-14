package com.example.saggu.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Saggu on 3/5/2017.
 */


public  class AlarmReceiver extends BroadcastReceiver {

    DbHendler dbHendler;
    String TAG = "Alarm Reciever";
    String done = "DONE";
    String notDone = "NOTDONE";
    Context context;




    @Override
    public void onReceive(Context ctx, Intent intent) {
        this.context = ctx;


        Log.d(TAG, "onRecieve called ");

        String day = intent.getStringExtra("id");
        int date = Integer.parseInt(day);


        dbHendler = new DbHendler(context, null, null, 1);
        String flag = dbHendler.monthFlag();
        Log.d(TAG, flag);


        //date == 1 means it will update the balance on 1st of every month
        if          (date == 9 )//               (date == 9 && flag.equals(notDone))
        {
            dbHendler.copyDbToExternalStorage(context);
            dbHendler.monthFlagChange(done);
            String fl = dbHendler.monthFlag();
            Log.d(TAG, "flag changed to " + fl);
            Log.d(TAG, "balance will be updated");



          //  dbHendler.endOfMonth(context);
       }

        if (date > 1 && flag.equals(done)) {
            dbHendler.monthFlagChange(notDone);
            String fl = dbHendler.monthFlag();
            Log.d(TAG, "flag changed to " + fl);

        }

 /*//Canceling the repeating alarm
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);*/


    }




}
