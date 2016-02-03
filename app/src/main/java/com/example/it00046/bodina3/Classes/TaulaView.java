package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
        float l_Factor;

        //canvas.save();
        //g_CanvasRect = canvas.getClipBounds();
        //canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        // Calculo factor de mida
        if (g_Taula.AmpladaDiametre > g_Taula.Llargada){
            l_Factor = g_Taula.AmpladaDiametre / 50;
        }
        else{
            l_Factor = g_Taula.Llargada / 50;
        }
        l_Factor *=2;
        // Pintem la taula

        Log.d("BODINA", "----------------------------------- Tipus que expreso " + g_Taula.Tipus);

        // Per no liar-nos amb els tamanys que mostrem i tal lo que fem es dibuixar el tipus de forma generica
        // i mostrem a sota el seu tamany.
        switch (g_Taula.Tipus){
            case TaulaClient.k_TipusRodona:
                //canvas.drawCircle(50, 50, g_Taula.AmpladaDiametre / l_Factor, g_PaintTaula);
                canvas.drawCircle(20, 20, 20, g_PaintTaula);
                break;
            case TaulaClient.k_TipusQuadrada:
                //canvas.drawRect(50, 50, g_Taula.AmpladaDiametre / l_Factor, g_Taula.AmpladaDiametre / l_Factor, g_PaintTaula);
                canvas.drawRect(5, 5, 35, 35, g_PaintTaula);
                break;
            case TaulaClient.k_TipusRectangular:
                // Orientem en funcio de que es mes llarg
                if (g_Taula.AmpladaDiametre > g_Taula.Llargada){
                    canvas.drawRect(10, 5, 30, 35, g_PaintTaula);
                }
                else{
                    canvas.drawRect(5, 10, 35, 30, g_PaintTaula);
                }
                //canvas.drawRect(50, 50, g_Taula.AmpladaDiametre / l_Factor, g_Taula.Llargada / l_Factor, g_PaintTaula);
                break;
        }
        //canvas.restore();
    }

    public void ExpresaTaula(TaulaClient p_Taula){
        g_Taula = p_Taula;
        this.invalidate();
    }
}
