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
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
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

public class PlanolVista extends RelativeLayout {
    static private float g_mPosX = 0;
    static private float g_mPosY = 0;
    private Path g_drawPath;
    private Paint g_PaintFinal, g_PaintCanvas;
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    static private Rect g_CanvasRect = null;
    static private int g_CenterX = 0, g_CenterY = 0;
    private int g_AmpladaScreen, g_AlsadaScreen;
    static public ArrayList<linia> g_LiniesPlanol = new ArrayList<>();

    public PlanolVista(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        // Definim path de dibuix
        g_drawPath = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        // Definim paint de planol "terminat"
        g_PaintFinal = new Paint();
        g_PaintFinal.setColor(Color.LTGRAY);
        g_PaintFinal.setAlpha(120);
        g_PaintFinal.setStyle(Paint.Style.FILL);
        g_PaintFinal.setAntiAlias(true);
        // Inicialitzem variables estatiques (oju!)
        g_LiniesPlanol = new ArrayList<>();
        g_mPosX = 0;
        g_mPosY = 0;
    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);

        g_CanvasBitmap = Bitmap.createBitmap(p_w, p_h, Bitmap.Config.ARGB_8888);
        g_DrawCanvas = new Canvas(g_CanvasBitmap);
        g_AmpladaScreen = p_w;
        g_AlsadaScreen = p_h;
        g_CenterX = g_AmpladaScreen / 2;
        g_CenterY = g_AlsadaScreen / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        linia l_Linia2;

        canvas.save();
        g_CanvasRect = canvas.getClipBounds();
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        // Pintem rectes i curves
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
        // Pintem el planol
        canvas.drawPath(g_drawPath, g_PaintFinal);
        canvas.scale(0.5f, 0.5f);
        //canvas.translate(g_CenterX, g_CenterY);
        canvas.restore();
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
                case 1: // Texte, res
                    break;
            }
        }
        //
        invalidate();
    }

    private linia DefineixLinia(PointF p_Inici, PointF p_Fi){
        linia l_Linia;

        l_Linia = new linia();
        l_Linia.Inici = p_Inici;
        l_Linia.Fi = p_Fi;
        l_Linia.Curva = false;
        return l_Linia;
    }

    private linia DefineixCurva(PointF p_Inici, PointF p_Fi, PointF p_Curva){
        linia l_Linia;

        l_Linia = new linia();
        l_Linia.Inici = p_Inici;
        l_Linia.Fi = p_Fi;
        l_Linia.Curva = true;
        l_Linia.PuntCurva = p_Curva;
        return l_Linia;
    }
}
