package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaInvitacions;
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
    private static final String TAG_Telefon = "Telefon";
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_PaisClient = Globals.g_Native.getString(R.string.TClient_Pais);
    private static final String TAG_Entitat = Globals.g_Native.getString(R.string.TEntitats);
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TInvitacions_CodiEntitat);
    private static final String TAG_DataInvitacio = Globals.g_Native.getString(R.string.TInvitacions_DataInvitacio);
    private static final String TAG_DataResolucio = Globals.g_Native.getString(R.string.TInvitacions_DataResolucio);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TInvitacions_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim les invitacions del client
    public static void Llegir(final ListView p_LVW_Invitacions, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Invitacio> l_Llista = new LVWLlistaInvitacions(p_Context, p_Layout);
        Invitacio l_Invitacio;
        TelephonyManager l_NumTelefon;
        int l_NumInvitacions;

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            // Recupero el telefon i pais del client
            g_parametresPHP.put(TAG_PaisClient, Globals.g_Client.Pais);
            l_NumTelefon = (TelephonyManager)Globals.g_Native.getSystemService(Context.TELEPHONY_SERVICE);
            g_parametresPHP.put(TAG_Telefon, l_NumTelefon.getLine1Number());
            //
            g_parametresPHP.put(Globals.TAG_OPERATIVA, Globals.k_OPE_InvitacionsLlegir);
            PhpJson.post("Invitacions.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu), p_Context);
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_Invitacions) {
                    try {
                        String l_Resposta = p_Invitacions.getString(Globals.TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Llegim les invitacions del client
                            JSONArray l_ArrayInvitacions = null;
                            l_ArrayInvitacions = p_Invitacions.getJSONArray(Globals.g_Native.getString(R.string.TInvitacions));
                            for (int i = 0; i < l_ArrayInvitacions.length(); i++) {
                                JSONObject l_JSONInvitacio = l_ArrayInvitacions.getJSONObject(i);
                                // Pasa les dades del objecte JSON a la Entitat
                                Invitacio l_Invitacio = JSONToInvitacio(l_JSONInvitacio);
                                // Carreguem
                                l_Llista.add(l_Invitacio);
                            }
                            // Associem
                            p_LVW_Invitacions.setAdapter(l_Llista);
                        } else {
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
    // Acceptem una invitacio
    public static void Acceptar(String p_CodiEntitat, final Context p_Context){
        OperativaInvitacio(p_CodiEntitat, Globals.k_OPE_InvitacionsAcceptar, p_Context);
    }
    // Rebutgem una invitacio
    public static void Rebutjar(String p_CodiEntitat, final Context p_Context){
        OperativaInvitacio(p_CodiEntitat, Globals.k_OPE_InvitacionsRebutjar, p_Context);
    }
    // De Json a Associacio
    public static Invitacio JSon(JSONObject p_Invitacio) {
        return JSONToInvitacio(p_Invitacio);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Operativa interna de acceptacio o denegacio
    private static void OperativaInvitacio(String p_CodiEntitat, final String p_Operativa, final Context p_Context){
        TelephonyManager l_NumTelefon;

        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new RequestParams();
            // Recupero el telefon i pais del client
            g_parametresPHP.put(TAG_PaisClient, Globals.g_Client.Pais);
            l_NumTelefon = (TelephonyManager)Globals.g_Native.getSystemService(Context.TELEPHONY_SERVICE);
            g_parametresPHP.put(TAG_Telefon, l_NumTelefon.getLine1Number());
            g_parametresPHP.put(TAG_CodiEntitat, p_CodiEntitat);
            //
            g_parametresPHP.put(Globals.TAG_OPERATIVA, p_Operativa);
            PhpJson.post("Invitacions.php", g_parametresPHP, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode,
                                      org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONObject errorResponse) {
                    Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.error_greu), p_Context);
                }

                @Override
                public void onSuccess(int p_statusCode, Header[] p_headers, JSONObject p_Resposta) {
                    try {
                        String l_Resposta = p_Resposta.getString(TAG_VALIDS);
                        if (l_Resposta.equals(Globals.k_PHPOK)) {
                            // Informem al usuari que hem modificat les dades
                            if (p_Operativa == Globals.k_OPE_InvitacionsAcceptar) {
                                Toast.makeText(Globals.g_Native,
                                        Globals.g_Native.getString(R.string.op_acceptar_invitacio),
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(Globals.g_Native,
                                        Globals.g_Native.getString(R.string.op_rebutjar_invitacio),
                                        Toast.LENGTH_LONG).show();
                            }
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
        else{
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
        }
    }

    // Pasa les dades del objecte JSON a la Invitacio
    private static Invitacio JSONToInvitacio(JSONObject p_Invitacio){
        Invitacio l_Invitacio = new Invitacio();

        try {
            // Dades invitacio
            l_Invitacio.DataInvitacio = p_Invitacio.getString(TAG_DataInvitacio);
            l_Invitacio.DataResolucio = p_Invitacio.getString(TAG_DataResolucio);
            l_Invitacio.Estat = p_Invitacio.getInt(TAG_Estat);
            // Dades entitat
            JSONObject l_Entitat = p_Invitacio.getJSONObject(TAG_Entitat);
            l_Invitacio.entitat = DAOEntitats.JSon(l_Entitat);
        }
        catch (JSONException e) {
            ;
        }
        return l_Invitacio;
    }
}
