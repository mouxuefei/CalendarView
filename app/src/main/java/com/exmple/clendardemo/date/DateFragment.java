package com.exmple.clendardemo.date;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.exmple.clendardemo.OverAndEndBean;
import com.exmple.clendardemo.R;

import java.util.HashMap;

/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: DateFragment.java
 * @author: villa_mou
 * @date: 10-14:40
 * @desc
 */
public class DateFragment extends Fragment implements ClendarView.CallBackListener {
    private static final String KEY_POSITION = "key_date";
    ClendarView calendarView;
    private boolean viewCreated = false;
    private boolean hasLoadData = false;

    public static Fragment getInstance(int position) {
        Fragment instance = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_date, null);
        calendarView = inflate.findViewById(R.id.calendarView);
        calendarView.setCallBackListener(this);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
        int position = getArguments().getInt(KEY_POSITION);
//        Log.e("villa", position + "");
        calendarView.setCurrentPosition(position);
        loadData(true);
    }

    @Override
    public void callBack(Object object, int position) {
        ClendarInfo clendarInfo = (ClendarInfo) object;
        Toast.makeText(getContext(),calendarView.formatDate(clendarInfo.date),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadData(isVisibleToUser);
    }

    private void loadData(boolean isVisibleToUser) {
        if (isVisibleToUser && viewCreated && !hasLoadData) {
            int position = getArguments().getInt(KEY_POSITION);
            Log.e("villa", position + "");
            HashMap<String, OverAndEndBean> stringOverAndEndBeanHashMap = new HashMap<>();
            stringOverAndEndBeanHashMap.put("2018" + (10 + position) + "01", new OverAndEndBean(0, true, false));
            stringOverAndEndBeanHashMap.put("2018" + (10 + position) + "11", new OverAndEndBean(10, true, true));
            stringOverAndEndBeanHashMap.put("2018" + (10 + position) + "22", new OverAndEndBean(120, false, true));
            stringOverAndEndBeanHashMap.put("2018" + (10 + position) + "28", new OverAndEndBean(0, true, false));
            stringOverAndEndBeanHashMap.put("2018" + (10 + position) + "30", new OverAndEndBean(0, true, false));
            calendarView.setOverAndEndDateBean(stringOverAndEndBeanHashMap);
        }
    }


}
