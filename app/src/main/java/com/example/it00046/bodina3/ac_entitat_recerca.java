package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.DAO.SQLEntitatsDAO;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.SpinnerClasses.SpnEntitat;
import com.example.it00046.bodina3.Classes.Tipus.Entitat;
import com.example.it00046.bodina3.Classes.params.PAREntitat;

import org.json.JSONArray;


public class ac_entitat_recerca extends Activity {

    private TextView resultText;
    ListView searchResults;
    private int l_Posicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_entitat_recerca);

        //resultText = (TextView)findViewById(R.id.searchViewResult);
        Globals.g_Recerca = this;
        setupSearchView();

        searchResults.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                return true;
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // Apuntem en quina linia estem (per si desprès l'usuari selecciona l'entitat)
                l_Posicio = position;
                View toolbar = view.findViewById(R.id.toolbar);
                // Definim l'animació del item
                ExpandAnimation expandAni = new ExpandAnimation(toolbar, 100);
                toolbar.startAnimation(expandAni);
            }

        });
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchResults = (ListView) findViewById(R.id.listview_search);
        // Events del searchview
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Recerquem a partir de 3 caracters
                if (newText.length() > 3) {
                    searchResults.setVisibility(View.VISIBLE);
                    SQLEntitatsDAO.F_LlistaEntitats("", searchResults);
                } else {
                    searchResults.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }
    // Aquesta funció es cridada pels elements de la llista quan seleccionem un element de la
    // llista
    public void btnEntitatRecerca_Acceptar(View view) {
        // Retornem les dades de l'entitat seleccionada gracies a l_Posicio
        Intent resultIntent = new Intent();
        // Fem servir PAREntitat perque es serializable
        PAREntitat l_Parametre = new PAREntitat();
        Entitat l_Entitat = (Entitat)searchResults.getItemAtPosition(l_Posicio);
        // Carreguem les dades
        l_Parametre.Codi = l_Entitat.Codi;
        l_Parametre.Nom = l_Entitat.Nom;
        resultIntent.putExtra("Seleccio", l_Parametre);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
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
    /*

    //     Codi d'exemple per recercar en els contactes....

    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            String displayName = getDisplayNameForContact(intent);
            //resultText.setText(displayName);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //resultText.setText("should search for query: '" + query + "'...");
        }
    }

    private String getDisplayNameForContact(Intent intent) {
        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
        phoneCursor.moveToFirst();
        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String name = phoneCursor.getString(idDisplayName);
        phoneCursor.close();
        return name;
    }
    */
}
