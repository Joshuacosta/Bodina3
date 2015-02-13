package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public static SQLClientsDAO g_DB_DAO;
    public static SQLiteDatabase g_DB;
    public static SQLDB g_BBDD;
    // Constants
    // Operatives
    public static final int k_OPE_Alta = 0;
    public static final int k_OPE_Update = 1;
    public static final int k_OPE_Select = 2;
    public static final int k_OPE_SelectCodiClientIntern = 21;
    // - Treball
    public static final String k_ClientNOU = "NOU";

    public static void CreateBBDD(){
        g_BBDD = new SQLDB(g_Native);
        g_DB = g_BBDD.getWritableDatabase();
    }

    public static void TancarBBDD(){
        g_DB.close();
    }

}