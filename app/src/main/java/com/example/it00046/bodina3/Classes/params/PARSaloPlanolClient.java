package com.example.it00046.bodina3.Classes.Params;

import java.io.Serializable;

/**
 * Created by it00046 on 28/09/2015.
 */
public class PARSaloPlanolClient implements Serializable {
    public int Tipus;       // Pot ser una linia o un texte
    public float OrigenX;
    public float OrigenY;
    public float DestiX;
    public float DestiY;
    public float CurvaX;
    public float CurvaY;
    public String Texte;

    public PARSaloPlanolClient(){
        Tipus = 0;
        OrigenX = 0;
        OrigenY = 0;
        DestiX = 0;
        DestiY = 0;
        CurvaX = 0;
        CurvaY = 0;
        Texte = "";
    }
}
