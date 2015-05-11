package com.example.it00046.bodina3.Classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

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
public final class SQLEntitatsClientDAO {
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A    S E R V I D O R    ( P R I V A D A )
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_entitats = "entitats";
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_CodiEntitat);
    private static final String TAG_eMailEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_eMailEntitat);
    private static final String TAG_NomEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_NomEntitat);
    private static final String TAG_PaisEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_PaisEntitat);
    private static final String TAG_ContacteEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_ContacteEntitat);
    private static final String TAG_AdresaEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_AdresaEntitat);
    private static final String TAG_TelefonEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_TelefonEntitat);
    private static final String TAG_EstatEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_EstatEntitat);
    private static final String TAG_DataDarrerCanviEntitat = Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviEntitat);
    private static final String TAG_DataPeticioAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_DataPeticioAssociacio);
    private static final String TAG_ContacteAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_ContacteAssociacio);
    private static final String TAG_DescripcioAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_DescripcioAssociacio);
    private static final String TAG_eMailAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_eMailAssociacio);
    private static final String TAG_DataAltaAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_DataAltaAssociacio);
    private static final String TAG_DataFiAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_DataFiAssociacio);
    private static final String TAG_DataDarrerCanviAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviAssociacio);
    private static final String TAG_EstatAssociacio = Globals.g_Native.getString(R.string.TEntitatsClient_EstatAssociacio);

    //
    // Llegim les entitats del client del servidor, si existeix en el servidor el recuperem localment (el grabem
    // a la BBDD local)
    static private void f_LlegirServidorClauInterna(final String p_CodiClientIntern){
        // Validem que la xarxa estigui activa
        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_CodiClientIntern), p_CodiClientIntern);
            g_parametresPHP.put("Operativa", Globals.k_OPE_SelectEntitatsClient);
            PhpJson.post("EntitatsClient.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse){
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject p_entitatsClientServidor){
                    try{
                        String l_Resposta = p_entitatsClientServidor.getString(TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Apuntem codi intern
                            Globals.g_Client.CodiClientIntern = p_CodiClientIntern;
                            // Llegim el client (nomes tornem un)
                            JSONArray l_ArrayClient = null;
                            l_ArrayClient = p_entitatsClientServidor.getJSONArray(TAG_client);
                            JSONObject l_client = l_ArrayClient.getJSONObject(0);
                            if (l_client.getString(TAG_CodiClient).equals(Globals.k_ClientNOU)) {
                                // Client nou, no hem de fer res
                            } else {
                                Globals.g_Client.CodiClient = l_client.getString(TAG_CodiClient);
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
                                    f_InserirLocal(Globals.g_Client);
                                    // Hi han dades
                                    Globals.g_NoHiHanDades = false;
                                }
                            }
                        }
                        else{
                            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                    Globals.g_Native.getString(R.string.error_greu));
                        }
                    }
                    catch (JSONException e) {
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
    // Funcio privada per inserir les dades localment
    private static Boolean f_InserirLocal(Client p_client){
        ContentValues l_values = new ContentValues();
        long l_resultat;

        p_client.Actualitzat = true;

        l_values.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_client.CodiClient);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Contacte), p_client.Contacte);
        l_values.put(Globals.g_Native.getString(R.string.TClient_DataAlta), p_client.DataAlta);
        l_values.put(Globals.g_Native.getString(R.string.TClient_eMail), p_client.eMail);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Idioma), p_client.Idioma);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Nom), p_client.Nom);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Pais), p_client.Pais);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Actualitzat), p_client.Actualitzat);
        l_resultat = Globals.g_DB.insert(Globals.g_Native.getString(R.string.TClient), null, l_values);
        // Informem si la operativa ha anat correctament
        return (l_resultat != -1);
    }
    //
    //
    private static void f_ModificarGlobal(final Client p_client){
        // Montem el php
        g_parametresPHP = new RequestParams();
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_client.CodiClient);
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_eMail), p_client.eMail);
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Nom), p_client.Nom);
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Pais), p_client.Pais);
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Contacte), p_client.Contacte);
        g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Idioma), p_client.Idioma);
        g_parametresPHP.put("Operativa", Globals.k_OPE_Update);
        PhpJson.post("Clients.php", g_parametresPHP, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode,
                                  org.apache.http.Header[] headers,
                                  java.lang.Throwable throwable,
                                  org.json.JSONObject errorResponse) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                        Globals.g_Native.getString(R.string.error_greu));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject p_clientServidor) {
                try {
                    String l_Resposta = p_clientServidor.getString(TAG_VALIDS);
                    if (l_Resposta.equals(Globals.k_PHPOK)) {
                        // Actualitzem el camp actualitzat a la BBDD local
                        if (f_ModificarActualitzat(p_client.CodiClient)) {
                            // Informem al usuari que hem modificat les dades
                            Toast.makeText(Globals.g_Native,
                                    Globals.g_Native.getString(R.string.op_modificacio_ok),
                                    Toast.LENGTH_LONG).show();
                            // Actualitzem client global
                            Globals.g_Client = p_client;
                        }
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
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcio per llegir les dades del client
    public static void Llegir(){
        // Recerquem localment
        try {
            // Aquest valor l'informem ja (CodiInternClient es la MAC)
            Globals.g_Client.CodiClientIntern = Globals.F_RecuperaID();
            //
            Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TClient),
                    Globals.g_Native.getResources().getStringArray(R.array.TClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                Globals.g_Client = f_cursorToClient(cursor);
                // Si hi ha xarxa validem la integritat de les dades, o sigui, si les nostres dades
                // no hi son actualitzades o fem.
                if (Globals.isNetworkAvailable()){
                    if (Globals.g_Client.Actualitzat == false){
                        f_ModificarGlobal(Globals.g_Client);
                    }
                }
                // Si que hi han dades
                Globals.g_NoHiHanDades = false;
            }
            else {
                // Recerquem al servidor per si lo que ha passat es que l'usuari ha esborrat
                // les dades locals (en aquest cas les tornarem a grabar).
                Globals.g_Clients_DAO.f_LlegirServidorClauInterna(Globals.g_Client.CodiClientIntern);
                // ...
            }
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
    }
    //
    // Funcio per modificar les dades del client
    public static void Modificar(Client p_client){
        // Primer modifiquem localment i despres globalment
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TClient),
                    f_clientToContentValues(p_client),
                    Globals.g_Native.getString(R.string.TClient_CodiClient) + "= '" + p_client.CodiClient + "'",
                    null);
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        finally{
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Montem el php
                f_ModificarGlobal(p_client);
            }
        }
    }
    //
    // Funcio per solicitar una associacio amb una entitat
    public static void Solicitar(final EntitatClient p_EntitatClient){
        // Primer definim localment i despres globalment.
        try {
            Globals.g_DB.insert(Globals.g_Native.getString(R.string.TEntitatsClient),
                    null,
                    f_entitatClientToContentValues(p_EntitatClient));
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        finally {
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Cridem al php
                PhpJson.post("EntitatsClient.php", f_entitatClientToRequestParams(p_EntitatClient), new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode,
                                          org.apache.http.Header[] headers,
                                          java.lang.Throwable throwable,
                                          org.json.JSONObject errorResponse) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                                Globals.g_Native.getString(R.string.error_greu));
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject p_entitatClientServidor) {
                        try {
                            String l_Resposta = p_entitatClientServidor.getString(TAG_VALIDS);
                            if (l_Resposta.equals(Globals.k_PHPOK)) {
                                // Actualitzem el camp actualitzat a la BBDD local
                                if (f_ModificarActualitzat(p_EntitatClient.CodiEntitat)) {
                                    // Informem al usuari que hem modificat les dades
                                    Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.op_afegir_ok),
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    // Error en el servidor
                                    Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.errorservidor_BBDD),
                                            Toast.LENGTH_LONG).show();
                                }
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
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcio per updatar el camp actualitzat
    private static Boolean f_ModificarActualitzat(String p_CodiEntitat) {
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(Globals.g_Native.getString(R.string.TEntitatsClient_Actualitzat), true);
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TEntitatsClient),
                    l_actualitzat,
                    Globals.g_Native.getString(R.string.TEntitatsClient_CodiEntitat) + "='" + p_CodiEntitat + "'",
                    null);
        }
        catch (Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
            l_resposta = false;
        }

        return l_resposta;
    }
    //
    // Pasa les dades del cursor al client
    private static Client f_cursorToClient(Cursor p_cursor){
        Client l_client = new Client();

        l_client.CodiClient = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_CodiClient)));
        l_client.eMail = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_eMail)));
        l_client.Nom = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_Nom)));
        l_client.Pais = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_Pais)));
        l_client.Contacte = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_Contacte)));
        l_client.DataAlta = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_DataAlta)));
        l_client.Idioma = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_Idioma)));
        l_client.DataGrabacioServidor = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_DataGrabacioServidor)));
        l_client.Actualitzat = (p_cursor.getInt(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_Actualitzat))) != 0);
        l_client.DataActualitzat = p_cursor.getString(p_cursor.getColumnIndex(Globals.g_Native.getString(R.string.TClient_DataActualitzat)));

        return l_client;
    }
    //
    // Posa les dades de la entitat client a contentValue
    private static ContentValues f_entitatClientToContentValues(EntitatClient p_entitatClient) {
        ContentValues l_values = new ContentValues();

        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_CodiEntitat), p_entitatClient.CodiEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_eMailEntitat), p_entitatClient.eMailEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_NomEntitat), p_entitatClient.NomEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_PaisEntitat), p_entitatClient.PaisEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_ContacteEntitat), p_entitatClient.ContacteEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_AdresaEntitat), p_entitatClient.AdresaEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_TelefonEntitat), p_entitatClient.TelefonEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_EstatEntitat), p_entitatClient.EstatEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviEntitat), p_entitatClient.DataDarrerCanviEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataPeticioAssociacio), p_entitatClient.DataPeticioAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_ContacteAssociacio), p_entitatClient.ContacteAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DescripcioAssociacio), p_entitatClient.DescripcioAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_eMailAssociacio), p_entitatClient.eMailAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataAltaAssociacio), p_entitatClient.DataAltaAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataFiAssociacio), p_entitatClient.DataFiAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviAssociacio), p_entitatClient.DataDarrerCanviAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_EstatAssociacio), p_entitatClient.EstatAssociacio);
        // Com gravem a la BD local posem a false per sapiguer que no es actualiztat al servidor
        // (ho posarem a cert si l'actualització al servidor va be)
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_Actualitzat), false);

        return l_values;
    }

    // Posa les dades de la entitat client a contentValue
    private static RequestParams f_entitatClientToRequestParams(EntitatClient p_entitatClient) {
        RequestParams l_values = new RequestParams();

        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_CodiEntitat), p_entitatClient.CodiEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_eMailEntitat), p_entitatClient.eMailEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_NomEntitat), p_entitatClient.NomEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_PaisEntitat), p_entitatClient.PaisEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_ContacteEntitat), p_entitatClient.ContacteEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_AdresaEntitat), p_entitatClient.AdresaEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_TelefonEntitat), p_entitatClient.TelefonEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_EstatEntitat), p_entitatClient.EstatEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviEntitat), p_entitatClient.DataDarrerCanviEntitat);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataPeticioAssociacio), p_entitatClient.DataPeticioAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_ContacteAssociacio), p_entitatClient.ContacteAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DescripcioAssociacio), p_entitatClient.DescripcioAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_eMailAssociacio), p_entitatClient.eMailAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataAltaAssociacio), p_entitatClient.DataAltaAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataFiAssociacio), p_entitatClient.DataFiAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_DataDarrerCanviAssociacio), p_entitatClient.DataDarrerCanviAssociacio);
        l_values.put(Globals.g_Native.getString(R.string.TEntitatsClient_EstatAssociacio), p_entitatClient.EstatAssociacio);
        l_values.put("Operativa", Globals.k_OPE_Alta);

        return l_values;
    }
}
