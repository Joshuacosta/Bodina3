package com.example.it00046.bodina3.Classes.Feina;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
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
        boolean l_llocTrobat = false;
        int l_canvilinia = 0;
        Rect l_Espai;

        if (g_Distribucio != null){
            p_Taula.View = new TaulaView(Globals.g_Native);
            // Expresem la taula i recuperem el params que hem de fer servir per afegir a la distribucio
            RelativeLayout.LayoutParams l_params = p_Taula.View.ExpresaTaula(p_Taula.Taula, true, g_Distribucio.g_UnitatX, true);
            g_Distribucio.addView(p_Taula.View, 0, l_params);
            // Calculem espai
            l_Espai = new Rect(Math.round(g_BoundsSalo.left) + g_SeparacioTaules,
                                Math.round(g_BoundsSalo.top) + g_SeparacioTaules,
                                Math.round(g_BoundsSalo.left + (p_Taula.Taula.AmpladaDiametre/g_Distribucio.g_UnitatX) + g_SeparacioTaules),
                                Math.round(g_BoundsSalo.top + (p_Taula.Taula.AmpladaDiametre/g_Distribucio.g_UnitatX) + g_SeparacioTaules));
            // Busquem on posar-la, comencem per la cantonada superior i iniciem el proces)
            while (l_llocTrobat == false){
                l_llocTrobat = true;
                for (int i=0; i < g_Llista.size(); i++){
                    if (g_Llista.get(i).View.Posicionament(l_Espai)) {
                        l_llocTrobat = false;
                        break;
                    }
                }
                if (l_llocTrobat){
                    p_Taula.Punt = new PointF(l_Espai.left, l_Espai.top);
                }
                else{
                    // Desplacem fins al final del salo
                    if (l_Espai.right < (g_BoundsSalo.right - g_SeparacioTaules)) {
                        l_Espai.offset((p_Taula.Taula.AmpladaDiametre / g_Distribucio.g_UnitatX) + g_SeparacioTaules, 0);
                    }
                    else{
                        // Hem de canviar de linia perque ens sortim de l'area
                        l_canvilinia++;
                        l_Espai = new Rect(Math.round(g_BoundsSalo.left + g_SeparacioTaules),
                                           Math.round(g_BoundsSalo.top + g_SeparacioTaules) + (p_Taula.Taula.AmpladaDiametre / g_Distribucio.g_UnitatX + g_SeparacioTaules)*l_canvilinia,
                                           Math.round(g_BoundsSalo.left + (p_Taula.Taula.AmpladaDiametre/g_Distribucio.g_UnitatX) + g_SeparacioTaules),
                                           Math.round(g_BoundsSalo.top + (p_Taula.Taula.AmpladaDiametre/g_Distribucio.g_UnitatX  + g_SeparacioTaules)*(l_canvilinia+1)));
                    }
                }
            }
            // Afegeixo la taula a la llista i la dibuixem
            g_Llista.add(p_Taula);
            p_Taula.View.PosaTaula(new PointF(p_Taula.Punt.x, p_Taula.Punt.y));
        }
    }


    public void Posar (taula p_Taula){
        g_Llista.add(p_Taula);
        if (g_Distribucio != null){
            p_Taula.View = new TaulaView(Globals.g_Native);
            // Expresem la taula i recuperem el params que hem de fer servir per afegir a la distribucio
            RelativeLayout.LayoutParams l_params = p_Taula.View.ExpresaTaula(p_Taula.Taula, true, g_Distribucio.g_UnitatX, true);
            g_Distribucio.addView(p_Taula.View, 0, l_params);
            p_Taula.View.PosaTaula(new PointF(p_Taula.Punt.x, p_Taula.Punt.y));
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
