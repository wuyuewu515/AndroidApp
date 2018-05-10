package com.vcredit.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 作者: 伍跃武
 * 时间： 2018/3/24
 * 描述：不可滑动的ExpandableListView
 */

public class FullyExpandaleListView extends ExpandableListView {
    public FullyExpandaleListView(Context context) {
        this(context, null);
    }

    public FullyExpandaleListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullyExpandaleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FullyExpandaleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        //将重新计算的高度传递回去
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
