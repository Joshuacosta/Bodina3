package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaSalonsClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNSalonsClient;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by it00046 on 02/06/2015.
 */
public final class DAOSalonsClient {
    private static final String TAG_SalonsClient = Globals.g_Native.getString(R.string.TSalonsClient);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TSalonsClient_Codi);
    private static final String TAG_Nom  = Globals.g_Native.getString(R.string.TSalonsClient_Nom);
    private static final String TAG_Amplada = Globals.g_Native.getString(R.string.TSalonsClient_Amplada);
    private static final String TAG_Alsada = Globals.g_Native.getString(R.string.TSalonsClient_Alsada);
    private static final String TAG_CodiPlanol = Globals.g_Native.getString(R.string.TSalonsClient_CodiPlanol);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TSalonsClient_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void Llegir(final ListView p_LVW_SalonsClient, int p_Layout, final Context p_Context) {
        final ArrayAdapter<SaloClient> l_Llista = new LVWLlistaSalonsClient(p_Context, p_Layout);
        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_SalonsClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TSalonsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    SaloClient l_Tipus = CursorToSaloClient(l_cursor);
                    l_Llista.add(l_Tipus);
                    l_cursor.moveToNext();
                }
                p_LVW_SalonsClient.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    // Llegim els tipus de celebracio del client en una spinner
    public static void Llegir(final Spinner p_SPN_SalonsClient, final Context p_Context) {
        ArrayAdapter<SPNSalonsClient> l_dataAdapter;
        final List<SPNSalonsClient> l_SalonsClient = new ArrayList<SPNSalonsClient>();
        String l_NomSaloSpinner;

        // Posem a la llista la entrada de "Seleccioni..."
        SPNSalonsClient l_SelectOne = new SPNSalonsClient(null, Globals.g_Native.getString(R.string.llista_Select));
        l_SalonsClient.add(l_SelectOne);
        //
        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_SalonsClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TSalonsClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    SaloClient l_Salo = CursorToSaloClient(l_cursor);
                    l_NomSaloSpinner = l_Salo.Nom;
                    SPNSalonsClient l_spinner = new SPNSalonsClient(l_Salo, l_NomSaloSpinner);
                    l_SalonsClient.add(l_spinner);
                }
                // Associem
                l_dataAdapter = new ArrayAdapter<SPNSalonsClient>(Globals.g_Native, R.layout.linia_spn_defecte, l_SalonsClient);
                l_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                p_SPN_SalonsClient.setAdapter(l_dataAdapter);

            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    public static boolean Afegir(SaloClient p_SaloClient, final Context p_Context, boolean p_Asistit, boolean p_Tancam){
        boolean l_resultat = true;

        if (p_Asistit) {
            Globals.MostrarEspera(p_Context);
        }
        try {
            Globals.g_DB.insert(TAG_SalonsClient,
                                null,
                                SaloClientToContentValues(p_SaloClient, true));
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

    public static boolean Modificar(SaloClient p_SaloClient, final Context p_Context, boolean p_Tancam){
        boolean l_resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.update(TAG_SalonsClient,
                    SaloClientToContentValues(p_SaloClient, false),
                    TAG_Codi + "= " + p_SaloClient.Codi,
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
            Globals.g_DB.delete(TAG_SalonsClient,
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
    private static ContentValues SaloClientToContentValues(SaloClient p_SaloClient, boolean p_Insercio) {
        ContentValues l_values = new ContentValues();

        if (p_Insercio == false) {
            l_values.put(TAG_Codi, p_SaloClient.Codi);
        }
        l_values.put(TAG_Nom, p_SaloClient.Nom);
        l_values.put(TAG_Amplada, p_SaloClient.Amplada);
        l_values.put(TAG_Alsada, p_SaloClient.Alsada);
        l_values.put(TAG_CodiPlanol, p_SaloClient.CodiPlanol);
        l_values.put(TAG_Estat, p_SaloClient.Estat);

        return l_values;
    }
    private static SaloClient CursorToSaloClient(Cursor p_cursor){
        SaloClient l_SaloClient = new SaloClient();

        l_SaloClient.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_SaloClient.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_SaloClient.Amplada = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Amplada));
        l_SaloClient.Alsada = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Alsada));
        l_SaloClient.CodiPlanol = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiPlanol));
        l_SaloClient.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));

        return l_SaloClient;
    }
}
