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

import com.example.it00046.bodina3.Classes.DAO.DAOCategoriesConvidats;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.CategoriaConvidats;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.melnykov.fab.FloatingActionButton;
import java.util.Comparator;

public class categories_convidats extends ActionBarActivity{
    static private ListView g_LVW_CategoriesConvidats;
    private int g_Posicio = -1;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static private int g_CodiModificacio;
    static private int g_CodiCelebracio;
    static private String g_Descripcio;

    public static void MostraOperacio(final Activity p_activity, final boolean p_Alta)
    {
        final EditText l_input = new EditText(p_activity);

        AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(p_activity);
        if (p_Alta) {
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.categories_convidats_Afegir));
        }
        else{
            l_input.setText(g_Descripcio);
            g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.categories_convidats_Modificar));
        }
        g_alertDialogBuilder.setView(l_input);
        g_alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p_dialog, int which) {
                        CategoriaConvidats l_CategoriaConvidats = new CategoriaConvidats();

                        // Apuntem el codi de la celebracio amb la que treballem
                        l_CategoriaConvidats.CodiCelebracio = g_CodiCelebracio;
                        //
                        l_CategoriaConvidats.Descripcio = l_input.getText().toString();
                        if (p_Alta) {
                            // Fem la insercio i si va be refresquem la llista
                            if (DAOCategoriesConvidats.Afegir(l_CategoriaConvidats, p_activity, false, false)) {
                                DAOCategoriesConvidats.Llegir(g_CodiCelebracio, g_LVW_CategoriesConvidats, R.layout.linia_lvw_llista_categoriesconvidats, p_activity);
                            }
                        }
                        else{
                            l_CategoriaConvidats.Codi = g_CodiModificacio;
                            if (DAOCategoriesConvidats.Modificar(l_CategoriaConvidats, p_activity, false)){
                                DAOCategoriesConvidats.Llegir(g_CodiCelebracio, g_LVW_CategoriesConvidats, R.layout.linia_lvw_llista_categoriesconvidats, p_activity);
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
        FloatingActionButton l_FLB_CategoriesConvidats;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_convidats);
        // Carreguen les entitats del client
        g_LVW_CategoriesConvidats = (ListView) findViewById(R.id.categories_convidatsLVWCategories);
        DAOCategoriesConvidats.Llegir(g_CodiCelebracio, g_LVW_CategoriesConvidats, R.layout.linia_lvw_llista_categoriesconvidats, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tamb? es pot fer des de el menu)
        l_FLB_CategoriesConvidats = (FloatingActionButton) findViewById(R.id.tipus_celebracionsFLBAfegirTipus);
        l_FLB_CategoriesConvidats.attachToListView(g_LVW_CategoriesConvidats);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_CategoriesConvidats.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(l_Animacio);
                MostraOperacio(categories_convidats.this, true);
            }
        });
        //
        // Codi de seleccio de un element de la llista
        g_LVW_CategoriesConvidats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                ImageButton l_IMB_Esborrar, l_IMB_Editar;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;

                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaCategoriesConvidatsIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaCategoriesConvidatsIMBEditar);
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
        getMenuInflater().inflate(R.menu.categories_convidats, p_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        int l_id = p_Item.getItemId();

        final Comparator<CategoriaConvidats> ComparaNom = new Comparator<CategoriaConvidats>() {
            public int compare(CategoriaConvidats p_a1, CategoriaConvidats p_a2) {
                return p_a1.Descripcio.compareToIgnoreCase(p_a2.Descripcio);
            }
        };

        switch (l_id) {
            case R.id.categories_convidatsMNUAfegir:
                MostraOperacio(categories_convidats.this, true);
                return true;
            case R.id.categories_convidatsMNUOrdenar:
                ((ArrayAdapter<CategoriaConvidats>) g_LVW_CategoriesConvidats.getAdapter()).sort(ComparaNom);
                return true;
        }
        return super.onOptionsItemSelected(p_Item);
    }
    // Aquesta funci? es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWTipusCelebracionsIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaCategoriaConvidats;
        CategoriaConvidats l_CategoriaConvidats;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaCategoriaConvidats = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaCategoriaConvidats.findViewById(R.id.LiniaLVWLlistaCategoriesConvidatsIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaCategoriaConvidats.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio del tipus
            l_CategoriaConvidats = (CategoriaConvidats)l_LiniaCategoriaConvidats.getTag();
            g_CodiModificacio = l_CategoriaConvidats.Codi;
            g_CodiCelebracio = l_CategoriaConvidats.CodiCelebracio;
            g_Descripcio = l_CategoriaConvidats.Descripcio;
            MostraOperacio(categories_convidats.this, false);
        }
    }
    // Aquesta funci? es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWTipusCelebracionsIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaCategoriaConvidats;
        CategoriaConvidats l_CategoriaConvidats;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaCategoriaConvidats = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem
            l_CategoriaConvidats = (CategoriaConvidats)l_LiniaCategoriaConvidats.getTag();
            if (DAOTipusCelebracions.Esborrar(l_CategoriaConvidats.Codi, Jo, false)) {
                // Refresquem la llista
                DAOCategoriesConvidats.Llegir(l_CategoriaConvidats.CodiCelebracio, g_LVW_CategoriesConvidats, R.layout.linia_lvw_llista_categoriesconvidats, Jo);
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
            l_IMB_Editar = (ImageButton)l_LiniaCategoriaConvidats.findViewById(R.id.LiniaLVWLlistaTipusCelebracionsIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaCategoriaConvidats.getBackground();
            l_transition.startTransition(500);
        }
    }
}