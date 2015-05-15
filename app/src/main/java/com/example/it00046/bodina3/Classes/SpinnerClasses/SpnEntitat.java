package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Tipus.Entitat;

/**
 * Created by it00046 on 13/05/2015.
 */
public class SpnEntitat {
    private Entitat l_entitat;
    private String l_Nom;

    public SpnEntitat ( Entitat Entitat , String P_Nom ) {
        this.l_entitat = Entitat;
        this.l_Nom = P_Nom;
    }

    public Entitat getId () {
        return l_entitat;
    }

    public String getValue () {
        return l_Nom;
    }

    @Override
    public String toString () {
        return l_Nom;
    }
}
