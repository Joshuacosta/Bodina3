package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
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
    private static final String TAG_Entitat = "Entitat";
    private static final String TAG_CodiClient = Globals.g_Native.getString(R.string.TAssociacions_CodiClient);
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TAssociacions_CodiEntitat);
    // Camps de la Associacio
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TAssociacions_Contacte);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TAssociacions_Descripcio);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TAssociacions_eMail);
    private static final String TAG_DataAlta = Globals.g_Native.getString(R.string.TAssociacions_DataAlta);
    private static final String TAG_DataFi = Globals.g_Native.getString(R.string.TAssociacions_DataFi);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TAssociacions_Estat);
    private static final String TAG_DataPeticio = Globals.g_Native.getString(R.string.TAssociacions_DataPeticio);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim de la BBDD del servidor les associacions del client
    public static void Llegir(final ListView p_LVW_Associacions, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Associacio> l_Llista = new LVWLlistaAssociacions(p_Context, p_Layout);

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(TAG_CodiClient, Globals.g_Client.Codi);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_AssociacionsLlegir);
            PhpJson.post("Associacio.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu), p_Context);
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
                                if (l_Associacio == null){
                                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                            Globals.g_Native.getString(R.string.error_greu), p_Context);
                                }
                                // Carreguem
                                l_Llista.add(l_Associacio);
                            }
                            // Associem
                            p_LVW_Associacions.setAdapter(l_Llista);
                        }
                        else {
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                    Globals.g_Native.getString(R.string.error_greu), p_Context);
                        }
                    } catch (JSONException e) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                Globals.g_Native.getString(R.string.error_greu), p_Context);
                    }
                }
            });
        }
        else{
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
        }
    }
    // Funcio per solicitar una associacio amb una entitat
    public static void Solicitar(final Associacio p_Associacio, final Context p_Context){
        // Actualitzem el servidor
        if (Globals.isNetworkAvailable()) {
            // Cridem al php
            PhpJson.post("Associacio.php", Solicitar_AssociacioToRequestParams(p_Associacio), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces), Globals.g_Native.getString(R.string.error_greu), p_Context);
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
                            // Tanquem a qui ens ha cridat
                            Activity l_activity = (Activity) p_Context;
                            l_activity.setResult(Activity.RESULT_OK);
                            l_activity.finish();
                        }else {
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                    Globals.g_Native.getString(R.string.error_greu), p_Context);
                        }
                    } catch (JSONException e) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                Globals.g_Native.getString(R.string.error_greu), p_Context);
                    }
                }
            });
        }
    }
    // De Json a Associacio
    public static Associacio JSon(JSONObject p_Associacio) {
        return JSONToAssociacio(p_Associacio);
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
            // Dades associacio
            l_Associacio.Contacte = p_Associacio.getString(TAG_Contacte);
            l_Associacio.Descripcio = p_Associacio.getString(TAG_Descripcio);
            l_Associacio.eMail = p_Associacio.getString(TAG_eMail);
            l_Associacio.DataAlta = p_Associacio.getString(TAG_DataAlta);
            l_Associacio.DataFi = p_Associacio.getString(TAG_DataFi);
            l_Associacio.DataPeticio = p_Associacio.getString(TAG_DataPeticio);
            l_Associacio.Estat = p_Associacio.getInt(TAG_Estat);
            // Dades entitat
            JSONObject l_Entitat = p_Associacio.getJSONObject(TAG_Entitat);
            l_Associacio.entitat = DAOEntitats.JSon(l_Entitat);
        }
        catch (JSONException e) {
            ;
        }
        return l_Associacio;
    }
}
