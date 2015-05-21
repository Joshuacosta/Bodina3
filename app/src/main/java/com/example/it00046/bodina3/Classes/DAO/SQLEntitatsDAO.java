package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SpnEntitat;
import com.example.it00046.bodina3.Classes.Tipus.Client;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.Classes.params.PAREntitat;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by it00046 on 13/05/2015.
 */
public class SQLEntitatsDAO {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_Entitat = "entitat";
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TEntitats_Codi);
    //
    // Funci� per llegir les entitats de un pais, retornem la info per un Spinner
    // AIX� DEL SPINNER HO TINDRES QUE PARAMETRITZAR PER ALTRES SITUACIONS!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    public static void F_Entitats (String p_Pais, final Spinner SPN_EntitatsClient){
        final List <SpnEntitat> l_Entitats = new ArrayList<SpnEntitat>();

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Pais), p_Pais);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_LlegirEntitatsPais);
            PhpJson.post("Entitats.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject p_entitats) {
                    try {
                        String l_Resposta = p_entitats.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Llegim les entitats
                            JSONArray l_ArrayEntitats = null;
                            l_ArrayEntitats = p_entitats.getJSONArray(Globals.g_Native.getString(R.string.TEntitats));
                            for (int i = 0; i < l_ArrayEntitats.length(); i++) {
                                JSONObject l_entitatServidor = l_ArrayEntitats.getJSONObject(i);
                                Entitat l_entitat = new Entitat();
                                // Pasa les dades del objecte JSON a la Entitat
                                l_entitat.Codi = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Codi));
                                l_entitat.Nom = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Nom));
                                l_entitat.Adresa = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Adresa));
                                l_entitat.Telefon = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Telefon));
                                l_entitat.Contacte = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Contacte));
                                l_entitat.eMail = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_eMail));
                                l_entitat.Pais = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Pais));
                                l_entitat.Estat = l_entitatServidor.getInt(Globals.g_Native.getString(R.string.TEntitats_Estat));
                                // Carreguem
                                SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom);
                                l_Entitats.add(l_spinner);
                            }
                            ArrayAdapter<SpnEntitat> dataAdapter = new ArrayAdapter<SpnEntitat>(Globals.g_Native.getApplicationContext(),android.R.layout.simple_spinner_item, l_Entitats);
                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            SPN_EntitatsClient.setAdapter(dataAdapter);
                        }
                        else {
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                    Globals.g_Native.getString(R.string.error_greu));
                        }
                    } catch (JSONException e) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                Globals.g_Native.getString(R.string.error_greu));
                    }
                }
            });
        }
        else{
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                    Globals.g_Native.getString(R.string.error_greu));
        }
    }
    //
    // Funci� per llegir les entitats de un pais, retornem la info per un Spinner
    // AIX� DEL SPINNER HO TINDRES QUE PARAMETRITZAR PER ALTRES SITUACIONS!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    public static void F_LlistaEntitats (String p_Pais, final ListView LV_Entitats){
        final List <SpnEntitat> l_Entitats = new ArrayList<SpnEntitat>();

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Pais), p_Pais);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_LlegirEntitatsPais);
            PhpJson.post("Entitats.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject p_entitats) {
                    try {
                        String l_Resposta = p_entitats.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            //
                            //ArrayAdapter<String> listAdapter = new CustomListAdapter(Globals.g_Native.getApplicationContext(), R.layout.list_item);
                            ArrayAdapter<Entitat> listAdapter = new CustomListAdapter(Globals.g_Native.getApplicationContext(), R.layout.list_item);

                            // Llegim les entitats
                            JSONArray l_ArrayEntitats = null;
                            l_ArrayEntitats = p_entitats.getJSONArray(Globals.g_Native.getString(R.string.TEntitats));
                            for (int i = 0; i < l_ArrayEntitats.length(); i++) {
                                JSONObject l_entitatServidor = l_ArrayEntitats.getJSONObject(i);
                                Entitat l_entitat = new Entitat();
                                // Pasa les dades del objecte JSON a la Entitat
                                l_entitat.Codi = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Codi));
                                l_entitat.Nom = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Nom));
                                l_entitat.Adresa = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Adresa));
                                l_entitat.Telefon = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Telefon));
                                l_entitat.Contacte = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Contacte));
                                l_entitat.eMail = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_eMail));
                                l_entitat.Pais = l_entitatServidor.getString(Globals.g_Native.getString(R.string.TEntitats_Pais));
                                l_entitat.Estat = l_entitatServidor.getInt(Globals.g_Native.getString(R.string.TEntitats_Estat));
                                // Carreguem
                                SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom);
                                l_Entitats.add(l_spinner);


                                listAdapter.add(l_entitat);
                            }
                            //ArrayAdapter<SpnEntitat> dataAdapter = new ArrayAdapter<SpnEntitat>(Globals.g_Native.getApplicationContext(),android.R.layout.simple_spinner_item, l_Entitats);
                            // Drop down layout style - list view with radio button
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to list
                            /*
                            final ArrayAdapter dataAdapter = new ArrayAdapter(Globals.g_Native.getApplicationContext(),
                                    android.R.layout.simple_list_item_1, l_Entitats);
                            LV_Entitats.setAdapter(dataAdapter);
                            */

                            LV_Entitats.setAdapter(listAdapter);

                        }
                        else {
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                    Globals.g_Native.getString(R.string.error_greu));
                        }
                    } catch (JSONException e) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                Globals.g_Native.getString(R.string.error_greu));
                    }
                }
            });
        }
        else{
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                    Globals.g_Native.getString(R.string.error_greu));
        }
    }

    static class CustomListAdapter extends ArrayAdapter<Entitat> {

        public CustomListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(Globals.g_Native);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
            }

            ((TextView)convertView.findViewById(R.id.NomEntitatRecerca)).setText(getItem(position).Nom);
            ((TextView)convertView.findViewById(R.id.AdresaRecerca)).setText(getItem(position).Adresa);
            ((TextView)convertView.findViewById(R.id.ContacteRecerca)).setText(getItem(position).Contacte);

            // Resets the toolbar to be closed
            View toolbar = convertView.findViewById(R.id.toolbar);
            ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
            toolbar.setVisibility(View.GONE);

            return convertView;
        }
    }
}
