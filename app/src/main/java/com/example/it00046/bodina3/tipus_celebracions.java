package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARTipusCelebracio;
import com.melnykov.fab.FloatingActionButton;

import java.util.Comparator;

public class tipus_celebracions extends ActionBarActivity{
    static private ListView g_LVW_TipusCelebracions;
    static EditText g_input;
    private int g_Posicio = -1;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static private int g_CodiModificacio;
    static private String g_Descripcio;
    static final int g_RQC_TIPUS_SOLICITEM = 1, g_RQC_TIPUS_MODIFIQUEM = 2;

    public static void MostraAlta(final Activity p_activity, final boolean p_Alta)
    {
        g_input = new EditText(p_activity);

        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(p_activity);
        if (p_Alta) {
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.tipus_celebracions_Afegir));
        }
        else{
            g_input.setText(g_Descripcio);
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.tipus_celebracions_Modificar));
        }
        g_alertDialogBuilder.setView(g_input);
        g_alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        TipusCelebracio l_TipusCelebracio = new TipusCelebracio();

                        l_TipusCelebracio.Descripcio = g_input.getText().toString();
                        if (p_Alta) {
                            // Fem la insercio i si va be refresquem la llista
                            if (DAOTipusCelebracions.Afegir(l_TipusCelebracio, p_activity, false, false)) {
                                DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions, R.layout.linia_lvw_llista_tipuscelebracions, p_activity);
                            }
                        }
                        else{
                            l_TipusCelebracio.Codi = g_CodiModificacio;
                            if (DAOTipusCelebracions.Modificar(l_TipusCelebracio, p_activity, false)){
                                DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions, R.layout.linia_lvw_llista_tipuscelebracions, p_activity);
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
        FloatingActionButton l_FLB_TipusCelebracio;
        final EditText g_input = new EditText(this);
        final AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(Jo);
        final AlertDialog l_Alert;
        final boolean l_ModusAlertAlta = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipus_celebracions);
        // Carreguen les entitats del client
        g_LVW_TipusCelebracions = (ListView) findViewById(R.id.tipus_celebracionsLVWTipus);
        DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions, R.layout.linia_lvw_llista_tipuscelebracions, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tamb? es pot fer des de el menu)
        l_FLB_TipusCelebracio = (FloatingActionButton) findViewById(R.id.tipus_celebracionsFLBAfegirTipus);
        l_FLB_TipusCelebracio.attachToListView(g_LVW_TipusCelebracions);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_TipusCelebracio.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MostraAlta(tipus_celebracions.this, true);
            }
        });
        //
        // Codi de seleccio de un element de la llista de entitats/associacions
        g_LVW_TipusCelebracions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;
                TipusCelebracio l_TipusCelebracio;

                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsIMBEditar);
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
        getMenuInflater().inflate(R.menu.tipus_celebracions, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        final Comparator<TipusCelebracio> ComparaNom = new Comparator<TipusCelebracio>() {
            public int compare(TipusCelebracio p_a1, TipusCelebracio p_a2) {
                return p_a1.Descripcio.compareToIgnoreCase(p_a2.Descripcio);
            }
        };

        switch (l_id) {
            case R.id.tipus_celebracionsMNUAfegir:
                l_Intent = new Intent(this, entitat_solicitar.class);
                startActivityForResult(l_Intent, g_RQC_TIPUS_SOLICITEM);
                return true;
            case R.id.tipus_celebracionsMNUOrdenar:
                ((ArrayAdapter<TipusCelebracio>) g_LVW_TipusCelebracions.getAdapter()).sort(ComparaNom);
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }
    // Resposta de les activitats que iniciem (solicitar,...)

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (g_RQC_TIPUS_SOLICITEM): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions,  R.layout.linia_lvw_llista_tipuscelebracions, Jo);
                }
                break;
            }
            case (g_RQC_TIPUS_MODIFIQUEM):{
                if (resultCode == Activity.RESULT_OK){
                    // Refresquem la llista (podiem ser mes optims i nomes modificar la entitat)
                    DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions,  R.layout.linia_lvw_llista_tipuscelebracions, Jo);
                }
                break;
            }
        }
    }
    */

    // Aquesta funci� es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWTipusCelebracionsIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaTipusCelebracio;
        PARTipusCelebracio l_Parametres;
        TipusCelebracio l_TipusCelebracio;
        final EditText g_input = new EditText(this);
        final AlertDialog l_Alert;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaTipusCelebracio = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaTipusCelebracio.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaTipusCelebracio.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio del tipus
            l_TipusCelebracio = (TipusCelebracio)l_LiniaTipusCelebracio.getTag();
            g_CodiModificacio = l_TipusCelebracio.Codi;
            g_Descripcio = l_TipusCelebracio.Descripcio;
            MostraAlta(tipus_celebracions.this, false);
        }
    }
    // Aquesta funci� es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWTipusCelebracionsIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaTipusCelebracio;
        TipusCelebracio l_TipusCelebracio;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaTipusCelebracio = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem
            l_TipusCelebracio = (TipusCelebracio)l_LiniaTipusCelebracio.getTag();
            if (DAOTipusCelebracions.Esborrar(l_TipusCelebracio.Codi, Jo, false)) {
                // Refresquem la llista
                DAOTipusCelebracions.Llegir(g_LVW_TipusCelebracions, R.layout.linia_lvw_llista_tipuscelebracions, Jo);
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
            l_IMB_Editar = (ImageButton)l_LiniaTipusCelebracio.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaTipusCelebracio.getBackground();
            l_transition.startTransition(500);
        }
    }
}