package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;

import java.util.ArrayList;

/**
 * Created by it00046 on 21/01/2016.
 */
public class planol {
    public ArrayList<SaloClient.DetallPlanol> dades;
    public String Unitats;
    public String Escala;
    public float Mesura;

    public planol(){
        dades = new ArrayList<>();
    }

    public SaloClient.DetallPlanol Llegeix(int i){
        return dades.get(i);
    }

    public void Grava(SaloClient.DetallPlanol l_Dades){
        dades.add(l_Dades);
    }

    public int Tamany(){
        return dades.size();
    }

    public planol LlegirLinies(){
        planol l_Linies = new planol();

        for (int i=0; i < dades.size(); i++){
            if (dades.get(i).Tipus == Globals.k_TipusLinia){
                l_Linies.Grava(dades.get(i));
            }
        }
        return l_Linies;
    }

    public planol LlegirTextes(){
        planol l_Textes = new planol();

        for (int i=0; i < dades.size(); i++){
            if (dades.get(i).Tipus == Globals.k_TipusTexte){
                l_Textes.Grava(dades.get(i));
            }
        }
        return l_Textes;
    }
}
