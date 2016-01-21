package com.example.it00046.bodina3.Classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTaulesClient;
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
    static private float g_mPosX = 0;
    static private float g_mPosY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    private Path g_drawPath;
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
    public enum g_Modus {taula,persona,parella,moure};
    static public g_Modus g_ModusDibuix = g_Modus.taula;
    static private Rect g_CanvasRect = null;
    static private int g_CenterX = 0, g_CenterY = 0;
    public int g_UnitatX, g_UnitatY;
    public int g_EscalaPlanolAmplada, g_EscalaPlanolLlargada;
    private int g_AmpladaScreen, g_LlargadaScreen;
    public ImageButton g_IMB_Esborrar;
    // Planol
    static public planol g_Planol;
    // Linies i textes del planol
    static public ArrayList<linia> g_LiniesPlanol = new ArrayList<>();
    static public ArrayList<texte> g_TextesPlanol = new ArrayList<>();
    // Taules que dibuixem
    static public llista_taules g_TaulesDistribucio = new llista_taules();

    public DistribucioEdicio(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
        g_Context = p_Context;

        // Definim el gesture detector.... QUE FEM AMB EL DOBLE TAP?
        //g_GestureDetector = new GestureDetector(p_Context, new GestureListener());

        // Definim el gesture detector de scala
        g_GestureScale = new ScaleGestureDetector(p_Context, new ScaleListener());
    }

    private void setupDrawing(){
        // Definim path de dibuix
        g_drawPath = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        // Definim paint de taula
        g_PaintTaula = new Paint();
        g_PaintTaula.setColor(Color.LTGRAY);
        g_PaintTaula.setAntiAlias(true);
        g_PaintTaula.setStrokeWidth(5);
        // Definim paint de taula esborrant-se o mal posicionada
        g_PaintTaulaBorrantoError = new Paint();
        g_PaintTaulaBorrantoError.setColor(Color.RED);
        g_PaintTaulaBorrantoError.setAntiAlias(true);
        g_PaintTaulaBorrantoError.setStrokeWidth(7);
        // Definim paint de quadricula
        g_PaintQuadricula = new Paint();
        g_PaintQuadricula.setColor(Color.BLACK);
        g_PaintQuadricula.setAlpha(100);
        g_PaintQuadricula.setStrokeWidth(1);
        g_PaintQuadricula.setStyle(Paint.Style.STROKE);
        // Definim paint de planol "terminat"
        g_PaintPlanol = new Paint();
        g_PaintPlanol.setColor(Color.BLACK);
        g_PaintPlanol.setAntiAlias(true);
        // Definim el paint de texte
        g_PaintText = new Paint();
        g_PaintText.setTextSize(35);
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
        PointF l_TextePoint, l_TaulaPoint;
        Path l_Quadricula = new Path();
        String l_Distancia;
        Rect l_RectDistancia = new Rect();
        linia l_Linia2 = new linia();
        PointF l_PuntMig;

        if (g_Planol == null) {
            // Recuperem el planol del salo i el gravem a les estructures de treball
            // (Nomes cal fer-ho un cop)
            g_Planol = DAOSalonsClient.LlegirPlanolSalo(g_CodiSalo, g_Context);
            CarregaPlanol(g_Planol);
            g_EscalaPlanol = g_Planol.Escala;
            g_UnitatsPlanol = g_Planol.Unitats;
        }
        canvas.save();
        // Calculem escala i unitats
        String[] l_Valors = g_EscalaPlanol.split("x");
        g_EscalaPlanolAmplada = Integer.valueOf(l_Valors[0]);
        g_UnitatX = Math.round(g_AmpladaScreen / g_EscalaPlanolAmplada);
        g_EscalaPlanolLlargada = Integer.valueOf(l_Valors[1]);
        g_UnitatY = Math.round(g_LlargadaScreen / g_EscalaPlanolLlargada);
        canvas.scale(g_ScaleFactor, g_ScaleFactor);
        g_CanvasRect = canvas.getClipBounds();
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
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
        // Rectes i curves
        g_drawPath.reset();
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
        canvas.drawPath(g_drawPath, g_PaintPlanol);
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Textes
        for (int k=0; k < g_TextesPlanol.size(); k++) {
            // Validem si el texte l'han esborrat
            if (g_TextesPlanol.get(k).Esborrat == false) {
                // Movem el punt
                l_TextePoint = g_TextesPlanol.get(k).Punt;
                l_TextePoint.offset(g_mPosX, g_mPosY);
                canvas.drawText(g_TextesPlanol.get(k).Texte,
                                l_TextePoint.x,
                                l_TextePoint.y,
                                g_PaintText);
                // Movem el Detector per si s'ha desplaçat el canvas
                g_TextesPlanol.get(k).Detector.offset(Math.round(g_mPosX), Math.round(g_mPosY));
            }
        }
        // ///////////////////////////////////////////////////////////////////////////////////////
        // Taules
        for (int l=0; l < g_TaulesDistribucio.Tamany(); l++){
            // Validem que la taula no estigui esborrada
            if (g_TaulesDistribucio.element(l).Esborrat == false) {
                l_TaulaPoint = g_TaulesDistribucio.element(l).Punt;
                l_TaulaPoint.offset(g_mPosX, g_mPosY);
                // Validant si volen esborrar-la
                if (g_TaulesDistribucio.element(l).Esborrantse == false) {
                    g_TaulesDistribucio.element(l).draw(canvas, g_PaintTaula);
                } else {
                    g_TaulesDistribucio.element(l).draw(canvas, g_PaintTaulaBorrantoError);
                }
                // Movem el Detector per si s'ha desplaçat el canvas
                g_TaulesDistribucio.element(l).Detector.offset(Math.round(g_mPosX), Math.round(g_mPosY));
            }
        }
        //
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent p_Event) {
        float l_X = p_Event.getX()/g_ScaleFactor;
        float l_Y = p_Event.getY()/g_ScaleFactor;
        texte l_Texte;
        taula l_Taula;
        PointF l_ActualPoint = new PointF(l_X, l_Y);
        Rect l_Detector, l_Esborrar;
        linia l_Linia = new linia();

        // Validem primer si hi han "gestos": doble tap
        g_GestureDetector.onTouchEvent(p_Event);
        if (g_ModusDibuix == g_Modus.moure) {
            g_GestureScale.onTouchEvent(p_Event);
        }
        if (!g_GestureScale.isInProgress()){
            // Continuem
            final int action = p_Event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    g_mPosX = 0;
                    g_mPosY = 0;
                    switch (g_ModusDibuix) {
                        case moure:
                            mLastTouchX = l_X;
                            mLastTouchY = l_Y;
                            break;

                        case taula:
                            l_Detector = new Rect(Math.round(l_ActualPoint.x) - 30, Math.round(l_ActualPoint.y) - 30,
                                    Math.round(l_ActualPoint.x) + 30, Math.round(l_ActualPoint.y) + 30);
                            // Validem que no tinguem marcada una taula
                            if (g_TaulaSeleccionada == null) {
                                l_Taula = MarquemTaula(l_Detector);
                                if (l_Taula != null) {
                                    g_TaulaSeleccionada = l_Taula;
                                }
                                else {
                                    g_TaulaSeleccionada = null;
                                    // Afegim una taula a la distribucio si treballem amb taula per defecte
                                    if (g_TaulaDefecteDefinida) {
                                        PosaTaula(l_ActualPoint, g_TaulaDefecte);
                                    }
                                    else{
                                        // Hem de preguntar quina taula posem
                                        FinestraTaules(l_ActualPoint);
                                    }
                                }
                            }
                            else{
                                // Tenim una taula seleccionada, validem si l'usuari esta marcant
                                // el moviment, sino, desmarcarem la taula.
                                if (g_TaulaSeleccionada.DetectorButo.intersect(l_Detector)){
                                    ;// Ok
                                }
                                else{
                                    g_TaulesDistribucio.DesmarcarActives();
                                    g_TaulaSeleccionada = null;
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
                        case moure:
                            // L'usuari està desplaçant el planol amb el dit
                            final float dx = l_X - mLastTouchX;
                            final float dy = l_Y - mLastTouchY;
                            g_mPosX = dx;
                            g_mPosY = dy;
                            // Guardem la darrera posicio
                            mLastTouchX = l_X;
                            mLastTouchY = l_Y;
                            // Movem els detectors si estem desplazant amb el dit i enacara estem dibuixant
                            invalidate();
                            break;

                        case taula:
                            if (g_TaulaSeleccionada != null) {
                                // Movem el punt que posiciona el texte i el detector
                                g_TaulesDistribucio.element(g_TaulaSeleccionada.Id).Punt = new PointF(l_ActualPoint.x, l_ActualPoint.y - 50);
                                g_TaulesDistribucio.element(g_TaulaSeleccionada.Id).Detector.offsetTo(Math.round(l_ActualPoint.x), Math.round(l_ActualPoint.y) + 20);
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
                            }
                            break;

                    }
                    break;

                case MotionEvent.ACTION_UP:
                    switch (g_ModusDibuix) {
                        case moure:
                            break;
                    }
                    invalidate();
                    break;

                default:
                    // Validem la resta de events amb el Gesture
                    return g_GestureDetector.onTouchEvent(p_Event);
            }
        }
        return true;
    }

    /*

       QUE FEM AMB EL DOBRE TAP?

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
    */

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector p_Detector) {
            g_ScaleFactor *= p_Detector.getScaleFactor();
            // Don't let the object get too small or too large.
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

    // Mostrem les taules que pot triar per disposar a la distribucio (on ha pres)
    private void FinestraTaules(final PointF p_Punt) {
        // Mostrem la finestra de configuracio
        final Spinner l_SPN_Taules;
        final ListView g_LVW_Taules;
        final CheckBox l_CBX_TaulaDefecte;
        ArrayAdapter<CharSequence> l_adapter_Taules;
        //LayoutInflater inflater = (LayoutInflater) g_Pare.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        LayoutInflater inflater = LayoutInflater.from(g_Pare);
        View l_VIW_Config = inflater.inflate(R.layout.distribucions_client_mant_dialog_config, null);

        // CheckBox
        l_CBX_TaulaDefecte = (CheckBox) l_VIW_Config.findViewById(R.id.DistribucionsClientMantDialogTaulesCBXTaulaDefecte);
        // ListView: carreguen les taules del client
        g_LVW_Taules = (ListView) l_VIW_Config.findViewById(R.id.DistribucionsClientMantDialogConfigLVWTaules);
        DAOTaulesClient.Llegir(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client, g_Pare);
        //
        AlertDialog.Builder g_DialogConfiguracio = new AlertDialog.Builder(g_Pare);
        g_DialogConfiguracio.setTitle(Globals.g_Native.getString(R.string.DistribucionsClientMantLABConfiguracioTriaTaula));
        g_DialogConfiguracio.setView(l_VIW_Config);
        g_DialogConfiguracio
                .setCancelable(false)
                .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        // Recuperem taula triada
                        TaulaClient l_TaulaTriada = (TaulaClient)g_LVW_Taules.getSelectedItem();
                        if (l_TaulaTriada != null) {
                            PosaTaula(p_Punt, l_TaulaTriada);
                            // Llegim configuracio definida
                            if (l_CBX_TaulaDefecte.isChecked()) {
                                g_TaulaDefecteDefinida = true;
                                g_TaulaDefecte = l_TaulaTriada;
                            }
                            invalidate();
                        }
                        else {
                            // No s'ha triat cap taula, no fem res
                            // AVISEM?
                        }
                    }
                })
                .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface p_dialog, int p_id) {
                    }
                });
        g_DialogConfiguracio.show();
    }

    public void PosaTaula(PointF p_PuntDonat, TaulaClient P_Taula){
        taula l_Taula = new taula(true);
        Rect l_Detector = new Rect();

        // Desmarco si hi ha alguna ctiva
        g_TaulesDistribucio.DesmarcarActives();
        // Definim el detector de la taula
        l_Detector = new Rect(Math.round(p_PuntDonat.x), Math.round(p_PuntDonat.y),
                              Math.round(p_PuntDonat.x) + 30, Math.round(p_PuntDonat.y) + 30);
        // Definim la taula
        l_Taula.Id = g_TaulesDistribucio.Tamany();
        l_Taula.Detector = l_Detector;
        l_Taula.Punt = p_PuntDonat;
        l_Taula.Esborrat = false;
        l_Taula.Esborrantse = false;
        l_Taula.Taula = P_Taula;
        g_TaulesDistribucio.Afegir(l_Taula);
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
        //
        invalidate();
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