package com.example.it00046.bodina3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOEntitats;
import com.example.it00046.bodina3.Classes.Entitats.Entitat;
import com.example.it00046.bodina3.Classes.Entitats.EntitatClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.DAO.DAOEntitatsClient;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNEntitat;
import com.example.it00046.bodina3.Classes.Validacio;
import com.example.it00046.bodina3.Classes.Params.PAREntitat;


public class entitat_solicitar extends ActionBarActivity {

    private Spinner g_SPN_EntitatsClient;
    private EditText g_ETX_Descripcio, g_ETX_Contacte, g_ETX_eMail;
    private TextView g_TXT_Entitat;
    static final int g_RQC_ENTITAT_RECERCA = 1;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.ly_entitat_solicitar);
        // Recuperem controls del layout
        g_ETX_Descripcio = (EditText) findViewById(R.id.TexteEntitatSolicitar_Descripcio);
        g_ETX_Contacte = (EditText) findViewById(R.id.TexteEntitatSolicitar_Contacte);
        g_ETX_eMail = (EditText) findViewById(R.id.TexteEntitatSolicitar_eMail);
        g_TXT_Entitat = (TextView) findViewById(R.id.litEntitatSolicitar_Entitat);
        g_SPN_EntitatsClient = (Spinner)findViewById(R.id.spinnerEntitatSolicitar_Entitat);
        // Llegim en el SERVIDOR les entitats del pais del client
        DAOEntitats.Llegir(Globals.g_Client.Pais, g_SPN_EntitatsClient);
        // Codi del Spinner de entitats del client
        g_SPN_EntitatsClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                SPNEntitat l_SPNEntitat;
                Entitat l_dadesEntitat = new Entitat();
                // Esborrem possible error
                g_TXT_Entitat.setError(null);
                // Si es una entitat de les "nostres" (amb la que hem treballat) recuperem
                // el e-mail i el contacte que vem donar i els posem en els camps
                l_SPNEntitat = (SPNEntitat) g_SPN_EntitatsClient.getSelectedItem();
                if (l_SPNEntitat.EsNova() == false) {
                    l_dadesEntitat = l_SPNEntitat.getId();
                    g_ETX_eMail.setText(l_dadesEntitat.eMail);
                    g_ETX_Contacte.setText(l_dadesEntitat.Contacte);
                }
                else{
                    g_ETX_eMail.setText("");
                    g_ETX_Contacte.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Codi de validacio dels camps de la finestra (fem servir la clase estatica Validacio)
        g_ETX_Descripcio.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {g_ETX_Descripcio.setError(null);}
        });
        g_ETX_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {g_ETX_Contacte.setError(null);}
        });
        g_ETX_eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {g_ETX_eMail.setError(null);}
        });
        // Control de enrera
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_esborrar);
    }
    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        if (!Validacio.hasText(g_ETX_Descripcio)) ret = false;
        if (!Validacio.hasText(g_ETX_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(g_ETX_eMail, true)) ret = false;
        if (g_SPN_EntitatsClient.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            g_TXT_Entitat.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        return ret;
    }
    // Codi de acceptació
    public void btnEntitatSolicitar_Acceptar(View view) {
        EntitatClient l_entitatClient = new EntitatClient();
        Entitat l_dadesEntitat = new Entitat();
        PAREntitat l_entitat = new PAREntitat();
        SPNEntitat l_SPNEntitat;

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_entitatClient.DescripcioAssociacio = g_ETX_Descripcio.getText().toString();
            l_entitatClient.ContacteAssociacio = g_ETX_Contacte.getText().toString();
            l_entitatClient.eMailAssociacio = g_ETX_eMail.getText().toString();
            l_SPNEntitat = (SPNEntitat) g_SPN_EntitatsClient.getSelectedItem();
            l_dadesEntitat = l_SPNEntitat.getId();

            l_entitatClient.CodiEntitat = l_dadesEntitat.Codi;
            l_entitatClient.NomEntitat = l_dadesEntitat.Nom;
            l_entitatClient.eMailEntitat = l_dadesEntitat.eMail;
            l_entitatClient.ContacteEntitat = l_dadesEntitat.Contacte;
            l_entitatClient.AdresaEntitat = l_dadesEntitat.Adresa;
            l_entitatClient.TelefonEntitat = l_dadesEntitat.Telefon;
            l_entitatClient.PaisEntitat = l_dadesEntitat.Pais;
            // Ho poso a 1 o llegeixo la info recuperada?...
            l_entitatClient.EstatEntitat = 1;
            //
            DAOEntitatsClient.Solicitar(l_entitatClient);
            this.finish();
        }
        else{
            Toast.makeText(entitat_solicitar.this,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }
    }
    //
    // Funcio per obrir la finestra de recerca de entitats
    public void btnAfegirEntitatOnClick(View view){
        Intent intent = new Intent(this, ac_entitat_recerca.class);
        startActivityForResult(intent, g_RQC_ENTITAT_RECERCA);
    }
    // Resposta de la recerca
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (g_RQC_ENTITAT_RECERCA) : {
                if (resultCode == Activity.RESULT_OK) {
                    /*
                    // Aquest codi ja no es fa servir pero el deixem per exemple

                    PAREntitat l_dadesEntitat = (PAREntitat) data.getSerializableExtra("Seleccio");
                    Entitat l_entitat = new Entitat();
                    // Recuperem les dades de la entitat amb la que volem associar-nos i l'afegirm
                    // al Spinner
                    l_entitat.Codi = l_dadesEntitat.Codi;
                    l_entitat.Pais = l_dadesEntitat.Pais;
                    l_entitat.Nom = l_dadesEntitat.Nom;
                    l_entitat.eMail = l_dadesEntitat.eMail;
                    l_entitat.Contacte = l_dadesEntitat.Contacte;
                    l_entitat.Adresa = l_dadesEntitat.Adresa;
                    l_entitat.Telefon = l_dadesEntitat.Telefon;
                    SpnEntitat l_spinner = new SpnEntitat(l_entitat, l_entitat.Nom, true);
                    ((ArrayAdapter)lSPN_EntitatsClient.getAdapter()).add(l_spinner);
                    // Notifiquem el canvi i ens posicionem al final per seleccionar el element
                    ((ArrayAdapter)lSPN_EntitatsClient.getAdapter()).notifyDataSetChanged();
                    lSPN_EntitatsClient.setSelection(lSPN_EntitatsClient.getCount());
                    */

                    int l_Posicio = data.getIntExtra("Seleccio", -1);
                    if (l_Posicio != -1) {
                        g_SPN_EntitatsClient.setSelection(l_Posicio);
                    }
                }
                break;
            }
        }
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