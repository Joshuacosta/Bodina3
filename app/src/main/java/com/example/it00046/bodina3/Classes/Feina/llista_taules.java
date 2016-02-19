package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.it00046.bodina3.Classes.DistribucioEdicio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.TaulaView;
import com.example.it00046.bodina3.R;


import java.util.ArrayList;

public class llista_taules {
    private ArrayList<taula> g_Llista;
    private PointF g_Guia = null;
    public RectF g_BoundsSalo = new RectF();
    public int g_SeparacioTaules = 0;
    public DistribucioEdicio g_Distribucio;

    public llista_taules(){
        g_Llista = new ArrayList<>();
    }

    public void Afegir (taula p_Taula){
        g_Llista.add(p_Taula);
        if (g_Distribucio != null){
            p_Taula.View = new TaulaView(Globals.g_Native);
            // Expresem la taula i recuperem el params que hem de fer servir per afegir a la distribucio
            RelativeLayout.LayoutParams l_params = p_Taula.View.ExpresaTaula(p_Taula.Taula, true, g_Distribucio.g_UnitatX, false);
            g_Distribucio.addView(p_Taula.View, 0, l_params);

            if (g_Guia == null){
                g_Guia = new PointF(g_BoundsSalo.left, g_BoundsSalo.top);
            }

            p_Taula.View.MouTaula(new PointF(g_Guia.x + g_SeparacioTaules, g_Guia.y + g_SeparacioTaules), null);

        }
    }

    public void Activar(int p_i){
        g_Llista.get(p_i).DIB_Actiu = true;
    }

    public void DesActivar(int p_i){
        g_Llista.get(p_i).DIB_Actiu = false;
    }

    public void DesmarcarActives(){
        for (int i=0; i < g_Llista.size(); i++){
            g_Llista.get(i).View.Activacio(false);
        }
    }

    public void DibuixaTaules(){
        for (int i=0; i < g_Llista.size(); i++){

        }
    }

    public taula element(int i){
        return g_Llista.get(i);
    }

    public int Tamany(){
        return g_Llista.size();
    }
}
