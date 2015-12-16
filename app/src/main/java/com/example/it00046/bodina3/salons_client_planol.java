package com.example.it00046.bodina3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Feina.linia;
import com.example.it00046.bodina3.Classes.Feina.texte;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARSaloClient;
import com.example.it00046.bodina3.Classes.SimpleDrawView;
import com.example.it00046.bodina3.Classes.Validacio;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by it00046 on 05/10/2015.
 */
public class salons_client_planol  extends ActionBarActivity {
    Context Jo = this;
    EditText g_NomSalo;
    SimpleDrawView g_Draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton l_FLB_Texte, l_FLB_Configuracio, l_FLB_Recta, l_FLB_Assistent;
        final FloatingActionMenu l_FLM_Eines;
        ImageButton l_IMB_Esborrar, l_IMB_Ajuda;
        final LayoutInflater inflater = getLayoutInflater();
        PARSaloClient l_CodiPlanol;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salons_client_planol);
        // Recuperem controls
        g_Draw = (SimpleDrawView) findViewById(R.id.SalonsClientPlanolVIWDrawing);
        g_Draw.g_Pare = Jo;
        l_IMB_Esborrar = (ImageButton) findViewById(R.id.salonsClientPlanolIMBEsborrar);
        g_Draw.g_IMB_Esborrar = l_IMB_Esborrar;
        l_IMB_Ajuda = (ImageButton) findViewById(R.id.salonsClientPlanolIMBAjuda);
        g_NomSalo = (EditText) findViewById(R.id.SalonsClientPlanolTXTNom);
        // Validem si estem editant una associacio
        Intent l_intent = getIntent();
        l_CodiPlanol = (PARSaloClient) l_intent.getSerializableExtra("SaloClient");
        if (l_CodiPlanol != null){
            // Estem modificant
        }
        else{
            // Estem en alta
        }
        // Accions dels FLB
        l_FLM_Eines = (FloatingActionMenu) findViewById(R.id.salonsClientPlanolFLMEines);
        // Inicialment dibuixem rectes
        l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_dibuixar_rectes_mes24dp));
        g_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.recta;
        // Informem resta de parametres inicials
        g_Draw.g_EscalaPlanol = Globals.g_Native.getString(R.string.LlistaEscalaDefault);
        g_Draw.g_UnitatsPlanol = Globals.g_Native.getString(R.string.LlistaUnitatsDefault);
        //
        l_FLM_Eines.setOnMenuButtonClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Tanquem el altre menu i ens obrim
                l_FLM_Eines.toggle(true);
            }
        });
        // Sub-menus
        l_FLB_Texte = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBTexte);
        l_FLB_Texte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                g_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.texte;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_format_color_text_white_24dp));
                l_FLM_Eines.close(true);
            }
        });
        l_FLB_Recta = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBRecta);
        l_FLB_Recta.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                g_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.recta;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_border_color_white_24dp));
                l_FLM_Eines.close(true);
            }
        });
        // Finestra de configuracio
        //
        l_FLB_Configuracio = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBConfiguracio);
        l_FLB_Configuracio.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Mostrem la finestra de configuracio
                final Spinner l_SPN_Escala, l_SPN_Unitats;
                final CheckBox l_CBX_Quadricula;
                ArrayAdapter<CharSequence> l_adapter_Escala, l_adapter_Unitats;
                View l_VIW_Config = inflater.inflate(R.layout.salons_client_planol_dialog_config, null);
                int l_spinnerPosition;

                // CheckBox
                l_CBX_Quadricula = (CheckBox) l_VIW_Config.findViewById(R.id.SalonsClientPlanolDialogConfigCBXQuadricula);
                // Spinners
                l_adapter_Escala = ArrayAdapter.createFromResource(Jo,R.array.Escala,android.R.layout.simple_spinner_item);
                l_adapter_Escala.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                l_SPN_Escala = (Spinner) l_VIW_Config.findViewById(R.id.SalonsClientPlanolDialogConfigSPNEscala);
                l_SPN_Escala.setAdapter(l_adapter_Escala);
                l_spinnerPosition = l_adapter_Escala.getPosition(g_Draw.g_EscalaPlanol);
                l_SPN_Escala.setSelection(l_spinnerPosition);
                //
                l_adapter_Unitats = ArrayAdapter.createFromResource(Jo,R.array.Unitats,android.R.layout.simple_spinner_item);
                l_adapter_Unitats.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                l_SPN_Unitats = (Spinner) l_VIW_Config.findViewById(R.id.SalonsClientPlanolDialogConfigSPNUnitats);
                l_SPN_Unitats.setAdapter(l_adapter_Unitats);
                l_SPN_Unitats.setAdapter(l_adapter_Unitats);
                l_spinnerPosition = l_adapter_Unitats.getPosition(g_Draw.g_UnitatsPlanol);
                l_SPN_Unitats.setSelection(l_spinnerPosition);
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
                                g_Draw.g_EscalaPlanol = l_SPN_Escala.getSelectedItem().toString();
                                g_Draw.g_UnitatsPlanol = l_SPN_Unitats.getSelectedItem().toString();
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
        l_FLB_Assistent = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBAssistent);
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
        // Boto/Imatge de esborrar
        l_IMB_Esborrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Hauriem de preguntar si ho fem o no!!!!!!!!!!!!!!!!!!!!!
                g_Draw.EsborrarPlanol();
            }
        });
        // Boto de ajuda
        l_IMB_Ajuda.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Mostrem el dialeg de ajuda
            }
        });
        // Codi de validacio dels camps de la finestra (fem servir la clase estatica Validacio)
        g_NomSalo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                g_NomSalo.setError(null);
            }
        });
        // Control de enrera/cancelacio
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_48dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salons_client_planol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        if (l_id == R.id.SalonsClientPlanolMNUAcceptar) {
            FerOperativa();
            return true;
        }

        return super.onOptionsItemSelected(p_Item);
    }

    // Funcio interna per validar la finestra
    private boolean ValidarFinestra() {
        boolean ret = true;

        // Validem si hi han linies aquestes tanquin el planol
        if ((g_Draw.g_LiniesPlanol.size() > 0) && (!g_Draw.g_Finalitzat)) {
            // El planol s'ha de tancar
            Toast.makeText(Jo,
                    Globals.g_Native.getString(R.string.error_PlanolNoTancat),
                    Toast.LENGTH_LONG).show();
            ret = false;
        }
        // Camps obligatoris
        if (!Validacio.hasText(g_NomSalo)) ret = false;
        //
        return ret;
    }

    // Funcio interna per fer la operativa
    private void FerOperativa(){
        SaloClient l_SaloClient = new SaloClient();
        SaloClient.DetallPlanol l_Detall = new SaloClient.DetallPlanol();
        int i;
        linia l_Linia;
        texte l_Texte;

        // Validem finestra
        if (ValidarFinestra()) {
            l_SaloClient.Nom = g_NomSalo.getText().toString();
            // Llegim les linies del plano
            for (i = 0; i < g_Draw.g_LiniesPlanol.size(); i++) {
                l_Linia = g_Draw.g_LiniesPlanol.get(i);
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
                l_SaloClient.g_Planol.add(l_Detall);
            }
            // LLegim els textes
            for (i = 0; i < g_Draw.g_TextesPlanol.size(); i++) {
                l_Texte = g_Draw.g_TextesPlanol.get(i);
                l_Detall.Tipus = 0;
                l_Detall.OrigenX = l_Texte.Punt.x;
                l_Detall.OrigenY = l_Texte.Punt.y;
                l_Detall.Texte = l_Texte.Texte;
                //
                l_SaloClient.g_Planol.add(l_Detall);
            }
            DAOSalonsClient.Afegir(l_SaloClient, Jo, true, true);
        }
    }
}
