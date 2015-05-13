package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.EntitatClient;
import com.example.it00046.bodina3.Classes.params.Entitat;

/**
 * Created by it00046 on 13/05/2015.
 */
public class SpnEntitat {
    private EntitatClient l_entitat;
    private String l_Nom;

    public SpnEntitat ( EntitatClient databaseId , String P_Nom ) {
        this.l_entitat = databaseId;
        this.l_Nom = P_Nom;
    }

    public EntitatClient getId () {
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
