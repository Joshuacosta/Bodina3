package com.example.it00046.bodina3.Classes.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaAssociacions;
import com.example.it00046.bodina3.Classes.Entitats.EntitatClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by it00046 on 06/05/2015.
 */
public final class DAOEntitatsClientOLD {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void Llegir(final ListView p_LlistaEntitats, int p_Layout) {
        final ArrayAdapter<EntitatClient> l_Llista = new LVWLlistaAssociacions(Globals.g_Native.getApplicationContext(), p_Layout);
        EntitatClient l_EntitatClient;
        int l_NumEntitats;

        try {
            Cursor l_cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TEntitatsClient),
                    Globals.g_Native.getResources().getStringArray(R.array.TEntitatsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            l_NumEntitats = l_cursor.getCount();
            if (l_NumEntitats > 0) {
                // Llegim les entitats
                for (int i =0; i < l_NumEntitats; i++){
                    l_cursor.moveToPosition(i);
                    l_EntitatClient = cursorToEntitatClient(l_cursor);
                    // Si hi ha xarxa validem la integritat de les dades si així està parametritzat.
                    if (Globals.isNetworkAvailable()){
                        if (l_EntitatClient.Actualitzat == false) {
                            SRV_Modificar(l_EntitatClient);
                        }
                    }
                    // Apuntem l'entitat del client a la llista
                    l_Llista.add(l_EntitatClient);
                }
                //
                // Adpatem
                p_LlistaEntitats.setAdapter(l_Llista);
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
    // Funcio per solicitar una associacio amb una entitat
    public static void Solicitar(final EntitatClient p_EntitatClient){
        // Primer definim localment i despres globalment.
        try {
            Globals.g_DB.insert(Globals.g_Native.getString(R.string.TEntitatsClient),
                    null,
                    entitatClientToContentValues(p_EntitatClient));
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        finally {
            // Actualitzem el servidor
            if (Globals.isNetworkAvailable()) {
                // Cridem al php
                PhpJson.post("Associacio.php", Solicitar_entitatClientToRequestParams(p_EntitatClient), new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode,
                                          Header[] headers,
                                          Throwable throwable,
                                          JSONObject errorResponse) {
                        Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                                Globals.g_Native.getString(R.string.error_greu));
                    }

                    @Override
                    public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_entitatClientServidor) {
                        try {
                            String l_Resposta = p_entitatClientServidor.getString(TAG_VALIDS);
                            if (l_Resposta.equals(Globals.k_PHPOK)) {
                                // Actualitzem el camp actualitzat a la BBDD local
                                if (ModificarActualitzat(p_EntitatClient.CodiEntitat)) {
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
    private static void SRV_Modificar(final EntitatClient p_EntitatClient){
        // Montem el php
        // Cridem al php
        PhpJson.post("Associacio.php", Modificar_entitatClientToRequestParams(p_EntitatClient), new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode,
                                  Header[] headers,
                                  Throwable throwable,
                                  JSONObject errorResponse) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                        Globals.g_Native.getString(R.string.error_greu));
            }

            @Override
            public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_entitatClientServidor) {
                try {
                    String l_Resposta = p_entitatClientServidor.getString(TAG_VALIDS);
                    if (l_Resposta.equals(Globals.k_PHPOK)) {
                        // Actualitzem el camp actualitzat a la BBDD local
                        if (ModificarActualitzat(p_EntitatClient.CodiEntitat)) {
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
    // Funcio per updatar el camp actualitzat i els relacionats
    private static Boolean ModificarActualitzat(String p_CodiEntitat) {
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
    private static EntitatClient cursorToEntitatClient(Cursor p_cursor){
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
    private static EntitatClient JSONToEntitatClient (JSONObject P_EntitatClientServidor){
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
    private static ContentValues entitatClientToContentValues(EntitatClient p_entitatClient) {
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
    private static RequestParams Solicitar_entitatClientToRequestParams(EntitatClient p_entitatClient) {
        RequestParams l_values = new RequestParams();

        l_values.put(TAG_CodiClient, Globals.g_Client.CodiClient);
        l_values.put(TAG_CodiEntitat, p_entitatClient.CodiEntitat);
        l_values.put(TAG_ContacteAssociacio, p_entitatClient.ContacteAssociacio);
        l_values.put(TAG_DescripcioAssociacio, p_entitatClient.DescripcioAssociacio);
        l_values.put(TAG_eMailAssociacio, p_entitatClient.eMailAssociacio);
        l_values.put("Operativa", Globals.k_OPE_AssociacioSolicitar);

        return l_values;
    }

    // Posa les dades a contentValue per cridar a un PHP
    private static RequestParams Modificar_entitatClientToRequestParams(EntitatClient p_entitatClient) {
        RequestParams l_values = new RequestParams();

        l_values.put(TAG_CodiClient, Globals.g_Client.CodiClient);
        l_values.put(TAG_CodiEntitat, p_entitatClient.CodiEntitat);
        l_values.put(TAG_ContacteAssociacio, p_entitatClient.ContacteAssociacio);
        l_values.put(TAG_DescripcioAssociacio, p_entitatClient.DescripcioAssociacio);
        l_values.put(TAG_eMailAssociacio, p_entitatClient.eMailAssociacio);
        l_values.put(TAG_DataFiAssociacio, p_entitatClient.DataFiAssociacio);
        l_values.put(TAG_EstatAssociacio, p_entitatClient.EstatAssociacio);
        l_values.put("Operativa", Globals.k_OPE_AssociacioModificar);

        return l_values;
    }
}
