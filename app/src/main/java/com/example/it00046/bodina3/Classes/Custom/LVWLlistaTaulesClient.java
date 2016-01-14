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
        String l_DescripcioTaula = new String();

        l_TaulaClient = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_tipuscelebracions, null);
        }
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaTaulesClientTXTDescripcio);
        // Anem a construir el texte de la descricpio de la taula
        l_DescripcioTaula = l_TaulaClient.Detall();
        /*
        switch (l_TaulaClient.Tipus){
            case 0:
                l_DescripcioTaula += " " + Globals.g_Native.getResources().getString(R.string.TipusTaulaRodona);
                l_DescripcioTaula += " " + l_TaulaClient.AmpladaRadi;
                break;
            case 1:
                l_DescripcioTaula += " " + Globals.g_Native.getResources().getString(R.string.TipusTaulaQuadrada);
                l_DescripcioTaula += " " + l_TaulaClient.AmpladaRadi;
                break;
            case 2:
                l_DescripcioTaula += " " + Globals.g_Native.getResources().getString(R.string.TipusTaulaRectangular);
                l_DescripcioTaula += " " + l_TaulaClient.AmpladaRadi + "x" + l_TaulaClient.Llargada;
                break;
        }
        */
        l_TXT_Descripcio.setText(l_DescripcioTaula);
        //
        p_convertView.setTag(l_TaulaClient);
        return p_convertView;
    }
}
