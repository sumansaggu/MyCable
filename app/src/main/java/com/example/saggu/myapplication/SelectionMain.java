package com.example.saggu.myapplication;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

public class SelectionMain extends AppCompatActivity  implements View.OnClickListener {
    CardView cartCatv;
    CardView cardInternet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_main);
        cartCatv =(CardView)findViewById(R.id.cardCatv);
        cardInternet = findViewById(R.id.cardInternet);
        cartCatv.setOnClickListener(this);
        cardInternet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int service = 0;
       if (v.getId()== R.id.cardCatv){
           service =1;
          
       }if(v.getId()==R.id.cardInternet){
         service =2;  
        }
        Intent intent = new Intent(this, ViewAll.class);
        intent.putExtra("selected",service);
        //   intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }
}