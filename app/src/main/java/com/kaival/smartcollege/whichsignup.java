package com.kaival.smartcollege;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


public class whichsignup extends AppCompatActivity implements sign_up_student.OnFragmentInteractionListener,sign_in_tab.OnFragmentInteractionListener{
    Button fsignup,stsignup,gsignin;
    BottomNavigationView main_bottom_view;
    MenuItem M1,M2;
    TextView tv,tv1;
    ViewPager viewPager;
    PagerAdapter adapter;
    ScrollView scrollView;

    AnimationDrawable animationDrawable,animationDrawable1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whichsignup);
        Typeface welcomefont = Typeface.createFromAsset(getAssets(), "AvenirNextLTPro-Bold.otf");
        Typeface fillupfont = Typeface.createFromAsset(getAssets(), "sf-ui-text-regular.otf");
       tv=(TextView)(findViewById(R.id.welcomeText));
       tv1=(TextView)(findViewById(R.id.filluptext));
        tv.setTypeface(welcomefont);
        tv1.setTypeface(fillupfont);
        viewPager=(ViewPager)(findViewById(R.id.viewpager));
        scrollView=findViewById(R.id.scrollview);
        animationDrawable=(AnimationDrawable)scrollView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        TabLayout tabLayout=(TabLayout)(findViewById(R.id.tablayout));
        tabLayout.addTab(tabLayout.newTab().setText("Sign-in"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign-Up"));
        animationDrawable1=(AnimationDrawable)tabLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(2000);
        animationDrawable1.start();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter=new StudentPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("TAB.getPosition:"+tab.getPosition()+","+tab.getText());
                viewPager.setCurrentItem(tab.getPosition());
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
    public void onFragmentInteraction(Uri uri0) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(whichsignup.this,welcome_page.class);
        startActivity(i);

    }
}
