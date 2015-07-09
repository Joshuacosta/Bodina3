package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Entitats.Entitat;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 13/05/2015.
 */
public class SPNEntitat {
    private Entitat g_Entitat;
    private String g_Nom;

    public SPNEntitat(Entitat p_Entitat, String p_Nom) {
        g_Entitat = p_Entitat;
        g_Nom = p_Nom;
    }

    public Entitat getId () {
        return g_Entitat;
    }

    public String getValue () {
        return g_Nom;
    }

    @Override
    public String toString () {
        return g_Nom;
    }
}
