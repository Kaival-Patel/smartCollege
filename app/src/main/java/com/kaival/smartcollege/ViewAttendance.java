package com.kaival.smartcollege;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewAttendance extends AppCompatActivity {
ScrollView scrollView;
LinearLayout upperLinearLayout,mainlinearlayout;
TextView enrollmenttxt,nametxt,nodatatxt, dateView,lectureView;
String fbatch,fdept,fdiv,fyear;
ProgressBar proBar;
int count=0;

FirebaseDatabase db=FirebaseDatabase.getInstance();
FirebaseAuth auth=FirebaseAuth.getInstance();
FirebaseUser user=auth.getCurrentUser();
int totaldays=0,totallecs=0,totallecsattended=0;
String enroll="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Attendance Details");
        }
        scrollView=findViewById(R.id.scrollview);
        upperLinearLayout=findViewById(R.id.upperlinearlayout);
        enrollmenttxt=findViewById(R.id.enrollmenttext);
        mainlinearlayout=findViewById(R.id.mainlinearlayout);
        nodatatxt=findViewById(R.id.nodatatxt);
        nametxt=findViewById(R.id.nametext);
        proBar=findViewById(R.id.probar);
        proBar.setVisibility(View.VISIBLE);
        if(auth.getCurrentUser()!=null)
        {
            //show all those details of attendance
            nametxt.setText(""+user.getDisplayName().toString());
            setEnrollment();
            fetchvariables();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddViews();
                }
            },2000);





        }
        else
        {
            //error that user is not valid
            Toast.makeText(this,"User Not Valid!!!!SignIn Again!!!",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
       {
           case android.R.id.home:
               onBackPressed();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void AddViews() {
        nodatatxt.setVisibility(View.GONE);
        proBar.setVisibility(View.GONE);
        db.getReference().child("Students Data").child("" + fdept + "" + fyear).child("" + fdept + "" + fdiv).child("" + fbatch).child("Attendance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("CHILDREN COUNT:"+ dataSnapshot.getChildrenCount());
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            dateView =new TextView(ViewAttendance.this);
                            mainlinearlayout.addView(dateView);
                            System.out.println("CHILDREN:"+ds.getKey());
                            dateView.setText(""+ds.getKey());
                            dateView.setTextColor(Color.parseColor("#c90808"));
                            dateView.setTextSize(20);
                            dateView.setGravity(Gravity.CENTER);
                            if(ds.getValue().toString().contains(enroll))
                            {
                                dateView.setTextColor(Color.parseColor("#046314"));
                                lectureView=new TextView(ViewAttendance.this);
                                mainlinearlayout.addView(lectureView);
                                lectureView.setTextSize(15);
                                lectureView.setText("Present on:"+ds.getKey());
                                lectureView.setGravity(Gravity.CENTER);
                            }
                            else
                            {   lectureView=new TextView(ViewAttendance.this);
                                mainlinearlayout.addView(lectureView);
                                lectureView.setText("Full Absent on:"+ds.getKey());
                                lectureView.setGravity(Gravity.CENTER);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void fetchvariables() {
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

    public void setEnrollment()
    {
        db.getReference().child("Students").child(""+user.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("senroll"))
                    {
                        enroll=ds.getValue().toString();
                        enrollmenttxt.setText(""+enroll);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ViewAttendance.this,StudentLoggedin.class);
        startActivity(i);
        finish();
    }
}
