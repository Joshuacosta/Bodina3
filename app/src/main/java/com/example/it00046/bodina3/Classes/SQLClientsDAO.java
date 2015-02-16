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
        UpdateClient(client);
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

    // Funcio per updatar la informació del client
    public static void UpdateClient(Client p_client){
        if (Globals.isNetworkAvailable()){
            PHP operacio = new PHP();
            operacio.execute("un altres phpss");
        }
    }

    // Funcio per updatar codi client
    public static void UpdateCodiClient(Client p_client){

    }


    // Funcio per updatar actualitzat
    public static void UpdateActualitzat(Client p_client){

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
    // ------------------------------------------------------------------------------------------
    // Funcions de acces a la BBDD del servidor (crides a PHP)
    // ------------------------------------------------------------------------------------------
    /*
    // Aquesta funció accedeix a la BBDD del servidor i recupera la informació del client existent
    // (si no troba informació es que es la primera vegada que l'usuari accedeix al sistema).
    static public function RecuperarDadesServidor():Boolean{
        var PHP:FuncionsPHP = new FuncionsPHP();
        var l_Parametres:Object = new Object();

        // Validem que la xarxa estigui activa
        if (Globals.g_HiHaXarxa){
            // La comunicació de variables es amb vars
            l_Parametres.CodiClientIntern = Globals.g_Client.CodiClientIntern;
            l_Parametres.Operativa = Globals.k_OPE_SelectCodiClientIntern;
            PHP.FerPHP_Vars("http://bodina.virtuol.com/php/Clients.php", l_Parametres, CarregaDades);
        }
        else{
            Funcions.Avis(ResourceManager.getInstance().getString('resources','php.Avis'),
                    ResourceManager.getInstance().getString('resources','php.NoServidor'));
        }
        return true;
    }
    // Si la crida anterior ha anat be carreguem les dades globals de client
    static public function CarregaDades(event:ResultEvent):void{
        var l_Resultat:Object = event.result;
        var c_Client:Client = new Client();

        if (l_Resultat.CodiClient != Globals.k_ClientNOU){
            c_Client.CodiClient = l_Resultat.CodiClient;
            c_Client.CodiIdioma = l_Resultat.CodiIdioma;
            c_Client.CodiPais = l_Resultat.CodiPais;
            c_Client.Contacte = l_Resultat.Contacte;
            c_Client.DataAlta = Funcions.StringDateADate(l_Resultat.DataAlta);
            c_Client.DataAltaTexte = Funcions.DateALocal(c_Client.DataAlta);
            c_Client.eMail = l_Resultat.eMail;
            c_Client.Nom = l_Resultat.Nom;
            Globals.g_Client = c_Client;
            // Gravem a la BBDD Local la info rebuda si el usuari no te informació perque la ha esborrat
            if (Globals.g_NoHiHanDades){
                InserirDades(c_Client);
                // Si hi han dades
                Globals.g_NoHiHanDades = false;
            }
        }
    }
    //
    // Funcio per informar (inserir/actualitzar) les dades en el servidor
    static public function InformarDadesServidor(P_Client:Client, P_Operativa:int, P_FuncioOK:Function = null, P_FuncioError:Function = null):Boolean{
        var PHP:FuncionsPHP = new FuncionsPHP();
        var l_Parametres:Object = new Object();

        // La comunicació de variables es amb vars
        // Validem que la xarxa estigui activa
        if (Globals.g_HiHaXarxa){
            l_Parametres.CodiClient = P_Client.CodiClient;
            l_Parametres.CodiClientIntern = P_Client.CodiClientIntern;
            l_Parametres.eMail = P_Client.eMail;
            l_Parametres.Nom = P_Client.Nom;
            l_Parametres.CodiPais = P_Client.CodiPais;
            l_Parametres.Contacte = P_Client.Contacte;
            l_Parametres.CodiIdioma = P_Client.CodiIdioma;
            l_Parametres.Operativa = P_Operativa;
            PHP.FerPHP_Vars("http://bodina.virtuol.com/php/Clients.php", l_Parametres, P_FuncioOK, P_FuncioError);
        }
        else{
            Funcions.Avis(ResourceManager.getInstance().getString('resources','php.Avis'),
                    ResourceManager.getInstance().getString('resources','php.NoServidor'));
        }
        return true;
    }
    */
}
