package com.example.it00046.bodina3.Classes.Feina;

import java.util.ArrayList;

public class llista_taules {
    ArrayList<taula> llista;

    public llista_taules(){
        llista = new ArrayList<>();
    }

    public void Afegir (taula p_Taula){
        llista.add(p_Taula);
    }

    public void Activar(int p_i){
        llista.get(p_i).DIB_Actiu = true;
    }

    public void DesActivar(int p_i){
        llista.get(p_i).DIB_Actiu = false;
    }

    public void DesmarcarActives(){
        for (int i=0; i < llista.size(); i++){
            llista.get(i).DIB_Actiu = false;
        }
    }

    public taula element(int i){
        return llista.get(i);
    }

    public int Tamany(){
        return llista.size();
    }
}
