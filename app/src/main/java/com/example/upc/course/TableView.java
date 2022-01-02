package com.example.upc.course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.upc.R;
import com.example.upc.course.course_util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Meteor
 */
public class TableView<T extends Lesson> extends LinearLayout {

    private RelativeLayout mon;
    private RelativeLayout tue;
    private RelativeLayout wed;
    private RelativeLayout thu;
    private RelativeLayout fri;
    private RelativeLayout sat;
    private RelativeLayout sun;

    private TextView mon_tv;
    private TextView tue_tv;
    private TextView wed_tv;
    private TextView thu_tv;
    private TextView fri_tv;
    private TextView sat_tv;
    private TextView sun_tv;


    private View view_line=null;
    private int countPerDay;
    private int lessonTextColor;
    private String[] flags;
    private int height_per_lesson;
    private List<T> lessons;
    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null){ return;}
        height_per_lesson=(int) context.getResources().getDimension(R.dimen.height_per_lesson);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        countPerDay = typedArray.getInteger(R.styleable.TableView_tv_count_per_day, 0);
        int arrId = typedArray.getResourceId(R.styleable.TableView_tv_resolve_flags, 0);
        if (arrId != 0) {
            flags = context.getResources().getStringArray(arrId);
        }
        int indicatorBgColor = typedArray.getColor(R.styleable.TableView_tv_indicator_bg_color, Color.TRANSPARENT);
        int indicatorTextColor = typedArray.getColor(R.styleable.TableView_tv_indicator_text_color, Color.BLACK);
        int weekBgColor = typedArray.getColor(R.styleable.TableView_tv_week_bg_color, Color.TRANSPARENT);
        int weekTextColor = typedArray.getColor(R.styleable.TableView_tv_week_text_color, Color.BLACK);
        lessonTextColor = typedArray.getColor(R.styleable.TableView_tv_lesson_text_color, Color.WHITE);
        typedArray.recycle();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (countPerDay * height_per_lesson)));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_view, this, false);
        addView(view);
        mon = view.findViewById(R.id.item_table_view_mon);
        tue = view.findViewById(R.id.item_table_view_tue);
        wed = view.findViewById(R.id.item_table_view_wed);
        thu = view.findViewById(R.id.item_table_view_thu);
        fri = view.findViewById(R.id.item_table_view_fri);
        sat = view.findViewById(R.id.item_table_view_sat);
        sun = view.findViewById(R.id.item_table_view_sun);

        mon_tv = findViewById(R.id.item_table_view_tv_mon);
        tue_tv = findViewById(R.id.item_table_view_tv_tue);
        wed_tv = findViewById(R.id.item_table_view_tv_wed);
        thu_tv = findViewById(R.id.item_table_view_tv_thu);
        fri_tv = findViewById(R.id.item_table_view_tv_fri);
        sat_tv = findViewById(R.id.item_table_view_tv_sat);
        sun_tv = findViewById(R.id.item_table_view_tv_sun);

        Calendar cal=Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int now_bgcolor = getResources().getColor(R.color.light_blue_200);
        if(w==0)
            sun.setBackgroundColor(now_bgcolor);
        else if(w==1)
            mon.setBackgroundColor(now_bgcolor);
        else if(w==2)
            tue.setBackgroundColor(now_bgcolor);
        else if(w==3)
            wed.setBackgroundColor(now_bgcolor);
        else if(w==4)
            thu.setBackgroundColor(now_bgcolor);
        else if(w==5)
            fri.setBackgroundColor(now_bgcolor);
        else if(w==6)
            sat.setBackgroundColor(now_bgcolor);
        TextView tvMon = view.findViewById(R.id.item_table_view_tv_mon);
        TextView tvTue = view.findViewById(R.id.item_table_view_tv_tue);
        TextView tvWed = view.findViewById(R.id.item_table_view_tv_wed);
        TextView tvThu = view.findViewById(R.id.item_table_view_tv_thu);
        TextView tvFri = view.findViewById(R.id.item_table_view_tv_fri);
        TextView tvSat = view.findViewById(R.id.item_table_view_tv_sat);
        TextView tvSun = view.findViewById(R.id.item_table_view_tv_sun);

        tvMon.setTextColor(weekTextColor);
        tvTue.setTextColor(weekTextColor);
        tvWed.setTextColor(weekTextColor);
        tvThu.setTextColor(weekTextColor);
        tvFri.setTextColor(weekTextColor);
        tvSat.setTextColor(weekTextColor);
        tvSun.setTextColor(weekTextColor);
        LinearLayout weekCon = view.findViewById(R.id.item_table_view_week_con);
        weekCon.setBackgroundColor(weekBgColor);
        LinearLayout indicatorCon = view.findViewById(R.id.item_table_view_indicator_con);
        indicatorCon.setBackgroundColor(indicatorBgColor);
        for (int i = 1; i <= countPerDay; i++) {
            TextView textView = new TextView(context);
            textView.setText(String.valueOf(i));
            textView.setTextColor(indicatorTextColor);
            textView.setGravity(Gravity.CENTER);
            textView.getPaint().setFakeBoldText(true);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,height_per_lesson);
            textView.setLayoutParams(layoutParams);
            indicatorCon.addView(textView);
        }
    }

    public void setLessons(List<T> _lessons) {
        lessons=_lessons;
    }

    public void setHeight_per_lesson(int _height_per_lesson)
    {
        height_per_lesson=_height_per_lesson;
        int indicatorTextColor = Color.BLACK;
        LinearLayout indicatorCon = findViewById(R.id.item_table_view_indicator_con);
        indicatorCon.removeAllViews();
        for (int i = 1; i <= countPerDay; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(String.valueOf(i));
            textView.setTextColor(indicatorTextColor);
            textView.setGravity(Gravity.CENTER);
            textView.getPaint().setFakeBoldText(true);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,height_per_lesson);
            textView.setLayoutParams(layoutParams);
            indicatorCon.addView(textView);
        }
    }

    public void setLessons(LessonView.LessonClickListener<T> lessonClickListener) {

        List<Date> weekdays=course_util.GetWeekDays();
        Calendar cal=Calendar.getInstance();
        cal.setTime(weekdays.get(0));
        sun_tv.setText("周日 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(1));
        mon_tv.setText("周一 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(2));
        tue_tv.setText("周二 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(3));
        wed_tv.setText("周三 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(4));
        thu_tv.setText("周四 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(5));
        fri_tv.setText("周五 "+cal.get(Calendar.DATE));
        cal.setTime(weekdays.get(6));
        sat_tv.setText("周六 "+cal.get(Calendar.DATE));

        mon.removeAllViews();
        tue.removeAllViews();
        wed.removeAllViews();
        thu.removeAllViews();
        fri.removeAllViews();
        sat.removeAllViews();
        sun.removeAllViews();
        if (flags == null) {
            flags = new String[]{ "sun","mon", "tue", "wed", "thur", "fri", "sat"};
        }
        for (int i = 0; i < countPerDay; i++) {
            for (T lesson : lessons) {
                if (lesson.getStart() == i) {
                    LessonView<T> lessonView = new LessonView<>(getContext());
//                    Log.e("高度",String.valueOf(height_per_lesson));
                    lessonView.setHeight_per_lesson(height_per_lesson);
                    lessonView.setTextColor(lessonTextColor);
                    lessonView.setLesson(lesson, lessonClickListener);
                    lessonView.setBgColor(lesson.getColor());
                    if (lesson.getWeekday().equals(flags[1])) {
                        mon.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[2])) {
                        tue.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[3])) {
                        wed.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[4])) {
                        thu.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[5])) {
                        fri.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[6])) {
                        sat.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[0])) {
                        sun.addView(lessonView);
                    }
                }
            }
        }
    }
}
