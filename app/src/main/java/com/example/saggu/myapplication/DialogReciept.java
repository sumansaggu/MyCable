package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Saggu on 12/16/2016.
 */

public class DialogReciept extends DialogFragment implements View.OnClickListener {
    String TAG = "MyApp_DialogBox";

    DbHendler dbHendler;
    Calendar calendar;
    TextView title_dialog;
    TextView fees_dailog;
    TextView balance_dialog;
    EditText reciept_dialog;
    TextView date;
    EditText remark;
    Button Ok, Cancel;
    CheckBox smsCheckbox, printCheckbox;
    RadioGroup radioGroupRcptDisc;
    RadioButton radioBtnCr, radioBtnDr, radioBtnReciept, radioBtnDiscount;
    String phoneno;
    int id;
    String entryCrOrDr = "credit";
    String entryRcptOrDisc = "reciept";

    // TODO: 14-08-2019 add debit,credit,reciept,discount option
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, null);
        Ok = (Button) view.findViewById(R.id.buttonYes);
        Cancel = (Button) view.findViewById(R.id.buttonNo);
        Ok.setOnClickListener(this);
        Cancel.setOnClickListener(this);


        radioGroupRcptDisc = view.findViewById(R.id.radioGrpRcptDisc);
        radioBtnCr = view.findViewById(R.id.radioBtnCredit);
        radioBtnCr.setOnClickListener(this);
        radioBtnDr = view.findViewById(R.id.radioBtnDebit);
        radioBtnDr.setOnClickListener(this);


        radioBtnReciept = view.findViewById(R.id.radioBtnReciept);
        radioBtnReciept.setOnClickListener(this);
        radioBtnDiscount = view.findViewById(R.id.radioBtnDiscount);
        radioBtnDiscount.setOnClickListener(this);


        fees_dailog = (TextView) view.findViewById(R.id.fees_dialog);
        balance_dialog = (TextView) view.findViewById(R.id.balance_dialog);
        reciept_dialog = (EditText) view.findViewById(R.id.reciept_dialog);


        title_dialog = (TextView) view.findViewById(R.id.title_dialog);
        date = (TextView) view.findViewById(R.id.date);
        date.setOnClickListener(this);

        remark = (EditText) view.findViewById(R.id.remarksEditText);
        smsCheckbox = view.findViewById(R.id.sendSMScheckBox);
        smsCheckbox.setOnClickListener(this);
        printCheckbox = view.findViewById(R.id.printcheckBox);
        printCheckbox.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        id = bundle.getInt("ID");
        // setCancelable(false);
        // preventing from cancel when clicking on background
        dbHendler = new DbHendler(getActivity(), null, null, 1);
        getinformation();
        getDate();
        Log.d(TAG, "onCreateView: " + entryCrOrDr);
        Log.d(TAG, "onCreateView: " + entryRcptOrDisc);
        return view;
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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
        if (v.getId() == R.id.date) {
            hideKeyboard();
            pickDate();
        }
        if (v.getId() == R.id.buttonYes) {
            if (reciept_dialog.getText().toString().equals("")) {
                Toast.makeText(this.getActivity(), "Enter the correct amount", Toast.LENGTH_SHORT).show();
            } else {
                updateReciept();
                getinformation();
                //viewfeestable();

                dismiss();
            }
        }
        if (v.getId() == R.id.printcheckBox) {

            if (printCheckbox.isChecked()) {
                Toast.makeText(this.getActivity(), "Print option is not working yet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), "Print option will be added soon", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.buttonNo) {
            dismiss();
        }
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


    public void updateReciept() {
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        int lbalance = personInfo.get_balance();
        Log.d(TAG, "" + lbalance);

        String Amount = reciept_dialog.getText().toString().trim();
        if (Amount.equals("")) {
            Toast.makeText(this.getActivity(), "Enter the amount", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int id = this.id;
            int amount = Integer.parseInt(Amount);
            String datefromEditText = date.getText().toString().trim();
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
            ViewAll activity = (ViewAll) getActivity();
            activity.refreshListView();
            Toast.makeText(this.getActivity(), "Added Rs. " + amount + " to " + name, Toast.LENGTH_SHORT).show();
            reciept_dialog.setText("");
            Ok.setEnabled(false);
            Cancel.setText("Back");
            getinformation();
        }

    }


    public void viewfeestable() {
        List<Fees> fees = dbHendler.viewFees();
        for (Fees fees1 : fees) {
            String log = "No: " + fees1.getNo() + " Id: " + fees1.getId()
                    + " Fees: " + fees1.getFees() + " Date: " + fees1.getDate();
            Log.d(TAG, log);
        }
    }

    public String getDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        date.setText(formattedDate);
        return formattedDate;

    }

    public void pickDate() {
        Log.d(TAG, "pick date called");
        DialogFragment newFragment = new MyDatePicker();
        newFragment.show(getFragmentManager(), "datepicker");
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        newFragment.setArguments(bundle);


    }

    public void changeText(String data) {
        date.setText(data);
    }


}
