package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaConvidatsComplerta;
import com.example.it00046.bodina3.Classes.Custom.LVWLlistaConvidatsSimple;
import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.Classes.Feina.llista_CategoriesConvidats;
import com.example.it00046.bodina3.Classes.Feina.llista_MenusConvidats;
import com.example.it00046.bodina3.Classes.Feina.llista_RelacionsConvidats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.RequestParams;

/**
 * Created by it00046 on 29/02/2016.
 */
public class DAOConvidats {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_Convidats = Globals.g_Native.getString(R.string.TConvidats);
    // Camps del convidat
    private static final String TAG_CodiCelebracio = Globals.g_Native.getString(R.string.TConvidats_CodiCelebracio);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TConvidats_Codi);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TConvidats_Nom);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TConvidats_Tipus);
    private static final String TAG_CodiParella = Globals.g_Native.getString(R.string.TConvidats_CodiParella);
    private static final String TAG_Adresa = Globals.g_Native.getString(R.string.TConvidats_Adresa);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TConvidats_Contacte);
    private static final String TAG_Telefon = Globals.g_Native.getString(R.string.TConvidats_Telefon);
    private static final String TAG_eMail = Globals.g_Native.getString(R.string.TConvidats_eMail);
    private static final String TAG_CodiMenu = Globals.g_Native.getString(R.string.TConvidats_CodiMenu);
    private static final String TAG_Confirmat = Globals.g_Native.getString(R.string.TConvidats_Confirmat);
    private static final String TAG_Avisat = Globals.g_Native.getString(R.string.TConvidats_Avisat);
    private static final String TAG_Transport = Globals.g_Native.getString(R.string.TConvidats_Transport);
    private static final String TAG_CodiCategoria1 = Globals.g_Native.getString(R.string.TConvidats_CodiCategoria1);
    private static final String TAG_CodiCategoria2 = Globals.g_Native.getString(R.string.TConvidats_CodiCategoria2);
    private static final String TAG_CodiRelacio1 = Globals.g_Native.getString(R.string.TConvidats_CodiRelacio1);
    private static final String TAG_Comentari = Globals.g_Native.getString(R.string.TConvidats_Comentari);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TConvidats_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim de la BBDD del client els contactes: simple
    public static void Llegir(int p_CodiCelebracio, final ListView p_LVW_Convidats, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Convidat> l_Llista = new LVWLlistaConvidatsSimple(p_Context, p_Layout);
        int i;

        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_Convidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TConvidats_Camps),
                    TAG_CodiCelebracio + " = " + p_CodiCelebracio,
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (i=0; i < l_cursor.getCount(); i++) {
                    Convidat l_Convidat = CursorToConvidat(l_cursor);
                    l_Llista.add(l_Convidat);
                    l_cursor.moveToNext();
                }
                p_LVW_Convidats.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    public static void LlegirComplerta(int p_CodiCelebracio, final ListView p_LVW_Convidats, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Convidat> l_Llista = new LVWLlistaConvidatsComplerta(p_Context, p_Layout);
        int i;

        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_Convidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TConvidats_Camps),
                    TAG_CodiCelebracio + " = " + p_CodiCelebracio,
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (i=0; i < l_cursor.getCount(); i++) {
                    Convidat l_Convidat = CursorToConvidat(l_cursor);
                    l_Llista.add(l_Convidat);
                    l_cursor.moveToNext();
                }
                p_LVW_Convidats.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    public static boolean Esborrar(int p_Codi, final Context p_Context, boolean p_Tancam) {
        ContentValues l_actualitzat = new ContentValues();
        boolean l_Resultat = true;

        Globals.MostrarEspera(p_Context);
        l_actualitzat.put(TAG_Estat, Convidat.k_EstatEsborrat);
        try {
            Globals.g_DB.update(TAG_Convidats,
                    l_actualitzat,
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
                l_activity.setResult(Activity.RESULT_OK);
                l_activity.finish();
            }
        }
        return l_Resultat;
    }

    // Es com un llegir convidat pero com la operativa la fem dintre de una altre no tenim contexte
    public static Convidat LlegirParella(int p_Codi, int p_CodiCelebracio){
        Convidat l_Convidat = new Convidat();

        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_Convidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TConvidats_Camps),
                    TAG_CodiCelebracio + " = " + p_CodiCelebracio + " AND " + TAG_Codi + " = " + p_Codi,
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    l_Convidat = CursorToConvidat(l_cursor);
                    break;
                }
            }
        }
        catch(Exception e) {
            // S'ha produit un error al recuperar el convidat
            l_Convidat.Nom = "...";
        }

        return l_Convidat;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static Convidat CursorToConvidat(Cursor p_cursor){
        Convidat l_Convidat = new Convidat();

        l_Convidat.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_Convidat.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_Convidat.Tipus = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        if (l_Convidat.Tipus == Convidat.k_TipusParella){
            l_Convidat.Parella = LlegirParella(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiParella)), p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCelebracio)));
        }
        l_Convidat.Adresa = p_cursor.getString(p_cursor.getColumnIndex(TAG_Adresa));
        l_Convidat.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_Convidat.Telefon = p_cursor.getString(p_cursor.getColumnIndex(TAG_Telefon));
        l_Convidat.eMail = p_cursor.getString(p_cursor.getColumnIndex(TAG_eMail));
        l_Convidat.Menu = llista_MenusConvidats.DadesMenu(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiMenu)));
        l_Convidat.Confirmat = (p_cursor.getInt(p_cursor.getColumnIndex(TAG_Confirmat)) != 0);
        l_Convidat.Avisat = (p_cursor.getInt(p_cursor.getColumnIndex(TAG_Avisat)) != 0);
        l_Convidat.Transport = (p_cursor.getInt(p_cursor.getColumnIndex(TAG_Transport)) != 0);
        l_Convidat.Categoria1 = llista_CategoriesConvidats.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria1)));
        l_Convidat.Categoria2 = llista_CategoriesConvidats.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria2)));
        l_Convidat.Relacio1 = llista_RelacionsConvidats.DadesGrup(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiRelacio1)));
        l_Convidat.Comentari = p_cursor.getString(p_cursor.getColumnIndex(TAG_Comentari));
        l_Convidat.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));

        return l_Convidat;
    }
}