package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasManual extends View {
    private Bitmap g_Bitmap;
    private Canvas g_Canvas;
    private Path g_Path = new Path();
    Context context;
    private Paint g_Paint;
    //
    private int g_CenterX, g_CenterY;
    private Paint g_CanvasPaint;
    //
    private PointF startPoint, endPoint;
    public boolean isDrawing;

    private ArrayList<Linia> Linies = new ArrayList<Linia>();
    class Linia{
        public PointF Inici, Final;
        public void Linia(){
        }
    }

    public CanvasManual(Context p_Context, AttributeSet p_Attrs){
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
        /*
        startNew();
        g_Canvas.drawRect(g_CenterX - (p_Amplada / 2),
                g_CenterY - (p_Alsada / 2),
                g_CenterX + (p_Amplada / 2), g_CenterY + (p_Alsada / 2), g_Paint);
        */
        g_Path.reset();
        g_Path.addRect(g_CenterX - (p_Amplada / 2),
                g_CenterY - (p_Alsada / 2),
                g_CenterX + (p_Amplada / 2), g_CenterY + (p_Alsada / 2), Path.Direction.CW);
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
        //canvas.drawBitmap(g_Bitmap, 0, 0, g_Paint);
        // Aixó no es necessari en el simple
        //canvas.drawPath(g_Path, g_Paint);
        if(isDrawing){
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, g_Paint);
            canvas.drawCircle(endPoint.x, endPoint.y, 25, g_Paint);
            // Pinto la resta de linies
            for (int i=0; i<Linies.size(); i++){
                Linia Aux = Linies.get(i);
                canvas.drawLine(Aux.Inici.x, Aux.Inici.y, Aux.Final.x, Aux.Final.y, g_Paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
        float l_touchX = event.getX();
        float l_touchY = event.getY();
        boolean l_fin = false;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startPoint = new PointF(event.getX(), event.getY());
                endPoint = new PointF();
                isDrawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDrawing){
                    endPoint.x = event.getX();
                    endPoint.y = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isDrawing){
                    endPoint.x = event.getX();
                    endPoint.y = event.getY();
                    isDrawing = false;
                    // Nova linia
                    Linia Lin = new Linia();
                    Lin.Inici = startPoint;
                    Lin.Final = endPoint;
                    Linies.add(Lin);
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
