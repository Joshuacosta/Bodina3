package com.example.it00046.bodina3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.it00046.bodina3.Classes.Globals;


public class ac_principal extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_principal);

        // Definim contexte a nivel global
        Globals.g_Native = this.getApplicationContext();

        //Globals.g_DB_DAO = new SQLClientsDAO(this);
        //Globals.g_DB_DAO.open();
        Globals.CreateBBDD();

        // Si estem executant i no hem trobat dades (no existia la BBDD) obrim la finestra de
        // configuració perque l'usuari determini Pais, idioma (abans hem aplicat el del
        // telefon) i resta de informació personal (aquest informació es basica per poder
        // treballar amb la aplicacio)
        Globals.g_Clients_DAO.Llegir();
        if (Globals.g_NoHiHanDades == true) {
            intent = new Intent(this, ac_configuracio.class);
            startActivity(intent);
        }else{
            //Haurem de carregar la llista de entitats i celebracions del client;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mn_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.celebracions_Afegir:
                intent = new Intent(this, ac_celebracio_alta.class);
                startActivity(intent);
                return true;
            case R.id.celebracions_AfegirEntitat:
                intent = new Intent(this, ac_entitat_alta.class);
                startActivity(intent);
                return true;
            case R.id.celebracions_Configuracio:
                intent = new Intent(this, ac_configuracio.class);
                startActivity(intent);
                return true;
            case R.id.celebracions_Actualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}