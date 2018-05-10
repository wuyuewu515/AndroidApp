package com.vcredit.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vcredit.app.R;
import com.vcredit.app.entities.ClendarInfo;
import com.vcredit.global.CommonAdapter;
import com.vcredit.global.OnItemClickListener;
import com.vcredit.global.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者: 伍跃武
 * 时间： 2018/5/9
 * 描述：自定义的日历控件
 */

public class ClendarView extends FrameLayout implements View.OnClickListener, OnItemClickListener {

    private ImageView btnPre; //上个月
    private ImageView btnNext;//下个月
    private TextView tvDateTitle;//当前月份
    private RecyclerView recyclerView;

    private Context mContext;
    private Calendar mCalendar = Calendar.getInstance();
    private List<ClendarInfo> dateList = new ArrayList<>();//每个数据单元格中数据
    private Map<String, String> dataMap = new HashMap<>();//具体哪天的数据
    private DateAdapter adapter;

    public ClendarView(Context context) {
        this(context, null);
    }

    public ClendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        initView(context, view, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(getLayout(), this, true);
        initView(context, view, attrs);
    }

    @LayoutRes
    private int getLayout() {
        return R.layout.calendarview_layout;
    }

    @SuppressLint("WrongViewCast")
    private void initView(Context context, View view, AttributeSet attrs) {

        tvDateTitle = (TextView) view.findViewById(R.id.tv_calendar_date);
        btnNext = (ImageView) view.findViewById(R.id.btn_calendar_next);
        btnPre = (ImageView) view.findViewById(R.id.btn_calendar_pre);
        recyclerView = (RecyclerView) view.findViewById(R.id.recy_calendar);


        GridLayoutManager manager = new GridLayoutManager(context, 7);
        recyclerView.setLayoutManager(manager);

        btnPre.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        initData(context);
    }

    private void initData(Context context) {
        dateList.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM");
        //年月
        String dateTitle = sdf.format(mCalendar.getTime());
        tvDateTitle.setText(dateTitle);

        //表格中的数据
        Calendar calendar = (Calendar) mCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1); //设置时间到当前月份的第一天

        //1---代表周日  2---代表周一
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK);  //获取日期的偏移量
//        int firstDay2 = calendar.get(Calendar.DAY_OF_MONTH);
//        int firstDay3 = calendar.get(Calendar.DAY_OF_YEAR);
//        Log.i("TAG", "DAY_OF_WEEK=" + firstDay);
//        Log.i("TAG", "DAY_OF_MONTH=" + firstDay2);
//        Log.i("TAG", "DAY_OF_YEAR=" + firstDay3);
//        Log.i("TAG", "时间是=" + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

        int preDays = firstDay - 1;
        //仅仅美观操作，下面代码可加可不加 效果参见pc系统的日历月份调至  2018-4
        preDays = preDays == 0 ? 7 : preDays; //为了保证第一行一定是 上个月+这个月(可能没有) 的数据 ，最后一行一定是 这个月（可能没有）+下个月 的数据

        calendar.add(Calendar.DAY_OF_MONTH, -preDays); //将偏移量移至上个月，把上个月的几天添加到本月的日历中

        int maxDays = 6 * 7;
        ClendarInfo clendarInfo;
        Date time = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean needDoThing = dataMap.size() > 0;  //表示当月有数据
        for (int i = 0; i < maxDays; i++) {
            time = calendar.getTime();
            clendarInfo = new ClendarInfo();

            String key = sdf.format(new Date(time.getYear(), time.getMonth() + 1, time.getDate()));
            if (needDoThing && dataMap.containsKey(key)) { //将本月需要做的事添加到集合中,而且的当前月份
                String thing = dataMap.get(key);
                if (!TextUtils.isEmpty(thing))
                    clendarInfo.setDoThing(thing);
            }
            clendarInfo.setDate(time);
            dateList.add(clendarInfo);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //  Log.i("TAG", "dataMap=" + dataMap.toString());
        //将日历数据填充到recycleview中
        if (adapter == null) {
            adapter = new DateAdapter(context, R.layout.item_calendar_layout, dateList);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_calendar_next: { //下一月
                mCalendar.add(Calendar.MONTH, +1);  //当前月加1
                initData(mContext);
            }
            break;
            case R.id.btn_calendar_pre: { //上个月
                mCalendar.add(Calendar.MONTH, -1);//当前月减1
                initData(mContext);
            }
            break;
        }
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

        @Override
        public void convert(ViewHolder holder, ClendarInfo clendarInfo) {

            Date date = clendarInfo.getDate();
            Date nowDate = new Date();
            holder.setText(R.id.tv_item_calendar_time, String.valueOf(date.getDate()));
            String thing = clendarInfo.getDoThing();
            holder.setVisible(R.id.tv_item_calendar_thing, !TextUtils.isEmpty(thing));
            if (!TextUtils.isEmpty(thing)) {
                holder.setText(R.id.tv_item_calendar_thing, thing);
            }
            if (date.getMonth() == nowDate.getMonth()) {
                holder.setTextColor(R.id.tv_item_calendar_time, Color.BLACK);

                //注意  getDate表示返回 几号， getDay表示返回 星期几
                if (nowDate.getDate() == date.getDate() &&
                        date.getYear() == nowDate.getYear()) { //当天 ---红色
                    holder.setTextColor(R.id.tv_item_calendar_time, Color.RED);
                }

            } else { //其他月份灰色
                holder.setTextColor(R.id.tv_item_calendar_time, Color.GRAY);

            }
        }
    }

    /**
     * 外部回调接口
     */
    interface CallBackListener {

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

    /**
     * 设置当前月份需要做的事
     *
     * @param dataMap 当前月份某天需要做的事
     */
    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap.clear();
        this.dataMap.putAll(dataMap);
        initData(mContext);
    }


}


