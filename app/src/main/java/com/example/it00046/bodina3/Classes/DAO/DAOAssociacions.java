package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaAssociacions;
import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by it00046 on 06/05/2015.
 */
public final class DAOAssociacions {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_Entitat = "entitat";
    private static final String TAG_Entitats = "entitats";
    private static final String TAG_CodiClient = Globals.g_Native.getString(R.string.TClient_CodiClient);
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TAssociacions_CodiEntitat);
    private static final String TAG_NomEntitat = Globals.g_Native.getString(R.string.TAssociacions_NomEntitat);
    private static final String TAG_eMailEntitat = Globals.g_Native.getString(R.string.TAssociacions_eMailEntitat);
    private static final String TAG_PaisEntitat = Globals.g_Native.getString(R.string.TAssociacions_PaisEntitat);
    private static final String TAG_AdresaEntitat = Globals.g_Native.getString(R.string.TAssociacions_AdresaEntitat);
    private static final String TAG_ContacteEntitat = Globals.g_Native.getString(R.string.TAssociacions_ContacteEntitat);
    private static final String TAG_TelefonEntitat = Globals.g_Native.getString(R.string.TAssociacions_TelefonEntitat);
    private static final String TAG_EstatEntitat = Globals.g_Native.getString(R.string.TAssociacions_EstatEntitat);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TAssociacions_Contacte);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TAssociacions_Descripcio);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TAssociacions_eMail);
    private static final String TAG_DataAlta = Globals.g_Native.getString(R.string.TAssociacions_DataAlta);
    private static final String TAG_DataFi = Globals.g_Native.getString(R.string.TAssociacions_DataFi);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TAssociacions_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim de la BBDD del servidor les associacions del client
    public static void Llegir(final ListView p_LVW_Associacions, int p_Layout, Context p_Context) {
        final ArrayAdapter<Associacio> l_Llista = new LVWLlistaAssociacions(p_Context, p_Layout);

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(TAG_CodiClient, Globals.g_Client.Codi);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_AssociacionsLlegir);
            PhpJson.post("Associacions.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_Associacions) {
                    try {
                        String l_Resposta = p_Associacions.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Llegim les associacions del client
                            JSONArray l_ArrayAssociacions = null;
                            l_ArrayAssociacions = p_Associacions.getJSONArray(Globals.g_Native.getString(R.string.TAssociacions));
                            for (int i = 0; i < l_ArrayAssociacions.length(); i++) {
                                JSONObject l_JSONAssociacio = l_ArrayAssociacions.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Associacio
                                Associacio l_Associacio = JSONToAssociacio(l_JSONAssociacio);
                                // Carreguem
                                l_Llista.add(l_Associacio);
                            }
                            // Associem
                            p_LVW_Associacions.setAdapter(l_Llista);
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
    // Funcio per solicitar una associacio amb una entitat
    public static void Solicitar(final Associacio p_Associacio){
        // Actualitzem el servidor
        if (Globals.isNetworkAvailable()) {
            // Cridem al php
            PhpJson.post("Associacio.php", Solicitar_AssociacioToRequestParams(p_Associacio), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces), Globals.g_Native.getString(R.string.error_greu));
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_Resposta) {
                    try {
                        String l_Resposta = p_Resposta.getString(TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Informem al usuari que hem modificat les dades
                            Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.op_afegir_ok),
                                            Toast.LENGTH_LONG).show();
                        }else {
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
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Posa les dades a contentValue per cridar al PHP per solicitar una associacio
    private static RequestParams Solicitar_AssociacioToRequestParams(Associacio p_Associacio) {
        RequestParams l_values = new RequestParams();

        l_values.put(TAG_CodiClient, Globals.g_Client.Codi);
        l_values.put(TAG_CodiEntitat, p_Associacio.entitat.Codi);
        l_values.put(TAG_Contacte, p_Associacio.Contacte);
        l_values.put(TAG_Descripcio, p_Associacio.Descripcio);
        l_values.put(TAG_eMail, p_Associacio.eMail);
        l_values.put("Operativa", Globals.k_OPE_AssociacionsSolicitar);

        return l_values;
    }
    // Pasa les dades del objecte JSON a la Associacio
    private static Associacio JSONToAssociacio(JSONObject p_Associacio){
        Associacio l_Associacio = new Associacio();

        try {
            l_Associacio.entitat.Codi = p_Associacio.getString(TAG_CodiEntitat);
            l_Associacio.entitat.Nom = p_Associacio.getString(TAG_NomEntitat);
            l_Associacio.entitat.eMail = p_Associacio.getString(TAG_eMailEntitat);
            l_Associacio.entitat.Pais = p_Associacio.getString(TAG_PaisEntitat);
            l_Associacio.entitat.Adresa = p_Associacio.getString(TAG_AdresaEntitat);
            l_Associacio.entitat.Contacte = p_Associacio.getString(TAG_ContacteEntitat);
            l_Associacio.entitat.Telefon = p_Associacio.getString(TAG_TelefonEntitat);
            l_Associacio.entitat.Estat = p_Associacio.getInt(TAG_EstatEntitat);
            l_Associacio.Contacte = p_Associacio.getString(TAG_Contacte);
            l_Associacio.eMail = p_Associacio.getString(TAG_eMail);
            l_Associacio.DataAlta = p_Associacio.getString(TAG_DataAlta);
            l_Associacio.DataFi = p_Associacio.getString(TAG_DataFi);
            l_Associacio.Estat = p_Associacio.getInt(TAG_Estat);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_Associacio;
    }
}
