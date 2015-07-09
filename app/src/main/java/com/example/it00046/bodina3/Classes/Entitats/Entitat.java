package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 15/05/2015.
 */
public class Entitat {
    public String Codi;
    public String Nom;
    public String Adresa;
    public String Contacte;
    public int TipusContacte;
    public String Telefon;
    public String Pais;
    public String eMail;
    public int Estat;

    public void Entitat(){
        Codi = new String();
        Nom = new String();
        Adresa = new String();
        Contacte = new String();
        TipusContacte = 0;
        Telefon = new String();
        Pais = new String();
        eMail = new String();
        Estat = 0;
    }
}
