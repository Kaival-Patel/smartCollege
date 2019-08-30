package com.kaival.smartcollege;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewMidMarks extends AppCompatActivity {
    TextView enrolltxt,name,nodatatxt,outoftv;
    ProgressBar progressBar;
    Spinner spinner;
    String enroll="";
    TableLayout tableLayout;
    TableRow tableRow;
    TextView textView;
    LinearLayout linearLayout;
    String fbatch,fdept,fdiv,fyear;
    int outofmarks=0;
    String currentselectedexam="Mid1";
    int totalmarks=0;
    Typeface productsans;
    Typeface oxygen;
    ArrayList<String> marks=new ArrayList<>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mid_marks);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("View Marks");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        enrolltxt=findViewById(R.id.enrolltext);
        name=findViewById(R.id.nametv);
        nodatatxt=findViewById(R.id.nodatatxt);
        progressBar=findViewById(R.id.probar);
        outoftv=findViewById(R.id.outoftv);
        spinner=findViewById(R.id.midspinner);
        linearLayout=findViewById(R.id.innerlayout);
        tableLayout=findViewById(R.id.table);
        String arr[]={"Mid1","Mid2","Rem"};
        ArrayList<String> examlist=new ArrayList<>();
        examlist.add("Mid1");
        examlist.add("Mid2");
        examlist.add("Rem");
        progressBar.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,examlist);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentselectedexam=parent.getItemAtPosition(position).toString();
                nodatatxt.setVisibility(View.VISIBLE);
                outoftv.setText("Total Marks://");
                linearLayout.removeView(tableLayout);
                totalmarks=0;
                outofmarks=0;
                marks.clear();
                fetchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(user!=null)
        {
            setEnrollAndName();
            fetchUserInfo();
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchResult();
                }
            },1000);
        }
        else
        {
            //display Error
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void fetchUserInfo() {
        db.getReference().child("Students").child(""+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("Batch"))
                    {
                        fbatch=ds.getValue().toString();
                        System.out.println("FBATCH:"+fbatch);
                    }
                    else if(ds.getKey().equals("Department"))
                    {
                        fdept=ds.getValue().toString();
                        System.out.println("FDEPT"+fdept);
                    }
                    else if(ds.getKey().equals("Division"))
                    {
                        fdiv=ds.getValue().toString();
                        System.out.println("FDIV:"+fdiv);
                    }
                    else if(ds.getKey().equals("Year"))
                    {
                        fyear=ds.getValue().toString();
                        System.out.println("FYEAR:"+fyear);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchResult() {
            //fetch
            db.getReference().child("Students Data").child("" + fdept + "" + fyear).child("" + fdept + "" + fdiv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        if(ds.getKey().toString().equals(""+currentselectedexam))
                        {

                            linearLayout.removeView(tableLayout);
                            linearLayout.addView(tableLayout);
                            tableLayout.removeAllViews();
                        nodatatxt.setVisibility(View.GONE);
                        System.out.println("DS CHILD:"+ds.getValue());
                            textView=new TextView(getApplicationContext());
                            textView.setText("SUBJECT:CODE");
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow=new TableRow(getApplicationContext());
                            tableRow.addView(textView);
                            tableLayout.addView(tableRow);
                            textView=new TextView(getApplicationContext());
                            textView.setText("MARKS");
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow.addView(textView);
                            for(DataSnapshot ds1:ds.getChildren())
                            {
                                System.out.println("Ds1 key:"+ds1.getKey());
                                if(ds1.getKey().equals("outof"))
                                {
                                    outoftv.setText("Total Marks:"+ds1.getValue());
                                    outofmarks=Integer.parseInt(ds1.getValue().toString());
                                }
                                if(ds1.getKey().equals(""+enroll))
                                {
                                    for(DataSnapshot ds2:ds1.getChildren()) {
                                        System.out.println("Ds2KEY::"+ds2.getKey());
                                        System.out.println("Ds2CHILD::"+ds2.getValue());
                                        textView=new TextView(getApplicationContext());
                                        textView.setText(""+ds2.getKey());
                                        textView.setTextColor(Color.parseColor("#000000"));
                                        textView.setTextSize(20);
                                        textView.setPadding(10,10,10,10);
                                        textView.setBackgroundResource(R.drawable.cell_borders);
                                        tableRow=new TableRow(getApplicationContext());
                                        tableRow.addView(textView);
                                        tableLayout.addView(tableRow);
                                        textView=new TextView(getApplicationContext());
                                        textView.setText(""+ds2.getValue());
                                        marks.add(""+ds2.getValue());
                                        textView.setTextColor(Color.parseColor("#000000"));
                                        textView.setTextSize(20);
                                        textView.setPadding(10,10,10,10);
                                        textView.setBackgroundResource(R.drawable.cell_borders);
                                        tableRow.addView(textView);

                                    }
                              /*  Pattern p=Pattern.compile("\\{([^}]*)\\}");
                                Matcher m=p.matcher(str);
                                while(m.find())
                                {
                                    System.out.println("MATCHER::::"+m.group());
                                    String subject=m.group(1);
                                    Pattern pattern=Pattern.compile("-?\\\\d+");
                                    Matcher matcher=pattern.matcher(subject);
                                    while (matcher.find())
                                    {
                                        System.out.println("2nd Matcher:"+matcher.group(1));
                                    }
                                }*/
                                }
                            }
                            totalmarks=0;
                            for(int i=0;i<marks.size();i++)
                            {
                                totalmarks+=Integer.parseInt(marks.get(i));
                            }
                            textView=new TextView(getApplicationContext());
                            textView.setText("TOTAL");
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow=new TableRow(getApplicationContext());
                            tableRow.addView(textView);
                            tableLayout.addView(tableRow);
                            textView=new TextView(getApplicationContext());
                            textView.setText(""+totalmarks+"/"+""+marks.size()*outofmarks);
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow.addView(textView);
                            textView=new TextView(getApplicationContext());
                            textView.setText("PERCENTAGE");
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow=new TableRow(getApplicationContext());
                            tableRow.addView(textView);
                            tableLayout.addView(tableRow);
                            textView=new TextView(getApplicationContext());
                            float noofmarks=marks.size()*outofmarks;
                            textView.setText(""+(int)(totalmarks*100/noofmarks)+"%");
                            textView.setTypeface(oxygen);
                            textView.setBackgroundResource(R.drawable.cell_borders);
                            textView.setTextColor(Color.parseColor("#263c60"));
                            textView.setTextSize(20);
                            textView.setPadding(10,10,10,10);
                            tableRow.addView(textView);
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            progressBar.setVisibility(View.GONE);
        }


    public void setEnrollAndName()
    {
        name.setText(""+user.getDisplayName().toString());
        db.getReference().child("Students").child(""+user.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                    if(ds.getKey().toString().equals("senroll"))
                    {
                        enroll=ds.getValue().toString();
                        enrolltxt.setText(""+enroll);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
