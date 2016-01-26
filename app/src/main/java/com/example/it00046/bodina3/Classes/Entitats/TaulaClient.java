package com.example.it00046.bodina3.Classes.Entitats;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 13/01/2016.
 */
public class TaulaClient {
    public int Codi;
    public int Tipus;
    public String Descripcio;
    public int MaxPersones;
    public int AmpladaDiametre;
    public int Llargada;
    public int Estat;
    // Constants
    public static final int k_TipusRodona = 0;
    public static final int k_TipusQuadrada = 1;
    public static final int k_TipusRectangular = 2;
    public static final int k_TipusImperial = 3;
    //
    public TaulaClient(){
        Codi = 0;
        Tipus = 0;
        Descripcio = "";
        MaxPersones = 0;
        AmpladaDiametre = 0;
        Llargada = 0;
        Estat = 0;
    }

    public String Detall(){
        String l_Descripcio = new String();

        if (Descripcio != "") {
            l_Descripcio = Descripcio + " ";
        }
        switch (Tipus){
            case k_TipusRodona:
                l_Descripcio += Globals.g_Native.getResources().getString(R.string.TipusTaulaRodona);
                l_Descripcio += " " + AmpladaDiametre;
                break;
            case k_TipusQuadrada:
                l_Descripcio += Globals.g_Native.getResources().getString(R.string.TipusTaulaQuadrada);
                l_Descripcio += " " + AmpladaDiametre;
                break;
            case k_TipusRectangular:
                l_Descripcio += Globals.g_Native.getResources().getString(R.string.TipusTaulaRectangular);
                l_Descripcio += " " + AmpladaDiametre + "x" + Llargada;
                break;
        }
        if (MaxPersones > 0){
            l_Descripcio += " (" + MaxPersones + " " + Globals.g_Native.getResources().getString(R.string.Maxim) + ")";
        }
        return l_Descripcio;
    }

    public String DetallAmplada(){
        String l_Descripcio = new String();

        switch (Tipus){
            case k_TipusRodona:
                l_Descripcio = Integer.toString(AmpladaDiametre);
                break;
            case k_TipusQuadrada:
                l_Descripcio = Integer.toString(AmpladaDiametre);
                break;
            case k_TipusRectangular:
                l_Descripcio = Integer.toString(AmpladaDiametre) + "x" + Integer.toString(Llargada);
                break;
        }
        return l_Descripcio;
    }
}
