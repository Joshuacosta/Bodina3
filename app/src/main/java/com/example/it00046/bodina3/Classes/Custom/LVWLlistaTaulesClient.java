package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaTaulesClient extends ArrayAdapter<TaulaClient> {

    private Context g_context;

    public LVWLlistaTaulesClient(Context p_context, int p_textViewResourceId) {
        super(p_context, p_textViewResourceId);
        g_context = p_context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        TaulaClient l_TaulaClient;
        TextView l_TXT_Descripcio;

        l_TaulaClient = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_taules_client, null);
        }
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaTaulesClientTXTDescripcio);
        // Mostrem el texte de la descripcio de la taula
        l_TXT_Descripcio.setText(l_TaulaClient.Detall());
        //
        p_convertView.setTag(l_TaulaClient);
        return p_convertView;
    }
}
