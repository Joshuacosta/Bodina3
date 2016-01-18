package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

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
    //
    public Boolean DIB_Actiu;
    private Paint p;
    private Bitmap b;


    private Drawable mCustomImage;

    public taula(){
        Punt = new PointF();
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        CodiTaula = -1;
        Detector = new Rect();
        //
        p = new Paint();
        p.setColor(Color.RED);
        b = BitmapFactory.decodeResource(Globals.g_Native.getResources(), R.drawable.ic_access_time_black_24dp);
    }

    public void draw(Canvas p_Canvas, Paint p_Paint, Boolean p_Actiu){
        p_Canvas.drawCircle(Punt.x, Punt.y, 20, p_Paint);
        //
        if (p_Actiu){
            p_Canvas.drawBitmap(b, Punt.x + 20, Punt.y + 20, p);
        }
    }


    public void MostraActiu(Canvas p_Canvas, Paint p_Paint){
        // Mostrem els control de

    }
}