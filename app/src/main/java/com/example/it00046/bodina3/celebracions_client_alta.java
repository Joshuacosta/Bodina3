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

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Validacio;

import java.util.Calendar;


public class celebracions_client_alta extends ActionBarActivity {

    public String g_DataCelebracio = new String();
    public Context jo = this;
    private EditText g_ETX_Contacte, g_ETX_Lloc, g_ETX_Descripcio, g_ETX_NumConvidats;
    Spinner g_SPN_Salo, g_SPN_TipusCelebracio;
    private TextView g_TXT_Data;
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

                //g_TXT_Entitat.setError(null);
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
                // Validem si hi caben els convidats

                //g_TXT_Entitat.setError(null);
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

        if (g_TXT_Data.getText() == Globals.g_Native.getString(R.string.CelebracionsClientAltaTXTData)){
            g_TXT_Data.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }
        if (!Validacio.hasText(g_ETX_Contacte)) ret = false;
        if (!Validacio.hasText(g_ETX_Lloc)) ret = false;
        if (!Validacio.hasText(g_ETX_Descripcio)) ret = false;
        if (!Validacio.hasText(g_ETX_NumConvidats)) ret = false;
        /* Validacio Spinner
        if (g_SPN_EntitatsClient.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select_pais))) {
            g_TXT_Entitat.setError(Globals.g_Native.getString(R.string.error_CampObligatori));
            ret = false;
        }

        Tambe hauras de validar certa logica: que hi capiguem els convidats definits, ...

        */
        return ret;
    }

    // Codi de acceptació
    public void FerOperativa(){
        CelebracioClient l_CelebracioClient = new CelebracioClient();

        if (ValidarFinestra()){

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
        if (id == R.id.action_settings) {
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
