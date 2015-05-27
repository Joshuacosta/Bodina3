package com.example.it00046.bodina3.Classes.SpinnerClasses;

import com.example.it00046.bodina3.Classes.Tipus.Entitat;

/**
 * Created by it00046 on 13/05/2015.
 */
public class SpnEntitat {
    private Entitat l_entitat;
    private String l_Nom;
    private boolean l_Nova;

    public SpnEntitat ( Entitat Entitat , String P_Nom , boolean P_Nova) {
        this.l_entitat = Entitat;
        this.l_Nom = P_Nom;
        this.l_Nova = P_Nova;
    }

    public Entitat getId () {
        return l_entitat;
    }

    public String getValue () {
        return l_Nom;
    }

    public boolean EsNova() { return l_Nova; }
    @Override
    public String toString () {
        return l_Nom;
    }
}
