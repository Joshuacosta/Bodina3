package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 22/09/2015.
 */
public class CategoriaConvidats {
    public int Codi;
    public CelebracioClient CodiCelebracio;
    public String Descripcio;

    public CategoriaConvidats(){
        Codi            = -1;
        CodiCelebracio  = new CelebracioClient();
        Descripcio      = null;
    }
}
