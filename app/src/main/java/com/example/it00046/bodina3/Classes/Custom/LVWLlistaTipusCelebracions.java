package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Invitacio;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaTipusCelebracions extends ArrayAdapter<TipusCelebracio> {

    private Context g_context;

    public LVWLlistaTipusCelebracions(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        g_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipusCelebracio l_TipusCelebracio;
        TextView l_TXT_Nom;

        l_TipusCelebracio = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.linia_lvw_llista_tipuscelebracions, null);
        }
        l_TXT_Nom = (TextView)convertView.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsTXTDescripcio);
        l_TXT_Nom.setText(l_TipusCelebracio.Descripcio);
        return convertView;
    }
}
