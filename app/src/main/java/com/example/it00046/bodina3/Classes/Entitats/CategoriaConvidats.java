package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 22/09/2015.
 */
public class CategoriaConvidats {
    public int Codi;
    public int CodiCelebracio;
    public CelebracioClient Celebracio;
    public String Descripcio;
    //
    public static final int k_EstatNoDefinit = -1;
    public static final int k_EstatActiu = 0;
    public static final int k_EstatEsborrat = 1;
    //
    public CategoriaConvidats(){
        Codi            = k_EstatNoDefinit;
        CodiCelebracio  = k_EstatNoDefinit;
        Celebracio      = new CelebracioClient();
        Descripcio      = null;
    }
}
