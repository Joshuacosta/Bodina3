package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOTaulesClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.Client;
import com.example.it00046.bodina3.Classes.Entitats.Taula;
import com.example.it00046.bodina3.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by it00046 on 04/02/2015.
 */
public final class Globals
{
    // Variables
    public static Boolean g_NoHiHanDades = true;
    public static Boolean g_BD_ValorsInicials = false;
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
    public static final int k_CelebracioClientBaixa = 0;
    public static final int k_CelebracioClientActiva = 1;
    public static final int k_SaloClientBaixa = 0;
    public static final int k_SaloClientActiu = 1;

    public static final int k_InvitacioBaixa = 0;
    public static final int k_InvitacioActiva = 1;
    public static final int k_InvitacioPendent = 2;
    public static final int k_InvitacioRebutjat = 3;

    public static final int k_TipusLinia = 0;
    public static final int k_TipusTexte = 1;

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
    public static final int k_CapacitatSenseDefinir = 0;
    //
    public static Taula g_TaulaSeleccio;

    public static void ExitAppDialog(final Activity activity)
    {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle("Warning");
        alertbox.setMessage("Exit Application?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.finish();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }

    public static void CreateBBDD(){
        g_BBDD = new SQLDB(g_Native);
        g_DB = g_BBDD.getWritableDatabase();
        if (Globals.g_BD_ValorsInicials) {
            // Inserim valors de defecte de les taules
            DAOTipusCelebracions.ValorsInicials();
            DAOTaulesClient.ValorsInicials();
        }
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
        SimpleDateFormat l_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return l_format.format(p_Data);
    }

    public static String HoraToString(Calendar p_Data){
        SimpleDateFormat l_format = new SimpleDateFormat("HH:mm");
        return l_format.format(p_Data.getTime());
    }

    public static String DataToString(Calendar p_Data){
        DateFormat l_format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return l_format.format(p_Data.getTime());
    }

    public static String MilliHoraToString(long p_Milli){
        Calendar l_Calendar = Calendar.getInstance();
        String l_Hora = "";

        if (p_Milli != 0) {
            l_Calendar.setTimeInMillis(p_Milli);
            SimpleDateFormat l_format = new SimpleDateFormat("HH:mm");
            l_Hora = l_format.format(l_Calendar.getTime());
        }
        return l_Hora;
    }

    public static String MilliDataToString(long p_Milli){
        Calendar l_Calendar = Calendar.getInstance();

        l_Calendar.setTimeInMillis(p_Milli);
        DateFormat l_format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return l_format.format(l_Calendar.getTime());
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

    public static Calendar StringToCalendar(String p_Data){
        Calendar l_Data = Calendar.getInstance();
        DateFormat l_format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String l_x;
        l_x = "dd/MM/yyyy HH:MM";
        SimpleDateFormat l_Simple = new SimpleDateFormat(l_x, Locale.getDefault());
        try {
            l_Data.setTime(l_Simple.parse(p_Data));
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

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
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

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /*
     * Generate a value suitable for use in {@link setid(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static double CalculaAngle(PointF p1, PointF p2) {
        // NOTE: Remember that most math has the Y axis as positive above the X.
        // However, for screens we have Y as positive below. For this reason,
        // the Y values are inverted to get the expected results.
        final double deltaY = (p1.y - p2.y);
        final double deltaX = (p2.x - p1.x);
        final double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return (result < 0) ? (360d + result) : result;
    }

    //
    public static Taula TaulaTreball(){
        return g_TaulaSeleccio;
    }

    public static void DefineixTaulaTreball(Taula p_Taula){
        g_TaulaSeleccio = p_Taula;
    }
}