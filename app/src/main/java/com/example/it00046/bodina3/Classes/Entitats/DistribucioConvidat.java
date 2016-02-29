package com.example.it00046.bodina3.Classes.Entitats;

import android.graphics.PointF;

/**
 * Created by Joshua on 27/02/2016.
 */
public class DistribucioConvidat {
    public CelebracioClient Celebracio;
    public Distribucio Distribucio;
    public Convidat Convidat;
    public PointF Posicio; // No hauria de ser una posicio relativa dintre de la taula?
    public Taula Taula;

    public DistribucioConvidat(){
        Celebracio = new CelebracioClient();
        Distribucio = new Distribucio();
        Convidat = new Convidat();
        Posicio = new PointF();
        Taula = new Taula();
    }
}
