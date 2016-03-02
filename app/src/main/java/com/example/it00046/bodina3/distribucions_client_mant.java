package com.example.it00046.bodina3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.DAO.DAOTaulesClient;
import com.example.it00046.bodina3.Classes.DistribucioEdicio;
import com.example.it00046.bodina3.Classes.Entitats.Planol;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Feina.linia;
import com.example.it00046.bodina3.Classes.Entitats.Taula;
import com.example.it00046.bodina3.Classes.Feina.texte;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARSaloClient;
import com.example.it00046.bodina3.Classes.Validacio;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by it00046 on 05/10/2015.
 */
public class distribucions_client_mant extends ActionBarActivity {
    Context Jo = this;
    EditText g_NomDistribucio;
    DistribucioEdicio g_Draw;
    String g_ModusFeina = Globals.k_OPE_Alta;
    int g_CodiSalo;
    public DrawerLayout l_DRL_Taules;
    private ListView g_LVW_Taules, g_LVW_OperativaTaula;
    private LinearLayout g_LVW_OperativaTaula2;
    private SeekBar g_SEK_Zoom;
    static final int g_RQC_DISTRIBUCIONS_CLIENT_TAULA_COMENSALS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton l_FLB_Configuracio, l_FLB_Taula, l_FLB_Assistent;
        final FloatingActionMenu l_FLM_Eines;
        ImageButton l_IMB_Ajuda, l_IMB_EsborrarTaula, l_IMB_Comensals, l_IMB_GirarTaula;
        final LayoutInflater inflater = getLayoutInflater();
        PARSaloClient l_SaloClient;

        Intent l_intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribucions_client_mant);
        // Recuperem controls
        g_Draw = (DistribucioEdicio) findViewById(R.id.DistribucionsClientMantVIWDrawing);
        g_Draw.g_Pare = Jo;
        g_Draw.papa = this;
        // Recuperem el codi del salo per poder construir el planol del mateix
        // (aixo es indiferent de si fem una alta o modificacio de la distribucio de un client)
        g_CodiSalo = l_intent.getIntExtra("CodiSaloCelebracioClient", 0);
        g_Draw.g_CodiSalo = g_CodiSalo;
        l_IMB_Ajuda = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBAjuda);
        g_NomDistribucio = (EditText) findViewById(R.id.DistribucionsClientMantTXTNom);
        // Seekbar del zoom
        g_SEK_Zoom = (SeekBar) findViewById(R.id.DistribucionsClientMantSEKZoom);
        // Codi de les seekbars
        g_SEK_Zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float l_Zoom = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                l_Zoom = progresValue/40f;
                if (l_Zoom < 1) l_Zoom = 1;
                g_Draw.g_ScaleFactor = l_Zoom;
                g_Draw.g_EscalemPlanol = true;
                g_Draw.invalidate();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                g_Draw.g_EscalemPlanol = false;
            }
        });
        // Validem si estem fent un alta o una modificacio
        l_SaloClient = (PARSaloClient) l_intent.getSerializableExtra("DistribucioClient");
        if (l_SaloClient != null){
            // Estem modificant: Haurem de recuperar les dades de la distribucio i expresar-les
            g_ModusFeina = Globals.k_OPE_Update;
            //g_NomDistribucio.setText();
            //...
        }
        else{
            g_ModusFeina = Globals.k_OPE_Alta;
        }
        // Accions dels FLB
        l_FLM_Eines = (FloatingActionMenu) findViewById(R.id.DistribucionsClientMantFLMEines);
        // Inicialment treballem amb taules !!!!!!!!!!!!!!!!!!!!!!!!
        // Serie bo expresar-lo diferent, com fem a planol, que es motri la taula amb un + o algo asi.
        l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_action_eye_closed));
        g_Draw.g_ModusDibuix = DistribucioEdicio.g_Modus.taula;

        l_FLM_Eines.setOnMenuButtonClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Tanquem el altre menu i ens obrim
                l_FLM_Eines.toggle(true);
            }
        });
        // Boto de taules
        l_FLB_Taula = (FloatingActionButton) findViewById(R.id.DistribucionsClientMantFLBTaula);
        l_FLB_Taula.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                g_Draw.g_ModusDibuix = DistribucioEdicio.g_Modus.taula;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_action_eye_closed));
                l_FLM_Eines.close(true);
            }
        });
        // Finestra de configuracio
        //
        l_FLB_Configuracio = (FloatingActionButton) findViewById(R.id.DistribucionsClientMantFLBConfiguracio);
        l_FLB_Configuracio.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Mostrem la finestra de configuracio
                final Spinner l_SPN_Escala, l_SPN_Unitats;
                final CheckBox l_CBX_Quadricula, l_CBX_LiniesTaules;
                ArrayAdapter<CharSequence> l_adapter_Escala, l_adapter_Unitats;
                View l_VIW_Config = inflater.inflate(R.layout.distribucions_client_mant_dialog_config, null);
                int l_spinnerPosition;

                // CheckBoxes
                l_CBX_Quadricula = (CheckBox) l_VIW_Config.findViewById(R.id.DistribucionsClientMantDialogConfigCBXQuadricula);
                l_CBX_LiniesTaules = (CheckBox) l_VIW_Config.findViewById(R.id.DistribucionsClientMantDialogConfigCBXLiniesTaules);
                //
                AlertDialog.Builder g_DialogConfiguracio = new AlertDialog.Builder(Jo);
                g_DialogConfiguracio.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolLABConfiguracio));
                g_DialogConfiguracio.setView(l_VIW_Config);
                g_DialogConfiguracio
                        .setCancelable(false)
                        .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p_dialog, int which) {
                                // Llegim configuracio definida
                                if (l_CBX_Quadricula.isChecked()) {
                                    g_Draw.g_Quadricula = true;
                                }
                                if (l_CBX_LiniesTaules.isChecked()) {
                                    g_Draw.g_LiniesTaules = true;
                                }
                                // Repintem
                                g_Draw.invalidate();
                            }
                        })
                        .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface p_dialog, int p_id) {
                            }
                        });
                g_DialogConfiguracio.show();
                //
                l_FLM_Eines.close(true);
            }
        });
        // Finestra de l'assistent
        //
        l_FLB_Assistent = (FloatingActionButton) findViewById(R.id.DistribucionsClientMantFLBAssistent);
        l_FLB_Assistent.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Mostrem la finestra de l'assistent
                final SeekBar l_SEK_Amplada, l_SEK_Llargada;
                final TextView l_TXT_Amplada, l_TXT_Llargada;

                View l_VIW_Config = inflater.inflate(R.layout.salons_client_planol_dialog_assistent, null);
                // Els seekbar i els textes de la finestra assistent
                l_SEK_Amplada = (SeekBar) l_VIW_Config.findViewById(R.id.SalonsClientAssistentSEKAmplada);
                l_SEK_Llargada = (SeekBar) l_VIW_Config.findViewById(R.id.SalonsClientAssistentSEKLlargada);
                // Definim els maxims definibles en funcio de la escala del planol
                l_SEK_Amplada.setMax(g_Draw.g_EscalaPlanolAmplada);
                l_SEK_Llargada.setMax(g_Draw.g_EscalaPlanolLlargada);
                // Textes
                l_TXT_Amplada = (TextView) l_VIW_Config.findViewById(R.id.SalonsClientAssistentLITAmplada);
                l_TXT_Llargada = (TextView) l_VIW_Config.findViewById(R.id.SalonsClientAssistentLITLlargada);
                // Codi de les seekbars
                l_SEK_Amplada.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int l_AmpladaTriada = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        l_AmpladaTriada = progresValue;
                        l_TXT_Amplada.setText(l_AmpladaTriada + " " + Globals.g_Native.getString(R.string.meters));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                l_SEK_Llargada.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int l_LlargadaTriada = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        l_LlargadaTriada = progresValue;
                        l_TXT_Llargada.setText(l_LlargadaTriada + " " + Globals.g_Native.getString(R.string.meters));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                //
                AlertDialog.Builder g_DialogAssistent = new AlertDialog.Builder(Jo);
                g_DialogAssistent.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolLABAssistent));
                g_DialogAssistent.setView(l_VIW_Config);
                g_DialogAssistent
                        .setCancelable(false)
                        .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p_dialog, int which) {
                                g_Draw.Assistent(l_SEK_Amplada.getProgress(), l_SEK_Llargada.getProgress());
                            }
                        })
                        .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface p_dialog, int p_id) {
                            }
                        });
                g_DialogAssistent.show();
                //
                l_FLM_Eines.close(true);
            }
        });
        // Boto de ajuda
        l_IMB_Ajuda.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Mostrem el dialeg de ajuda
            }
        });
        // Codi de validacio dels camps de la finestra (nomes tenim el nom de la distribucio)
        g_NomDistribucio.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_NomDistribucio.setError(null);
            }
        });
        // Menu lateral dret: carreguem la llista de taules
        l_DRL_Taules = (DrawerLayout) findViewById(R.id.DistribucionsClientMantNDRTaules);
        g_LVW_Taules = (ListView) findViewById(R.id.DistribucionsClientMantNDLTaules);
        DAOTaulesClient.LlegirSeleccio(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client_seleccio, this);
        // Listener damunt la llista i obrim
        g_LVW_Taules.setOnItemClickListener(new DrawerItemClickListener());
        l_DRL_Taules.openDrawer(Gravity.RIGHT);
        // Menu lateral esquerra: menu taula
        g_LVW_OperativaTaula2 = (LinearLayout) findViewById(R.id.DistribucionsClientMantNDLOperativaTaula);
        // Codi dels botons del menu de taula (el de la esquerra)
        l_IMB_EsborrarTaula = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBEsborrarTaula);
        l_IMB_EsborrarTaula.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        l_IMB_Comensals = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBComensals);
        l_IMB_Comensals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Obrim la finestra de la taula
                Intent l_Intent;

                l_Intent = new Intent(Jo, distribucions_client_taula_convidats.class);
                startActivityForResult(l_Intent, g_RQC_DISTRIBUCIONS_CLIENT_TAULA_COMENSALS);
            }
        });
        l_IMB_GirarTaula = (ImageButton) findViewById(R.id.DistribucionsClientMantIMBGirarTaula);
        l_IMB_GirarTaula.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        // Control de enrera/cancelacio
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TaulaClient l_TaulaClient = (TaulaClient) view.getTag();
            g_Draw.AfegirTaula(l_TaulaClient);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.distribucions_client_mant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id){
            case R.id.DistribucionsClientMantMNUAcceptar:
                FerOperativa();
                return true;
            case R.id.DistribucionsClientMantMNUEsborrar:
                // Hauriem de preguntar si ho fem o no!!!!!!!!!!!!!!!!!!!!!
                //
                // MANCA ESBORRAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // I QUE ESBORREM
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }

    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        // Validem si hi han linies aquestes tanquin el planol
        if ((g_Draw.g_LiniesPlanol.size() > 0)) {
            // El planol s'ha de tancar
            Toast.makeText(Jo,
                    Globals.g_Native.getString(R.string.error_PlanolNoTancat),
                    Toast.LENGTH_LONG).show();
            ret = false;
        }
        // Camps obligatoris
        if (!Validacio.hasText(g_NomDistribucio)) ret = false;
        //
        return ret;
    }

    // Funcio interna per fer la operativa
    private void FerOperativa(){
        SaloClient l_SaloClient = new SaloClient();
        Planol.Detall l_Detall;
        int i;
        linia l_Linia;
        texte l_Texte;

        // Validem finestra
        if (ValidarFinestra()) {
            l_SaloClient.Nom = g_NomDistribucio.getText().toString();
            // Llegim les linies del planol
            for (i = 0; i < g_Draw.g_LiniesPlanol.size(); i++) {
                l_Linia = g_Draw.g_LiniesPlanol.get(i);
                l_Detall = new Planol.Detall();
                l_Detall.Tipus = 0;
                l_Detall.OrigenX = l_Linia.Inici.x;
                l_Detall.OrigenY = l_Linia.Inici.y;
                l_Detall.DestiX = l_Linia.Fi.x;
                l_Detall.DestiY = l_Linia.Fi.y;
                if (l_Linia.Curva) {
                    l_Detall.CurvaX = l_Linia.PuntCurva.x;
                    l_Detall.CurvaY = l_Linia.PuntCurva.y;
                }
                //
                l_SaloClient.Planol.Grava(l_Detall);
            }
            // LLegim els textes
            for (i = 0; i < g_Draw.g_TextesPlanol.size(); i++) {
                l_Texte = g_Draw.g_TextesPlanol.get(i);
                l_Detall = new Planol.Detall();
                l_Detall.Tipus = 1;
                l_Detall.OrigenX = l_Texte.Punt.x;
                l_Detall.OrigenY = l_Texte.Punt.y;
                l_Detall.Texte = l_Texte.Texte;
                //
                l_SaloClient.Planol.Grava(l_Detall);
            }
            // Dades de configuracio
            l_SaloClient.UnitatsPlanol = g_Draw.g_UnitatsPlanol;
            l_SaloClient.EscalaPlanol = g_Draw.g_EscalaPlanol;
            l_SaloClient.UnitatMesura = g_Draw.g_UnitatX;
            if (g_ModusFeina == Globals.k_OPE_Alta) {
                DAOSalonsClient.Afegir(l_SaloClient, Jo, true, true);
            }
            else{
                l_SaloClient.Codi = g_CodiSalo;
                DAOSalonsClient.Modificar(l_SaloClient, Jo, true);
            }
        }
    }

    public void obreOperativaTaula(Taula p_Taula){
        Globals.DefineixTaulaTreball(p_Taula);
        l_DRL_Taules.openDrawer(Gravity.LEFT);
    }
}
