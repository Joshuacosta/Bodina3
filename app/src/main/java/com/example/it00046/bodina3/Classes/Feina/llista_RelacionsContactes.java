package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.RelacioContactes;
import com.example.it00046.bodina3.Classes.Entitats.RelacioConvidats;

import java.util.ArrayList;

/**
 * En aquest llista guardem els grups de la celebracio amb la que estem
 * treballant
 */
public final class llista_RelacionsContactes {
    public static ArrayList<RelacioContactes> Llista = new ArrayList<>();

    static public RelacioContactes DadesGrup(int p_CodiGrup){
        RelacioContactes l_Grup = new RelacioContactes();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_CodiGrup){
                l_Grup = Llista.get(i);
                break;
            }
        }
        return l_Grup;
    }

    static public void Carrega(ArrayList<RelacioContactes> p_LLista){
        Llista = p_LLista;
    }
}
