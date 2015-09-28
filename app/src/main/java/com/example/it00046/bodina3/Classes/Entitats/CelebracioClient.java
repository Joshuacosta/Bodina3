package com.example.it00046.bodina3.Classes.Entitats;

import java.util.Date;

/**
 * Created by it00046 on 28/09/2015.
 */
public class CelebracioClient {
    public int Codi;
    public int CodiSalo;
    public int Tipus;
    public String Descripcio;
    public int Convidats;
    public String Data;
    public String Lloc;
    public String Contacte;
    public int Estat;

    public void CelebracionsClient(){
        Codi = 0;
        CodiSalo = 0;
        Tipus = 0;
        Descripcio = "";
        Convidats = 0;
        Data = "";
        Lloc = "";
        Contacte = "";
        Estat = -1;
    }
}
