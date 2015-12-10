package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;

/**
 * Created by it00046 on 10/12/2015.
 */
public class SPNTipusCelebracio {
    public TipusCelebracio g_Tipus;
    public String g_Descripcio;

    public SPNTipusCelebracio(TipusCelebracio p_Tipus, String p_Descripcio){
        g_Tipus = p_Tipus;
        g_Descripcio = p_Descripcio;
    }

    public TipusCelebracio getId () {
        return g_Tipus;
    }

    public String getValue () {
        return g_Descripcio;
    }

    @Override
    public String toString () { return g_Descripcio; }
}
