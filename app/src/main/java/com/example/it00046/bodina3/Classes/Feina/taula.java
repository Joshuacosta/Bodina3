package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Globals;
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
    //
    public Boolean DIB_Actiu;
    private Paint p;
    private Bitmap b;
    private Bitmap bGraf;
    private BitmapDrawable b1;
    public Rect DetectorButo;

    private Drawable mCustomImage;

    public taula(boolean p_Actiu) {
        Punt = new PointF();
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        CodiTaula = -1;
        Detector = new Rect();
        DetectorButo = new Rect();
        //
        p = new Paint();
        p.setColor(Color.RED);
        b = BitmapFactory.decodeResource(Globals.g_Native.getResources(), R.drawable.ic_ma_blanc_24);
        //b.setWidth(15);
        //b1 = new BitmapDrawable(Globals.g_Native.getResources(), b);
        DIB_Actiu = p_Actiu;
    }

    public void draw(Canvas p_Canvas, Paint p_Paint) {
        if (DIB_Actiu) {
            p_Canvas.drawCircle(Punt.x, Punt.y, 20, p);
            bGraf = Bitmap.createScaledBitmap(b, 40, 40, false);
            p_Canvas.drawCircle(Punt.x - 20, Punt.y - 20, 20, p_Paint);
            p_Canvas.drawBitmap(bGraf, Punt.x, Punt.y + 25, p);
            // Detector buto
            DetectorButo = new Rect(Math.round(Punt.x) - 20, (Math.round(Punt.y) + 25) - 20, Math.round(Punt.x) + 20, (Math.round(Punt.y) + 25) + 10);
            /*
            Rect l_Bounds = new Rect(Math.round(Punt.x) + 20, Math.round(Punt.y) + 20, Math.round(Punt.x) + 30, Math.round(Punt.y) + 30);
            b1.setBounds(l_Bounds);
            b1.draw(p_Canvas);
            */
        }
        else{
            p_Canvas.drawCircle(Punt.x, Punt.y, 20, p_Paint);
        }
    }

    public int PrememBoto(PointF p_Punt){
        int l_Boto = 0;

        return l_Boto;
    }
}