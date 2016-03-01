package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.RelacioConvidats;

import java.util.ArrayList;

/**
 * En aquest llista guardem els grups de la celebracio amb la que estem
 * treballant
 */
public final class llista_RelacionsConvidats {
    public static ArrayList<RelacioConvidats> Llista;

    public llista_RelacionsConvidats(){
        Llista = new ArrayList<RelacioConvidats>();
    }

    static public RelacioConvidats DadesGrup(int p_CodiGrup){
        RelacioConvidats l_Grup = new RelacioConvidats();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_CodiGrup){
                l_Grup = Llista.get(i);
                break;
            }
        }
        return l_Grup;
    }

    static public void Carrega(ArrayList<RelacioConvidats> p_LLista){
        Llista = p_LLista;
    }
}
