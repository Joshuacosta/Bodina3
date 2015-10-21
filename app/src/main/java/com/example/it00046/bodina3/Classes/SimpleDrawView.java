package com.example.it00046.bodina3.Classes;

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
    // Path
    private Path g_drawPath;
    // Paint's
    private Paint drawPaint, drawPaintFinal, canvasPaint, drawPaintText;
    // Colors
    private int paintColor = 0xFF660000;
    // Canvas
    private Canvas drawCanvas;
    // Canvas bitmap
    private Bitmap canvasBitmap;
    // Punts de treball
    private PointF startPoint = null, endPoint = null, g_PrimerPuntDibuix = null;
    private ArrayList<PointF> Punts = new ArrayList<PointF>();
    private PointF g_AnteriorPunt = new PointF();
    // Modes de dibuix
    private enum g_Modus {recta,curva,texte};
    private g_Modus g_ModusDibuix = g_Modus.recta;


    private PointF g_AnteriorPunt_1 = new PointF();
    private PointF g_AnteriorPunt_2 = new PointF();
    private Double g_Angle = null;

    private Rect g_DetectorFi = new Rect(), g_DetectorIni = null;
    public boolean isDrawing, g_Finalitzat = false;
    static private int g_CenterX, g_CenterY;

    // Array per guardar els punts amb el que fem les linies
    private ArrayList<punt> g_PuntsPlanol = new ArrayList<punt>();
    class punt{
        public PointF Punt;
        public Double Angle;
        public Boolean Descartat = false;
        public float dx, dy;
        public void punt(){
        }
    }
    // Array per guardar les linies amb el que fem el planol
    private ArrayList<ArrayList<punt>> g_LiniesPlanol = new ArrayList<ArrayList<punt>>();
    // Array per guardar els textes del planol
    private ArrayList<texte> g_TextesPlanol = new ArrayList<texte>();
    class texte{
        public PointF Punt;
        public String Texte;
        public void texte(){
        }
    }

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    public SimpleDrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        // Definim path
        g_drawPath = new Path();
        drawPaint = new Paint();
        // Definim paint de linies
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
        // Definim paint de planol (li diem final)
        drawPaintFinal = new Paint();
        drawPaintFinal.setColor(Color.LTGRAY);
        drawPaintFinal.setStyle(Paint.Style.FILL);
        drawPaintFinal.setAntiAlias(true);
        //
        drawPaintText = new Paint();
        drawPaintText.setTextSize(35);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        g_CenterX = w / 2;
        g_CenterY = h / 2;
    }

    // La definida inicialment

    @Override
    protected void onDraw(Canvas canvas) {
        PointF l_EndPoint;
        ArrayList<punt> l_Linia = new ArrayList<>();

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
        g_drawPath.reset();
        switch (g_ModusDibuix){
            case recta:
            case curva:
                Log.d("BODINA-OnDraw-Inici", "Numero de linies " + g_LiniesPlanol.size());
                for (int j=0; j < g_LiniesPlanol.size(); j++) {
                    //drawPath.moveTo(startPoint.x, startPoint.y);
                    l_Linia = g_LiniesPlanol.get(j);
                    if (j==0) {
                        g_drawPath.moveTo(l_Linia.get(0).Punt.x, l_Linia.get(0).Punt.y);
                    }
                    Log.d("BODINA-OnDraw-Inici", l_Linia.get(0).Punt.x + "," + l_Linia.get(0).Punt.y);
                    for (int i = 1; i < l_Linia.size(); i++) {
                        punt ara = l_Linia.get(i);
                        if (l_Linia.get(i).Descartat == false) {
                            Log.d("BODINA-OnDraw-Pintem", String.valueOf(i) + " (" + l_Linia.get(i).Punt.x + "," + l_Linia.get(i).Punt.y + ")");
                            //drawPath.lineTo(l_Linia.get(i).Punt.x, l_Linia.get(i).Punt.y);
                            //
                            if (i < l_Linia.size() - 1) {
                                punt next = l_Linia.get(i + 1);
                                Log.d("BODINA-OnDraw-QUAD", String.valueOf(i));
                                g_drawPath.quadTo(ara.Punt.x, ara.Punt.y, next.Punt.x, next.Punt.y);
                            } else {
                                Log.d("BODINA-OnDraw-LINE", String.valueOf(i));
                                g_drawPath.lineTo(ara.Punt.x, ara.Punt.y);
                            }
                            //
                        } else {
                            Log.d("BODINA-OnDraw-NO Pintem", String.valueOf(i));
                        }
                    }
                }
                // Pintem el "detector" final a la darrera linia si el dibuix no esta finalitzat
                if (g_Finalitzat == false){
                    if (l_Linia.size() > 1) {
                        l_EndPoint = l_Linia.get(l_Linia.size() - 1).Punt;
                        g_DetectorFi = new Rect(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
                                Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
                        canvas.drawRect(g_DetectorFi, drawPaint);
                    }
                }
                break;

            case texte:
                if (g_AnteriorPunt_1 != null) {
                    g_DetectorFi = new Rect(Math.round(g_AnteriorPunt_1.x) - 50, Math.round(g_AnteriorPunt_1.y) - 50,
                            Math.round(g_AnteriorPunt_1.x) + 50, Math.round(g_AnteriorPunt_1.y) + 50);
                    canvas.drawRect(g_DetectorFi, drawPaint);
                    canvas.drawText("FINAL", g_AnteriorPunt_1.x, g_AnteriorPunt_1.y, drawPaintText);
                }
                break;
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
                switch (g_ModusDibuix) {
                    case recta:
                    case curva:
                        if (g_PrimerPuntDibuix == null){
                            g_PrimerPuntDibuix = l_ActualPoint;
                            g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        }
                        //if (startPoint == null) {
                        startPoint = l_ActualPoint;
                        // Afegim la linia que anem definint
                        g_LiniesPlanol.add(g_PuntsPlanol);
                        //}
                        // Definim el rectangle inicial de conexio si iniciem la linia
                        l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        if (l_DetectorIni.intersect(g_DetectorFi)) {
                            Log.d("BODINA-TouchDOWN", "Reinici -------------");
                            //g_AnteriorPunt = endPoint;
                            // Inici nova linia
                            // Definim el punt inicial que es el darrer anteior
                            punt l_punt = new punt();
                            l_punt.Punt = endPoint;
                            l_punt.Descartat = false;
                            l_punt.Angle = -999.0;
                            g_PuntsPlanol.add(l_punt);
                            g_AnteriorPunt = endPoint;
                        }
                        else {
                            Log.d("BODINA-TouchDOWN", "Inici ---------------");
                            // Definim el punt inicial
                            punt l_punt = new punt();
                            l_punt.Punt = startPoint;
                            l_punt.Descartat = false;
                            l_punt.Angle = -999.0;
                            g_PuntsPlanol.add(l_punt);
                            //LiniesPlanol.add(PuntsPlanol);
                            g_AnteriorPunt = startPoint;
                            //g_AnteriorPunt_1 = startPoint;
                            //g_AnteriorPunt_2 = startPoint;
                        }
                        break;

                    case texte:
                        g_AnteriorPunt_1 = l_ActualPoint;
                        if (g_DetectorIni == null) {
                            g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        }
                        l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        if (g_DetectorFi.contains(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y))) {
                            Log.d("BODINA-Touch", "Intersecta");
                            isDrawing = true;
                        }
                        else{
                            Log.d("BODINA-Touch", "NO Intersecta");
                            isDrawing = false;
                        }
                        break;
                }
                //2//startPoint = new PointF(event.getX(), event.getY());
                //1//drawPath.moveTo(touchX, touchY);
                // Primer punt del dibuix
                /*
                */
                break;
            case MotionEvent.ACTION_MOVE:
                switch (g_ModusDibuix) {
                    case recta:
                    case curva:
                        double part1 = new Float(l_ActualPoint.x-g_AnteriorPunt.x);
                        double part2 = new Float(l_ActualPoint.y-g_AnteriorPunt.y);
                        double dist = Math.sqrt( Math.pow(part1, 2) + Math.pow(part2, 2));
                        // Validem si portem massa distancia i hem de forzar un punt
                        //dist = -1; // !!!!!!!!!!!!!!!!!!! Fem prova de dibuixar linies tal qual
                        if (dist > 5){
                            punt l_punt = new punt();
                            l_punt.Punt = l_ActualPoint;
                            l_punt.Descartat = false;
                            l_punt.Angle = Globals.CalculaAngle(l_ActualPoint, g_AnteriorPunt);
                            Log.d("BODINA-Touch-Afegim", String.valueOf(l_punt.Punt.x) + ", " + String.valueOf(l_punt.Punt.y));
                            g_PuntsPlanol.add(l_punt);
                            // Validem que el punt anterior no quedi descartat per l'angle
                            Log.d("BODINA-Touch-Angle", String.valueOf(g_PuntsPlanol.size()) + " " + String.valueOf(l_punt.Angle));
                            if (g_PuntsPlanol.size() >= 3){
                                punt l_aux = g_PuntsPlanol.get(g_PuntsPlanol.size() - 2);
                                Log.d("BODINA-Touch-Recuperem", (g_PuntsPlanol.size() - 2) + " " + l_aux.Angle);
                                Double l_DiferenciaAngles = Math.abs(l_aux.Angle - l_punt.Angle);
                                Log.d("BODINA-Touch-Diferencia", String.valueOf(l_DiferenciaAngles));
                                if (l_DiferenciaAngles < 15) {
                                    punt l_Aux2 = g_PuntsPlanol.get(g_PuntsPlanol.size() - 2);
                                    l_Aux2.Descartat = true;
                                    g_PuntsPlanol.set(g_PuntsPlanol.size() - 2, l_Aux2);
                                    Log.d("BODINA-Touch-Descartem", String.valueOf(g_PuntsPlanol.size() -2));
                                }
                            }
                            g_AnteriorPunt = l_ActualPoint;
                            Log.d("BODINA-Touch-Fi", "------------");
                        }
                        else{
                            if (dist == -1){
                                if (g_PuntsPlanol.size() == 1){
                                    // Afegim punt
                                    punt l_punt = new punt();
                                    l_punt.Punt = l_ActualPoint;
                                    l_punt.Descartat = false;
                                    l_punt.Angle = 0.0;
                                    g_PuntsPlanol.add(l_punt);
                                }
                                else{
                                    // Modifiquem extrem
                                    punt l_Aux2 = g_PuntsPlanol.get(1);
                                    l_Aux2.Punt = l_ActualPoint;
                                    g_PuntsPlanol.set(1, l_Aux2);
                                }
                            }
                        }
                        break;

                    case texte:
                        if (isDrawing) {
                            g_AnteriorPunt_1 = l_ActualPoint;
                        }
                        break;
                }
                //2//endPoint = new PointF(event.getX(), event.getY());
                //1//drawPath.lineTo(touchX, touchY);
                //3// Afegiem els punts que ens "interessan"
                //var d = Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
                /*
                */
                break;
            case MotionEvent.ACTION_UP:
                switch (g_ModusDibuix) {
                    case recta:
                    case curva:
                        l_DetectorFinal = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        if (l_DetectorFinal.intersect(g_DetectorIni)){
                            Log.d("BODINA-TouchUP", "------------------------ Enganxat");
                            // Modifiquem el darrer punt perque apunti al inici
                            punt l_Aux3 = g_PuntsPlanol.get(g_PuntsPlanol.size()-1);
                            l_Aux3.Punt = g_PrimerPuntDibuix;//startPoint;
                            g_PuntsPlanol.set(g_PuntsPlanol.size() - 1, l_Aux3);
                            endPoint = g_PrimerPuntDibuix;//startPoint;
                            // Afegim la darrera linia
                            //LiniesPlanol.add(PuntsPlanol);
                            // Indiquem que ja esta esta finalitzat (perque sigui detectat en el invalidate)
                            g_Finalitzat = true;
                            g_LiniesPlanol.add(g_PuntsPlanol);
                        }
                        else{
                            endPoint = l_ActualPoint;
                            // Afegim el darrer punt (i que no es descarti)
                            punt l_Aux2 = new punt();
                            l_Aux2.Punt = endPoint;
                            l_Aux2.Descartat = false;
                            l_Aux2.Angle = -999.0;
                            g_PuntsPlanol.add(l_Aux2);
                            // Afegim la linia
                            //LiniesPlanol.add(PuntsPlanol);
                            //PuntsPlanol = new ArrayList<punt>();
                            Log.d("BODINA-TouchUP", "------------------------ NO enganxat");
                        }
                        // Netegem linia
                        g_PuntsPlanol = new ArrayList<punt>();
                        startPoint = null;
                        //drawCanvas.drawPath(drawPath, drawPaint);
                        //drawPath.reset();
                        break;

                    case texte:
                        isDrawing = false;
                        break;
                }
                /*
                */
                //g_AnteriorPunt_1 = null;
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

}
