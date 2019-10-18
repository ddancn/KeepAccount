package com.ucar.training.lib.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.ucar.training.lib.R;

/**
 * @author ddan.zhuang
 * @date 2019/9/18
 */
public class ViewUtil {
    private ViewUtil(){}

    public static View getEmptyView(Context context, String text){
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, null ,false);
        TextView emptyView = view.findViewById(R.id.text);
        emptyView.setText(text);
        return view;
    }

    public static View getEmptyView(Context context, @DrawableRes int res){
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, null ,false);
        ImageView imageView = view.findViewById(R.id.pic);
        imageView.setImageResource(res);
        return view;
    }

    public static View getEmptyView(Context context, String text, @DrawableRes int res){
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, null ,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(text);
        ImageView imageView = view.findViewById(R.id.pic);
        imageView.setImageResource(res);
        return view;
    }

    public static View getEmptyView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.view_empty, null ,false);
    }

    public static View getEmptyText(Context context){
        return LayoutInflater.from(context).inflate(R.layout.view_empty_text, null ,false);
    }

    public static View getEmptyText(Context context, String text){
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty_text, null ,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(text);
        return view;
    }

    public static View getFooterView(Context context, String text){
        View view = LayoutInflater.from(context).inflate(R.layout.view_bottom, null ,false);
        TextView emptyView = view.findViewById(R.id.text);
        emptyView.setText(text);
        return view;
    }

    public static View getFooterView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.view_bottom, null ,false);
    }
}
