package com.example.it00046.bodina3.Classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.IntegerRes;
import android.text.InputFilter;
import android.text.TextPaint;
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

import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Feina.linia;
import com.example.it00046.bodina3.Classes.Feina.texte;
import com.example.it00046.bodina3.R;
import java.util.ArrayList;

public class PlanolEdicio extends RelativeLayout {

    ///////////////////////////////////////////////////
    public Context g_Pare;
    public boolean g_Quadricula = false;
    public String g_EscalaPlanol = "1x1";   // Definim aquest valors per poder fer una preview.
                                            // Així no dona error la execució del onDraw en disseny
    public String g_UnitatsPlanol;
    ////////////////////////////////////////////////
    private static final int g_MaxCaractersNom = 20;
    ////////////////////////////////////////////////
    private ScaleGestureDetector g_GestureScale;
    private float g_ScaleFactor = 1;
    private float scaleFactorAnterior = 1;
    static private float g_mPosX = 0;
    static private float g_mPosY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    private Path g_drawPath;
    private Paint g_PaintNormal, g_PaintFinal, g_PaintCanvas, g_PaintText, g_PaintQuadricula;
    private Paint g_PaintTextDistanciaBase, g_PaintTextDistancia, g_PaintTextEsborrantse;
    private Paint g_PaintLiniaAjuda;
    // Controlador de events
    private GestureDetector g_GestureDetector;
    // Canvas i Bitmap
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    // Punts i texte de treball
    static private PointF g_PuntInicialLinia = null, g_PuntFinalAnterior = null, g_PrimerPuntDibuix = null, g_PuntActual = null;
    static private texte g_TexteSeleccionat = null;
    static private linia g_MarcaDistancia = null;
    // Modes i variables de treball de dibuix
    public enum g_Modus {recta,curva,texte};
    static public g_Modus g_ModusDibuix = g_Modus.recta;
    static public g_Modus g_ModusDibuixAnterior = g_Modus.recta;
    static private Rect g_Punter = null, g_DetectorIni = null, g_CanvasRect = null;
    static public boolean g_Finalitzat = false, g_Dibuixant = false, g_IniciDibuix = false;
    //
    static private int g_CenterX = 0, g_CenterY = 0;
    public int g_UnitatX, g_UnitatY;
    public int g_EscalaPlanolAmplada, g_EscalaPlanolLlargada;
    private int g_AmpladaScreen, g_LlargadaScreen;
    public ImageButton g_IMB_Esborrar;

    // Array per guardar les linies que fem
    static public ArrayList<linia> g_LiniesPlanol = new ArrayList<>();
    // Array per guardar els textes del planol
    static public ArrayList<texte> g_TextesPlanol = new ArrayList<texte>();

    public PlanolEdicio(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
        // Definim el gesture detector
        g_GestureDetector = new GestureDetector(p_Context, new GestureListener());
        // Definim el gesture detector de scala
        g_GestureScale = new ScaleGestureDetector(p_Context, new ScaleListener());
    }

    private void setupDrawing(){
        // Definim path de dibuix
        g_drawPath = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        // Definim paint de linies
        g_PaintNormal = new Paint();
        g_PaintNormal.setColor(Color.LTGRAY);
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
        // Definim paint de quadricula
        g_PaintLiniaAjuda = new Paint();
        g_PaintLiniaAjuda.setColor(Color.RED);
        g_PaintLiniaAjuda.setAlpha(100);
        g_PaintLiniaAjuda.setStrokeWidth(4);
        g_PaintLiniaAjuda.setStyle(Paint.Style.STROKE);
        // Definim paint de planol "terminat"
        g_PaintFinal = new Paint();
        g_PaintFinal.setColor(Color.LTGRAY);
        g_PaintFinal.setAlpha(120);
        g_PaintFinal.setStyle(Paint.Style.FILL);
        g_PaintFinal.setAntiAlias(true);
        // Definim el paint de texte
        g_PaintText = new Paint();
        g_PaintText.setTextSize(35);
        // Definim el paint de texte distancia
        g_PaintTextDistancia = new Paint();
        g_PaintTextDistancia.setTextSize(22);
        g_PaintTextDistancia.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        g_PaintTextDistancia.setColor(Color.RED);
        // Definim paint de la base de la distancia
        g_PaintTextDistanciaBase = new Paint();
        g_PaintTextDistanciaBase.setColor(Color.GRAY);
        g_PaintTextDistanciaBase.setStyle(Paint.Style.FILL);
        g_PaintTextDistanciaBase.setAntiAlias(true);
        // Definim el paint de texte esborrante
        g_PaintTextEsborrantse = new Paint();
        g_PaintTextEsborrantse.setColor(Color.RED);
        g_PaintTextEsborrantse.setTextSize(35);
        // Inicialitzem variables estatiques (oju!)
        EsborrarPlanol();
        //g_mPosX = 0;
        //g_mPosY = 0;

    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);

        g_CanvasBitmap = Bitmap.createBitmap(p_w, p_h, Bitmap.Config.ARGB_8888);
        g_DrawCanvas = new Canvas(g_CanvasBitmap);
        g_AmpladaScreen = p_w;
        g_LlargadaScreen = p_h;
        g_CenterX = g_AmpladaScreen / 2;
        g_CenterY = g_LlargadaScreen / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        PointF l_TextePoint;
        Path l_Quadricula = new Path();
        String l_Distancia;
        Rect l_RectDistancia = new Rect();
        linia l_Linia2 = new linia();
        PointF l_PuntMig;

        canvas.save();
        // Calculem escala i unitats
        String[] l_Valors = g_EscalaPlanol.split("x");
        g_EscalaPlanolAmplada = Integer.valueOf(l_Valors[0]);
        g_UnitatX = Math.round(g_AmpladaScreen / g_EscalaPlanolAmplada);
        g_EscalaPlanolLlargada = Integer.valueOf(l_Valors[1]);
        g_UnitatY = Math.round(g_LlargadaScreen / g_EscalaPlanolLlargada);
        //
        Log.d("BODINA-Draw", "-----> Escalat de l'usuari amb gestos " + g_ScaleFactor);
        //canvas.translate(g_mPosX, g_mPosY);
        canvas.scale(g_ScaleFactor, g_ScaleFactor);
        g_CanvasRect = canvas.getClipBounds();
        //draw view
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        //Log.d("BODINA-OnDraw-Reset", "------------------------------------------------------------------------------------------------");
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem quadricula si es activa
        if (g_Quadricula){
            // Ja volarem l'escala...
            DisplayMetrics displayMetrics = g_Pare.getResources().getDisplayMetrics();

            float l_dpHeight = displayMetrics.heightPixels;
            float l_dpWidth = displayMetrics.widthPixels;
            int l_NumLiniesVerticals = Math.round(l_dpHeight)/g_UnitatY;
            int l_NumLiniesHoritzontals = Math.round(l_dpWidth)/g_UnitatX;

            for (int v=1; v <  l_NumLiniesVerticals; v++) {
                l_Quadricula.moveTo(0, g_UnitatY*v);
                l_Quadricula.lineTo(l_dpWidth, g_UnitatY*v);
            }

            for (int v=1; v <  l_NumLiniesHoritzontals; v++) {
                l_Quadricula.moveTo(g_UnitatX*v, 0);
                l_Quadricula.lineTo(g_UnitatX*v, l_dpHeight);
            }

        }
        canvas.drawPath(l_Quadricula, g_PaintQuadricula);
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem rectes i curves
        g_drawPath.reset();
        Log.d("BODINA-OnDraw", "Numero de linies " + g_LiniesPlanol.size());
        for (int I=0; I < g_LiniesPlanol.size(); I++) {
            l_Linia2 = g_LiniesPlanol.get(I);
            l_Linia2.Inici.offset(g_mPosX, g_mPosY);
            // Si estem a la primera linia ens posicionem "al principi" amb un move
            if (I == 0) {
                g_drawPath.moveTo(l_Linia2.Inici.x, l_Linia2.Inici.y);
            }
            // Pintem el punt final
            l_Linia2.Fi.offset(g_mPosX, g_mPosY);
            // Validem si es una curva
            if (l_Linia2.Curva) {
                l_Linia2.PuntCurva.offset(g_mPosX, g_mPosY);
                g_drawPath.quadTo(l_Linia2.PuntCurva.x, l_Linia2.PuntCurva.y, l_Linia2.Fi.x, l_Linia2.Fi.y);
                // Si s'ha mogut tambe hem de modificar la posicio
                if (l_Linia2.ObjDistancia.Mogut){
                    l_Linia2.ObjDistancia.Punt.offset(g_mPosX, g_mPosY);
                }
            }
            else {
                g_drawPath.lineTo(l_Linia2.Fi.x, l_Linia2.Fi.y);
            }
        }
        if (g_Finalitzat == false){
            if (g_LiniesPlanol.size() > 0) {
                g_Punter = new Rect(Math.round(l_Linia2.Fi.x) - 30, Math.round(l_Linia2.Fi.y) - 30,
                        Math.round(l_Linia2.Fi.x) + 30, Math.round(l_Linia2.Fi.y) + 30);
                // Pintem un cercle on es el detector (crec que queda mes elegant...)
                canvas.drawCircle(Math.round(l_Linia2.Fi.x), Math.round(l_Linia2.Fi.y), 25, g_PaintNormal);
                // Pintem el planol
                canvas.drawPath(g_drawPath, g_PaintNormal);
                // Pintem distancies de les rectes/curves
                for (int I=0; I < g_LiniesPlanol.size(); I++) {
                    l_Linia2 = g_LiniesPlanol.get(I);
                    // Calculem distancia de la linia i els bounds del texte
                    l_Distancia = EscalaDistancia(CalculaDistancia(l_Linia2.Inici, l_Linia2.Fi));
                    l_PuntMig = new PointF((l_Linia2.Inici.x + l_Linia2.Fi.x) / 2, (l_Linia2.Inici.y + l_Linia2.Fi.y) / 2);
                    l_RectDistancia = new Rect(Math.round(l_PuntMig.x) - 15, Math.round(l_PuntMig.y) - 15,
                            Math.round(l_PuntMig.x) + 15, Math.round(l_PuntMig.y) + 15);
                    // Definim el objecte distancia si no es definit
                    if (l_Linia2.ObjDistancia == null){
                        l_Linia2.ObjDistancia = new linia.metresCurva(l_PuntMig, l_Distancia, l_RectDistancia);

                    }
                    else{
                        // Modifiquem la posicio i els bounds
                        l_Linia2.ObjDistancia.Distancia = l_Distancia;
                        // Modifiquem la posicio segons la recta salvo que hagui sigut moguda per fer una curva
                        // (si s'ha mogut amb el dit la posició ja hi sera donada)
                        if (!l_Linia2.ObjDistancia.Mogut) {
                            l_Linia2.ObjDistancia.Detector = l_RectDistancia;
                            l_Linia2.ObjDistancia.Punt = l_PuntMig;
                        }
                        else{
                            // Movem el detector (hem mogut en el MOVE el punt base)
                            l_Linia2.ObjDistancia.Detector.offsetTo(Math.round(l_Linia2.ObjDistancia.Punt.x)-7, Math.round(l_Linia2.ObjDistancia.Punt.y)+7);
                        }
                    }
                    // Pintem cercle
                    canvas.drawCircle(l_Linia2.ObjDistancia.Detector.centerX(),
                            l_Linia2.ObjDistancia.Detector.centerY(),
                            30,
                            g_PaintTextDistanciaBase);
                    // Pintem distancia
                    canvas.drawText(l_Linia2.ObjDistancia.Distancia, l_Linia2.ObjDistancia.Detector.centerX() -15 , l_Linia2.ObjDistancia.Detector.centerY() + 7, g_PaintTextDistancia);
                }
                // Remarquem la linia que ens pot tancar el dibuix (si estem amb quadricula)
                if (g_Quadricula && (g_LiniesPlanol.size() > 2) ) {
                    if ((Math.round(l_Linia2.Fi.x) == Math.round(g_PrimerPuntDibuix.x)) ||
                        (Math.round(l_Linia2.Fi.y) == Math.round(g_PrimerPuntDibuix.y))){
                        // Mostrar la linia que els pot conectar
                        canvas.drawLine(l_Linia2.Fi.x, l_Linia2.Fi.y, g_PrimerPuntDibuix.x, g_PrimerPuntDibuix.y, g_PaintLiniaAjuda);
                    }
                }
            }
        }
        else {
            // Pintem el planol terminat
            canvas.drawPath(g_drawPath, g_PaintFinal);
            // Pintem distancies de les rectes
            for (int I=0; I < g_LiniesPlanol.size(); I++) {
                l_Linia2 = g_LiniesPlanol.get(I);
                // Calculem distancia de la linia i els bounds del texte
                l_Distancia = EscalaDistancia(CalculaDistancia(l_Linia2.Inici, l_Linia2.Fi));
                l_PuntMig = new PointF((l_Linia2.Inici.x + l_Linia2.Fi.x) / 2, (l_Linia2.Inici.y + l_Linia2.Fi.y) / 2);
                canvas.drawText(l_Distancia, l_PuntMig.x, l_PuntMig.y, g_PaintTextDistancia);
            }
        }
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem textes
        Log.d("BODINA-Draw", "-----> Textes " + g_TextesPlanol.size() + "/ Desplaçament " + g_mPosX + ", " + g_mPosY);
        for (int k=0; k < g_TextesPlanol.size(); k++) {
            // Validem si el texte l'han esborrat
            if (g_TextesPlanol.get(k).Esborrat == false) {
                // Movem el punt
                l_TextePoint = g_TextesPlanol.get(k).Punt;
                l_TextePoint.offset(g_mPosX, g_mPosY);
                // Validant si volen esborrar el texte
                if (g_TextesPlanol.get(k).Esborrantse == false) {
                    canvas.drawText(g_TextesPlanol.get(k).Texte,
                                    l_TextePoint.x,
                                    l_TextePoint.y,
                                    g_PaintText);
                }
                else{
                    canvas.drawText(g_TextesPlanol.get(k).Texte,
                                    l_TextePoint.x,
                                    l_TextePoint.y,
                                    g_PaintTextEsborrantse);
                }
                // Movem el Detector per si s'ha desplaçat el canvas
                g_TextesPlanol.get(k).Detector.offset(Math.round(g_mPosX), Math.round(g_mPosY));
            }
        }
        // ///////////////////////////////////////////////////////////////////////////////////////
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent p_Event) {
        float l_X = p_Event.getX()/g_ScaleFactor;
        float l_Y = p_Event.getY()/g_ScaleFactor;
        texte l_Texte;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_Detector, l_Esborrar;
        linia l_Linia = new linia();

        // Validem primer si hi han "gestos": doble tap
        g_GestureDetector.onTouchEvent(p_Event);
        // Validem escalat si no estem amb texte (si al fer escala fa linia perque "segueix", que fem?)
        if (g_ModusDibuix != g_Modus.texte) {
            g_GestureScale.onTouchEvent(p_Event);
        }
        if (!g_GestureScale.isInProgress()){
            // Continuem
            g_PuntActual = l_ActualPoint;
            final int action = p_Event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    g_mPosX = 0;
                    g_mPosY = 0;
                    switch (g_ModusDibuix) {
                        case recta:
                            if (!g_Finalitzat) {
                                Log.d("BODINA-TouchDOWN", "-----> Inici recta: " + l_ActualPoint.x + ", " + l_ActualPoint.y);
                                // Validem que si estem amb quadricula el punt s'ajusta a aquesta
                                if (g_Quadricula){
                                    l_ActualPoint.set(Math.round(l_ActualPoint.x) - mod(Math.round(l_ActualPoint.x),g_UnitatX), Math.round(l_ActualPoint.y) - mod(Math.round(l_ActualPoint.y),g_UnitatY));
                                }
                                // Si es el primer punt lo que fem es definir el detector inicial per poder determinar
                                // quan tanquem el dibuix
                                if (g_PrimerPuntDibuix == null) {
                                    g_PrimerPuntDibuix = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                    g_DetectorIni = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                                             Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                }
                                //
                                g_PuntInicialLinia = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                // Validem si tenim punter per seguir el planol
                                if (g_Punter != null) {
                                    l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                                          Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                    //g_Punter.offset(Math.round(g_mPosX), Math.round(g_mPosY));
                                    if (l_Detector.intersect(g_Punter)) {
                                        Log.d("BODINA-TouchDOWN", "Seguim -------------");
                                        l_Linia.Inici = new PointF(g_PuntFinalAnterior.x, g_PuntFinalAnterior.y);
                                        l_Linia.Fi = new PointF(g_PuntFinalAnterior.x, g_PuntFinalAnterior.y);
                                        g_LiniesPlanol.add(l_Linia);
                                        // Podem dibuixar
                                        g_IniciDibuix = true;
                                    }
                                    else {
                                        // No seguim amb el punter
                                        Log.d("BODINA-TouchDOWN", "No continuem ---------------");
                                        // Validem si estem arrosegant una distancia
                                        g_MarcaDistancia = MarquemDistanciaPunt(l_ActualPoint);
                                        if (g_MarcaDistancia != null) {
                                            g_IniciDibuix = false; //?
                                            g_ModusDibuixAnterior = g_ModusDibuix;
                                            g_ModusDibuix = g_Modus.curva;
                                        }
                                        else {
                                            // Ens apuntem posició per si lo que fa el usuari es moure el planol
                                            mLastTouchX = l_X;
                                            mLastTouchY = l_Y;
                                            // No podem dibuixar
                                            g_IniciDibuix = false;
                                        }
                                    }
                                }
                                else {
                                    Log.d("BODINA-TouchDOWN", "Inici ---------------");
                                    l_Linia.Inici = new PointF(g_PuntInicialLinia.x, g_PuntInicialLinia.y);
                                    l_Linia.Fi = new PointF(g_PuntInicialLinia.x, g_PuntInicialLinia.y);
                                    l_Linia.Curva = false;
                                    g_LiniesPlanol.add(l_Linia);
                                    // Podem dibuixar
                                    g_IniciDibuix = true;
                                }
                            }
                            else{
                                // Ens apuntem posició per si lo que vol el usuari
                                // es moure el planol
                                mLastTouchX = l_X;
                                mLastTouchY = l_Y;
                            }
                            break;

                        case texte:
                            Log.d("BODINA-TouchDown", "-----> Inici texte");
                            // Validem si hem tocat un texte que volem arrosegar
                            // (recorda que per editar un texte cal fer un doble tap)
                            l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                                  Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                            l_Texte = MarquemTexte(l_Detector);
                            if (l_Texte != null) {
                                Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                                g_TexteSeleccionat = l_Texte;
                            }
                            else {
                                // Afegim un texte al planol i mostrem la finestra de modificació de texte
                                g_TexteSeleccionat = null;
                                FinestraTexte(Globals.g_Native.getResources().getString(R.string.Text));
                            }
                            break;

                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    // Validem que si estem amb quadricula el punt segueix la quadricula
                    if (g_Quadricula){
                        l_ActualPoint.set(Math.round(l_ActualPoint.x) - mod(Math.round(l_ActualPoint.x),g_UnitatX), Math.round(l_ActualPoint.y) - mod(Math.round(l_ActualPoint.y),g_UnitatY));
                    }
                    //
                    switch (g_ModusDibuix) {
                        case curva:
                            // Estem arrosegant una distancia i a g_MarcaDistancia tenim la linia "afectada"
                            if (g_MarcaDistancia != null){
                                g_MarcaDistancia.Curva = true;
                                g_MarcaDistancia.ObjDistancia.Mogut = true;
                                g_MarcaDistancia.ObjDistancia.Punt = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                g_MarcaDistancia.PuntCurva = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                            }
                            invalidate();
                            break;

                        case recta:
                            Log.d("BODINA-Touch-Down", "Move");
                            if (g_IniciDibuix) {
                                g_LiniesPlanol.get(g_LiniesPlanol.size()-1).Fi = new PointF(g_PuntActual.x, g_PuntActual.y);
                                g_Dibuixant = true;
                                invalidate();
                            }
                            else {
                                // L'usuari està desplaçant el planol amb el dit
                                final float dx = l_X - mLastTouchX;
                                final float dy = l_Y - mLastTouchY;
                                g_mPosX = dx;
                                g_mPosY = dy;
                                // Guardem la darrera posicio
                                mLastTouchX = l_X;
                                mLastTouchY = l_Y;
                                // Movem els detectors si estem desplazant amb el dit i enacara estem dibuixant
                                if (g_DetectorIni != null) {
                                    g_DetectorIni.offset(Math.round(g_mPosX), Math.round(g_mPosY));
                                }
                                if (g_PuntInicialLinia != null) {
                                    g_PuntInicialLinia.offset(Math.round(g_mPosX), Math.round(g_mPosY));
                                }
                                if (g_PuntFinalAnterior != null) {
                                    g_PuntFinalAnterior.offset((int) g_mPosX, (int) g_mPosY);
                                }
                                if (g_PrimerPuntDibuix != null) {
                                    g_PrimerPuntDibuix.offset((int) g_mPosX, (int) g_mPosY);
                                }
                                //
                                invalidate();
                            }
                            break;

                        case texte:
                            if (g_TexteSeleccionat != null) {
                                // Movem el punt que posiciona el texte i el detector
                                g_TextesPlanol.get(g_TexteSeleccionat.Id).Punt = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                g_TextesPlanol.get(g_TexteSeleccionat.Id).Detector.offsetTo(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y));
                                // Validem si ens volen esborrar
                                l_Esborrar = new Rect();
                                g_IMB_Esborrar.getHitRect(l_Esborrar);
                                if (g_TexteSeleccionat.Detector.intersect(l_Esborrar)) {
                                    g_TexteSeleccionat.Esborrantse = true;
                                }
                                else {
                                    g_TexteSeleccionat.Esborrantse = false;
                                }
                                invalidate();
                            }
                            break;

                    }
                    break;

                case MotionEvent.ACTION_UP:
                    switch (g_ModusDibuix) {
                        case curva:
                            // Deixem de fer curva i tornem a lo que feiem abans
                            g_ModusDibuix = g_ModusDibuixAnterior;
                            break;

                        case recta:
                            if (g_Dibuixant) {
                                l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                            Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                                l_Detector.offset(Math.round(g_mPosX), Math.round(g_mPosY));
                                // Validem que hagui fet una linia o curva llarga (o fem validant que no intersecti amb el punt inicial)
                                if (l_Detector.contains(Math.round(g_PuntInicialLinia.x), Math.round(g_PuntInicialLinia.y))) {
                                    // Hem apretat i res mes (no ens hem desplaçat). Esborrem la linia
                                    g_LiniesPlanol.remove(g_LiniesPlanol.size()-1);
                                    if (g_LiniesPlanol.size() == 0) {
                                        g_PrimerPuntDibuix = null;
                                        g_Punter = null;
                                        g_DetectorIni = null;
                                        g_PuntInicialLinia = null;
                                        g_PuntFinalAnterior = null;
                                        // No he de esborrar mes coses?
                                    }
                                }
                                else {
                                    if (l_Detector.intersect(g_DetectorIni)) {
                                        Log.d("BODINA-TouchUP", "------------------------ Enganxat");
                                        // No "enganxem del tot" per evitar arrosegar o moure dues vegades el mateix punt (el de contacte)
                                        g_LiniesPlanol.get(g_LiniesPlanol.size()-1).Fi = new PointF(g_PrimerPuntDibuix.x+1, g_PrimerPuntDibuix.y+1);
                                        g_Finalitzat = true;
                                    }
                                    else{
                                        // Apuntem el punt per quan continuem
                                        g_PuntFinalAnterior = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                    }
                                }
                                // Netegem linia
                                g_PuntInicialLinia = null;
                            }
                            break;

                        case texte:
                            if (g_TexteSeleccionat != null) {
                                // Validem si l'usuari ens vol esborrar
                                l_Esborrar = new Rect();
                                g_IMB_Esborrar.getHitRect(l_Esborrar);
                                // Movem el detector
                                g_TexteSeleccionat.Detector.offsetTo(Math.round(l_ActualPoint.x + g_mPosX), Math.round(l_ActualPoint.y + g_mPosY));
                                if (g_TexteSeleccionat.Detector.intersect(l_Esborrar)) {
                                    g_TexteSeleccionat.Esborrat = true;
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
            Log.d("BODINA-Touch", "Estem fent un escalat");
        }
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent p_Event) {
            Rect l_DetectorIni;
            texte l_Texte;
            final EditText l_Input;
            float l_X = p_Event.getX();
            float l_Y = p_Event.getY();

            // Sigui lo que sigui, sino marquem un texte recuperem escala per si estem apliats
            switch (g_ModusDibuix) {
                default:
                    l_DetectorIni = new Rect(Math.round(l_X) - 50, Math.round(l_Y) - 50,
                                             Math.round(l_X) + 50, Math.round(l_Y) + 50);
                    l_Texte = MarquemTexte(l_DetectorIni);
                    if (l_Texte != null) {
                        Log.d("BODINA-Down", "--------> Tocat " + l_Texte.Texte);
                        g_TexteSeleccionat = l_Texte;
                        l_Input = new EditText(g_Pare);
                        l_Input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(g_MaxCaractersNom)});
                        // Mostrem una finestra per modificar el texte
                        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(g_Pare);
                        g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITModifyTexte));
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
                        // Recuperem escala i posicio (ho podriem animar!)
                        g_ScaleFactor = 1;
                        g_mPosX = 0;
                        g_mPosY = 0;
                    }
            }
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector p_Detector) {
            g_ScaleFactor *= p_Detector.getScaleFactor();
            Log.d("BODINA-Scale", "Escala canvia " + g_ScaleFactor);
            // Don't let the object get too small or too large.
            g_ScaleFactor = Math.max(0.1f, Math.min(g_ScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

    public void EsborrarPlanol(){
        Log.d("BODINA-TouchUP", "------------------------ Esborrem");
        // Inicialitzem
        g_LiniesPlanol = new ArrayList<>();
        g_TextesPlanol = new ArrayList<>();
        g_Finalitzat = false;
        g_DetectorIni = null;
        g_PrimerPuntDibuix = null;
        g_PuntInicialLinia = null;
        g_PuntFinalAnterior = null;
        g_Punter = null;
        g_IniciDibuix = false;
        g_Dibuixant = false;
        // Recuperem escala i posicio (ho podriem animar!)
        g_ScaleFactor = 1;
        g_mPosX = 0;
        g_mPosY = 0;
        //
        invalidate();
    }

    private double CalculaDistancia(PointF P_Punt1, PointF P_Punt2){
        double l_Part1, l_Part2, l_Resultat;

        l_Part1 = new Float(Math.abs(P_Punt1.x)-Math.abs(P_Punt2.x));
        l_Part2 = new Float(Math.abs(P_Punt1.y)-Math.abs(P_Punt2.y));

        l_Resultat = Math.sqrt(Math.pow(l_Part1, 2) + Math.pow(l_Part2, 2));
        return l_Resultat;
    }

    private String EscalaDistancia(double P_Distancia){
        String l_Resultat = null;

        l_Resultat = String.valueOf(Math.round(P_Distancia / g_UnitatX));
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

    private texte MarquemTextePunt(int P_X, int P_Y){
        texte l_Marcat = null;

        Log.d("BODINA-MarquemTexte2", "g_TextesPlanol.size()");
        for (int i=0; i < g_TextesPlanol.size(); i++){
            Log.d("BODINA-MarquemTexte2", "Rect trobat " + g_TextesPlanol.get(i).Detector.top + ", " + g_TextesPlanol.get(i).Detector.left + " / " + g_TextesPlanol.get(i).Detector.bottom + ", " + g_TextesPlanol.get(i).Detector.right);
            if (g_TextesPlanol.get(i).Detector.contains(P_X, P_Y)){
                l_Marcat = g_TextesPlanol.get(i);
                break;
            }
        }
        return l_Marcat;
    }

    private linia MarquemDistanciaPunt(PointF p_Punt){
        linia l_Marcada = null;

        Log.d("BODINA-MarquemDistancia", "Inici");
        for (int i=0; i < g_LiniesPlanol.size(); i++){
            if (g_LiniesPlanol.get(i).ObjDistancia.Detector.contains(Math.round(p_Punt.x), Math.round(p_Punt.y))){
                Log.d("BODINA-MarquemDistancia", "Trobat " + i);
                l_Marcada = g_LiniesPlanol.get(i);
                break;
            }
        }
        return l_Marcada;
    }


    private void FinestraTexte(final String p_Texte){
        final EditText l_Input;

        l_Input = new EditText(g_Pare);
        // Mostrem una finestra per modificar el texte
        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(g_Pare);
        g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
        g_alertDialogBuilder.setView(l_Input);
        l_Input.setText(p_Texte);
        l_Input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(g_MaxCaractersNom)});
        g_alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        EscriuTexte(l_Input, g_PuntActual);
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

    public texte EscriuTexte(EditText P_TexteDonat, PointF p_PuntDonat){
        // Afegim el texte que ha introduit l'usuari, el posem al mig sino s'indica un altre,
        // el usuari el podra moure
        texte l_Texte = new texte();
        Rect l_Detector = new Rect();
        TextPaint l_TextPaint = P_TexteDonat.getPaint();

        // Definim el detector del texte
        //l_Detector = new Rect(Math.round(p_PuntDonat.x), Math.round(p_PuntDonat.y),
        //                      Math.round(p_PuntDonat.x) + 20, Math.round(p_PuntDonat.y) + 20);
        l_TextPaint.getTextBounds(P_TexteDonat.getText().toString(), 0, P_TexteDonat.getText().length(), l_Detector);
        l_Detector.offsetTo(Math.round(p_PuntDonat.x), Math.round(p_PuntDonat.y));
        // Definim el texte
        l_Texte.Id = g_TextesPlanol.size();
        l_Texte.Detector = l_Detector;
        l_Texte.Texte = P_TexteDonat.getText().toString();
        l_Texte.Punt = p_PuntDonat;
        l_Texte.Esborrat = false;
        l_Texte.Esborrantse = false;
        g_TextesPlanol.add(l_Texte);

        return l_Texte;
    }

    public void DibuixaPlanol(ArrayList<SaloClient.DetallPlanol> p_Planol){
        SaloClient.DetallPlanol l_Element;
        PointF l_PuntInici, l_PuntFi, l_PuntCurva, l_PuntTexte;

        for (int i=0; i < p_Planol.size(); i++){
            l_Element = p_Planol.get(i);
            switch (l_Element.Tipus){
                case 0: // Linia
                    l_PuntInici = new PointF(l_Element.OrigenX, l_Element.OrigenY);
                    l_PuntFi = new PointF(l_Element.DestiX, l_Element.DestiY);
                    if (l_Element.CurvaX != 0){
                        // Curva
                        l_PuntCurva = new PointF(l_Element.CurvaX, l_Element.CurvaY);
                        g_LiniesPlanol.add(DefineixCurva(l_PuntInici, l_PuntFi, l_PuntCurva));
                    }
                    else{
                        g_LiniesPlanol.add(DefineixLinia(l_PuntInici, l_PuntFi));
                    }
                    break;
                case 1: // Texte
                    l_PuntTexte = new PointF(l_Element.OrigenX, l_Element.OrigenY);
                    g_TextesPlanol.add(DefineixTexte(l_Element.Texte, l_PuntTexte));
                    break;
            }
        }
        //
        g_Finalitzat = true;
        invalidate();
    }

    public void Assistent(int p_Amplada, int p_Llargada){
        PointF l_Punt1, l_Punt2, l_Punt3, l_Punt4;

        EsborrarPlanol();
        // Definim les linies que conformem el planol mitjaçant els seus punts
        p_Amplada *= g_UnitatX;
        p_Llargada *= g_UnitatY;

        l_Punt1 = new PointF(g_CenterX - (p_Amplada / 2), g_CenterY - (p_Llargada / 2));
        l_Punt2 = new PointF(g_CenterX + (p_Amplada / 2), g_CenterY - (p_Llargada / 2));
        l_Punt3 = new PointF(g_CenterX + (p_Amplada / 2), g_CenterY + (p_Llargada / 2));
        l_Punt4 = new PointF(g_CenterX - (p_Amplada / 2), g_CenterY + (p_Llargada / 2));
        //
        g_LiniesPlanol.add(DefineixLinia(l_Punt1, l_Punt2));
        g_LiniesPlanol.add(DefineixLinia(l_Punt2, l_Punt3));
        g_LiniesPlanol.add(DefineixLinia(l_Punt3, l_Punt4));
        g_LiniesPlanol.add(DefineixLinia(l_Punt4, l_Punt1));
        //
        g_Finalitzat = true;
        invalidate();
    }

    private linia DefineixLinia(PointF p_Inici, PointF p_Fi){
        linia l_Linia;
        String l_Distancia;
        Rect l_RectDistancia = new Rect();
        PointF l_PuntMig;

        l_Linia = new linia();
        l_Linia.Inici = p_Inici;
        l_Linia.Fi = p_Fi;
        l_Linia.Curva = false;
        l_Distancia = EscalaDistancia(CalculaDistancia(l_Linia.Inici, l_Linia.Fi));
        l_PuntMig = new PointF((l_Linia.Inici.x + l_Linia.Fi.x) / 2, (l_Linia.Inici.y + l_Linia.Fi.y) / 2);
        l_RectDistancia = new Rect(Math.round(l_PuntMig.x) - 15, Math.round(l_PuntMig.y) - 15,
                Math.round(l_PuntMig.x) + 15, Math.round(l_PuntMig.y) + 15);
        l_Linia.ObjDistancia = new linia.metresCurva(l_PuntMig, l_Distancia, l_RectDistancia);

        return l_Linia;
    }

    private linia DefineixCurva(PointF p_Inici, PointF p_Fi, PointF p_Curva){
        linia l_Linia;
        String l_Distancia;
        Rect l_RectDistancia = new Rect();
        PointF l_PuntMig;

        l_Linia = new linia();
        l_Linia.Inici = p_Inici;
        l_Linia.Fi = p_Fi;
        l_Linia.Curva = true;
        l_Linia.PuntCurva = p_Curva;
        l_Distancia = EscalaDistancia(CalculaDistancia(l_Linia.Inici, l_Linia.Fi));
        l_PuntMig = new PointF((l_Linia.Inici.x + l_Linia.Fi.x) / 2, (l_Linia.Inici.y + l_Linia.Fi.y) / 2);
        l_RectDistancia = new Rect(Math.round(l_PuntMig.x) - 15, Math.round(l_PuntMig.y) - 15,
                Math.round(l_PuntMig.x) + 15, Math.round(l_PuntMig.y) + 15);
        l_Linia.ObjDistancia = new linia.metresCurva(l_PuntMig, l_Distancia, l_RectDistancia);

        return l_Linia;
    }

    public texte DefineixTexte(String p_Texte, PointF p_Punt){
        // Afegim el texte que ha introduit l'usuari, el posem al mig sino s'indica un altre,
        // el usuari el podra moure
        texte l_Texte = new texte();
        Rect l_Detector = new Rect();
        EditText l_EditText = new EditText(g_Pare);
        TextPaint l_TextPaint;

        l_EditText.setText(p_Texte);
        l_TextPaint = l_EditText.getPaint();
        // Definim el detector del texte
        l_TextPaint.getTextBounds(l_EditText.getText().toString(), 0, l_EditText.getText().length(), l_Detector);
        l_Detector.offsetTo(Math.round(p_Punt.x), Math.round(p_Punt.y));
        // Definim el texte
        l_Texte.Id = g_TextesPlanol.size();
        l_Texte.Detector = l_Detector;
        l_Texte.Texte = l_EditText.getText().toString();
        l_Texte.Punt = p_Punt;
        l_Texte.Esborrat = false;
        l_Texte.Esborrantse = false;
        g_TextesPlanol.add(l_Texte);

        return l_Texte;
    }

    private int mod(int x, int y){
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
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
