package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 15/05/2015.
 */
public class Entitat {
    public String Codi;
    public String Nom;
    public String Adresa;
    public String Contacte;
    public String Telefon;
    public String Pais;
    public String eMail;
    public int Estat;

    public void Entitat(){
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
