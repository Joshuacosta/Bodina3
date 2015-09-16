package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.it00046.bodina3.Classes.Custom.LVWRecercaEntitats;
import com.example.it00046.bodina3.Classes.DAO.DAOEntitats;
import com.example.it00046.bodina3.Classes.Entitats.Entitat;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PAREntitat;


public class entitat_recerca extends ActionBarActivity{

    private ListView g_LVW_searchResults;
    private View g_LIN_ToolbarAnterior = null;
    private TextView g_TXT_NomAnterior = null;
    private ImageView g_IMB_Acceptar = null;
    private Spinner g_SPN_Paissos;
    private int g_Posicio = -1;
    private Context Jo = this;
    private Entitat g_Entitat;

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
                ExpandAnimation l_expandAni;

                if (g_Posicio != p_position) {
                    // Desmarquem el que hi havia marcat
                    if (g_TXT_NomAnterior != null) {
                        // Recuperem color i amaguem seleccio
                        g_TXT_NomAnterior.setBackgroundResource(R.color.blue);
                        g_IMB_Acceptar.setVisibility(View.GONE);
                        // El colapsem
                        l_expandAni = new ExpandAnimation(g_LIN_ToolbarAnterior, 100);
                        g_LIN_ToolbarAnterior.startAnimation(l_expandAni);
                    }
                    // Marquem el actual i modifiquem el color de fons de la linia en funcio de si es pot agafar o no
                    // (hi ha entitats que no es poden solicitar, que treballen amb invitació)
                    l_TXT_Nom = (TextView) p_view.findViewById(R.id.LiniaLVWRecercaEntitatsNom);
                    l_IMB_Acceptar = (ImageView) p_view.findViewById(R.id.LiniaLVWRecercaEntitatsIMBAcceptar);
                    // Recuperem la informació de si permet solicitar del tag que hem definit
                    g_Entitat = (Entitat)p_view.getTag();
                    if (g_Entitat.TipusContacte == Globals.k_Entitat_PermetSolicitar) {
                        l_IMB_Acceptar.setVisibility(View.VISIBLE);
                        l_TXT_Nom.setBackgroundResource(R.color.green);
                    }
                    else {
                        l_TXT_Nom.setBackgroundResource(R.color.red);
                    }
                    // Apuntem en quina linia estem (per si desprès l'usuari selecciona l'entitat)
                    g_Posicio = p_position;
                    l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWRecercaEntitatsLINToolbar);
                    // Definim l'animació del item
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Apuntem tot lo "anterior"
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_TXT_NomAnterior = l_TXT_Nom;
                    g_IMB_Acceptar = l_IMB_Acceptar;
                }
            }

        });
    }

    // SetUp de la recerca
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
                    DAOEntitats.Recercar(newText, g_SPN_Paissos.getSelectedItem().toString(), g_LVW_searchResults, Jo, R.layout.linia_lvw_recerca_entitats);
                }
                else {
                    g_LVW_searchResults.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    // Aquesta funció es cridada pels elements de la llista quan premem el boto acceptar
    public void LiniaLVWRecercaEntitatsIMBAcceptar_Click(View view) {
        // Ens seleccionan
        Intent l_resultIntent = new Intent();
        int l_Parametre = g_Posicio + 1;
        PAREntitat l_Entitat = new PAREntitat();

        // Carreguem les dades de la entitat que ens interesen
        l_Entitat.Codi = g_Entitat.Codi;
        l_Entitat.Nom = g_Entitat.Nom;
        l_Entitat.Pais = g_Entitat.Pais;
        l_Entitat.eMail = g_Entitat.eMail;
        l_Entitat.Contacte = g_Entitat.Contacte;
        l_Entitat.Estat = g_Entitat.Estat;
        l_Entitat.Telefon = g_Entitat.Telefon;
        l_Entitat.Adresa = g_Entitat.Adresa;
        l_Entitat.TipusContacte = g_Entitat.TipusContacte;
        l_resultIntent.putExtra("Entitat", l_Entitat);
        // Tornem
        setResult(Activity.RESULT_OK, l_resultIntent);
        finish();
    }

    // Boto de trucada
    public void LiniaLVWRecercaEntitatsIMBTrucar_Click(View p_view) {
        Intent l_callIntent = new Intent(Intent.ACTION_DIAL);
        TextView l_TXT_Telefon;
        String l_NumTelefon;
        View l_parent;

        // Recuperem el telefon
        l_parent = (View)p_view.getParent();
        if (l_parent != null) {
            l_TXT_Telefon = (TextView) l_parent.findViewById(R.id.LiniaLVWRecercaEntitatsTXTTelefon);
            l_NumTelefon = l_TXT_Telefon.getText().toString();
            l_callIntent.setData(Uri.parse("tel:" + l_NumTelefon));
            startActivity(l_callIntent);
        }
    }
    // Boto de eMail
    public void LiniaLVWRecercaEntitatsIMBeMail_Click(View p_view) {
        Intent l_Intent;
        TextView l_TXT_eMail;
        View l_parent;

        // Recuperem el eMail
        l_parent = (View)p_view.getParent();
        if (l_parent != null) {
            l_TXT_eMail = (TextView) l_parent.findViewById(R.id.LiniaLVWRecercaEntitatsTXTeMail);
            l_Intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", l_TXT_eMail.getText().toString(), null));
            l_Intent.putExtra(Intent.EXTRA_SUBJECT, Globals.g_Native.getString(R.string.Subject_Invitació));
            startActivity(Intent.createChooser(l_Intent, Globals.g_Native.getString(R.string.Tria_Mail)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ArrayAdapter<CharSequence> l_adapter_Pais;
        int l_spinnerPosition;
        MenuItem l_item;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entitat_recerca, menu);
        // Expressem el Spinner que tenim al menu
        l_item = menu.findItem(R.id.entitat_recercaSPNPaissos);
        g_SPN_Paissos = (Spinner) MenuItemCompat.getActionView(l_item);
        l_adapter_Pais = ArrayAdapter.createFromResource(this, R.array.Paisos, R.layout.linia_spn_defecte_white);
        l_adapter_Pais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //l_adapter_Pais.setDropDownViewResource(R.layout.linia_spn_defecte_white);
        g_SPN_Paissos.setAdapter(l_adapter_Pais);
        // Seleccionem el nostre pais en el spinner
        l_spinnerPosition = l_adapter_Pais.getPosition(Globals.g_Client.Pais);
        g_SPN_Paissos.setSelection(l_spinnerPosition);
        //g_SPN_Paissos.setBackgroundColor(Globals.g_Native.getResources().getColor(R.color.white));
        return true;
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
