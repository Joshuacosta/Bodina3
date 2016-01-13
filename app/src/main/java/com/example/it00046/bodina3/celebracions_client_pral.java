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
import com.example.it00046.bodina3.Classes.DAO.DAOCelebracionsClient;
import com.example.it00046.bodina3.Classes.Entitats.CelebracioClient;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARCelebracioClient;
import com.melnykov.fab.FloatingActionButton;
import java.util.Comparator;

/**
 * Created by it00046 on 25/09/2015.
 */
public class celebracions_client_pral extends ActionBarActivity {
    private ListView g_LVW_CelebracionsClient;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
    private CelebracioClient g_CelebracioClientAnterior;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static final int g_RQC_CELEBRACIO_CLIENT_ALTA = 1, g_RQC_CELEBRACIO_CLIENT_MODIFIQUEM = 2;
    private AlertDialog.Builder g_alertDialogBuilder;
    private int g_OpcioOrdenacio = -1;

    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_CelebracioClient;

        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.celebracions_client_pral);
        // Carreguen les entitats del client
        g_LVW_CelebracionsClient = (ListView) findViewById(R.id.celebracions_clientLVWCelebracions);
        DAOCelebracionsClient.Llegir(g_LVW_CelebracionsClient, R.layout.linia_lvw_llista_celebracions_client, Jo);
        l_FLB_CelebracioClient = (FloatingActionButton) findViewById(R.id.celebracions_clientFLBAltaCelebracio);
        l_FLB_CelebracioClient.attachToListView(g_LVW_CelebracionsClient);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_CelebracioClient.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent l_Intent;

                arg0.startAnimation(l_Animacio);
                // Obrim la finestra de alta
                l_Intent = new Intent(Jo, celebracions_client_mant.class);
                startActivityForResult(l_Intent, g_RQC_CELEBRACIO_CLIENT_ALTA);
            }
        });
        //
        g_LVW_CelebracionsClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;
                CelebracioClient l_CelebracioClient;

                l_CelebracioClient = (CelebracioClient) p_view.getTag();
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
                        if (g_CelebracioClientAnterior.Estat == Globals.k_CelebracioClientActiva) {
                            g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                            g_IMB_Editar.setVisibility(View.INVISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                            g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                        }
                    }
                    // Definim l'animacio de la linia
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Animacio de botons SI CORRESPON (en funcio de l'estat)
                    if (l_CelebracioClient.Estat == Globals.k_CelebracioClientActiva) {
                        l_IMB_Esborrar.setVisibility(View.VISIBLE);
                        l_IMB_Editar.setVisibility(View.VISIBLE);
                        l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                        l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                    }
                    // Apuntem lo que hem tocat
                    g_Posicio = p_position;
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                    g_CelebracioClientAnterior = l_CelebracioClient;
                }
                else {
                    // Ens tornen a marcar
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Amaguem/mostrem els botons
                    if (l_CelebracioClient.Estat == Globals.k_CelebracioClientActiva) {
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
        final String[] l_Ordres = Globals.g_Native.getResources().getStringArray(R.array.OrdreCelebracionsClient);
        // Funcions de comparacio
        final Comparator<CelebracioClient> ComparaDescripcio = new Comparator<CelebracioClient>() {
            public int compare(CelebracioClient p_a1, CelebracioClient p_a2) {
                return p_a1.Descripcio.compareToIgnoreCase(p_a2.Descripcio);
            }
        };
        final Comparator<CelebracioClient> ComparaData = new Comparator<CelebracioClient>() {
            public int compare(CelebracioClient p_a1, CelebracioClient p_a2) {
                //return Globals.StringToDate(p_a1.Data).compareTo(Globals.StringToDate(p_a2.Data));
                return Math.round(p_a2.Data - p_a1.Data);
            }
        };
        final Comparator<CelebracioClient> ComparaConvidats = new Comparator<CelebracioClient>() {
            public int compare(CelebracioClient p_a1, CelebracioClient p_a2) {
                return p_a2.Convidats - p_a1.Convidats;
            }
        };
        final Comparator<CelebracioClient> ComparaEstat = new Comparator<CelebracioClient>() {
            public int compare(CelebracioClient p_a1, CelebracioClient p_a2) {
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
                            case 0:// Per descripcio
                                ((ArrayAdapter<CelebracioClient>) g_LVW_CelebracionsClient.getAdapter()).sort(ComparaDescripcio);
                                break;
                            case 1:// Per data
                                ((ArrayAdapter<CelebracioClient>) g_LVW_CelebracionsClient.getAdapter()).sort(ComparaData);
                                break;
                            case 2:// Per numero de convidats
                                ((ArrayAdapter<CelebracioClient>) g_LVW_CelebracionsClient.getAdapter()).sort(ComparaConvidats);
                                break;
                            case 3:// Per estat
                                ((ArrayAdapter<CelebracioClient>) g_LVW_CelebracionsClient.getAdapter()).sort(ComparaEstat);
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
        getMenuInflater().inflate(R.menu.celebracions_client_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id) {
            case R.id.celebracions_client_pralMNUAltaCelebracio:
                l_Intent = new Intent(this, celebracions_client_mant.class);
                startActivityForResult(l_Intent, g_RQC_CELEBRACIO_CLIENT_ALTA);
                return true;
            case R.id.celebracions_client_pralMNUOrdenar:
                // create alert dialog
                AlertDialog l_alertDialog = g_alertDialogBuilder.create();
                // show it
                l_alertDialog.show();
                return true;
            case R.id.celebracions_client_pralMNUSalons:
                l_Intent = new Intent(this, salons_client_pral.class);
                startActivity(l_Intent);
                return true;
            case R.id.celebracions_client_pralMNUTaules:
                l_Intent = new Intent(this, taules_client.class);
                startActivity(l_Intent);
                return true;
            case R.id.celebracions_client_pralMNUActualitzar:
                DAOCelebracionsClient.Llegir(g_LVW_CelebracionsClient, R.layout.linia_lvw_llista_celebracions_client, Jo);
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
            case (g_RQC_CELEBRACIO_CLIENT_ALTA): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOCelebracionsClient.Llegir(g_LVW_CelebracionsClient, R.layout.linia_lvw_llista_celebracions_client, Jo);
                }
                break;
            }
            case (g_RQC_CELEBRACIO_CLIENT_MODIFIQUEM):{
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes modificar la entitat)
                    DAOCelebracionsClient.Llegir(g_LVW_CelebracionsClient, R.layout.linia_lvw_llista_celebracions_client, Jo);
                }
                break;
            }
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWCelebracionsClientIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaCelebracio;
        PARCelebracioClient l_Parametres;
        CelebracioClient l_Celebracio;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaCelebracio = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaCelebracio.findViewById(R.id.LiniaLVWLlistaCelebracionsClientIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaCelebracio.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio de la celebracio
            l_Parametres = new PARCelebracioClient();
            l_Celebracio = (CelebracioClient)l_LiniaCelebracio.getTag();
            l_Parametres.Codi = l_Celebracio.Codi;
            l_Parametres.CodiSalo = l_Celebracio.Salo.Codi;
            l_Parametres.Tipus = l_Celebracio.Tipus.Codi;
            l_Parametres.Descripcio = l_Celebracio.Descripcio;
            l_Parametres.Convidats = l_Celebracio.Convidats;
            l_Parametres.Data = l_Celebracio.Data;
            l_Parametres.Hora = l_Celebracio.Hora;
            l_Parametres.Lloc = l_Celebracio.Lloc;
            l_Parametres.Contacte = l_Celebracio.Contacte;
            l_Parametres.Estat = l_Celebracio.Estat;
            Intent l_editem = new Intent(this, celebracions_client_mant.class);
            l_editem.putExtra("CelebracioClient", l_Parametres);
            startActivityForResult(l_editem, g_RQC_CELEBRACIO_CLIENT_MODIFIQUEM);
        }
    }
    // Aquesta funcio es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWCelebracionsClientIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaCelebracio;
        CelebracioClient l_Celebracio;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaCelebracio = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem (modifiquem el seu estat)
            l_Celebracio = (CelebracioClient)l_LiniaCelebracio.getTag();
            if (DAOCelebracionsClient.Esborrar(l_Celebracio.Codi, Jo, false)){
                DAOCelebracionsClient.Llegir(g_LVW_CelebracionsClient, R.layout.linia_lvw_llista_celebracions_client, Jo);
            }
        }
        else {
            g_EstatEsborrar = true;
            // Adaptem la linia per fer la baixa.
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton) l_view;
            l_IMB_Esborrar.setImageResource(R.drawable.ic_check_white_48dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton)l_LiniaCelebracio.findViewById(R.id.LiniaLVWLlistaCelebracionsClientIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaCelebracio.getBackground();
            l_transition.startTransition(500);
        }
    }
}
