package com.example.it00046.bodina3.Classes;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by Joshua on 14/02/2015.
 */
// Tema PHP
public class PHP extends AsyncTask<String, Integer, Integer> {

    public String g_FuncioPHP = new String();
    public List<NameValuePair> g_Parametres = new ArrayList<NameValuePair>();
    public Runnable g_OperacioOK = null;
    public Runnable g_OperacioKO = null;

    public PHP(List<NameValuePair> p_Parametres, Runnable p_OperacioOK,
                    Runnable p_OperacioKO){
        g_Parametres = p_Parametres;
        g_OperacioOK = p_OperacioOK;
        g_OperacioKO = p_OperacioKO;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        //postData(params[0]);
        return postData(params[0]);

    }

    protected void onPostExecute(Integer result){
        //pb.setVisibility(View.GONE);
        Toast.makeText(Globals.g_Native.getApplicationContext(), "Resultat: " + result, Toast.LENGTH_LONG).show();
        String xml = ParseXMLMethods.getXML();
        if (g_OperacioOK != null) {
            g_OperacioOK.run();
        }
    }

    protected void onProgressUpdate(Integer... progress){
        //pb.setProgress(progress[0]);
    }

    public Integer postData(String p_ProgramaPHP) {
        Integer Aux = 0;
        HttpClient l_httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("http://bodina.virtuol.com/prova.php");
        g_FuncioPHP = p_ProgramaPHP;
        HttpPost l_httppost = new HttpPost(g_FuncioPHP);

        try {
            // Add your data
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("usuari", "Joshua"));
            //nameValuePairs.add(new BasicNameValuePair("email", "email"));

            l_httppost.setEntity(new UrlEncodedFormEntity(g_Parametres));

            // Execute HTTP Post Request
            HttpResponse response = l_httpclient.execute(l_httppost);
            Aux = response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            if (g_OperacioKO != null) {
                g_OperacioKO.run();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (g_OperacioKO != null) {
                g_OperacioKO.run();
            }
        }
        return Aux;
    }

}