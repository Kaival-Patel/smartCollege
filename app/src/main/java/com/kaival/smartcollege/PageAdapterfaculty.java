package com.kaival.smartcollege;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class PageAdapterfaculty extends FragmentStatePagerAdapter {
    int num_of_tabs;
    public PageAdapterfaculty(FragmentManager fm,int number_of_tabs) {
        super(fm);
        this.num_of_tabs=number_of_tabs;

    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            frag_fac_sign_in fsignintab=new frag_fac_sign_in();
            return fsignintab;
        }
        else if(position==1)
        {
            frag_fac_sign_up fsignuptab=new frag_fac_sign_up();
            return fsignuptab;
        }
        else{return null;}
    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
