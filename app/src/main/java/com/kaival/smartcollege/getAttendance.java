package com.kaival.smartcollege;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class getAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String[] semsp;
    String[] deptsp;
    String[] clgsp;
    String[] slotsp;
    String[] divisionsp;
    String[] batchsp;
    ArrayList yearsp=new ArrayList();
    Button lecturernamebtn;
    AdView madview;
    int day,month,year;
    int dayfinal;
    String getDay,lecturername;
    static int monthfinal;
    static int yearfinal;
    String selectedsem, selecteddept;
    Spinner college;
    static Spinner styear;
    static Spinner division;
    static Spinner sem;
    static Spinner slot;
    Spinner subject;
    static Spinner dept;
    static Spinner batch;
    TextView name,datetv,depttv,semtv,yeartv,slottv,batchtv,subtv,fetchlecttv;
    static TextView dateandday;
    TextView lecturernametxtview;
    static Button submit;
    Button refreshsubs;
    Button editname;
    Button fetchlecturer;
    Button dateselectorbtn;
    static ProgressBar pb;
    boolean ttfound = false,lecturerfound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);
        this.getSupportActionBar().setTitle("Mark Attendance");
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        college = findViewById(R.id.clgchoosett);
        datetv=findViewById(R.id.datetv);
        depttv=findViewById(R.id.depttv);
        batchtv=findViewById(R.id.batchtv);
        fetchlecttv=findViewById(R.id.fetchlecttv);
        subtv=findViewById(R.id.subjecttv);
        semtv=findViewById(R.id.semtv);
        slottv=findViewById(R.id.slottv);
        yeartv=findViewById(R.id.yeartv);
        sem = findViewById(R.id.semchoosett);
        division = findViewById(R.id.divchoosett);
        slot = findViewById(R.id.slotchoosett);
        subject = findViewById(R.id.subjectchoosett);
        submit = findViewById(R.id.submitbtntt);
        dept = findViewById(R.id.deptchoosett);
        batch = findViewById(R.id.batchchoosett);
        pb = findViewById(R.id.pb);
        styear=findViewById(R.id.yearchoosett);
        name = findViewById(R.id.facultynametxtview);
        lecturernametxtview=findViewById(R.id.lecturernameedittext);
        fetchlecturer = findViewById(R.id.getlecturer);
        deptsp = new String[]{"SELECT", "MECHANICAL", "CIVIL", "COMPUTER", "IT", "CHEMICAL", "AERONAUTICAL", "EC"};
        semsp = new String[]{"SELECT", "1", "2", "3", "4", "5", "6", "7", "8"};
        clgsp = new String[]{"SELECT", "SOCET", "ASOIT"};
        slotsp = new String[]{"SELECT", "LEC 1", "LAB 1", "LEC 2", "LAB 2", "LEC 3", "LAB 3", "LEC 4", "LEC 5", "LEC 6"};
        divisionsp = new String[]{"A", "B", "C", "D", "E", "F"};




        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        name.setTypeface(oxygen);
        datetv.setTypeface(productsans);
        depttv.setTypeface(productsans);
        semtv.setTypeface(productsans);
        yeartv.setTypeface(productsans);
        slottv.setTypeface(productsans);
        batchtv.setTypeface(productsans);
        subtv.setTypeface(productsans);
        fetchlecttv.setTypeface(productsans);
        lecturernametxtview.setTypeface(oxygen);

        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int currentyear=calendar.get(Calendar.YEAR);
        System.out.println("CURRENT YEAR:");
       yearsp.add("SELECT");
        for(int i=(currentyear-4);i<=currentyear;i++)
        {
            yearsp.add(""+Integer.toString(i));

        }

        refreshsubs = findViewById(R.id.refreshsubs);
        editname = findViewById(R.id.editnamebtn);
        dateandday=findViewById(R.id.txtdate);
        dateselectorbtn=findViewById(R.id.dateselector);
        ArrayAdapter deptadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deptsp);
        ArrayAdapter semadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, semsp);
        ArrayAdapter clgadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, clgsp);
        ArrayAdapter slotadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, slotsp);
        ArrayAdapter yearadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, yearsp);
        ArrayAdapter divisionadapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, divisionsp);
        division.setAdapter(divisionadapter);
        styear.setAdapter(yearadapter);
        college.setAdapter(clgadapter);
        dept.setAdapter(deptadapter);
        slot.setAdapter(slotadapter);
        sem.setAdapter(semadapter);
        dateselectorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaddate();
            }
        });
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadprofile();
            }
        });
        loadprofilename();
        loaddeptandsem();
        loadbatch();
        refreshsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadsubject();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchtimetable();
            }
        });
        fetchlecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                lecturernametxtview.setText("");
                getLecturer();
            }
        });


    }

    public void loadprofilename() {
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        if (user.getDisplayName() != null) {
            name.setText("Hello " + user.getDisplayName());
        } else {
            name.setText("Hello Your Name");
        }
    }
    public void onBackPressed() {
        Intent i=new Intent(getAttendance.this,Facultyloggedin.class);
        finish();
        startActivity(i);
    }

    public void loaddeptandsem() {
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteddept = deptsp[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedsem = semsp[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadsubject() {
        System.out.println("Selected Sem:" + selectedsem);
        System.out.println("Selected DEPT:" + selecteddept);
        if (selecteddept == "IT") {
            if (selectedsem == "1") {
                String[] it1 = {"SELECT", "EG", "EEE", "EME", "CALCULUS", "CS"};
                ArrayAdapter<String> it1adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, it1);
                subject.setAdapter(it1adapter);
            }
            if (selectedsem == "2") {
                String[] it2 = {"SELECT", "CPD", "EWS", "OOPC", "MATHS 2", "BE"};
                ArrayAdapter<String> it2adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, it2);
                subject.setAdapter(it2adapter);
            }
            if (selectedsem == "3") {
                String[] it3 = {"SELECT", "DS", "DBMS", "AEM(MATHS-3)", "DE"};
                ArrayAdapter<String> it3adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, it3);
                subject.setAdapter(it3adapter);

            }
            if (selectedsem == "5") {
                String[] it5 = {"SELECT", "ADA", "OOPJ", "CS", "SP","CG"};
                ArrayAdapter<String> it5adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, it5);
                subject.setAdapter(it5adapter);

            }
            if (selectedsem == "6") {
                String[] it5 = {"SELECT", "ADJ", "DCDR", ".NET","WT"};
                ArrayAdapter<String> it5adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, it5);
                subject.setAdapter(it5adapter);

            }


        } else {
            String nothingstring[] = {"Nothing Selected!"};
            ArrayAdapter nothingadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, nothingstring);
            subject.setAdapter(nothingadapter);
        }
    }

    public void loadprofile() {
        Toast.makeText(getApplicationContext(), "Go to Profile Section for Editing Name!", Toast.LENGTH_SHORT).show();
    }
    public void loaddate()
    {

                Calendar c=Calendar.getInstance();
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                year=c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getAttendance.this,getAttendance.this,year,month,day);
                datePickerDialog.show();
    }


    public void loadbatch() {
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchsp = new String[]{"" + divisionsp[position] + "1", "" + divisionsp[position] + "2", "" + divisionsp[position] + "3", "Lecture"};
                ArrayAdapter batchadapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, batchsp);
                batch.setAdapter(batchadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        System.out.println("Batch Generated:" + batchsp);


    }

    public void getLecturer() {
        try {

            if(dateandday.getText().equals(""))
            {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Date!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (styear.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Student Year!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dept.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Department!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sem.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Semester!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (college.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the College!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (subject.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Subject!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (division.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Division!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (slot.getSelectedItem().toString().equals("SELECT")) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please Select the Slot!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (slot.getSelectedItem().toString().contains("LAB") == true && batch.getSelectedItem().toString().equals("Lecture") == true) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Batch must not be selected for lecture!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (slot.getSelectedItem().toString().contains("LEC") == true && batch.getSelectedItem().toString().equals("Lecture") == false) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "For Lecture Dont Select any Batches!", Toast.LENGTH_SHORT).show();
                return;
                }


            //firebase fetch
            final DatabaseReference fd;
            fd = FirebaseDatabase.getInstance().getReference();
            pb.setVisibility(View.VISIBLE);
            System.out.println("GET DAY:"+getDay);
            System.out.println("College:"+college.getSelectedItem().toString());
            System.out.println("DEPT:"+dept.getSelectedItem().toString());
            System.out.println("sem:"+sem.getSelectedItem().toString());
            System.out.println("Slot:"+slot.getSelectedItem().toString());
            System.out.println("Batch:"+batch.getSelectedItem().toString());
            fd.child("Faculty Data").child("TimeTable").child(college.getSelectedItem().toString()).child(dept.getSelectedItem().toString()).
                    child(dept.getSelectedItem().toString()+styear.getSelectedItem().toString()).child(dept.getSelectedItem().toString()+division.getSelectedItem().toString()).child("Class TT").child(getDay.toUpperCase()).child(slot.getSelectedItem().toString()).child(batch.getSelectedItem().toString())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pb.setVisibility(View.GONE);
                          for(DataSnapshot child:dataSnapshot.getChildren())
                          {

                              System.out.println("Lecturer name:"+child.getValue().toString());
                              System.out.println("Subject Fetched:"+child.getKey().toString());
                              System.out.println("Subject selected:"+subject.getSelectedItem().toString());
                              if(child.getKey().toString().equals(subject.getSelectedItem().toString())) {
                                  lecturerfound = true;
                                  lecturername=child.getValue().toString();
                              }

                          }
                            if(lecturerfound==false)
                            {
                                Toast.makeText(getApplicationContext(),"No Lecturer Found at this time!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(lecturerfound==true)
                            {
                                lecturernametxtview.setText(lecturername);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearfinal=year;
        monthfinal=month+1;
        dayfinal = dayOfMonth;
        dateandday.setText(""+dayfinal+"-"+monthfinal+"-"+yearfinal);
        Date date=new Date(year,month,dayOfMonth-1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE");
        getDay=simpleDateFormat.format(date);
        System.out.println("Day we got:"+getDay);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void fetchtimetable()
    {
        if(lecturernametxtview.getText().equals(""))
        {
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Please Fetch the Faculty Name!",Toast.LENGTH_SHORT).show();
            return;
        }

           pb.setVisibility(View.VISIBLE);
            final DatabaseReference fd;
            fd = FirebaseDatabase.getInstance().getReference();
        fd.child("Faculty Data").child("TimeTable").child(college.getSelectedItem().toString()).child(dept.getSelectedItem().toString()).
                child(dept.getSelectedItem().toString()+styear.getSelectedItem().toString()).child(dept.getSelectedItem().toString()+division.getSelectedItem().toString()).child("Class TT").child(getDay.toUpperCase()).child(slot.getSelectedItem().toString()).child(batch.getSelectedItem().toString())
        .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot child:dataSnapshot.getChildren())
                            {
                                ttfound=true;
                                System.out.println("Timetable Found!");
                                System.out.println("Key found at this timetable:"+child.getKey());
                                System.out.println("Value found at this timetable:"+child.getValue());
                            }
                            if(ttfound==false)
                            {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"No Timetable Found at this slot!Please add it first!",Toast.LENGTH_LONG).show();
                            }
                            if(ttfound==true)
                            {
                                pb.setVisibility(View.GONE);
                                if(batch.getSelectedItem().toString().equals("Lecture"))
                                {
                                    Toast.makeText(getApplicationContext(), "Timetable found!Loading...", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getAttendance.this, MainActivity2.class);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Timetable found!Loading...", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getAttendance.this, getbatch.class);
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }



}
