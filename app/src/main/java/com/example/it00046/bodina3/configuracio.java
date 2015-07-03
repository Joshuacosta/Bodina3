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
    private EditText g_ETX_Name, g_ETX_eMail, g_ETX_Contacte;
    private TextView g_TXT_Idioma, g_TXT_Pais, g_TXT_DataAlta;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        int l_spinnerPosition;
        DateFormat l_df;
        Date l_date = new Date();

        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.configuracio);
        // Anem recuperant els controls:
        g_ETX_Name = (EditText) findViewById(R.id.configuracioETXnom);
        g_ETX_Contacte = (EditText) findViewById(R.id.configuracioETXContacte);
        g_ETX_eMail = (EditText) findViewById(R.id.configuracioETXeMail);
        g_TXT_Idioma = (TextView) findViewById(R.id.configuracioTXTidioma);
        g_TXT_Pais = (TextView) findViewById(R.id.configuracioTXTpais);
        g_TXT_DataAlta = (TextView) findViewById(R.id.configuracioTXTdataAlta);
        g_SPN_Idioma = (Spinner)findViewById(R.id.configuracioSPNidioma);
        g_SPN_Paissos = (Spinner)findViewById(R.id.configuracioSPNpais);
        // Codi per tractar el spinner del idioma
        ArrayAdapter<CharSequence> adapter_Idioma = ArrayAdapter.createFromResource(this,R.array.Idioma,android.R.layout.simple_spinner_item);
        adapter_Idioma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Codi del Spinner de idioma
        g_SPN_Idioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                g_TXT_Idioma.setError(null);
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
                g_TXT_Pais.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Informem les dades si es necessari
        if (!Globals.g_Client.Codi.equals("")){
            // Mostrem dades
            g_ETX_Name.setText(Globals.g_Client.Nom);
            g_ETX_eMail.setText(Globals.g_Client.eMail);
            g_ETX_Contacte.setText(Globals.g_Client.Contacte);
            if (!Globals.g_Client.Idioma.isEmpty()){
                l_spinnerPosition = adapter_Idioma.getPosition(Globals.g_Client.Idioma);
                g_SPN_Idioma.setSelection(l_spinnerPosition);
            }
            if (!Globals.g_Client.Pais.isEmpty()){
                l_spinnerPosition = adapter_Pais.getPosition(Globals.g_Client.Pais);
                g_SPN_Paissos.setSelection(l_spinnerPosition);
            }
            g_TXT_DataAlta.setText(Globals.g_Client.DataAlta);
        }
        else{
            // Mostrem com a data de alta la del dia
            l_df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            g_TXT_DataAlta.setText(l_df.format(l_date));
        }
        // Codi de validacio de la finestra (fem servir la clase estàtica Validació)
        g_ETX_Name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){ g_ETX_Name.setError(null);}
        });
        g_ETX_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){ g_ETX_Contacte.setError(null);}
        });
        g_ETX_eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { g_ETX_eMail.setError(null);}
        });
    }
    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean l_ret = true;

        if (!Validacio.hasText(g_ETX_Name)) l_ret = false;
        if (!Validacio.hasText(g_ETX_Contacte)) l_ret = false;
        if (!Validacio.isEmailAddress(g_ETX_eMail, true)) l_ret = false;
        if (g_SPN_Idioma.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))){
            g_TXT_Idioma.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            l_ret = false;
        }
        if (g_SPN_Paissos.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            g_TXT_Pais.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            l_ret = false;
        }
        //
        return l_ret;
    }

    private void FerOperativa(){
        Client l_client = new Client();

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_client.CodiIntern = Globals.g_Client.CodiIntern;
            l_client.Codi = Globals.g_Client.Codi;
            l_client.Nom = g_ETX_Name.getText().toString();
            l_client.eMail = g_ETX_eMail.getText().toString();
            l_client.Contacte = g_ETX_Contacte.getText().toString();
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

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuracio, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int l_id = p_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (l_id == R.id.configuracioMNUacceptar) {
            FerOperativa();
            return true;
        }

        return super.onOptionsItemSelected(p_item);
    }
}