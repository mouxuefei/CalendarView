package com.exmple.clendardemo.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.exmple.clendardemo.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static com.exmple.clendardemo.calendar.CalendarFactory.getMonthOfDayList;


/**
 * Created by codbking on 2016/12/18.
 * email:codbking@gmail.com
 * github:https://github.com/codbking
 * blog:http://www.jianshu.com/users/49d47538a2dd/latest_articles
 */

public class CalendarDateView extends ViewPager {
    HashMap<Integer, CalendarView> views = new HashMap<>();
    private CalendarView.OnItemClickListener onItemClickListener;
    private LinkedList<CalendarView> cache = new LinkedList();
    private int row = 6;
    private CaledarAdapter mAdapter;
    private int calendarItemHeight = 0;

    public void setAdapter(CaledarAdapter adapter) {
        mAdapter = adapter;
        initData();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        row = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 6);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                calendarItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {

        setAdapter(mPagerAdapter);

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (onItemClickListener != null) {
                    CalendarView view = views.get(position);
                    Object[] obs = view.getSelect();
                    onItemClickListener.onPageSelect((View) obs[0], (int) obs[1], (CalendarBean) obs[2]);
                }
//                if (mCaledarLayoutChangeListener != null) {
//                    mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
//                }
            }
        });
    }

    public void notifyDataChanged() {
        if (views!=null&&views.size()>0){
            Iterator iter = views.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                CalendarView val = (CalendarView) entry.getValue();
                val.setItem();
            }
        }
    }

    private final int[] dateArr = CalendarUtil.getYMD(new Date());
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            CalendarView view;
            if (!cache.isEmpty()) {
                view = cache.removeFirst();
            } else {
                view = new CalendarView(container.getContext(), row);
            }
            view.setOnItemClickListener(onItemClickListener);
            view.setAdapter(mAdapter);
            view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2);
            container.addView(view);
            views.put(position, view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            cache.addLast((CalendarView) object);
            views.remove(position);
        }
    };

    private void initData() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();
    }


}
