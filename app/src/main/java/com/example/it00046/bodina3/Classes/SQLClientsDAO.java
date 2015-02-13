package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;

import com.example.it00046.bodina3.R;

public final class SQLClientsDAO {
    // Database fields
    private SQLDB db;

    public SQLClientsDAO(Context context) {
        //db = new SQLDB(context);
    }

    public void open() throws SQLException {
        //Globals.g_DB = db.getWritableDatabase();
    }

    public void close() {
        //Globals.g_DB.close();
    }

    public static void createClient(Client client) {
        ContentValues values = new ContentValues();
        values.put(Globals.g_Native.getString(R.string.TClient_CodiClient), client.CodiClient);
        values.put(Globals.g_Native.getString(R.string.TClient_Contacte), client.Contacte);
        values.put(Globals.g_Native.getString(R.string.TClient_DataAlta), client.DataAltaTexte);
        values.put(Globals.g_Native.getString(R.string.TClient_eMail), client.eMail);
        values.put(Globals.g_Native.getString(R.string.TClient_Idioma), client.Idioma);
        values.put(Globals.g_Native.getString(R.string.TClient_Nom), client.Nom);
        values.put(Globals.g_Native.getString(R.string.TClient_Pais), client.Pais);

        long insertId = Globals.g_DB.insert(Globals.g_Native.getString(R.string.TClient), null, values);

        /*
            Aquest codi serveix per donar un identificador a la insercio i
            despres com tornem lo inserit sabem quin valor s'ens ha donat

        Cursor cursor = database.query(SQLClients.TABLE_COMMENTS,
                allColumns, SQLClients.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Client newClient = cursorToComment(cursor);
        cursor.close();
        return newClient;
        */
    }

    public static void deleteClient(Client client) {
        String id = client.CodiClient;
        Globals.g_DB.delete(Globals.g_Native.getString(R.string.TClient), Globals.g_Native.getString(R.string.TClient_CodiClient) + " = " + id, null);
    }

    public static List<Client> getAllClients() {
        List<Client> clients = new ArrayList<Client>();

        Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TClient),
                Globals.g_Native.getResources().getStringArray(R.array.TClient_Camps)
                , null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Client client = cursorToClient(cursor);
            clients.add(client);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return clients;
    }

    // Funcio per recuperar el client, nomès tenim un.
    public static Client RecuperaClient(){
        Client client = new Client();

        Cursor cursor = Globals.g_DB.query(Globals.g_Native.getString(R.string.TClient), // a. table
                Globals.g_Native.getResources().getStringArray(R.array.TClient_Camps), // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            client = cursorToClient(cursor);
            Globals.g_NoHiHanDades = false;
        }
        else {
            Globals.g_NoHiHanDades = true;
        }
        return client;
    }
    //
    // Funcions privades
    //
    private static Client cursorToClient(Cursor cursor) {
        Client client = new Client();


        client.CodiClient = cursor.getString(0);
        client.eMail = cursor.getString(1);
        client.Nom = cursor.getString(2);
        client.Pais = cursor.getString(3);
        client.Contacte = cursor.getString(4);
        client.DataAltaTexte = cursor.getString(5);
        client.Idioma = cursor.getString(6);

        return client;
    }
    //
    // Funcions de acces a la BBDD del servidor (crides a PHP)
    //
}
