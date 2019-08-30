package com.kaival.smartcollege;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class sliding_activitymain extends AppCompatActivity {
    private ViewPager mslideViewPager;
    private LinearLayout mdotsLayout;
    private sliderAdapater sliderAdapater;
    private Button next,prev;
    private int mcurrentpage;
    TextView[] mdots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sliding_activitymain);
            mslideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
            mdotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
            next = (Button) (findViewById(R.id.nextbutton));
            prev = (Button) (findViewById(R.id.prevbutton));


            sliderAdapater = new sliderAdapater(this);
            mslideViewPager.setAdapter(sliderAdapater);
            addDotsIndicators(0);
            mslideViewPager.addOnPageChangeListener(viewlistener);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mslideViewPager.setCurrentItem(mcurrentpage + 1);
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mslideViewPager.setCurrentItem(mcurrentpage - 1);
                }
            });


        }


    public void addDotsIndicators(int position) {
        mdots = new TextView[4];
        mdotsLayout.removeAllViews();
        for (int i = 0; i < mdots.length; i++) {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setGravity(Gravity.CENTER);
            mdots[i].setTextColor(getResources().getColor(R.color.transparentgrey));
            mdotsLayout.addView(mdots[i]);
        }
        if(mdots.length>0)
        {
            mdots[position].setTextColor(getResources().getColor(R.color.whiteTextcolor));
        }
    }
    ViewPager.OnPageChangeListener viewlistener=new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicators(position);
            mcurrentpage=position;
            if(position==0)
            {
                next.setEnabled(true);
                prev.setEnabled(false);
                prev.setVisibility(View.INVISIBLE);
                next.setText("NEXT");
                prev.setText("");
            }
            else if(position==mdots.length-1 )
            {
                next.setEnabled(true);
                prev.setEnabled(true);
                prev.setVisibility(View.VISIBLE);
                next.setText("FINISH");
                prev.setText("PREVIOUS");
            }
            else
            {
                next.setEnabled(true);
                prev.setEnabled(true);
                prev.setVisibility(View.VISIBLE);
                next.setText("NEXT");
                prev.setText("PREVIOUS");
            }
            if(next.getText().equals("FINISH"))
            {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(sliding_activitymain.this,welcome_page.class);
                        startActivity(i);
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




}
