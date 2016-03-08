package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaConvidatsSimple extends ArrayAdapter<Convidat> {

    private Context g_context;

    public LVWLlistaConvidatsSimple(Context p_context, int p_textViewResourceId) {
        super(p_context, p_textViewResourceId);
        g_context = p_context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        Convidat l_Convidat;
        TextView l_TXT_Nom;

        l_Convidat = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_taulacomensals_convidats_seleccio, null);
        }
        // Mostrem nom del convidat
        l_TXT_Nom = (TextView) p_convertView.findViewById(R.id.LiniaLVWLlistaTaulesConvidatsSeleccioTXTNom);
        l_TXT_Nom.setText(l_Convidat.Nom);

        p_convertView.setTag(l_Convidat);
        return p_convertView;
    }
}
