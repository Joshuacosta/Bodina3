package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 26/02/2016.
 */
public class RelacioConvidats {
    public int Codi;
    public CelebracioClient CodiCelebracio;
    public String Descripcio;

    public RelacioConvidats(){
        Codi            = -1;
        CodiCelebracio  = new CelebracioClient();
        Descripcio      = "";
    }
}
