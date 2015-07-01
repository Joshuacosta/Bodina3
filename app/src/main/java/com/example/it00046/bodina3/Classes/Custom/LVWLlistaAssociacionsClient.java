package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Entitats.EntitatClient;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaAssociacionsClient extends ArrayAdapter<Associacio> {

    private Context l_context;

    public LVWLlistaAssociacionsClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        l_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Associacio l_Associacio;
        TextView tv_Nom;
        l_Associacio = getItem(position);
        //LayoutInflater inflater = LayoutInflater.from(Globals.g_Recerca);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ly_entitat_listview_linia, null);
        }
        tv_Nom = (TextView)convertView.findViewById(R.id.ly_entitat_listview_linia_Nom);
        tv_Nom.setText(l_Associacio.NomEntitat);
        // Mostrem estat de la associació amb l'entitat
        if(l_Associacio.EstatEntitat == Globals.k_EntitatBaixa){
            tv_Nom.setBackgroundResource(R.color.red);
        }
        else{
            if (l_Associacio.DataAlta == ""){
                tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
            }
            else{
                if (l_Associacio.DataFi != ""){
                    tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                }
            }
        }
        return convertView;
    }
}
