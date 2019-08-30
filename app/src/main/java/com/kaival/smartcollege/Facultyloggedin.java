package com.kaival.smartcollege;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import junit.runner.Version;

import de.hdodenhof.circleimageview.CircleImageView;


public class Facultyloggedin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView Logout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    TextView attendance, mid1, mid2, timetable,generate;
    NavigationView navview;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AdView madview;
    FirebaseAuth mauth;
    FirebaseUser user;
    private fragment_upload upload_fragmet;
    private fragment_edit edit_fragment;
    private timetable_frag timetable_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyloggedin);
        Intent getint = getIntent();
        final String getusername = getint.getStringExtra("Username");
        final String gettype = getint.getStringExtra("type");
        System.out.println("Username got from Intent!" + getusername);
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        collapsingToolbarLayout=findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitle("");
        mauth = FirebaseAuth.getInstance();
        if(mauth.getCurrentUser()!=null) {
            user = mauth.getCurrentUser();
            if (user.getDisplayName() != null) {
                collapsingToolbarLayout.setTitle("Hello " + user.getDisplayName());
            } else {
                collapsingToolbarLayout.setTitle("My DashBoard");
            }
        }
        else {
            collapsingToolbarLayout.setTitle("My DashBoard");
        }
        getSupportActionBar().setTitle("Faculty DashBoard");
        Logout = (findViewById(R.id.logout));
        drawerLayout = (findViewById(R.id.mydrawer));
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attendance = findViewById(R.id.atttxtview);
        mid1 = findViewById(R.id.mid1marks);
        mid2 = findViewById(R.id.mid2marks);
        generate=findViewById(R.id.generatereporttxtview);
        timetable = findViewById(R.id.timetabletxtview);
        setNavigationViewListner();
        navview=findViewById(R.id.nav_view);
        View headerview=navview.getHeaderView(0);
        TextView fname=headerview.findViewById(R.id.sidebarname);
        TextView femail=headerview.findViewById(R.id.sidebaremail);
        CircleImageView imgprofile=headerview.findViewById(R.id.sidebarimg);
        Typeface productsans = Typeface.createFromAsset(getAssets(), "Product Sans Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen.otf");
        fname.setTypeface(oxygen);
        femail.setTypeface(productsans);
        if (user.getDisplayName() != null) {
            femail.setText(""+user.getEmail().toString());
            fname.setText(""+user.getDisplayName().toString());
            if (user.getPhotoUrl() != null){
            Glide.with(this).load(user.getPhotoUrl().toString()).into(imgprofile);
            }
            else if(user.getPhotoUrl() == null)
            {
                imgprofile.setImageResource(R.drawable.profilelogo);
            }
        }
        else{
            femail.setText("");
            fname.setText("Hello Professor");
        }
        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Facultyloggedin.this,profileactivity.class);
                i.putExtra("type","professor");
                startActivity(i);
            }
        });

        mid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Facultyloggedin.this,Midmarkssetup.class);
                startActivity(i);
            }
        });
        mid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Facultyloggedin.this,Midmarkssetup.class);
                startActivity(i);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent i = new Intent(Facultyloggedin.this, getAttendance.class);
                    startActivity(i);

                }
                Intent i = new Intent(Facultyloggedin.this, getAttendance.class);
                startActivity(i);
                finish();
            }
        });
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Facultyloggedin.this, com.kaival.smartcollege.timetable.class);
                startActivity(i);
                finish();
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Facultyloggedin.this,Generate_Reports.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setCancelable(false).setTitle("Logout?").setMessage("Going Back will Log you out! Agree?").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
                Intent i = new Intent(Facultyloggedin.this, welcome_page.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }).setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int menuid = item.getItemId();
        System.out.println("Id we got:" + menuid);
        if (menuid == R.id.profile) {
            Toast.makeText(this, "You Selected for Profile", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Facultyloggedin.this,profileactivity.class);
            i.putExtra("type","professor");
            startActivity(i);
            return true;
        } else if (menuid == R.id.logout) {
            Toast.makeText(this, "You Selected for Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
            Intent i = new Intent(Facultyloggedin.this, whichactivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        } else if (menuid == R.id.notifications) {
            Toast.makeText(this, "Choose the Notification Channel of Your Choice!", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Facultyloggedin.this, NotificationSetting.class);
            startActivity(i);
            return true;
        } else if (menuid == R.id.cndatabase) {
            Toast.makeText(this, "ALERT! You are going to Delete Student Database here", Toast.LENGTH_LONG).show();
            Intent i=new Intent(Facultyloggedin.this, stdatabasesetting.class);
            startActivity(i);
            return true;}
            else {

            return false;
        }
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}



