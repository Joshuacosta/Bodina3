package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.it00046.bodina3.Classes.Custom.LVWLlistaInvitacionsClient;
import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Entitats.EntitatClient;
import com.example.it00046.bodina3.Classes.Entitats.Invitacio;
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
 * Created by it00046 on 02/07/2015.
 */
public class DAOInvitacions {
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_CodiClient = Globals.g_Native.getString(R.string.TClient_CodiClient);
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TInvitacions_CodiEntitat);
    private static final String TAG_NomEntitat = Globals.g_Native.getString(R.string.TInvitacions_NomEntitat);
    private static final String TAG_eMailEntitat = Globals.g_Native.getString(R.string.TInvitacions_eMailEntitat);
    private static final String TAG_PaisEntitat = Globals.g_Native.getString(R.string.TInvitacions_PaisEntitat);
    private static final String TAG_AdresaEntitat = Globals.g_Native.getString(R.string.TInvitacions_AdresaEntitat);
    private static final String TAG_ContacteEntitat = Globals.g_Native.getString(R.string.TInvitacions_ContacteEntitat);
    private static final String TAG_TelefonEntitat = Globals.g_Native.getString(R.string.TInvitacions_TelefonEntitat);
    private static final String TAG_EstatEntitat = Globals.g_Native.getString(R.string.TInvitacions_EstatEntitat);
    private static final String TAG_DataInvitacio = Globals.g_Native.getString(R.string.TInvitacions_DataInvitacio);
    private static final String TAG_DataConfirmacio = Globals.g_Native.getString(R.string.TInvitacions_DataConfirmacio);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TAssociacions_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim les invitacions del client
    public static void Llegir(final ListView p_LVW_Invitacions, int p_Layout, Context p_Context) {
        final ArrayAdapter<Invitacio> l_Llista = new LVWLlistaInvitacionsClient(p_Context, p_Layout);
        Invitacio l_Invitacio;
        int l_NumInvitacions;

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            g_parametresPHP.put(TAG_CodiClient, Globals.g_Client.Codi);
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_Llegir);
            PhpJson.post("Invitacions.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu));
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_Invitacions) {
                    try {
                        String l_Resposta = p_Invitacions.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Llegim les associacions del client
                            JSONArray l_ArrayInvitacions = null;
                            l_ArrayInvitacions = p_Invitacions.getJSONArray(Globals.g_Native.getString(R.string.TInvitacions));
                            for (int i = 0; i < l_ArrayInvitacions.length(); i++) {
                                JSONObject l_JSONAssociacio = l_ArrayInvitacions.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Entitat
                                Invitacio l_Invitacio = JSONToInvitacio(l_JSONAssociacio);
                                // Carreguem
                                l_Llista.add(l_Invitacio);
                            }
                            // Associem
                            p_LVW_Invitacions.setAdapter(l_Llista);
                        } else {
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
    // Acceptem una invitació
    public static void Acceptar(){

    }
    // Rebutgem una invitació
    public static void Rebutjar(){

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Pasa les dades del objecte JSON a la Associacio
    private static Invitacio JSONToInvitacio(JSONObject p_Invitacio){
        Invitacio l_Invitacio = new Invitacio();

        try {
            l_Invitacio.entitat.Codi = p_Invitacio.getString(TAG_CodiEntitat);
            l_Invitacio.entitat.Nom = p_Invitacio.getString(TAG_NomEntitat);
            l_Invitacio.entitat.eMail = p_Invitacio.getString(TAG_eMailEntitat);
            l_Invitacio.entitat.Pais = p_Invitacio.getString(TAG_PaisEntitat);
            l_Invitacio.entitat.Adresa = p_Invitacio.getString(TAG_AdresaEntitat);
            l_Invitacio.entitat.Contacte = p_Invitacio.getString(TAG_ContacteEntitat);
            l_Invitacio.entitat.Telefon = p_Invitacio.getString(TAG_TelefonEntitat);
            l_Invitacio.entitat.Estat = p_Invitacio.getInt(TAG_EstatEntitat);
            l_Invitacio.DataInvitacio = p_Invitacio.getString(TAG_DataInvitacio);
            l_Invitacio.DataConfirmacio = p_Invitacio.getString(TAG_DataConfirmacio);
            l_Invitacio.Estat = p_Invitacio.getInt(TAG_Estat);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_Invitacio;
    }
}
