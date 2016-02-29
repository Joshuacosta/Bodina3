package com.example.it00046.bodina3.Classes.Entitats;

import com.example.it00046.bodina3.Classes.Globals;

/**
 * Created by it00046 on 26/02/2016.
 */
public class Convidat {
    public int Codi;
    public String Nom;
    public int Tipus; // Home, Dona, Nen, Parella
    public String Adresa;
    public String Contacte;
    public String Comentari;
    public CategoriaConvidats Categoria1;
    public CategoriaConvidats Categoria2;
    public CategoriaConvidats Categoria3;
    public GrupConvidats Grup;
    public int Estat;

    public Convidat(){
        Codi        = -1;
        Nom         = "";
        Tipus       = 0;
        Adresa      = "";
        Contacte    = "";
        Comentari   = "";
        Categoria1  = new CategoriaConvidats();
        Categoria2  = new CategoriaConvidats();
        Categoria3  = new CategoriaConvidats();
        Grup        = new GrupConvidats();
        Estat       = 0;
    }
}
