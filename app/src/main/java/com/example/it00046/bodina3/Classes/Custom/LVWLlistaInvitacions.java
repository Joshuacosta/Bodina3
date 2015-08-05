package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Invitacio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaInvitacions extends ArrayAdapter<Invitacio> {

    private Context g_context;

    public LVWLlistaInvitacions(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        g_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Invitacio l_Invitacio;
        TextView l_TXT_Nom, l_TXT_DataInvitacio, l_TXT_DataResolucio;

        l_Invitacio = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.linia_lvw_llista_invitacions, null);
        }
        l_TXT_Nom = (TextView)convertView.findViewById(R.id.LiniaLVWLlistaInvitacionsTXTNom);
        l_TXT_Nom.setText(l_Invitacio.entitat.Nom);
        l_TXT_DataInvitacio = (TextView)convertView.findViewById(R.id.LiniaLVWLlistaInvitacionsTXTDataInvitacio);
        l_TXT_DataInvitacio.setText(l_Invitacio.DataInvitacio);
        l_TXT_DataResolucio = (TextView)convertView.findViewById(R.id.LiniaLVWLlistaInvitacionsTXTDataResolucio);
        l_TXT_DataResolucio.setVisibility(View.INVISIBLE);
        // Mostrem estat de la associacio amb l'entitat
        if(l_Invitacio.entitat.Estat == Globals.k_EntitatBaixa){
            l_TXT_Nom.setBackgroundResource(R.color.red);
        }
        else{
            if (l_Invitacio.Estat == Globals.k_InvitacioPendent){
                l_TXT_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
            }
            else{
                if (l_Invitacio.Estat == Globals.k_InvitacioRebutjat){
                    l_TXT_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                    l_TXT_DataResolucio.setVisibility(View.VISIBLE);
                    l_TXT_DataResolucio.setText(l_Invitacio.DataResolucio);
                }
            }
        }
        return convertView;
    }
}
