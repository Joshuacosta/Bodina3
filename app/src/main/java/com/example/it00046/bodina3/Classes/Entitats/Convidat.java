package com.example.it00046.bodina3.Classes.Entitats;

import android.provider.ContactsContract;

import com.example.it00046.bodina3.Classes.Globals;

/**
 * Created by it00046 on 26/02/2016.
 */
public class Convidat {
    public CelebracioClient Celebracio;
    public int Codi;
    public String Nom;
    public int Tipus; // k_Tipus...
    public Convidat Parella;
    public String Adresa;
    public String Contacte;
    public String Telefon;
    public String eMail;
    public MenuConvidats Menu;
    public boolean Confirmat;
    public boolean Avisat;
    public boolean Transport;
    public CategoriaConvidats Categoria1;
    public CategoriaConvidats Categoria2;
    public RelacioConvidats Relacio1;
    public String Comentari;
    public int Estat;
    // Constants de la classe
    public static final int k_TipusNoDefinit = -1;
    public static final int k_TipusHome = 0;
    public static final int k_TipusDona = 1;
    public static final int k_TipusNen = 2;
    public static final int k_TipusParella = 3;
    //
    public static final int k_EstatNoDefinit = -1;
    public static final int k_EstatActiu = 0;
    public static final int k_EstatEsborrat = 1;
    //
    public Convidat(){
        Celebracio  = new CelebracioClient();
        Codi        = k_EstatNoDefinit;
        Nom         = "";
        Tipus       = k_TipusNoDefinit;
        Parella     = new Convidat();
        Adresa      = "";
        Contacte    = "";
        Telefon     = null;
        eMail       = null;
        Menu        = new MenuConvidats();
        Confirmat   = false;
        Avisat      = false;
        Transport   = false;
        Categoria1  = new CategoriaConvidats();
        Categoria2  = new CategoriaConvidats();
        Relacio1    = new RelacioConvidats();
        Comentari   = "";
        Estat       = k_EstatNoDefinit;
    }
}
