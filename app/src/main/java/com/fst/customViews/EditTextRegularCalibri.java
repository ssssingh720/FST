package com.fst.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by HP240-22u on 22-07-2016.
 */
public class EditTextRegularCalibri extends AppCompatEditText {

    public EditTextRegularCalibri(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextRegularCalibri(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRegularCalibri(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/dimbo_regular.ttf");
            setTypeface(tf);
        }
    }

}

