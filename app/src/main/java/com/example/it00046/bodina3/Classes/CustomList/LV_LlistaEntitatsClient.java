package com.example.it00046.bodina3.Classes.CustomList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Tipus.EntitatClient;
import com.example.it00046.bodina3.R;

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

        //LayoutInflater inflater = LayoutInflater.from(Globals.g_Recerca);
        LayoutInflater inflater = LayoutInflater.from(this.l_context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ly_entitat_listview_linia, null);
        }

        ((TextView)convertView.findViewById(R.id.ly_entitat_listview_linia_Nom)).setText(getItem(position).NomEntitat);

        return convertView;
    }
}
