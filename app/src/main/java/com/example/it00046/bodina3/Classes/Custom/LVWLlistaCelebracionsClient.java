package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public class LVWLlistaCelebracionsClient extends ArrayAdapter<CelebracioClient> {

    private Context g_context;

    public LVWLlistaCelebracionsClient(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        g_context = context;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {
        CelebracioClient l_Celebracio;
        TextView l_TXT_NomSalo, l_TXT_Tipus, l_TXT_Descripcio, l_TXT_Convidats, l_TXT_Data;
        TextView l_TXT_Lloc, l_TXT_Contacte;

        l_Celebracio = getItem(p_position);
        LayoutInflater inflater = LayoutInflater.from(this.g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(R.layout.linia_lvw_llista_celebracions_client, null);
        }
        // Omplim camps visibles
        l_TXT_Descripcio = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTDescripcio);
        l_TXT_Descripcio.setText(l_Celebracio.Descripcio);
        l_TXT_NomSalo = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTNomSalo);
        // Manca definir i recuperar el nom del salo
        l_TXT_NomSalo.setText(l_Celebracio.CodiSalo);
        l_TXT_Data = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTData);
        l_TXT_Data.setText(l_Celebracio.Data);
        // Omplim camps "invisibles"
        l_TXT_Tipus = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTTipus);
        // Manca expresar el nom del tipus
        l_TXT_Tipus.setText(l_Celebracio.Tipus);
        l_TXT_Convidats = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTConvidats);
        l_TXT_Convidats.setText(l_Celebracio.Convidats);
        l_TXT_Lloc = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTLloc);
        l_TXT_Lloc.setText(l_Celebracio.Lloc);
        l_TXT_Contacte = (TextView)p_convertView.findViewById(R.id.LiniaLVWLlistaCelebracionsClientTXTContacte);
        l_TXT_Contacte.setText(l_Celebracio.Contacte);
        // Validem si la entitat esta de baixa
        if(l_Celebracio.Estat == Globals.k_CelebracioClientBaixa){
            l_TXT_Descripcio.setText(l_TXT_Descripcio.getText() + " " + Globals.g_Native.getString(R.string.CelebracioBaixa));
            l_TXT_Descripcio.setBackgroundResource(R.color.red);
        }
        p_convertView.setTag(l_Celebracio);

        return p_convertView;
    }
}
