package com.example.it00046.bodina3.Classes.DAO;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaTaulesContactesSeleccio;
import com.example.it00046.bodina3.Classes.Entitats.Contacte;
import com.example.it00046.bodina3.Classes.Feina.llista_CategoriesContactes;
import com.example.it00046.bodina3.Classes.Feina.llista_RelacionsContactes;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.RequestParams;

/**
 * Created by it00046 on 29/02/2016.
 */
public class DAOContactes {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_Contactes = Globals.g_Native.getString(R.string.TContactes);
    // Camps del contacte
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TContactes_Codi);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TContactes_Nom);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TContactes_Tipus);
    private static final String TAG_Adresa = Globals.g_Native.getString(R.string.TContactes_Adresa);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TContactes_Contacte);
    private static final String TAG_Comentari = Globals.g_Native.getString(R.string.TContactes_Comentari);
    private static final String TAG_CodiCategoria1 = Globals.g_Native.getString(R.string.TContactes_CodiCategoria1);
    private static final String TAG_CodiCategoria2 = Globals.g_Native.getString(R.string.TContactes_CodiCategoria2);
    private static final String TAG_CodiCategoria3 = Globals.g_Native.getString(R.string.TContactes_CodiCategoria3);
    private static final String TAG_CodiRelacio1 = Globals.g_Native.getString(R.string.TContactes_CodiRelacio1);
    private static final String TAG_CodiRelacio2 = Globals.g_Native.getString(R.string.TContactes_CodiRelacio2);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TContactes_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim de la BBDD del client els contactes
    public static void Llegir(final ListView p_LVW_Contactes, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Contacte> l_Llista = new LVWLlistaTaulesContactesSeleccio(p_Context, p_Layout);
        int i;

        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_Contactes,
                    Globals.g_Native.getResources().getStringArray(R.array.TContactes_Camps),
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if (l_cursor.getCount() > 0) {
                l_cursor.moveToFirst();
                for (i=0; i < l_cursor.getCount(); i++) {
                    Contacte l_Contacte = CursorToContacte(l_cursor);
                    l_Llista.add(l_Contacte);
                    l_cursor.moveToNext();
                }
                p_LVW_Contactes.setAdapter(l_Llista);
            }

            Globals.TancarEspera();
        }
        catch(Exception e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu), p_Context);
            Globals.TancarEspera();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static Contacte CursorToContacte(Cursor p_cursor){
        Contacte l_Contacte = new Contacte();

        l_Contacte.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_Contacte.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_Contacte.Tipus = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        l_Contacte.Adresa = p_cursor.getString(p_cursor.getColumnIndex(TAG_Adresa));
        l_Contacte.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_Contacte.Comentari = p_cursor.getString(p_cursor.getColumnIndex(TAG_Comentari));
        l_Contacte.Categoria1 = llista_CategoriesContactes.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria1)));
        l_Contacte.Categoria2 = llista_CategoriesContactes.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria2)));
        l_Contacte.Categoria3 = llista_CategoriesContactes.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria3)));
        l_Contacte.Relacio1 = llista_RelacionsContactes.DadesGrup(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiRelacio1)));
        l_Contacte.Relacio2 = llista_RelacionsContactes.DadesGrup(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiRelacio2)));
        l_Contacte.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));

        return l_Contacte;
    }
}