package com.fst.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EdittextViewRalewayRegular extends AppCompatEditText {

    public EdittextViewRalewayRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EdittextViewRalewayRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdittextViewRalewayRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/dimbo_regular.ttf");
        setTypeface(tf);
    }
}
