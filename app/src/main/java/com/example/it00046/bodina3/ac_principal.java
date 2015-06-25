package com.example.it00046.bodina3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.it00046.bodina3.Classes.Custom.LVWEntitatsClientCelebracions;
import com.example.it00046.bodina3.Classes.DAO.DAOClients;
import com.example.it00046.bodina3.Classes.Globals;

import java.util.ArrayList;


public class ac_principal extends ActionBarActivity {

    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_principal);

        // Definim contexte a nivel global
        Globals.g_Native = this;
        Globals.CreateBBDD();
        // Si estem executant i no hem trobat dades (no existia la BBDD) obrim la finestra de
        // configuració perque l'usuari determini Pais, idioma (abans hem aplicat el del
        // telefon) i resta de informació personal (aquest informació es basica per poder
        // treballar amb la aplicacio) i cal que aquesta informació sigui guardada al
        // servidor, sino, no es pot treballar amb l'aplicació sense connexió, necessitem
        // CodiClient.
        //Globals.g_Clients_DAO.Llegir();
        DAOClients.Llegir();
        if (Globals.g_NoHiHanDades == true) {
            intent = new Intent(this, configuracio.class);
            startActivity(intent);
        }
        else{
            // Haurem de carregar desde la BBDD local la llista de celebracions del client
            ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.exLV_EntitatsClient);
            expandableList.setDividerHeight(2);
            // Aixó es el tema del indicador de si te elements
            expandableList.setGroupIndicator(null);
            expandableList.setClickable(true);

            setGroupParents();
            setChildData();

            LVWEntitatsClientCelebracions adapter = new LVWEntitatsClientCelebracions(parentItems, childItems);

            adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
            expandableList.setAdapter(adapter);
            //expandableList.setOnChildClickListener(this);
        }
    }

    public void setGroupParents() {
        parentItems.add("Android");
        parentItems.add("Core Java");
        parentItems.add("Desktop Java");
        parentItems.add("Enterprise Java");
    }

    public void setChildData() {

        // Android
        ArrayList<String> child = new ArrayList<String>();
        child.add("Core");
        child.add("Games");
        childItems.add(child);

        // Core Java
        child = new ArrayList<String>();
        child.add("Apache");
        child.add("Applet");
        child.add("AspectJ");
        child.add("Beans");
        child.add("Crypto");
        childItems.add(child);

        // Desktop Java
        child = new ArrayList<String>();
        child.add("Accessibility");
        child.add("AWT");
        child.add("ImageIO");
        child.add("Print");
        childItems.add(child);

        // Enterprise Java
        child = new ArrayList<String>();
        child.add("EJB3");
        child.add("GWT");
        child.add("Hibernate");
        child.add("JSP");
        childItems.add(child);
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
        int id = item.getItemId();

        switch (id) {
            case R.id.celebracions_Entitats:
                intent = new Intent(this, ac_entitat_pral.class);
                startActivity(intent);
                break;
            case R.id.celebracions_Afegir:
                intent = new Intent(this, ac_celebracio_alta.class);
                startActivity(intent);
                return true;
                /*
                    Experiment pasar dades a una activitat

                PAREntitat details = new PAREntitat();
                details.Adresa = "Adresa";
                details.eMail = "eMail";
                details.Nom = "Nom";
                details.Telefon = "Telefon";
                details.Contacte = "Contacte";

                Intent l_intent = new Intent(this, ac_entitat_detall.class);
                l_intent.putExtra("Info", details);
                startActivity(l_intent);

                */
            case R.id.celebracions_Configuracio:
                intent = new Intent(this, configuracio.class);
                startActivity(intent);
                return true;
            case R.id.celebracions_Actualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}