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
        TextView l_TXT_Descripcio, l_TXT_DataAlta_o_Peticio, l_TXT_DataFi;

        l_Associacio = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_associacions, null);
        }
        // Omplim camps visibles (omplim mes tard el camp de data perque pot ser la de alta o la de peticio)
        l_TXT_NomEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTNomEntitat);
        l_TXT_NomEntitat.setText(l_Associacio.entitat.Nom);
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTDescripcio);
        l_TXT_Descripcio.setText(l_Associacio.Descripcio);
        l_TXT_DataAlta_o_Peticio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTDataAltaoPeticio);
        l_TXT_DataFi = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTDataFi);
        l_TXT_DataFi.setText(l_Associacio.DataFi);
        l_TXT_DataFi.setVisibility(View.INVISIBLE);
        // Omplim camps "invisibles"
        l_TXT_AdresaEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTAdresaEntitat);
        l_TXT_AdresaEntitat.setText(l_Associacio.entitat.Adresa);
        l_TXT_ContacteEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTContacteEntitat);
        l_TXT_ContacteEntitat.setText(l_Associacio.entitat.Contacte);
        l_TXT_TelefonEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTTelefonEntitat);
        l_TXT_TelefonEntitat.setText(l_Associacio.entitat.Telefon);
        l_TXT_eMailEntitat = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsTXTeMailEntitat);
        l_TXT_eMailEntitat.setText(l_Associacio.entitat.eMail);
        // Mostrem estat de la associacio amb l'entitat
        if(l_Associacio.entitat.Estat == Globals.k_EstatBaixa){
            l_TXT_NomEntitat.setBackgroundResource(R.color.red);
            l_TXT_DataAlta_o_Peticio.setText(l_Associacio.DataAlta);
        }
        else{
            if (l_Associacio.Estat == Globals.k_EstatPendent){
                l_TXT_NomEntitat.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
                l_TXT_DataAlta_o_Peticio.setText(l_Associacio.DataPeticio);
            }
            else{
                l_TXT_DataAlta_o_Peticio.setText(l_Associacio.DataAlta);
                if (l_Associacio.Estat == Globals.k_EstatBaixa){
                    l_TXT_NomEntitat.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                    l_TXT_DataFi.setVisibility(View.VISIBLE);
                }
            }
        }
        return p_convertView;
    }
}
