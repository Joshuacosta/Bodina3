package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Entitats.SaloClient;

/**
 * Created by it00046 on 10/12/2015.
 */
public class SPNSalonsClient {
    public SaloClient g_Salo;
    public String g_NomSalo;

    public SPNSalonsClient(SaloClient p_Salo, String p_NomSalo){
        g_Salo = p_Salo;
        g_NomSalo = p_NomSalo;
    }

    public SaloClient getId () {
        return g_Salo;
    }

    public String getValue () {
        return g_NomSalo;
    }

    @Override
    public String toString () { return g_NomSalo; }
}
