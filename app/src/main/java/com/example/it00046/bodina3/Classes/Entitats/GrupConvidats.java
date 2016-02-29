package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 26/02/2016.
 */
public class GrupConvidats {
    public int Codi;
    public CelebracioClient Celebracio;
    public String Descripcio;

    public GrupConvidats(){
        Codi = -1;
        Celebracio = new CelebracioClient();
        Descripcio = "";
    }
}
