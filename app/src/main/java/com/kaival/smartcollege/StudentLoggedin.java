package com.kaival.smartcollege;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentLoggedin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //components in android
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    CardView mid1card, mid2card, attendance, tt;
    ImageView ttimg;
    TextView attxt, mid1txt, mid2txt;
    ProgressBar pb;
    String enrollment;
    Button savebtn;
    NavigationView navigationView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    boolean infosaved=false;
    ArrayList<String> batches=new ArrayList<>();
    //database decalarations
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    String fbatch,fdept,fdiv,fyear,fenrollment;
    //boolean and integer decalarations
    boolean isdatasaved;
    String admissionyearval=null;
    String deptval=null;
    String divval=null;
    String batchval=null;
    int totaldays=0;
    int totallecsattended=0;
    int totallecs=0;
    boolean datainit=false;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_loggedin);
        drawerLayout = (findViewById(R.id.studentdrawer));
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student DashBoard");
        mid1card = (CardView) findViewById(R.id.mid1card);
        mid2card = (CardView) findViewById(R.id.mid2card);
        attendance = (CardView) findViewById(R.id.attendancecard);
        tt = (CardView) findViewById(R.id.ttcard);
        ttimg = (ImageView) findViewById(R.id.imgtimetable);
        attxt = (TextView) findViewById(R.id.atttxt);
        navigationView=findViewById(R.id.nav_view_student);
        View headerview=navigationView.getHeaderView(0);
        TextView fname=headerview.findViewById(R.id.sidebarname);
        TextView femail=headerview.findViewById(R.id.sidebaremail);
        CircleImageView circleImageView=headerview.findViewById(R.id.sidebarimg);
        mid1txt = (TextView) findViewById(R.id.mid1txt);
        mid2txt = (TextView)findViewById(R.id.mid2txt);
        pb=(ProgressBar)findViewById(R.id.pb);
        collapsingToolbarLayout=findViewById(R.id.collapsetoolbar);
        Intent intent=this.getIntent();
        enrollment=intent.getStringExtra("Username");
        System.out.println("Enrollment:"+enrollment);
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        if (user.getDisplayName() != null) {
            collapsingToolbarLayout.setTitle("Hello "+user.getDisplayName().toString());
        } else {
            collapsingToolbarLayout.setTitle("My DashBoard");
        }
        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        fname.setTypeface(oxygen);
        femail.setTypeface(productsans);
        if (user.getDisplayName() != null) {
            femail.setText(""+user.getEmail().toString());
            fname.setText(""+user.getDisplayName().toString());
            if (user.getPhotoUrl() != null){
                Glide.with(this).load(user.getPhotoUrl().toString()).into(circleImageView);
            }
            else if(user.getPhotoUrl() == null)
            {
                circleImageView.setImageResource(R.drawable.profilelogo);
            }
        }
        else{
            femail.setText("");
            fname.setText("Hello Student");
        }
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StudentLoggedin.this,profileactivity.class);
                i.putExtra("type","student");
                startActivity(i);
            }
        });
        mid1card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentLoggedin.this,ViewMidMarks.class);
                startActivity(i);
            }
        });
        mid2card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentLoggedin.this,ViewMidMarks.class);
                startActivity(i);
            }
        });
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentLoggedin.this, WebViewGTU.class);
                startActivity(i);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentLoggedin.this,ViewAttendance.class);
                startActivity(i);
                finish();
            }
        });
        setNavigationViewListner();
        pb.setVisibility(View.VISIBLE);
        final fetchinfo fi=new fetchinfo();
        fi.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(totallecsattended>0 && totaldays > 0 && totallecs>0 )
                {
                    float percentage=(float)totallecsattended/(float)totallecs;
                    percentage*=100;
                    attxt.setText(""+(int)percentage+"%");
                    pb.setVisibility(View.GONE);
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    FirebaseAuth mauth=FirebaseAuth.getInstance();
                    FirebaseUser user=mauth.getCurrentUser();
                    firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Attendance").setValue(""+(int)percentage);
                }
                else
                {
                    FirebaseAuth mauth=FirebaseAuth.getInstance();
                    FirebaseUser user=mauth.getCurrentUser();
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                if(ds.getKey().equals("Attendance"))
                                {
                                    attxt.setText(""+ds.getValue().toString()+"%");
                                    Toast.makeText(getApplicationContext(),"Couldn't Refresh DashBoard!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                pb.setVisibility(View.GONE);
            }
        },2000);


    }
    private class fetchinfo extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pd;
        boolean fetched=false;
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase db=FirebaseDatabase.getInstance();
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            db.getReference().child("Students").child(""+firebaseUser.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild("Division") && !dataSnapshot.hasChild("Department") && !dataSnapshot.hasChild("Year") && !dataSnapshot.hasChild("Batch"))
                    {
                        showAlertDialog();
                    }
                    else
                    {
                        System.out.println("DATASAVED TRUEE::::::::");
                        isdatasaved=true;
                        if(isdatasaved==true)
                        {
                            calculatedashboard cdb=new calculatedashboard();
                            cdb.execute();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    calculatedashboard cdb=new calculatedashboard();
                                    cdb.dofetch();
                                }
                            },1000);

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            pd=ProgressDialog.show(StudentLoggedin.this,"Loading Data","Fetching Info");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
        }
    }
    public void showAlertDialog()
    {
        Spinner admissionyear;
        Spinner deptspinner;
        final Spinner batchspinner;
        Spinner divspinner;
        AlertDialog.Builder ad=new AlertDialog.Builder(StudentLoggedin.this);
        View v=LayoutInflater.from(StudentLoggedin.this).inflate(R.layout.studentloggedincustom_alert,null);
        ad.setMessage("Fill Info").setView(v).setCancelable(false);
        final AlertDialog dialog=ad.create();
        dialog.setView(v);
        batchspinner=v.findViewById(R.id.batchpicker);
        batchspinner.setEnabled(false);
        ArrayList<String> years=new ArrayList<>();
        years.add("SELECT");
        for(int i=2015;i<2030;i++)
        {
            years.add(""+i);
        }
        ArrayList<String> depts=new ArrayList<>();
        depts.add("SELECT");
        depts.add("IT");
        depts.add("COMPUTER");
        depts.add("MECHANICAL");
        depts.add("CIVIL");
        batches.add("SELECT");
        ArrayList<String> divs=new ArrayList<>();
        divs.add("SELECT");
        for(char ch='A';ch<'H';ch++)
        {
            divs.add(""+ch);
        }



        ArrayAdapter<String> spinneryearadapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,years);
        ArrayAdapter<String> deptadapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,depts);
        ArrayAdapter<String> batchadapater=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,batches);
        final ArrayAdapter<String> divadapater=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,divs);




        admissionyear=v.findViewById(R.id.yearpickerbtn);
        admissionyear.setAdapter(spinneryearadapter);
        deptspinner=v.findViewById(R.id.departmentpicker);
        deptspinner.setAdapter(deptadapter);
        batchspinner.setAdapter(batchadapater);
        savebtn=v.findViewById(R.id.savebtn);
        divspinner=v.findViewById(R.id.divpicker);
        divspinner.setAdapter(divadapater);

        admissionyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                admissionyearval=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deptspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deptval=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        divspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divval=parent.getItemAtPosition(position).toString();
                if(divval!=null && !divval.equals("SELECT"))
                {
                    batchspinner.setEnabled(true);
                    for(int i=1;i<4;i++)
                    {
                        batches.add(""+divval+""+i+" BATCH");
                    }
                }
                else
                {
                    batchspinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        batchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batchval=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(divval.equals("SELECT") || divval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Division!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(admissionyearval.equals("SELECT") || admissionyearval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Year!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(batchval.equals("SELECT") || batchval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Batch!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(deptval.equals("SELECT") || deptval==null)
                {
                    Toast.makeText(getApplicationContext(),"Enter Department!",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                FirebaseAuth mauth=FirebaseAuth.getInstance();
                FirebaseUser user=mauth.getCurrentUser();
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Division").setValue(divval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Year").setValue(admissionyearval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Department").setValue(deptval);
                firebaseDatabase.getReference().child("Students").child(""+user.getUid().toString()).child("Batch").setValue(batchval);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Data Saved!Tap Again to Close", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setCancelable(false).setTitle("Logout?").setMessage("Going Back will Log you out! Agree?").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
                Intent i=new Intent(StudentLoggedin.this,whichsignup.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }).setNegativeButton("Cancel",null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            System.out.println("Item i got:" + item);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_student);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuid = item.getItemId();
        System.out.println("Id we got:" + menuid);
        if (menuid == R.id.profile) {
            Intent i=new Intent(StudentLoggedin.this,profileactivity.class);
            i.putExtra("type","student");
            startActivity(i);
            return true;
        } else if (menuid == R.id.logout) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
            Intent i=new Intent(StudentLoggedin.this,whichsignup.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        } else if (menuid == R.id.settings) {
            Toast.makeText(this, "Setting Section Coming Soon..", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return true;
        }
    }

    private class calculatedashboard extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd=ProgressDialog.show(StudentLoggedin.this,"Calculating!!","Filling DashBoard!!!");
        }

        public void dofetch()
        {
            ProgressDialog pd;
            pd=ProgressDialog.show(StudentLoggedin.this,"Loading.....","Fetching DATA....");
            System.out.println("DOING FETCHINGG:::::::::");
            db.getReference().child("Students Data").child("" + fdept + "" + fyear).child("" + fdept + "" + fdiv).child("" + fbatch).child("Attendance")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            totaldays +=1;
                            System.out.println("totaldays:"+totaldays);
                            System.out.println("DS:"+dataSnapshot.getValue());
                            for(DataSnapshot str:dataSnapshot.getChildren())
                            {
                                totallecs+=1;
                                System.out.println("TOTAL LECS:"+totallecs);
                                if(str.getValue().toString().contains(fenrollment))
                                {
                                    totallecsattended+=1;
                                    System.out.println("TOTAL LECS ATTENDED:"+totallecsattended);
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            pd.dismiss();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
                //first fetch the values from firebase
                System.out.println("HELLO DOING READY SCREEN!!!!");
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                FirebaseAuth mauth=FirebaseAuth.getInstance();
                FirebaseUser user=mauth.getCurrentUser();
                db.getReference().child("Students").child(""+user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if(ds.getKey().equals("Batch"))
                            {
                                fbatch=ds.getValue().toString();
                                System.out.println("FBATCH:"+fbatch);
                                datainit=true;
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
                            else if(ds.getKey().equals("senroll"))
                            {
                                fenrollment=ds.getValue().toString();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //now calculate attendance




                //calculate mid marks
            return null;
        }
    }
}



