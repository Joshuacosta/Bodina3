package com.example.it00046.bodina3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.CanvasManual;
import com.example.it00046.bodina3.Classes.DAO.DAOTipusCelebracions;
import com.example.it00046.bodina3.Classes.Entitats.TipusCelebracio;
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
        TextView l_TXT_Distancia;
        FloatingActionButton l_FLB_Texte, l_FLB_Curva,l_FLB_Recta;
        final FloatingActionMenu l_FLM_Eines;
        final SimpleDrawView l_Draw;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salons_client_planol);
        // Recuperem controls
        // Pasem el control de distancia al Draw
        l_TXT_Distancia = (TextView) findViewById(R.id.SalonsClientPlanolTXTDistancia);
        //
        l_Draw = (SimpleDrawView) findViewById(R.id.SalonsClientPlanolVIWDrawing);
        l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.texte;
        l_Draw.DefinimMetres(l_TXT_Distancia);
        //
        l_Draw.pare = Jo;
        //SimpleDrawView.DefinimMetres(l_TXT_Distancia); Quin metode es millor?
        // Crec que son lo mateix JA QUE DefinimMetres es static!
        // Accions dels FLB
        l_FLM_Eines = (FloatingActionMenu) findViewById(R.id.salonsClientPlanolFLMEines);
        //
        l_FLB_Texte = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBTexte);
        l_FLB_Texte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final EditText l_input = new EditText(Jo);

                // Mostrem una finestra per demanar el texte a introduir
                AlertDialog.Builder g_alertDialogBuilder = new AlertDialog.Builder(Jo);
                g_alertDialogBuilder.setTitle(Globals.g_Native.getString(R.string.SalonsClientPlanolTITAddTexte));
                g_alertDialogBuilder.setView(l_input);
                g_alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(Globals.g_Native.getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p_dialog, int which) {
                                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.texte;
                                l_Draw.g_NouTexte = l_input.getText().toString();
                                //SimpleDrawView.g_NouTexte = l_input.getText().toString(); No se quina opcio es millor
                                l_Draw.EscriuTexte(l_Draw.g_NouTexte);
                                l_FLM_Eines.close(true);
                            }
                        })
                        .setNegativeButton(Globals.g_Native.getString(R.string.boto_Cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface p_dialog, int p_id) {
                                l_FLM_Eines.close(true);
                            }
                        });
                g_alertDialogBuilder.show();
            }
        });
        l_FLB_Recta = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBRecta);
        l_FLB_Recta.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.recta;
            }
        });
        l_FLB_Curva = (FloatingActionButton) findViewById(R.id.salonsClientPlanolFLBCurva);
        l_FLB_Curva.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                l_Draw.g_ModusDibuix = SimpleDrawView.g_Modus.curva;
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

    public void ModificarTexte(String P_Texte){
        Log.d("BODINA-Draw", "-----> Escribim " + P_Texte);
    }
}
