package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaAssociacions extends ArrayAdapter<Associacio> {

    private Context l_context;

    public LVWLlistaAssociacions(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        l_context = context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        Associacio l_Associacio;
        TextView l_TXT_NomEntitat, l_TXT_AdresaEntitat, l_TXT_ContacteEntitat, l_TXT_TelefonEntitat, l_TXT_eMailEntitat;
        TextView l_TXT_Descripcio, l_TXT_Dates;

        l_Associacio = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_associacions, null);
        }
        // Omplim camps visibles (omplim mes tard el camp de dates segons estat associacio)
        l_TXT_NomEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTNomEntitat);
        l_TXT_NomEntitat.setText(l_Associacio.entitat.Nom);
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTDescripcio);
        l_TXT_Descripcio.setText(l_Associacio.Descripcio);
        l_TXT_Dates = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTDates);
        // Omplim camps "invisibles"
        l_TXT_AdresaEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTAdresaEntitat);
        l_TXT_AdresaEntitat.setText(l_Associacio.entitat.Adresa);
        l_TXT_ContacteEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTContacteEntitat);
        l_TXT_ContacteEntitat.setText(l_Associacio.entitat.Contacte);
        l_TXT_TelefonEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTTelefonEntitat);
        l_TXT_TelefonEntitat.setText(l_Associacio.entitat.Telefon);
        l_TXT_eMailEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTeMailEntitat);
        l_TXT_eMailEntitat.setText(l_Associacio.entitat.eMail);
        // Validem si la entitat esta de baixa
        if(l_Associacio.entitat.Estat == Globals.k_EstatBaixa){
            l_TXT_NomEntitat.setText(l_TXT_NomEntitat.getText() + " " + Globals.g_Native.getString(R.string.EntitatBaixa));
            l_TXT_NomEntitat.setBackgroundResource(R.color.red);
        }
        // Mostrem estat de la associacio amb l'entitat i les dates que hem de expressar
        switch (l_Associacio.Estat) {
            case Globals.k_EstatPendent:
                l_TXT_NomEntitat.setText(l_TXT_NomEntitat.getText() + " " + Globals.g_Native.getString(R.string.AssociacioPendent));
                l_TXT_NomEntitat.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
                l_TXT_Dates.setText(Globals.g_Native.getString(R.string.DataPeticio) + ":" + l_Associacio.DataPeticio);
                break;
            case Globals.k_EstatActiva:
                l_TXT_Dates.setText(Globals.g_Native.getString(R.string.DataAlta) + ":" + l_Associacio.DataAlta);
                l_TXT_Dates.setText(l_Associacio.DataAlta);
                break;
            case Globals.k_EstatBaixa:
                l_TXT_NomEntitat.setText(l_TXT_NomEntitat.getText() + " " + Globals.g_Native.getString(R.string.AssociacioBaixa));
                l_TXT_NomEntitat.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                l_TXT_Dates.setText(Globals.g_Native.getString(R.string.DataAlta) + ":" + l_Associacio.DataAlta + " -> " +
                                    Globals.g_Native.getString(R.string.DataBaixa) + ":" + l_Associacio.DataFi);
                break;
            case Globals.k_EstatRebutjat:
                l_TXT_NomEntitat.setText(l_TXT_NomEntitat.getText() + " " + Globals.g_Native.getString(R.string.AssociacioRebutjada));
                l_TXT_NomEntitat.setTextColor(Globals.g_Native.getResources().getColor(R.color.darkpurple));
                l_TXT_Dates.setText(l_Associacio.DataPeticio);
                break;
        }
        return p_convertView;
    }
}
