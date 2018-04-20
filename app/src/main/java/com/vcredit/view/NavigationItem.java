package com.vcredit.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.vcredit.app.R;
import com.vcredit.utils.CommonUtils;

/**
 * Created by shibenli on 2016/8/1.
 */
public class NavigationItem extends AbsView {
    /**
     * 图标
     */
    protected ImageView navigationImage;

    /**
     * 文字
     */
    protected TextView navigationText;

    /**
     * 新消息数
     */
    protected TextView navigationCount;

    /**
     * 新消息数
     */
    protected TextView navigationDot;

    int numSize = 0;


    public NavigationItem(Context context) {
        super(context);
    }

    public NavigationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected
    @LayoutRes
    int getLayout() {
        return R.layout.layout_navigation_item;
    }

    protected void initView(Context context, AttributeSet attrs) {
        ImageView navigationImage = this.navigationImage = (ImageView) findViewById(R.id.navigation_item_image);
        TextView navigationText = this.navigationText = (TextView) findViewById(R.id.navigation_item_text);
        TextView navigationCount = this.navigationCount = (TextView) findViewById(R.id.navigation_item_count);
        TextView navigationDot = this.navigationDot = (TextView) findViewById(R.id.navigation_item_dot);

        numSize = CommonUtils.Dp2Px(context, 15);

        setNewCount(0);

        if (attrs != null) {
            TypedArray taCustom = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem);
            Drawable image = taCustom.getDrawable(R.styleable.NavigationItem_navigation_image);
            String text = taCustom.getString(R.styleable.NavigationItem_navigation_text);

            navigationImage.setImageDrawable(image);
            navigationText.setText(text);

            taCustom.recycle();
        }
    }

    /**
     * count为0时不显示,小于零时显示显示小红点，大于0时显示数字
     *
     * @param count
     * @return
     */
    public NavigationItem setNewCount(int count) {
        String disTemp = getNewByCount(count);
        navigationCount.setVisibility(count > 0 ? VISIBLE : GONE);
        navigationCount.setText(disTemp);

        navigationDot.setVisibility(count < 0 ? VISIBLE : GONE);

        return this;
    }

    @NonNull
    public static String getNewByCount(int count) {
        String disTemp;
        if (count > 9) {
            disTemp = "9+";
        } else {
            disTemp = count > 0 ? String.valueOf(count) : "";
        }
        return disTemp;
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
    }
}
