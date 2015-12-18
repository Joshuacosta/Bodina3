package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARSaloPlanolClient;
import com.example.it00046.bodina3.Classes.PlanolVista;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaSalonsClient extends ArrayAdapter<SaloClient> {

    private Context g_context;

    public LVWLlistaSalonsClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        g_context = context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        SaloClient l_Salo;
        PlanolVista l_Draw;
        TextView l_TXT_Nom;

        l_Salo = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_salons_client, null);
        }
        // Omplim camps visibles
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaSalonsClientTXTNom);
        l_TXT_Nom.setText(l_Salo.Nom);
        // Planol
        if (l_Salo.g_Planol.size() > 0) {
            l_Draw = (PlanolVista)p_convertView.findViewById(R.id.LiniaLVWLlistaSalonsClientVIWDrawing);
            l_Draw.DibuixaPlanol(l_Salo.g_Planol);
        }
        // Guardem les dades al tag
        p_convertView.setTag(l_Salo);

        return p_convertView;
    }
}
