package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaCelebracionsClient;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

/**
 * Created by it00046 on 02/06/2015.
 */
public final class DAOCelebracionsClient {
    private static final String TAG_CelebracionsClient = Globals.g_Native.getString(R.string.TCelebracionsClient);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TCelebracionsClient_Codi);
    private static final String TAG_CodiSalo = Globals.g_Native.getString(R.string.TCelebracionsClient_CodiSalo);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TCelebracionsClient_Tipus);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TCelebracionsClient_Descripcio);
    private static final String TAG_Convidats  = Globals.g_Native.getString(R.string.TCelebracionsClient_Convidats);
    private static final String TAG_Data = Globals.g_Native.getString(R.string.TCelebracionsClient_Data);
    private static final String TAG_Lloc = Globals.g_Native.getString(R.string.TCelebracionsClient_Lloc);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TCelebracionsClient_Contacte);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TCelebracionsClient_Estat);

    private static final String TAG_SalonsClient = Globals.g_Native.getString(R.string.TSalonsClient);
    private static final String TAG_SalonsClientCodi = Globals.g_Native.getString(R.string.TSalonsClient_Codi);
    private static final String TAG_SalonsClientNom = Globals.g_Native.getString(R.string.TSalonsClient_Nom);

    private static final String TAG_TipusCelebracio = Globals.g_Native.getString(R.string.TTipusCelebracio);
    private static final String TAG_TipusCelebracioCodi = Globals.g_Native.getString(R.string.TTipusCelebracio_Codi);
    private static final String TAG_TipusCelebracioDescripcio = Globals.g_Native.getString(R.string.TTipusCelebracio_Descripcio);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim les celebracions propies del client
    public static void Llegir(final ListView p_LVW_CelebracionsClient, int p_Layout, final Context p_Context) {
        final ArrayAdapter<CelebracioClient> l_Llista = new LVWLlistaCelebracionsClient(p_Context, p_Layout);
        Globals.MostrarEspera(p_Context);
        try {
            /*
            Cursor l_cursor = Globals.g_DB.query(TAG_CelebracionsClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TCelebracionsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            */
            /*
            Cursor l_cursor = Globals.g_DB.query(TAG_CelebracionsClient +
                                                 " LEFT OUTER JOIN " +
                                                 TAG_SalonsClient + " ON " + TAG_CelebracionsClient + "." + TAG_CodiSalo + "=" +
                                                 TAG_SalonsClient + "." + TAG_SalonsClientCodi +
                                                 " LEFT OUTER JOIN " +
                                                 TAG_TipusCelebracio + " ON " + TAG_CelebracionsClient + "." + TAG_Tipus + "=" +
                                                 TAG_TipusCelebracio + "." + TAG_TipusCelebracioCodi,
                    Globals.g_Native.getResources().getStringArray(R.array.JTCelebracionsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            */
            String l_Query = "SELECT CelebracionsClient.Codi, " +
                                    "CelebracionsClient.CodiSalo, " +
                                    "SalonsClient.Nom, " +
                                    "CelebracionsClient.Tipus, " +
                                    "TipusCelebracio.Descripcio, " +
                                    "CelebracionsClient.Descripcio, " +
                                    "CelebracionsClient.Convidats, " +
                                    "CelebracionsClient.Data, " +
                                    "CelebracionsClient.Lloc, " +
                                    "CelebracionsClient.Contacte, " +
                                    "CelebracionsClient.Estat " +
                                    "FROM CelebracionsClient " +
                            "LEFT OUTER JOIN SalonsClient " +
                            "ON CelebracionsClient.CodiSalo = SalonsClient.Codi " +
                            "LEFT OUTER JOIN TipusCelebracio " +
                            "ON CelebracionsClient.Tipus = TipusCelebracio.Codi";
            Cursor l_cursor = Globals.g_DB.rawQuery(l_Query, null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    CelebracioClient l_Tipus = CursorToCelebracioClient(l_cursor);
                    l_Llista.add(l_Tipus);
                    l_cursor.moveToNext();
                }
                p_LVW_CelebracionsClient.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }
    public static boolean Afegir(CelebracioClient p_CelebracioClient, final Context p_Context, boolean p_Asistit, boolean p_Tancam){
        boolean l_resultat = true;

        if (p_Asistit) {
            Globals.MostrarEspera(p_Context);
        }
        try {
            Globals.g_DB.insert(TAG_CelebracionsClient,
                                null,
                                CelebracioClientToContentValues(p_CelebracioClient, true));
        }
        catch(Exception e) {
            if (p_Asistit) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                        Globals.g_Native.getString(R.string.error_greu), p_Context);
                Globals.TancarEspera();
            }
            l_resultat = false;
        }
        finally{
            if (p_Asistit) {
                Globals.TancarEspera();
                // Informem de la operativa feta
                Toast.makeText(p_Context,
                        Globals.g_Native.getString(R.string.op_afegir_ok),
                        Toast.LENGTH_LONG).show();
                // Tanquem a qui ens ha cridat
                if (p_Tancam) {
                    Activity l_activity = (Activity) p_Context;
                    l_activity.finish();
                }
            }
        }
        return l_resultat;
    }
    public static boolean Modificar(CelebracioClient p_CelebracioClient, final Context p_Context, boolean p_Tancam){
        boolean l_resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.update(TAG_CelebracionsClient,
                    CelebracioClientToContentValues(p_CelebracioClient, false),
                    TAG_Codi + "= " + p_CelebracioClient.Codi,
                    null);
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
            l_resultat = false;
        }
        finally{
            Globals.TancarEspera();
            // Informem de la operativa feta
            Toast.makeText(p_Context,
                    Globals.g_Native.getString(R.string.op_modificacio_ok),
                    Toast.LENGTH_LONG).show();
            // Tanquem a qui ens ha cridat
            if (p_Tancam) {
                Activity l_activity = (Activity) p_Context;
                l_activity.finish();
            }
        }
        return l_resultat;
    }
    public static boolean Esborrar(int p_Codi, final Context p_Context, boolean p_Tancam){
        boolean l_Resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.delete(TAG_CelebracionsClient,
                    TAG_Codi + "= " + p_Codi,
                    null);
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
            l_Resultat = false;
        }
        finally{
            Globals.TancarEspera();
            // Informem de la operativa feta
            Toast.makeText(p_Context,
                    Globals.g_Native.getString(R.string.op_esborrar_ok),
                    Toast.LENGTH_LONG).show();
            // Tanquem a qui ens ha cridat
            if (p_Tancam) {
                Activity l_activity = (Activity) p_Context;
                l_activity.finish();
            }
        }
        return l_Resultat;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Posa les dades del client a contentValue
    private static ContentValues CelebracioClientToContentValues(CelebracioClient p_CelebracioClient, boolean p_Insercio) {
        ContentValues l_values = new ContentValues();

        if (p_Insercio == false) {
            l_values.put(TAG_Codi, p_CelebracioClient.Codi);
        }
        l_values.put(TAG_CodiSalo, p_CelebracioClient.Salo.Codi);
        l_values.put(TAG_Tipus, p_CelebracioClient.Tipus.Codi);
        l_values.put(TAG_Descripcio, p_CelebracioClient.Descripcio);
        l_values.put(TAG_Convidats, p_CelebracioClient.Convidats);
        l_values.put(TAG_Data, p_CelebracioClient.Data);
        l_values.put(TAG_Lloc, p_CelebracioClient.Lloc);
        l_values.put(TAG_Contacte, p_CelebracioClient.Contacte);
        l_values.put(TAG_Estat, p_CelebracioClient.Estat);

        return l_values;
    }
    // Pasa les dades del cursor a TipusCelebracio
    private static CelebracioClient CursorToCelebracioClient(Cursor p_cursor){
        CelebracioClient l_CelebracioClient = new CelebracioClient();

        l_CelebracioClient.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_CelebracioClient.Salo.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiSalo));
        l_CelebracioClient.Salo.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_SalonsClientNom));
        l_CelebracioClient.Tipus.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        l_CelebracioClient.Tipus.Descripcio = p_cursor.getString(p_cursor.getColumnIndex(TAG_TipusCelebracioDescripcio));
        l_CelebracioClient.Descripcio = p_cursor.getString(p_cursor.getColumnIndex(TAG_Descripcio));
        l_CelebracioClient.Convidats = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Convidats));
        l_CelebracioClient.Data = p_cursor.getString(p_cursor.getColumnIndex(TAG_Data));
        l_CelebracioClient.Lloc = p_cursor.getString(p_cursor.getColumnIndex(TAG_Lloc));
        l_CelebracioClient.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_CelebracioClient.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));
        return l_CelebracioClient;
    }
}
