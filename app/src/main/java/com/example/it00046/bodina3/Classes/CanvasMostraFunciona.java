package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

public class CanvasMostraFunciona extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, g_CanvasPaint;
    private Bitmap g_CanvasBitmap;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Paint g_Paint = new Paint();
    //
    private int g_CenterX;
    private int g_CenterY;

    public CanvasMostraFunciona(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        g_CanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void Dibuixa(int p_Amplada, int p_Alsada){
        startNew();
        /*
        drawCanvas.drawRect(g_CenterX - (p_Amplada / 2),
                g_CenterY - (p_Alsada / 2),
                g_CenterX + (p_Amplada / 2), g_CenterY + (p_Alsada / 2), canvasPaint);
        */
        g_Paint.setColor(paintColor);
        g_Paint.setStyle(Paint.Style.STROKE);
        g_Paint.setStrokeWidth(5);
        g_Paint.setAntiAlias(true);
        g_Paint.setStrokeJoin(Paint.Join.ROUND);
        g_Paint.setStrokeCap(Paint.Cap.ROUND);
        drawCanvas.drawRect(g_CenterX - (p_Amplada / 2),
                g_CenterY - (p_Alsada / 2),
                g_CenterX + (p_Amplada / 2), g_CenterY + (p_Alsada / 2), g_Paint);
        invalidate();
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        //if (w != 0 && h!= 0) {
            super.onSizeChanged(w, h, oldw, oldh);
            g_CanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(g_CanvasBitmap);
            //
            g_CenterX = w / 2;
            g_CenterY = h / 2;
        //}
    }

    @Override
    protected void onDraw(Canvas p_Canvas) {
        //Definim el canvas on dibuixem
        p_Canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_CanvasPaint);
        //
        p_Canvas.drawPath(drawPath, drawPaint);
    }
}
