package com.example.it00046.bodina3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Client;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SQLClientsDAO;
import com.example.it00046.bodina3.Classes.Validacio;


public class ac_configuracio extends ActionBarActivity {

    private Spinner lSPN_Idioma, lSPN_Paissos;
    private EditText lTXT_Name, lTXT_eMail, lTXT_Contacte, lTXT_Prova;
    private TextView lTextIdioma, lTextPais;

    private SQLClientsDAO sqlclientsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int spinnerPostion;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_configuracio);

        lTXT_Prova = (EditText) findViewById(R.id.TextProva);
        //
        lTextIdioma = (TextView) findViewById(R.id.litIdioma);
        lTextPais = (TextView) findViewById(R.id.litPais);
        // Codi per tractar el spinner del idioma
        lSPN_Idioma = (Spinner)findViewById(R.id.spinnerIdioma);
        ArrayAdapter<CharSequence> adapter_Idioma = ArrayAdapter.createFromResource(this,R.array.Idioma,android.R.layout.simple_spinner_item);
        adapter_Idioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lSPN_Idioma.setAdapter(adapter_Idioma);
        lSPN_Idioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                lTextIdioma.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });
        // Codi per tractar el spinner de paissos
        lSPN_Paissos = (Spinner)findViewById(R.id.spinnerPais);
        ArrayAdapter<CharSequence> adapter_Pais = ArrayAdapter.createFromResource(this,R.array.Paisos,android.R.layout.simple_spinner_item);
        adapter_Pais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lSPN_Paissos.setAdapter(adapter_Pais);
        lSPN_Paissos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                lTextPais.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });
        // Informem les dades si es necessari
        if (Globals.g_Client.CodiClient != ""){
            // Mostrem dades
            lTXT_Name = (EditText) findViewById(R.id.TextName);
            lTXT_Name.setText(Globals.g_Client.Nom);
            lTXT_eMail = (EditText) findViewById(R.id.TexteMail);
            lTXT_eMail.setText(Globals.g_Client.eMail);
            lTXT_Contacte = (EditText) findViewById(R.id.TexteContacte);
            lTXT_Contacte.setText(Globals.g_Client.Contacte);
            if (!Globals.g_Client.Idioma.isEmpty()){
                spinnerPostion = adapter_Idioma.getPosition(Globals.g_Client.Idioma);
                lSPN_Idioma.setSelection(spinnerPostion);
            }
            if (!Globals.g_Client.Pais.isEmpty()){
                spinnerPostion = adapter_Pais.getPosition(Globals.g_Client.Pais);
                lSPN_Paissos.setSelection(spinnerPostion);
            }
        }
        // Validacions de la finestra
        lTXT_Name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validacio.hasText(lTXT_Name);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        lTXT_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validacio.hasText(lTXT_Contacte);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        lTXT_eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validacio.isEmailAddress(lTXT_eMail, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    private boolean checkValidation() {
        boolean ret = true;
        TextView lTextIdioma = (TextView) findViewById(R.id.litIdioma);
        TextView lTextPais = (TextView) findViewById(R.id.litPais);

        if (!Validacio.hasText(lTXT_Name)) ret = false;
        if (!Validacio.hasText(lTXT_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(lTXT_eMail, true)) ret = false;
        if (lSPN_Idioma.getSelectedItem().toString() == Globals.g_Native.getString(R.string.llista_Select)){
            lTextIdioma.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        if (lSPN_Paissos.getSelectedItem().toString() == Globals.g_Native.getString(R.string.llista_Select)){
            lTextPais.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }

        return ret;

    }

    public void btnAcceptarOnClick(View view){
        Client client = new Client();

        // Validem que els camps estiguin informats
        if (checkValidation()) {

            client.Nom = lTXT_Name.getText().toString();
            client.eMail = lTXT_eMail.getText().toString();
            client.Contacte = lTXT_Contacte.getText().toString();
            client.Pais = lSPN_Paissos.getSelectedItem().toString();
            client.Idioma = lSPN_Idioma.getSelectedItem().toString();

            Globals.g_DB_DAO.createClient(client);
            SQLClientsDAO.createClient(client);
            // Gravem les dades del client i tornem enrera
            Globals.g_Client = client;
            this.finish();
        }
        else{
            Toast.makeText(ac_configuracio.this,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void btnEsborrarOnClick(View view) {
        lTXT_Prova.setError(null);
        lTXT_Prova.setText(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mn_configuracio, menu);
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
