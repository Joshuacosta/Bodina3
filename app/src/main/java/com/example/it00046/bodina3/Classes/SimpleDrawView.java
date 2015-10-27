package com.example.it00046.bodina3.Classes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.R;
import com.example.it00046.bodina3.salons_client_planol;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by it00046 on 26/01/2015.
 */
//public class DrawingView extends View {
public class SimpleDrawView extends RelativeLayout {
    public Context pare;
    // Path
    private Path g_drawPath;
    // Paint's
    private Paint drawPaint, drawPaintFinal, canvasPaint, drawPaintText;
    // Controlador de events
    GestureDetector gestureDetector;
    // Colors
    private int paintColor = 0xFF660000;
    // Canvas
    private Canvas drawCanvas;
    // Canvas bitmap
    private Bitmap canvasBitmap;
    // Punts i texte de treball
    private PointF g_PuntInicial = null, g_PuntFinalAnterior = null, g_PrimerPuntDibuix = null;
    private ArrayList<PointF> Punts = new ArrayList<PointF>();
    private PointF g_AnteriorPunt = new PointF();
    private texte g_TexteSeleccionat = null;
    // Modes i variables de treball de dibuix
    public enum g_Modus {recta,curva,texte};
    public g_Modus g_ModusDibuix = g_Modus.recta;
    static public String g_NouTexte;
    // Distancia
    static private TextView g_Metres;

    private PointF g_AnteriorPunt_1 = new PointF();
    private PointF g_AnteriorPunt_2 = new PointF();
    private Double g_Angle = null;

    private Rect g_DetectorFi = new Rect(), g_DetectorIni = null;
    private AnimatableRectF g_Detector;
    public boolean g_Finalitzat = false;
    static private int g_CenterX, g_CenterY;

    // Array per guardar els punts amb el que fem les linies
    private ArrayList<punt> g_PuntsPlanol = new ArrayList<punt>();
    class punt {
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
        public int Id;
        public Rect Detector;
        public void texte(){
        }
    }

    public SimpleDrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        // creating new gesture detector
        gestureDetector = new GestureDetector(context, new GestureListener());
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
        texte l_Texte = new texte();

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
        g_drawPath.reset();

        // Pintem rectes i curves
        /*
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
                    }
                    else {
                        Log.d("BODINA-OnDraw-LINE", String.valueOf(i));
                        g_drawPath.lineTo(ara.Punt.x, ara.Punt.y);
                    }
                    //
                }
                else {
                    Log.d("BODINA-OnDraw-NO Pintem", String.valueOf(i));
                }
            }
        }
        */
        // Pintem el "detector" final a la darrera linia si el dibuix no esta finalitzat
        if (g_Finalitzat == false){
            //if (l_Linia.size() > 1) {
                //l_EndPoint = l_Linia.get(l_Linia.size() - 1).Punt;
                l_EndPoint = new PointF(100,100);
                g_DetectorFi = new Rect(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
                                        Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
                //canvas.drawRect(g_DetectorFi, drawPaint);
                //
                if (g_Detector != null) {
                    canvas.drawRect(g_Detector, drawPaint);
                }
                //
                /*
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
            //}
        }
        // Pintem textes
        /*
        Log.d("BODINA-Draw", "-----> Textes " + g_TextesPlanol.size());
        for (int k=0; k < g_TextesPlanol.size(); k++) {
            l_Texte = g_TextesPlanol.get(k);
            Log.d("BODINA-Draw", "-----> Escribim " + l_Texte.Texte);
            canvas.drawText(l_Texte.Texte, l_Texte.Punt.x, l_Texte.Punt.y, drawPaintText);
        }
        */
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            float l_X = e.getX();
            float l_Y = e.getY();
            texte l_Texte;
            PointF l_ActualPoint = new PointF(l_X, l_Y);
            Rect l_Detector;

            switch (g_ModusDibuix) {
                case recta:
                case curva:
                    // Si es el primer punt lo que fem es definir el detector inicial per poder
                    // determinar quan tanquem el dibuix
                    if (g_PrimerPuntDibuix == null){
                        Log.d("BODINA-Draw", "-----> Definim detector");
                        g_PrimerPuntDibuix = l_ActualPoint;
                        g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
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
                    }
                    g_PuntInicial = l_ActualPoint;
                    // Afegim la linia que anem definint (si no es la primera)
                    if (g_PuntsPlanol.size() > 0) {
                        g_LiniesPlanol.add(g_PuntsPlanol);
                    }
                    // Definim el rectangle inicial de conexio per validar que continuem
                    l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                            Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                    if (l_Detector.intersect(g_DetectorFi)) {
                        Log.d("BODINA-TouchDOWN", "Reinici -------------");
                        // Inici nova linia
                        // Definim el punt inicial que es el darrer anteior (es guardat a g_PuntFinalAnterior)
                        punt l_punt = new punt();
                        l_punt.Punt = g_PuntFinalAnterior;
                        l_punt.Descartat = false;
                        l_punt.Angle = -999.0;
                        g_PuntsPlanol.add(l_punt);
                        // Apuntem punt anterior de la linia que fem ara
                        g_AnteriorPunt = g_PuntFinalAnterior;
                    }
                    else {
                        Log.d("BODINA-TouchDOWN", "Inici ---------------");
                        // Definim el punt inicial
                        punt l_punt = new punt();
                        l_punt.Punt = g_PuntInicial;
                        l_punt.Descartat = false;
                        l_punt.Angle = -999.0;
                        g_PuntsPlanol.add(l_punt);
                        // El anterior punt es el primer punt
                        g_AnteriorPunt = g_PuntInicial;
                        //g_AnteriorPunt_1 = startPoint;
                        //g_AnteriorPunt_2 = startPoint;
                    }
                    break;

                case texte:
                        /*
                        g_AnteriorPunt_1 = l_ActualPoint;
                        if (g_DetectorIni == null) {
                            g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        }
                        l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        if (g_DetectorFi.contains(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y))) {
                            Log.d("BODINA-Touch", "Intersecta");
                        }
                        else{
                            Log.d("BODINA-Touch", "NO Intersecta");
                        }
                        */
                    l_Detector = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                            Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                    l_Texte = MarquemTexte(l_Detector);
                    if (l_Texte != null){
                        Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                        g_TexteSeleccionat = l_Texte;
                    }
                    else {
                        Log.d("BODINA-Down", "--------> Nou " + g_NouTexte + ".");
                        l_Texte = new texte();
                        l_Texte.Id = g_TextesPlanol.size();
                        l_Texte.Detector = l_Detector;
                        l_Texte.Texte = g_NouTexte;
                        l_Texte.Punt = l_ActualPoint;
                        g_TextesPlanol.add(l_Texte);
                        //
                        g_TexteSeleccionat = null;
                    }
                    break;
            }
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Rect l_DetectorIni;
            texte l_Texte;

            l_DetectorIni = new Rect(Math.round(e.getX()) - 50, Math.round(e.getY()) - 50,
                    Math.round(e.getX()) + 50, Math.round(e.getY()) + 50);
            l_Texte = MarquemTexte(l_DetectorIni);
            if (l_Texte != null){
                Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                g_TexteSeleccionat = l_Texte;

                final EditText l_input = new EditText(pare);

                // Mostrem una finestra per demanar el texte a introduir
                AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(pare);
                g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
                g_alertDialogBuilder.setView(l_input);
                l_input.setText(l_Texte.Texte);
                g_alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p_dialog, int which) {

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

    public boolean onTouchEvent3(MotionEvent event) {
        //detect user touch
        float l_X = event.getX();
        float l_Y = event.getY();
        Double l_Angle;
        texte l_Texte;
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
                        g_PuntInicial = l_ActualPoint;
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
                            l_punt.Punt = g_PuntFinalAnterior;
                            l_punt.Descartat = false;
                            l_punt.Angle = -999.0;
                            g_PuntsPlanol.add(l_punt);
                            g_AnteriorPunt = g_PuntFinalAnterior;
                        }
                        else {
                            Log.d("BODINA-TouchDOWN", "Inici ---------------");
                            // Definim el punt inicial
                            punt l_punt = new punt();
                            l_punt.Punt = g_PuntInicial;
                            l_punt.Descartat = false;
                            l_punt.Angle = -999.0;
                            g_PuntsPlanol.add(l_punt);
                            //LiniesPlanol.add(PuntsPlanol);
                            g_AnteriorPunt = g_PuntInicial;
                            //g_AnteriorPunt_1 = startPoint;
                            //g_AnteriorPunt_2 = startPoint;
                        }
                        break;

                    case texte:
                        /*
                        g_AnteriorPunt_1 = l_ActualPoint;
                        if (g_DetectorIni == null) {
                            g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        }
                        l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        if (g_DetectorFi.contains(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y))) {
                            Log.d("BODINA-Touch", "Intersecta");
                        }
                        else{
                            Log.d("BODINA-Touch", "NO Intersecta");
                        }
                        */
                        l_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        l_Texte = MarquemTexte(l_DetectorIni);
                        if (l_Texte != null){
                            Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                            g_TexteSeleccionat = l_Texte;
                        }
                        else {
                            Log.d("BODINA-Down", "--------> Nou " + g_NouTexte + ".");
                            l_Texte = new texte();
                            l_Texte.Id = g_TextesPlanol.size();
                            l_Texte.Detector = l_DetectorIni;
                            l_Texte.Texte = g_NouTexte;
                            l_Texte.Punt = l_ActualPoint;
                            g_TextesPlanol.add(l_Texte);
                            //
                            g_TexteSeleccionat = null;
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
                        if (g_TexteSeleccionat != null){
                            // Modifiquem el punt
                            g_TextesPlanol.get(g_TexteSeleccionat.Id).Punt = l_ActualPoint;
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
                            g_PuntFinalAnterior = g_PrimerPuntDibuix;//startPoint;
                            // Afegim la darrera linia
                            //LiniesPlanol.add(PuntsPlanol);
                            // Indiquem que ja esta esta finalitzat (perque sigui detectat en el invalidate)
                            g_Finalitzat = true;
                            g_LiniesPlanol.add(g_PuntsPlanol);
                        }
                        else{
                            g_PuntFinalAnterior = l_ActualPoint;
                            // Afegim el darrer punt (i que no es descarti)
                            punt l_Aux2 = new punt();
                            l_Aux2.Punt = g_PuntFinalAnterior;
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
                        g_PuntInicial = null;
                        //drawCanvas.drawPath(drawPath, drawPaint);
                        //drawPath.reset();
                        break;

                    case texte:
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

    static public void DefinimMetres(TextView p_Metres){
        g_Metres = p_Metres;
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

    public void EscriuTexte(String P_TexteDonat){
        // Afegim el texte que ha introduit l'usuari, el posem al mig, el usuari el podra moure
        texte l_Texte = new texte();
        Rect l_Detector;

        l_Detector = new Rect(Math.round(g_CenterX) - 50, Math.round(g_CenterY) - 50,
                              Math.round(g_CenterX) + 50, Math.round(g_CenterY) + 50);
        l_Texte.Id = g_TextesPlanol.size();
        l_Texte.Detector = l_Detector;
        l_Texte.Texte = P_TexteDonat;
        l_Texte.Punt = new PointF(g_CenterX, g_CenterY);
        g_TextesPlanol.add(l_Texte);
        // Pintem
        invalidate();
    }

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
            this.left = left;
        }

    }
}
