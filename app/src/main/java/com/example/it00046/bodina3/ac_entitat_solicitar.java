package com.example.it00046.bodina3;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
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

import com.example.it00046.bodina3.Classes.DAO.SQLEntitatsDAO;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.Classes.Tipus.EntitatClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.DAO.SQLEntitatsClientDAO;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SpnEntitat;
import com.example.it00046.bodina3.Classes.Validacio;
import com.example.it00046.bodina3.Classes.params.PAREntitat;

import java.util.List;


public class ac_entitat_solicitar extends ActionBarActivity {

    private Spinner lSPN_EntitatsClient;
    private EditText lTXT_Descripcio, lTXT_Contacte, lTXT_eMail;
    private TextView lTextEntitat;

    static final int PICK_ENTITAT_RECERCA = 1;  // Codis de resposta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_solicitar);
        // Recuperem controls del layout
        // Entrada de dades:
        lTXT_Descripcio = (EditText) findViewById(R.id.TexteEntitatSolicitar_Descripcio);
        lTXT_Contacte = (EditText) findViewById(R.id.TexteEntitatSolicitar_Contacte);
        lTXT_eMail = (EditText) findViewById(R.id.TexteEntitatSolicitar_eMail);
        //  Literals:
        lTextEntitat = (TextView) findViewById(R.id.litEntitatSolicitar_Entitat);
        // Spinners:
        lSPN_EntitatsClient = (Spinner)findViewById(R.id.spinnerEntitatSolicitar_Entitat);
        // Llegim en el SERVIDOR les entitats del pais del client
        SQLEntitatsDAO.F_SERVIDOR_LlistaEntitats(Globals.g_Client.Pais, lSPN_EntitatsClient);
        // Codi del Spinner de entitats del client
        lSPN_EntitatsClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                SpnEntitat l_SpnEntitat;
                Entitat l_dadesEntitat = new Entitat();
                // Esborrem possible error
                lTextEntitat.setError(null);
                // Si es una entitat de les "nostres" (amb la que hem treballat) recuperem
                // el e-mail i el contacte que vem donar i els posem en els camps
                l_SpnEntitat = (SpnEntitat) lSPN_EntitatsClient.getSelectedItem();
                if (l_SpnEntitat.EsNova() == false) {
                    l_dadesEntitat = l_SpnEntitat.getId();
                    lTXT_eMail.setText(l_dadesEntitat.eMail);
                    lTXT_Contacte.setText(l_dadesEntitat.Contacte);
                }
                else{
                    lTXT_eMail.setText("");
                    lTXT_Contacte.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Codi de validacio dels camps de la finestra (fem servir la clase estatica Validacio)
        lTXT_Descripcio.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {lTXT_Descripcio.setError(null);}
        });
        lTXT_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {lTXT_Contacte.setError(null);}
        });
        lTXT_eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {lTXT_eMail.setError(null);}
        });
    }
    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        if (!Validacio.hasText(lTXT_Descripcio)) ret = false;
        if (!Validacio.hasText(lTXT_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(lTXT_eMail, true)) ret = false;
        if (lSPN_EntitatsClient.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            lTextEntitat.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        return ret;
    }
    // Codi de acceptació
    public void btnEntitatSolicitar_Acceptar(View view) {
        EntitatClient l_entitatClient = new EntitatClient();
        Entitat l_dadesEntitat = new Entitat();
        PAREntitat l_entitat = new PAREntitat();
        SpnEntitat l_SpnEntitat;

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_entitatClient.DescripcioAssociacio = lTXT_Descripcio.getText().toString();
            l_entitatClient.ContacteAssociacio = lTXT_Contacte.getText().toString();
            l_entitatClient.eMailAssociacio = lTXT_eMail.getText().toString();
            l_SpnEntitat = (SpnEntitat) lSPN_EntitatsClient.getSelectedItem();
            l_dadesEntitat = l_SpnEntitat.getId();

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
            SQLEntitatsClientDAO.Solicitar(l_entitatClient);
            this.finish();
        }
        else{
            Toast.makeText(ac_entitat_solicitar.this,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }
    }
    //
    // Funcio per obrir la finestra de recerca de entitats
    public void btnAfegirEntitatOnClick(View view){
        Intent intent = new Intent(this, ac_entitat_recerca.class);
        startActivityForResult(intent, PICK_ENTITAT_RECERCA);
    }
    // Resposta de la recerca
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PICK_ENTITAT_RECERCA) : {
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
                    lSPN_EntitatsClient.setSelection(l_Posicio);
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
