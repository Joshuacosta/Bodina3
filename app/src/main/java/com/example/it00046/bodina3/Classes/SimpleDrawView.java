package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 07/10/2015.
 */
/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnTouchListener;

        import java.util.ArrayList;
        import java.util.List;

public class SimpleDrawView extends View implements OnTouchListener {

    private static final float STROKE_WIDTH = 5f;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public SimpleDrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        boolean first = true;
        for (Point point : points) {
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                Point point = new Point();
                point.x = event.getHistoricalX(i);
                point.y = event.getHistoricalY(i);
                points.add(point);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        points.clear();
    }

    class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
*/

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by it00046 on 26/01/2015.
 */
//public class DrawingView extends View {
public class SimpleDrawView extends RelativeLayout {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, drawPaintFinal, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    // Experiment
    private PointF startPoint = null, endPoint = null;
    private ArrayList<PointF> Punts = new ArrayList<PointF>();
    private PointF g_AnteriorPunt = new PointF();

    private PointF g_AnteriorPunt_1 = new PointF();
    private PointF g_AnteriorPunt_2 = new PointF();
    private Double g_Angle = null;

    private Rect g_DetectorFi = new Rect(), g_DetectorIni = new Rect();
    public boolean isDrawing, g_Finalitzat = false;

    private ArrayList<punt> PuntsPlanol = new ArrayList<punt>();
    class punt{
        public PointF Punt;
        public Double Angle;
        public Boolean Descartat = false;
        public void punt(){
        }
    }

    private ArrayList<ArrayList<punt>> LiniesPlanol = new ArrayList<ArrayList<punt>>();

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    public SimpleDrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = 2;
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
        //
        drawPaintFinal = new Paint();
        drawPaintFinal.setColor(Color.LTGRAY);
        drawPaintFinal.setStyle(Paint.Style.FILL);
        drawPaintFinal.setAntiAlias(true);
        /*
        drawPaintFinal.setStrokeWidth(40);
        drawPaintFinal.setStyle(Paint.Style.STROKE);
        drawPaintFinal.setStrokeJoin(Paint.Join.ROUND);
        drawPaintFinal.setStrokeCap(Paint.Cap.ROUND);
        */
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    // La definida inicialment

    @Override
    protected void onDraw(Canvas canvas) {
        PointF l_EndPoint;
        //ArrayList<punt> l_Linia = new ArrayList<>();

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
        drawPath.reset();
        if (startPoint != null) {
            //1//drawPath.moveTo(startPoint.x, startPoint.y);
            //1//drawPath.lineTo(endPoint.x, endPoint.y);
            //2//canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, drawPaint);

            Log.d("BODINA-OnDraw-Inici", "Numero de linies " + LiniesPlanol.size());
            //for (int j=1; j < LiniesPlanol.size(); j++) {
                //drawPath.moveTo(startPoint.x, startPoint.y);
                //l_Linia = LiniesPlanol.get(j);
                drawPath.moveTo(PuntsPlanol.get(0).Punt.x, PuntsPlanol.get(0).Punt.y);
                Log.d("BODINA-OnDraw-Inici", PuntsPlanol.get(0).Punt.x + "," + PuntsPlanol.get(0).Punt.y);
                for (int i = 1; i < PuntsPlanol.size(); i++) {
                    if (PuntsPlanol.get(i).Descartat == false) {
                        Log.d("BODINA-OnDraw-Pintem", String.valueOf(i) + " (" + PuntsPlanol.get(i).Punt.x + "," + PuntsPlanol.get(i).Punt.y + ")");
                        drawPath.lineTo(PuntsPlanol.get(i).Punt.x, PuntsPlanol.get(i).Punt.y);
                    } else {
                        Log.d("BODINA-OnDraw-NO Pintem", String.valueOf(i));
                    }
                }
            //}
            // Pintem el "detector" final a la darrera linia si el dibuix no esta finalitzat
            if (g_Finalitzat == false){
                //if (l_Linia.size() > 1) {
                    l_EndPoint = PuntsPlanol.get(PuntsPlanol.size() - 1).Punt;
                    g_DetectorFi = new Rect(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
                            Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
                    canvas.drawRect(g_DetectorFi, drawPaint);
                //}
            }
        }
        //1//canvas.drawPath(drawPath, drawPaint);
        if (g_Finalitzat) {
            canvas.drawPath(drawPath, drawPaintFinal);
        }
        else{
            canvas.drawPath(drawPath, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
        float l_X = event.getX();
        float l_Y = event.getY();
        Double l_Angle;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_DetectorFinal, l_DetectorIni;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //2//startPoint = new PointF(event.getX(), event.getY());
                //1//drawPath.moveTo(touchX, touchY);
                // Definim el rectangle inicial de conexio
                if (startPoint == null) {
                    startPoint = l_ActualPoint;
                    g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                            Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                }
                l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                if (l_DetectorIni.intersect(g_DetectorFi)) {
                    Log.d("BODINA-TouchDOWN", "Reinici -------------");
                    g_AnteriorPunt = endPoint;
                }
                else {
                    // Definim el punt
                    punt l_punt = new punt();
                    l_punt.Punt = startPoint;
                    l_punt.Descartat = false;
                    l_punt.Angle = -999.0;
                    PuntsPlanol.add(l_punt);
                    Log.d("BODINA-TouchDOWN", "Inici ---------------");
                    //LiniesPlanol.add(PuntsPlanol);
                    g_AnteriorPunt = startPoint;
                    //g_AnteriorPunt_1 = startPoint;
                    //g_AnteriorPunt_2 = startPoint;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //2//endPoint = new PointF(event.getX(), event.getY());
                //1//drawPath.lineTo(touchX, touchY);
                //3// Afegiem els punts que ens "interessan"
                //var d = Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
                double part1 = new Float(l_ActualPoint.x-g_AnteriorPunt.x);
                double part2 = new Float(l_ActualPoint.y-g_AnteriorPunt.y);
                double dist = Math.sqrt( Math.pow(part1, 2) + Math.pow(part2, 2));
                // Validem si portem massa distancia i hem de forzar un punt
                if (dist > 5){
                    punt l_punt = new punt();
                    l_punt.Punt = l_ActualPoint;
                    l_punt.Descartat = false;
                    l_punt.Angle = Globals.CalculaAngle(l_ActualPoint, g_AnteriorPunt);
                    Log.d("BODINA-Touch-Afegim", String.valueOf(l_punt.Punt.x) + ", " + String.valueOf(l_punt.Punt.y));
                    PuntsPlanol.add(l_punt);
                    // Validem que el punt anterior no quedi descartat per l'angle
                    Log.d("BODINA-Touch-Angle", String.valueOf(PuntsPlanol.size()) + " " + String.valueOf(l_punt.Angle));
                    if (PuntsPlanol.size() >= 3){
                        punt l_aux = PuntsPlanol.get(PuntsPlanol.size() - 2);
                        Log.d("BODINA-Touch-Recuperem", (PuntsPlanol.size() - 2) + " " + l_aux.Angle);
                        Double l_DiferenciaAngles = Math.abs(l_aux.Angle - l_punt.Angle);
                        Log.d("BODINA-Touch-Diferencia", String.valueOf(l_DiferenciaAngles));
                        if (l_DiferenciaAngles < 25) {
                            punt l_Aux2 = PuntsPlanol.get(PuntsPlanol.size() - 2);
                            l_Aux2.Descartat = true;
                            PuntsPlanol.set(PuntsPlanol.size() - 2, l_Aux2);
                            Log.d("BODINA-Touch-Descartem", String.valueOf(PuntsPlanol.size() -2));
                        }
                    }
                    Log.d("BODINA-Touch-Fi", "------------");
                    g_AnteriorPunt = l_ActualPoint;
                }
                else{
                    /*
                    // Validem que no hem fet un "canvi de rumb" important
                    if (l_ActualPoint != g_AnteriorPunt_1) {
                        l_Angle = Globals.CalculaAngle(l_ActualPoint, g_AnteriorPunt_1);
                        if (g_Angle != null) {
                            Log.d("BODINA-Touch-CanviRumb", l_Angle + " " + g_Angle);
                            Double l_DiffAngles = Math.abs(g_Angle - l_Angle);
                            if (l_DiffAngles >= 35) {
                                Log.d("BODINA-Touch-CanviRumb", l_DiffAngles + " -----------");
                                // Forcem una nova linia
                                punt l_Aux2 = new punt();
                                l_Aux2.Punt = l_ActualPoint;
                                l_Aux2.Descartat = false;
                                l_Aux2.Angle = l_Angle;
                                PuntsPlanol.add(l_Aux2);
                                Log.d("BODINA-Touch-CanviRumb", l_ActualPoint.x + ", " + l_ActualPoint.y);
                                LiniesPlanol.add(PuntsPlanol);
                                // Iniciem la seguent
                                PuntsPlanol = new ArrayList<punt>();
                                // Definim el punt
                                punt l_punt = new punt();
                                l_punt.Punt = l_ActualPoint;
                                l_punt.Descartat = false;
                                l_punt.Angle = l_Angle;
                                PuntsPlanol.add(l_punt);
                            }
                        }
                        else{
                            Log.d("BODINA-Touch-CanviRumb", " Primer NULL -----------");
                        }
                        g_Angle = l_Angle;
                    }
                    g_AnteriorPunt_1 = l_ActualPoint;
                    */
                }
                break;
            case MotionEvent.ACTION_UP:
                l_DetectorFinal = new Rect(Math.round(l_X) - 30, Math.round(l_Y) - 30,
                                           Math.round(l_X) + 30, Math.round(l_Y) + 30);
                if (l_DetectorFinal.intersect(g_DetectorIni)){
                    Log.d("BODINA-TouchUP", "------------------------ Enganxat");
                    // Modifiquem el darrer punt perque apunti al inici
                    punt l_Aux3 = PuntsPlanol.get(PuntsPlanol.size()-1);
                    l_Aux3.Punt = startPoint;
                    PuntsPlanol.set(PuntsPlanol.size() - 1, l_Aux3);
                    endPoint = startPoint;
                    // Afegim la darrera linia
                    //LiniesPlanol.add(PuntsPlanol);
                    // Indiquem que ja esta esta finalitzat (perque sigui detectat en el invalidate)
                    g_Finalitzat = true;
                }
                else{
                    endPoint = l_ActualPoint;
                    // Afegim la linia
                    //LiniesPlanol.add(PuntsPlanol);
                    //PuntsPlanol = new ArrayList<punt>();
                    Log.d("BODINA-TouchUP", "------------------------ NO enganxat");
                }
                //drawCanvas.drawPath(drawPath, drawPaint);
                //drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
    /*
    // Prova per dibuixar linies
    @Override
    protected void onDraw(Canvas canvas)
    {
        if(isDrawing)
        {
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, drawPaint);
            canvas.drawCircle(endPoint.x, endPoint.y, 25, drawPaint);
            // Pinto la resta de linies
            for (int i=0; i<Linies.size(); i++){
                Linia Aux = Linies.get(i);
                canvas.drawLine(Aux.Inici.x, Aux.Inici.y, Aux.Final.x, Aux.Final.y, drawPaint);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Validem si estem damunt d
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
    // Fi experiment
       */
    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);

    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    // Drag and drop

}
