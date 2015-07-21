package com.example.it00046.bodina3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOAssociacions;
import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARAssociacio;
import com.example.it00046.bodina3.Classes.Validacio;

public class entitat_modificar extends ActionBarActivity {
    private EditText g_ETX_Descripcio, g_ETX_Contacte, g_ETX_eMail;
    private Context Jo = this;
    private PARAssociacio g_DadesAssociacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entitat_modificar);
        // Recuperem controls del layout
        g_ETX_Descripcio = (EditText) findViewById(R.id.TexteEntitatModificar_Descripcio);
        g_ETX_Contacte = (EditText) findViewById(R.id.TexteEntitatModificar_Contacte);
        g_ETX_eMail = (EditText) findViewById(R.id.TexteEntitatModificar_eMail);
        // Validem si estem editant una associacio
        Intent l_intent = getIntent();
        g_DadesAssociacio = (PARAssociacio) l_intent.getSerializableExtra("Associacio");
        g_ETX_Descripcio.setText(g_DadesAssociacio.Descripcio);
        g_ETX_Contacte.setText(g_DadesAssociacio.Contacte);
        g_ETX_eMail.setText(g_DadesAssociacio.eMail);
    }

    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        if (!Validacio.hasText(g_ETX_Descripcio)) ret = false;
        if (!Validacio.hasText(g_ETX_Contacte)) ret = false;
        if (!Validacio.isEmailAddress(g_ETX_eMail, true)) ret = false;
        return ret;
    }

    // Codi de acceptacio
    public void FerOperativa(){
        Associacio l_Associacio = new Associacio();

        // Validem que els camps estiguin informats
        if (ValidarFinestra()) {
            l_Associacio.Descripcio = g_ETX_Descripcio.getText().toString();
            l_Associacio.Contacte = g_ETX_Contacte.getText().toString();
            l_Associacio.eMail = g_ETX_eMail.getText().toString();
            l_Associacio.entitat.Codi = g_DadesAssociacio.CodiEntitat;
            //
            DAOAssociacions.Modificar(l_Associacio, Jo);
        }
        else{
            Toast.makeText(Jo,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu) {
        getMenuInflater().inflate(R.menu.entitat_modificar, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_item) {
        int l_id = p_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (l_id == R.id.entitat_modificarMNUAcceptar) {
            FerOperativa();
            return true;
        }

        return super.onOptionsItemSelected(p_item);
    }
}
