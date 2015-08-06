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


public class principal extends ActionBarActivity {

    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private Context jo = this;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        Intent l_intent;

        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.principal);

        // Definim contexte a nivel global
        Globals.g_Native = jo;
        Globals.CreateBBDD();
        // Si estem executant i no hem trobat dades (no existia la BBDD) obrim la finestra de
        // configuració perque l'usuari determini Pais, idioma (abans hem aplicat el del
        // telefon) i resta de informació personal (aquest informació es basica per poder
        // treballar amb la aplicacio) i cal que aquesta informació sigui guardada al
        // servidor, sino, no es pot treballar amb l'aplicació sense connexió, necessitem
        // CodiClient.
        //Globals.g_Clients_DAO.Llegir();
        DAOClients.Llegir(jo);
        if (Globals.g_NoHiHanDades == true) {
            l_intent = new Intent(this, configuracio.class);
            startActivity(l_intent);
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
    public boolean onCreateOptionsMenu(Menu p_menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_item) {
        Intent l_intent;
        int l_id = p_item.getItemId();

        switch (l_id) {
            case R.id.principalMNUentitats:
                l_intent = new Intent(this, entitat_pral.class);
                startActivity(l_intent);
                break;
            case R.id.principalMNUafegir:
                l_intent = new Intent(this, celebracio_alta.class);
                startActivity(l_intent);
                return true;
            case R.id.principalMNUconfiguracio:
                l_intent = new Intent(this, configuracio.class);
                startActivity(l_intent);
                return true;
            case R.id.principalMNUTipusCelebracions:
                l_intent = new Intent(this, tipus_celebracions.class);
                startActivity(l_intent);
                return true;
            case R.id.principalMNUactualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(p_item);
    }
}