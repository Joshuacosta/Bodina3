package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.example.it00046.bodina3.R;

import java.util.ArrayList;

public class CanvasManual extends View {
    static private Bitmap g_Bitmap;
    static private Canvas g_Canvas;
    static private Path g_Path = new Path();
    Context context;
    static private Paint g_Paint;
    static private Rect g_DetectorIni = new Rect();
    static private Rect g_DetectorFi = new Rect();
    static private Rect g_DetectorPrimer = new Rect();
    static private PointF g_PrimerPunt;
    static private boolean g_PuntInicial = true;
    static private boolean g_PlanolTancat = false;
    //
    static private int g_CenterX, g_CenterY;
    static private Paint g_CanvasPaint;
    //
    static private PointF startPoint = null , endPoint = null;
    static public boolean isDrawing = false;
    static public boolean g_HiHaDibuix = false;
    //
    static private TextView g_Metres;

    static private ArrayList<Linia> Linies = new ArrayList<Linia>();
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
        //
        Linies = new ArrayList<Linia>();
        g_HiHaDibuix = false;
        isDrawing = false;
    }

    static public void DefinimMetres(TextView p_Metres){
        g_Metres = p_Metres;
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
        //if(isDrawing || g_HiHaDibuix){
        if(isDrawing){
            if (g_PlanolTancat){
                isDrawing = false;
            }
            if (g_PuntInicial){
                g_PuntInicial = false;
                g_DetectorPrimer = new Rect(Math.round(startPoint.x) - 30, Math.round(startPoint.y) - 30, Math.round(startPoint.x) + 30, Math.round(startPoint.y) + 30);
                g_PrimerPunt = startPoint;
            }
            // Cal pintar-ho?
            //canvas.drawRect(g_DetectorPrimer, g_Paint);
            // Pintem linia que estem dibuixant
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, g_Paint);
            //canvas.drawCircle(endPoint.x, endPoint.y, 25, g_Paint);
            // int left, int top, int right, int bottom)
            g_DetectorFi = new Rect(Math.round(endPoint.x) - 50, Math.round(endPoint.y) - 50, Math.round(endPoint.x) + 50, Math.round(endPoint.y) + 50);
            canvas.drawRect(g_DetectorFi, g_Paint);
            // Pinto la resta de linies
            for (int i=0; i<Linies.size(); i++){
                Linia Aux = Linies.get(i);
                canvas.drawLine(Aux.Inici.x, Aux.Inici.y, Aux.Final.x, Aux.Final.y, g_Paint);
            }
        }
        else{
            if (g_HiHaDibuix == false){
                //canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                g_Bitmap.eraseColor(Color.TRANSPARENT);
                canvas.drawBitmap(g_Bitmap, 0, 0, g_Paint);
            }
        }
    }

    private Path drawCurve(Canvas canvas, Paint paint, PointF mPointa, PointF mPointb) {

        Path myPath = new Path();
        myPath.moveTo(63*g_CenterX/64, g_CenterY/10);
        myPath.quadTo(mPointa.x, mPointa.y, mPointb.x, mPointb.y);
        return myPath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect l_DetectorFinal = new Rect();
        //detect user touch
        float l_touchX = event.getX();
        float l_touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startPoint = new PointF(event.getX(), event.getY());
                // Definim el rectangle inicial de conexio
                g_DetectorIni = new Rect(Math.round(startPoint.x) - 30, Math.round(startPoint.y) - 30, Math.round(startPoint.x) + 30, Math.round(startPoint.y) + 30);
                if (g_DetectorIni.intersect(g_DetectorFi)){
                    startPoint = endPoint;
                }
                endPoint = new PointF();
                isDrawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDrawing){
                    endPoint.x = event.getX();
                    endPoint.y = event.getY();
                    invalidate();
                    double dist = Math.sqrt(Math.pow(startPoint.x - endPoint.x, 2) + Math.pow(startPoint.y - endPoint.y, 2));
                    g_Metres.setText(String.valueOf(Math.round(dist)) + " " + Globals.g_Native.getString(R.string.meters));
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isDrawing){
                    // Definim el rectangle final de conexio
                    l_DetectorFinal = new Rect(Math.round(event.getX()) - 30, Math.round(event.getY()) - 30, Math.round(event.getX()) + 30, Math.round(event.getY()) + 30);
                    if (l_DetectorFinal.intersect(g_DetectorPrimer)){
                        endPoint = g_PrimerPunt;
                        g_PlanolTancat = true;
                    }
                    else{
                        endPoint.x = event.getX();
                        endPoint.y = event.getY();
                    }
                    // Nova linia
                    Linia Lin = new Linia();
                    Lin.Inici = startPoint;
                    Lin.Final = endPoint;
                    Linies.add(Lin);
                    if (g_PlanolTancat){
                        invalidate();
                    }
                    g_HiHaDibuix = true;
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
