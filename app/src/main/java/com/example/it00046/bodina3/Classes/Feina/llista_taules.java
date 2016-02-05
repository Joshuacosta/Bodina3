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


import java.util.ArrayList;

public class llista_taules {
    private ArrayList<taula> g_Llista;
    private PointF g_Guia = new PointF();
    public RectF g_BoundsSalo = new RectF();
    public int g_SeparacioTaules = 0;

    public llista_taules(){

        g_Llista = new ArrayList<>();
    }

    public void Afegir (taula p_Taula, DistribucioEdicio p_Distribucio){
        int l_NumTaules, l_PosX, l_PosY;

        g_Llista.add(p_Taula);
        if (p_Distribucio != null){
            TaulaView l_Taula = new TaulaView(Globals.g_Native);
            RelativeLayout.LayoutParams l_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            l_Taula.ExpresaTaula(p_Taula.Taula, true, p_Distribucio.g_UnitatX);
            p_Distribucio.addView(l_Taula, l_params);

            if (g_Guia == null){
                g_Guia = new PointF(g_BoundsSalo.left, g_BoundsSalo.top);
            }
            l_Taula.MouTaula(new PointF(g_Guia.x + g_SeparacioTaules, g_Guia.y + g_SeparacioTaules), null);
            /*
            // Posem la taula al centre fent que sembli que ve de la esquerra
            l_Taula.setX(p_Distribucio.g_CenterX);
            l_Taula.setX(2500);
            l_Taula.animate().translationX(p_Distribucio.g_CenterX).translationY(p_Distribucio.g_CenterY);
            */
            /*
            l_Taula.setX(p_Distribucio.g_CenterX);
            l_Taula.setX(2500);
            l_NumTaules = g_Llista.size();
            //
            if (g_DarreraPosicio == null){
                g_DarreraPosicio = new PointF(g_BoundsSalo.left, g_BoundsSalo.top);
            }
            //if (l_NumTaules == 1){
                l_PosX = Math.round(g_DarreraPosicio.x + g_SeparacioTaules);
                l_PosY = Math.round(g_DarreraPosicio.y + g_SeparacioTaules);
                l_Taula.animate().translationX(l_PosX).translationY(l_PosY);
            //}
            g_DarreraPosicio = new PointF(l_PosX, l_PosY);
            */

            //g_Guia = new Point();
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
            g_Llista.get(i).DIB_Actiu = false;
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
