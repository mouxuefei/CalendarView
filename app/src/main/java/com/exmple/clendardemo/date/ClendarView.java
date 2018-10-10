package com.exmple.clendardemo.date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.exmple.clendardemo.OverAndEndBean;
import com.exmple.clendardemo.R;
import com.exmple.clendardemo.date.common.CommonAdapter;
import com.exmple.clendardemo.date.common.OnItemClickListener;
import com.exmple.clendardemo.date.common.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * 作者: 伍跃武
 * 时间： 2018/5/9
 * 描述：自定义的日历控件
 */

public class ClendarView extends FrameLayout implements OnItemClickListener {
    private RecyclerView recyclerView;
    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
    //每个数据单元格中数据
    private List<ClendarInfo> dateList = new ArrayList<>();
    //    private Map<String, OverAndEndBean> datas = new HashMap<>();
    private DateAdapter adapter;

    public ClendarView(Context context) {
        this(context, null);
    }

    public ClendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        initView(context, view, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        initView(context, view, attrs);
    }

    @LayoutRes
    private int getLayout() {
        return R.layout.calendarview_layout;
    }

    @SuppressLint("WrongViewCast")
    private void initView(Context context, View view, AttributeSet attrs) {
        initRecyclerView(context, view);
//        initData(context);
    }

    private void initRecyclerView(Context context, View view) {
        recyclerView = view.findViewById(R.id.recy_calendar);
        GridLayoutManager manager = new GridLayoutManager(context, 7){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
    }

    /**
     * 设置当前页面的日期
     */
    public void setCurrentPosition(int position) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MONTH, position);
        mCalendar = calendar;
        initData(getContext());
    }

    private void initData(Context context) {
        dateList.clear();
        //表格中的数据
        Calendar calendar = (Calendar) mCalendar.clone();
//        //设置时间到当前月份的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //1---代表周日  2---代表周一
        //获取日期的偏移量
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK);
        int preDays = firstDay - 1;
        //仅仅美观操作，下面代码可加可不加 效果参见pc系统的日历月份调至  2018-4
        //为了保证第一行一定是 上个月+这个月(可能没有) 的数据 ，最后一行一定是 这个月（可能没有）+下个月 的数据
        preDays = preDays == 0 ? 7 : preDays;
        //将偏移量移至上个月，把上个月的几天添加到本月的日历中
        calendar.add(Calendar.DAY_OF_MONTH, -preDays);
        int maxDays = 6 * 7;

        for (int i = 0; i < maxDays; i++) {
            Date time = calendar.getTime();
            ClendarInfo clendarInfo = new ClendarInfo();
            clendarInfo.setDate(time);

//            if (datas != null && datas.size() > 0) {
//                String cureentDate = formatDate(time);
//                Log.e("villa", cureentDate);
//                if (datas.containsKey(cureentDate)) {
//                    OverAndEndBean overAndEndBean = datas.get(cureentDate);
//                    clendarInfo.isOver = overAndEndBean.isOver;
//                    clendarInfo.isEnd = overAndEndBean.isEnd;
//                    clendarInfo.numHouse = overAndEndBean.numHouse;
//                }
//            }
            dateList.add(clendarInfo);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //  Log.i("TAG", "dataMap=" + dataMap.toString());
        //将日历数据填充到recycleview中
        if (null == adapter) {
            adapter = new DateAdapter(context, R.layout.item_calendar_layout, dateList);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 设置到期数据
     */
    public void setOverAndEndDateBean(Map<String, OverAndEndBean> data) {
        if (data == null && data.size() == 0) {
            return;
        }
        for (int i = 0; i < dateList.size(); i++) {
            ClendarInfo clendarInfo = dateList.get(i);
            String cureentDate = formatDate(clendarInfo.date);
            if (data.containsKey(cureentDate)) {
                OverAndEndBean bean = data.get(cureentDate);
                clendarInfo.isOver = bean.isOver;
                clendarInfo.isEnd = bean.isEnd;
                clendarInfo.numHouse = bean.numHouse;
            }
        }
        adapter.notifyDataSetChanged();
//        initData(getContext());
    }


    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        if (null != callBackListener) {
            callBackListener.callBack(o, position);
        }
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }


    class DateAdapter extends CommonAdapter<ClendarInfo> {
        public DateAdapter(Context context, int layoutId, List<ClendarInfo> datas) {
            super(context, layoutId, datas);
        }

        private Calendar cal = Calendar.getInstance();

        @Override
        public void convert(ViewHolder holder, ClendarInfo clendarInfo) {
            Date date = clendarInfo.getDate();
            Date currentDate = mCalendar.getTime();
            holder.setText(R.id.tv_item_calendar_time, String.valueOf(date.getDate()));
            cal.setTime(date);
            if (date.getMonth() == currentDate.getMonth()) {//当前月份
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    holder.setTextColor(R.id.tv_item_calendar_time, Color.RED);
                } else {
                    holder.setTextColor(R.id.tv_item_calendar_time, Color.parseColor("#222222"));
                }
                holder.setBackgroundRes(R.id.tv_item_calendar_thing, R.drawable.shape_circle_item_red);
                holder.setBackgroundRes(R.id.tv_item_calendar_over, R.drawable.shape_circle_item_blue);
            } else { //其他月份灰色
                holder.setBackgroundRes(R.id.tv_item_calendar_thing, R.drawable.shape_circle_item_halfred);
                holder.setBackgroundRes(R.id.tv_item_calendar_over, R.drawable.shape_circle_item_halfblue);
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    holder.setTextColor(R.id.tv_item_calendar_time, Color.parseColor("#44FF0000"));
                } else {
                    holder.setTextColor(R.id.tv_item_calendar_time, Color.parseColor("#44222222"));
                }
            }
            //当前日期
            if (isNow(date)) {
                holder.setBackgroundRes(R.id.item_calendar_root, R.drawable.today_shape);
            }
            //逾期
            if (clendarInfo.isOver) {
                holder.setVisible(R.id.tv_item_calendar_over, true);
            } else {
                holder.setVisible(R.id.tv_item_calendar_over, false);
            }
            //到期
            if (clendarInfo.isEnd) {
                holder.setVisible(R.id.tv_item_calendar_thing, true);
                holder.setText(R.id.tv_item_calendar_thing, clendarInfo.numHouse + "间");
            } else {
                holder.setVisible(R.id.tv_item_calendar_thing, false);
            }
        }
    }

    public String formatDate(Date date) {
        return sf.format(date);
    }

    private SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    private Date now = new Date();

    /**
     * 判断时间是不是今天
     */
    private boolean isNow(Date date) {
        String nowDay = sf.format(now);
        String day = sf.format(date);
        return day.equals(nowDay);
    }

    /**
     * 外部回调接口
     */
    public interface CallBackListener {

        void callBack(Object object, int position);
    }

    private CallBackListener callBackListener;

    /**
     * 点击回调
     *
     * @param callBackListener
     */
    public void setCallBackListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }
}


