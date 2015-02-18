package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Date;

/**
 * Created by it00046 on 04/02/2015.
 */
public final class Globals
{
    // Variables
    public static Boolean g_NoHiHanDades = false;
    public static Client  g_Client = new Client();
    public static Context g_Native;
    public static Boolean g_HiHaXarxa = false;

    public static SQLClientsDAO g_Clients_DAO;
    public static SQLiteDatabase g_DB;
    public static SQLDB g_BBDD;
    // Constants
    // Operatives
    public static final String k_OPE_Alta = "0";
    public static final String k_OPE_Update = "1";
    public static final String k_OPE_Select = "2";
    public static final String k_OPE_SelectCodiClientIntern = "21";
    public static final String k_DirectoriPHP = "http://bodina.virtuol.com/php/";
    // - Treball
    public static final String k_ClientNOU = "NOU";

    public static void CreateBBDD(){
        g_BBDD = new SQLDB(g_Native);
        g_DB = g_BBDD.getWritableDatabase();
    }

    public static void TancarBBDD(){
        g_DB.close();
    }

    public static boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) g_Native.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String F_RecuperaMAC(){
        WifiManager wifiManager = (WifiManager) g_Native.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }

    public static void F_Alert(String p_capcalera, String p_texte){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(g_Native);
        builder.setMessage(p_texte).setTitle(p_capcalera);
        AlertDialog dialog = builder.create();
    }
}