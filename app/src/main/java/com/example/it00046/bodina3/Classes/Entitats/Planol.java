package com.example.it00046.bodina3.Classes.Entitats;

import com.example.it00046.bodina3.Classes.Globals;

import java.util.ArrayList;

/**
 * Created by it00046 on 21/01/2016.
 */
public class Planol {
    public ArrayList<Detall> Dades;

    public Planol(){
        Dades = new ArrayList<>();
    }

    public Detall Llegeix(int i){
        return Dades.get(i);
    }

    public void Grava(Detall l_Dades){
        Dades.add(l_Dades);
    }

    public int Elements(){
        return Dades.size();
    }

    public ArrayList<Detall> Linies(){
        ArrayList<Detall> l_Linies = new ArrayList<>();

        for (int i=0; i < Dades.size(); i++){
            if (Dades.get(i).Tipus == Globals.k_TipusLinia){
                l_Linies.add(Dades.get(i));
            }
        }
        return l_Linies;
    }

    public ArrayList<Detall> Textes(){
        ArrayList<Detall> l_Textes = new ArrayList<>();

        for (int i=0; i < Dades.size(); i++){
            if (Dades.get(i).Tipus == Globals.k_TipusTexte){
                l_Textes.add(Dades.get(i));
            }
        }
        return l_Textes;
    }

    static public class Detall{
        public int Tipus;       // Pot ser una linia (0) o un texte (1)
        public float OrigenX;
        public float OrigenY;
        public float DestiX;
        public float DestiY;
        public float CurvaX;
        public float CurvaY;
        public String Texte;

        public Detall(){
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
