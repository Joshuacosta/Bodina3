package com.example.it00046.bodina3.Classes.CustomList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 25/05/2015.
 */
public final class RecercaEntitats extends ArrayAdapter<Entitat> {

    private Context l_context;
    private int l_textViewResourceId;
    public int lPosicio = -1;

    public RecercaEntitats(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        l_context = context;
        l_textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //LayoutInflater inflater = LayoutInflater.from(Globals.g_Recerca);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.lycustom_recerca_entitats, null);
            convertView = inflater.inflate(l_textViewResourceId, null);
        }

        ((TextView)convertView.findViewById(R.id.NomEntitatRecerca)).setText(getItem(position).Nom);
        ((TextView)convertView.findViewById(R.id.AdresaRecerca)).setText(getItem(position).Adresa);
        ((TextView)convertView.findViewById(R.id.ContacteRecerca)).setText(getItem(position).Contacte);
        ((TextView)convertView.findViewById(R.id.TelefonRecerca)).setText(getItem(position).Telefon);
        ((TextView)convertView.findViewById(R.id.eMailRecerca)).setText(getItem(position).eMail);

        //
        if (lPosicio == -1){
            lPosicio = position;
        }

        // Resets the toolbar to be closed
        View toolbar = convertView.findViewById(R.id.toolbar);
        ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -80;
        toolbar.setVisibility(View.GONE);
        return convertView;
    }
}
