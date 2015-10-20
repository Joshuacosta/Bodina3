package com.example.it00046.bodina3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
//import android.provider.Contacts;
import android.provider.ContactsContract;
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
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.it00046.bodina3.Classes.DAO.DAOAssociacions;
import com.example.it00046.bodina3.Classes.Entitats.Associacio;
import com.example.it00046.bodina3.Classes.ExpandAnimation;
import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.Classes.Params.PARAssociacio;
import com.melnykov.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionMenu;

import java.util.Comparator;


public class entitat_pral extends ActionBarActivity {
    private ListView g_LVW_Associacions;
    private int g_Posicio = -1;
    private View g_LIN_ToolbarAnterior = null;
    private Associacio g_AssociacioAnterior;
    private ImageButton g_IMB_Esborrar = null, g_IMB_Editar = null, g_IMB_AfegirEntitat = null;
    private Context Jo = this;
    private Boolean g_EstatEsborrar = false;
    static final int g_RQC_ENTITAT_SOLICITEM = 1, g_RQC_ENTITAT_MODIFIQUEM = 2;
    private AlertDialog.Builder g_alertDialogBuilder;
    private int g_OpcioOrdenacio = -1;


    @Override
    protected void onCreate(Bundle l_savedInstanceState) {
        final Animation l_Animacio;
        FloatingActionButton l_FLB_Associacio;

        super.onCreate(l_savedInstanceState);
        setContentView(R.layout.entitat_pral);
        // Carreguen les entitats del client
        g_LVW_Associacions = (ListView) findViewById(R.id.entitat_pralLVWAssociacions);
        DAOAssociacions.Llegir(g_LVW_Associacions, R.layout.linia_lvw_llista_associacions, Jo);
        // El floating boto serveix per afegir associacions amb entitats (tamb� es pot fer des de el menu)
        l_FLB_Associacio = (FloatingActionButton) findViewById(R.id.entitat_pralFLBAfegirAssociacio);
        l_FLB_Associacio.attachToListView(g_LVW_Associacions);
        l_Animacio = AnimationUtils.loadAnimation(this, R.anim.alpha_parpadeig);
        l_FLB_Associacio.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent l_Intent;

                arg0.startAnimation(l_Animacio);
                // Obrim la finestra de associacio
                l_Intent = new Intent(Jo, entitat_solicitar.class);
                startActivityForResult(l_Intent, g_RQC_ENTITAT_SOLICITEM);
            }
        });
        //
        // Codi de seleccio de un element de la llista de entitats/associacions
        g_LVW_Associacions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View p_view,
                                    int p_position, long p_id) {
                View l_LIN_Toolbar;
                ImageButton l_IMB_Esborrar, l_IMB_Editar, l_IMB_AfegirEntitat;
                ExpandAnimation l_expandAni;
                final Animation l_Animacio_Amagar, l_Animacio_Mostrar;
                Associacio l_Associacio;

                //g_LiniaSeleccionada = p_view;
                // Recuperem associacio
                l_Associacio = (Associacio) p_view.getTag();
                // Preparem animacions
                l_Animacio_Amagar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_0);
                l_Animacio_Mostrar = AnimationUtils.loadAnimation(Jo, R.anim.alpha_a_1);
                //
                l_LIN_Toolbar = p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsLINToolbar);
                if (g_Posicio != p_position) {
                    l_IMB_Esborrar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEsborrar);
                    l_IMB_Editar = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEditar);
                    l_IMB_AfegirEntitat = (ImageButton) p_view.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBAfegirEntitat);
                    if (g_LIN_ToolbarAnterior != null && g_LIN_ToolbarAnterior.getVisibility() == View.VISIBLE) {
                        // Desmarquem el que hi havia marcat
                        l_expandAni = new ExpandAnimation(g_LIN_ToolbarAnterior, 100);
                        g_LIN_ToolbarAnterior.startAnimation(l_expandAni);
                        // Animacio de botons SI CORRESPON (en funcio de l'estat de la associacio)
                        if (g_AssociacioAnterior.Estat == Globals.k_AssociacioActiva || g_AssociacioAnterior.Estat == Globals.k_AssociacioPendent) {
                            g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                            g_IMB_Editar.setVisibility(View.INVISIBLE);
                            g_IMB_AfegirEntitat.setVisibility(View.INVISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                            g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                            g_IMB_AfegirEntitat.startAnimation(l_Animacio_Amagar);
                        }
                    }
                    // Definim l'animacio de la linia
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Animacio de botons SI CORRESPON (en funcio de l'estat de la associacio)
                    if (l_Associacio.Estat == Globals.k_AssociacioActiva || l_Associacio.Estat == Globals.k_AssociacioPendent) {
                        l_IMB_Esborrar.setVisibility(View.VISIBLE);
                        l_IMB_Editar.setVisibility(View.VISIBLE);
                        l_IMB_AfegirEntitat.setVisibility(View.VISIBLE);
                        l_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                        l_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                        l_IMB_AfegirEntitat.startAnimation(l_Animacio_Mostrar);
                    }
                    // Apuntem lo que hem tocat
                    g_Posicio = p_position;
                    g_LIN_ToolbarAnterior = l_LIN_Toolbar;
                    g_IMB_Esborrar = l_IMB_Esborrar;
                    g_IMB_Editar = l_IMB_Editar;
                    g_IMB_AfegirEntitat = l_IMB_AfegirEntitat;
                    g_AssociacioAnterior = l_Associacio;
                }
                else {
                    // Ens tornen a marcar
                    l_expandAni = new ExpandAnimation(l_LIN_Toolbar, 100);
                    l_LIN_Toolbar.startAnimation(l_expandAni);
                    // Amaguem/mostrem els botons
                    // Animacio de botons SI CORRESPON (en funcio de l'estat de la associacio)
                    if (l_Associacio.Estat == Globals.k_AssociacioActiva || l_Associacio.Estat == Globals.k_AssociacioPendent) {
                        if (g_IMB_Editar.getVisibility() == View.VISIBLE) {
                            g_IMB_Esborrar.setVisibility(View.INVISIBLE);
                            g_IMB_Editar.setVisibility(View.INVISIBLE);
                            g_IMB_AfegirEntitat.setVisibility(View.INVISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Amagar);
                            g_IMB_Editar.startAnimation(l_Animacio_Amagar);
                            g_IMB_AfegirEntitat.startAnimation(l_Animacio_Amagar);
                        } else {
                            g_IMB_Esborrar.setVisibility(View.VISIBLE);
                            g_IMB_Editar.setVisibility(View.VISIBLE);
                            g_IMB_AfegirEntitat.setVisibility(View.VISIBLE);
                            g_IMB_Esborrar.startAnimation(l_Animacio_Mostrar);
                            g_IMB_Editar.startAnimation(l_Animacio_Mostrar);
                            g_IMB_AfegirEntitat.startAnimation(l_Animacio_Mostrar);
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
        final String[] l_Ordres = Globals.g_Native.getResources().getStringArray(R.array.OrdreAssociacio);
        // Funcions de comparacio
        final Comparator<Associacio> ComparaEstat = new Comparator<Associacio>() {
            public int compare(Associacio p_a1, Associacio p_a2) {
                return ((Integer)p_a1.Estat).compareTo(p_a2.Estat);
            }
        };
        final Comparator<Associacio> ComparaNom = new Comparator<Associacio>() {
            public int compare(Associacio p_a1, Associacio p_a2) {
                return p_a1.entitat.Nom.compareToIgnoreCase(p_a2.entitat.Nom);
            }
        };
        final Comparator<Associacio> ComparaDataPeticio = new Comparator<Associacio>() {
            public int compare(Associacio p_a1, Associacio p_a2) {
                return Globals.StringToDate(p_a1.DataPeticio).compareTo(Globals.StringToDate(p_a2.DataPeticio));
            }
        };
        final Comparator<Associacio> ComparaDataAlta = new Comparator<Associacio>() {
            public int compare(Associacio p_a1, Associacio p_a2) {
                return Globals.StringToDate(p_a1.DataAlta).compareTo(Globals.StringToDate(p_a2.DataAlta));
            }
        };
        // Configurem
        g_alertDialogBuilder
                .setCancelable(false)
                .setSingleChoiceItems(l_Ordres, g_OpcioOrdenacio, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1) {
                            case 0:// Per nom
                                ((ArrayAdapter<Associacio>) g_LVW_Associacions.getAdapter()).sort(ComparaNom);
                                break;
                            case 1:// Per estat
                                ((ArrayAdapter<Associacio>) g_LVW_Associacions.getAdapter()).sort(ComparaEstat);
                                break;
                            case 2:// Per data entrada
                                ((ArrayAdapter<Associacio>) g_LVW_Associacions.getAdapter()).sort(ComparaDataPeticio);
                                break;
                            case 3:// Per data alta
                                ((ArrayAdapter<Associacio>) g_LVW_Associacions.getAdapter()).sort(ComparaDataAlta);
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
        getMenuInflater().inflate(R.menu.entitat_pral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem p_Item) {
        Intent l_Intent;
        int l_id = p_Item.getItemId();

        switch (l_id) {
            case R.id.entitat_solicitarMNUDemanar:
                l_Intent = new Intent(this, entitat_solicitar.class);
                startActivityForResult(l_Intent, g_RQC_ENTITAT_SOLICITEM);
                return true;
            case R.id.entitat_solicitarMNUOrdenar:
                // create alert dialog
                AlertDialog l_alertDialog = g_alertDialogBuilder.create();
                // show it
                l_alertDialog.show();
                return true;
            case R.id.entitat_solicitarMNUActualitzar:
                DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
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
            case (g_RQC_ENTITAT_SOLICITEM): {
                if (resultCode == Activity.RESULT_OK) {
                    // Refresquem la llista (podiem ser mes optims i nomes afegir la entitat
                    // amb la que hem demanat associar-nos
                    DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
                }
                break;
            }
            case (g_RQC_ENTITAT_MODIFIQUEM):{
                if (resultCode == Activity.RESULT_OK){
                    // Refresquem la llista (podiem ser mes optims i nomes modificar la entitat)
                    DAOAssociacions.Llegir(g_LVW_Associacions,  R.layout.linia_lvw_llista_associacions, Jo);
                }
                break;
            }
        }
    }

    // Aquesta funció es cridada pels elements de la llista quan premem el boto de afegir entitat
    public void LiniaLVWAssociacionsIMBAfegirEntitat_Click(View l_view) {
        Intent l_intent;
        View l_parent, l_LiniaAssociacio;
        PARAssociacio l_Parametres;
        Associacio l_Associacio;

        // Llegim la jeraquia i recuperem dades
        l_parent = (View) l_view.getParent();
        l_LiniaAssociacio = (View) l_parent.getParent();
        l_Parametres = new PARAssociacio();
        l_Associacio = (Associacio)l_LiniaAssociacio.getTag();
        // Llancem la finestra de contactes (si ja el tenim s'obre el contacte, recerquem pel numero)
        l_intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + l_Associacio.entitat.Telefon));
        l_intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
        //l_intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        l_intent.putExtra(ContactsContract.Intents.Insert.NAME, l_Associacio.entitat.Nom);
        l_intent.putExtra(ContactsContract.Intents.Insert.PHONE, l_Associacio.entitat.Telefon);
        l_intent.putExtra(ContactsContract.Intents.Insert.EMAIL, l_Associacio.entitat.eMail);
        l_intent.putExtra(ContactsContract.Intents.Insert.COMPANY, l_Associacio.entitat.Contacte);

        startActivity(l_intent);
    }
    // Aquesta funció es cridada pels elements de la llista quan premem el boto editar
    public void LiniaLVWAssociacionsIMBEditar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar, l_IMB_AfegirEntitat;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaAssociacio;
        PARAssociacio l_Parametres;
        Associacio l_Associacio;

        // Recuperem "jerarquia"
        l_parent = (View) l_view.getParent();
        l_LiniaAssociacio = (View) l_parent.getParent();
        // Validem el estat
        if (g_EstatEsborrar) {
            // Cancelem
            g_EstatEsborrar = false;
            // Boto de afegir entitat visible
            l_IMB_AfegirEntitat = (ImageButton)l_LiniaAssociacio.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBAfegirEntitat);
            l_IMB_AfegirEntitat.setVisibility(View.VISIBLE);
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton)l_LiniaAssociacio.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEsborrar);
            l_IMB_Esborrar.setImageResource(R.drawable.ic_delete_black_36dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton) l_view;
            l_IMB_Editar.setImageResource(R.drawable.ic_mode_edit_black_36dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaAssociacio.getBackground();
            l_transition.reverseTransition(300);
            //
        }
        else {
            // Obrim la activitat de modificacio de associacio
            l_Parametres = new PARAssociacio();
            l_Associacio = (Associacio)l_LiniaAssociacio.getTag();
            l_Parametres.CodiEntitat = l_Associacio.entitat.Codi;
            l_Parametres.NomEntitat = l_Associacio.entitat.Nom;
            l_Parametres.Descripcio = l_Associacio.Descripcio;
            l_Parametres.Contacte = l_Associacio.Contacte;
            l_Parametres.eMail = l_Associacio.eMail;

            Intent l_editem = new Intent(this, entitat_modificar.class);
            l_editem.putExtra("Associacio", l_Parametres);
            startActivityForResult(l_editem, g_RQC_ENTITAT_MODIFIQUEM);
        }
    }
    // Aquesta funció es cridada pels elements de la llista quan premem el boto esborrar
    public void LiniaLVWAssociacionsIMBEsborrar_Click(View l_view) {
        ImageButton l_IMB_Esborrar, l_IMB_Editar, l_IMB_AfegirEntitat;
        TransitionDrawable l_transition;
        View l_parent, l_LiniaAssociacio;
        Associacio l_Associacio;

        // Llegim la jeraquia
        l_parent = (View) l_view.getParent();
        l_LiniaAssociacio = (View) l_parent.getParent();
        // Validem si "ja" esborrem
        if (g_EstatEsborrar){
            // Esborrem (modifiquem el seu estat)
            l_Associacio = (Associacio)l_LiniaAssociacio.getTag();
            DAOAssociacions.CancelarClient(l_Associacio, Jo, g_LVW_Associacions, R.layout.linia_lvw_llista_associacions);
            /* Aquest codi serveix per animar l'esborrat de un element de la linia, a nosaltres, en
               aquest cas, no es necessari perque no ho volem fer, es mostra vermell

               final ArrayAdapter<Associacio> l_Llista = (ArrayAdapter<Associacio>)g_LVW_Associacions.getAdapter();

            //l_Llista = (ArrayAdapter<Associacio>)g_LVW_Associacions.getAdapter();
            //l_Llista.remove(l_Llista.getItem(g_Posicio));
            //l_Llista.notifyDataSetChanged();

            final Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.esborrar_listview);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    g_LiniaSeleccionada.setHasTransientState(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    l_Llista.remove(l_Llista.getItem(g_Posicio));
                    l_Llista.notifyDataSetChanged();
                    g_LiniaSeleccionada.setHasTransientState(false);
                }
            });
            g_LiniaSeleccionada.startAnimation(animation);
            */
        }
        else {
            g_EstatEsborrar = true;
            // Adaptem la linia per fer la baixa.
            // Boto de afegir entitat invisible
            l_IMB_AfegirEntitat = (ImageButton)l_LiniaAssociacio.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBAfegirEntitat);
            l_IMB_AfegirEntitat.setVisibility(View.GONE);
            // Boto de esborrar actiu
            l_IMB_Esborrar = (ImageButton) l_view;
            l_IMB_Esborrar.setImageResource(R.drawable.ic_check_white_48dp);
            // Boto de edicio es cancelar
            l_IMB_Editar = (ImageButton)l_LiniaAssociacio.findViewById(R.id.LiniaLVWLlistaAssociacionsIMBEditar);
            l_IMB_Editar.setImageResource(R.drawable.ic_close_white_48dp);
            // Camviem fons
            l_transition = (TransitionDrawable)l_LiniaAssociacio.getBackground();
            l_transition.startTransition(500);
        }
    }
}
