package com.example.upc.course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.upc.R;
/**
 * @author Meteor
 */
public class LessonView<T extends Lesson> extends FrameLayout {

    private LinearLayout content;
    private TextView name;
    private TextView place;
    private int height_per_lesson;

    public void setHeight_per_lesson(int _height_per_lesson)
    {
        height_per_lesson=_height_per_lesson;
//        Log.e("高度2",String.valueOf(height_per_lesson));
    }
    public LessonView(Context context) {
        this(context, null);
    }

    public LessonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LessonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface LessonClickListener<T extends Lesson> {
        /**
         * 课程块点击事件
         * @param lesson 被点击的课程数据
         */
        void onClick(T lesson);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, this, false);
        addView(view);
        content = view.findViewById(R.id.item_lesson_content);
        name = view.findViewById(R.id.item_lesson_name);
        place = view.findViewById(R.id.item_lesson_place);
    }

    @SuppressLint("SetTextI18n")
    public void setLesson(final T lesson, final LessonClickListener<T> lessonClickListener) {
        content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessonClickListener != null) {
                    lessonClickListener.onClick(lesson);
                }
            }
        });
        name.setText(lesson.getName());
        place.setText("@" + lesson.getPlace());
//        Log.e("高度1",String.valueOf(height_per_lesson));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height_per_lesson * (lesson.getEnd() - lesson.getStart() + 1));
        layoutParams.topMargin = (int) ((lesson.getStart() - 1) * height_per_lesson);
        setLayoutParams(layoutParams);
    }

    public void setBgColor(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) content.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setTextColor(int color) {
        name.setTextColor(color);
    }

}
