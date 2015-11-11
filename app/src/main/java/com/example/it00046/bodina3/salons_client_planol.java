package com.example.it00046.bodina3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SimpleDrawView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by it00046 on 05/10/2015.
 */
public class salons_client_planol  extends ActionBarActivity {
    Context Jo = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton l_FLB_Texte, l_FLB_Curva,l_FLB_Recta;
        FloatingActionButton l_FLB_Quadricula, l_FLB_Escala,l_FLB_Unitats;
        final FloatingActionMenu l_FLM_Eines, l_FLM_Eines2;
        final SimpleDrawView l_Draw;
        ImageButton l_IMB_Esborrar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salons_client_planol);
        // Recuperem controls
        l_Draw = (SimpleDrawView) findViewById(R.id.SalonsClientPlanolVIWDrawing);
        l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.recta;
        //
        l_Draw.g_Pare = Jo;
        l_IMB_Esborrar = (ImageButton) findViewById(R.id.salonsClientPlanolIMBEsborrar);
        l_Draw.g_IMB_Esborrar = l_IMB_Esborrar;
        // Accions dels FLB
        l_FLM_Eines = (FloatingActionMenu) findViewById(R.id.salonsClientPlanolFLMEines);
        l_FLM_Eines2 = (FloatingActionMenu) findViewById(R.id.salonsClientPlanolFLMEines2);
        l_FLM_Eines.setOnMenuButtonClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_FLM_Eines2.close(true);
                l_FLM_Eines.toggle(true);
            }
        });
        l_FLM_Eines2.setOnMenuButtonClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_FLM_Eines.close(true);
                l_FLM_Eines2.toggle(true);
            }
        });
        // Sub-menus
        l_FLB_Texte = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBTexte);
        l_FLB_Texte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.texte;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_format_color_text_white_24dp));
                l_FLM_Eines.close(true);
            }
        });
        l_FLB_Recta = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBRecta);
        l_FLB_Recta.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.recta;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_border_color_white_24dp));
                l_FLM_Eines.close(true);
            }
        });
        l_FLB_Curva = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBCurva);
        l_FLB_Curva.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.curva;
                // Cambien el icon de eines
                l_FLM_Eines.getMenuIconView().setImageDrawable(Globals.g_Native.getResources().getDrawable(R.drawable.ic_gesture_white_24dp));
                l_FLM_Eines.close(true);
            }
        });
        // Sub-menus 2
        l_FLB_Quadricula = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBQuadricula);
        l_FLB_Quadricula.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.Quadricula();
                l_FLM_Eines2.close(true);
            }
        });
        l_FLB_Escala = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBEscala);
        l_FLB_Escala.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_FLM_Eines2.close(true);
            }
        });
        l_FLB_Unitats = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBUnitats);
        l_FLB_Unitats.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_FLM_Eines2.close(true);
            }
        });
        // Boto/Imatge de esborrar
        l_IMB_Esborrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Hauriem de preguntar si ho fem o no
                l_Draw.EsborrarPlanol();
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

        return super.onOptionsItemSelected(p_Item);
    }

}
