package com.example.it00046.bodina3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOAssociacions;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.melnykov.fab.FloatingActionButton;


public class entitat_pral extends ActionBarActivity {
    private ListView g_LVW_Associacions;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    static final int g_RQC_ENTITAT_SOLICITEM = 1;

    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_Associacio;

        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.entitat_pral);
        // Carreguen les entitats del client
        g_LVW_Associacions = (ListView) findViewById(R.id.entitat_pralLVWAssociacions);
        Globals.MostrarEspera(Jo);
        DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tamb� es pot fer des de el menu)
        l_FLB_Associacio = (FloatingActionButton) findViewById(R.id.entitat_pralFLBAfegirAssociacio);
        l_FLB_Associacio.attachToListView(g_LVW_Associacions);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_Associacio.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent l_Intent;

                arg0.startAnimation(l_Animacio);
                // Obrim la finestra de associacio
                l_Intent = new Intent(Jo, entitat_solicitar.class);
                startActivityForResult(l_Intent, g_RQC_ENTITAT_SOLICITEM);
            }
        });
        //
        // Codi de seleccio de un element de la llista de entitats/associacions
        g_LVW_Associacions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;

                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                //
                l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsLINToolbar);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton)p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEsborrar);
                    l_IMB_Editar = (ImageButton)p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEditar);
                    if (g_LIN_ToolbarAnterior != null && g_LIN_ToolbarAnterior.getVisibility() == View.VISIBLE) {
                        // Desmarquem el que hi havia marcat
                        l_expandAni = new ExpandAnimation(g_LIN_ToolbarAnterior, 100);
                        g_LIN_ToolbarAnterior.startAnimation(l_expandAni);
                        g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                        g_IMB_Editar.setVisibility(View.INVISIBLE);
                        g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                        g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                    }
                    // Definim l'animacio de la linia
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Animacio de botons
                    l_IMB_Esborrar.setVisibility(View.VISIBLE);
                    l_IMB_Editar.setVisibility(View.VISIBLE);
                    l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                    l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    // Apuntem
                    g_Posicio = p_position;
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                }
                else{
                    // Ens tornen a marcar
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Amaguem/mostrem els botons
                    if (g_IMB_Editar.getVisibility() == View.VISIBLE) {
                        g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                        g_IMB_Editar.setVisibility(View.INVISIBLE);
                        g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                        g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                    }
                    else{
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entitat_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id) {
            case R.id.entitat_solicitarMNUDemanar:
                l_Intent = new Intent(this, entitat_solicitar.class);
                startActivityForResult(l_Intent, g_RQC_ENTITAT_SOLICITEM);
                return true;
            case R.id.entitat_solicitarMNUActualitzar:
                DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
                //
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }

    // Resposta de les activitats que iniciem (solicitar,...)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (g_RQC_ENTITAT_SOLICITEM): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
                }
                break;
            }
        }
    }

    // Aquesta funció es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWRecercaEntitatsIMBEditar_Click(View view) {

    }
    // Aquesta funció es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWRecercaEntitatsIMBEsborrar_Click(View view) {
    }
}
