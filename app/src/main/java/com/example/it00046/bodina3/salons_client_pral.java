package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARSaloClient;
import com.melnykov.fab.FloatingActionButton;

import java.util.Comparator;

/**
 * Created by it00046 on 25/09/2015.
 */
public class salons_client_pral extends ActionBarActivity {
    private ListView g_LVW_SalonsClient;
    private int g_Posicio = -1;
    private SaloClient g_SaloClientAnterior;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static final int g_RQC_SALO_CLIENT_ALTA = 1, g_RQC_SALO_CLIENT_MODIFIQUEM = 2;

    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_SaloClient;

        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.salons_client_pral);
        // Carreguen les entitats del client
        g_LVW_SalonsClient = (ListView) findViewById(R.id.salons_clientLVWSalons);
        DAOSalonsClient.Llegir(g_LVW_SalonsClient, R.layout.linia_lvw_llista_salons_client, Jo);
        l_FLB_SaloClient = (FloatingActionButton) findViewById(R.id.salons_clientFLBAltaSalo);
        l_FLB_SaloClient.attachToListView(g_LVW_SalonsClient);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_SaloClient.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent l_Intent;

                arg0.startAnimation(l_Animacio);
                // Obrim la finestra de alta
                l_Intent = new Intent(Jo, salons_client_alta.class);
                startActivityForResult(l_Intent, g_RQC_SALO_CLIENT_ALTA);
            }
        });
        //
        g_LVW_SalonsClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;
                SaloClient l_SaloClient;

                l_SaloClient = (SaloClient) p_view.getTag();
                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                //
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaSalonsClientIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaSalonsClientIMBEditar);
                    if (g_IMB_Editar != null) {
                        // Animacio de botons SI CORRESPON (en funcio de l'estat)
                        if (g_SaloClientAnterior.Estat == Globals.k_SaloClientActiu) {
                            g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                            g_IMB_Editar.setVisibility(View.INVISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                            g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                        }
                    }
                    // Animacio de botons SI CORRESPON (en funcio de l'estat)
                    if (l_SaloClient.Estat == Globals.k_SaloClientActiu) {
                        l_IMB_Esborrar.setVisibility(View.VISIBLE);
                        l_IMB_Editar.setVisibility(View.VISIBLE);
                        l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                        l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    }
                    // Apuntem lo que hem tocat
                    g_Posicio = p_position;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                    g_SaloClientAnterior = l_SaloClient;
                }
                else {
                    // Amaguem/mostrem els botons
                    if (l_SaloClient.Estat == Globals.k_SaloClientActiu) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salons_client_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        final Comparator<SaloClient> ComparaNom = new Comparator<SaloClient>() {
            public int compare(SaloClient p_a1, SaloClient p_a2) {
                return p_a1.Nom.compareToIgnoreCase(p_a2.Nom);
            }
        };

        switch (l_id) {
            case R.id.salons_client_pralMNUAltaSalo:
                l_Intent = new Intent(this, salons_client_alta.class);
                startActivityForResult(l_Intent, g_RQC_SALO_CLIENT_ALTA);
                return true;
            case R.id.salons_client_pralMNUOrdenar:
                ((ArrayAdapter<SaloClient>) g_LVW_SalonsClient.getAdapter()).sort(ComparaNom);
                return true;
            case R.id.salons_client_pralMNUActualitzar:
                DAOSalonsClient.Llegir(g_LVW_SalonsClient, R.layout.linia_lvw_llista_salons_client, Jo);
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
            case (g_RQC_SALO_CLIENT_ALTA): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOSalonsClient.Llegir(g_LVW_SalonsClient, R.layout.linia_lvw_llista_salons_client, Jo);
                }
                break;
            }
            case (g_RQC_SALO_CLIENT_MODIFIQUEM):{
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes modificar la entitat)
                    DAOSalonsClient.Llegir(g_LVW_SalonsClient, R.layout.linia_lvw_llista_salons_client, Jo);
                }
                break;
            }
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWSalonsClientIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaSalo;
        PARSaloClient l_Parametres;
        SaloClient l_Salo;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaSalo = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaSalo.findViewById(R.id.LiniaLVWLlistaSalonsClientIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaSalo.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio de associacio
            l_Parametres = new PARSaloClient();
            l_Salo = (SaloClient)l_LiniaSalo.getTag();
            l_Parametres.Codi = l_Salo.Codi;
            l_Parametres.Nom = l_Salo.Nom;
            l_Parametres.Amplada = l_Salo.Amplada;
            l_Parametres.Alsada = l_Salo.Alsada;
            l_Parametres.CodiPlanol = l_Salo.CodiPlanol;
            l_Parametres.Estat = l_Salo.Estat;
            Intent l_editem = new Intent(this, salons_client_modificar.class);
            l_editem.putExtra("SaloClient", l_Parametres);
            startActivityForResult(l_editem, g_RQC_SALO_CLIENT_MODIFIQUEM);
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWSalonsClientIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaSalo;
        SaloClient l_Salo;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaSalo = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem (modifiquem el seu estat)
            l_Salo = (SaloClient)l_LiniaSalo.getTag();
            if (DAOSalonsClient.Esborrar(l_Salo.Codi, Jo, false)){
                DAOSalonsClient.Llegir(g_LVW_SalonsClient, R.layout.linia_lvw_llista_salons_client, Jo);
            }
        }
        else {
            g_EstatEsborrar = true;
            // Adaptem la linia per fer la baixa.
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton) l_view;
            l_IMB_Esborrar.setImageResource(R.drawable.ic_check_white_48dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton)l_LiniaSalo.findViewById(R.id.LiniaLVWLlistaSalonsClientIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaSalo.getBackground();
            l_transition.startTransition(500);
        }
    }
}
