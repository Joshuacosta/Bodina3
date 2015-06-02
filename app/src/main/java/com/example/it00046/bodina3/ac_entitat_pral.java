package com.example.it00046.bodina3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ac_entitat_pral extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_pral);
        // Carreguen les entitats del client

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
                intent = new Intent(this, ac_entitat_solicitar.class);
                startActivity(intent);
                return true;
            case R.id.entitat_Actualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
