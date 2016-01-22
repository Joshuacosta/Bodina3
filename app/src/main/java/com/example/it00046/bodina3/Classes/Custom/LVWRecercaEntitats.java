package com.example.it00046.bodina3.Classes.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Entitats.Entitat;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 25/05/2015.
 */
public final class LVWRecercaEntitats extends ArrayAdapter<Entitat> {

    private Context g_context;
    private int g_Layout;
    public int g_Posicio = -1;

    public LVWRecercaEntitats(Context p_context, int p_textViewResourceId) {
        super(p_context, p_textViewResourceId);
        g_context = p_context;
        g_Layout = p_textViewResourceId;
    }

    @Override
    public View getView(int p_position, View p_convertView, ViewGroup p_parent) {

        LayoutInflater inflater = LayoutInflater.from(g_context);
        if (p_convertView == null) {
            p_convertView = inflater.inflate(g_Layout, null);
        }
        ((TextView)p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsNom)).setText(getItem(p_position).Nom);
        ((TextView)p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsTXTAdresa)).setText(getItem(p_position).Adresa);
        ((TextView)p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsTXTContacte)).setText(getItem(p_position).Contacte);
        ((TextView)p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsTXTTelefon)).setText(getItem(p_position).Telefon);
        ((TextView)p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsTXTeMail)).setText(getItem(p_position).eMail);
        //
        if (g_Posicio == -1){
            g_Posicio = p_position;
        }
        // Guardem la info de la entitat com a tag
        p_convertView.setTag(getItem(p_position));
        // Resets the toolbar to be closed
        View toolbar = p_convertView.findViewById(R.id.LiniaLVWRecercaEntitatsLINToolbar);
        ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -80;
        toolbar.setVisibility(View.GONE);
        return p_convertView;
    }

}
