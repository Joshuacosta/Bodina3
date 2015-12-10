package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 13/02/2015.
 */

/**
 * Created by Joshua on 04/02/2015.
 */
public class Client
{
    public String Codi;
    public String CodiIntern;
    public String eMail;
    public String Nom;
    public String Pais;
    public String Contacte;
    public String DataAlta;
    public String Idioma;
    public Boolean Actualitzat;

    public Client()
    {
        Codi            = "";		// Es el codi de client que donarem la primera vegada
        CodiIntern      = "";  // Android_ID
        eMail           = "";
        Nom             = "";
        Pais            = "";
        Contacte        = "";
        Idioma          = "";
        DataAlta        = "";
        Actualitzat     = false;
    }
}