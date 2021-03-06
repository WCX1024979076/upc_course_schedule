package com.example.upc.course;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.upc.R;
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
    private View view_line=null;
    private int countPerDay;
    private int lessonTextColor;
    private String[] flags;

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
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (countPerDay * context.getResources().getDimension(R.dimen.height_per_lesson))));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_table_view, this, false);
        addView(view);
        mon = view.findViewById(R.id.item_table_view_mon);
        tue = view.findViewById(R.id.item_table_view_tue);
        wed = view.findViewById(R.id.item_table_view_wed);
        thu = view.findViewById(R.id.item_table_view_thu);
        fri = view.findViewById(R.id.item_table_view_fri);
        sat = view.findViewById(R.id.item_table_view_sat);
        sun = view.findViewById(R.id.item_table_view_sun);
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
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.height_per_lesson));
            textView.setLayoutParams(layoutParams);
            indicatorCon.addView(textView);
        }
    }

    public void setLessons(List<T> lessons) {
        setLessons(lessons, null);
    }


    public void setLessons(List<T> lessons, LessonView.LessonClickListener<T> lessonClickListener) {
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
                    lessonView.setTextColor(lessonTextColor);
                    lessonView.setLesson(lesson, lessonClickListener);
                    lessonView.setBgColor(lesson.getColor());
                    if (lesson.getWeekday().equals(flags[0])) {
                        mon.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[1])) {
                        tue.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[2])) {
                        wed.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[3])) {
                        thu.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[4])) {
                        fri.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[5])) {
                        sat.addView(lessonView);
                    }
                    if (lesson.getWeekday().equals(flags[6])) {
                        sun.addView(lessonView);
                    }
                }
            }
        }
    }
}
