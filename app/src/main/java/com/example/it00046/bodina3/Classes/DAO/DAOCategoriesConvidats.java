package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaCategoriesConvidats;
import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import com.example.it00046.bodina3.Classes.Feina.llista_CategoriesConvidats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import java.util.ArrayList;

/**
 * Created by it00046 on 05/08/2015.
 */
public class DAOCategoriesConvidats {

    public static ArrayList<CategoriaConvidats> LlistaCache = new ArrayList<>();

    private static final String TAG_CategoriesConvidats = Globals.g_Native.getString(R.string.TCategoriesConvidats);
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TCategoriesConvidats_Codi);
    private static final String TAG_CodiCelebracio = Globals.g_Native.getString(R.string.TCategoriesConvidats_CodiCelebracio);
    private static final String TAG_Descripcio = Globals.g_Native.getString(R.string.TCategoriesConvidats_Descripcio);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim les categories de convidats del client per una celebracio
    // (aprofitem per carregar la llista cache
    public static void Llegir(int p_CodiCelebracio, final ListView p_LVW_CategoriesConvidats, int p_Layout, final Context p_Context) {
        final ArrayAdapter<CategoriaConvidats> l_Llista = new LVWLlistaCategoriesConvidats(p_Context, p_Layout);
        Globals.MostrarEspera(p_Context);
        try {
            LlistaCache = new ArrayList<>();
            Cursor l_cursor = Globals.g_DB.query(TAG_CategoriesConvidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TCategoriesConvidats_Camps),
                    TAG_CodiCelebracio + "= " + p_CodiCelebracio, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    CategoriaConvidats l_Categoria = CursorToCategoriaConvidats(l_cursor);
                    l_Llista.add(l_Categoria);
                    LlistaCache.add(l_Categoria);
                    l_cursor.moveToNext();
                }
                p_LVW_CategoriesConvidats.setAdapter(l_Llista);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }

    // Llegim les categories de convidats del client per una celebracio a una estructura propia
    public static void Llegir(int p_CodiCelebracio, final Context p_Context) {
        try {
            Globals.MostrarEspera(p_Context);
            Cursor l_cursor = Globals.g_DB.query(TAG_CategoriesConvidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TCategoriesConvidats_Camps),
                    TAG_CodiCelebracio + "= " + p_CodiCelebracio, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (int i=0; i < l_cursor.getCount(); i++) {
                    CategoriaConvidats l_Categoria = CursorToCategoriaConvidats(l_cursor);
                    LlistaCache.add(l_Categoria);
                    l_cursor.moveToNext();
                }
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }
    // Afegim categoria
    public static boolean Afegir(CategoriaConvidats p_CategoriaConvidats, final Context p_Context, boolean p_Asistit, boolean p_Tancam){
        boolean l_resultat = true;

        if (p_Asistit) {
            Globals.MostrarEspera(p_Context);
        }
        try {
            Globals.g_DB.insert(TAG_CategoriesConvidats,
                    null,
                    CategoriaConvidatsToContentValues(p_CategoriaConvidats, true));
        }
        catch(Exception e) {
            if (p_Asistit) {
                Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                        Globals.g_Native.getString(R.string.error_greu), p_Context);
                Globals.TancarEspera();
                l_resultat = false;
            }
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
    // Llegim categoria
    public static CategoriaConvidats LlegirCategoria(int p_CodiCelebracio, final Context p_Context){
        CategoriaConvidats l_Categoria = new CategoriaConvidats();

        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_CategoriesConvidats,
                    Globals.g_Native.getResources().getStringArray(R.array.TCategoriesConvidats_Camps),
                    TAG_CodiCelebracio + "= " + p_CodiCelebracio, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                l_Categoria = CursorToCategoriaConvidats(l_cursor);
            }
            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
        return l_Categoria;
    }
    // Modifiquem la categoria
    public static boolean Modificar(CategoriaConvidats p_CategoriaConvidats, final Context p_Context, boolean p_Tancam){
        boolean l_resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.update(TAG_CategoriesConvidats,
                    CategoriaConvidatsToContentValues(p_CategoriaConvidats, false),
                    TAG_Codi + "= " + p_CategoriaConvidats.Codi + " AND " +
                            TAG_CodiCelebracio + "= " + p_CategoriaConvidats.CodiCelebracio,
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
    // Esborrem la categoria
    public static boolean Esborrar(int p_Codi, int p_CodiCelebracio, final Context p_Context, boolean p_Tancam){
        boolean l_Resultat = true;

        Globals.MostrarEspera(p_Context);
        try {
            Globals.g_DB.delete(TAG_CategoriesConvidats,
                    TAG_Codi + "= " + p_Codi + " AND " +
                            TAG_CodiCelebracio + "= " + p_CodiCelebracio,
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
    // Definim les categories inicials per una celebracio donada
    public static void ValorsInicialsCelebracio(int p_CodiCelebracio) {
        CategoriaConvidats l_CategoriaConvidats;

        l_CategoriaConvidats = new CategoriaConvidats();
        l_CategoriaConvidats.Codi = 0;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats0);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
        l_CategoriaConvidats.Codi = 1;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats1);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
        l_CategoriaConvidats.Codi = 2;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats2);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
        l_CategoriaConvidats.Codi = 3;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats3);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
        l_CategoriaConvidats.Codi = 4;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats4);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
        l_CategoriaConvidats.Codi = 5;
        l_CategoriaConvidats.CodiCelebracio = p_CodiCelebracio;
        l_CategoriaConvidats.Descripcio = Globals.g_Native.getString(R.string.CategoriaConvidats5);
        Afegir(l_CategoriaConvidats, Globals.g_Native, false, false);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Posa les dades del client a contentValue
    private static ContentValues CategoriaConvidatsToContentValues(CategoriaConvidats p_CategoriaConvidats, boolean p_Insercio) {
        ContentValues l_values = new ContentValues();

        if (p_Insercio == false) {
            l_values.put(TAG_Codi, p_CategoriaConvidats.Codi);
        }
        l_values.put(TAG_CodiCelebracio, p_CategoriaConvidats.CodiCelebracio);
        l_values.put(TAG_Descripcio, p_CategoriaConvidats.Descripcio);
        return l_values;
    }
    // Pasa les dades del cursor a CategoriaConvidats
    private static CategoriaConvidats CursorToCategoriaConvidats(Cursor p_cursor){
        CategoriaConvidats l_CategoriaConvidats = new CategoriaConvidats();

        l_CategoriaConvidats.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_CategoriaConvidats.CodiCelebracio = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCelebracio));
        l_CategoriaConvidats.Descripcio = p_cursor.getString(p_cursor.getColumnIndex(TAG_Descripcio));
        return l_CategoriaConvidats;
    }
}