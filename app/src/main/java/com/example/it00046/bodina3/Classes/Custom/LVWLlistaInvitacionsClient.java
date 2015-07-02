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
public class LVWLlistaInvitacionsClient extends ArrayAdapter<Invitacio> {

    private Context l_context;

    public LVWLlistaInvitacionsClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        l_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Invitacio l_Invitacions;
        TextView tv_Nom;
        l_Invitacions = getItem(position);
        //LayoutInflater inflater = LayoutInflater.from(Globals.g_Recerca);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ly_entitat_listview_linia, null);
        }
        tv_Nom = (TextView)convertView.findViewById(R.id.ly_entitat_listview_linia_Nom);
        tv_Nom.setText(l_Invitacions.entitat.Nom);
        // Mostrem estat de la associació amb l'entitat
        if(l_Invitacions.entitat.Estat == Globals.k_EstatBaixa){
            tv_Nom.setBackgroundResource(R.color.red);
        }
        else{
            if (l_Invitacions.Estat == Globals.k_EstatPendent){
                tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
            }
            else{
                if (l_Invitacions.Estat == Globals.k_EstatBaixa){
                    tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                }
            }
        }
        return convertView;
    }
}
