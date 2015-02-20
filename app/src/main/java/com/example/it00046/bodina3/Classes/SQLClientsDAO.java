package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class SQLClientsDAO {
    //
    // O P E R A T I V A    S E R V I D O R
    //
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_CodiClient = Globals.g_Native.getString(R.string.TClient_CodiClient);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TClient_eMail);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TClient_Nom);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TClient_Contacte);
    private static final String TAG_DataAlta = Globals.g_Native.getString(R.string.TClient_DataAlta);
    private static final String TAG_Pais = Globals.g_Native.getString(R.string.TClient_Pais);
    private static final String TAG_Idioma = Globals.g_Native.getString(R.string.TClient_Idioma);
    //
    // Llegim el client del servidor, si existeix en el servidor el recuprem localment (el grabem
    // a la BBDD local)
    static private void f_LlegirServidorClauInterna(final String p_CodiClientIntern){
        // Validem que la xarxa estigui activa
        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_CodiClientIntern), p_CodiClientIntern);
            g_parametresPHP.put("Operativa", Globals.k_OPE_SelectCodiClientIntern);
            PhpJson.post("Clients.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse){
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject p_clientServidor){
                    try{
                        if (p_clientServidor.getString(TAG_VALIDS) == Globals.k_PHPOK) {
                            // Apuntem codi intern
                            Globals.g_Client.CodiClientIntern = p_CodiClientIntern;
                            if (p_clientServidor.getString(TAG_CodiClient) == Globals.k_ClientNOU) {
                                // Client nou, no hem de fer res
                            } else {
                                Globals.g_Client.CodiClient = p_clientServidor.getString(TAG_CodiClient);
                                Globals.g_Client.eMail = p_clientServidor.getString(TAG_eMail);
                                Globals.g_Client.Nom = p_clientServidor.getString(TAG_Nom);
                                Globals.g_Client.Contacte = p_clientServidor.getString(TAG_Contacte);
                                Globals.g_Client.DataAlta = p_clientServidor.getString(TAG_DataAlta);
                                Globals.g_Client.Pais = p_clientServidor.getString(TAG_Pais);
                                Globals.g_Client.Idioma = p_clientServidor.getString(TAG_Idioma);
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
    // Funcio per inserir les dades localment
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
                    if (p_clientServidor.getString(TAG_VALIDS) == Globals.k_PHPOK) {
                        // Actualitzem el camp actualitzat a la BBDD local
                        if (f_ModificarActualitzat(p_client.CodiClient)) {
                            // Informem al usuari que hem modificat les dades
                            Toast.makeText(Globals.g_Native,
                                    Globals.g_Native.getString(R.string.op_modificacio_ok),
                                    Toast.LENGTH_LONG).show();
                            // Actualitzem client global
                            Globals.g_Client = p_client;
                        }
                        else{
                            // Si ha anat malament l'actualització ja informarà la funció
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
    //
    // ---------------------------------
    // O P E R A T I V A   P U B L I C A
    // ---------------------------------
    //
    // Funcio per llegir les dades del client
    public static void Llegir(){
        // Recerquem localment
        try {
            // Aquest valor l'informem ja (CodiInternClient es la MAC)
            Globals.g_Client.CodiClientIntern = Globals.F_RecuperaMAC();
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
    public static void Modificar(final Client p_client){
        // Primer modifiquem localment i despres globalment
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TClient),
                    f_clientToContentValues(p_client),
                    Globals.g_Native.getString(R.string.TClient_CodiClient) + "=" + p_client.CodiClient,
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
    // Funcio per definir el client
    public static void Definir(final Client p_client){
        // Primer definim localment i despres globalment. L'unic important del procès es que el codi
        // de client ho determinem quan donem d'alta en el servidor per lo que posteriorment hem de
        // actualitzat les dades locals que hem insertat previament.
        try {
            Globals.g_DB.insert(Globals.g_Native.getString(R.string.TClient),
                                null,
                                f_clientToContentValues(p_client));
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                            Globals.g_Native.getString(R.string.error_greu));
        }
        finally {
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Montem el php
                g_parametresPHP = new RequestParams();
                g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_eMail), p_client.eMail);
                g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Nom), p_client.Nom);
                g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Pais), p_client.Pais);
                g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Contacte), p_client.Contacte);
                g_parametresPHP.put(Globals.g_Native.getString(R.string.TClient_Idioma), p_client.Idioma);
                g_parametresPHP.put("Operativa", Globals.k_OPE_Alta);
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
                            if (p_clientServidor.getString(TAG_VALIDS) == Globals.k_PHPOK) {
                                // Recuperem el codi de client calcular al servidor
                                String l_CodiClient = p_clientServidor.getString(TAG_CodiClient);
                                // Actualitzem el camp actualitzat a la BBDD local
                                if (f_ModificaCodiClient(l_CodiClient)) {
                                    // Informem al usuari que hem modificat les dades
                                    Toast.makeText(Globals.g_Native,
                                            Globals.g_Native.getString(R.string.op_afegir_ok),
                                            Toast.LENGTH_LONG).show();
                                    // Actualitzem les dades globals de client
                                    p_client.CodiClient = l_CodiClient;
                                    Globals.g_Client = p_client;
                                }
                                else{
                                    // Si ha anat malament l'inserció ja informarà la funció
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
    // Aquest codi el guardo per altres classes ja que amb clients no hi ha llista
    /*
    public static List<Client> getAllClients(){
        List<Client> clients = new ArrayList<Client>();

        Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TClient),
                Globals.g_Native.getResources().getStringArray(R.array.TClient_Camps)
                , null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Client client = cursorToClient(cursor);
            clients.add(client);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return clients;
    }
    */
    //
    /*
    // Guardo el codi per altres taules
    public static void Esborrar(Client client){
        String id = client.CodiClient;
        Globals.g_DB.delete(Globals.g_Native.getString(R.string.TClient), Globals.g_Native.getString(R.string.TClient_CodiClient) + " = " + id, null);
    }
    */
    //
    // Funcions privades
    //
    //
    // Funcio per updatar codi client
    public static Boolean f_ModificaCodiClient(String p_CodiClient){
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_CodiClient);
        l_actualitzat.put(Globals.g_Native.getString(R.string.TClient_Actualitzat), true);
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TClient),
                    l_actualitzat,
                    Globals.g_Native.getString(R.string.TClient_CodiClient) + "=" + p_CodiClient,
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
    // Funcio per updatar el camp actualitzat
    private static Boolean f_ModificarActualitzat(String p_CodiClient) {
        ContentValues l_actualitzat = new ContentValues();
        Boolean l_resposta = true;

        l_actualitzat.put(Globals.g_Native.getString(R.string.TClient_Actualitzat), true);
        try {
            Globals.g_DB.update(Globals.g_Native.getString(R.string.TClient),
                    l_actualitzat,
                    Globals.g_Native.getString(R.string.TClient_CodiClient) + "=" + p_CodiClient,
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
    // Fa un case
    private static Client f_cursorToClient(Cursor cursor){
        Client l_client = new Client();

        l_client.CodiClient = cursor.getString(0);
        l_client.eMail = cursor.getString(1);
        l_client.Nom = cursor.getString(2);
        l_client.Pais = cursor.getString(3);
        l_client.Contacte = cursor.getString(4);
        l_client.Idioma = cursor.getString(5);
        l_client.DataAlta = cursor.getString(6);
        l_client.DataGrabacioServidor = cursor.getString(7);
        l_client.Actualitzat = (cursor.getInt(8) != 0);
        l_client.DataActualitzat = cursor.getString(9);

        return l_client;
    }
    //
    // Fa un case
    private static ContentValues f_clientToContentValues(Client p_client){
        ContentValues l_values = new ContentValues();

        l_values.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_client.CodiClient);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Contacte), p_client.Contacte);
        l_values.put(Globals.g_Native.getString(R.string.TClient_DataAlta), p_client.DataAlta);
        l_values.put(Globals.g_Native.getString(R.string.TClient_eMail), p_client.eMail);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Idioma), p_client.Idioma);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Nom), p_client.Nom);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Pais), p_client.Pais);
        // Com gravem a la BD local posem a false per sapiguer que no es actualiztat al servidor
        // (ho posarem a cert si l'actualització al servidor va be)
        l_values.put(Globals.g_Native.getString(R.string.TClient_Actualitzat), false);

        return l_values;
    }
}

        /*
            Aquest codi serveix per donar un identificador a la insercio i
            despres com tornem lo inserit sabem quin valor s'ens ha donat

        Cursor cursor = database.query(SQLClients.TABLE_COMMENTS,
                allColumns, SQLClients.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Client newClient = cursorToComment(cursor);
        cursor.close();
        return newClient;
        */
