package com.example.it00046.bodina3;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOContactes;
import com.example.it00046.bodina3.Classes.Entitats.Taula;
import com.example.it00046.bodina3.Classes.Globals;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class distribucions_client_taula_convidats extends ActionBarActivity {
    public Taula g_Taula;
    public ListView g_LVW_Convidats, g_LVW_Contactes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DrawerLayout l_DRL_ConvidatsContactes;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribucions_client_taula_convidats);

        // MEU CODI
        // Recuperem taula
        g_Taula = Globals.TaulaTreball();

        // Menu lateral dret: carreguem la llista de contactes
        l_DRL_ConvidatsContactes = (DrawerLayout) findViewById(R.id.DistribucionsClientTaulaComenslasNDRContactes);
        g_LVW_Contactes = (ListView) findViewById(R.id.DistribucionsClientTaulaComenslasNDLContactes);
        DAOContactes.Llegir(g_LVW_Contactes, R.layout.linia_lvw_llista_taules_contactes_seleccio, this);
        // Listener damunt la llista de contactes i obrim
        g_LVW_Contactes.setOnItemClickListener(new DrawerItemClickListener());
        l_DRL_ConvidatsContactes.openDrawer(Gravity.RIGHT);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.distribucions_client_mant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id){
            case R.id.DistribucionsClientMantMNUAcceptar:
                return true;
            case R.id.DistribucionsClientMantMNUEsborrar:
                // Hauriem de preguntar si ho fem o no!!!!!!!!!!!!!!!!!!!!!
                //
                // MANCA ESBORRAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // I QUE ESBORREM
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }

}
