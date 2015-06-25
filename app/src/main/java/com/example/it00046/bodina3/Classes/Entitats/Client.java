package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 13/02/2015.
 */

/**
 * Created by Joshua on 04/02/2015.
 */
public class Client
{
    public String CodiClient = new String();
    public String CodiClientIntern = new String();
    public String eMail = new String();
    public String Nom = new String();
    public String Pais = new String();
    public String Contacte = new String();
    public String DataAlta = new String();
    public String Idioma = new String();
    public Boolean Sincronitzacio;
    public Boolean Actualitzat;
    public String DataActualitzat;
    public String DataGrabacioServidor;

    public void Client()
    {
        CodiClient = "";		// Es el codi de client que donarem la primera vegada
        CodiClientIntern = "";  // Android_ID
        eMail = "";
        Nom = "";
        Pais = "";
        Contacte = "";
        Idioma = "";
        DataAlta = "";
        Sincronitzacio = false;
        Actualitzat = false;
        DataActualitzat = "";
        DataGrabacioServidor = "";
    }
}