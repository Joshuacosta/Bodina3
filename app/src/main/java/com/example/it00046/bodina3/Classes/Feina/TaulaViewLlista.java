package com.example.it00046.bodina3.Classes.Feina;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Globals;

/**
 * Amb aquesta clase mostrem la taula de forma grafica a les llistes
 */
public class TaulaViewLlista extends View {
    private Paint g_PaintTaula;
    private TaulaClient g_Taula = new TaulaClient();
    Context g_Context;

    public TaulaViewLlista(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
    }

    public TaulaViewLlista(Context p_Context) {
        super(p_Context);
        g_Context = p_Context;
        setupDrawing();
    }

    public TaulaViewLlista(){
        super(Globals.g_Native);
        g_Context = Globals.g_Native;
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
                canvas.drawCircle(20, 20, 20, g_PaintTaula);
                break;
            case TaulaClient.k_TipusQuadrada:
                canvas.drawRect(5, 5, 35, 35, g_PaintTaula);
                break;
            case TaulaClient.k_TipusRectangular:
                // Orientem en funcio de que es mes llarg
                if (g_Taula.AmpladaDiametre > g_Taula.Llargada) {
                    canvas.drawRect(10, 5, 30, 35, g_PaintTaula);
                } else {
                    canvas.drawRect(5, 10, 35, 30, g_PaintTaula);
                }
                break;
        }
    }

    public void ExpresaTaula(TaulaClient p_Taula){
        g_Taula = p_Taula;
        // Ens pintem
        invalidate();
    }

}
