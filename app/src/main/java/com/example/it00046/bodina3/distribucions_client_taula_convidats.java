package com.example.it00046.bodina3;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOContactes;
import com.example.it00046.bodina3.Classes.Entitats.Taula;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.TaulaConvidatsEdicio;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class distribucions_client_taula_convidats extends ActionBarActivity {
    public Taula g_Taula;
    public ListView g_LVW_Convidats, g_LVW_Contactes;
    private LinearLayout g_LVW_OperativaPersona;
    public TaulaConvidatsEdicio g_Draw;
    Context Jo = this;
    static final int g_RQC_DISTRIBUCIONS_CLIENT_TAULA_COMENSALS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DrawerLayout l_DRL_ConvidatsContactes;
        ImageButton l_IMB_Ajuda, l_IMB_EsborrarPersona, l_IMB_Comensals, l_IMB_GirarTaula;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribucions_client_taula_convidats);
        // Recuperem taula on hem de actuar
        g_Taula = Globals.TaulaTreball();
        // Recuperem controls
        g_Draw = (TaulaConvidatsEdicio) findViewById(R.id.DistribucionsClientMantTaulaConvidatsVIWDrawing);
        // Menu lateral dret: carreguem la llista de contactes
        l_DRL_ConvidatsContactes = (DrawerLayout) findViewById(R.id.DistribucionsClientTaulaConvidatsNDRContactes);
        g_LVW_Contactes = (ListView) findViewById(R.id.DistribucionsClientMantTaulaConvidatsNDLContactes);
        DAOContactes.Llegir(g_LVW_Contactes, R.layout.linia_lvw_llista_taulacomensals_contactes_seleccio, this);
        // Listener damunt la llista de contactes i obrim
        g_LVW_Contactes.setOnItemClickListener(new DrawerItemClickListener());
        l_DRL_ConvidatsContactes.openDrawer(Gravity.RIGHT);
        // Menu lateral esquerra: menu persona
        g_LVW_OperativaPersona = (LinearLayout) findViewById(R.id.DistribucionsClientMantTaulaConvidatsNDLOperativaPersona);
        // Codi dels botons del menu de persona (el de la esquerra)
        l_IMB_EsborrarPersona = (ImageButton) findViewById(R.id.DistribucionsClientMantTaulaConvidatsIMBEsborrarPersona);
        l_IMB_EsborrarPersona.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        /*
        l_IMB_Comensals = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBComensals);
        l_IMB_Comensals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Obrim la finestra de la taula
                Intent l_Intent;

                l_Intent = new Intent(Jo, distribucions_client_taula_convidats.class);
                startActivityForResult(l_Intent, g_RQC_DISTRIBUCIONS_CLIENT_TAULA_COMENSALS);
            }
        });
        l_IMB_GirarTaula = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBGirarTaula);
        l_IMB_GirarTaula.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        */
        // Control de enrera/cancelacio
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
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
