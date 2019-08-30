package com.kaival.smartcollege;


import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.ScientificNumberFormatter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class whichactivity extends AppCompatActivity implements frag_fac_sign_up.OnFragmentInteractionListener,frag_fac_sign_in.OnFragmentInteractionListener {
    ViewPager mviewpager;
    PagerAdapter mpager;
    TextView tv,tv1;
    ScrollView scrollView;
    LinearLayout linearLayout;
    AnimationDrawable animationDrawable,animationDrawable1;
    TabLayout tabLayout;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    boolean foundinfaculty=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whichactivity);
        tv=(TextView)(findViewById(R.id.welcomeText));
        tv1=(TextView)(findViewById(R.id.filluptext));
        scrollView=findViewById(R.id.scrollview);
        //linearLayout=findViewById(R.id.linearlayout);
        tabLayout=findViewById(R.id.faculytablayout);
       animationDrawable=(AnimationDrawable)scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        animationDrawable1=(AnimationDrawable)tabLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(2000);
        animationDrawable1.start();

        Typeface welcomefont = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");
        Typeface fillupfont = Typeface.createFromAsset(getAssets(), "sf-ui-text-regular.otf");
        tv.setTypeface(welcomefont);
        tv1.setTypeface(fillupfont);
        mviewpager=(ViewPager)(findViewById(R.id.facultyviewpager));
        TabLayout facultytabLayout=(TabLayout)(findViewById(R.id.faculytablayout));
        facultytabLayout.addTab(facultytabLayout.newTab().setText("Sign-in"));
        facultytabLayout.addTab(facultytabLayout.newTab().setText("Sign-Up"));
        facultytabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        System.out.println("TAB COUNT:"+facultytabLayout.getTabCount());
        mpager=new PageAdapterfaculty(getSupportFragmentManager(),facultytabLayout.getTabCount());
        mviewpager.setAdapter(mpager);
        mviewpager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(facultytabLayout));
        facultytabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mviewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onFragmentInteraction(String uri01) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(whichactivity.this,welcome_page.class);
        startActivity(i);
    }
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            System.out.println("Mauth=" + mAuth.getUid());
            mAuth.signOut();
        }
    }
}

