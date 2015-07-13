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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.it00046.bodina3.Classes.DAO.DAOAssociacions;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.melnykov.fab.FloatingActionButton;


public class entitat_pral extends ActionBarActivity {
    private ListView g_LVW_Associacions;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
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
        DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tambè es pot fer des de el menu)
        l_FLB_Associacio = (FloatingActionButton) findViewById(R.id.entitat_pralFLBAfegirAssociacio);
        l_FLB_Associacio.attachToListView(g_LVW_Associacions);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_a_zero);
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Codi de seleccio de un element de la llista de entitats/associacions
        g_LVW_Associacions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;

                l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsLINToolbar);

                if (g_Posicio != p_position) {
                    // Desmarquem el que hi havia marcat
                    ((LinearLayout.LayoutParams) g_LIN_ToolbarAnterior.getLayoutParams()).bottomMargin = -80;
                    g_LIN_ToolbarAnterior.setVisibility(View.GONE);
                    // Apuntem en quina linia estem
                    g_Posicio = p_position;
                    // Definim l'animacio del item
                    ExpandAnimation l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                }
                else{
                    // Ens tornen a marcar, ens tanquem
                    ((LinearLayout.LayoutParams) l_LIN_Toolbar.getLayoutParams()).bottomMargin = -80;
                }
            }

        });
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
                startActivity(l_Intent);
                return true;
            case R.id.entitat_solicitarMNUActualitzar:

                //
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }

    // Resposta de la recerca
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (g_RQC_ENTITAT_SOLICITEM) : {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
                }
                break;
            }
        }
    }

}
