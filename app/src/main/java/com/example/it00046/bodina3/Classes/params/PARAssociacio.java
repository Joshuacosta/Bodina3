package com.example.it00046.bodina3.Classes.Params;

import java.io.Serializable;

/**
 * Created by Joshua on 14/04/2015.
 */
public class PARAssociacio implements Serializable {
    public String CodiEntitat;
    public String NomEntitat;
    public String Descripcio;
    public String Contacte;
    public String eMail;

    public void PARAssociacio(){
        this.CodiEntitat = new String();
        this.NomEntitat = new String();
        this.Descripcio = new String();
        this.Contacte = new String();
        this.eMail = new String();
    }
}
