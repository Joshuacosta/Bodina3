package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by it00046 on 29/02/2016.
 */
public class TaulaEdicio extends RelativeLayout {

    public TaulaEdicio(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
