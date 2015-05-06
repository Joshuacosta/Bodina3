package com.example.it00046.bodina3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class ac_entitat_solicitar extends ActionBarActivity {

    private Spinner lSPN_EntitatsClient;
    private TextView lTextEntitat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_solicitar);
        // Recuperem controls del layout

        //  Literals
        lTextEntitat = (TextView) findViewById(R.id.litEntitatSolicitar_Entitat);
        // Spinners
        lSPN_EntitatsClient = (Spinner)findViewById(R.id.spinnerEntitatSolicitar_Entitat);
        // Codi per tractar el spinner de les entitats del client
        ArrayAdapter<CharSequence> adapter_EntitatsClient = ArrayAdapter.createFromResource(this,R.array.Idioma,android.R.layout.simple_spinner_item);
        adapter_EntitatsClient.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lSPN_EntitatsClient.setAdapter(adapter_EntitatsClient);
        // Codi del Spinner de entitats
        lSPN_EntitatsClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                lTextEntitat.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });

    }

    public void btnAfegirEntitatOnClick(View view) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mn_entitat_solicitar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
