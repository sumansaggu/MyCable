package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecieptActivity extends AppCompatActivity implements View.OnClickListener, Communicator {

    String TAG = "MyApp_DialogBox";
    DatePicker datePicker;
    DbHendler dbHendler;
    Calendar calendar;
    TextView title_dialog;
    TextView fees_dailog;
    TextView balance_dialog;
    EditText reciept_dialog;
    TextView dateTxtView;
    EditText remark;
    Button Ok, Cancel;
    CheckBox smsCheckbox, printCheckbox;
    RadioGroup radioGroupRcptDisc;
    RadioButton radioBtnCr, radioBtnDr, radioBtnReciept, radioBtnDiscount;
    String phoneno;
    int id;
    String entryCrOrDr = "credit";
    String entryRcptOrDisc = "reciept";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);
        Ok = (Button) findViewById(R.id.buttonYes);
        Cancel = (Button) findViewById(R.id.buttonNo);
        Ok.setOnClickListener(this);
        Cancel.setOnClickListener(this);


        radioGroupRcptDisc = findViewById(R.id.radioGrpRcptDisc);
        radioBtnCr = findViewById(R.id.radioBtnCredit);
        radioBtnCr.setOnClickListener(this);
        radioBtnDr = findViewById(R.id.radioBtnDebit);
        radioBtnDr.setOnClickListener(this);


        radioBtnReciept = findViewById(R.id.radioBtnReciept);
        radioBtnReciept.setOnClickListener(this);
        radioBtnDiscount = findViewById(R.id.radioBtnDiscount);
        radioBtnDiscount.setOnClickListener(this);


        fees_dailog = (TextView) findViewById(R.id.fees_dialog);
        balance_dialog = (TextView) findViewById(R.id.balance_dialog);
        reciept_dialog = (EditText) findViewById(R.id.reciept_dialog);


        title_dialog = (TextView) findViewById(R.id.title_dialog);
        dateTxtView = (TextView) findViewById(R.id.date_receipt);
        dateTxtView.setOnClickListener(this);

        remark = (EditText) findViewById(R.id.remarksEditText);
        smsCheckbox = findViewById(R.id.sendSMScheckBox);
        smsCheckbox.setOnClickListener(this);
        printCheckbox = findViewById(R.id.printcheckBox);
        printCheckbox.setOnClickListener(this);

     //   Bundle bundle = getArguments();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");
        // setCancelable(false);
        // preventing from cancel when clicking on background
        dbHendler = new DbHendler(this, null, null, 1);

        getinformation();
        getDateTxtView();
        Log.d(TAG, "onCreateView: " + entryCrOrDr);
        Log.d(TAG, "onCreateView: " + entryRcptOrDisc);
    }
    public String getDateTxtView() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        dateTxtView.setText(formattedDate);
        return formattedDate;

    }
    public void getinformation() {
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        String name = personInfo.getName().toString().trim();
        title_dialog.setText(name);
        phoneno = personInfo.getPhoneNumber().toString().trim();

        int fees = personInfo.get_fees();

        int balance = personInfo.get_balance();

        fees_dailog.setText(Integer.toString(fees));
        balance_dialog.setText(Integer.toString(balance));
        //getDialog().setTitle(name);
    }
    public void pickDate() {
        Log.d(TAG, "pick date called");
        DialogFragment datePicker = new MyDatePicker();
        datePicker.show(getFragmentManager(), "datepicker");
        Bundle bundle = new Bundle();
        datePicker.setArguments(bundle);
        bundle.putInt("id", 1);


    }

    public void changeText(String data) {
        dateTxtView.setText(data);
    }

    public void hideKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {
        //  Log.d(TAG, "onClick: "+ entryCrOrDr +"---"+entryRcptOrDisc);
        if (v.getId() == R.id.radioBtnCredit) {
            radioGroupRcptDisc.setVisibility(View.VISIBLE);
            entryCrOrDr = "credit";
            Log.d(TAG, "onClick: " + entryCrOrDr);
        }
        if (v.getId() == R.id.radioBtnDebit) {
            radioGroupRcptDisc.setVisibility(View.INVISIBLE);
            entryCrOrDr = "debit";
            Log.d(TAG, "onClick: " + entryCrOrDr);
        }
        if (v.getId() == R.id.radioBtnReciept) {
            entryRcptOrDisc = "reciept";
            Log.d(TAG, "onClick: " + entryRcptOrDisc);
        }
        if (v.getId() == R.id.radioBtnDiscount) {
            entryRcptOrDisc = "discount";
            Log.d(TAG, "onClick: " + entryRcptOrDisc);
        }
        if (v.getId() == R.id.date_receipt) {
           hideKeyboard( v );
            pickDate();


        }
        if (v.getId() == R.id.buttonYes) {
            if (reciept_dialog.getText().toString().equals("")) {
                Toast.makeText(this, "Enter the correct amount", Toast.LENGTH_SHORT).show();
            } else {
                updateReciept();
                getinformation();
                //viewfeestable();

            //    dismiss();
            }
        }
        if (v.getId() == R.id.printcheckBox) {

            if (printCheckbox.isChecked()) {
                Toast.makeText(this, "Print option is not working yet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Print option will be added soon", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.buttonNo) {
          //  dismiss();
        }
    }
    public void updateReciept() {
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        int lbalance = personInfo.get_balance();
        Log.d(TAG, "" + lbalance);

        String Amount = reciept_dialog.getText().toString().trim();
        if (Amount.equals("")) {
            Toast.makeText(this, "Enter the amount", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int id = this.id;
            int amount = Integer.parseInt(Amount);
            String datefromEditText = dateTxtView.getText().toString().trim();
            Log.d(TAG, "" + (lbalance - amount));
            int curBalance=0;
            if(entryCrOrDr.equals("debit")){
                curBalance = lbalance + amount;
            }
            if(entryCrOrDr.equals("credit")){
                curBalance = lbalance - amount;
            }

            String name = personInfo.getName();
            String remark = this.remark.getText().toString();
            dbHendler.updateBalance(new PersonInfo(id, curBalance));  //new balance to customer table
            dbHendler.addFees(new Fees(id, datefromEditText, amount, lbalance, curBalance, remark), entryCrOrDr, entryRcptOrDisc);//fees recieved and date to fees table

            if (smsCheckbox.isChecked()) {
                Log.d(TAG, "checkbox checked");
                String smsBody = "Dear Customer.\nYour Last CableTV Balance was " + lbalance + ".\nRecived: " + amount + ". Current Balance is " + curBalance + "\nThank you";
                SmsManager.getDefault().sendTextMessage(phoneno, null, smsBody, null, null);
            }
    //        ViewAll activity = (ViewAll) getActivity();
    //        activity.refreshListView();
            Toast.makeText(this, "Added Rs. " + amount + " to " + name, Toast.LENGTH_SHORT).show();
            reciept_dialog.setText("");
            Ok.setEnabled(false);
            Cancel.setText("Back");
            getinformation();
        }

    }

    @Override
    public void respond(String date) {
        dateTxtView.setText(date);
    }

    @Override
    public void respond2(String date2) {

    }
}
