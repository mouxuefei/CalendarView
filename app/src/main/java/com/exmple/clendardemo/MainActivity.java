package com.exmple.clendardemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.exmple.clendardemo.date.adapter.CalendarViewPagerAdapter;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewpager = findViewById(R.id.viewpager);
        CalendarViewPagerAdapter vpAdapter = new CalendarViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(vpAdapter);
        viewpager.setCurrentItem(501);
    }
}
