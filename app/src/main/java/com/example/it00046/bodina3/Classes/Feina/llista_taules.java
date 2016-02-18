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
            //RelativeLayout.LayoutParams l_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //RelativeLayout.LayoutParams l_params = new RelativeLayout.LayoutParams(50, 50);
            //l_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            //p_Taula.View.setLayoutParams(new RelativeLayout.LayoutParams(50, 50)); //p_Taula.View.setMinimumWidth(50);
            RelativeLayout.LayoutParams l_params;
            p_Taula.View.setBackgroundColor(Globals.g_Native.getResources().getColor(R.color.red));
            l_params = p_Taula.View.ExpresaTaula2(p_Taula.Taula, true, g_Distribucio.g_UnitatX, false);
            g_Distribucio.addView(p_Taula.View, 0, l_params);

            if (g_Guia == null){
                g_Guia = new PointF(g_BoundsSalo.left, g_BoundsSalo.top);
            }

            p_Taula.View.MouTaula(new PointF(g_Guia.x + g_SeparacioTaules, g_Guia.y + g_SeparacioTaules), null);

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
