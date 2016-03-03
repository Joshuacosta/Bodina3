package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.CategoriaContactes;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * En aquest llista guardem els tipus de categories de la celebracio amb la que estem
 * treballant
 */
public final class llista_CategoriesContactes {
    public static ArrayList<CategoriaContactes> Llista = new ArrayList<>();

    static public CategoriaContactes DadesCategoria(int p_CodiCategoria){
        CategoriaContactes l_Categoria = new CategoriaContactes();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_CodiCategoria){
                l_Categoria = Llista.get(i);
                break;
            }
        }
        // Per si de cas se ha esborrat
        if (l_Categoria.Descripcio == null){
            l_Categoria.Descripcio = Globals.g_Native.getString(R.string.esborrat);
        }
        return l_Categoria;
    }
}
