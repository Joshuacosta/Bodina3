package com.example.it00046.bodina3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.CanvasManual;

/**
 * Created by it00046 on 05/10/2015.
 */
public class salons_client_planol  extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView l_TXT_Distancia;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salons_client_planol);
        //
        l_TXT_Distancia = (TextView) findViewById(R.id.SalonsClientPlanolTXTDistancia);
        CanvasManual.DefinimMetres(l_TXT_Distancia);
        // Control de enrera/cancelacio
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salons_client_planol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        return super.onOptionsItemSelected(p_Item);
    }

}
