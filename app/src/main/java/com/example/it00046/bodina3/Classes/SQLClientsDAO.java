package com.example.it00046.bodina3.Classes;

/**
 * Created by it00046 on 13/02/2015.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.it00046.bodina3.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class SQLClientsDAO {

    //
    // O P E R A T I V A    S E R V I D O R
    //
    // Variables
    private static List<NameValuePair> g_parametresPHP = new ArrayList<NameValuePair>();
    //
    // Runnable's
    // Graba el client a base de dades local
    public static class R_InserirLocal implements Runnable  {
        private Client data;
        public R_InserirLocal(Client _data) {
            this.data = _data;
        }
        public void run() {
            F_InserirLocal(data);
        }
    }
    public static class MyRunnableOK implements Runnable  {
        private Client data;
        public MyRunnableOK(Client _data) {
            this.data = _data;
        }

        public void run() {
            UpdateClient(data);
        }
    }
    public static class MyRunnableKO implements Runnable  {
        private Client data;
        public MyRunnableKO(Client _data) {
            this.data = _data;
        }

        public void run() {
            UpdateClient(data);
        }
        public void error(){

        }
    }
    // Llegim el client del servidor
    static public Boolean LlegirServidor(Client p_client){

        // Validem que la xarxa estigui activa
        if (Globals.isNetworkAvailable()){
            // Montem el php
            g_parametresPHP = new ArrayList<NameValuePair>(2);
            g_parametresPHP.add(new BasicNameValuePair("CodiClientIntern", p_client.CodiClientIntern));
            g_parametresPHP.add(new BasicNameValuePair("Operativa", Globals.k_OPE_SelectCodiClientIntern));
            PHP l_operacio = new PHP(g_parametresPHP,
                                     new R_InserirLocal(p_client),
                                     null);
            l_operacio.execute("http://bodina.virtuol.com/php/Clients.php");
        }
        else{
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_noAcces),
                            Globals.g_Native.getString(R.string.errorservidor_avis));
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

    public static Boolean F_InserirLocal(Client p_client){
        ContentValues l_values = new ContentValues();
        long l_resultat;

        p_client.Actualitzat = true;

        l_values.put(Globals.g_Native.getString(R.string.TClient_CodiClient), p_client.CodiClient);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Contacte), p_client.Contacte);
        l_values.put(Globals.g_Native.getString(R.string.TClient_DataAlta), p_client.DataAlta);
        l_values.put(Globals.g_Native.getString(R.string.TClient_eMail), p_client.eMail);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Idioma), p_client.Idioma);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Nom), p_client.Nom);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Pais), p_client.Pais);
        l_values.put(Globals.g_Native.getString(R.string.TClient_Actualitzat), p_client.Actualitzat);
        l_resultat = Globals.g_DB.insert(Globals.g_Native.getString(R.string.TClient), null, l_values);
        // Informem si la operativa ha anat correctament
        return (l_resultat != -1);
    }

    public static void F_InserirGlobal(Client p_client) {
        Boolean l_grabarlocal = true;

        if (Globals.isNetworkAvailable()) {
            // Montem el php
            g_parametresPHP = new ArrayList<NameValuePair>(8);
            g_parametresPHP.add(new BasicNameValuePair("CodiClient", p_client.CodiClient));
            g_parametresPHP.add(new BasicNameValuePair("CodiClientIntern", p_client.CodiClientIntern));
            g_parametresPHP.add(new BasicNameValuePair("eMail", p_client.eMail));
            g_parametresPHP.add(new BasicNameValuePair("Nom", p_client.Nom));
            g_parametresPHP.add(new BasicNameValuePair("Pais", p_client.Pais));
            g_parametresPHP.add(new BasicNameValuePair("Contacte", p_client.Contacte));
            g_parametresPHP.add(new BasicNameValuePair("Idioma", p_client.Idioma));
            g_parametresPHP.add(new BasicNameValuePair("Operativa", Globals.k_OPE_Alta));

            PHP operacio = new PHP(g_parametresPHP,
                                    new R_InserirLocal(p_client),
                                    null);
            operacio.execute("http://bodina.virtuol.com/php/Clients.php");
        }
        if (l_grabarlocal){
            F_InserirLocal(p_client);
        }
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

    //
    // O P E R A T I V A    L O C A L
    //
    public static void deleteClient(Client client){
        String id = client.CodiClient;
        Globals.g_DB.delete(Globals.g_Native.getString(R.string.TClient), Globals.g_Native.getString(R.string.TClient_CodiClient) + " = " + id, null);
    }

    public static List<Client> getAllClients(){
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
    //
    // Funcio per llegir el client, nomès tenim un.
    public static Client Llegir(){
        Client l_client = new Client();

        // Aquest valor l'informem ja (CodiInternClient es la MAC)
        l_client.CodiClientIntern = Globals.F_RecuperaMAC();
        // Recerquem localment
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
            l_client = cursorToClient(cursor);
            // Si que hi han dades
            Globals.g_NoHiHanDades = false;
        }
        else {
            // Recerquem al servidor per si lo que ha passat es que l'usuari ha esborrat
            // les dades locals


            Globals.g_NoHiHanDades = true;

        }
        //
        return l_client;
    }

    // Funcio per updatar la informació del client
    public static void UpdateClient(Client p_client){
        if (Globals.isNetworkAvailable()){
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("usuari", "Joshua"));
            nameValuePairs.add(new BasicNameValuePair("email", "email"));
            PHP operacio = new PHP(nameValuePairs,
                                   new MyRunnableOK(p_client),
                                   new MyRunnableKO(p_client));
            operacio.execute("http://bodina.virtuol.com/prova.php");
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
    private static Client cursorToClient(Cursor cursor){
        Client l_client = new Client();

        l_client.CodiClient = cursor.getString(0);
        l_client.eMail = cursor.getString(1);
        l_client.Nom = cursor.getString(2);
        l_client.Pais = cursor.getString(3);
        l_client.Contacte = cursor.getString(4);
        l_client.Idioma = cursor.getString(5);
        l_client.DataAlta = cursor.getString(6);
        l_client.DataGrabacioServidor = cursor.getString(7);
        l_client.Actualitzat = (cursor.getInt(8) != 0);
        l_client.DataActualitzat = cursor.getString(9);

        return l_client;
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
