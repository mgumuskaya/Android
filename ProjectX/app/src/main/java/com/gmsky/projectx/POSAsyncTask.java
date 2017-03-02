package com.gmsky.projectx;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by gmsky on 15/01/2017.
 */

public class POSAsyncTask extends AsyncTask<String, Void, String> {

    POSCallback posCallback;
    public static final String TAG="INFO=>";
    public static final String ERR="ERROR=>";

    public POSAsyncTask(POSCallback posCallback_){
        this.posCallback=posCallback_;
    }

    @Override
    protected void onPostExecute(String s) {
        posCallback.onSuccess(s);
    }

    @Override
    protected String doInBackground(String... params) {

        String result = new String();
        try {
            result += mTagSentence(params[0]);
        } catch (IOException e) {
            Log.e(ERR, e.getLocalizedMessage());
        }
        return result;
    }

    public String mTagSentence(String param) throws IOException {
        URL url = new URL("http://parts-of-speech.info/tagger/postagger?callback=jQuery21406901011160274857_1484480878238&text="+
                URLEncoder.encode(param, "UTF-8")+"&language=en&_=1484480878239");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Something Else");
        Log.i(TAG, String.valueOf(urlConnection));
        BufferedReader br = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String result = br.readLine();
        Log.i(TAG, result);
        result = result.substring(24, result.indexOf("\"})"));
        Log.i(TAG, result);
        Log.i(TAG, param);
        br.close();

        return result;
    }
}
