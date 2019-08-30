package com.kaival.smartcollege;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Type;

public class welcome_splash_screen extends AppCompatActivity {
TextView tv,tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash_screen);
        tv=findViewById(R.id.tv);
        tv1=findViewById(R.id.tv1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome=new Intent(welcome_splash_screen.this,welcome_page.class);
                startActivity(welcome);
                finish();
            }
        },1250);
        Typeface robotoreg = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        Typeface oxygen= Typeface.createFromAsset(getAssets(), "Oxygen-Bold.otf");
        tv.setTypeface(robotoreg);
        tv1.setTypeface(oxygen);
    }
}
