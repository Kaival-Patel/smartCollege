package com.kaival.smartcollege;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.kaival.smartcollege.R.drawable.cardborderselector;

public class Midmarkssetup extends AppCompatActivity {
TextView choose,allfields,college,chooseexamtv,typeofexamtv,divisontv,departmenttv,semestertv,yeartv;
Spinner collegespin;
    static Spinner yearspin;

    static Spinner semesterspin;
    static Spinner divisionspin;
    static Spinner departmentspin;
    boolean subjectlistadded=false;
static String examtype;
String []collegespinval,semesterval,divisionval,departmentval;
    AdView madview;
FrameLayout mid1frame,mid2frame,remframe;
SubmitButton showlist;
ArrayList yearsp =new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midmarkssetup);
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Marks Fillup");

        }
        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        choose=findViewById(R.id.choosetv);
        allfields=findViewById(R.id.allfieldstv);
        collegespin=findViewById(R.id.collegespin);
        semesterspin=findViewById(R.id.semesterspin);
        divisionspin=findViewById(R.id.divisionspin);
        departmentspin=findViewById(R.id.departmentspin);
        yeartv=findViewById(R.id.yeartv);
        yearspin=findViewById(R.id.yearspin);
        college=findViewById(R.id.collegetv);
        showlist=findViewById(R.id.showlist);
        mid1frame=findViewById(R.id.mid1frame);
        mid2frame=findViewById(R.id.mid2frame);
        remframe=findViewById(R.id.remframe);
        chooseexamtv=findViewById(R.id.chooseexamtv);
        semestertv=findViewById(R.id.semestertv);
        departmenttv=findViewById(R.id.depratmenttv);
        divisontv=findViewById(R.id.divisiontv);
        typeofexamtv=findViewById(R.id.typetv);
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int currentyear=calendar.get(Calendar.YEAR);
        System.out.println("CURRENT YEAR:");
        yearsp.add("SELECT");
        for(int i=(currentyear-4);i<=currentyear;i++)
        {
            yearsp.add(""+Integer.toString(i));

        }
        ArrayAdapter yearadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearsp);
        yearspin.setAdapter(yearadapter);
        mid1frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examtype="Mid1";
                chooseexamtv.setVisibility(View.GONE);
               mid1frame.setBackgroundResource(R.drawable.cardborderselector);
                mid2frame.setBackgroundResource(R.drawable.nonecardselected);
                remframe.setBackgroundResource(R.drawable.nonecardselected);
            }
        });
        mid2frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examtype="Mid2";
                chooseexamtv.setVisibility(View.GONE);
                mid1frame.setBackgroundResource(R.drawable.nonecardselected);
                remframe.setBackgroundResource(R.drawable.nonecardselected);
                mid2frame.setBackgroundResource(R.drawable.cardborderselector);
            }
        });
        remframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examtype="Remedial";
                chooseexamtv.setVisibility(View.GONE);
                mid1frame.setBackgroundResource(R.drawable.nonecardselected);
                mid2frame.setBackgroundResource(R.drawable.nonecardselected);
                remframe.setBackgroundResource(R.drawable.cardborderselector);
            }
        });


        choose.setTypeface(productsans);
        allfields.setTypeface(productsans);
        college.setTypeface(oxygen);
        chooseexamtv.setTypeface(oxygen);
        semestertv.setTypeface(oxygen);
        departmenttv.setTypeface(oxygen);
        divisontv.setTypeface(oxygen);;
        typeofexamtv.setTypeface(oxygen);

        collegespinval= new String[]{"SELECT","SOCET","ASOIT"};
        ArrayAdapter collegevals=new ArrayAdapter(this,android.R.layout.simple_spinner_item,collegespinval);
        collegevals.setDropDownViewResource(R.layout.spinnertext);
        collegespin.setAdapter(collegevals);

        semesterval= new String[]{"SELECT","1","2","3","4","5","6","7","8"};
        ArrayAdapter semesteradapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,semesterval);
        semesteradapter.setDropDownViewResource(R.layout.spinnertext);
        semesterspin.setAdapter(semesteradapter);

        divisionval= new String[]{"SELECT","A","B","C","D","E","F"};
        ArrayAdapter divisionadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,divisionval);
        semesteradapter.setDropDownViewResource(R.layout.spinnertext);
        divisionspin.setAdapter(divisionadapter);

        departmentval= new String[]{"SELECT","Computer","IT","Mechanical","Civil","Aeronautical","EC"};
        ArrayAdapter departmentadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,departmentval);
        semesteradapter.setDropDownViewResource(R.layout.spinnertext);
        departmentspin.setAdapter(departmentadapter);

        showlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkallfields();
            }
        });

    }







    private void checkallfields() {
        if(collegespin.getSelectedItem().equals("SELECT"))
        {
            Toast.makeText(this,"Please Select College!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(divisionspin.getSelectedItem().equals("SELECT"))
        {
            Toast.makeText(this,"Please Select Division!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(departmentspin.getSelectedItem().equals("SELECT"))
        {
            Toast.makeText(this,"Please Select Department!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(semesterspin.getSelectedItem().equals("SELECT"))
        {
            Toast.makeText(this,"Please Select Semester!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(yearspin.getSelectedItem().toString().equals("SELECT"))
        {
            Toast.makeText(this,"Please Select Year!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(examtype==null)
        {
            chooseexamtv.setVisibility(View.VISIBLE);
            return;
        }

        Toast.makeText(this,"Loading Students!",Toast.LENGTH_SHORT).show();
        Intent i=new Intent(Midmarkssetup.this,SubjectSelector.class);
        startActivity(i);
        System.out.println("College:"+collegespin.getSelectedItem());
        System.out.println("Division:"+divisionspin.getSelectedItem());
        System.out.println("Department:"+departmentspin.getSelectedItem());
        System.out.println("semester:"+semesterspin.getSelectedItem());
        System.out.println("examtype:"+examtype);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
