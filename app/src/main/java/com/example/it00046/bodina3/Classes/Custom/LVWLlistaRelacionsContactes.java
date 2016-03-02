package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.RelacioContactes;
import com.example.it00046.bodina3.Classes.Entitats.RelacioConvidats;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaRelacionsContactes extends ArrayAdapter<RelacioContactes> {

    private Context g_context;

    public LVWLlistaRelacionsContactes(Context p_context, int p_textViewResourceId) {
        super(p_context, p_textViewResourceId);
        g_context = p_context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        RelacioContactes l_RelacioContactes;
        TextView l_TXT_Nom;

        l_RelacioContactes = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_relacionscontactes, null);
        }
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaRelacionsContactesTXTDescripcio);
        l_TXT_Nom.setText(l_RelacioContactes.Descripcio);
        p_convertView.setTag(l_RelacioContactes);
        return p_convertView;
    }
}
