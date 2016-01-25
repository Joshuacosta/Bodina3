package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.taula;

/**
 * Amb aquesta clase mostrem la taula de forma grafica
 */
public class TaulaView extends RelativeLayout {

    private float g_mPosX = 0;
    private float g_mPosY = 0;
    private Paint g_PaintTaula, g_PaintCanvas, g_PaintTextDistancia;
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    private Rect g_CanvasRect = null;
    private int g_CenterX = 0, g_CenterY = 0;
    private int g_AmpladaScreen, g_AlsadaScreen;
    private Path g_drawPath;
    private TaulaClient g_Taula;
    private float g_Factor = 1;

    public TaulaView(Context p_Context, AttributeSet p_Attrs) {
        super(p_Context, p_Attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        // Definim path de dibuix
        g_drawPath = new Path();
        // Definim paint de canvas
        g_PaintCanvas = new Paint(Paint.DITHER_FLAG);
        g_PaintTaula = new Paint();
        g_PaintTaula.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        g_CanvasRect = canvas.getClipBounds();
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        // Pintem la taula
        canvas.drawCircle(0, 0, g_Taula.AmpladaDiametre * g_Factor, g_PaintTaula);
    }

    public void ExpresaTaula(TaulaClient p_Taula, float p_Factor){
        g_Taula = p_Taula;
        g_Factor = p_Factor;
    }
}
