package com.example.it00046.bodina3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOCelebracionsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNSalonsClient;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNTipusCelebracio;
import com.example.it00046.bodina3.Classes.Validacio;

import java.util.Calendar;


public class celebracions_client_alta extends ActionBarActivity {

    public String g_DataCelebracio = new String();
    public Context jo = this;
    private EditText g_ETX_Contacte, g_ETX_Lloc, g_ETX_Descripcio, g_ETX_NumConvidats;
    private Spinner g_SPN_Salo, g_SPN_TipusCelebracio;
    private TextView g_TXT_Data, g_TXT_TipusCelebracio, g_TXT_Salo;
    private Context Jo = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton l_IMB_Data;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.celebracions_client_alta);
        // Recuperem controls del layout
        l_IMB_Data = (ImageButton) findViewById(R.id.CelebracionsClientAltaIMBData);
        l_IMB_Data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(jo,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });
        g_TXT_Data = (TextView) findViewById(R.id.CelebracionsClientAltaTXTData);
        g_TXT_TipusCelebracio = (TextView) findViewById(R.id.CelebracionsClientAltatTXTTipusCelebracio);
        g_TXT_Salo = (TextView) findViewById(R.id.CelebracionsClientAltatTXTSalo);
        //
        g_ETX_Contacte = (EditText) findViewById(R.id.CelebracionsClientAltaTXTContacte);
        g_ETX_Lloc = (EditText) findViewById(R.id.CelebracionsClientAltaTXTLloc);
        g_ETX_Descripcio = (EditText) findViewById(R.id.CelebracionsClientAltaTXTDescripcio);
        g_ETX_NumConvidats = (EditText) findViewById(R.id.CelebracionsClientAltaTXTNumConvidats);
        // Spinner de tipus de celebracions
        g_SPN_TipusCelebracio = (Spinner)findViewById(R.id.CelebracionsClientAltaSPNTipusCelebracio);
        DAOTipusCelebracions.Llegir(g_SPN_TipusCelebracio, Jo);
        g_SPN_TipusCelebracio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                // Esborrem possible error
                g_TXT_TipusCelebracio.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Spinner de salons del client
        g_SPN_Salo = (Spinner)findViewById(R.id.CelebracionsClientAltaSPNSalo);
        DAOSalonsClient.Llegir(g_SPN_Salo, Jo);
        g_SPN_Salo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                g_TXT_Salo.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Codi de validacio dels camps de la finestra (fem servir la clase estatica Validacio)
        g_TXT_Data.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_TXT_Data.setError(null);}
        });
        g_ETX_Contacte.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_ETX_Contacte.setError(null);}
        });
        g_ETX_Lloc.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_ETX_Lloc.setError(null);}
        });
        g_ETX_Descripcio.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_ETX_Descripcio.setError(null);}
        });
        g_ETX_NumConvidats.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_ETX_NumConvidats.setError(null);}
        });
        // Control de enrera/cancelacio
        ActionBar l_actionBar = getSupportActionBar();
        l_actionBar.setDisplayHomeAsUpEnabled(true);
        l_actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
    }

    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        // Validacio data celebracio
        if (g_TXT_Data.getText() == Globals.g_Native.getString(R.string.CelebracionsClientAltaTXTData)){
            g_TXT_Data.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        // Camps obligatoris
        if (!Validacio.hasText(g_ETX_Contacte)) ret = false;
        if (!Validacio.hasText(g_ETX_Lloc)) ret = false;
        if (!Validacio.hasText(g_ETX_Descripcio)) ret = false;
        if (!Validacio.hasText(g_ETX_NumConvidats)) ret = false;
        // Validacions Spinner's informats
        if (g_SPN_TipusCelebracio.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            g_TXT_TipusCelebracio.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        if (g_SPN_Salo.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
            g_TXT_Salo.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        return ret;
    }

    // Codi de acceptacio
    public void FerOperativa(){
        CelebracioClient l_CelebracioClient = new CelebracioClient();
        SPNSalonsClient l_SaloSeleccionat;
        SPNTipusCelebracio l_TipusCelebracioSeleccionat;

        if (ValidarFinestra()) {
            // Donem d'alta la celebracio (no validem res mes com podria ser el numero maxim de comensals
            // que admet el salo ja que en les celebracions i salons del client no ho valorem)
            l_SaloSeleccionat = (SPNSalonsClient) g_SPN_Salo.getSelectedItem();
            l_CelebracioClient.CodiSalo = l_SaloSeleccionat.g_Salo.Codi;
            l_TipusCelebracioSeleccionat = (SPNTipusCelebracio) g_SPN_TipusCelebracio.getSelectedItem();
            l_CelebracioClient.Tipus = l_TipusCelebracioSeleccionat.g_Tipus.Codi;
            l_CelebracioClient.Descripcio = g_ETX_Descripcio.getText().toString();
            l_CelebracioClient.Convidats = Integer.valueOf(g_ETX_NumConvidats.getText().toString());
            l_CelebracioClient.Data = g_TXT_Data.toString();
            l_CelebracioClient.Lloc = g_ETX_Lloc.toString();
            l_CelebracioClient.Contacte = g_ETX_Contacte.toString();
            //
            DAOCelebracionsClient.Afegir(l_CelebracioClient, Jo, true, true);
        }
        else{
            Toast.makeText(Jo,
                    Globals.g_Native.getString(R.string.error_Layout),
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.celebracions_client_alta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.CelebracionsClientAltaMNUAcceptar) {
            FerOperativa();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            g_DataCelebracio = (mMonth + 1) + "/" + mDay + "/" + mYear;
        }
    }
}
