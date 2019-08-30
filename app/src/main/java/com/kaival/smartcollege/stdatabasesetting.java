package com.kaival.smartcollege;

import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class stdatabasesetting extends AppCompatActivity {
Spinner servicespin,batchyearspin,departmentsp,divisionspin,noofbatchesspin;
TextView servicetv,batchyeartv,departmenttv,divisiontv,noofbatchestv,passwordtv;
String servicevals[],departmentvals[],divisionvals[],noofbatchesvals[];
ArrayList yearlist=new ArrayList();
EditText passwordet;
AdView madview;
TextInputLayout textInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdatabasesetting);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Create/Destroy the Database");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        servicetv=findViewById(R.id.servicetv);
        servicespin=findViewById(R.id.servicespin);
        batchyeartv=findViewById(R.id.yeartv);
        batchyearspin=findViewById(R.id.yearspin);
        departmenttv=findViewById(R.id.departmenttv);
        divisiontv=findViewById(R.id.divisiontv);
        divisionspin=findViewById(R.id.divisionspin);
        departmentsp=findViewById(R.id.departmentsp);
        passwordtv=findViewById(R.id.passwordtv);
        passwordet=findViewById(R.id.passwordet);
        textInputLayout=findViewById(R.id.textlayoutpassword);
        noofbatchestv=findViewById(R.id.noofbatchestv);
        noofbatchesspin=findViewById(R.id.noofbatchesspin);
        passwordtv.setTypeface(productsans);
        passwordet.setTypeface(productsans);
        batchyeartv.setTypeface(productsans);
        departmenttv.setTypeface(productsans);
        noofbatchestv.setTypeface(productsans);
        servicetv.setTypeface(productsans);
        servicevals=new String[]{"SELECT","Create Database","Delete Database"};
        ArrayAdapter serviceadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,servicevals);
        servicespin.setAdapter(serviceadapter);
        departmentvals=new String[]{"SELECT","COMPUTER","CIVIL","IT","MECHANICAL","EC"};
        ArrayAdapter departmentadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,departmentvals);
        departmentsp.setAdapter(departmentadapter);
        divisionvals=new String[]{"SELECT","A","B","C","D","E","F"};
        ArrayAdapter divisionadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,divisionvals);
        divisionspin.setAdapter(divisionadapter);
        noofbatchesvals=new String[]{"SELECT","1","2","3","4","5","6"};
        ArrayAdapter noofbatchadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,noofbatchesvals);
        noofbatchesspin.setAdapter(noofbatchadapter);
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int currentyear=calendar.get(Calendar.YEAR);
        System.out.println("CURRENT YEAR:");
        yearlist.add("SELECT");
        for(int i=(currentyear-4);i<=currentyear;i++)
        {
            yearlist.add(""+Integer.toString(i));

        }
        ArrayAdapter yearadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearlist);
        batchyearspin.setAdapter(yearadapter);
        servicespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(servicevals[position].equals("Create Database"))
                {
                    batchyeartv.setVisibility(View.VISIBLE);
                    departmenttv.setVisibility(View.VISIBLE);
                    divisiontv.setVisibility(View.VISIBLE);
                    noofbatchestv.setVisibility(View.VISIBLE);
                    batchyearspin.setVisibility(View.VISIBLE);
                    departmentsp.setVisibility(View.VISIBLE);
                    divisionspin.setVisibility(View.VISIBLE);
                    noofbatchesspin.setVisibility(View.VISIBLE);
                    passwordtv.setVisibility(View.VISIBLE);
                    passwordet.setVisibility(View.VISIBLE);
                    textInputLayout.setVisibility(View.VISIBLE);

                }
                else if(servicevals[position].equals("SELECT"))
                {
                    batchyeartv.setVisibility(View.GONE);
                    departmenttv.setVisibility(View.GONE);
                    divisiontv.setVisibility(View.GONE);
                    noofbatchestv.setVisibility(View.GONE);
                    batchyearspin.setVisibility(View.GONE);
                    departmentsp.setVisibility(View.GONE);
                    divisionspin.setVisibility(View.GONE);
                    noofbatchesspin.setVisibility(View.GONE);
                    passwordtv.setVisibility(View.GONE);
                    passwordet.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
