package com.example.it00046.bodina3;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOEntitatsClient;
import com.melnykov.fab.FloatingActionButton;


public class ac_entitat_pral extends ActionBarActivity {
    private ListView l_EntitatsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_pral);
        // Carreguen les entitats del client
        l_EntitatsClient = (ListView) findViewById(R.id.LV_Entitats);
        //SQLEntitatsClientDAO.F_LOCAL_Llegir(l_EntitatsClient);
        DAOEntitatsClient.Llegir(l_EntitatsClient, R.layout.ly_entitat_listview_linia);
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(l_EntitatsClient);
        //fab.show(true);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        fab.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animAlpha);
            }
        });
        //
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mn_entitat_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.entitat_SolicitarEntitat:
                intent = new Intent(this, entitat_solicitar.class);
                startActivity(intent);
                return true;
            case R.id.entitat_Actualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
