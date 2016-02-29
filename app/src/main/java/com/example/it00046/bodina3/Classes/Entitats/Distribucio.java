package com.example.it00046.bodina3.Classes.Entitats;

import java.util.Date;

/**
 * Created by Joshua on 27/02/2016.
 */
public class Distribucio {
    public CelebracioClient Celebracio;
    public int Codi;
    public String Nom;
    public Date DataAlta;
    public Date DataModificacio;
    public int Estat;

    public Distribucio(){
        Celebracio = new CelebracioClient();
        Codi = -1;
        Nom = "";
        DataAlta = new Date();
        DataModificacio = new Date();
        Estat = 0;
    }
}
