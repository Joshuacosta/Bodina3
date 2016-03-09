package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaConvidatsSimple extends ArrayAdapter<Convidat> {

    private Context g_context;

    public LVWLlistaConvidatsSimple(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        g_context = context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        Convidat l_Convidat;
        TextView l_TXT_Nom, l_TXT_NomParella, l_TXT_Adresa, l_TXT_Contacte, l_TXT_Telefon;
        TextView l_TXT_eMail;

        l_Convidat = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_convidats_simple, null);
        }
        // Omplim camps visibles
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsSimpleTXTNom);
        l_TXT_Nom.setText(l_Convidat.Nom);
        l_TXT_NomParella = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsSimpleTXTNomParella);
        if (l_Convidat.Tipus == Convidat.k_TipusParella) {
            l_TXT_NomParella.setText(l_Convidat.Parella.Nom);
        }
        // Validem si la entitat esta de baixa
        if(l_Convidat.Estat == Convidat.k_EstatEsborrat){
            l_TXT_Nom.setText(l_TXT_Nom.getText() + " " + Globals.g_Native.getString(R.string.ConvidatEsborrat));
            l_TXT_Nom.setBackgroundResource(R.color.red);
        }
        p_convertView.setTag(l_Convidat);

        return p_convertView;
    }
}
