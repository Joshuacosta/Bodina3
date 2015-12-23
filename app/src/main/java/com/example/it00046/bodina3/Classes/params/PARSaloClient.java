package com.example.it00046.bodina3.Classes.Params;

import com.example.it00046.bodina3.Classes.Globals;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by it00046 on 28/09/2015.
 */
public class PARSaloClient implements Serializable {
    public int Codi;
    public String Nom;
    public String Descripcio;
    public int Capacitat;
    public String EscalaPlanol;
    public String UnitatsPlanol;
    public float UnitatMesura;
    public int Estat;

    public PARSaloClient(){
        Codi = 0;
        Nom = "";
        Descripcio = "";
        Capacitat = Globals.k_CapacitatSenseDefinir;
        UnitatsPlanol = "";
        EscalaPlanol = "";
        UnitatMesura = 0.0f;
        Estat = 0;
    }
}
