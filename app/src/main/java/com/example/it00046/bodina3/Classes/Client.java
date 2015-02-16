package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */

import java.util.Date;

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
    public Date DataAlta ;
    public String DataAltaTexte = new String();
    public String Idioma = new String();
    public Boolean Actualitzat;

    public void Client()
    {
        CodiClient = "";		// Es el codi de client que donarem la primera vegada
        CodiClientIntern = "";  // Es la MAC del movil/tablet que ens conecta i necessitem per validar-ho tot
        eMail = "";
        Nom = "";
        Pais = "";
        Contacte = "";
        Idioma = "";
        DataAltaTexte = "";
        DataAlta = new Date();
        Actualitzat = false;
    }
}