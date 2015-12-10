package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by it00046 on 13/02/2015.
 */

/**
 * Created by Joshua on 04/02/2015.
 */
public class Associacio
{
    public Entitat entitat;
    public String Contacte;
    public String Descripcio;
    public String eMail;
    public String DataAlta;
    public String DataFi;
    public String DataPeticio;
    public int Estat;

    public Associacio()
    {
        entitat     = new Entitat();
        Contacte    = "";
        Descripcio  = "";
        eMail       = "";
        DataAlta    = "";
        DataFi      = "";
        DataPeticio = "";
        Estat       = 0;
    }
}