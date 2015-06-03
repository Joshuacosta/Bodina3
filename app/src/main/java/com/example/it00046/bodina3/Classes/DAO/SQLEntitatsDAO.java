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
    private static final String TAG_EstatEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_EstatEntitat);
    //
    // Funció per llegir del SERVIDOR les entitats de un pais, retornem la info per un ListView
    //
    public static void F_SERVIDOR_LlistaEntitats (String p_Pais, final ListView LV_Entitats){
        final ArrayAdapter<Entitat> listAdapter = new RecercaEntitats(Globals.g_Native.getApplicationContext(), R.layout.lycustom_recerca_entitats);

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
                                // Pasa les dades del objecte JSON a la Entitat
                                Entitat l_entitat = f_JSONToEntitat(l_entitatServidor);
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
    // Funció per llegir del SERVIDOR les entitats de un pais, retornem la info a un spinner
    //
    public static void F_SERVIDOR_LlistaEntitats (String p_Pais, final Spinner SPN_EntitatsClient){
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
                            // Posem a la llista la entrada de "Seleccioni..."
                            SpnEntitat l_SelectOne = new SpnEntitat(null, Globals.g_Native.getString(R.string.llista_Select), true);
                            l_Entitats.add(l_SelectOne);
                            // Llegim les entitats
                            JSONArray l_ArrayEntitats = null;
                            l_ArrayEntitats = p_entitats.getJSONArray(Globals.g_Native.getString(R.string.TEntitats));
                            for (int i = 0; i < l_ArrayEntitats.length(); i++) {
                                JSONObject l_entitatServidor = l_ArrayEntitats.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Entitat
                                Entitat l_entitat = f_JSONToEntitat(l_entitatServidor);
                                // Carreguem
                                SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom, true);
                                l_Entitats.add(l_spinner);
                            }
                            // Associem
                            ArrayAdapter<SpnEntitat> dataAdapter = new ArrayAdapter<SpnEntitat>(Globals.g_Native, R.layout.ly_spinnerdefecte, l_Entitats);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    //
    // Pasa les dades del objecte JSON a la Entitat
    private static Entitat f_JSONToEntitat(JSONObject P_EntitatServidor){
        Entitat l_entitat = new Entitat();

        try {
            l_entitat.Codi = P_EntitatServidor.getString(TAG_CodiEntitat);
            l_entitat.Nom = P_EntitatServidor.getString(TAG_NomEntitat);
            l_entitat.Adresa = P_EntitatServidor.getString(TAG_AdresaEntitat);
            l_entitat.Telefon = P_EntitatServidor.getString(TAG_TelefonEntitat);
            l_entitat.Contacte = P_EntitatServidor.getString(TAG_ContacteEntitat);
            l_entitat.eMail = P_EntitatServidor.getString(TAG_eMailEntitat);
            l_entitat.Pais = P_EntitatServidor.getString(TAG_PaisEntitat);
            l_entitat.Estat = P_EntitatServidor.getInt(TAG_EstatEntitat);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_entitat;
    }
}
