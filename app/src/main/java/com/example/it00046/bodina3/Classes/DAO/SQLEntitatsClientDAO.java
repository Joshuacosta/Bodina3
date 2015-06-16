package com.example.it00046.bodina3.Classes.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Layout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.CustomList.LV_LlistaEntitatsClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SpnEntitat;
import com.example.it00046.bodina3.Classes.Tipus.Client;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.Classes.Tipus.EntitatClient;
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
 * Created by it00046 on 06/05/2015.
 */
public final class SQLEntitatsClientDAO {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_Entitat = "entitat";
    private static final String TAG_Entitats = "entitats";
    private static final String TAG_CodiClient = Globals.g_Native.getString(R.string.TClient_CodiClient);
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
    private static final String TAG_ActualitzatServidor = Globals.g_Native.getString(R.string.TEntitatsClient_ActualitzatServidor);
    private static final String TAG_DataGrabacioLocal = Globals.g_Native.getString(R.string.TEntitatsClient_DataGrabacioLocal);
    private static final String TAG_DataGrabacioServidor = Globals.g_Native.getString(R.string.TEntitatsClient_DataGrabacioServidor);
    //
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A    S E R V I D O R    ( P R I V A D A )
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //
    // Llegim les entitats del client del servidor, si existeix en el servidor el recuperem localment (el grabem
    // a la BBDD local)
    /*
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
    */
    //
    // Funcio privada per inserir les dades localment
    private static Boolean F_LOCAL_Inserir(Client p_client){
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
    private static void f_ModificarGlobal(final EntitatClient p_EntitatCient){
        // Montem el php
        /*
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
        */
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcio per actualitzar les dades entre servidor i client
    public static void F_SERVIDOR_ACTUALITZAR(){

    }
    // Funcio per llegir les entitats del client en una listview
    public static void F_LOCAL_Llegir(final ListView P_LlistaEntitats, int P_Layout){
        //final ArrayAdapter<EntitatClient> l_Llista = new LV_LlistaEntitatsClient(Globals.g_Native.getApplicationContext(), R.layout.ly_entitat_listview_linia);
        final ArrayAdapter<EntitatClient> l_Llista = new LV_LlistaEntitatsClient(Globals.g_Native.getApplicationContext(), P_Layout);
        EntitatClient l_EntitatClient;
        //
        EntitatClient l_EntitatClientX = new EntitatClient();
        //
        int l_NumEntitats;
        // Recerquem localment
        try {
            Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TEntitatsClient),
                    Globals.g_Native.getResources().getStringArray(R.array.TEntitatsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            l_NumEntitats = cursor.getCount();
            if (l_NumEntitats > 0) {
                // Llegim les entitats
                for (int i =0; i < l_NumEntitats; i++){
                    cursor.moveToPosition(i);
                    l_EntitatClient = f_cursorToEntitatClient(cursor);
                    // Si hi ha xarxa validem la integritat de les dades si així està parametritzat.
                    //
                    // Manca la sincronitzacio!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //if (Globals.isNetworkAvailable() && (Globals.g_Client.Sincronitzacio)){
                    if (Globals.isNetworkAvailable()){
                        if (l_EntitatClient.Actualitzat == false) {
                            f_ModificarGlobal(l_EntitatClient);
                        }
                    }
                    // Apuntem l'entitat del client a la llista
                    l_Llista.add(l_EntitatClient);

                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                    l_Llista.add(l_EntitatClient);
                }
                //
                // Adpatem
                P_LlistaEntitats.setAdapter(l_Llista);
            }
            else {
                // El client no te cap entitat
            }
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
    }
    //
    // Funció per llegir LOCALMENT les entitats del client
    //
    /*
    public static void F_LOCAL_EntitatsClient(final Spinner P_SPN_EntitatsClient){
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
    // Funció per llegir del SERVIDOR les entitats de un client
    //
    public static void F_SERVIDOR_EntitatsClient(String p_CodiClient, final Spinner SPN_EntitatsClient){
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
                                // Pasa les dades del objecte JSON a la Entitat
                                Entitat l_entitat = f_JSONToEntitat(l_entitatServidor);;
                                // Carreguem
                                SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom, true);
                                l_Entitats.add(l_spinner);
                            }
                            // Associem
                            ArrayAdapter<SpnEntitat> dataAdapter = new ArrayAdapter<SpnEntitat>(Globals.g_Native.getApplicationContext(),android.R.layout.simple_spinner_item, l_Entitats);
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
    */
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
                PhpJson.post("Associacio.php", f_Solicitar_entitatClientToRequestParams(p_EntitatClient), new JsonHttpResponseHandler() {
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
                                    // Error a la BBDD local
                                    Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.errorlocal_BBDD),
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
    // Funcio per updatar el camp actualitzat i els relacionats
    private static Boolean f_ModificarActualitzat(String p_CodiEntitat) {
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(TAG_ActualitzatServidor, true);
        l_actualitzat.put(TAG_DataGrabacioServidor, Globals.F_Avui());
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TEntitatsClient),
                    l_actualitzat,
                    TAG_CodiEntitat + "='" + p_CodiEntitat + "'",
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
    private static EntitatClient f_cursorToEntitatClient(Cursor p_cursor){
        EntitatClient l_entitatClient = new EntitatClient();

        l_entitatClient.CodiEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_CodiEntitat));
        l_entitatClient.eMailEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMailEntitat));
        l_entitatClient.NomEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_NomEntitat));
        l_entitatClient.PaisEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_PaisEntitat));
        l_entitatClient.ContacteEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_ContacteEntitat));
        l_entitatClient.AdresaEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_AdresaEntitat));
        l_entitatClient.TelefonEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_TelefonEntitat));
        l_entitatClient.EstatEntitat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_EstatEntitat));
        l_entitatClient.DataDarrerCanviEntitat = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataDarrerCanviEntitat));
        l_entitatClient.DataPeticioAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataPeticioAssociacio));
        l_entitatClient.ContacteAssociacio= p_cursor.getString(p_cursor.getColumnIndex(TAG_ContacteAssociacio));
        l_entitatClient.DescripcioAssociacio= p_cursor.getString(p_cursor.getColumnIndex(TAG_DescripcioAssociacio));
        l_entitatClient.eMailAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMailAssociacio));
        l_entitatClient.DataAltaAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataAltaAssociacio));
        l_entitatClient.DataFiAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataFiAssociacio));
        l_entitatClient.DataDarrerCanviAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataDarrerCanviAssociacio));
        l_entitatClient.EstatAssociacio = p_cursor.getString(p_cursor.getColumnIndex(TAG_EstatAssociacio));
        l_entitatClient.Actualitzat = (p_cursor.getInt(p_cursor.getColumnIndex(TAG_ActualitzatServidor)) != 0);
        l_entitatClient.DataActualitzat = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataGrabacioLocal));
        l_entitatClient.DataGrabacioServidor = p_cursor.getString(p_cursor.getColumnIndex(TAG_DataGrabacioServidor));
        //
        l_entitatClient.HiHanCanvis = false;

        return l_entitatClient;
    }
    //
    // Pasa les dades del objecte JSON a la EntitatClient
    private static EntitatClient f_JSONToEntitatClient (JSONObject P_EntitatClientServidor){
        EntitatClient l_EntitatClient = new EntitatClient();

        try {
            l_EntitatClient.CodiEntitat = P_EntitatClientServidor.getString(TAG_CodiEntitat);
            l_EntitatClient.eMailEntitat = P_EntitatClientServidor.getString(TAG_eMailEntitat);
            l_EntitatClient.NomEntitat = P_EntitatClientServidor.getString(TAG_NomEntitat);
            l_EntitatClient.PaisEntitat = P_EntitatClientServidor.getString(TAG_PaisEntitat);
            l_EntitatClient.ContacteEntitat = P_EntitatClientServidor.getString(TAG_ContacteEntitat);
            l_EntitatClient.AdresaEntitat = P_EntitatClientServidor.getString(TAG_AdresaEntitat);
            l_EntitatClient.TelefonEntitat = P_EntitatClientServidor.getString(TAG_TelefonEntitat);
            l_EntitatClient.EstatEntitat = P_EntitatClientServidor.getInt(TAG_EstatEntitat);
            l_EntitatClient.DataDarrerCanviEntitat = P_EntitatClientServidor.getString(TAG_DataDarrerCanviEntitat);
            l_EntitatClient.DataPeticioAssociacio = P_EntitatClientServidor.getString(TAG_DataPeticioAssociacio);
            l_EntitatClient.ContacteAssociacio= P_EntitatClientServidor.getString(TAG_ContacteAssociacio);
            l_EntitatClient.DescripcioAssociacio= P_EntitatClientServidor.getString(TAG_DescripcioAssociacio);
            l_EntitatClient.eMailAssociacio = P_EntitatClientServidor.getString(TAG_eMailAssociacio);
            l_EntitatClient.DataAltaAssociacio = P_EntitatClientServidor.getString(TAG_DataAltaAssociacio);
            l_EntitatClient.DataFiAssociacio = P_EntitatClientServidor.getString(TAG_DataFiAssociacio);
            l_EntitatClient.DataDarrerCanviAssociacio = P_EntitatClientServidor.getString(TAG_DataDarrerCanviAssociacio);
            l_EntitatClient.EstatAssociacio = P_EntitatClientServidor.getString(TAG_EstatAssociacio);
            l_EntitatClient.Actualitzat = (P_EntitatClientServidor.getInt(TAG_ActualitzatServidor) != 0);
            l_EntitatClient.DataActualitzat = P_EntitatClientServidor.getString(TAG_DataGrabacioLocal);
            l_EntitatClient.DataGrabacioServidor = P_EntitatClientServidor.getString(TAG_DataGrabacioServidor);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_EntitatClient;
    }
    // Posa les dades de la entitat client a contentValue per inserir a la BBDD local
    private static ContentValues f_entitatClientToContentValues(EntitatClient p_entitatClient) {
        ContentValues l_values = new ContentValues();

        l_values.put(TAG_CodiEntitat, p_entitatClient.CodiEntitat);
        l_values.put(TAG_eMailEntitat, p_entitatClient.eMailEntitat);
        l_values.put(TAG_NomEntitat, p_entitatClient.NomEntitat);
        l_values.put(TAG_PaisEntitat, p_entitatClient.PaisEntitat);
        l_values.put(TAG_ContacteEntitat, p_entitatClient.ContacteEntitat);
        l_values.put(TAG_AdresaEntitat, p_entitatClient.AdresaEntitat);
        l_values.put(TAG_TelefonEntitat, p_entitatClient.TelefonEntitat);
        l_values.put(TAG_EstatEntitat, p_entitatClient.EstatEntitat);
        l_values.put(TAG_DataDarrerCanviEntitat, p_entitatClient.DataDarrerCanviEntitat);
        l_values.put(TAG_DataPeticioAssociacio, p_entitatClient.DataPeticioAssociacio);
        l_values.put(TAG_ContacteAssociacio, p_entitatClient.ContacteAssociacio);
        l_values.put(TAG_DescripcioAssociacio, p_entitatClient.DescripcioAssociacio);
        l_values.put(TAG_eMailAssociacio, p_entitatClient.eMailAssociacio);
        l_values.put(TAG_DataAltaAssociacio, p_entitatClient.DataAltaAssociacio);
        l_values.put(TAG_DataFiAssociacio, p_entitatClient.DataFiAssociacio);
        l_values.put(TAG_DataDarrerCanviAssociacio, p_entitatClient.DataDarrerCanviAssociacio);
        l_values.put(TAG_EstatAssociacio, p_entitatClient.EstatAssociacio);
        // Com gravem a la BD local posem a false per sapiguer que no es actualiztat al servidor
        // (ho posarem a cert si l'actualitzaci� al servidor va be, es la operacio que es fa
        // a continuaci� de gravar a la BBDD local).
        l_values.put(TAG_ActualitzatServidor, false);
        l_values.put(TAG_DataGrabacioLocal, Globals.F_Avui());
        return l_values;
    }

    // Posa les dades a contentValue per cridar al PHP per solicitar una associacio
    private static RequestParams f_Solicitar_entitatClientToRequestParams(EntitatClient p_entitatClient) {
        RequestParams l_values = new RequestParams();

        l_values.put(TAG_CodiClient, Globals.g_Client.CodiClient);
        l_values.put(TAG_CodiEntitat, p_entitatClient.CodiEntitat);
        l_values.put(TAG_ContacteAssociacio, p_entitatClient.ContacteAssociacio);
        l_values.put(TAG_DescripcioAssociacio, p_entitatClient.DescripcioAssociacio);
        l_values.put(TAG_eMailAssociacio, p_entitatClient.eMailAssociacio);
        l_values.put("Operativa", Globals.k_OPE_Alta);

        return l_values;
    }

    // Posa les dades a contentValue per cridar a un PHP
    private static RequestParams f_entitatClientToRequestParams(EntitatClient p_entitatClient) {
        RequestParams l_values = new RequestParams();

        l_values.put(TAG_CodiClient, Globals.g_Client.CodiClient);
        l_values.put(TAG_CodiEntitat, p_entitatClient.CodiEntitat);
        l_values.put(TAG_DataPeticioAssociacio, p_entitatClient.DataPeticioAssociacio);
        l_values.put(TAG_ContacteAssociacio, p_entitatClient.ContacteAssociacio);
        l_values.put(TAG_DescripcioAssociacio, p_entitatClient.DescripcioAssociacio);
        l_values.put(TAG_eMailAssociacio, p_entitatClient.eMailAssociacio);
        l_values.put(TAG_DataAltaAssociacio, p_entitatClient.DataAltaAssociacio);
        l_values.put(TAG_DataFiAssociacio, p_entitatClient.DataFiAssociacio);
        l_values.put(TAG_DataDarrerCanviAssociacio, p_entitatClient.DataDarrerCanviAssociacio);
        l_values.put(TAG_EstatAssociacio, p_entitatClient.EstatAssociacio);
        l_values.put("Operativa", Globals.k_OPE_Alta);

        return l_values;
    }
}
