package com.example.it00046.bodina3.Classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.R;

import java.util.ArrayList;

public class SimpleDrawView extends RelativeLayout {


    ///////////////////////////////////////////////////
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private float scaleFactorAnterior = 1;
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    ///////////////////////////////////////////////////

    private Context Jo = this.getContext();
    public Context g_Pare;
    private Path g_drawPath;
    private Paint g_PaintNormal, g_PaintFinal, g_PaintCanvas, g_PaintText, g_PaintTextEsborrantse;
    private Paint g_PaintQuadricula;
    // Controlador de events
    private GestureDetector g_GestureDetector;
    // Colors
    private int g_PaintColor = 0xFF660000;
    // Canvas i Bitmap
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    // Punts i texte de treball
    private PointF g_PuntInicialLinia = null, g_PuntFinalAnterior = null, g_PrimerPuntDibuix = null;
    private PointF g_AnteriorPuntLinia = null, g_PuntActual = null;
    private texte g_TexteSeleccionat = null;
    // Modes i variables de treball de dibuix
    public enum g_Modus {recta,curva,texte,ma};
    public g_Modus g_ModusDibuix = g_Modus.recta;
    public g_Modus g_ModusDibuixAnterior = null;
    static public String g_NouTexte = null;
    static public int g_RatioDistancia = 20; // Es la finura de la curva
    static public int g_RatioAngle =15; // Idem, podrien ser parametritzables?
    //
    public ImageButton g_IMB_Esborrar;
    //
    private Rect g_Punter = null, g_DetectorIni = null;
    public boolean g_Finalitzat = false, g_Dibuixant = false, g_Quadricula = false, g_IniciDibuix = false;
    static private int g_CenterX, g_CenterY;
    private int g_Escala = 20;
    // Array per guardar els punts amb el que fem les linies i/o curves
    private ArrayList<punt> g_LiniaPunts = new ArrayList<punt>();
    class punt {
        public PointF Punt;
        public Double Angle;
        public Boolean Descartat = false;
        public void punt(){
        }
    }
    // Array per guardar les linies amb el que fem el planol
    private ArrayList<ArrayList<punt>> g_LiniesPlanol = new ArrayList<ArrayList<punt>>();

    // Array per guardar els textes del planol
    private ArrayList<texte> g_TextesPlanol = new ArrayList<texte>();
    class texte{
        public PointF Punt;
        public Boolean Esborrat;
        public Boolean Esborrantse;
        public String Texte;
        public int Id;
        public Rect Detector;
        public void texte(){
        }
    }

    public SimpleDrawView(Context p_Context, AttributeSet p_Attrs){
        super(p_Context, p_Attrs);
        setupDrawing();
        // creating new gesture detector
        g_GestureDetector = new GestureDetector(p_Context, new GestureListener());
        gestureScale = new ScaleGestureDetector(p_Context, new ScaleListener());
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            Log.d("BODINA-Scale", "Escala canvia " + scaleFactor);
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

    private void setupDrawing(){
        // Definim path de dibuix
        g_drawPath = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        // Definim paint de linies
        g_PaintNormal = new Paint();
        g_PaintNormal.setColor(g_PaintColor);
        g_PaintNormal.setAntiAlias(true);
        g_PaintNormal.setStrokeWidth(5);
        g_PaintNormal.setStyle(Paint.Style.STROKE);
        g_PaintNormal.setStrokeJoin(Paint.Join.ROUND);
        g_PaintNormal.setStrokeCap(Paint.Cap.ROUND);
        // Definim paint de quadricula
        g_PaintQuadricula = new Paint();
        g_PaintQuadricula.setColor(Color.BLACK);
        g_PaintQuadricula.setAlpha(100);
        g_PaintQuadricula.setStrokeWidth(1);
        g_PaintQuadricula.setStyle(Paint.Style.STROKE);
        // Definim paint de planol "terminat"
        g_PaintFinal = new Paint();
        g_PaintFinal.setColor(Color.LTGRAY);
        g_PaintFinal.setAlpha(120);
        g_PaintFinal.setStyle(Paint.Style.FILL);
        g_PaintFinal.setAntiAlias(true);
        // Definim el paint de texte
        g_PaintText = new Paint();
        g_PaintText.setTextSize(35);
        // Definim el paint de texte esborrante
        g_PaintTextEsborrantse = new Paint();
        g_PaintTextEsborrantse.setColor(Color.RED);
        g_PaintTextEsborrantse.setTextSize(35);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        g_CanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        g_DrawCanvas = new Canvas(g_CanvasBitmap);
        g_CenterX = w / 2;
        g_CenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        PointF l_EndPoint = null;
        ArrayList<punt> l_Linia = new ArrayList<>();
        texte l_Texte = new texte();
        punt l_Actual, l_Seguent;
        Path l_Quadricula = new Path();

        canvas.save();
        Log.d("BODINA-Draw", "-----> Escalem " + scaleFactor);
        canvas.translate(mPosX, mPosY);
        canvas.scale(scaleFactor, scaleFactor);

        //draw view
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        //Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
        // Pintem quadricula si es activa
        if (g_Quadricula){
            // Ja volarem l'escala...
            DisplayMetrics displayMetrics = g_Pare.getResources().getDisplayMetrics();

            float l_dpHeight = displayMetrics.heightPixels;
            float l_dpWidth = displayMetrics.widthPixels;
            int l_NumLiniesVerticals = Math.round(l_dpHeight)/g_Escala;
            int l_NumLiniesHoritzontals = Math.round(l_dpWidth)/g_Escala;

            for (int v=1; v <  l_NumLiniesVerticals; v++) {
                l_Quadricula.moveTo(0, g_Escala*v);
                l_Quadricula.lineTo(l_dpWidth, g_Escala*v);
            }

            for (int v=1; v <  l_NumLiniesHoritzontals; v++) {
                l_Quadricula.moveTo(g_Escala*v, 0);
                l_Quadricula.lineTo(g_Escala*v, l_dpHeight);
            }

        }
        canvas.drawPath(l_Quadricula, g_PaintQuadricula);
        //
        g_drawPath.reset();
        // Pintem rectes i curves
        Log.d("BODINA-OnDraw-Inici", "Numero de linies " + g_LiniesPlanol.size());
        for (int j=0; j < g_LiniesPlanol.size(); j++) {
            // Agafem linia
            l_Linia = g_LiniesPlanol.get(j);
            // Si estem a la primera linia ens posicionem "al principi" amb un move
            if (j==0) {
                g_drawPath.moveTo(l_Linia.get(0).Punt.x, l_Linia.get(0).Punt.y);
            }
            Log.d("BODINA-OnDraw-Inici", l_Linia.get(0).Punt.x + "," + l_Linia.get(0).Punt.y);
            // Anem dibuixant els punts de la linia
            for (int i = 1; i < l_Linia.size(); i++) {
                l_Actual = l_Linia.get(i);
                if (l_Linia.get(i).Descartat == false) {
                    Log.d("BODINA-OnDraw-Pintem", String.valueOf(i) + " (" + l_Linia.get(i).Punt.x + "," + l_Linia.get(i).Punt.y + ")");
                    // Anem pintant els punts on en funcio de l'eine tindrem "mes o menys" i aixó fara que fem l'efecte
                    // de la curva
                    if (i < l_Linia.size() - 1) {
                        l_Seguent = l_Linia.get(i + 1);
                        Log.d("BODINA-OnDraw-QUAD", String.valueOf(i));
                        g_drawPath.quadTo(l_Actual.Punt.x, l_Actual.Punt.y, l_Seguent.Punt.x, l_Seguent.Punt.y);
                    }
                    else {
                        Log.d("BODINA-OnDraw-LINE", String.valueOf(i));
                        g_drawPath.lineTo(l_Actual.Punt.x, l_Actual.Punt.y);
                    }
                    l_EndPoint = l_Linia.get(i).Punt;
                }
                else {
                    Log.d("BODINA-OnDraw-NO Pintem", String.valueOf(i));
                }
            }
            // Pintem la longitut en el punt mig (agafem primer punt i darrer de la linia)
            canvas.drawText(this.EscalaDistancia(CalculaDistancia(l_Linia.get(0).Punt, l_EndPoint)),
                        (l_EndPoint.x + l_Linia.get(0).Punt.x) / 2, ((l_EndPoint.y + l_Linia.get(0).Punt.y) / 2) - 20, g_PaintText);
        }
        // l_Linia contindra la darrera linia del dibuix (si hi ha, claar)
        if (g_Finalitzat == false){
            if (l_Linia.size() > 1) {
                g_Punter = new Rect(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
                                    Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
                //
                // Pintem un cercle on es el detector (crec que queda mes elegant...)
                canvas.drawCircle(Math.round(l_EndPoint.x), Math.round(l_EndPoint.y), 25, g_PaintNormal);
            }
            // Pintem el planol
            canvas.drawPath(g_drawPath, g_PaintNormal);
        }
        else{
            // Pintem el planol terminat
            canvas.drawPath(g_drawPath, g_PaintFinal);
        }
        // Pintem textes
        Log.d("BODINA-Draw", "-----> Textes " + g_TextesPlanol.size());
        for (int k=0; k < g_TextesPlanol.size(); k++) {
            l_Texte = g_TextesPlanol.get(k);
            if (l_Texte.Esborrat == false) {
                if (l_Texte.Esborrantse == false) {
                    canvas.drawText(l_Texte.Texte, l_Texte.Punt.x, l_Texte.Punt.y, g_PaintText);
                }
                else{
                    canvas.drawText(l_Texte.Texte, l_Texte.Punt.x, l_Texte.Punt.y, g_PaintTextEsborrantse);
                }
            }
        }
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent p_Event) {
        float l_X = p_Event.getX();
        float l_Y = p_Event.getY();
        texte l_Texte;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_Detector, l_Esborrar;
        punt l_Punt = new punt(), l_Aux = new punt(), l_Aux2 = new punt();
        double l_Distancia;

        // Validem primer si hi han "gestos": doble tap
        g_GestureDetector.onTouchEvent(p_Event);
        // Validem escalat si
        gestureScale.onTouchEvent(p_Event);
        if (!gestureScale.isInProgress()){
            Log.d("BODINA-Touch", "-----> Continuem");
            // Continuem
            Log.d("BODINA-Touch", "-----> Continuem1");
            g_PuntActual = l_ActualPoint;
            Log.d("BODINA-Touch", "-----> Continuem2");
            final int action = p_Event.getAction();
            Log.d("BODINA-Touch", "-----> Continuem3");
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    switch (g_ModusDibuix) {
                        case recta:
                        case curva:
                            Log.d("BODINA-Draw", "-----> Inici recta/curva");
                            // Si es el primer punt lo que fem es definir el detector inicial per poder determinar
                            // quan tanquem el dibuix
                            if (g_PrimerPuntDibuix == null) {
                                Log.d("BODINA-Draw", "-----> Definim detector");
                                g_PrimerPuntDibuix = l_ActualPoint;
                                g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);

                            }
                            g_PuntInicialLinia = l_ActualPoint;
                            //
                            // ??????????????????????????
                            //
                            // Afegim la linia que anem definint si no es la primera, que tingui algo.
                            Log.d("BODINA-Draw", "-----> g_LiniaPunts.size(): " + g_LiniaPunts.size());
                            // Validem si tenim punter per seguir el planol
                            if (g_Punter != null) {
                                // Definim el rectangle inicial de conexio per validar que continuem, sino, no fem res
                                l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                if (l_Detector.intersect(g_Punter)) {
                                    // Tenim linia
                                    g_LiniesPlanol.add(g_LiniaPunts);
                                    Log.d("BODINA-TouchDOWN", "Reinici -------------");
                                    // Inici nova linia: Definim el punt inicial que es el darrer anteior
                                    // (es guardat a g_PuntFinalAnterior)
                                    l_Punt = new punt();
                                    l_Punt.Punt = g_PuntFinalAnterior;
                                    l_Punt.Descartat = false;
                                    l_Punt.Angle = -999.0;
                                    g_LiniaPunts.add(l_Punt);
                                    // Apuntem punt anterior de la linia que fem ara
                                    g_AnteriorPuntLinia = g_PuntFinalAnterior;
                                    // Podem dibuixar
                                    g_IniciDibuix = true;
                                }
                                else{
                                    // No seguim amb el punter
                                    Log.d("BODINA-TouchDOWN", "No continuem ---------------");
                                    // No podem dibuixar
                                    g_IniciDibuix = false;
                                }
                            }
                            else {
                                g_LiniesPlanol.add(g_LiniaPunts);
                                Log.d("BODINA-TouchDOWN", "Inici ---------------");
                                // Definim el punt inicial de la linia
                                l_Punt = new punt();
                                l_Punt.Punt = g_PuntInicialLinia;
                                l_Punt.Descartat = false;
                                l_Punt.Angle = -999.0;
                                g_LiniaPunts.add(l_Punt);
                                // El anterior punt es el primer punt
                                g_AnteriorPuntLinia = g_PuntInicialLinia;
                                // Podem dibuixar
                                g_IniciDibuix = true;
                            }
                            // ??????? Es necesari
                            //invalidate();

                            mActivePointerId = p_Event.getPointerId(0);

                            break;

                        case texte:
                            Log.d("BODINA-Draw", "-----> Inici texte");
                            // Validem si hem tocat un texte
                            l_Detector = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                                  Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                            l_Texte = MarquemTexte(l_Detector);
                            if (l_Texte != null) {
                                Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                                g_TexteSeleccionat = l_Texte;
                            } else {
                                // Afegim un texte al planol i mostrem la finestra de modificació de texte
                                g_TexteSeleccionat = null;
                                FinestraTexte(Globals.g_Native.getResources().getString(R.string.Text));
                            }
                            break;
                    }
                    // Per si fem pitch
                    // Remember where we started
                    mLastTouchX = l_X;
                    mLastTouchY = l_Y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    switch (g_ModusDibuix) {
                        case curva:
                            if (g_IniciDibuix) {
                                // Calculem distancia
                                //l_Part1 = new Float(l_ActualPoint.x-g_AnteriorPuntLinia.x);
                                //l_Part2 = new Float(l_ActualPoint.y-g_AnteriorPuntLinia.y);
                                //l_Dist = Math.sqrt( Math.pow(l_Part1, 2) + Math.pow(l_Part2, 2));
                                l_Distancia = CalculaDistancia(l_ActualPoint, g_AnteriorPuntLinia);
                                // Validem si portem massa distancia i hem de forzar un punt
                                if (l_Distancia > g_RatioDistancia) {
                                    // Estem dibuixant curves (en rectes no superem el ratio)
                                    l_Punt = new punt();
                                    l_Punt.Punt = l_ActualPoint;
                                    l_Punt.Descartat = false;
                                    l_Punt.Angle = Globals.CalculaAngle(l_ActualPoint, g_AnteriorPuntLinia);
                                    Log.d("BODINA-Touch-Afegim", String.valueOf(l_Punt.Punt.x) + ", " + String.valueOf(l_Punt.Punt.y));
                                    g_LiniaPunts.add(l_Punt);
                                    Log.d("BODINA-Touch-Angle", String.valueOf(g_LiniaPunts.size()) + " " + String.valueOf(l_Punt.Angle));
                                    // Validem que el punt anterior no quedi descartat per l'angle
                                    if (g_LiniaPunts.size() >= 3) {
                                        l_Aux = g_LiniaPunts.get(g_LiniaPunts.size() - 2);
                                        Log.d("BODINA-Touch-Recuperem", (g_LiniaPunts.size() - 2) + " " + l_Aux.Angle);
                                        Double l_DiferenciaAngles = Math.abs(l_Aux.Angle - l_Punt.Angle);
                                        Log.d("BODINA-Touch-Diferencia", String.valueOf(l_DiferenciaAngles));
                                        if (l_DiferenciaAngles < g_RatioAngle) {
                                            l_Aux2 = g_LiniaPunts.get(g_LiniaPunts.size() - 2);
                                            l_Aux2.Descartat = true;
                                            g_LiniaPunts.set(g_LiniaPunts.size() - 2, l_Aux2);
                                            Log.d("BODINA-Touch-Descartem", String.valueOf(g_LiniaPunts.size() - 2));
                                        }
                                    }
                                    g_AnteriorPuntLinia = l_ActualPoint;
                                    Log.d("BODINA-Touch-Fi", "------------");
                                }
                                g_Dibuixant = true;
                                invalidate();
                            }
                            // Per si fem pitch
                            // Calculate the distance moved
                            final float dx = l_X - mLastTouchX;
                            final float dy = l_Y - mLastTouchY;
                            // Move the object
                            mPosX += dx;
                            mPosY += dy;
                            // Remember this touch position for the next move event
                            mLastTouchX = l_X;
                            mLastTouchY = l_Y;
                            break;

                        case recta:
                            Log.d("BODINA-Touch", "-----> Continuem5");
                            if (g_IniciDibuix) {
                                if (g_LiniaPunts.size() == 1) {
                                    // Afegim punt (el altre punt es el inicial)
                                    l_Punt = new punt();
                                    l_Punt.Punt = l_ActualPoint;
                                    l_Punt.Descartat = false;
                                    l_Punt.Angle = 0.0;
                                    g_LiniaPunts.add(l_Punt);
                                } else {
                                    // Modifiquem el punt final
                                    l_Aux = g_LiniaPunts.get(1);
                                    l_Aux.Punt = l_ActualPoint;
                                    g_LiniaPunts.set(1, l_Aux);
                                }
                                g_Dibuixant = true;
                                invalidate();
                            }
                            break;

                        case texte:
                            if (g_TexteSeleccionat != null) {
                                // Movem el punt
                                g_TextesPlanol.get(g_TexteSeleccionat.Id).Punt = l_ActualPoint;
                                // Calculem el detector del texte i validem si ens volen esborrar
                                l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                l_Esborrar = new Rect();
                                g_IMB_Esborrar.getHitRect(l_Esborrar);
                                if (l_Detector.intersect(l_Esborrar)) {
                                    g_TexteSeleccionat.Esborrantse = true;
                                } else {
                                    g_TexteSeleccionat.Esborrantse = false;
                                }
                                invalidate();
                            }
                            break;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d("BODINA-Touch", "-----> Continuem6");
                    switch (g_ModusDibuix) {
                        case recta:
                        case curva:
                            if (g_Dibuixant) {
                                l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                if (l_Detector.intersect(g_DetectorIni)) {
                                    Log.d("BODINA-TouchUP", "------------------------ Enganxat");
                                    // Modifiquem el darrer punt perque apunti exacatament al inici
                                    l_Aux = g_LiniaPunts.get(g_LiniaPunts.size() - 1);
                                    l_Aux.Punt = g_PrimerPuntDibuix;//startPoint;
                                    g_LiniaPunts.set(g_LiniaPunts.size() - 1, l_Aux);
                                    g_PuntFinalAnterior = g_PrimerPuntDibuix;//startPoint;
                                    // Indiquem que ja esta esta finalitzat (perque sigui detectat en el invalidate)
                                    g_Finalitzat = true;
                                    g_LiniesPlanol.add(g_LiniaPunts);
                                } else {
                                    g_PuntFinalAnterior = l_ActualPoint;
                                    // Afegim el darrer punt (i que no es descarti)
                                    l_Aux = new punt();
                                    l_Aux.Punt = g_PuntFinalAnterior;
                                    l_Aux.Descartat = false;
                                    l_Aux.Angle = -999.0;
                                    g_LiniaPunts.add(l_Aux);
                                    // Afegim la linia
                                    //LiniesPlanol.add(PuntsPlanol);
                                    //PuntsPlanol = new ArrayList<punt>();
                                    Log.d("BODINA-TouchUP", "------------------------ NO enganxat");
                                }
                                // Netegem linia
                                g_LiniaPunts = new ArrayList<punt>();
                                g_PuntInicialLinia = null;
                                //drawCanvas.drawPath(drawPath, drawPaint);
                                //drawPath.reset();
                            }
                            break;

                        case texte:
                            if (g_TexteSeleccionat != null) {
                                // Recalculem el detector del texte que hem mogut
                                l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                        Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                // Validem si l'usuari ens vol esborrar
                                l_Esborrar = new Rect();
                                g_IMB_Esborrar.getHitRect(l_Esborrar);
                                if (l_Detector.intersect(l_Esborrar)) {
                                    g_TexteSeleccionat.Esborrat = true;
                                } else {
                                    g_TexteSeleccionat.Detector = l_Detector;
                                }
                            }
                            break;
                    }
                    g_Dibuixant = false;
                    g_IniciDibuix = false;
                    invalidate();
                    break;

                default:
                    // Validem la resta de events amb el Gesture
                    return g_GestureDetector.onTouchEvent(p_Event);
            }
        }
        else{
            Log.d("BODINA-Touch", "Hem escalat");
        }
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent p_Event) {
            Rect l_DetectorIni;
            texte l_Texte;
            final EditText l_Input;

            l_DetectorIni = new Rect(Math.round(p_Event.getX()) - 50, Math.round(p_Event.getY()) - 50,
                                     Math.round(p_Event.getX()) + 50, Math.round(p_Event.getY()) + 50);
            l_Texte = MarquemTexte(l_DetectorIni);
            if (l_Texte != null){
                Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                g_TexteSeleccionat = l_Texte;
                l_Input = new EditText(g_Pare);
                // Mostrem una finestra per modificar el texte
                AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(g_Pare);
                g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
                g_alertDialogBuilder.setView(l_Input);
                l_Input.setText(l_Texte.Texte);
                g_alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p_dialog, int which) {
                                g_TexteSeleccionat.Texte = l_Input.getText().toString();
                                invalidate();
                            }
                        })
                        .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface p_dialog, int p_id) {
                            }
                        });
                g_alertDialogBuilder.show();
            }
            else {
                Log.d("BODINA-Down", "--------> Nou " + g_NouTexte + ".");
            }
            return true;
        }
    }

    public void EsborrarPlanol(){
        Log.d("BODINA-TouchUP", "------------------------ Esborrem");
        g_LiniesPlanol = new ArrayList<ArrayList<punt>>();
        g_TextesPlanol = new ArrayList<texte>();
        invalidate();
    }

    private double CalculaDistancia(PointF P_Punt1, PointF P_Punt2){
        double l_Part1, l_Part2, l_Resultat;

        l_Part1 = new Float(P_Punt1.x-P_Punt2.x);
        l_Part2 = new Float(P_Punt1.y-P_Punt2.y);

        l_Resultat = Math.sqrt( Math.pow(l_Part1, 2) + Math.pow(l_Part2, 2));
        return l_Resultat;
    }

    private String EscalaDistancia(double P_Distancia){
        String l_Resultat = null;

        l_Resultat = Math.round(P_Distancia/5) + " m.";
        return l_Resultat;
    }

    private texte MarquemTexte(Rect P_Posicio){
        texte l_Marcat = null;

        for (int i=0; i < g_TextesPlanol.size(); i++){
            if (P_Posicio.intersect(g_TextesPlanol.get(i).Detector)){
                l_Marcat = g_TextesPlanol.get(i);
                break;
            }
        }
        return l_Marcat;
    }

    private void FinestraTexte(final String p_Texte){
        final EditText l_Input;

        l_Input = new EditText(g_Pare);
        // Mostrem una finestra per modificar el texte
        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(g_Pare);
        g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
        g_alertDialogBuilder.setView(l_Input);
        l_Input.setText(p_Texte);
        g_alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        EscriuTexte(l_Input.getText().toString(), g_PuntActual);
                        // Pintem
                        invalidate();
                    }
                })
                .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface p_dialog, int p_id) {
                    }
                });
        g_alertDialogBuilder.show();
    }

    public texte EscriuTexte(String P_TexteDonat, PointF p_PuntDonat){
        // Afegim el texte que ha introduit l'usuari, el posem al mig sino s'indica un altre,
        // el usuari el podra moure
        texte l_Texte = new texte();
        Rect l_Detector;

        l_Detector = new Rect(Math.round(p_PuntDonat.x) - 30, Math.round(p_PuntDonat.y) - 30,
                              Math.round(p_PuntDonat.x) + 30, Math.round(p_PuntDonat.y) + 30);
        l_Texte.Id = g_TextesPlanol.size();
        l_Texte.Detector = l_Detector;
        l_Texte.Texte = P_TexteDonat;
        l_Texte.Punt = p_PuntDonat;
        l_Texte.Esborrat = false;
        l_Texte.Esborrantse = false;
        g_TextesPlanol.add(l_Texte);

        return l_Texte;
    }

    public void Quadricula(){
        g_Quadricula = !g_Quadricula;
        invalidate();
    }

}

                        /* ANIMACIO
    // Experiment per fer un rectangle que es modifiqui amb animation
    private class AnimatableRectF extends RectF {
        public AnimatableRectF() {
            super();
        }

        public AnimatableRectF(float left, float top, float right, float bottom) {
            super(left, top, right, bottom);
        }

        public AnimatableRectF(RectF r) {
            super(r);
        }

        public AnimatableRectF(Rect r) {
            super(r);
        }

        public void setTop(float top){
            this.top = top;
        }
        public void setBottom(float bottom){
            this.bottom = bottom;
        }
        public void setRight(float right){
            this.right = right;
        }
        public void setLeft(float left){
            Log.d("BODINA-TouchUP", "------------------------ Aplha2");
            //drawPaint.setStyle(Paint.Style.FILL);
        }
        public void setRotation(float inici, float p_final ){
            Log.d("BODINA-TouchUP", "------------------------ Rotar");
        }
        public void setAlpha(float inici, float p_final ){
            Log.d("BODINA-TouchUP", "------------------------ Aplha");
            //drawPaint.setStyle(Paint.Style.FILL);
        }

    }                        private AnimatableRectF g_Detector;

                        g_Detector = new AnimatableRectF(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                                         Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        float translateX = 50.0f;
                        float translateY = 50.0f;

                        ObjectAnimator animateLeft = ObjectAnimator.ofFloat(g_Detector, "left", g_Detector.left, g_Detector.left+translateX);
                        ObjectAnimator animateRight = ObjectAnimator.ofFloat(g_Detector, "right", g_Detector.right, g_Detector.right+translateX);
                        ObjectAnimator animateTop = ObjectAnimator.ofFloat(g_Detector, "top", g_Detector.top, g_Detector.top+translateY);
                        ObjectAnimator animateBottom = ObjectAnimator.ofFloat(g_Detector, "bottom", g_Detector.bottom, g_Detector.bottom + translateY);
                        animateBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                Log.d("BODINA-Draw", "-----> PostInvalidate");
                                postInvalidate();
                            }
                        });
                        final AnimatorSet rectAnimation = new AnimatorSet();
                        rectAnimation.playTogether(animateLeft, animateRight, animateTop, animateBottom);
                        rectAnimation.setDuration(10000).start();
                        rectAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Log.d("BODINA-Draw", "-----> Creix?");
                                rectAnimation.start();
                            }

                        });
                        */
                        /* No me ha funcionat
                        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(g_Detector ,
                                "rotation", 0f, 360f);
                        imageViewObjectAnimator.setDuration(1000); // miliseconds
                        imageViewObjectAnimator.start();
                        imageViewObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                        imageViewObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                Log.d("BODINA-Draw", "-----> PostInvalidate");
                                postInvalidate();
                            }
                        });
                        ObjectAnimator mAlphaAnimation = ObjectAnimator.ofFloat(g_Detector, "alpha", 0.0f,1.0f);
                        mAlphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                postInvalidate();
                            }

                        });



        g_Detector = new AnimatableRectF(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
        Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
        float translateX = 50.0f;
        float translateY = 50.0f;
        ObjectAnimator animateLeft = ObjectAnimator.ofFloat(g_Detector, "left", g_Detector.left, g_Detector.left+translateX);
        ObjectAnimator animateRight = ObjectAnimator.ofFloat(g_Detector, "right", g_Detector.right, g_Detector.right+translateX);
        ObjectAnimator animateTop = ObjectAnimator.ofFloat(g_Detector, "top", g_Detector.top, g_Detector.top+translateY);
        ObjectAnimator animateBottom = ObjectAnimator.ofFloat(g_Detector, "bottom", g_Detector.bottom, g_Detector.bottom + translateY);
        animateBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
@Override
public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Log.d("BODINA-Draw", "-----> PostInvalidate");
        postInvalidate();
        }
        });
final AnimatorSet rectAnimation = new AnimatorSet();
        rectAnimation.playTogether(animateLeft, animateRight, animateTop, animateBottom);
        rectAnimation.setDuration(10000).start();
        */
                /*
                rectAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.d("BODINA-Draw", "-----> Creix?");
                        rectAnimation.start();
                    }

                });
       */

                        /*
                        final AnimatorSet rectAnimation = new AnimatorSet();
                        rectAnimation.play(mAlphaAnimation);
                        rectAnimation.setDuration(1000).start();
                        rectAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Log.d("BODINA-Draw", "-----> Parpadeja?");
                                if (drawPaint.getStyle() == Paint.Style.FILL) {
                                    drawPaint.setStyle(Paint.Style.STROKE);
                                }
                                else{
                                    drawPaint.setStyle(Paint.Style.FILL);
                                }
                                //rectAnimation.setDuration(1000).start();
                                rectAnimation.start();
                            }

                        });
                        */
