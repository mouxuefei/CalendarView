package com.exmple.clendardemo.date.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.exmple.clendardemo.date.DateFragment;


public class CalendarViewPagerAdapter extends FragmentPagerAdapter {
    public CalendarViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DateFragment.getInstance(computeDate(position));
    }

    @Override
    public int getCount() {
        return 1001;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    public int computeDate(int position) {
        int offset = position - 501;
        return offset;
    }
}
