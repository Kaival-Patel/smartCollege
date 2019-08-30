package com.kaival.smartcollege;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class sliderAdapater extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public boolean showthiseverytime=true;
    public sliderAdapater(Context context )
    {
        this.context=context;
    }
    public int[] slide_images={

            R.drawable.plus,
            R.drawable.graduate,
            R.drawable.teacher,
            R.drawable.rocket
};
     public String[] slide_headings={"SIGN-UP","STUDENT SIGN-IN","FACULTY SIGN-IN","Ready To Rock"};

    public String[] slide_text ={
        "First All Students and Faculties are required to signed up in this app",
            "Now Sign in using Student login Credentials",
            "Now Sign in using Faculty Login Credentials and Input the marks",
            "Thats it Now You're Ready To Rock!!"

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageview=(ImageView)view.findViewById(R.id.imageView);
        TextView slideheadings=(TextView)view.findViewById(R.id.heading);
        TextView slidecontents=(TextView)view.findViewById(R.id.content);
        slideImageview.setImageResource(slide_images[position]);
        slideheadings.setText(slide_headings[position]);
        slidecontents.setText(slide_text[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}

