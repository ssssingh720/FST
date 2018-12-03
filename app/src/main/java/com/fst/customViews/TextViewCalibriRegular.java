package com.fst.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by HP240-22u on 22-07-2016.
 */
public class TextViewCalibriRegular extends AppCompatTextView {


    public TextViewCalibriRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCalibriRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCalibriRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/dimbo_regular.ttf");
        setTypeface(tf);
    }
}
