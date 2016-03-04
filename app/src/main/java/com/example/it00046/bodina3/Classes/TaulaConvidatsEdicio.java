package com.example.it00046.bodina3.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.it00046.bodina3.Classes.Entitats.Planol;
import com.example.it00046.bodina3.Classes.Entitats.Taula;
import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.linia;
import com.example.it00046.bodina3.distribucions_client_taula_convidats;

import java.util.ArrayList;

public class TaulaConvidatsEdicio extends RelativeLayout {
    public distribucions_client_taula_convidats papa;
    private float g_mPosX = 0;
    private float g_mPosY = 0;
    private Path g_drawPath;
    private Paint g_PaintFinal, g_PaintCanvas, g_PaintTextDistancia;
    private Canvas g_DrawCanvas;
    private Bitmap g_CanvasBitmap;
    private Rect g_CanvasRect = null;
    private int g_CenterX = 0, g_CenterY = 0;
    private int g_AmpladaScreen, g_AlsadaScreen;
    public ArrayList<linia> g_LiniesPlanol = new ArrayList<>();
    private float g_UnitatMesura;

    public TaulaConvidatsEdicio(Context p_Context, AttributeSet p_Attrs) {
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
        // Definim el paint de texte distancia
        g_PaintTextDistancia = new Paint();
        g_PaintTextDistancia.setTextSize(22);
        g_PaintTextDistancia.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        g_PaintTextDistancia.setColor(Color.RED);
        //
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
        linia l_Linia;
        RectF l_Bounds = new RectF();
        PointF l_PuntMig;
        String l_Distancia;
        int l_Factor;
        Taula l_Taula = new Taula();


        canvas.save();
        g_CanvasRect = canvas.getClipBounds();
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        // Pintem taula
        l_Taula = papa.g_Taula;
        l_Taula.View.DibuixaPerEdicio(canvas, g_AmpladaScreen, g_AlsadaScreen);
        //
        canvas.restore();
    }
}
