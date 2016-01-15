package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.PointF;
import android.graphics.Rect;

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

    public taula(){
        Punt = new PointF();
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        CodiTaula = -1;
        Detector = new Rect();
    }
}