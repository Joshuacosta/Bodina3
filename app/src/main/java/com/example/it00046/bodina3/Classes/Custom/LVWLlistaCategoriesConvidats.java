package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaCategoriesConvidats extends ArrayAdapter<CategoriaConvidats> {

    private Context g_context;

    public LVWLlistaCategoriesConvidats(Context p_context, int p_textViewResourceId) {
        super(p_context, p_textViewResourceId);
        g_context = p_context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        CategoriaConvidats l_CategoriaConvidats;
        TextView l_TXT_Nom;

        l_CategoriaConvidats = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_categoriesconvidats, null);
        }
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCategoriesConvidatsTXTDescripcio);
        l_TXT_Nom.setText(l_CategoriaConvidats.Descripcio);
        p_convertView.setTag(l_CategoriaConvidats);
        return p_convertView;
    }
}
