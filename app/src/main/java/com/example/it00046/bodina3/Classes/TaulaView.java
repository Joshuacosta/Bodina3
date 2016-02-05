package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;

/**
 * Amb aquesta clase mostrem la taula de forma grafica
 */
public class TaulaView extends View {

    private Paint g_PaintTaula;
    private TaulaClient g_Taula = new TaulaClient();
    Context g_Context;
    private boolean g_Real = true;
    private int g_Factor = 0;

    public TaulaView(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
    }

    public TaulaView(Context p_Context) {
        super(p_Context);
        g_Context = p_Context;
        setupDrawing();
    }

    private void setupDrawing() {
        g_PaintTaula = new Paint();
        g_PaintTaula.setColor(Color.LTGRAY);
        g_PaintTaula.setAntiAlias(true);
        g_PaintTaula.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int p_w, int p_h, int p_oldw, int p_oldh) {
        super.onSizeChanged(p_w, p_h, p_oldw, p_oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Pintem la taula

        switch (g_Taula.Tipus){
            case TaulaClient.k_TipusRodona:
                if (g_Real) {
                    canvas.drawCircle((g_Taula.AmpladaDiametre / g_Factor), (g_Taula.AmpladaDiametre / g_Factor), (g_Taula.AmpladaDiametre/2) / g_Factor, g_PaintTaula);
                }
                else {
                    canvas.drawCircle(20, 20, 20, g_PaintTaula);
                }
                break;
            case TaulaClient.k_TipusQuadrada:
                if (g_Real) {
                    canvas.drawRect(0, 0, g_Taula.AmpladaDiametre / g_Factor, g_Taula.AmpladaDiametre / g_Factor, g_PaintTaula);
                }
                else {
                    canvas.drawRect(5, 5, 35, 35, g_PaintTaula);
                }
                break;
            case TaulaClient.k_TipusRectangular:
                if (g_Real){
                    canvas.drawRect(0, 0, g_Taula.AmpladaDiametre / g_Factor, g_Taula.Llargada / g_Factor, g_PaintTaula);
                }
                else {
                    // Orientem en funcio de que es mes llarg
                    if (g_Taula.AmpladaDiametre > g_Taula.Llargada) {
                        canvas.drawRect(10, 5, 30, 35, g_PaintTaula);
                    } else {
                        canvas.drawRect(5, 10, 35, 30, g_PaintTaula);
                    }
                }
                break;
        }
    }

    public void ExpresaTaula(TaulaClient p_Taula, boolean p_Real, int p_Factor){
        g_Taula = p_Taula;
        g_Real = p_Real;
        g_Factor = p_Factor;
        // Ens pintem
        this.invalidate();
    }

    public void MouTaula(PointF p_PuntDesti, PointF p_PuntOrigen){
        // Si no hi ha origen simulem que venim de fora
        if (p_PuntOrigen == null){
            this.setX(2500);
        }
        else{
            // !!!!!!!!!!!!!!!!!! Aixo anira en funcio del tipo de taula?
            // En aquest cas crec que si.
            this.setX(p_PuntOrigen.x);
            this.setY(p_PuntOrigen.y);
        }
        this.animate().translationX(p_PuntDesti.x).translationY(p_PuntDesti.y);
    }

    public PointF DonamGuia(){
        // !!!!!!!!!!!!!!!!!! Aixo anira en funcio del tipo de taula?
        PointF l_Punt = new PointF(this.getX() + (g_Taula.AmpladaDiametre/2), this.getY() - (g_Taula.AmpladaDiametre/2));
        return l_Punt;
    }

}
