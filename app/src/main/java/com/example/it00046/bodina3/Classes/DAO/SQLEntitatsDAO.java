package com.example.it00046.bodina3.Classes.DAO;

import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.it00046.bodina3.Classes.CustomList.RecercaEntitats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SpnEntitat;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.Classes.Tipus.EntitatClient;
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
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_CodiEntitat);
    private static final String TAG_eMailEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_eMailEntitat);
    private static final String TAG_NomEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_NomEntitat);
    private static final String TAG_PaisEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_PaisEntitat);
    private static final String TAG_ContacteEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_ContacteEntitat);
    private static final String TAG_AdresaEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_AdresaEntitat);
    private static final String TAG_TelefonEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_TelefonEntitat);
    //
    // Funció per llegir LOCALMENT les entitats del client
    //
    public static void F_LOCAL_LlegirEntitats(final Spinner P_SPN_EntitatsClient){
        final List <SpnEntitat> l_Entitats = new ArrayList<SpnEntitat>();

        try {
            Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TEntitatsClient),
                    Globals.g_Native.getResources().getStringArray(R.array.TEntitatsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    Entitat l_Entitat = f_cursorToEntitat(cursor);
                    // Carreguem
                    SpnEntitat l_spinner = new SpnEntitat(l_Entitat, l_Entitat.Nom, false);
                    l_Entitats.add(l_spinner);
                    //
                    cursor.moveToNext();
                }
            }
            // Informem el Spinner
            ArrayAdapter<SpnEntitat> dataAdapter = new ArrayAdapter<SpnEntitat>(Globals.g_Native.getApplicationContext(),android.R.layout.simple_spinner_item, l_Entitats);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            P_SPN_EntitatsClient.setAdapter(dataAdapter);
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
    }
    //
    // Funci� per llegir del SERVIDOR les entitats de un client, retornem la info per un Spinner
    // AIX� DEL SPINNER HO TINDRES QUE PARAMETRITZAR PER ALTRES SITUACIONS!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    public static void F_EntitatsClient (String p_CodiClient, final Spinner SPN_EntitatsClient){
        final List <SpnEntitat> l_Entitats = new ArrayList<SpnEntitat>();

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_CodiClient);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_LlegirEntitatsClient);
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
                                SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom, true);
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
    // Funció per llegir del SERVIDOR les entitats de un pais, retornem la info per un ListView
    //
    public static void F_LlistaEntitats (String p_Pais, final ListView LV_Entitats){
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
                            ArrayAdapter<Entitat> listAdapter = new RecercaEntitats(Globals.g_Native.getApplicationContext(), R.layout.lycustom_recerca_entitats);

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
                                listAdapter.add(l_entitat);
                            }
                            // Associem
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
    //
    // Funcions internes
    //
    // Pasa les dades del cursor al client
    private static Entitat f_cursorToEntitat(Cursor p_cursor){
        Entitat l_entitat = new Entitat();

        l_entitat.Codi = p_cursor.getString(p_cursor.getColumnIndex(TAG_CodiEntitat));
        l_entitat.eMail = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMailEntitat));
        l_entitat.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_NomEntitat));
        l_entitat.Pais = p_cursor.getString(p_cursor.getColumnIndex(TAG_PaisEntitat));
        l_entitat.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_ContacteEntitat));
        l_entitat.Adresa = p_cursor.getString(p_cursor.getColumnIndex(TAG_AdresaEntitat));
        l_entitat.Telefon = p_cursor.getString(p_cursor.getColumnIndex(TAG_TelefonEntitat));

        return l_entitat;
    }
}
