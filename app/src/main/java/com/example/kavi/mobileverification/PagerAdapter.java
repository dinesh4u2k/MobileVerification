package com.example.kavi.mobileverification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by vignesh on 07-09-2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
              Home1 home = new Home1();
                return home;
            case 1:
                Myaccount myaccount = new Myaccount();
                return  myaccount;
             default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
