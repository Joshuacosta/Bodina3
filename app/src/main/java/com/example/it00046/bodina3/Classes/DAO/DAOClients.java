package com.example.it00046.bodina3.Classes.DAO;

/**
 * Created by it00046 on 13/02/2015.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.Classes.Entitats.Client;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class DAOClients {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_client = "client";
    private static final String TAG_Client = Globals.g_Native.getString(R.string.TClient);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TClient_Codi);
    private static final String TAG_CodiIntern = Globals.g_Native.getString(R.string.TClient_CodiIntern);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TClient_eMail);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TClient_Nom);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TClient_Contacte);
    private static final String TAG_DataAlta = Globals.g_Native.getString(R.string.TClient_DataAlta);
    private static final String TAG_Pais = Globals.g_Native.getString(R.string.TClient_Pais);
    private static final String TAG_Idioma = Globals.g_Native.getString(R.string.TClient_Idioma);
    private static final String TAG_Actualitzat = Globals.g_Native.getString(R.string.TClient_Actualitzat);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcio per llegir les dades del client
    public static void Llegir(final Context p_Context){
        Globals.MostrarEspera(p_Context);
        // Recerquem localment
        try {
            // Aquest valor l'informem ja (CodiInternClient es la MAC)
            Globals.g_Client.CodiIntern = Globals.F_RecuperaID();
            //
            Cursor l_cursor = Globals.g_DB.query(TAG_Client,
                    Globals.g_Native.getResources().getStringArray(R.array.TClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() == 1) {
                l_cursor.moveToFirst();
                Globals.g_Client = CursorToClient(l_cursor);
                // Si hi ha xarxa validem la integritat de les dades, o sigui, si les nostres dades
                // no hi son actualitzades o fem.
                if (Globals.isNetworkAvailable()){
                    if (Globals.g_Client.Actualitzat == false){
                        SRV_Modificar(Globals.g_Client, p_Context);
                    }
                    else{
                        Globals.TancarEspera();
                    }
                }
                else{
                    Globals.TancarEspera();
                }
                // Si que hi han dades
                Globals.g_NoHiHanDades = false;
            }
            else {
                // Recerquem al servidor per si lo que ha passat es que l'usuari ha esborrat
                // les dades locals (en aquest cas les tornarem a grabar).
                SRV_LlegirClauInterna(Globals.g_Client.CodiIntern, p_Context);
                // ... hem de tractar tot lo que recuperem
            }
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }
    //
    // Funcio per modificar les dades del client
    public static void Modificar(Client p_client, final Context p_Context){
        Globals.MostrarEspera(p_Context);
        // Primer modifiquem localment i despres globalment
        try {
            Globals.g_DB.update(TAG_Client,
                    ClientToContentValues(p_client),
                    TAG_Codi + "= '" + p_client.Codi + "'",
                    null);
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
        finally{
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Montem el php
                SRV_Modificar(p_client, p_Context);
            }
            else{
                Globals.TancarEspera();
                // Informem de la operativa feta
                Toast.makeText(Globals.g_Native,
                        Globals.g_Native.getString(R.string.op_modificacio_ok),
                        Toast.LENGTH_LONG).show();
                // Gravem les dades del client
                Globals.g_Client = p_client;
                // Tanquem a qui ens ha cridat
                Activity l_activity = (Activity) p_Context;
                l_activity.finish();
            }
        }
    }
    //
    // Funcio per definir el client
    public static void Definir(final Client p_client, final Context p_Context){
        Globals.MostrarEspera(p_Context);
        // Primer definim localment i despres globalment. L'unic important del procès es que el codi
        // de client ho determinem quan donem d'alta en el servidor per lo que posteriorment hem de
        // actualitzat les dades locals que hem insertat previament.
        try {
            Globals.g_DB.insert(TAG_Client,
                    null,
                    ClientToContentValues(p_client));
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
        finally {
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Montem el php
                g_parametresPHP = new RequestParams();
                g_parametresPHP.put(TAG_CodiIntern, p_client.CodiIntern);
                g_parametresPHP.put(TAG_eMail, p_client.eMail);
                g_parametresPHP.put(TAG_Nom, p_client.Nom);
                g_parametresPHP.put(TAG_Pais, p_client.Pais);
                g_parametresPHP.put(TAG_Contacte, p_client.Contacte);
                g_parametresPHP.put(TAG_Idioma, p_client.Idioma);
                g_parametresPHP.put("Operativa", Globals.k_OPE_Alta);
                PhpJson.post("Clients.php", g_parametresPHP, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode,
                                          org.apache.http.Header[] headers,
                                          java.lang.Throwable throwable,
                                          org.json.JSONObject errorResponse) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                                Globals.g_Native.getString(R.string.error_greu), p_Context);
                        Globals.TancarEspera();
                    }

                    @Override
                    public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_clientServidor) {
                        Globals.TancarEspera();
                        try {
                            String l_Resposta = p_clientServidor.getString(TAG_VALIDS);
                            if (l_Resposta.equals(Globals.k_PHPErrorMail)){
                                Toast.makeText(Globals.g_Native,
                                        Globals.g_Native.getString(R.string.errorservidor_Mail),
                                        Toast.LENGTH_LONG).show();
                                // Gravem les dades del client
                                Globals.g_Client = p_client;
                            }
                            if (l_Resposta.equals(Globals.k_PHPOK) || l_Resposta.equals(Globals.k_PHPErrorMail)) {
                                // Recuperem el codi de client calcular al servidor
                                String l_Codi = p_clientServidor.getString(TAG_Codi);
                                // Actualitzem el camp actualitzat a la BBDD local
                                if (LOC_ModificaCodiClient(l_Codi, p_Context)) {
                                    // Informem al usuari que hem modificat les dades
                                    Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.op_afegir_ok),
                                            Toast.LENGTH_LONG).show();
                                    // Actualitzem les dades globals de client
                                    p_client.Codi = l_Codi;
                                    Globals.g_Client = p_client;
                                }
                                // Tanquem a qui ens ha cridat
                                Activity l_activity = (Activity) p_Context;
                                l_activity.finish();
                            }
                            else {
                                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                        Globals.g_Native.getString(R.string.error_greu), p_Context);
                            }
                        } catch (JSONException e) {
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                                    Globals.g_Native.getString(R.string.error_greu), p_Context);
                            Globals.TancarEspera();
                        }
                    }
                });
            }
            else{
                Globals.TancarEspera();
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim el client del servidor, si existeix en el servidor el recuprem localment (el grabem
    // a la BBDD local)
    private static void SRV_LlegirClauInterna(final String p_CodiIntern, final Context p_Context){
        // Validem que la xarxa estigui activa
        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(TAG_CodiIntern, p_CodiIntern);
            g_parametresPHP.put("Operativa", Globals.k_OPE_SelectCodiClientIntern);
            PhpJson.post("Clients.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu), p_Context);
                    Globals.TancarEspera();
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_clientServidor) {
                    Globals.TancarEspera();
                    try {
                        String l_Resposta = p_clientServidor.getString(TAG_VALIDS);

                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Apuntem codi intern
                            Globals.g_Client.CodiIntern = p_CodiIntern;
                            // Llegim el client (nomes tornem un)
                            JSONArray l_ArrayClient = null;
                            l_ArrayClient = p_clientServidor.getJSONArray(TAG_client);
                            JSONObject l_client = l_ArrayClient.getJSONObject(0);
                            if (l_client.getString(TAG_Codi).equals(Globals.k_ClientNOU)) {
                                // Client nou, no hem de fer res
                            } else {
                                Globals.g_Client.Codi = l_client.getString(TAG_Codi);
                                Globals.g_Client.eMail = l_client.getString(TAG_eMail);
                                Globals.g_Client.Nom = l_client.getString(TAG_Nom);
                                Globals.g_Client.Contacte = l_client.getString(TAG_Contacte);
                                Globals.g_Client.DataAlta = Globals.F_FormatDataServidorALocal(l_client.getString(TAG_DataAlta));
                                Globals.g_Client.Pais = l_client.getString(TAG_Pais);
                                Globals.g_Client.Idioma = l_client.getString(TAG_Idioma);
                                // Es actualitzat perque l'hem recuperat de la BBDD
                                Globals.g_Client.Actualitzat = true;
                                // Inserim les dades a la BBDD
                                if (Globals.g_NoHiHanDades) {
                                    LOC_Inserir(Globals.g_Client);
                                    // Hi han dades
                                    Globals.g_NoHiHanDades = false;
                                }
                            }
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
            Globals.TancarEspera();
        }
    }
    //
    // Funcio per inserir les dades localment
    private static Boolean LOC_Inserir(Client p_client){
        ContentValues l_values = new ContentValues();
        long l_resultat;

        p_client.Actualitzat = true;
        l_values.put(TAG_Codi, p_client.Codi);
        l_values.put(TAG_Contacte, p_client.Contacte);
        l_values.put(TAG_DataAlta, p_client.DataAlta);
        l_values.put(TAG_eMail, p_client.eMail);
        l_values.put(TAG_Idioma, p_client.Idioma);
        l_values.put(TAG_Nom, p_client.Nom);
        l_values.put(TAG_Pais, p_client.Pais);
        l_values.put(TAG_Actualitzat, p_client.Actualitzat);
        l_resultat = Globals.g_DB.insert(TAG_Client, null, l_values);
        // Informem si la operativa ha anat correctament
        return (l_resultat != -1);
    }
    //
    //
    private static void SRV_Modificar(final Client p_client, final Context p_Context){
        // Montem el php
        g_parametresPHP = new RequestParams();
        g_parametresPHP.put(TAG_Codi, p_client.Codi);
        g_parametresPHP.put(TAG_eMail, p_client.eMail);
        g_parametresPHP.put(TAG_Nom, p_client.Nom);
        g_parametresPHP.put(TAG_Pais, p_client.Pais);
        g_parametresPHP.put(TAG_Contacte, p_client.Contacte);
        g_parametresPHP.put(TAG_Idioma, p_client.Idioma);
        g_parametresPHP.put("Operativa", Globals.k_OPE_Update);
        PhpJson.post("Clients.php", g_parametresPHP, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode,
                                  org.apache.http.Header[] headers,
                                  java.lang.Throwable throwable,
                                  org.json.JSONObject errorResponse) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                        Globals.g_Native.getString(R.string.error_greu), p_Context);
                Globals.TancarEspera();
            }

            @Override
            public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_clientServidor) {
                Globals.TancarEspera();
                try {
                    String l_Resposta = p_clientServidor.getString(TAG_VALIDS);
                    if (l_Resposta.equals(Globals.k_PHPOK)) {
                        // Actualitzem el camp actualitzat a la BBDD local
                        if (LOC_ModificarActualitzat(p_client.Codi, p_Context)) {
                            // Informem al usuari que hem modificat les dades
                            Toast.makeText(Globals.g_Native,
                                    Globals.g_Native.getString(R.string.op_modificacio_ok),
                                    Toast.LENGTH_LONG).show();
                            // Actualitzem client global
                            Globals.g_Client = p_client;
                            // Tanquem a qui ens ha cridat
                            Activity l_activity = (Activity) p_Context;
                            l_activity.finish();
                        }
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
    // Funcio per updatar codi client (com nomes tenim un no posem where)
    private static Boolean LOC_ModificaCodiClient(String p_Codi, Context p_Context){
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(TAG_Codi, p_Codi);
        l_actualitzat.put(TAG_Actualitzat, true);
        try {
            Globals.g_DB.update(TAG_Client,
                    l_actualitzat,
                    null,
                    null);
        }
        catch (Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            l_resposta = false;
        }

        return l_resposta;
    }
    //
    // Funcio per updatar el camp actualitzat
    private static Boolean LOC_ModificarActualitzat(String p_Codi, Context p_Context) {
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(TAG_Actualitzat, true);
        try {
            Globals.g_DB.update(TAG_Client,
                    l_actualitzat,
                    TAG_Codi + "='" + p_Codi + "'",
                    null);
        }
        catch (Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            l_resposta = false;
        }

        return l_resposta;
    }
    //
    // Pasa les dades del cursor al client
    private static Client CursorToClient(Cursor p_cursor){
        Client l_client = new Client();

        l_client.Codi = p_cursor.getString(p_cursor.getColumnIndex(TAG_Codi));
        l_client.eMail = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMail));
        l_client.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_client.Pais = p_cursor.getString(p_cursor.getColumnIndex(TAG_Pais));
        l_client.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_client.DataAlta = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataAlta));
        l_client.Idioma = p_cursor.getString(p_cursor.getColumnIndex(TAG_Idioma));
        l_client.Actualitzat = (p_cursor.getInt(p_cursor.getColumnIndex(TAG_Actualitzat)) != 0);

        return l_client;
    }
    //
    // Posa les dades del client a contentValue
    private static ContentValues ClientToContentValues(Client p_client) {
        ContentValues l_values = new ContentValues();

        l_values.put(TAG_Codi, p_client.Codi);
        l_values.put(TAG_Contacte, p_client.Contacte);
        l_values.put(TAG_DataAlta, p_client.DataAlta);
        l_values.put(TAG_eMail, p_client.eMail);
        l_values.put(TAG_Idioma, p_client.Idioma);
        l_values.put(TAG_Nom, p_client.Nom);
        l_values.put(TAG_Pais, p_client.Pais);
        // Com gravem a la BD local posem a false per sapiguer que no es actualiztat al servidor
        // (ho posarem a cert si l'actualització al servidor va be)
        l_values.put(TAG_Actualitzat, false);

        return l_values;
    }
}
