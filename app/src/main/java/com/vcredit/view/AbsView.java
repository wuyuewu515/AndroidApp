package com.vcredit.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by shibenli on 2016/8/1.
 * 复合控件的基类
 */
public abstract class AbsView extends FrameLayout {
    public AbsView(Context context) {
        this(context, null);
    }

    public AbsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(getLayout(), this, true);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(getLayout(), this);
        initView(context, attrs);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void initView(Context context, AttributeSet attrs);
}
