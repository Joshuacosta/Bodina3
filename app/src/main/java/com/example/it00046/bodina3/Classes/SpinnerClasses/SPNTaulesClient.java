package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;

public class SPNTaulesClient {
    public TaulaClient g_Taula;
    public String g_Descripcio;

    public SPNTaulesClient(TaulaClient p_Taula, String p_Descripcio){
        g_Taula = p_Taula;
        g_Descripcio = p_Descripcio;
    }

    public TaulaClient getId () {
        return g_Taula;
    }

    public String getValue () {
        return g_Descripcio;
    }

    @Override
    public String toString () { return g_Descripcio; }
}
