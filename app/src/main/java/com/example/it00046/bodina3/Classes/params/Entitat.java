package com.example.it00046.bodina3.Classes.params;

import java.io.Serializable;

/**
 * Created by Joshua on 14/04/2015.
 */
public class Entitat implements Serializable {
    public String Codi;
    public String Nom;
    public String Adresa;
    public String Contacte;
    public String Telefon;
    public String eMail;

    public void Entitat(){
        this.Codi = new String();
        this.Nom = new String();
        this.Adresa = new String();
        this.Contacte = new String();
        this.Telefon = new String();
        this.eMail = new String();
    }
}