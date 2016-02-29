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
import com.example.it00046.bodina3.Classes.Entitats.Planol;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNSalonsClient;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by it00046 on 02/06/2015.
 */
public final class DAOSalonsClient {
    // Dades salo
    private static final String TAG_SalonsClient = Globals.g_Native.getString(R.string.TSalonsClient);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TSalonsClient_Codi);
    private static final String TAG_Nom  = Globals.g_Native.getString(R.string.TSalonsClient_Nom);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TSalonsClient_Estat);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TSalonsClient_Descripcio);
    private static final String TAG_Capacitat  = Globals.g_Native.getString(R.string.TSalonsClient_Capacitat);
    private static final String TAG_UnitatsPlanol = Globals.g_Native.getString(R.string.TSalonsClient_UnitatsPlanol);
    private static final String TAG_EscalaPlanol = Globals.g_Native.getString(R.string.TSalonsClient_EscalaPlanol);
    private static final String TAG_UnitatMesura = Globals.g_Native.getString(R.string.TSalonsClient_UnitatMesura);
    // Planol
    private static final String TAG_PlanolClient = Globals.g_Native.getString(R.string.TPlanols);
    // Detall planol
    private static final String TAG_CodiPlanol = Globals.g_Native.getString(R.string.TPlanols_CodiSalo);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TPlanols_Tipus);
    private static final String TAG_OrigenX = Globals.g_Native.getString(R.string.TPlanols_OrigenX);
    private static final String TAG_OrigenY = Globals.g_Native.getString(R.string.TPlanols_OrigenY);
    private static final String TAG_DestiX = Globals.g_Native.getString(R.string.TPlanols_DestiX);
    private static final String TAG_DestiY = Globals.g_Native.getString(R.string.TPlanols_DestiY);
    private static final String TAG_CurvaX = Globals.g_Native.getString(R.string.TPlanols_CurvaX);
    private static final String TAG_CurvaY = Globals.g_Native.getString(R.string.TPlanols_CurvaY);
    private static final String TAG_Texte = Globals.g_Native.getString(R.string.TPlanols_Texte);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Recuperem salons i planols
    public static void Llegir(final ListView p_LVW_SalonsClient, int p_Layout, final Context p_Context) {
        int i, j;
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
                for (i=0; i < l_cursor.getCount(); i++) {
                    SaloClient l_Salo = CursorToSaloClient(l_cursor);
                    // Llegim les dades del planol (si hi ha)
                    Cursor l_cursorPlanol = Globals.g_DB.query(TAG_PlanolClient,
                            Globals.g_Native.getResources().getStringArray(R.array.TPlanols_Camps),
                            TAG_CodiPlanol + "= " + l_Salo.Codi,
                            null, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit
                    if (l_cursorPlanol.getCount() > 0) {
                        l_cursorPlanol.moveToFirst();
                        for (j=0; j < l_cursorPlanol.getCount(); j++) {
                            Planol.Detall l_DetallPlanol = CursorToSaloPlanolClient(l_cursorPlanol);
                            l_Salo.Planol.Grava(l_DetallPlanol);
                            l_cursorPlanol.moveToNext();
                        }
                    }
                    l_cursorPlanol.close();
                    // Afegim salo i seguent
                    l_Llista.add(l_Salo);
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

    // Llegim els salons del client a un spinner (sense la info de planol)
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
                    l_cursor.moveToNext();
                }
            }
            // Associem
            l_dataAdapter = new ArrayAdapter<SPNSalonsClient>(Globals.g_Native, R.layout.linia_spn_defecte, l_SalonsClient);
            l_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            p_SPN_SalonsClient.setAdapter(l_dataAdapter);
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
        long l_CodiSalo;

        if (p_Asistit) {
            Globals.MostrarEspera(p_Context);
        }
        try {
            Globals.g_DB.beginTransaction();
            // Inserim el salo i recuperem el codi de salo
            l_CodiSalo = Globals.g_DB.insert(TAG_SalonsClient,
                                             null,
                                             SaloClientToContentValues(p_SaloClient, true));
            // Inserim el planol (si hi ha)
            if (p_SaloClient.Planol.Elements() > 0){
                for (int i=0; i < p_SaloClient.Planol.Elements(); i ++){
                    Globals.g_DB.insert(TAG_PlanolClient,
                            null,
                            SaloClientPlanolToContentValues(p_SaloClient, i, l_CodiSalo));
                }
            }
            Globals.g_DB.setTransactionSuccessful();
        }
        catch(Exception e) {
            Globals.g_DB.endTransaction();
            if (p_Asistit) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                        Globals.g_Native.getString(R.string.error_greu), p_Context);
                Globals.TancarEspera();
            }
            l_resultat = false;
        }
        finally{
            Globals.g_DB.endTransaction();
            if (p_Asistit) {
                Globals.TancarEspera();
                // Informem de la operativa feta
                Toast.makeText(p_Context,
                        Globals.g_Native.getString(R.string.op_afegir_ok),
                        Toast.LENGTH_LONG).show();
                // Tanquem a qui ens ha cridat
                if (p_Tancam) {
                    Activity l_activity = (Activity) p_Context;
                    l_activity.setResult(Activity.RESULT_OK);
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
            Globals.g_DB.beginTransaction();
            // Modiquem el salo
            Globals.g_DB.update(TAG_SalonsClient,
                                SaloClientToContentValues(p_SaloClient, false),
                                TAG_Codi + "= " + p_SaloClient.Codi,
                                null);
            // Modifiquem el planol (si hi ha)
            if (p_SaloClient.Planol.Elements() > 0){
                // Esborrem previament
                Globals.g_DB.delete(TAG_PlanolClient,
                        TAG_CodiPlanol + "= " + p_SaloClient.Codi,
                        null);
                // Gravem el nou (o lo mateix, si no s'ha tocat res)
                for (int i = 0; i < p_SaloClient.Planol.Elements(); i++){
                    Globals.g_DB.insert(TAG_PlanolClient,
                            null,
                            SaloClientPlanolToContentValues(p_SaloClient, i, p_SaloClient.Codi));
                }
            }
            Globals.g_DB.setTransactionSuccessful();
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

            // Amb el tema del planol hauras d'anar amb compte de esborrar-lo abans

            // Tanquem a qui ens ha cridat
            if (p_Tancam) {
                Activity l_activity = (Activity) p_Context;
                l_activity.setResult(Activity.RESULT_OK);
                l_activity.finish();
            }
        }
        return l_resultat;
    }

    public static boolean Esborrar(int p_Codi, final Context p_Context, boolean p_Tancam){
        boolean l_Resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.beginTransaction();
            // Esborrem salo
            Globals.g_DB.delete(TAG_SalonsClient,
                    TAG_Codi + "= " + p_Codi,
                    null);
            // Esborrem planol (si hi ha)
            Globals.g_DB.delete(TAG_PlanolClient,
                    TAG_CodiPlanol + "= " + p_Codi,
                    null);
            //
            Globals.g_DB.setTransactionSuccessful();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
            l_Resultat = false;
        }
        finally{
            Globals.g_DB.endTransaction();
            Globals.TancarEspera();
            // Informem de la operativa feta
            Toast.makeText(p_Context,
                    Globals.g_Native.getString(R.string.op_esborrar_ok),
                    Toast.LENGTH_LONG).show();
            // Tanquem a qui ens ha cridat
            if (p_Tancam) {
                Activity l_activity = (Activity) p_Context;
                l_activity.setResult(Activity.RESULT_OK);
                l_activity.finish();
            }
        }
        return l_Resultat;
    }

    public static Planol LlegirPlanolSalo(int P_Codi, Context p_Context){
        Planol l_PlanolSalo = new Planol();
        int i, j;

        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_SalonsClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TSalonsClient_Camps),
                    TAG_Codi + "= " + P_Codi,
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                // Nomes llegirem un salo (el trobat)
                for (i = 0; i < l_cursor.getCount(); i++) {
                    // Llegim les dades del planol
                    Cursor l_cursorPlanol = Globals.g_DB.query(TAG_PlanolClient,
                            Globals.g_Native.getResources().getStringArray(R.array.TPlanols_Camps),
                            TAG_CodiPlanol + "= " + P_Codi,
                            null, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit
                    if (l_cursorPlanol.getCount() > 0) {
                        l_cursorPlanol.moveToFirst();
                        for (j = 0; j < l_cursorPlanol.getCount(); j++) {
                            Planol.Detall l_DetallPlanol = CursorToSaloPlanolClient(l_cursorPlanol);
                            l_PlanolSalo.Grava(l_DetallPlanol);
                            l_cursorPlanol.moveToNext();
                        }
                    }
                    l_cursorPlanol.close();
                }
                l_cursor.close();
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
        return l_PlanolSalo;
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
        l_values.put(TAG_Descripcio, p_SaloClient.Descripcio);
        l_values.put(TAG_Capacitat, p_SaloClient.Capacitat);
        l_values.put(TAG_EscalaPlanol, p_SaloClient.EscalaPlanol);
        l_values.put(TAG_UnitatsPlanol, p_SaloClient.UnitatsPlanol);
        l_values.put(TAG_UnitatMesura, p_SaloClient.UnitatMesura);
        l_values.put(TAG_Estat, p_SaloClient.Estat);

        return l_values;
    }
    private static ContentValues SaloClientPlanolToContentValues(SaloClient p_SaloClient, int p_Index, long p_CodiSalo) {
        ContentValues l_values = new ContentValues();
        Planol.Detall l_DetallPlanol;

        l_DetallPlanol = p_SaloClient.Planol.Llegeix(p_Index);

        l_values.put(TAG_CodiPlanol, p_CodiSalo);
        l_values.put(TAG_Tipus, l_DetallPlanol.Tipus);
        l_values.put(TAG_OrigenX, l_DetallPlanol.OrigenX);
        l_values.put(TAG_OrigenY, l_DetallPlanol.OrigenY);
        l_values.put(TAG_DestiX, l_DetallPlanol.DestiX);
        l_values.put(TAG_DestiY, l_DetallPlanol.DestiY);
        l_values.put(TAG_CurvaX, l_DetallPlanol.CurvaX);
        l_values.put(TAG_CurvaY, l_DetallPlanol.CurvaY);
        l_values.put(TAG_Texte, l_DetallPlanol.Texte);

        return l_values;
    }

    private static SaloClient CursorToSaloClient(Cursor p_cursor){
        SaloClient l_SaloClient = new SaloClient();

        l_SaloClient.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_SaloClient.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_SaloClient.Capacitat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Capacitat));
        l_SaloClient.Descripcio = p_cursor.getString(p_cursor.getColumnIndex(TAG_Descripcio));
        l_SaloClient.EscalaPlanol = p_cursor.getString(p_cursor.getColumnIndex(TAG_EscalaPlanol));
        l_SaloClient.UnitatsPlanol = p_cursor.getString(p_cursor.getColumnIndex(TAG_UnitatsPlanol));
        l_SaloClient.UnitatMesura = p_cursor.getInt(p_cursor.getColumnIndex(TAG_UnitatMesura));
        l_SaloClient.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));

        return l_SaloClient;
    }

    private static Planol.Detall CursorToSaloPlanolClient(Cursor p_cursor){
        Planol.Detall l_Detall = new Planol.Detall();

        l_Detall.Tipus = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        l_Detall.OrigenX = p_cursor.getInt(p_cursor.getColumnIndex(TAG_OrigenX));
        l_Detall.OrigenY = p_cursor.getInt(p_cursor.getColumnIndex(TAG_OrigenY));
        l_Detall.DestiX = p_cursor.getInt(p_cursor.getColumnIndex(TAG_DestiX));
        l_Detall.DestiY = p_cursor.getInt(p_cursor.getColumnIndex(TAG_DestiY));
        l_Detall.CurvaX = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CurvaX));
        l_Detall.CurvaY = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CurvaY));
        l_Detall.Texte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Texte));

        return l_Detall;
    }
}
