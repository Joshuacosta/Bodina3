package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaTaulesClient;
import com.example.it00046.bodina3.Classes.Custom.LVWLlistaTaulesClientSeleccio;
import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNTaulesClient;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNTipusCelebracio;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by it00046 on 05/08/2015.
 */
public class DAOTaulesClient {
    private static final String TAG_TaulesClient = Globals.g_Native.getString(R.string.TTaulesClient);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TTaulesClient_Codi);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TTaulesClient_Tipus);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TTaulesClient_Descripcio);
    private static final String TAG_MaxPersones = Globals.g_Native.getString(R.string.TTaulesClient_MaxPersones);
    private static final String TAG_AmpladaDiametre = Globals.g_Native.getString(R.string.TTaulesClient_AmpladaDiametre);
    private static final String TAG_Llargada = Globals.g_Native.getString(R.string.TTaulesClient_Llargada);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TTaulesClient_Estat);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim les taules del client en una list view
    public static void Llegir(final ListView p_LVW_TaulesClient, int p_Layout, final Context p_Context) {
        final ArrayAdapter<TaulaClient> l_Llista = new LVWLlistaTaulesClient(p_Context, p_Layout);
        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_TaulesClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TTaulesClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    TaulaClient l_Tipus = CursorToTaulaClient(l_cursor);
                    l_Llista.add(l_Tipus);
                    l_cursor.moveToNext();
                }
                p_LVW_TaulesClient.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    // Llegim les taules del client en una list view per seleccionar
    public static void LlegirSeleccio(final ListView p_LVW_TaulesClient, int p_Layout, final Context p_Context) {
        final ArrayAdapter<TaulaClient> l_Llista = new LVWLlistaTaulesClientSeleccio(p_Context, p_Layout);
        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_TaulesClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TTaulesClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    TaulaClient l_Tipus = CursorToTaulaClient(l_cursor);
                    l_Llista.add(l_Tipus);
                    l_cursor.moveToNext();
                }
                p_LVW_TaulesClient.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    // Llegim les taules del client en una spinner
    public static void Llegir(final Spinner p_SPN_TaulesClient, final Context p_Context) {
        ArrayAdapter<SPNTaulesClient> l_dataAdapter;
        final List<SPNTaulesClient> l_TaulesClient = new ArrayList<SPNTaulesClient>();
        String l_DescripcioTaula;

        // Posem a la llista la entrada de "Seleccioni..."
        SPNTaulesClient l_SelectOne = new SPNTaulesClient(null, Globals.g_Native.getString(R.string.llista_Select));
        l_TaulesClient.add(l_SelectOne);
        //
        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_TaulesClient,
                    Globals.g_Native.getResources().getStringArray(R.array.TTaulesClient_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    TaulaClient l_Taula = CursorToTaulaClient(l_cursor);
                    l_DescripcioTaula = l_Taula.Detall();
                    SPNTaulesClient l_spinner = new SPNTaulesClient(l_Taula, l_DescripcioTaula);
                    l_TaulesClient.add(l_spinner);
                    l_cursor.moveToNext();
                }
            }
            // Associem
            l_dataAdapter = new ArrayAdapter<SPNTaulesClient>(Globals.g_Native, R.layout.linia_spn_defecte, l_TaulesClient);
            l_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            p_SPN_TaulesClient.setAdapter(l_dataAdapter);
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    public static boolean Afegir(TaulaClient p_TaulaClient, final Context p_Context, boolean p_Asistit, boolean p_Tancam){
        boolean l_resultat = true;

        if (p_Asistit) {
            Globals.MostrarEspera(p_Context);
        }
        try {
            Globals.g_DB.insert(TAG_TaulesClient,
                                null,
                                TaulaClientToContentValues(p_TaulaClient, true));
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

    public static boolean Modificar(TaulaClient p_TaulaClient, final Context p_Context, boolean p_Tancam){
        boolean l_resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.update(TAG_TaulesClient,
                    TaulaClientToContentValues(p_TaulaClient, false),
                    TAG_Codi + "= " + p_TaulaClient.Codi,
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
        // Per esborrar una taula validem previament que no es faci servir a cap distribucio

        try {
            Globals.g_DB.delete(TAG_TaulesClient,
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

    // Definim les taules inicials
    public static void ValorsInicials() {
        TaulaClient l_TaulaClient;

        l_TaulaClient = new TaulaClient();
        l_TaulaClient.Codi = 0;
        l_TaulaClient.Tipus = TaulaClient.k_TipusRodona;
        l_TaulaClient.AmpladaDiametre = 150;
        l_TaulaClient.MaxPersones = 10;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef0);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
        l_TaulaClient.Codi = 1;
        l_TaulaClient.Tipus = TaulaClient.k_TipusRodona;
        l_TaulaClient.AmpladaDiametre = 130;
        l_TaulaClient.MaxPersones = 8;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef1);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
        l_TaulaClient.Codi = 2;
        l_TaulaClient.Tipus = TaulaClient.k_TipusRodona;
        l_TaulaClient.AmpladaDiametre = 180;
        l_TaulaClient.MaxPersones = 12;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef2);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
        l_TaulaClient.Codi = 3;
        l_TaulaClient.Tipus = TaulaClient.k_TipusRodona;
        l_TaulaClient.AmpladaDiametre = 120;
        l_TaulaClient.MaxPersones = 4;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef3);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
        l_TaulaClient.Codi = 4;
        l_TaulaClient.Tipus = TaulaClient.k_TipusQuadrada;
        l_TaulaClient.AmpladaDiametre = 150;
        l_TaulaClient.MaxPersones = 8;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef4);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
        l_TaulaClient.Codi = 5;
        l_TaulaClient.Tipus = TaulaClient.k_TipusRectangular;
        l_TaulaClient.AmpladaDiametre = 180;
        l_TaulaClient.Llargada = 75;
        l_TaulaClient.MaxPersones = 8;
        l_TaulaClient.Descripcio = Globals.g_Native.getString(R.string.TaulaClientDef5);
        Afegir(l_TaulaClient, Globals.g_Native, false, false);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Posa les dades del client a contentValue
    private static ContentValues TaulaClientToContentValues(TaulaClient p_TaulaClient, boolean p_Insercio) {
        ContentValues l_values = new ContentValues();

        if (p_Insercio == false) {
            l_values.put(TAG_Codi, p_TaulaClient.Codi);
        }
        l_values.put(TAG_Tipus, p_TaulaClient.Tipus);
        l_values.put(TAG_Descripcio, p_TaulaClient.Descripcio);
        l_values.put(TAG_MaxPersones, p_TaulaClient.MaxPersones);
        l_values.put(TAG_AmpladaDiametre, p_TaulaClient.AmpladaDiametre);
        l_values.put(TAG_Llargada, p_TaulaClient.Llargada);
        l_values.put(TAG_Estat, p_TaulaClient.Estat);
        return l_values;
    }

    // Pasa les dades del cursor a TaulaClient
    private static TaulaClient CursorToTaulaClient(Cursor p_cursor){
        TaulaClient l_TaulaClient = new TaulaClient();

        l_TaulaClient.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_TaulaClient.Tipus = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        l_TaulaClient.Descripcio = p_cursor.getString(p_cursor.getColumnIndex(TAG_Descripcio));
        l_TaulaClient.MaxPersones = p_cursor.getInt(p_cursor.getColumnIndex(TAG_MaxPersones));
        l_TaulaClient.AmpladaDiametre = p_cursor.getInt(p_cursor.getColumnIndex(TAG_AmpladaDiametre));
        l_TaulaClient.Llargada = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Llargada));
        l_TaulaClient.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));
        return l_TaulaClient;
    }
}
