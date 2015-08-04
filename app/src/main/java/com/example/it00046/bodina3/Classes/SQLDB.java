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
        String l_Create;
        // Definim les taules de la BBDD local
        // Client
        l_Create = String.format(Globals.g_Native.getString(R.string.TClientCreate),
                Globals.g_Native.getString(R.string.TClient),
                Globals.g_Native.getString(R.string.TClient_Codi),
                Globals.g_Native.getString(R.string.TClient_eMail),
                Globals.g_Native.getString(R.string.TClient_Nom),
                Globals.g_Native.getString(R.string.TClient_Pais),
                Globals.g_Native.getString(R.string.TClient_Contacte),
                Globals.g_Native.getString(R.string.TClient_DataAlta),
                Globals.g_Native.getString(R.string.TClient_Idioma),
                Globals.g_Native.getString(R.string.TClient_Actualitzat));
        db.execSQL(l_Create);
        // Salons
        l_Create = String.format(Globals.g_Native.getString(R.string.TSalons),
                Globals.g_Native.getString(R.string.TSalons_Codi),
                Globals.g_Native.getString(R.string.TSalons_Nom),
                Globals.g_Native.getString(R.string.TSalons_Amplada),
                Globals.g_Native.getString(R.string.TSalons_Alsada),
                Globals.g_Native.getString(R.string.TSalons_CodiPlanol),
                Globals.g_Native.getString(R.string.TSalons_Estat));
        db.execSQL(l_Create);
        // Tipus de celebracio
        l_Create = String.format(Globals.g_Native.getString(R.string.TTipusCelebracio),
                Globals.g_Native.getString(R.string.TTipusCelebracio_Codi),
                Globals.g_Native.getString(R.string.TTipusCelebracio_Descripcio));
        db.execSQL(l_Create);
        // Planols
        l_Create = String.format(Globals.g_Native.getString(R.string.TPlanols),
                Globals.g_Native.getString(R.string.TPlanols_Codi),
                Globals.g_Native.getString(R.string.TPlanols_OrigenX),
                Globals.g_Native.getString(R.string.TPlanols_OrigenY),
                Globals.g_Native.getString(R.string.TPlanols_DestiX),
                Globals.g_Native.getString(R.string.TPlanols_DestiY),
                Globals.g_Native.getString(R.string.TPlanols_Angle));
        db.execSQL(l_Create);
        // Celebracions
        l_Create = String.format(Globals.g_Native.getString(R.string.TCelebracions),
                Globals.g_Native.getString(R.string.TCelebracions_Codi),
                Globals.g_Native.getString(R.string.TCelebracions_CodiSalo),
                Globals.g_Native.getString(R.string.TCelebracions_Tipus),
                Globals.g_Native.getString(R.string.TCelebracions_Descripcio),
                Globals.g_Native.getString(R.string.TCelebracions_Convidats),
                Globals.g_Native.getString(R.string.TCelebracions_Data),
                Globals.g_Native.getString(R.string.TCelebracions_Lloc),
                Globals.g_Native.getString(R.string.TCelebracions_Contacte),
                Globals.g_Native.getString(R.string.TCelebracions_Estat));
        db.execSQL(l_Create);
        // Distribucions
        l_Create = String.format(Globals.g_Native.getString(R.string.TDistribucions),
                Globals.g_Native.getString(R.string.TDistribucions_CodiCelebracio),
                Globals.g_Native.getString(R.string.TDistribucions_CodiDistribucio),
                Globals.g_Native.getString(R.string.TDistribucions_Nom),
                Globals.g_Native.getString(R.string.TDistribucions_DataAlta),
                Globals.g_Native.getString(R.string.TDistribucions_DataModificacio),
                Globals.g_Native.getString(R.string.TDistribucions_Estat));
        db.execSQL(l_Create);
        // Distribucio Taules
        l_Create = String.format(Globals.g_Native.getString(R.string.TDistribucioTaules),
                Globals.g_Native.getString(R.string.TDistribucioTaules_CodiCelebracio),
                Globals.g_Native.getString(R.string.TDistribucioTaules_CodiDistribucio),
                Globals.g_Native.getString(R.string.TDistribucioTaules_NumTaula),
                Globals.g_Native.getString(R.string.TDistribucioTaules_CodiTaula),
                Globals.g_Native.getString(R.string.TDistribucioTaules_Descripcio),
                Globals.g_Native.getString(R.string.TDistribucioTaules_PosicioX),
                Globals.g_Native.getString(R.string.TDistribucioTaules_PosicioY));
        db.execSQL(l_Create);
        // Distribucio Convidats
        l_Create = String.format(Globals.g_Native.getString(R.string.TDistribucioConvidats),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_CodiCelebracio),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_CodiDistribucio),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_CodiConvidat),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_PosicioX),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_PosicioY),
                Globals.g_Native.getString(R.string.TDistribucioConvidats_NumTaula));
        db.execSQL(l_Create);
        // Categories de convidats
        l_Create = String.format(Globals.g_Native.getString(R.string.TCategoriesConvidats),
                Globals.g_Native.getString(R.string.TCategoriesConvidats_Codi),
                Globals.g_Native.getString(R.string.TCategoriesConvidats_CodiCelebracio),
                Globals.g_Native.getString(R.string.TCategoriesConvidats_Descripcio));
        db.execSQL(l_Create);
        // Menus de convidats
        l_Create = String.format(Globals.g_Native.getString(R.string.TMenusConvidats),
                Globals.g_Native.getString(R.string.TMenusConvidats_Codi),
                Globals.g_Native.getString(R.string.TMenusConvidats_Descripcio));
        db.execSQL(l_Create);
        // Si executem aixó es que hem creat la BBDD i no hi han dades, de moment:
        Globals.g_NoHiHanDades = true;
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