package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class CanvasAuto extends View {
    private Bitmap g_Bitmap;
    private Canvas g_Canvas;
    private Path g_Path = new Path();
    Context context;
    private Paint g_Paint;
    //
    private int g_CenterX, g_CenterY;
    private Paint g_CanvasPaint;

    public CanvasAuto(Context p_Context, AttributeSet p_Attrs){
        super(p_Context, p_Attrs);
        context = p_Context;
        // Definim "com" pintem
        g_Paint = new Paint();
        g_Paint.setAntiAlias(true);
        g_Paint.setColor(Color.BLACK);
        g_Paint.setStyle(Paint.Style.STROKE);
        g_Paint.setStrokeJoin(Paint.Join.ROUND);
        g_Paint.setStrokeWidth(4f);
        // Definim el Canvas
        g_CanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void Dibuixa(int p_Amplada, int p_Alsada){
        startNew();
        g_Canvas.drawRect(g_CenterX - (p_Amplada / 2),
                g_CenterY - (p_Alsada / 2),
                g_CenterX + (p_Amplada / 2), g_CenterY + (p_Alsada / 2), g_Paint);
        invalidate();
    }

    public void startNew(){
        g_Canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Construim el canvas i aprofitem per determinar el punt mig
        g_Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        g_Canvas = new Canvas(g_Bitmap);
        g_CenterX = w / 2;
        g_CenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Pintem
        canvas.drawBitmap(g_Bitmap, 0, 0, g_Paint);
        // Aixó no es necessari en el simple
        //canvas.drawPath(g_Path, g_Paint);
    }
}
