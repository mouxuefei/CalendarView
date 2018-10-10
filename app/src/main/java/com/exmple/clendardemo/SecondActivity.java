package com.exmple.clendardemo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exmple.clendardemo.calendar.CaledarAdapter;
import com.exmple.clendardemo.calendar.CalendarBean;
import com.exmple.clendardemo.calendar.CalendarDateView;
import com.exmple.clendardemo.calendar.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: SecondActivity.java
 * @author: villa_mou
 * @date: 10-13:43
 * @desc
 */
public class SecondActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CalendarBean calendarBean = (CalendarBean) msg.obj;
            String substring = formatDate(calendarBean.date).substring(0, 6);
            Log.e("villa", substring);
            data.put(substring + "05", new OverAndEndBean(0, true, false));
            data.put(substring + "11", new OverAndEndBean(10, true, true));
            data.put(substring + "22", new OverAndEndBean(180, false, true));
            data.put(substring + "14", new OverAndEndBean(0, true, false));
            data.put(substring + "30", new OverAndEndBean(0, true, false));
            mMCalendarDateView.notifyDataChanged();
        }
    };
    private CalendarDateView mMCalendarDateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mMCalendarDateView = findViewById(R.id.calendarDateView);
        Button btn = findViewById(R.id.btn);
//        ListView mlist = findViewById(R.id.list);
        mMCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {

                TextView tvNumber;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_layout, null);
                }
                tvNumber = convertView.findViewById(R.id.tv_item_calendar_time);
                tvNumber.setText("" + bean.day);
                TextView tvEndNumber = convertView.findViewById(R.id.tv_item_calendar_thing);
                TextView tvOver = convertView.findViewById(R.id.tv_item_calendar_over);
                View rootView = convertView.findViewById(R.id.item_calendar_root);
                if (bean.mothFlag != 0) {//不是当月
//                    tvEndNumber.setBackgroundResource(R.drawable.shape_circle_item_halfred);
//                    tvOver.setBackgroundResource(R.drawable.shape_circle_item_halfblue);
//                    if (bean.week == 1 || bean.week == 7) {
//                        tvNumber.setTextColor(Color.parseColor("#44FF0000"));
//                    } else {
//                        tvNumber.setTextColor(Color.parseColor("#44222222"));
//                    }
                    rootView.setVisibility(View.GONE);

                } else {//当月
                    rootView.setVisibility(View.VISIBLE);

                    tvEndNumber.setBackgroundResource(R.drawable.shape_circle_item_red);
                    tvOver.setBackgroundResource(R.drawable.shape_circle_item_blue);
                    if (bean.week == 1 || bean.week == 7) {
                        tvNumber.setTextColor(Color.parseColor("#FF0000"));
                    } else {
                        tvNumber.setTextColor(Color.parseColor("#222222"));
                    }
                }

                if (isNow(bean.date)) {
                    rootView.setBackgroundResource(R.drawable.today_shape);
                } else {
                    rootView.setBackgroundResource(R.drawable.selector_bg);
                }
                if (data.containsKey(formatDate(bean.date))) {
                    OverAndEndBean overAndEndBean = data.get(formatDate(bean.date));
                    //逾期
                    if (overAndEndBean.isOver) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }
                    //到期
                    if (overAndEndBean.isEnd) {
                        tvEndNumber.setVisibility(View.VISIBLE);
                        tvEndNumber.setText(overAndEndBean.numHouse + "间");
                    } else {
                        tvEndNumber.setVisibility(View.GONE);
                    }
                } else {
                    tvOver.setVisibility(View.GONE);
                    tvEndNumber.setVisibility(View.GONE);
                }
                return convertView;
            }
        });

        mMCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, final CalendarBean bean) {
                String s = formatDate(bean.date);
                Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelect(View view, int postion, final CalendarBean bean) {
                String s = formatDate(bean.date);
                Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.obj = bean;
                        mHandler.sendMessage(message);
                        String s = formatDate(bean.date);
                    }
                }, 500);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.put("2018" + 10 + "01", new OverAndEndBean(0, true, false));
                data.put("2018" + 10 + "11", new OverAndEndBean(10, true, true));
                data.put("2018" + 10 + "22", new OverAndEndBean(120, false, true));
                data.put("2018" + 10 + "28", new OverAndEndBean(0, true, false));
                data.put("2018" + 10 + "30", new OverAndEndBean(0, true, false));
                mMCalendarDateView.notifyDataChanged();
            }
        });

    }

    HashMap<String, OverAndEndBean> data = new HashMap<>();
    private SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    private Date now = new Date();

    public String formatDate(Date date) {
        return sf.format(date);
    }

    /**
     * 判断时间是不是今天
     */
    private boolean isNow(Date date) {
        String nowDay = sf.format(now);
        String day = sf.format(date);
        return day.equals(nowDay);
    }

}