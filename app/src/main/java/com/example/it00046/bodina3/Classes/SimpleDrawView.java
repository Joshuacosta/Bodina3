package com.example.it00046.bodina3.Classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.R;

import java.util.ArrayList;

public class SimpleDrawView extends RelativeLayout {
    public Context g_Pare;
    private Path g_drawPath;
    private Paint g_PaintNormal, g_PaintFinal, g_PaintCanvas, g_PaintText;
    // Controlador de events
    private GestureDetector g_GestureDetector;
    // Colors
    private int g_PaintColor = 0xFF660000;
    // Canvas i Bitmap
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    // Punts i texte de treball
    private PointF g_PuntInicialLinia = null, g_PuntFinalAnterior = null, g_PrimerPuntDibuix = null;
    private PointF g_AnteriorPuntLinia = null;
    private texte g_TexteSeleccionat = null;
    // Modes i variables de treball de dibuix
    public enum g_Modus {recta,curva,texte};
    public g_Modus g_ModusDibuix = g_Modus.recta;
    static public String g_NouTexte = null;
    static public int g_RatioDistancia = 100; // Es la finura de la curva
    static public int g_RatioAngle =15; // Idem, podrien ser parametritzables?
    static private TextView g_Metres;
    //
    private Rect g_Punter = new Rect(), g_DetectorIni = null;
    public boolean g_Finalitzat = false;
    static private int g_CenterX, g_CenterY;

    // Array per guardar els punts amb el que fem les linies i/o curves
    private ArrayList<punt> g_LiniaPunts = new ArrayList<punt>();
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

    public SimpleDrawView(Context p_Context, AttributeSet p_Attrs){
        super(p_Context, p_Attrs);
        setupDrawing();
        // creating new gesture detector
        g_GestureDetector = new GestureDetector(p_Context, new GestureListener());
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
        g_PaintNormal.setStrokeWidth(20);
        g_PaintNormal.setStyle(Paint.Style.STROKE);
        g_PaintNormal.setStrokeJoin(Paint.Join.ROUND);
        g_PaintNormal.setStrokeCap(Paint.Cap.ROUND);
        // Definim paint de planol "terminat"
        g_PaintFinal = new Paint();
        g_PaintFinal.setColor(Color.LTGRAY);
        g_PaintFinal.setStyle(Paint.Style.FILL);
        g_PaintFinal.setAntiAlias(true);
        // Definim el paint de texte
        g_PaintText = new Paint();
        g_PaintText.setTextSize(35);
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

    // La definida inicialment

    @Override
    protected void onDraw(Canvas canvas) {
        PointF l_EndPoint;
        ArrayList<punt> l_Linia = new ArrayList<>();
        texte l_Texte = new texte();
        punt l_Actual, l_Seguent;

        //draw view
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        //Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
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
                    //
                }
                else {
                    Log.d("BODINA-OnDraw-NO Pintem", String.valueOf(i));
                }
            }
        }
        // Pintem el paath definit i pintem el punter si el dibuix encara no esta finalitzat
        if (g_Finalitzat == false){
            if (l_Linia.size() > 1) {
                l_EndPoint = l_Linia.get(l_Linia.size() - 1).Punt;
                g_Punter = new Rect(Math.round(l_EndPoint.x) - 50, Math.round(l_EndPoint.y) - 50,
                                    Math.round(l_EndPoint.x) + 50, Math.round(l_EndPoint.y) + 50);
                //
                //canvas.drawRect(g_Punter, drawPaint);
                // Pintem un cercle on es el detector (crec que queda mes elegant...)
                canvas.drawCircle(Math.round(l_EndPoint.x), Math.round(l_EndPoint.y), 100, g_PaintNormal);
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
            Log.d("BODINA-Draw", "-----> Escribim " + l_Texte.Texte);
            canvas.drawText(l_Texte.Texte, l_Texte.Punt.x, l_Texte.Punt.y, g_PaintText);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent p_Event) {
        float l_X = p_Event.getX();
        float l_Y = p_Event.getY();
        texte l_Texte;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_Detector;
        punt l_Punt = new punt(), l_Aux = new punt(), l_Aux2 = new punt();
        double l_Part1, l_Part2, l_Dist;


        switch (p_Event.getAction()){
            case MotionEvent.ACTION_DOWN:
                switch (g_ModusDibuix) {
                    case recta:
                    case curva:
                        // Si es el primer punt lo que fem es definir el detector inicial per poder determinar
                        // quan tanquem el dibuix
                        if (g_PrimerPuntDibuix == null){
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
                        //if (g_LiniaPunts.size() > 0) {
                            g_LiniesPlanol.add(g_LiniaPunts);
                        //}
                        // Definim el rectangle inicial de conexio per validar que continuem, sino, no fem res
                        l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        if (l_Detector.intersect(g_Punter)) {
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
                        }
                        else {
                            // Validem que es el primer, sino, no hem de fer res, hem de dibuixar desde la
                            // darrera linia
                            if (g_Punter == null) {
                                Log.d("BODINA-TouchDOWN", "Inici ---------------");
                                // Definim el punt inicial de la linia
                                l_Punt = new punt();
                                l_Punt.Punt = g_PuntInicialLinia;
                                l_Punt.Descartat = false;
                                l_Punt.Angle = -999.0;
                                g_LiniaPunts.add(l_Punt);
                                // El anterior punt es el primer punt
                                g_AnteriorPuntLinia = g_PuntInicialLinia;
                            }
                        }
                        invalidate();
                        break;

                    case texte:
                        // Validem si hem tocat un texte
                        l_Detector = new Rect(Math.round(l_ActualPoint.x) - 50, Math.round(l_ActualPoint.y) - 50,
                                Math.round(l_ActualPoint.x) + 50, Math.round(l_ActualPoint.y) + 50);
                        l_Texte = MarquemTexte(l_Detector);
                        if (l_Texte != null){
                            Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                            g_TexteSeleccionat = l_Texte;
                        }
                        else {
                            g_TexteSeleccionat = null;
                        }
                        break;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                switch (g_ModusDibuix) {
                    case curva:
                        // Calculem distancia
                        l_Part1 = new Float(l_ActualPoint.x-g_AnteriorPuntLinia.x);
                        l_Part2 = new Float(l_ActualPoint.y-g_AnteriorPuntLinia.y);
                        l_Dist = Math.sqrt( Math.pow(l_Part1, 2) + Math.pow(l_Part2, 2));
                        // Validem si portem massa distancia i hem de forzar un punt
                        if (l_Dist > g_RatioDistancia){
                            // Estem dibuixant curves (en rectes no superem el ratio)
                            l_Punt = new punt();
                            l_Punt.Punt = l_ActualPoint;
                            l_Punt.Descartat = false;
                            l_Punt.Angle = Globals.CalculaAngle(l_ActualPoint, g_AnteriorPuntLinia);
                            Log.d("BODINA-Touch-Afegim", String.valueOf(l_Punt.Punt.x) + ", " + String.valueOf(l_Punt.Punt.y));
                            g_LiniaPunts.add(l_Punt);
                            Log.d("BODINA-Touch-Angle", String.valueOf(g_LiniaPunts.size()) + " " + String.valueOf(l_Punt.Angle));
                            // Validem que el punt anterior no quedi descartat per l'angle
                            if (g_LiniaPunts.size() >= 3){
                                l_Aux = g_LiniaPunts.get(g_LiniaPunts.size() - 2);
                                Log.d("BODINA-Touch-Recuperem", (g_LiniaPunts.size() - 2) + " " + l_Aux.Angle);
                                Double l_DiferenciaAngles = Math.abs(l_Aux.Angle - l_Punt.Angle);
                                Log.d("BODINA-Touch-Diferencia", String.valueOf(l_DiferenciaAngles));
                                if (l_DiferenciaAngles < g_RatioAngle) {
                                    l_Aux2 = g_LiniaPunts.get(g_LiniaPunts.size() - 2);
                                    l_Aux2.Descartat = true;
                                    g_LiniaPunts.set(g_LiniaPunts.size() - 2, l_Aux2);
                                    Log.d("BODINA-Touch-Descartem", String.valueOf(g_LiniaPunts.size() -2));
                                }
                            }
                            g_AnteriorPuntLinia = l_ActualPoint;
                            Log.d("BODINA-Touch-Fi", "------------");
                        }
                        invalidate();
                        break;

                    case recta:
                        if (g_LiniaPunts.size() == 1){
                            // Afegim punt (el altre punt es el inicial)
                            l_Punt = new punt();
                            l_Punt.Punt = l_ActualPoint;
                            l_Punt.Descartat = false;
                            l_Punt.Angle = 0.0;
                            g_LiniaPunts.add(l_Punt);
                        }
                        else{
                            // Modifiquem el punt final
                            l_Aux = g_LiniaPunts.get(1);
                            l_Aux.Punt = l_ActualPoint;
                            g_LiniaPunts.set(1, l_Aux);
                        }
                        invalidate();
                        break;

                    case texte:
                        if (g_TexteSeleccionat != null){
                            // Movem el punt
                            g_TextesPlanol.get(g_TexteSeleccionat.Id).Punt = l_ActualPoint;
                            invalidate();
                        }
                        break;
                }
                break;

            case MotionEvent.ACTION_UP:
                switch (g_ModusDibuix) {
                    case recta:
                    case curva:
                        l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                        if (l_Detector.intersect(g_DetectorIni)){
                            Log.d("BODINA-TouchUP", "------------------------ Enganxat");
                            // Modifiquem el darrer punt perque apunti exacatament al inici
                            l_Aux = g_LiniaPunts.get(g_LiniaPunts.size()-1);
                            l_Aux.Punt = g_PrimerPuntDibuix;//startPoint;
                            g_LiniaPunts.set(g_LiniaPunts.size() - 1, l_Aux);
                            g_PuntFinalAnterior = g_PrimerPuntDibuix;//startPoint;
                            // Indiquem que ja esta esta finalitzat (perque sigui detectat en el invalidate)
                            g_Finalitzat = true;
                            g_LiniesPlanol.add(g_LiniaPunts);
                        }
                        else{
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
                        break;

                    case texte:
                        break;
                }
                invalidate();
                break;

            default:
                // Validem la resta de events amb el Gesture
                return g_GestureDetector.onTouchEvent(p_Event);
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

                // Mostrem una finestra per demanar el texte a introduir
                AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(g_Pare);
                g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
                g_alertDialogBuilder.setView(l_Input);
                l_Input.setText(l_Texte.Texte);
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
