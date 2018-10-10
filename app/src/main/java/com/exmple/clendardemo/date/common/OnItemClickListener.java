package com.exmple.clendardemo.date.common;

import android.view.View;
import android.view.ViewGroup;

/**
 * 作者: 伍跃武
 * 时间： 2018/3/13
 * 描述： 通用的RecyclerView 的ItemClickListener，包含点击和长按
 */

public interface OnItemClickListener<T>
{
    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}