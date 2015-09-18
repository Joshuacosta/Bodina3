package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.Entitats.Client;
import com.example.it00046.bodina3.Classes.Entitats.Entitat;
import com.example.it00046.bodina3.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by it00046 on 04/02/2015.
 */
public final class Globals
{
    // Variables
    public static Boolean g_NoHiHanDades = true;
    public static Client g_Client = new Client();
    public static Context g_Native;
    public static Context g_Recerca;
    public static Boolean g_HiHaXarxa = false;
    public static ProgressDialog g_DialogEspera = null;
    public static String g_DataNula = "0000-00-00 00:00:00";
    //public static SQLClientsDAO g_Clients_DAO;
    public static SQLiteDatabase g_DB;
    public static SQLDB g_BBDD;
    // Constants
    // Operatives
    public static final String k_OPE_Alta = "0";
    public static final String k_OPE_AssociacionsSolicitar = "0";
    public static final String k_OPE_AssociacionsLlegir = "1";
    public static final String k_OPE_Llegir = "1"; // La generalitzo?
    public static final String k_OPE_AssociacionsModificar = "2";
    public static final String k_OPE_AssociacionsCancelarClient = "3";
    public static final String k_OPE_AssociacionsCancelarPeticioClient = "4";
    public static final String k_OPE_InvitacionsLlegir = "0";
    public static final String k_OPE_InvitacionsAcceptar = "1";
    public static final String k_OPE_InvitacionsRebutjar = "2";
    public static final String k_OPE_Update = "1";
    public static final String k_OPE_Select = "2";
    public static final String k_OPE_SelectCodiClientIntern = "21";
    public static final String k_OPE_LlegirEntitatsPais = "0";
    public static final String k_OPE_RecercaEntitatsPais = "1";
    public static final String k_OPE_LlegirEntitatsClient = "1";
    public static final String k_OPE_SelectEntitatsClient = "0";
    public static final String k_DirectoriPHP = "http://bodina.virtuol.com/php/";
    // Altres
    public static final int k_AssociacioBaixa = 0;
    public static final int k_AssociacioActiva = 1;
    public static final int k_AssociacioPendent = 2;
    public static final int k_AssociacioRebutjat = 3;
    public static final int k_AssociacioBaixaAbansConfirmar = 4;

    public static final int k_InvitacioBaixa = 0;
    public static final int k_InvitacioActiva = 1;
    public static final int k_InvitacioPendent = 2;
    public static final int k_InvitacioRebutjat = 3;


    public static final int k_EntitatBaixa = 0;
    // - Treball
    public static final String k_ClientNOU = "NOU";
    public static final String k_PHPOK = "1";
    public static final String k_PHPErrorBBDD = "2";
    public static final String k_PHPErrorMail = "3";
    public static final String TAG_OPERATIVA = "Operativa";
    public static final String TAG_VALIDS = "valids";
    public static final String TAG_RECERCA = "Recerca";
    public static final int k_Entitat_NomesInvitacio = 0;
    public static final int k_Entitat_PermetSolicitar = 1;

    public static void CreateBBDD(){
        g_BBDD = new SQLDB(g_Native);
        g_DB = g_BBDD.getWritableDatabase();
    }

    public static void TancarBBDD() {
        g_DB.close();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) g_Native.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String F_RecuperaID() {
        //
        // Amb aquest codi recuperariem la MAC (que no ens interesa)
        //WifiManager wifiManager = (WifiManager) g_Native.getSystemService(Context.WIFI_SERVICE);
        //WifiInfo wInfo = wifiManager.getConnectionInfo();
        //return wInfo.getMacAddress();
        //
        // I amb aquest recuperem el que ens interesa, el ANDROID_ID
        return Secure.getString(Globals.g_Native.getContentResolver(), Secure.ANDROID_ID);
    }

    public static void F_Alert(String p_capcalera, String p_texte, Context p_Context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(p_Context);
        builder.setMessage(p_capcalera).setTitle(p_texte);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String F_FormatDataServidorALocal(String P_Data) {
        SimpleDateFormat l_dfSistema = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date l_DataSistema = l_dfSistema.parse(P_Data);
            DateFormat l_df;
            l_df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            return l_df.format(l_DataSistema);
        }
        catch (ParseException e) {
            return "";
        }
    }

    public static String DateToString(Date p_Data){
        String l_Data = null;
        SimpleDateFormat l_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        l_Data = l_format.format(p_Data);
        return l_Data;
    }

    public static Date StringToDate(String p_Data){
        Date l_Data = null;
        DateFormat l_format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        try {
            l_Data = l_format.parse(p_Data);
        }
        catch (ParseException e) {
            ;
        }
        return l_Data;
    }

    public static String Avui(){
        Calendar c = Calendar.getInstance();
        DateFormat l_sdf;
        l_sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return l_sdf.format(c.getTime());
    }

    public static void MostrarEspera(Context p_Context) {
        if (g_DialogEspera == null) {
            g_DialogEspera = ProgressDialog.show(p_Context, "", Globals.g_Native.getString(R.string.Esperi), true);
        }
    }

    public static void MostrarEsperaTitol(Context p_Context, String p_Titol){
        String l_Titol = "";

        if (p_Titol == null){
            l_Titol = Globals.g_Native.getString(R.string.Carregant);
        }
        else{
            l_Titol = p_Titol;
        }
        g_DialogEspera = ProgressDialog.show(p_Context, l_Titol, Globals.g_Native.getString(R.string.Esperi), true);
    }

    public static void MostrarEsperaTitolMissatge(Context p_Context, String p_Titol, String p_Missatge){
        String l_Titol = "", l_Missatge = "";

        if (p_Titol == null){
            l_Titol = Globals.g_Native.getString(R.string.Carregant);
        }
        else{
            l_Titol = p_Titol;
        }
        if (p_Missatge == null){
            l_Missatge = Globals.g_Native.getString(R.string.Esperi);
        }
        else{
            l_Missatge = p_Missatge;
        }
        g_DialogEspera = ProgressDialog.show(p_Context, l_Titol, l_Missatge, true);
    }

    public static void TancarEspera(){
        if (g_DialogEspera != null){
            g_DialogEspera.dismiss();
            g_DialogEspera = null;
        }
    }

    public static View GetViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}