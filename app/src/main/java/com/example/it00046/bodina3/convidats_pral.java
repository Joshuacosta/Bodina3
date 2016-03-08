package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOConvidats;
import com.example.it00046.bodina3.Classes.Entitats.Convidat;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.melnykov.fab.FloatingActionButton;

import java.util.Comparator;

/**
 * Created by it00046 on 02/03/2016.
 */
public class convidats_pral extends ActionBarActivity {
    private ListView g_LVW_Convidats;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
    private Convidat g_ConvidatAnterior;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static final int g_RQC_CONVIDAT_ALTA = 1, g_RQC_CONVIDAT_MODIFIQUEM = 2;
    private AlertDialog.Builder g_alertDialogBuilder;
    private int g_OpcioOrdenacio = -1;
    private int g_CodiCelebracio;

    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_Convidat;

        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.convidats_pral);
        // Llegim la celebracio amb la que treballem
        g_CodiCelebracio = Globals.g_CelebracioClientTreball.Codi;
        // Carreguen les entitats del client
        g_LVW_Convidats = (ListView) findViewById(R.id.convidats_pralLVWConvidats);
        DAOConvidats.LlegirComplerta(g_CodiCelebracio, g_LVW_Convidats, R.layout.linia_lvw_llista_convidats_complerta, Jo);
        l_FLB_Convidat = (FloatingActionButton) findViewById(R.id.convidats_pralFLBAltaConvidat);
        l_FLB_Convidat.attachToListView(g_LVW_Convidats);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_Convidat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent l_Intent;

                arg0.startAnimation(l_Animacio);
                // Obrim la finestra de alta
                l_Intent = new Intent(Jo, convidats_mant.class);
                startActivityForResult(l_Intent, g_RQC_CONVIDAT_ALTA);
            }
        });
        //
        g_LVW_Convidats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;
                Convidat l_Convidat;
                TransitionDrawable l_transition;

                l_Convidat = (Convidat) p_view.getTag();
                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                //
                l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWLlistaCelebracionsClientLINToolbar);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaCelebracionsClientIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaCelebracionsClientIMBEditar);
                    if (g_LIN_ToolbarAnterior != null && g_LIN_ToolbarAnterior.getVisibility() == View.VISIBLE) {
                        // Desmarquem el que hi havia marcat
                        l_expandAni = new ExpandAnimation(g_LIN_ToolbarAnterior, 100);
                        g_LIN_ToolbarAnterior.startAnimation(l_expandAni);
                        // Animacio de botons SI CORRESPON (en funcio de l'estat)
                        if (g_ConvidatAnterior.Estat == Globals.k_ConvidatActiu) {
                            g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                            g_IMB_Editar.setVisibility(View.INVISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                            g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                        }
                        // Treiem el color de seleccio
                        l_transition = (TransitionDrawable)g_LIN_ToolbarAnterior.getBackground();
                        l_transition.reverseTransition(200);
                    }
                    // Definim l'animacio de la linia
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Animacio de botons SI CORRESPON (en funcio de l'estat)
                    if (l_Convidat.Estat == Globals.k_ConvidatActiu) {
                        l_IMB_Esborrar.setVisibility(View.VISIBLE);
                        l_IMB_Editar.setVisibility(View.VISIBLE);
                        l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                        l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    }
                    // Seleccionem la linia
                    l_LIN_Toolbar.setBackgroundResource(R.drawable.fons_seleccionat);
                    l_transition = (TransitionDrawable)l_LIN_Toolbar.getBackground();
                    l_transition.startTransition(200);
                    // Apuntem lo que hem tocat
                    g_Posicio = p_position;
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                    g_ConvidatAnterior = l_Convidat;
                }
                else {
                    // Ens tornen a marcar
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Amaguem/mostrem els botons
                    if (l_Convidat.Estat == Globals.k_ConvidatActiu) {
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
            }

        });
        // Mostrem el tornar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Construim la finestra de ordenar
        // Titul
        g_alertDialogBuilder = new AlertDialog.Builder(Jo);
        g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.Ordenacio));
        // Recuperem la llista de opcions de ordenacio
        final String[] l_Ordres = Globals.g_Native.getResources().getStringArray(R.array.OrdreConvidats);
        // Funcions de comparacio
        final Comparator<Convidat> ComparaNom = new Comparator<Convidat>() {
            public int compare(Convidat p_a1, Convidat p_a2) {
                return p_a1.Nom.compareToIgnoreCase(p_a2.Nom);
            }
        };
        final Comparator<Convidat> ComparaCategoriaPral = new Comparator<Convidat>() {
            public int compare(Convidat p_a1, Convidat p_a2) {
                return ((Integer)p_a1.Categoria1.Codi).compareTo(p_a2.Categoria1.Codi);
            }
        };
        final Comparator<Convidat> ComparaCategoriaSec = new Comparator<Convidat>() {
            public int compare(Convidat p_a1, Convidat p_a2) {
                return ((Integer)p_a1.Categoria2.Codi).compareTo(p_a2.Categoria2.Codi);
            }
        };
        final Comparator<Convidat> ComparaTipus = new Comparator<Convidat>() {
            public int compare(Convidat p_a1, Convidat p_a2) {
                return ((Integer)p_a1.Tipus).compareTo(p_a2.Tipus);
            }
        };
        final Comparator<Convidat> ComparaEstat = new Comparator<Convidat>() {
            public int compare(Convidat p_a1, Convidat p_a2) {
                return ((Integer)p_a1.Estat).compareTo(p_a2.Estat);
            }
        };
        // Configurem
        g_alertDialogBuilder
                .setCancelable(false)
                .setSingleChoiceItems(l_Ordres, g_OpcioOrdenacio, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:// Per nom
                                ((ArrayAdapter<Convidat>) g_LVW_Convidats.getAdapter()).sort(ComparaNom);
                                break;
                            case 1:// Per categoria pral
                                ((ArrayAdapter<Convidat>) g_LVW_Convidats.getAdapter()).sort(ComparaCategoriaPral);
                                break;
                            case 2:// Per categoria sec
                                ((ArrayAdapter<Convidat>) g_LVW_Convidats.getAdapter()).sort(ComparaCategoriaSec);
                                break;
                            case 3:// Per tipus
                                ((ArrayAdapter<Convidat>) g_LVW_Convidats.getAdapter()).sort(ComparaTipus);
                                break;
                            case 4:// Per estat (?)
                                ((ArrayAdapter<Convidat>) g_LVW_Convidats.getAdapter()).sort(ComparaEstat);
                                break;
                        }
                        // Tanquem la finestra de ordenacio
                        arg0.cancel();
                    }
                })
                .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface p_dialog, int p_id) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.convidats_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id) {
            case R.id.convidats_pralMNUAltaConvidat:
                l_Intent = new Intent(this, convidats_mant.class);
                startActivityForResult(l_Intent, g_RQC_CONVIDAT_ALTA);
                return true;
            case R.id.convidats_pralMNUOrdenar:
                AlertDialog l_alertDialog = g_alertDialogBuilder.create();
                l_alertDialog.show();
                return true;
            case R.id.convidats_pralMNUActualitzar:
                DAOConvidats.LlegirComplerta(g_CodiCelebracio, g_LVW_Convidats, R.layout.linia_lvw_llista_convidats_complerta, Jo);
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }

    // Resposta de les activitats que iniciem (solicitar,...)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (g_RQC_CONVIDAT_ALTA): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOConvidats.LlegirComplerta(g_CodiCelebracio, g_LVW_Convidats, R.layout.linia_lvw_llista_convidats_complerta, Jo);
                }
                break;
            }
            case (g_RQC_CONVIDAT_MODIFIQUEM):{
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes modificar la entitat)
                    DAOConvidats.LlegirComplerta(g_CodiCelebracio, g_LVW_Convidats, R.layout.linia_lvw_llista_convidats_complerta, Jo);
                }
                break;
            }
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWConvidatsIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaConvidat;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaConvidat = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaConvidat.findViewById(R.id.LiniaLVWLlistaConvidatsIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaConvidat.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            Globals.g_ConvidatTreball = (Convidat)l_LiniaConvidat.getTag();
            Intent l_editem = new Intent(this, convidats_mant.class);
            startActivityForResult(l_editem, g_RQC_CONVIDAT_MODIFIQUEM);
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWConvidatsIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaConvidat;
        Convidat l_Convidat;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaConvidat = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem (modifiquem el seu estat)
            l_Convidat = (Convidat)l_LiniaConvidat.getTag();
            if (DAOConvidats.Esborrar(l_Convidat.Codi, Jo, false)){
                DAOConvidats.LlegirComplerta(g_CodiCelebracio, g_LVW_Convidats, R.layout.linia_lvw_llista_convidats_complerta, Jo);
            }
        }
        else {
            g_EstatEsborrar = true;
            // Adaptem la linia per fer la baixa.
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton) l_view;
            l_IMB_Esborrar.setImageResource(R.drawable.ic_check_white_48dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton)l_LiniaConvidat.findViewById(R.id.LiniaLVWLlistaConvidatsIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_LiniaConvidat.setBackgroundResource(R.drawable.fons_esborrar);
            l_transition = (TransitionDrawable)l_LiniaConvidat.getBackground();
            l_transition.startTransition(500);
        }
    }
}
