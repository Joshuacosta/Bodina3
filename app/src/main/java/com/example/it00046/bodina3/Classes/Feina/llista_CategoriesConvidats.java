package com.example.it00046.bodina3.Classes.Feina;

import android.content.Context;

import com.example.it00046.bodina3.Classes.DAO.DAOCategoriesConvidats;
import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * En aquest llista guardem els tipus de categories de la celebracio amb la que estem
 * treballant
 */
public final class llista_CategoriesConvidats {
    public static ArrayList<CategoriaConvidats> Llista;

    public llista_CategoriesConvidats(){
        Llista = new ArrayList<CategoriaConvidats>();
    }

    static public CategoriaConvidats DadesCategoria(int p_CodiCategoria){
        CategoriaConvidats l_Categoria = new CategoriaConvidats();
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
