package com.example.it00046.bodina3.Classes.Feina;

import android.content.Context;

import com.example.it00046.bodina3.Classes.DAO.DAOCategoriesConvidats;
import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import java.util.ArrayList;

/**
 * En aquest llista guardem els tipus de categories de la celebracio amb la que estem
 * treballant
 */
public class llista_CategoriesConvidats {
    private static ArrayList<CategoriaConvidats> Llista;

    public llista_CategoriesConvidats(){
        Llista = new ArrayList<CategoriaConvidats>();
    }

    public void Carrega(int p_CodiCelebracio, final Context p_Context){
        DAOCategoriesConvidats.Llegir(p_CodiCelebracio, p_Context);
    }

    static public CategoriaConvidats DadesCategoria(int p_CodiCategoria){
        CategoriaConvidats l_Categoria = new CategoriaConvidats();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_CodiCategoria){
                l_Categoria = Llista.get(i);
                break;
            }
        }
        return l_Categoria;
    }
}
