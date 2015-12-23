package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;
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
        TextView l_TXT_Nom, l_TXT_Descripcio, l_TXT_Capacitat;

        l_Salo = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_salons_client, null);
        }
        // Omplim camps visibles
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaSalonsClientTXTNom);
        l_TXT_Nom.setText(l_Salo.Nom);
        // Omplim camps no visibles
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaSalonsClientTXTDescripcio);
        l_TXT_Descripcio.setText(l_Salo.Descripcio);
        l_TXT_Capacitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaSalonsClientTXTCapacitat);
        l_TXT_Capacitat.setText(Globals.g_Native.getString(R.string.SalonsClientPralLITCapacitat) + " : " + Integer.toString(l_Salo.Capacitat));
        // Guardem les dades del salo al tag
        p_convertView.setTag(l_Salo);

        return p_convertView;
    }
}
