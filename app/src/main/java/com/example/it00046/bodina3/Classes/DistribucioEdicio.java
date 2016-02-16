package com.example.it00046.bodina3.Classes;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.linia;
import com.example.it00046.bodina3.Classes.Feina.llista_taules;
import com.example.it00046.bodina3.Classes.Feina.planol;
import com.example.it00046.bodina3.Classes.Feina.taula;
import com.example.it00046.bodina3.Classes.Feina.texte;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

public class DistribucioEdicio extends RelativeLayout {

    ///////////////////////////////////////////////////
    public DistribucioEdicio Jo = this;
    public Context g_Pare;
    public boolean g_Quadricula = false;
    public boolean g_LiniesTaules = false;
    public String g_EscalaPlanol = "1x1";   // Definim aquest valors per poder fer una preview.
                                            // Així no dona error la execució del onDraw en disseny
    public String g_UnitatsPlanol;
    ////////////////////////////////////////////////
    private static final int g_MaxCaractersNom = 20;
    ////////////////////////////////////////////////
    private int g_PosicioTaula = -1;
    private View g_SeleccioAnterior = null;
    private ScaleGestureDetector g_GestureScale;
    public float g_ScaleFactor = 1;
    static private float g_mPosX = 0;
    static private float g_mPosY = 0;
    private float mLastTouchX = 0;
    private float mLastTouchY = 0;
    private Path g_drawPlanolSalo;
    private Paint g_PaintTaula, g_PaintTaulaBorrantoError, g_PaintPlanol;
    private Paint g_PaintCanvas, g_PaintText, g_PaintQuadricula;
    public boolean g_TaulaDefecteDefinida = false;
    public TaulaClient g_TaulaDefecte = null;
    public int g_CodiSalo = 0;
    static private taula g_TaulaSeleccionada = null;
    private GestureDetector g_GestureDetector;
    private Context g_Context;
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    public enum g_Modus {taula,persona,parella};
    // Inicialment treballem amb modus taula
    static public g_Modus g_ModusDibuix = g_Modus.taula;
    static public int g_CenterX = 0, g_CenterY = 0;
    public int g_UnitatX, g_UnitatY;
    public int g_EscalaPlanolAmplada, g_EscalaPlanolLlargada;
    private int g_AmpladaScreen, g_LlargadaScreen;
    private boolean g_MovemPlanol = false;
    public boolean g_EscalemPlanol = false;
    public PointF g_DarrerPuntTocat = new PointF();
    public ImageButton g_IMB_Esborrar;
    // Planol
    static public planol g_Planol;
    // Linies i textes del planol
    static public ArrayList<linia> g_LiniesPlanol = new ArrayList<>();
    static public ArrayList<texte> g_TextesPlanol = new ArrayList<>();
    // Taules que dibuixem
    static public llista_taules g_TaulesDistribucio = new llista_taules();

    public Rect g_Ditet = null;
    private TaulaView g_TaulaView = new TaulaView(Globals.g_Native);

    public DistribucioEdicio(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
        g_Context = p_Context;
        // Definim la nostra distribucio a la llista de taules
        g_TaulesDistribucio.g_Distribucio = Jo;
        // Definim el gesture detector....
        g_GestureDetector = new GestureDetector(p_Context, new GestureListener());
        // Definim el gesture detector de scala
        g_GestureScale = new ScaleGestureDetector(p_Context, new ScaleListener());
    }

    private void setupDrawing(){
        // Definim path de dibuix
        g_drawPlanolSalo = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        // de Taula
        g_PaintTaula = new Paint();
        g_PaintTaula.setColor(Color.LTGRAY);
        g_PaintTaula.setAntiAlias(true);
        g_PaintTaula.setStrokeWidth(1);
        //g_PaintTaula.setStyle(Paint.Style.STROKE);
        // de Taula esborrant-se o mal posicionada
        g_PaintTaulaBorrantoError = new Paint();
        g_PaintTaulaBorrantoError.setColor(Color.RED);
        g_PaintTaulaBorrantoError.setAntiAlias(true);
        g_PaintTaulaBorrantoError.setStrokeWidth(2);
        g_PaintTaulaBorrantoError.setStyle(Paint.Style.STROKE);
        // de Quadricula
        g_PaintQuadricula = new Paint();
        g_PaintQuadricula.setColor(Color.BLACK);
        g_PaintQuadricula.setStyle(Paint.Style.STROKE);
        g_PaintQuadricula.setAlpha(100);
        g_PaintQuadricula.setStrokeWidth(1);
        // de Planol
        g_PaintPlanol = new Paint();
        g_PaintPlanol.setColor(Color.BLACK);
        g_PaintPlanol.setStyle(Paint.Style.STROKE);
        g_PaintPlanol.setStrokeWidth(2);
        g_PaintPlanol.setAntiAlias(true);
        // de texte
        g_PaintText = new Paint();
        g_PaintText.setTextSize(35);
    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);

        if (p_w > 0 && p_h > 0) {
            g_CanvasBitmap = Bitmap.createBitmap(p_w, p_h, Bitmap.Config.ARGB_8888);
            g_DrawCanvas = new Canvas(g_CanvasBitmap);
            g_AmpladaScreen = p_w;
            g_LlargadaScreen = p_h;
            g_CenterX = g_AmpladaScreen / 2;
            g_CenterY = g_LlargadaScreen / 2;
        }
    }

    @Override
    // En el OnDraw pintem el planol i els textes, i movem les taules si es necesari
    // Tema escala?
    protected void onDraw(Canvas canvas) {
        PointF l_TextePoint, l_TaulaPoint;
        Path l_Quadricula = new Path();
        linia l_Linia2 = new linia();
        RectF l_BoundsPlanol = new RectF();
        float l_Adaptar;

        canvas.save();

        if (g_Planol == null) {
            // ////////////////////////////////////////////////////////////////////////////////////////
            // Recuperem el planol del salo i el gravem a les estructures de treball
            // (Nomes cal fer-ho un cop, el planol no es modifica)
            g_Planol = DAOSalonsClient.LlegirPlanolSalo(g_CodiSalo, g_Context);
            this.CarregaPlanol(g_Planol);
            g_EscalaPlanol = g_Planol.Escala;
            g_UnitatsPlanol = g_Planol.Unitats;
            // Calculem escala i unitats
            String[] l_Valors = g_EscalaPlanol.split("x");
            g_EscalaPlanolAmplada = Integer.valueOf(l_Valors[0]);
            g_UnitatX = Math.round(g_AmpladaScreen / g_EscalaPlanolAmplada);
            g_EscalaPlanolLlargada = Integer.valueOf(l_Valors[1]);
            g_UnitatY = Math.round(g_LlargadaScreen / g_EscalaPlanolLlargada);
            // Informem la llista de taules del factor de separacio de taules (per poder distribuir-les)
            g_TaulesDistribucio.g_SeparacioTaules = g_UnitatX*2;
            // ////////////////////////////////////////////////////////////////////////////////////////
            // Definim la estructura draw del planol
            g_drawPlanolSalo.reset();
            for (int I=0; I < g_LiniesPlanol.size(); I++) {
                l_Linia2 = g_LiniesPlanol.get(I);
                l_Linia2.Inici.offset(g_mPosX, g_mPosY);
                // Si estem a la primera linia ens posicionem "al principi" amb un move
                if (I == 0) {
                    g_drawPlanolSalo.moveTo(l_Linia2.Inici.x, l_Linia2.Inici.y);
                }
                // Pintem el punt final
                l_Linia2.Fi.offset(g_mPosX, g_mPosY);
                // Validem si es una curva
                if (l_Linia2.Curva) {
                    l_Linia2.PuntCurva.offset(g_mPosX, g_mPosY);
                    g_drawPlanolSalo.quadTo(l_Linia2.PuntCurva.x, l_Linia2.PuntCurva.y, l_Linia2.Fi.x, l_Linia2.Fi.y);
                    // Si s'ha mogut tambe hem de modificar la posicio
                    if (l_Linia2.ObjDistancia.Mogut){
                        l_Linia2.ObjDistancia.Punt.offset(g_mPosX, g_mPosY);
                    }
                }
                else {
                    g_drawPlanolSalo.lineTo(l_Linia2.Fi.x, l_Linia2.Fi.y);
                }
            }
            g_drawPlanolSalo.computeBounds(l_BoundsPlanol, true);
            if (l_BoundsPlanol.width() > l_BoundsPlanol.height()) {
                l_Adaptar = l_BoundsPlanol.width() / l_BoundsPlanol.height();
            } else {
                l_Adaptar = l_BoundsPlanol.height() / l_BoundsPlanol.width();
            }
            //
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(l_Adaptar, l_Adaptar, l_BoundsPlanol.centerX(), l_BoundsPlanol.centerY());
            g_drawPlanolSalo.transform(scaleMatrix);
            // Informe a la distribucio del planol dels bounds del salo (tornem a recalcular els bounds
            // per l'adaptacio feta)
            g_drawPlanolSalo.computeBounds(l_BoundsPlanol, true);
            g_TaulesDistribucio.g_BoundsSalo = l_BoundsPlanol;
        }

        if (g_EscalemPlanol) {

            Log.d("BOD-DistribucioEdicio", "------------------------ Scale desde fora " + g_ScaleFactor);

            if (g_DarrerPuntTocat == null){
                g_DarrerPuntTocat.x = g_CenterX;
                g_DarrerPuntTocat.y = g_CenterY;
            }
            canvas.scale(g_ScaleFactor, g_ScaleFactor, g_DarrerPuntTocat.x, g_DarrerPuntTocat.y);
        }
        else {
            canvas.scale(g_ScaleFactor, g_ScaleFactor);
        }
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Quadricula (la pintem si es activa)
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
        //

        Log.d("BOD-DistribucioEdicio", "------------------------ ??????? " + g_mPosX + ", " + g_mPosY);

        //if (!g_EscalemPlanol) {
        //    g_drawPlanolSalo.offset(Math.round(g_mPosX), Math.round(g_mPosY));
        //}
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem planol
        g_drawPlanolSalo.offset(Math.round(g_mPosX), Math.round(g_mPosY));
        canvas.drawPath(g_drawPlanolSalo, g_PaintPlanol);
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem textes
        for (int k=0; k < g_TextesPlanol.size(); k++) {
            l_TextePoint = g_TextesPlanol.get(k).Punt;
            l_TextePoint.offset(g_mPosX, g_mPosY);
            canvas.drawText(g_TextesPlanol.get(k).Texte,
                                l_TextePoint.x,
                                l_TextePoint.y,
                                g_PaintText);
        }
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Pintem taules
        for (int l=0; l < g_TaulesDistribucio.Tamany(); l++){
            // Validem que la taula no estigui esborrada
            if (g_TaulesDistribucio.element(l).Esborrat == false) {
                /*
                g_TaulesDistribucio.element(l).Punt.offset(Math.round(g_mPosX), Math.round(g_mPosY));
                // Validant si volen esborrar-la
                if (g_TaulesDistribucio.element(l).Esborrantse == false) {
                    g_TaulesDistribucio.element(l).draw(canvas, g_PaintTaula, g_UnitatX);
                }
                else {
                    g_TaulesDistribucio.element(l).draw(canvas, g_PaintTaulaBorrantoError, g_UnitatX);
                }
                // Movem el Detector per si s'ha desplaçat el canvas
                g_TaulesDistribucio.element(l).Detector.offset(Math.round(g_mPosX), Math.round(g_mPosY));

                canvas.drawRect(g_TaulesDistribucio.element(l).Detector, g_PaintPlanol);
                */
            }
        }



        //
        if (g_Ditet != null)
            canvas.drawRect(g_Ditet, g_PaintTaulaBorrantoError);

        //

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent p_Event) {
        float l_X = p_Event.getX()/g_ScaleFactor;
        float l_Y = p_Event.getY()/g_ScaleFactor;
        taula l_Taula;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_Detector, l_Esborrar;

        // Validem primer si hi han mes "gestos"?: doble tap es recuperar posicio inicial
        g_GestureDetector.onTouchEvent(p_Event);
        if (g_ModusDibuix == g_Modus.taula) {
            //g_GestureScale.onTouchEvent(p_Event);
        }
        if (!g_GestureScale.isInProgress()){
            g_EscalemPlanol = false;
            // Continuem
            final int action = p_Event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    g_mPosX = 0;
                    g_mPosY = 0;
                    switch (g_ModusDibuix) {
                        case taula:

                            Log.d("BOD-DistribucioEdicio", "------------------------ Toco a " + Math.round(l_ActualPoint.x) + ", "  + Math.round(l_ActualPoint.y));

                            l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);

                            g_Ditet = new Rect(Math.round(l_ActualPoint.x) - 10, Math.round(l_ActualPoint.y) - 10,
                                               Math.round(l_ActualPoint.x) + 10, Math.round(l_ActualPoint.y) + 10);

                            // Validem que no tinguem marcada una taula
                            if (g_TaulaSeleccionada == null) {
                                l_Taula = MarquemTaula(l_Detector);
                                if (l_Taula != null) {
                                    g_TaulaSeleccionada = l_Taula;
                                    invalidate();
                                }
                                else {
                                    // Hem marcat en el planol, apuntem posicio per si
                                    // l'usuari vol moure el planol
                                    mLastTouchX = l_X;
                                    mLastTouchY = l_Y;
                                    g_MovemPlanol = true;
                                }
                            }
                            else{
                                // Tenim una taula seleccionada, validem si l'usuari l'esta marcant,
                                if (g_TaulaSeleccionada.Detector.intersect(l_Detector)){
                                    ;// Ok, la volem moure
                                }
                                else{
                                    // No toquem cap taula i teniem una marcada, la desmarquem i
                                    // apuntem posicio per si l'usuari vol moure el planol
                                    g_TaulesDistribucio.DesmarcarActives();
                                    g_TaulaSeleccionada = null;
                                    mLastTouchX = l_X;
                                    mLastTouchY = l_Y;
                                    g_MovemPlanol = true;
                                }
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
                        case taula:

                            //g_TaulaView.setX(l_X);
                            //g_TaulaView.setY(l_Y);

                            if (g_TaulaSeleccionada != null){
                            //if (g_TaulaSeleccionada == null){

                                g_TaulaView.setX(l_ActualPoint.x);
                                g_TaulaView.setY(l_ActualPoint.y);

                                /*
                                // Movem el punt que posiciona la taula i el detector
                                g_TaulesDistribucio.element(g_TaulaSeleccionada.Id).Punt = new PointF(l_ActualPoint.x, l_ActualPoint.y);
                                g_TaulesDistribucio.element(g_TaulaSeleccionada.Id).Detector.offsetTo(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y));
                                // Validem si ens volen esborrar
                                l_Esborrar = new Rect();
                                g_IMB_Esborrar.getHitRect(l_Esborrar);
                                if (g_TaulaSeleccionada.Detector.intersect(l_Esborrar)) {
                                    g_TaulaSeleccionada.Esborrantse = true;
                                }
                                else {
                                    g_TaulaSeleccionada.Esborrantse = false;
                                }
                                invalidate();
                                */
                            }
                            else{
                                if (g_MovemPlanol){
                                    // L'usuari està desplaçant el planol amb el dit
                                    final float dx = l_X - mLastTouchX;
                                    final float dy = l_Y - mLastTouchY;
                                    g_mPosX = dx;
                                    g_mPosY = dy;
                                    // Guardem la darrera posicio
                                    mLastTouchX = l_X;
                                    mLastTouchY = l_Y;
                                    // Movem els detectors si estem desplazant amb el dit i enacara estem dibuixant?
                                    // Tenim detectors de treball?
                                    //

                                    for (int l=0; l < g_TaulesDistribucio.Tamany(); l++){
                                        // Validem que la taula no estigui esborrada
                                        if (g_TaulesDistribucio.element(l).Esborrat == false) {
                                            g_TaulaView.setX(g_TaulaView.getX()+g_mPosX);
                                            g_TaulaView.setY(g_TaulaView.getY() + g_mPosY);
                                            Log.d("BOD-DistribucioEdicio", "------------------------ Pintem... ");
                                        }
                                    }

                                    // Crec que amb el for anterior aixó es innecesari
                                    //invalidate();
                                    postInvalidate();
                                }
                            }
                            break;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    switch (g_ModusDibuix) {
                        case taula:
                            g_MovemPlanol = false;
                            g_EscalemPlanol = false;
                            g_DarrerPuntTocat = l_ActualPoint;
                            invalidate();
                            break;
                    }
                    // De moment no hi ha res que ho necessiti
                    //invalidate();
                    break;

                default:
                    // Validem la resta de events amb el Gesture
                    return g_GestureDetector.onTouchEvent(p_Event);
            }
        }
        else{
            g_EscalemPlanol = true;
        }
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent p_Event) {
            // Sigui lo que sigui, de moment recuperem posicio
            switch (g_ModusDibuix) {
                default:
                    // Si hem marcat una taula la maximitzem per treballar amb ella
                    if (g_TaulaSeleccionada != null){
                        Log.d("BOD-DistribucioEdicio", "------------------------ Funciona!");
                        /*
                        Animation animFadein;
                        animFadein = AnimationUtils.loadAnimation(Globals.g_Native.getApplicationContext(), R.anim.zoom_in);
                        Jo.startAnimation(animFadein);
                        */
                        g_ScaleFactor = 10;
                        g_mPosX = g_TaulaSeleccionada.Punt.x;
                        g_mPosY = g_TaulaSeleccionada.Punt.y;
                    }
                    else {
                        // Recuperem escala i posicio (ho podriem animar!)
                        /*
                        g_ScaleFactor = 1;
                        g_mPosX = 0;
                        g_mPosY = 0;
                        invalidate();
                        */
                        Animation animFadein;
                        animFadein = AnimationUtils.loadAnimation(Globals.g_Native.getApplicationContext(), R.anim.zoom_in);
                        g_TaulaView.startAnimation(animFadein);
                    }
            }
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector p_Detector) {
            g_ScaleFactor *= p_Detector.getScaleFactor();

            Log.d("BOD-DistribucioEdicio", "------------------------ ScaleReal " + g_ScaleFactor + " (" + p_Detector.getScaleFactor() + ")");
            // No deixem que es faci massa gran
            g_ScaleFactor = Math.max(0.1f, Math.min(g_ScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

    private taula MarquemTaula(Rect P_Posicio){
        taula l_Marcat = null;

        g_TaulesDistribucio.DesmarcarActives();

        for (int i=0; i < g_TaulesDistribucio.Tamany(); i++){
            if (P_Posicio.intersect(g_TaulesDistribucio.element(i).Detector)){
                l_Marcat = g_TaulesDistribucio.element(i);
                g_TaulesDistribucio.Activar(i);
                break;
            }
        }
        return l_Marcat;
    }

    public void PosaTaula(PointF p_PuntDonat, TaulaClient P_Taula){
        taula l_Taula = new taula(true);
        PointF l_PosicioTaula;

        if (p_PuntDonat == null){
            // Si el punt no es definit el posem en el centre
            l_PosicioTaula = new PointF(Math.round(g_CenterX), Math.round(g_CenterY));
        }
        else{
            l_PosicioTaula = p_PuntDonat;
        }
        // Definim la taula a la distribucio de taules
        l_Taula.Id = g_TaulesDistribucio.Tamany();
        l_Taula.Punt = l_PosicioTaula;
        l_Taula.Esborrat = false;
        l_Taula.Esborrantse = false;
        l_Taula.Taula = P_Taula;
        g_TaulesDistribucio.Afegir(l_Taula);
        // La dibuixem
        /*
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 40;
        params.height = 40;
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        g_TaulaView.ExpresaTaula(P_Taula, true);

        Jo.addView(g_TaulaView, params);
        */
    }

    public void MoureTaula(){

        if (g_TaulaSeleccionada != null) {

            ObjectAnimator animation2 = ObjectAnimator.ofFloat(g_TaulaSeleccionada, "x", 50);
            animation2.setDuration(2000);
            animation2.start();

            /*
            g_TaulaSeleccionada.animate().translationX(100).withStartAction(new Runnable(){
                public void run(){
                    viewer.setTranslationX(100-myView.getWidth());
                    // do something
                }
            });
            */
        }
    }

    public void CarregaPlanol(planol p_Planol){
        SaloClient.DetallPlanol l_Element;
        PointF l_PuntInici, l_PuntFi, l_PuntCurva, l_PuntTexte;

        for (int i=0; i < p_Planol.Tamany(); i++){
            l_Element = p_Planol.Llegeix(i);
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
    }

    public void Assistent(int p_Amplada, int p_Llargada){
        PointF l_Punt1, l_Punt2, l_Punt3, l_Punt4;

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
        l_Distancia = "";
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
        l_Distancia = "";
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