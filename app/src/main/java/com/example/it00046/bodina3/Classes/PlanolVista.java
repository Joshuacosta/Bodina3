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

import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Feina.linia;

import java.util.ArrayList;

public class PlanolVista extends RelativeLayout {
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
        Float l_Factor, l_Escala, l_NouX, l_NouY;

        canvas.save();
        g_CanvasRect = canvas.getClipBounds();
        canvas.drawBitmap(g_CanvasBitmap, 0, 0, g_PaintCanvas);
        // Pintem rectes i curves
        g_drawPath.reset();
        for (int I=0; I < g_LiniesPlanol.size(); I++) {
            l_Linia = g_LiniesPlanol.get(I);
            l_Linia.Inici.offset(g_mPosX, g_mPosY);
            // Si estem a la primera linia ens posicionem "al principi" amb un move
            if (I == 0) {
                g_drawPath.moveTo(l_Linia.Inici.x, l_Linia.Inici.y);
            }
            // Pintem el punt final
            l_Linia.Fi.offset(g_mPosX, g_mPosY);
            // Validem si es una curva
            if (l_Linia.Curva) {
                l_Linia.PuntCurva.offset(g_mPosX, g_mPosY);
                g_drawPath.quadTo(l_Linia.PuntCurva.x, l_Linia.PuntCurva.y, l_Linia.Fi.x, l_Linia.Fi.y);
                // Si s'ha mogut tambe hem de modificar la posicio
                if (l_Linia.ObjDistancia.Mogut) {
                    l_Linia.ObjDistancia.Punt.offset(g_mPosX, g_mPosY);
                }
            }
            else {
                g_drawPath.lineTo(l_Linia.Fi.x, l_Linia.Fi.y);
            }
        }
        // Ens centrem: recuperem bound i calculem escala
        g_drawPath.computeBounds(l_Bounds, true);
        // Centrem
        l_NouX = -(l_Bounds.centerX() - g_CenterX);
        l_NouY = -(l_Bounds.centerY() - g_CenterY);
        canvas.translate(l_NouX, l_NouY);
        // Escalem
        if (l_Bounds.height() >= l_Bounds.width()){
            l_Factor = l_Bounds.height();
        }
        else{
            l_Factor = l_Bounds.width();
        }
        l_Escala = (l_Factor / g_AmpladaScreen);
        canvas.scale(l_Escala, l_Escala);
        // Pintem distancies de les rectes
        for (int I=0; I < g_LiniesPlanol.size(); I++) {
            l_Linia = g_LiniesPlanol.get(I);
            // Calculem distancia de la linia i els bounds del texte
            l_Distancia = EscalaDistancia(CalculaDistancia(l_Linia.Inici, l_Linia.Fi));
            l_PuntMig = new PointF((l_Linia.Inici.x + l_Linia.Fi.x) / 2, (l_Linia.Inici.y + l_Linia.Fi.y) / 2);
            canvas.drawText(l_Distancia, l_PuntMig.x, l_PuntMig.y, g_PaintTextDistancia);
        }
        // Pintem el planol
        canvas.drawPath(g_drawPath, g_PaintFinal);
        canvas.restore();
    }

    public void DibuixaPlanol(ArrayList<SaloClient.DetallPlanol> p_Planol){
        SaloClient.DetallPlanol l_Element;
        PointF l_PuntInici, l_PuntFi, l_PuntCurva;

        for (int i = 0; i < p_Planol.size(); i++) {
            l_Element = p_Planol.get(i);
            switch (l_Element.Tipus) {
                case 0: // Linia
                    l_PuntInici = new PointF(l_Element.OrigenX, l_Element.OrigenY);
                    l_PuntFi = new PointF(l_Element.DestiX, l_Element.DestiY);
                    if (l_Element.CurvaX != 0) {
                        // Curva
                        l_PuntCurva = new PointF(l_Element.CurvaX, l_Element.CurvaY);
                        g_LiniesPlanol.add(DefineixCurva(l_PuntInici, l_PuntFi, l_PuntCurva));
                    } else {
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

    public void EsborraPlanol(){
        g_LiniesPlanol = new ArrayList<>();
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

    private double CalculaDistancia(PointF P_Punt1, PointF P_Punt2){
        double l_Part1, l_Part2, l_Resultat;

        l_Part1 = new Float(Math.abs(P_Punt1.x)-Math.abs(P_Punt2.x));
        l_Part2 = new Float(Math.abs(P_Punt1.y)-Math.abs(P_Punt2.y));

        l_Resultat = Math.sqrt(Math.pow(l_Part1, 2) + Math.pow(l_Part2, 2));
        return l_Resultat;
    }

    private String EscalaDistancia(double P_Distancia){
        String l_Resultat = null;

        //l_Resultat = String.valueOf(Math.round(P_Distancia / g_UnitatX));
        l_Resultat = String.valueOf(Math.round(P_Distancia));
        return l_Resultat;
    }


}
