package com.example.it00046.bodina3.Classes.params;

import java.io.Serializable;

/**
 * Created by Joshua on 14/04/2015.
 */
public class PAREntitat implements Serializable {
    public String Codi;
    public String Nom;
    public String Adresa;
    public String Contacte;
    public String Telefon;
    public String Pais;
    public String eMail;
    public int Estat;

    public void PAREntitat(){
        this.Codi = new String();
        this.Nom = new String();
        this.Adresa = new String();
        this.Contacte = new String();
        this.Telefon = new String();
        this.Pais = new String();
        this.eMail = new String();
        this.Estat = 0;
    }
}
