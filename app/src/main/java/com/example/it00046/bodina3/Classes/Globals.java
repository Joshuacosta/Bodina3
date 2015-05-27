package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;

import com.example.it00046.bodina3.Classes.DAO.SQLClientsDAO;
import com.example.it00046.bodina3.Classes.Tipus.Client;

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

    public static SQLClientsDAO g_Clients_DAO;
    public static SQLiteDatabase g_DB;
    public static SQLDB g_BBDD;
    // Constants
    // Operatives
    public static final String k_OPE_Alta = "0";
    public static final String k_OPE_Update = "1";
    public static final String k_OPE_Select = "2";
    public static final String k_OPE_LlegirEntitatsPais = "0";
    public static final String k_OPE_LlegirEntitatsClient = "1";
    public static final String k_OPE_SelectCodiClientIntern = "21";
    public static final String k_OPE_SelectEntitatsClient = "0";
    public static final String k_DirectoriPHP = "http://bodina.virtuol.com/php/";
    // - Treball
    public static final String k_ClientNOU = "NOU";
    public static final String k_PHPOK = "1";
    public static final String k_PHPErrorBBDD = "2";
    public static final String k_PHPErrorMail = "3";
    public static final String TAG_OPERATIVA = "Operativa";
    public static final String TAG_VALIDS = "valids";

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
        return Secure.ANDROID_ID.toString();
    }

    public static void F_Alert(String p_capcalera, String p_texte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(g_Native);
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
        catch  (ParseException e) {
            return "";
        }
    }

    public static String F_Avui(){
        Calendar c = Calendar.getInstance();
        DateFormat l_sdf;
        l_sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return l_sdf.format(c.getTime());
    }
}