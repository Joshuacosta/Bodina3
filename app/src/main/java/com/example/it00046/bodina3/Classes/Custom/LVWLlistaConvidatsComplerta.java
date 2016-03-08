package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaConvidatsComplerta extends ArrayAdapter<Convidat> {

    private Context g_context;

    public LVWLlistaConvidatsComplerta(Context context, int textViewResourceId) {
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
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_convidats_complerta, null);
        }
        // Omplim camps visibles
        l_TXT_Nom = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTNom);
        l_TXT_Nom.setText(l_Convidat.Nom);
        l_TXT_NomParella = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTNomParella);
        if (l_Convidat.Tipus == Convidat.k_TipusParella) {
            l_TXT_NomParella.setText(l_Convidat.Parella.Nom);
        }
        // Omplim camps "invisibles"
        l_TXT_Adresa = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTAdresa);
        l_TXT_Adresa.setText(l_Convidat.Adresa);
        l_TXT_Contacte = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTContacte);
        l_TXT_Contacte.setText(l_Convidat.Contacte);
        l_TXT_Telefon = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTTelefon);
        l_TXT_Telefon.setText(l_Convidat.Telefon);
        l_TXT_eMail = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaConvidatsTXTeMail);
        l_TXT_eMail.setText(l_Convidat.eMail);
        // Validem si la entitat esta de baixa
        if(l_Convidat.Estat == Convidat.k_EstatEsborrat){
            l_TXT_Nom.setText(l_TXT_Nom.getText() + " " + Globals.g_Native.getString(R.string.ConvidatEsborrat));
            l_TXT_Nom.setBackgroundResource(R.color.red);
        }
        p_convertView.setTag(l_Convidat);

        return p_convertView;
    }
}
