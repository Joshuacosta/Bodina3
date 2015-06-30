package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 13/02/2015.
 */

/**
 * Created by Joshua on 04/02/2015.
 */
public class Client
{
    public String Codi = new String();
    public String CodiIntern = new String();
    public String eMail = new String();
    public String Nom = new String();
    public String Pais = new String();
    public String Contacte = new String();
    public String DataAlta = new String();
    public String Idioma = new String();
    public Boolean Actualitzat;

    public void Client()
    {
        Codi = "";		// Es el codi de client que donarem la primera vegada
        CodiIntern = "";  // Android_ID
        eMail = "";
        Nom = "";
        Pais = "";
        Contacte = "";
        Idioma = "";
        DataAlta = "";
        Actualitzat = false;
    }
}