package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.TaulaView;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 15/12/2015.
 */
public class taula {
    public PointF Punt;
    public Boolean Esborrat;
    public Boolean Esborrantse;
    public String Texte;
    public int Id;
    public int CodiTaula;
    public Rect Detector;
    public TaulaClient Taula;
    public TaulaView View;
    //
    public Boolean DIB_Actiu;
    private Paint l_TaulaActiva;
    private BitmapDrawable b1;
    public Rect DetectorButo;

    public taula(boolean p_Actiu) {
        Punt = new PointF();
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        CodiTaula = -1;
        Detector = new Rect();
        DetectorButo = new Rect();
        View = new TaulaView();
        //
        l_TaulaActiva = new Paint();
        l_TaulaActiva.setColor(Color.GREEN);
        l_TaulaActiva.setAntiAlias(true);
        l_TaulaActiva.setStyle(Paint.Style.STROKE);
        l_TaulaActiva.setStrokeWidth(2);

        //b.setWidth(15);
        //b1 = new BitmapDrawable(Globals.g_Native.getResources(), b);
        DIB_Actiu = p_Actiu;
    }

    public void draw(Canvas p_Canvas, Paint p_Paint, float p_FactorMida) {
        int l_aux;
        l_aux = Math.round((Taula.AmpladaDiametre / 100) * p_FactorMida);
        // Dibuixem la taula
        if (DIB_Actiu) {
            p_Canvas.drawCircle(Punt.x, Punt.y, l_aux, l_TaulaActiva);
        }
        else{
            p_Canvas.drawCircle(Punt.x, Punt.y, l_aux, p_Paint);
        }

        Log.d("BOD-DistribucioEdicio", "------------------------ Taula a " + Math.round(Punt.x) + ", "  + Math.round(Punt.y));

        // Expresem el detector
        Detector = new Rect(Math.round(Punt.x) -l_aux, Math.round(Punt.y) -l_aux, Math.round(Punt.x) + l_aux, Math.round(Punt.y) + l_aux);
        // Validem si hi es activa
        if (DIB_Actiu) {
            //p_Canvas.drawBitmap(bGraf, Punt.x, Punt.y + 25, p);
            //p_Canvas.drawBitmap(bGraf, Math.round(Punt.x)-20, Math.round(Punt.y)-20, p);
            // Detector del buto de moviment
            //DetectorButo = new Rect(Math.round(Punt.x) - 20, (Math.round(Punt.y) + 25) - 20, Math.round(Punt.x) + 20, (Math.round(Punt.y) + 25) + 10);
            /*
            Rect l_Bounds = new Rect(Math.round(Punt.x) + 20, Math.round(Punt.y) + 20, Math.round(Punt.x) + 30, Math.round(Punt.y) + 30);
            b1.setBounds(l_Bounds);
            b1.draw(p_Canvas);
            */
        }
    }

    public int PrememBoto(PointF p_Punt){
        int l_Boto = 0;

        return l_Boto;
    }
}