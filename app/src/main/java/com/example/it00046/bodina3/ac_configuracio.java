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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int spinnerPostion;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_configuracio);
        // Anem recuperant els controls:
        //  Entrada de dades
        lTXT_Name = (EditText) findViewById(R.id.TextName);
        lTXT_Contacte = (EditText) findViewById(R.id.TexteContacte);
        lTXT_eMail = (EditText) findViewById(R.id.TexteMail);
        lTXT_Prova = (EditText) findViewById(R.id.TextProva);
        //  Literals
        lTextIdioma = (TextView) findViewById(R.id.litIdioma);
        lTextPais = (TextView) findViewById(R.id.litPais);
        // Spinners
        lSPN_Idioma = (Spinner)findViewById(R.id.spinnerIdioma);
        lSPN_Paissos = (Spinner)findViewById(R.id.spinnerPais);
        // Codi per tractar el spinner del idioma
        ArrayAdapter<CharSequence> adapter_Idioma = ArrayAdapter.createFromResource(this,R.array.Idioma,android.R.layout.simple_spinner_item);
        adapter_Idioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lSPN_Idioma.setAdapter(adapter_Idioma);
        // Codi del Spinner de idioma
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
        ArrayAdapter<CharSequence> adapter_Pais = ArrayAdapter.createFromResource(this,R.array.Paisos,android.R.layout.simple_spinner_item);
        adapter_Pais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lSPN_Paissos.setAdapter(adapter_Pais);
        // Codi del Spinner de pais
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
        if (!Globals.g_Client.CodiClient.equals("")){
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
        // Codi de validacio de la finestra (fem servir la clase estàtica Validació)
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

    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;
        TextView lTextIdioma = (TextView) findViewById(R.id.litIdioma);
        TextView lTextPais = (TextView) findViewById(R.id.litPais);

        if (!Validacio.hasText(lTXT_Name)) ret = false;
        if (!Validacio.hasText(lTXT_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(lTXT_eMail, true)) ret = false;
        if (lSPN_Idioma.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))){
            lTextIdioma.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        if (lSPN_Paissos.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))){
            lTextPais.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }

        return ret;

    }

    public void btnAcceptarOnClick(View view){
        Client l_client = new Client();

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_client.CodiClientIntern = Globals.g_Client.CodiClientIntern;
            l_client.Nom = lTXT_Name.getText().toString();
            l_client.eMail = lTXT_eMail.getText().toString();
            l_client.Contacte = lTXT_Contacte.getText().toString();
            l_client.Pais = lSPN_Paissos.getSelectedItem().toString();
            l_client.Idioma = lSPN_Idioma.getSelectedItem().toString();
            //
            if (Globals.g_NoHiHanDades) {
                SQLClientsDAO.Definir(l_client);
                Globals.g_NoHiHanDades = false;
            }
            else{
                SQLClientsDAO.Modificar(l_client);
            }
            // Gravem les dades del client i tornem enrera
            Globals.g_Client = l_client;
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
