package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.it00046.bodina3.R;

public class SQLDB extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = Globals.g_Native.getString(R.string.DBName);

    public SQLDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Definim les taules de la BBDD
        String CREATE = String.format(Globals.g_Native.getString(R.string.TClientCreate),
                Globals.g_Native.getString(R.string.TClient),
                Globals.g_Native.getString(R.string.TClient_CodiClient),
                Globals.g_Native.getString(R.string.TClient_eMail),
                Globals.g_Native.getString(R.string.TClient_Nom),
                Globals.g_Native.getString(R.string.TClient_Pais),
                Globals.g_Native.getString(R.string.TClient_Contacte),
                Globals.g_Native.getString(R.string.TClient_DataAlta),
                Globals.g_Native.getString(R.string.TClient_Idioma),
                Globals.g_Native.getString(R.string.TClient_Actualitzat),
                Globals.g_Native.getString(R.string.TClient_DataActualitzat),
                Globals.g_Native.getString(R.string.TClient_DataGrabacioServidor));
        db.execSQL(CREATE);
        // Si executem aixó es que hem creat la BBDD i no hi han dades, de moment:
        Globals.g_NoHiHanDades = true;
        //
        // Experiment per recuperar el identificador a partir de un texte
        //int aux = Globals.g_Native.getResources().getIdentifier("TClientCreate", "string", "com.example.it00046.bodina3");
        //String auxc = Globals.g_Native.getString(aux);
        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui expresem el canvis (no se si podria ser necessari trespassar dades i tal
        // per evitar problema en l'usuari
        String DROP = String.format(Globals.g_Native.getString(R.string.TDrop),
                                    Globals.g_Native.getString(R.string.TClient));
        db.execSQL(DROP);
        // Tornem a definir les taules
        this.onCreate(db);
    }

}