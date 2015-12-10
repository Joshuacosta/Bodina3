package com.example.it00046.bodina3.Classes.Entitats;

import android.content.Context;

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
    public Invitacio(){
    }
}
