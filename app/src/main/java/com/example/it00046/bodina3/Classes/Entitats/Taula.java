package com.example.it00046.bodina3.Classes.Entitats;

import android.graphics.Paint;
import android.graphics.Rect;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.TaulaView;

/**
 * Created by it00046 on 15/12/2015.
 */

public class Taula {
    public Boolean Esborrat;
    public Boolean Esborrantse;
    public String Texte;
    public int Id;
    public int NumTaula;
    public Rect Detector;
    public TaulaClient Taula;
    public TaulaView View;

    public Taula() {
        Esborrat = false;
        Esborrantse = false;
        Texte = new String();
        Id = -1;
        NumTaula = 0;
        // La taula dibuixada i les dades
        View = new TaulaView();
        Taula = new TaulaClient();
    }
}
