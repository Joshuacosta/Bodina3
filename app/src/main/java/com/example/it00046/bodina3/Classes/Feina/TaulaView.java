package com.example.it00046.bodina3.Classes.Feina;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.taula;
import com.example.it00046.bodina3.Classes.Globals;

/**
 * Amb aquesta clase mostrem la taula de forma grafica
 */
public class TaulaView extends View {
    private Paint g_PaintTaula, g_PaintTaulaActiva, g_PaintText;
    private TaulaClient g_Taula = new TaulaClient();
    Context g_Context;
    private boolean g_Activa = false;
    private int g_NumTaula = 0;
    private int g_Factor = 0;
    private int g_Width, g_Height;
    //
    private PointF Punt = new PointF();
    private PointF PuntInicial = null;

    public TaulaView(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
    }

    public TaulaView(Context p_Context, PointF p_Punt) {
        super(p_Context);
        g_Context = p_Context;
        DefineixPunt(p_Punt);
        setupDrawing();
    }

    public TaulaView(Context p_Context) {
        super(p_Context);
        g_Context = p_Context;
        setupDrawing();
    }

    public TaulaView(){
        super(Globals.g_Native);
        g_Context = Globals.g_Native;
        setupDrawing();
    }

    private void setupDrawing() {
        g_PaintTaula = new Paint();
        g_PaintTaula.setColor(Color.LTGRAY);
        g_PaintTaula.setAntiAlias(true);
        g_PaintTaula.setStrokeWidth(5);
        //
        g_PaintTaulaActiva = new Paint();
        g_PaintTaulaActiva.setColor(Color.GREEN);
        g_PaintTaulaActiva.setAntiAlias(true);
        g_PaintTaulaActiva.setStrokeWidth(2);
        //
        g_PaintText = new Paint();
        g_PaintText.setTextSize(16);
        g_PaintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        g_PaintText.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);
    }

    public void Dibuixa(Canvas p_Canvas){
        switch (g_Taula.Tipus) {
            case TaulaClient.k_TipusRodona:
                if (g_Activa) {
                    p_Canvas.drawCircle(Punt.x + ((g_Taula.AmpladaDiametre / 2) / g_Factor), Punt.y + ((g_Taula.AmpladaDiametre / 2) / g_Factor), (g_Taula.AmpladaDiametre / 2) / g_Factor, g_PaintTaulaActiva);
                }
                else{
                    p_Canvas.drawCircle(Punt.x + ((g_Taula.AmpladaDiametre / 2) / g_Factor), Punt.y + ((g_Taula.AmpladaDiametre / 2) / g_Factor), (g_Taula.AmpladaDiametre / 2) / g_Factor, g_PaintTaula);
                }
                g_Width = g_Height = g_Taula.AmpladaDiametre / g_Factor;
                break;
            case TaulaClient.k_TipusQuadrada:
                if (g_Activa) {
                    p_Canvas.drawRect(Punt.x, Punt.y, Punt.x + g_Taula.AmpladaDiametre / g_Factor, Punt.y + g_Taula.AmpladaDiametre / g_Factor, g_PaintTaulaActiva);
                } else {
                    p_Canvas.drawRect(Punt.x, Punt.y, Punt.x + g_Taula.AmpladaDiametre / g_Factor, Punt.y + g_Taula.AmpladaDiametre / g_Factor, g_PaintTaula);
                }
                break;
            case TaulaClient.k_TipusRectangular:
                if (g_Activa) {
                    p_Canvas.drawRect(Punt.x, Punt.y, Punt.x + g_Taula.AmpladaDiametre / g_Factor, Punt.y + g_Taula.Llargada / g_Factor, g_PaintTaulaActiva);
                }
                else{
                    p_Canvas.drawRect(Punt.x, Punt.y, Punt.x + g_Taula.AmpladaDiametre / g_Factor, Punt.y + g_Taula.Llargada / g_Factor, g_PaintTaula);
                }
                break;
        }
        // Numero de taula
        p_Canvas.drawText(Integer.toString(g_NumTaula), Punt.x +2 , Punt.y +2, g_PaintText);
    }

    public void DefineixPunt(PointF p_Punt){
        Punt = p_Punt;
        if (PuntInicial == null) {
            PuntInicial = p_Punt;
        }
    }

    public void OffSet(float p_OffX, float p_OffY){
        Punt.offset(p_OffX, p_OffY);
    }

    public PointF Punt(){
        return Punt;
    }

    public PointF PuntInicial(){
        return PuntInicial;
    }

    public void ExpresaTaula(TaulaClient p_Taula, int p_Factor, boolean p_Activa, int p_NumTaula){
        g_Taula = p_Taula;
        g_Factor = p_Factor;
        g_Activa = p_Activa;
        g_NumTaula = p_NumTaula;
    }

    public void Activacio(boolean p_Activa){
        g_Activa = p_Activa;
        // Ens pintem
        invalidate();
    }

    public boolean Tocada(Rect p_Punt){
        // Validem si estem dintre de l'area marcada
        return p_Punt.contains(Math.round(Punt.x + (g_Taula.AmpladaDiametre / g_Factor)), Math.round(Punt.y + (g_Taula.AmpladaDiametre / g_Factor)));
    }

    public boolean Posicionament(Rect p_Punt){
        // Validem si estem dintre de l'area marcada
        return p_Punt.contains(Math.round(Punt.x), Math.round(Punt.y));
    }


    public void MouTaula(PointF p_PuntDesti){
        animate().translationX(p_PuntDesti.x).translationY(p_PuntDesti.y);
    }

    public void PosaTaula(PointF p_PuntDesti){
        // Amb aixo simulem que ve de fora
        setX(2500);
        animate().translationX(p_PuntDesti.x).translationY(p_PuntDesti.y);
    }

    public PointF DonamGuia(){
        // !!!!!!!!!!!!!!!!!! Aixo anira en funcio del tipo de taula?
        PointF l_Punt = new PointF(getX() + (g_Taula.AmpladaDiametre/2), getY() - (g_Taula.AmpladaDiametre/2));
        return l_Punt;
    }

}
