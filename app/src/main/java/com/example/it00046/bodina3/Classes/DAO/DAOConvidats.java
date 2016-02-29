package com.example.it00046.bodina3.Classes.DAO;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Custom.LVWLlistaTaulesConvidatsSeleccio;
import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.Classes.Feina.llista_CategoriesConvidats;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.PhpJson;
import com.example.it00046.bodina3.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by it00046 on 29/02/2016.
 */
public class DAOConvidats {
    // Variables
    private static RequestParams g_parametresPHP = new RequestParams();
    private static final String TAG_VALIDS = "valids";
    private static final String TAG_Convidats = Globals.g_Native.getString(R.string.TConvidats);
    // Camps del convidat
    private static final String TAG_Codi = Globals.g_Native.getString(R.string.TConvidats_Codi);
    private static final String TAG_Nom = Globals.g_Native.getString(R.string.TConvidats_Nom);
    private static final String TAG_Tipus = Globals.g_Native.getString(R.string.TConvidats_Tipus);
    private static final String TAG_Adresa = Globals.g_Native.getString(R.string.TConvidats_Adresa);
    private static final String TAG_Contacte = Globals.g_Native.getString(R.string.TConvidats_Contacte);
    private static final String TAG_Comentari = Globals.g_Native.getString(R.string.TConvidats_Comentari);
    private static final String TAG_CodiCategoria1 = Globals.g_Native.getString(R.string.TConvidats_CodiCategoria1);
    private static final String TAG_CodiCategoria2 = Globals.g_Native.getString(R.string.TConvidats_CodiCategoria2);
    private static final String TAG_CodiCategoria3 = Globals.g_Native.getString(R.string.TConvidats_CodiCategoria3);
    private static final String TAG_CodiGrup = Globals.g_Native.getString(R.string.TConvidats_CodiGrup);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TConvidats_Estat);
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // O P E R A T I V A   P U B L I C A
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Llegim de la BBDD del client els convidats
    public static void Llegir(final ListView p_LVW_Convidats, int p_Layout, final Context p_Context) {
        final ArrayAdapter<Convidat> l_Llista = new LVWLlistaTaulesConvidatsSeleccio(p_Context, p_Layout);
        int i;

        Globals.MostrarEspera(p_Context);
        try {
            Cursor l_cursor = Globals.g_DB.query(TAG_Convidats,
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
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Funcions privades
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static Convidat CursorToConvidat(Cursor p_cursor){
        Convidat l_Convidat = new Convidat();

        l_Convidat.Codi = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Codi));
        l_Convidat.Nom = p_cursor.getString(p_cursor.getColumnIndex(TAG_Nom));
        l_Convidat.Tipus = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Tipus));
        l_Convidat.Adresa = p_cursor.getString(p_cursor.getColumnIndex(TAG_Adresa));
        l_Convidat.Contacte = p_cursor.getString(p_cursor.getColumnIndex(TAG_Contacte));
        l_Convidat.Comentari = p_cursor.getString(p_cursor.getColumnIndex(TAG_Comentari));
        l_Convidat.Categoria1 = llista_CategoriesConvidats.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria1)));
        l_Convidat.Categoria2 = llista_CategoriesConvidats.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria2)));
        l_Convidat.Categoria3 = llista_CategoriesConvidats.DadesCategoria(p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiCategoria3)));
        //l_Convidat.Grup = p_cursor.getInt(p_cursor.getColumnIndex(TAG_CodiGrup));
        l_Convidat.Estat = p_cursor.getInt(p_cursor.getColumnIndex(TAG_Estat));

        return l_Convidat;
    }
}
