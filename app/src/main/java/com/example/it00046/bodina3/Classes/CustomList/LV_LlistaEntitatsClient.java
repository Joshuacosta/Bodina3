package com.example.it00046.bodina3.Classes.CustomList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Tipus.EntitatClient;
import com.example.it00046.bodina3.R;

import org.w3c.dom.Text;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LV_LlistaEntitatsClient extends ArrayAdapter<EntitatClient> {

    private Context l_context;

    public LV_LlistaEntitatsClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        l_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntitatClient l_Entitat;
        TextView tv_Nom;
        l_Entitat = getItem(position);
        //LayoutInflater inflater = LayoutInflater.from(Globals.g_Recerca);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ly_entitat_listview_linia, null);
        }
        tv_Nom = (TextView)convertView.findViewById(R.id.ly_entitat_listview_linia_Nom);
        tv_Nom.setText(l_Entitat.NomEntitat);
        // Mostrem en bold si ha sigut modificat
        if (l_Entitat.HiHanCanvis){
            tv_Nom.setTypeface(null, Typeface.BOLD);
        }
        // Mostrem estat de la associació amb l'entitat
        if(l_Entitat.EstatEntitat == Globals.k_EntitatBaixa){
            tv_Nom.setBackgroundResource(R.color.red);
        }
        else{
            if (l_Entitat.DataAltaAssociacio == ""){
                tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
            }
            else{
                if (l_Entitat.DataFiAssociacio != ""){
                    tv_Nom.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                }
            }
        }
        return convertView;
    }
}
