package com.kaival.smartcollege;


import android.graphics.pdf.PdfDocument;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

public class StudentPagerAdapter extends FragmentStatePagerAdapter {
    int nooftabs;

    public StudentPagerAdapter(FragmentManager fm, int NumberofTabs) {
        super(fm);
        this.nooftabs = NumberofTabs;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            sign_in_tab tab1 = new sign_in_tab();

            return tab1;
        }

        else if (position == 1) {
            sign_up_student tab2 = new sign_up_student();
            return tab2;
        } else {
            return null;
        }

    }

    @Override
    public int getCount() {
        return nooftabs;
    }
}