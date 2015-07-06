package com.example.it00046.bodina3.Classes.Entitats;

import com.example.it00046.bodina3.Classes.Globals;
import com.example.it00046.bodina3.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by it00046 on 02/07/2015.
 */
public class Invitacio {
    public Entitat entitat = new Entitat();
    public String DataInvitacio = new String();
    public String DataResolucio = new String();
    public int Estat;

    //
    private static final String TAG_CodiEntitat = Globals.g_Native.getString(R.string.TInvitacions_CodiEntitat);
    private static final String TAG_NomEntitat = Globals.g_Native.getString(R.string.TInvitacions_NomEntitat);
    private static final String TAG_eMailEntitat = Globals.g_Native.getString(R.string.TInvitacions_eMailEntitat);
    private static final String TAG_PaisEntitat = Globals.g_Native.getString(R.string.TInvitacions_PaisEntitat);
    private static final String TAG_AdresaEntitat = Globals.g_Native.getString(R.string.TInvitacions_AdresaEntitat);
    private static final String TAG_ContacteEntitat = Globals.g_Native.getString(R.string.TInvitacions_ContacteEntitat);
    private static final String TAG_TelefonEntitat = Globals.g_Native.getString(R.string.TInvitacions_TelefonEntitat);
    private static final String TAG_EstatEntitat = Globals.g_Native.getString(R.string.TInvitacions_EstatEntitat);
    private static final String TAG_DataInvitacio = Globals.g_Native.getString(R.string.TInvitacions_DataInvitacio);
    private static final String TAG_DataResolucio = Globals.g_Native.getString(R.string.TInvitacions_DataResolucio);
    private static final String TAG_Estat = Globals.g_Native.getString(R.string.TInvitacions_Estat);
    //
    public void Invitacions(){
    }
    //
    private static Invitacio JSONToInvitacio(JSONObject p_Invitacio){
        Invitacio l_Invitacio = new Invitacio();

        try {
            l_Invitacio.entitat.Codi = p_Invitacio.getString(TAG_CodiEntitat);
            l_Invitacio.entitat.Nom = p_Invitacio.getString(TAG_NomEntitat);
            l_Invitacio.entitat.eMail = p_Invitacio.getString(TAG_eMailEntitat);
            l_Invitacio.entitat.Pais = p_Invitacio.getString(TAG_PaisEntitat);
            l_Invitacio.entitat.Adresa = p_Invitacio.getString(TAG_AdresaEntitat);
            l_Invitacio.entitat.Contacte = p_Invitacio.getString(TAG_ContacteEntitat);
            l_Invitacio.entitat.Telefon = p_Invitacio.getString(TAG_TelefonEntitat);
            l_Invitacio.entitat.Estat = p_Invitacio.getInt(TAG_EstatEntitat);
            l_Invitacio.DataInvitacio = p_Invitacio.getString(TAG_DataInvitacio);
            l_Invitacio.DataResolucio = p_Invitacio.getString(TAG_DataResolucio);
            l_Invitacio.Estat = p_Invitacio.getInt(TAG_Estat);
        }
        catch (JSONException e) {
            Globals.F_Alert(Globals.g_Native.getString(R.string.errorservidor_ProgramError),
                    Globals.g_Native.getString(R.string.error_greu));
        }
        return l_Invitacio;
    }
}
