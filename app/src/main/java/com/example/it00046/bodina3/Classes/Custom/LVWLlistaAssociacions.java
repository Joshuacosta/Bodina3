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
    public View getView(int position, View convertView, ViewGroup parent) {
        Associacio l_Associacio;
        TextView l_TXT_Nom;

        l_Associacio = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.LiniaLVWLlistaAssociacions, null);
        }
        l_TXT_Nom = (TextView)convertView.findViewById(R.id.LiniaLVWLlistaAssociacionsNom);
        l_TXT_Nom.setText(l_Associacio.entitat.Nom);
        // Mostrem estat de la associació amb l'entitat
        if(l_Associacio.entitat.Estat == Globals.k_EstatBaixa){
            l_TXT_Nom.setBackgroundResource(R.color.red);
        }
        else{
            if (l_Associacio.Estat == Globals.k_EstatPendent){
                l_TXT_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
            }
            else{
                if (l_Associacio.Estat == Globals.k_EstatBaixa){
                    l_TXT_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                }
            }
        }
        return convertView;
    }
}
