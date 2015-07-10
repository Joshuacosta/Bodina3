package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Custom.LVWRecercaEntitats;
import com.example.it00046.bodina3.Classes.DAO.DAOEntitats;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;


public class entitat_recerca extends Activity {

    private ListView g_LVW_searchResults;
    private View g_LIN_ToolbarAnterior = null;
    private TextView g_TXT_NomAnterior = null;
    private ImageView g_IMB_Acceptar = null;
    private int g_Posicio = -1;
    private Context Jo = this;

    @Override
    protected void onCreate(Bundle p_savedInstanceState) {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.entitat_recerca);

        setupSearchView();

        g_LVW_searchResults.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                return true;
            }
        });

        g_LVW_searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p_parent, final View p_view,
                                    int p_position, long p_id) {
                ImageView l_IMB_Acceptar;
                TextView l_TXT_Nom;
                View l_LIN_Toolbar;

                if (g_Posicio != p_position) {
                    // Desmarquem el que hi havia marcat
                    if (g_TXT_NomAnterior != null) {
                        // Recuperem color i amaguem seleccio
                        g_TXT_NomAnterior.setBackgroundResource(R.color.blue);
                        g_IMB_Acceptar.setVisibility(View.GONE);
                        // El colapsem
                        ((LinearLayout.LayoutParams) g_LIN_ToolbarAnterior.getLayoutParams()).bottomMargin = -80;
                        g_LIN_ToolbarAnterior.setVisibility(View.GONE);
                    }
                    // Marquem el actual
                    // Modifiquem el color de fons de la linia en funcio de si es pot agafar o no
                    // (hi ha entitats que no es poden solicitar)
                    l_TXT_Nom = (TextView) p_view.findViewById(R.id.LiniaLVWRecercaEntitatsNom);
                    l_IMB_Acceptar = (ImageView) p_view.findViewById(R.id.LiniaLVWRecercaEntitatsIMBAcceptar);
                    if (p_view.getTag() == Globals.k_Entitat_PermetSolicitar) {
                        l_IMB_Acceptar.setVisibility(View.VISIBLE);
                        l_TXT_Nom.setBackgroundResource(R.color.green);
                    }
                    else{
                        l_TXT_Nom.setBackgroundResource(R.color.red);
                    }
                    // Apuntem en quina linia estem (per si desprès l'usuari selecciona l'entitat)
                    g_Posicio = p_position;
                    l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWRecercaEntitatsLINToolbar);
                    // Definim l'animació del item
                    ExpandAnimation l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Apuntem tot lo "anterior"
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_TXT_NomAnterior = l_TXT_Nom;
                    g_IMB_Acceptar = l_IMB_Acceptar;
                }
            }

        });
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.entitat_recercaSVW);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        g_LVW_searchResults = (ListView) findViewById(R.id.entitat_recercaLVW);
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
                    g_LVW_searchResults.setVisibility(View.VISIBLE);
                    DAOEntitats.Llegir("", g_LVW_searchResults, Jo, R.layout.linia_lvw_recerca_entitats);
                } else {
                    g_LVW_searchResults.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }
    // Aquesta funció es cridada pels elements de la llista quan seleccionem un element de la
    // llista
    public void btnEntitatRecerca_Acceptar(View view) {
        // Retornem les dades de l'entitat seleccionada gracies a l_Posicio
        Intent l_resultIntent = new Intent();
        int l_Parametre = g_Posicio +1;

        l_resultIntent.putExtra("Seleccio", l_Parametre);
        setResult(Activity.RESULT_OK, l_resultIntent);
        this.finish();
    }

    public void LiniaLVWRecercaEntitatsIMBAcceptar_Click(View view) {
        // Ens seleccionan
        Intent l_resultIntent = new Intent();
        int l_Parametre = g_Posicio + 1;

        l_resultIntent.putExtra("Seleccio", l_Parametre);
        setResult(Activity.RESULT_OK, l_resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int l_id = p_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (l_id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(p_item);
    }
    /*

    //     Codi d'exemple per recercar en els contactes....

    private TextView resultText;

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
