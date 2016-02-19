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
    public Rect DetectorButo;

    public taula() {
        Punt = new PointF();
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        CodiTaula = -1;
        // La taula dibuixada
        View = new TaulaView();
    }

}