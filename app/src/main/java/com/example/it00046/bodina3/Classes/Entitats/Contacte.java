package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 26/02/2016.
 */
public class Contacte {
    public int Codi;
    public String Nom;
    public int Tipus; // Home, Dona, Nen, Parella
    public String Adresa;
    public String Contacte;
    public String Comentari;
    public CategoriaContactes Categoria1;
    public CategoriaContactes Categoria2;
    public CategoriaContactes Categoria3;
    public RelacioContactes Relacio1;
    public RelacioContactes Relacio2;
    public int Estat;

    public Contacte(){
        Codi        = -1;
        Nom         = "";
        Tipus       = 0;
        Adresa      = "";
        Contacte    = "";
        Comentari   = "";
        Categoria1  = new CategoriaContactes();
        Categoria2  = new CategoriaContactes();
        Categoria3  = new CategoriaContactes();
        Relacio1    = new RelacioContactes();
        Relacio2    = new RelacioContactes();
        Estat       = 0;
    }
}
