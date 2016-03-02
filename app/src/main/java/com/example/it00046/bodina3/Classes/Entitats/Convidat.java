package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 26/02/2016.
 */
public class Convidat {
    public CelebracioClient Celebracio;
    public Contacte Contacte;
    public boolean Confirmat;
    public boolean Avisat;
    public boolean Transport;
    public String Comentari;
    public int Estat;

    public Convidat(){
        Celebracio  = new CelebracioClient();
        Contacte    = new Contacte();
        Confirmat   = false;
        Avisat      = false;
        Transport   = false;
        Comentari   = "";
        Estat       = 0;
    }
}
