package com.example.it00046.bodina3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Params.PAREntitat;


public class ac_entitat_detall extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView lTextNomEntitat, lTextAdresaEntitat, lTextContacteEntitat,
                 lTextTelefonEntitat, lTexteMailEntitat;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_detall);

        // Metode tradicional
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("new_variable_name");
        }

        // Amb classes
        PAREntitat dadesEntitat = (PAREntitat) getIntent().getSerializableExtra("Info");

        // Ara mostrem la info, primer recuperem els camps
        lTextNomEntitat = (TextView) findViewById(R.id.txtNomEntitat);
        lTextNomEntitat.setText(dadesEntitat.Nom);
        lTextAdresaEntitat = (TextView) findViewById(R.id.txtAdresaEntitat);
        lTextAdresaEntitat.setText(dadesEntitat.Adresa);
        lTextContacteEntitat = (TextView) findViewById(R.id.txtContacteEntitat);
        lTextContacteEntitat.setText(dadesEntitat.Contacte);
        lTextTelefonEntitat = (TextView) findViewById(R.id.txtTelefonEntitat);
        lTextTelefonEntitat.setText(dadesEntitat.Telefon);
        lTexteMailEntitat = (TextView) findViewById(R.id.txteMailEntitat);
        lTexteMailEntitat.setText(dadesEntitat.eMail);
        /*

        // send where details is object
        ClassName details = new ClassName();
        Intent i = new Intent(context, EditActivity.class);
        i.putExtra("Editing", details);
        startActivity(i);


        //receive
        ClassName model = (ClassName) getIntent().getSerializableExtra("Editing");

        And

        Class ClassName implements Serializable {}
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mn_entitat_detall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
