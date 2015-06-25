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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.Entitats.Client;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.DAO.DAOClients;
import com.example.it00046.bodina3.Classes.Validacio;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class configuracio extends ActionBarActivity {

    private Spinner g_SPN_Idioma, g_SPN_Paissos;
    private EditText g_TXT_Name, g_TXT_eMail, g_TXT_Contacte, g_TXT_Prova;
    private TextView g_TextIdioma, g_TextPais, g_textDataAlta;
    private Button g_BotoEsborrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int spinnerPostion;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_configuracio);
        // Anem recuperant els controls:
        //  Entrada de dades
        g_TXT_Name = (EditText) findViewById(R.id.TextName);
        g_TXT_Contacte = (EditText) findViewById(R.id.TexteContacte);
        g_TXT_eMail = (EditText) findViewById(R.id.TexteMail);
        g_TXT_Prova = (EditText) findViewById(R.id.TextProva);
        //  Literals
        g_TextIdioma = (TextView) findViewById(R.id.litIdioma);
        g_TextPais = (TextView) findViewById(R.id.litPais);
        // Resta camps
        g_textDataAlta = (TextView) findViewById(R.id.textDataAlta);
        g_BotoEsborrar = (Button) findViewById(R.id.AspaProva);
        g_BotoEsborrar.setVisibility(View.INVISIBLE);
        // Spinners
        g_SPN_Idioma = (Spinner)findViewById(R.id.spinnerIdioma);
        g_SPN_Paissos = (Spinner)findViewById(R.id.spinnerPais);
        // Codi per tractar el spinner del idioma
        ArrayAdapter<CharSequence> adapter_Idioma = ArrayAdapter.createFromResource(this,R.array.Idioma,android.R.layout.simple_spinner_item);
        adapter_Idioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Codi del Spinner de idioma
        g_SPN_Idioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                g_TextIdioma.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Codi per tractar el spinner de paissos
        ArrayAdapter<CharSequence> adapter_Pais = ArrayAdapter.createFromResource(this,R.array.Paisos,android.R.layout.simple_spinner_item);
        adapter_Pais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        g_SPN_Paissos.setAdapter(adapter_Pais);
        // Codi del Spinner de pais
        g_SPN_Paissos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                g_TextPais.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Informem les dades si es necessari
        if (!Globals.g_Client.CodiClient.equals("")){
            // Mostrem dades
            g_TXT_Name.setText(Globals.g_Client.Nom);
            g_TXT_eMail.setText(Globals.g_Client.eMail);
            g_TXT_Contacte.setText(Globals.g_Client.Contacte);
            if (!Globals.g_Client.Idioma.isEmpty()){
                spinnerPostion = adapter_Idioma.getPosition(Globals.g_Client.Idioma);
                g_SPN_Idioma.setSelection(spinnerPostion);
            }
            if (!Globals.g_Client.Pais.isEmpty()){
                spinnerPostion = adapter_Pais.getPosition(Globals.g_Client.Pais);
                g_SPN_Paissos.setSelection(spinnerPostion);
            }
            g_textDataAlta.setText(Globals.g_Client.DataAlta);
        }
        else{
            // Mostrem com a data de alta la del dia
            DateFormat l_df;
            Date l_date = new Date();
            l_df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            g_textDataAlta.setText(l_df.format(l_date));
        }
        // Codi de validacio de la finestra (fem servir la clase estàtica Validació)
        g_TXT_Name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){ g_TXT_Name.setError(null);}
        });
        g_TXT_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){ g_TXT_Contacte.setError(null);}
        });
        g_TXT_eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { g_TXT_eMail.setError(null);}
        });
        /*
            De moment no ho fem servir

        // Codi de control del camp de texte (la visibilitat del aspa). Aixó anirà dintre
        // del component que has de crear !!!!
        g_SPN_Idioma.setAdapter(adapter_Idioma);
        g_TXT_Prova.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    g_BotoEsborrar.setVisibility(View.VISIBLE);
                } else {
                    g_BotoEsborrar.setVisibility(View.INVISIBLE);
                }
            }
        });
        */
    }
    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;
        TextView g_TextIdioma = (TextView) findViewById(R.id.litIdioma);
        TextView g_TextPais = (TextView) findViewById(R.id.litPais);

        if (!Validacio.hasText(g_TXT_Name)) ret = false;
        if (!Validacio.hasText(g_TXT_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(g_TXT_eMail, true)) ret = false;
        if (g_SPN_Idioma.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))){
            g_TextIdioma.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        if (g_SPN_Paissos.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            g_TextPais.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }

        return ret;
    }

    //public void btnAcceptarOnClick(View view){
    public void btnAcceptarOnClick(){
        Client l_client = new Client();

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_client.CodiClientIntern = Globals.g_Client.CodiClientIntern;
            l_client.CodiClient = Globals.g_Client.CodiClient;
            l_client.Nom = g_TXT_Name.getText().toString();
            l_client.eMail = g_TXT_eMail.getText().toString();
            l_client.Contacte = g_TXT_Contacte.getText().toString();
            l_client.Pais = g_SPN_Paissos.getSelectedItem().toString();
            l_client.Idioma = g_SPN_Idioma.getSelectedItem().toString();
            //
            if (Globals.g_NoHiHanDades) {
                DAOClients.Definir(l_client);
                Globals.g_NoHiHanDades = false;
            }
            else{
                DAOClients.Modificar(l_client);
            }
            // Gravem les dades del client i tornem enrera
            Globals.g_Client = l_client;
            this.finish();
        }
        else{
            Toast.makeText(configuracio.this,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void btnEsborrarOnClick(View view) {
        g_TXT_Prova.setError(null);
        g_TXT_Prova.setText(null);
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
            btnAcceptarOnClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
