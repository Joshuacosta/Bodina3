package com.example.it00046.bodina3.Classes.Feina;

import com.example.it00046.bodina3.Classes.Entitats.MenuConvidats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * En aquest llista guardem els menus dels convidats per la celebracio amb la que treballem
 */
public final class llista_MenusConvidats {
    public static ArrayList<MenuConvidats> Llista = new ArrayList<>();

    static public MenuConvidats DadesMenu(int p_CodiMenu){
        MenuConvidats l_Menu = new MenuConvidats();
        for (int i=0; i < Llista.size(); i++){
            if (Llista.get(i).Codi == p_CodiMenu){
                l_Menu = Llista.get(i);
                break;
            }
        }
        // Per si de cas se ha esborrat
        if (l_Menu.Descripcio == null){
            l_Menu.Descripcio = Globals.g_Native.getString(R.string.esborrat);
        }
        return l_Menu;
    }

}
