package com.example.it00046.bodina3.Classes.Entitats;

/**
 * Created by Joshua on 27/02/2016.
 */
public class DistribucioTaula {
    public CelebracioClient Celebracio;
    public Distribucio Distribucio;
    public Taula Taula;

    public DistribucioTaula(){
        Celebracio = new CelebracioClient();
        Distribucio = new Distribucio();
        Taula = new Taula();
    }
}
