package com.example.it00046.bodina3.Classes.Entitats;

import java.util.ArrayList;

/**
 * Created by it00046 on 29/09/2015.
 */
public class SaloClient {
    public int Codi;
    public String Nom, Descripcio, UnitatsPlanol, EscalaPlanol;
    public int Estat, Capacitat;
    public float UnitatMesura;
    public Planol Planol;

    public SaloClient(){
        Codi            = 0;
        Nom             = "";
        Descripcio      = "";
        Capacitat       = 0;    // Numero maxim de persones del salo
        Estat           = 1;    // Per defecte actiu
        EscalaPlanol    = "";
        UnitatsPlanol   = "";
        UnitatMesura    = 0.0f;
        Planol          = new Planol();
    }
}
