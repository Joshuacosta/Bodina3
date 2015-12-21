package com.example.it00046.bodina3.Classes.Entitats;

import java.util.ArrayList;

/**
 * Created by it00046 on 29/09/2015.
 */
public class SaloClient {
    public int Codi;
    public String Nom, Descripcio, UnitatPlanol;
    public int Estat, MaxPersones;
    public float EscalaPlanol;
    public ArrayList<DetallPlanol> g_Planol;

    public SaloClient(){
        Codi            = 0;
        Nom             = "";
        Descripcio      = "";
        MaxPersones     = 0;    // Numero maxim de persones del salo
        Estat           = 1;    // Per defecte actiu
        EscalaPlanol    = 0;    // Escala del planol (per saber les distancies)
        UnitatPlanol    = "";   // Unitat de mesura del planol
        g_Planol = new ArrayList<>();
    }

    static public class DetallPlanol{
        public int Tipus;       // Pot ser una linia o un texte
        public float OrigenX;
        public float OrigenY;
        public float DestiX;
        public float DestiY;
        public float CurvaX;
        public float CurvaY;
        public String Texte;

        public DetallPlanol(){
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
}
