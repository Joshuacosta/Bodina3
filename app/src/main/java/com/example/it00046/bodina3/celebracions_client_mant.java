package com.example.it00046.bodina3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOCelebracionsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARCelebracioClient;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNSalonsClient;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SPNTipusCelebracio;
import com.example.it00046.bodina3.Classes.Validacio;
import java.util.Calendar;


public class celebracions_client_mant extends ActionBarActivity {
    public Context jo = this;
    private EditText g_ETX_Contacte, g_ETX_Lloc, g_ETX_Descripcio, g_ETX_NumConvidats;
    private Spinner g_SPN_Salo, g_SPN_TipusCelebracio;
    private TextView g_TXT_Data, g_TXT_Hora, g_TXT_TipusCelebracio, g_TXT_Salo;
    private Context Jo = this;
    private String g_Operativa = "Alta";
    private Calendar g_CAL_DataCelebracio = null, g_CAL_HoraCelebracio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ImageButton l_IMB_Data, l_IMB_Temps;
        PARCelebracioClient l_CelebracioClient;
        Intent l_intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.celebracions_client_mant);
        // Recuperem controls del layout
        // Data celebracio
        l_IMB_Data = (ImageButton) findViewById(R.id.CelebracionsClientMantIMBData);
        l_IMB_Data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int l_Any, l_Mes, l_Dia;
                Calendar l_Calendari = Calendar.getInstance();

                // Si hi ha data definida la informem, sino mostrem el dia actual
                if (g_CAL_DataCelebracio != null){
                    l_Any = g_CAL_DataCelebracio.get(Calendar.YEAR);
                    l_Mes = g_CAL_DataCelebracio.get(Calendar.MONTH);
                    l_Dia = g_CAL_DataCelebracio.get(Calendar.DAY_OF_MONTH);
                } else {
                    // No tenim data, agafem la actuial
                    l_Any = l_Calendari.get(Calendar.YEAR);
                    l_Mes = l_Calendari.get(Calendar.MONTH);
                    l_Dia = l_Calendari.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(jo, new LST_DatePickerSet(), l_Any, l_Mes, l_Dia);
                dialog.show();
            }
        });
        // Hora celebracio
        l_IMB_Temps = (ImageButton) findViewById(R.id.CelebracionsClientMantIMBTime);
        l_IMB_Temps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int l_Hora, l_Minut;
                Calendar l_Calendari = Calendar.getInstance();

                // Si hi ha hora definida la informem, sino mostrem el dia actual
                if (g_CAL_HoraCelebracio != null){
                    l_Hora = g_CAL_HoraCelebracio.get(Calendar.HOUR_OF_DAY);
                    l_Minut = g_CAL_HoraCelebracio.get(Calendar.MINUTE);
                }
                else{
                    // No tenim hora, agafem la actuial
                    l_Hora = l_Calendari.get(Calendar.HOUR_OF_DAY);
                    l_Minut = l_Calendari.get(Calendar.MINUTE);
                }
                TimePickerDialog dialog = new TimePickerDialog(jo, new LST_TimePickerSet(), l_Hora, l_Minut, DateFormat.is24HourFormat(jo));
                dialog.show();
            }
        });
        // Resta controls
        g_TXT_Data = (TextView) findViewById(R.id.CelebracionsClientMantTXTData);
        g_TXT_Hora = (TextView) findViewById(R.id.CelebracionsClientMantTXTHora);
        g_TXT_TipusCelebracio = (TextView) findViewById(R.id.CelebracionsClientMantTXTTipusCelebracio);
        g_TXT_Salo = (TextView) findViewById(R.id.CelebracionsClientMantTXTSalo);
        //
        g_ETX_Contacte = (EditText) findViewById(R.id.CelebracionsClientMantTXTContacte);
        g_ETX_Lloc = (EditText) findViewById(R.id.CelebracionsClientMantTXTLloc);
        g_ETX_Descripcio = (EditText) findViewById(R.id.CelebracionsClientMantTXTDescripcio);
        g_ETX_NumConvidats = (EditText) findViewById(R.id.CelebracionsClientMantTXTNumConvidats);
        // Spinner de tipus de celebracions
        g_SPN_TipusCelebracio = (Spinner)findViewById(R.id.CelebracionsClientMantSPNTipusCelebracio);
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
        g_SPN_Salo = (Spinner)findViewById(R.id.CelebracionsClientMantSPNSalo);
        DAOSalonsClient.Llegir(g_SPN_Salo, Jo);
        g_SPN_Salo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                SPNSalonsClient l_SaloSeleccionat;

                g_TXT_Salo.setError(null);
                // Validem si superem la capacitat del salo si tenim indicat el numero de comensals
                if (g_ETX_NumConvidats.getText().toString().trim().length() > 0){
                    l_SaloSeleccionat = (SPNSalonsClient) g_SPN_Salo.getSelectedItem();
                    if (Integer.parseInt(g_ETX_NumConvidats.getText().toString().trim()) > l_SaloSeleccionat.g_Salo.Capacitat){
                        g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                        g_ETX_NumConvidats.setError(Globals.g_Native.getString(R.string.error_CapacitatSuperada));
                    }
                    else{
                        if (Integer.parseInt(g_ETX_NumConvidats.getText().toString().trim()) == l_SaloSeleccionat.g_Salo.Capacitat) {
                            g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
                            g_ETX_NumConvidats.setError(Globals.g_Native.getString(R.string.error_LimitCapacitat));
                        }
                        else{
                            g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.black));
                            g_ETX_NumConvidats.setError(null);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });
        // Validem si estem fent un alta o una modificacio
        l_CelebracioClient = (PARCelebracioClient) l_intent.getSerializableExtra("CelebracioClient");
        if (l_CelebracioClient != null) {
            g_Operativa = "Modificacio";
            // Estem modificant, carreguem dades
            g_ETX_Descripcio.setText(l_CelebracioClient.Descripcio);
            g_ETX_NumConvidats.setText(Integer.toString(l_CelebracioClient.Convidats));
            g_ETX_Lloc.setText(l_CelebracioClient.Lloc);
            g_ETX_Contacte.setText(l_CelebracioClient.Contacte);
            g_TXT_Data.setText(Globals.MilliDataToString(l_CelebracioClient.Data));
            g_TXT_Hora.setText(Globals.MilliDataToString(l_CelebracioClient.Hora));
            // Marquem en el Spinners els valors adequats
            ArrayAdapter<SPNSalonsClient> l_LlistaSalons = (ArrayAdapter<SPNSalonsClient>)g_SPN_Salo.getAdapter();
            for (int i=0; i < l_LlistaSalons.getCount(); i++){
                SPNSalonsClient l_Element = l_LlistaSalons.getItem(i);
                if (l_Element.g_Salo != null){
                    if (l_Element.g_Salo.Codi == l_CelebracioClient.CodiSalo) {
                        g_SPN_Salo.setSelection(i);
                        break;
                    }
                }
            }

            ArrayAdapter<SPNTipusCelebracio> l_LlistaTipus = (ArrayAdapter<SPNTipusCelebracio>)g_SPN_TipusCelebracio.getAdapter();
            for (int i=0; i < l_LlistaTipus.getCount(); i++){
                SPNTipusCelebracio l_Element = l_LlistaTipus.getItem(i);
                if (l_Element.g_Tipus != null){
                    if (l_Element.g_Tipus.Codi == l_CelebracioClient.Tipus) {
                        g_SPN_TipusCelebracio.setSelection(i);
                        break;
                    }
                }
            }
        }
        else{
            // Fem un alta (no cal fer res)
        }
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
            public void afterTextChanged(Editable s) {
                SPNSalonsClient l_SaloSeleccionat;
                int l_Capacitat;
                // Validem que si hi ha definida una capacitat del salo mostrem
                // el valor en vermell i una explicacio de lo que pasa (ho fem si hi ha indicat un salo)
                if (!s.toString().trim().equalsIgnoreCase("")){
                    if (!g_SPN_Salo.getSelectedItem().toString().equals(Globals.g_Native.getString(R.string.llista_Select))) {
                        int l_NumConvidats = Integer.valueOf(s.toString());
                        l_SaloSeleccionat = (SPNSalonsClient) g_SPN_Salo.getSelectedItem();
                        l_Capacitat = l_SaloSeleccionat.g_Salo.Capacitat;
                        if (l_Capacitat >= 0) {
                            if (l_NumConvidats > l_Capacitat) {
                                g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.red));
                                g_ETX_NumConvidats.setError(Globals.g_Native.getString(R.string.error_CapacitatSuperada));
                            } else {
                                if (l_NumConvidats == l_Capacitat) {
                                    g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.orange));
                                    g_ETX_NumConvidats.setError(Globals.g_Native.getString(R.string.error_LimitCapacitat));
                                } else {
                                    g_ETX_NumConvidats.setTextColor(Globals.g_Native.getResources().getColor(R.color.black));
                                    g_ETX_NumConvidats.setError(null);
                                }
                            }
                        }
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                g_ETX_NumConvidats.setError(null);
            }
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
        if (g_TXT_Data.getText() == Globals.g_Native.getString(R.string.CelebracionsClientMantTXTData)){
            g_TXT_Data.setError(Globals.g_Native.getString(R.string.error_NoEventDate));
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
            l_SaloSeleccionat = (SPNSalonsClient) g_SPN_Salo.getSelectedItem();
            l_CelebracioClient.Salo.Codi = l_SaloSeleccionat.g_Salo.Codi;
            l_TipusCelebracioSeleccionat = (SPNTipusCelebracio) g_SPN_TipusCelebracio.getSelectedItem();
            l_CelebracioClient.Tipus.Codi = l_TipusCelebracioSeleccionat.g_Tipus.Codi;
            l_CelebracioClient.Descripcio = g_ETX_Descripcio.getText().toString();
            l_CelebracioClient.Convidats = Integer.valueOf(g_ETX_NumConvidats.getText().toString());
            l_CelebracioClient.Data = g_CAL_DataCelebracio.getTimeInMillis();
            l_CelebracioClient.Hora = g_CAL_HoraCelebracio.getTimeInMillis();
            l_CelebracioClient.Lloc = g_ETX_Lloc.getText().toString();
            l_CelebracioClient.Contacte = g_ETX_Contacte.getText().toString();
            l_CelebracioClient.Estat = Globals.k_CelebracioClientActiva;
            //
            if (g_Operativa == "Alta") {
                DAOCelebracionsClient.Afegir(l_CelebracioClient, Jo, true, true);
            }
            else{
                DAOCelebracionsClient.Modificar(l_CelebracioClient, Jo, true);
            }
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

    class LST_DatePickerSet implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker p_view, int p_year, int p_monthOfYear, int p_dayOfMonth) {
            if (g_CAL_DataCelebracio == null){
                g_CAL_DataCelebracio = Calendar.getInstance();
            }
            g_CAL_DataCelebracio.set(Calendar.DAY_OF_MONTH, p_dayOfMonth);
            g_CAL_DataCelebracio.set(Calendar.MONTH, p_monthOfYear+1);
            g_CAL_DataCelebracio.set(Calendar.YEAR, p_year);
            g_TXT_Data.setText(Globals.DataToString(g_CAL_DataCelebracio));
        }
    }

    class LST_TimePickerSet implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int p_Hora, int p_Minuts) {
            if (g_CAL_HoraCelebracio == null){
                g_CAL_HoraCelebracio = Calendar.getInstance();
            }
            g_CAL_HoraCelebracio.set(Calendar.HOUR_OF_DAY, p_Hora);
            g_CAL_HoraCelebracio.set(Calendar.MINUTE, p_Minuts);
            g_TXT_Hora.setText(Globals.HoraToString(g_CAL_HoraCelebracio));
        }
    }
}