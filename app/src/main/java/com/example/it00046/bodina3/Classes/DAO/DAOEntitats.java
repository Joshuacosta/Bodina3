package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.it00046.bodina3.Classes.Custom.LVWRecercaEntitats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNEntitat;
import com.example.it00046.bodina3.Classes.Entitats.Entitat;
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
public final class DAOEntitats {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    // Camps de entitat
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TEntitats_Codi);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TEntitats_eMail);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TEntitats_Nom);
    private static final String TAG_Pais = Globals.g_Native.getString(R.string.TEntitats_Pais);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TEntitats_Contacte);
    private static final String TAG_Adresa = Globals.g_Native.getString(R.string.TEntitats_Adresa);
    private static final String TAG_Telefon = Globals.g_Native.getString(R.string.TEntitats_Telefon);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TEntitats_Estat);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcio per retornar les entitats de un pais en ListView
    public static void Llegir(String p_Pais, final ListView p_LVW_Entitat, Context p_Context, int p_Layout){
        SRV_LlistaEntitats(p_Pais, p_LVW_Entitat, p_Context, p_Layout);
    }
    // en Spinner
    public static void Llegir(String p_Pais, final Spinner p_SPN_EntitatsClient){
        SRV_LlistaEntitats(p_Pais, p_SPN_EntitatsClient);
    }
    // De Json a Entitat
    public static Entitat JSon(JSONObject p_Entitat){
        return JSONToEntitat(p_Entitat);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funció per llegir del SERVIDOR les entitats de un pais, retornem la info per un ListView
    private static void SRV_LlistaEntitats (String p_Pais, final ListView p_LVW_Entitats, Context p_Context, int p_Layout){
        final ArrayAdapter<Entitat> l_listAdapter = new LVWRecercaEntitats(p_Context, p_Layout);

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
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_entitats) {
                    try {
                        String l_Resposta = p_entitats.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Llegim les entitats
                            JSONArray l_ArrayEntitats = null;
                            l_ArrayEntitats = p_entitats.getJSONArray(Globals.g_Native.getString(R.string.TEntitats));
                            for (int i = 0; i < l_ArrayEntitats.length(); i++) {
                                JSONObject l_entitatServidor = l_ArrayEntitats.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Entitat
                                Entitat l_entitat = JSONToEntitat(l_entitatServidor);
                                // Carreguem
                                l_listAdapter.add(l_entitat);
                            }
                            // Associem
                            p_LVW_Entitats.setAdapter(l_listAdapter);
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
    // Funció per llegir del SERVIDOR les entitats de un pais, retornem la info a un spinner
    private static void SRV_LlistaEntitats (String p_Pais, final Spinner p_SPN_EntitatsClient){
        final List <SPNEntitat> l_Entitats = new ArrayList<SPNEntitat>();

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
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_entitats) {
                    try {
                        String l_Resposta = p_entitats.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Posem a la llista la entrada de "Seleccioni..."
                            SPNEntitat l_SelectOne = new SPNEntitat(null, Globals.g_Native.getString(R.string.llista_Select), true);
                            l_Entitats.add(l_SelectOne);
                            // Llegim les entitats
                            JSONArray l_ArrayEntitats = null;
                            l_ArrayEntitats = p_entitats.getJSONArray(Globals.g_Native.getString(R.string.TEntitats));
                            for (int i = 0; i < l_ArrayEntitats.length(); i++) {
                                JSONObject l_entitatServidor = l_ArrayEntitats.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Entitat
                                Entitat l_entitat = JSONToEntitat(l_entitatServidor);
                                // Carreguem
                                SPNEntitat l_spinner = new SPNEntitat(l_entitat, l_entitat.Nom, true);
                                l_Entitats.add(l_spinner);
                            }
                            // Associem
                            ArrayAdapter<SPNEntitat> dataAdapter = new ArrayAdapter<SPNEntitat>(Globals.g_Native, R.layout.linia_spn_defecte, l_Entitats);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            p_SPN_EntitatsClient.setAdapter(dataAdapter);
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
    // Pasa les dades del cursor al client
    private static Entitat cursorToEntitat(Cursor p_cursor){
        Entitat l_entitat = new Entitat();

        l_entitat.Codi = p_cursor.getString(p_cursor.getColumnIndex(TAG_Codi));
        l_entitat.eMail = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMail));
        l_entitat.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_entitat.Pais = p_cursor.getString(p_cursor.getColumnIndex(TAG_Pais));
        l_entitat.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_entitat.Adresa = p_cursor.getString(p_cursor.getColumnIndex(TAG_Adresa));
        l_entitat.Telefon = p_cursor.getString(p_cursor.getColumnIndex(TAG_Telefon));

        return l_entitat;
    }
    // Pasa les dades del objecte JSON a la Entitat
    private static Entitat JSONToEntitat(JSONObject p_EntitatServidor){
        Entitat l_entitat = new Entitat();

        try {
            l_entitat.Codi = p_EntitatServidor.getString(TAG_Codi);
            l_entitat.Nom = p_EntitatServidor.getString(TAG_Nom);
            l_entitat.Adresa = p_EntitatServidor.getString(TAG_Adresa);
            l_entitat.Telefon = p_EntitatServidor.getString(TAG_Telefon);
            l_entitat.Contacte = p_EntitatServidor.getString(TAG_Contacte);
            l_entitat.eMail = p_EntitatServidor.getString(TAG_eMail);
            l_entitat.Pais = p_EntitatServidor.getString(TAG_Pais);
            l_entitat.Estat = p_EntitatServidor.getInt(TAG_Estat);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_entitat;
    }
}
