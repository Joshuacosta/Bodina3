package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 26/02/2016.
 */
public class Comensal {
    public CelebracioClient Celebracio;
    public Convidat Convidat;
    public CategoriaConvidats Categoria1;
    public CategoriaConvidats Categoria2;
    public CategoriaConvidats Categoria3;
    public GrupConvidats Grup;
    public boolean Confirmat;
    public boolean Avisat;
    public boolean Transport;
    public String Comentari;
    public int Estat;

    public Comensal(){
        Celebracio  = new CelebracioClient();
        Convidat    = new Convidat();
        Categoria1  = new CategoriaConvidats();
        Categoria2  = new CategoriaConvidats();
        Categoria3  = new CategoriaConvidats();
        Grup        = new GrupConvidats();
        Confirmat   = false;
        Avisat      = false;
        Transport   = false;
        Comentari   = "";
        Estat       = 0;
    }
}
