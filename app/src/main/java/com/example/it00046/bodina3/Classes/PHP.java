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

/**
 * Created by Joshua on 14/02/2015.
 */
// Tema PHP
public class PHP extends AsyncTask<String, Integer, Integer> {
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
    }

    protected void onProgressUpdate(Integer... progress){
        //pb.setProgress(progress[0]);
    }

    public Integer postData(String valueIWantToSend) {
        Integer Aux = 0;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://bodina.virtuol.com/prova.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("usuari", "Joshua"));
            nameValuePairs.add(new BasicNameValuePair("email", "email"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Aux = response.getStatusLine().getStatusCode();
            Log.i("Hola", EntityUtils.toString(response.getEntity()));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Aux;
    }

}