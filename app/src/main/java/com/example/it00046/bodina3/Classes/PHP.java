package com.example.it00046.bodina3.Classes;

import com.loopj.android.http.*;

/**
 * Created by it00046 on 18/02/2015.
 */
public class Php {

    private static AsyncHttpClient client = new AsyncHttpClient();





    public static void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
            return Globals.k_DirectoriPHP + relativeUrl;
    }
}
