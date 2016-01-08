package com.example.it00046.bodina3.Classes.Entitats;

import java.util.Date;

/**
 * Created by it00046 on 28/09/2015.
 */
public class CelebracioClient {
    public int Codi;
    public SaloClient Salo;
    public TipusCelebracio Tipus;
    public String Descripcio;
    public int Convidats;
    public String Data;
    public String Lloc;
    public String Contacte;
    public int Estat;

    public CelebracioClient(){
        Codi = 0;
        Salo = new SaloClient();
        Salo.Codi = 0;
        Salo.Nom = "";
        Tipus = new TipusCelebracio();
        Tipus.Codi = 0;
        Tipus.Descripcio = "";
        Descripcio = "";
        Convidats = 0;
        Data = "";
        Lloc = "";
        Contacte = "";
        Estat = -1;
    }
}
