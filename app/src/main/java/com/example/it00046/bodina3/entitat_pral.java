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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.DAO.DAOAssociacions;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.melnykov.fab.FloatingActionButton;


public class entitat_pral extends ActionBarActivity {
    private ListView g_LVW_Associacions;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
    private Context Jo = this;

    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.entitat_pral);
        // Carreguen les entitats del client
        g_LVW_Associacions = (ListView) findViewById(R.id.entitat_pralLVWAssociacions);
        DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.LiniaLVWLlistaAssociacions, Jo);
        // Experiment boto
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(g_LVW_Associacions);
        //fab.show(true);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        fab.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animAlpha);
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
                    // Definim l'animació del item
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
            case R.id.entitat_SolicitarEntitat:
                l_Intent = new Intent(this, entitat_solicitar.class);
                startActivity(l_Intent);
                return true;
            case R.id.entitat_Actualitzar:
                //
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }
}
