package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by it00046 on 15/12/2015.
 */
public class linia {
    public PointF Inici;                     // Punt inicial de la recta
    public PointF Fi;                        // Punt final
    public Boolean Curva;                    // Es curva?
    public PointF PuntCurva;                 // Punt que defineix la curva
    public metresCurva ObjDistancia;         // Element metresCurva (distancia i que a mes, si l'arroseguem         // converteix la recta en curva

    public linia(){
        Inici = new PointF();
        Fi = new PointF();
        Curva = false;
        PuntCurva = new PointF();
        ObjDistancia = new metresCurva();
    }

    // Class per definir distancies de les linies i que ens serveix per arrosegar i convertirles
    // en curves
    static public class metresCurva {
        public PointF Punt;         // Punt on es definit
        public String Distancia;    // Distancia que mostra
        public Rect Detector;       // Detector
        public Boolean Mogut;

        public metresCurva(){
            Punt = new PointF();
            Distancia = new String();
            Detector = new Rect();
            Mogut = false;
        }

        public metresCurva(PointF p_Punt, String p_Distancia, Rect p_Detector){
            Punt = p_Punt;
            Distancia = p_Distancia;
            Detector = p_Detector;
        }
    }
}