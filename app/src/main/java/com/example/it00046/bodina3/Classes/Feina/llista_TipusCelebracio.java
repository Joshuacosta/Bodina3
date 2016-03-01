package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * En aquest llista guardem els tipus de categories de la celebracio amb la que estem
 * treballant
 */
public final class llista_TipusCelebracio {
    public static ArrayList<TipusCelebracio> Llista;

    public llista_TipusCelebracio(){
        Llista = new ArrayList<TipusCelebracio>();
    }

    static public TipusCelebracio DadesTipus(int p_Tipus){
        TipusCelebracio l_Tipus = new TipusCelebracio();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_Tipus){
                l_Tipus = Llista.get(i);
                break;
            }
        }
        // Per si de cas se ha esborrat
        if (l_Tipus.Descripcio == null){
            l_Tipus.Descripcio = Globals.g_Native.getString(R.string.esborrat);
        }
        return l_Tipus;
    }

}
