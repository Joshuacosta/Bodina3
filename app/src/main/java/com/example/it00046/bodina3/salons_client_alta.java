package com.example.it00046.bodina3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it00046.bodina3.Classes.CanvasSimple;
import com.example.it00046.bodina3.Classes.DAO.DAOSalonsClient;
import com.example.it00046.bodina3.Classes.Entitats.SaloClient;
import com.example.it00046.bodina3.Classes.Globals;

import java.util.Comparator;

/**
 * Created by it00046 on 29/09/2015.
 */
public class salons_client_alta extends ActionBarActivity {
    private CanvasSimple g_Canvas;
    private int g_Amplada = 0, g_Alsada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SeekBar l_SEK_Amplada, l_SEK_Alsada;
        final TextView l_TXT_Amplada, l_TXT_Alsada;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salons_client_alta);
        // Recuperem els controls
        l_SEK_Amplada = (SeekBar) findViewById(R.id.SalonsClientAltaSEKAmplada);
        l_SEK_Alsada = (SeekBar) findViewById(R.id.SalonsClientAltaSEKAlsada);
        l_TXT_Amplada = (TextView) findViewById(R.id.SalonsClientAltaLITAmplada);
        l_TXT_Alsada = (TextView) findViewById(R.id.SalonsClientAltaLITAlsada);
        g_Canvas = (CanvasSimple) findViewById(R.id.SalonsClientAltaVIWDrawing);
        // Codi de les seekbars
        l_SEK_Amplada.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int l_AmpladaTriada = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                l_AmpladaTriada = progresValue;
                g_Amplada = l_AmpladaTriada;
                l_TXT_Amplada.setText(l_AmpladaTriada + " " + Globals.g_Native.getString(R.string.meters));
                if (g_Alsada != 0){
                    g_Canvas.Dibuixa(g_Amplada * 5, g_Alsada * 5);
                }
                else{
                    // Pintem una linia
                    g_Canvas.Dibuixa(g_Amplada * 5, 5);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //l_TXT_Amplada.setText(seekBar.getMax());
                g_Amplada = l_AmpladaTriada;
            }
        });
        l_SEK_Alsada.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int l_AlsadaTriada = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                l_AlsadaTriada = progresValue;
                g_Alsada = l_AlsadaTriada;
                l_TXT_Alsada.setText(l_AlsadaTriada + " " + Globals.g_Native.getString(R.string.meters));
                if (g_Amplada != 0) {
                    g_Canvas.Dibuixa(g_Amplada * 5, g_Alsada * 5);
                }
                else{
                    // Pintem una linia
                    g_Canvas.Dibuixa(5, g_Alsada * 5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                g_Alsada = l_AlsadaTriada;
            }
        });
        // Mostrem el tornar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salons_client_alta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        return super.onOptionsItemSelected(p_Item);
    }

}
