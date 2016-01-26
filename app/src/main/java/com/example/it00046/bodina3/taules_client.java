package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.it00046.bodina3.Classes.DAO.DAOTaulesClient;
import com.example.it00046.bodina3.Classes.Entitats.TaulaClient;
import com.example.it00046.bodina3.Classes.Globals;
import com.melnykov.fab.FloatingActionButton;

import java.util.Comparator;

public class taules_client extends ActionBarActivity{
    static private ListView g_LVW_Taules;
    private int g_Posicio = -1;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    public Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static private TaulaClient g_TaulaSeleccionada;

    public void MostraOperacio(final Activity p_activity, final boolean p_Alta)
    {
        final EditText l_ETX_Descripcio, l_ETX_MaxPersones, l_ETX_AmpladaDiametre, l_ETX_Llargada;
        final Spinner l_SPN_Tipus;
        ArrayAdapter<CharSequence> l_adapter_Tipus;
        View l_VIW_MantenimentTaula;
        final LayoutInflater inflater = getLayoutInflater();

        l_VIW_MantenimentTaula = inflater.inflate(R.layout.taules_client_dialog_mant, null);
        l_ETX_Descripcio = (EditText)l_VIW_MantenimentTaula.findViewById(R.id.TaulesClientDialogMantTXTDescripcio);
        l_ETX_MaxPersones = (EditText)l_VIW_MantenimentTaula.findViewById(R.id.TaulesClientDialogMantTXTMaxPersones);
        l_ETX_AmpladaDiametre = (EditText)l_VIW_MantenimentTaula.findViewById(R.id.TaulesClientDialogMantTXTAmpladaDiametre);
        l_ETX_Llargada = (EditText)l_VIW_MantenimentTaula.findViewById(R.id.TaulesClientDialogMantTXTLlargada);
        //
        l_adapter_Tipus = ArrayAdapter.createFromResource(Jo,R.array.TipusTaulesClient,android.R.layout.simple_spinner_item);
        l_adapter_Tipus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        l_SPN_Tipus = (Spinner)l_VIW_MantenimentTaula.findViewById(R.id.TaulesClientDialogMantSPNTipus);
        l_SPN_Tipus.setAdapter(l_adapter_Tipus);
        //
        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(p_activity);
        if (p_Alta) {
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.taules_client_Afegir));
        }
        else{
            // Posem els valors de la taula seleccionada
            l_SPN_Tipus.setSelection(g_TaulaSeleccionada.Tipus);
            l_ETX_Descripcio.setText(g_TaulaSeleccionada.Descripcio);
            l_ETX_MaxPersones.setText(Integer.toString(g_TaulaSeleccionada.MaxPersones));
            l_ETX_AmpladaDiametre.setText(Integer.toString(g_TaulaSeleccionada.AmpladaDiametre));
            l_ETX_Llargada.setText(Integer.toString(g_TaulaSeleccionada.Llargada));
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.taules_client_Modificar));
        }
        // Codi del Spinner de tipus de taula (afecta a camps)
        l_SPN_Tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                switch (pos){
                    case 0: // rodona
                        l_ETX_AmpladaDiametre.setHint(Globals.g_Native.getString(R.string.TaulesClientDialogTXTDiametre));
                        l_ETX_Llargada.setVisibility(View.GONE);
                        break;
                    case 1: // quadrada
                        l_ETX_AmpladaDiametre.setHint(Globals.g_Native.getString(R.string.TaulesClientDialogTXTAmplada));
                        l_ETX_Llargada.setVisibility(View.GONE);
                        break;
                    case 2: // rectangular
                        l_ETX_AmpladaDiametre.setHint(Globals.g_Native.getString(R.string.TaulesClientDialogTXTAmplada));
                        l_ETX_Llargada.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }

        });
        g_alertDialogBuilder.setView(l_VIW_MantenimentTaula);
        g_alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        TaulaClient l_TaulaClient = new TaulaClient();

                        // Posem els valors
                        l_TaulaClient.Descripcio = l_ETX_Descripcio.getText().toString();
                        l_TaulaClient.MaxPersones = Integer.valueOf(l_ETX_MaxPersones.getText().toString());;
                        l_TaulaClient.AmpladaDiametre = Integer.valueOf(l_ETX_AmpladaDiametre.getText().toString());
                        l_TaulaClient.Tipus = l_SPN_Tipus.getSelectedItemPosition();
                        if (l_TaulaClient.Tipus == TaulaClient.k_TipusRectangular || l_TaulaClient.Tipus == TaulaClient.k_TipusImperial) {
                            l_TaulaClient.Llargada = Integer.valueOf(l_ETX_Llargada.getText().toString());
                        }
                        else{
                            l_TaulaClient.Llargada = 0;
                        }
                        if (p_Alta) {
                            // Fem la insercio i si va be refresquem la llista
                            if (DAOTaulesClient.Afegir(l_TaulaClient, p_activity, false, false)) {
                                DAOTaulesClient.Llegir(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client, p_activity);
                                g_Posicio = -1;
                            }
                        }
                        else{
                            l_TaulaClient.Codi = g_TaulaSeleccionada.Codi;
                            if (DAOTaulesClient.Modificar(l_TaulaClient, p_activity, false)){
                                DAOTaulesClient.Llegir(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client, p_activity);
                                g_Posicio = -1;
                            }
                        }
                    }
                })
                .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface p_dialog, int p_id) {
                    }
                });
        g_alertDialogBuilder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_TaulesClient;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.taules_client);
        // Carreguen les entitats del client
        g_LVW_Taules = (ListView) findViewById(R.id.taulesClientLVWLlista);
        DAOTaulesClient.Llegir(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tamb? es pot fer des de el menu)
        l_FLB_TaulesClient = (FloatingActionButton) findViewById(R.id.taulesClientFLBAfegir);
        l_FLB_TaulesClient.attachToListView(g_LVW_Taules);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_TaulesClient.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(l_Animacio);
                MostraOperacio(taules_client.this, true);
            }
        });
        //
        // Codi de seleccio de un element de la llista de entitats/associacions
        g_LVW_Taules.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;

                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaTaulesClientIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaTaulesClientIMBEditar);
                    if (g_IMB_Editar != null) {
                        g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                        g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                        g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                        g_IMB_Editar.setVisibility(View.INVISIBLE);
                    }
                    l_IMB_Esborrar.setVisibility(View.VISIBLE);
                    l_IMB_Editar.setVisibility(View.VISIBLE);
                    l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                    l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    // Apuntem lo que hem tocat
                    g_Posicio = p_position;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                } else {
                    // Amaguem/mostrem els botons
                    // Animacio de botons SI CORRESPON (en funcio de l'estat de la associacio)
                    if (g_IMB_Editar.getVisibility() == View.VISIBLE) {
                        g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                        g_IMB_Editar.setVisibility(View.INVISIBLE);
                        g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                        g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                    } else {
                        g_IMB_Esborrar.setVisibility(View.VISIBLE);
                        g_IMB_Editar.setVisibility(View.VISIBLE);
                        g_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                        g_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    }
                }
            }

        });
        // Mostrem el tornar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu p_menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.taules_client, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        int l_id = p_Item.getItemId();

        switch (l_id) {
            case R.id.taulesClientMNUAfegir:
                MostraOperacio(taules_client.this, true);
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWTaulesClientIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaTaulaClient;
        TaulaClient l_TaulaClient;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaTaulaClient = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaTaulaClient.findViewById(R.id.LiniaLVWLlistaTaulesClientIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaTaulaClient.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio de la taula
            g_TaulaSeleccionada = (TaulaClient)l_LiniaTaulaClient.getTag();
            MostraOperacio(taules_client.this, false);
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWTaulesClientIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaTaulaClient;
        TaulaClient l_TaulaClient;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaTaulaClient = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem
            l_TaulaClient = (TaulaClient)l_LiniaTaulaClient.getTag();
            if (DAOTaulesClient.Esborrar(l_TaulaClient.Codi, Jo, false)) {
                // Refresquem la llista
                DAOTaulesClient.Llegir(g_LVW_Taules, R.layout.linia_lvw_llista_taules_client, Jo);
                g_Posicio = -1;
            }
        }
        else {
            // "Preparem" el esborrat
            g_EstatEsborrar = true;
            // Adaptem la linia per fer la baixa.
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton) l_view;
            l_IMB_Esborrar.setImageResource(R.drawable.ic_check_white_48dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton)l_LiniaTaulaClient.findViewById(R.id.LiniaLVWLlistaTaulesClientIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaTaulaClient.getBackground();
            l_transition.startTransition(500);
        }
    }
}