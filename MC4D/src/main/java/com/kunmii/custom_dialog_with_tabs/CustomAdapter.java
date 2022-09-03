package com.kunmii.custom_dialog_with_tabs;

//based on http://kunmii.blogspot.com/2017/01/how-to-create-custom-dialog-with-tabs.html

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends FragmentPagerAdapter {
    Fragment[] mFragmentCollection;
    String[] mTitleCollection;
    public CustomAdapter(FragmentManager fm, int N) {
        super(fm);
        mFragmentCollection = new Fragment[N];
        mTitleCollection = new String[N];
    }
    public void addFragment(String title, Fragment fragment, int i)
    {
        mTitleCollection[i] = title;
        mFragmentCollection[i] = fragment;
    }
    //Needed for
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleCollection[position];
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentCollection[position];
    }
    @Override
    public int getCount() {
        return mFragmentCollection.length;
    }
}