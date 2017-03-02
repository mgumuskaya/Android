package com.gmsky.projectx;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

public class TranslateAsyncTask extends AsyncTask<String, Void, String> {

    TranslateCallback translateCallback;

    public TranslateAsyncTask(TranslateCallback translateCallback) {
        this.translateCallback = translateCallback;
    }

    @Override
    protected void onPostExecute(String result) {
        translateCallback.onSuccess(result);
    }

    @Override
    protected String doInBackground(String... params) {
        String result = new String();
        try {
            result += mTranslateData(params[0], "tr", "en");
        } catch (IOException e) {
            Log.e("ERROR=>", e.getLocalizedMessage());
        }
        return result;
    }

    public String mTranslateData(String text, String to, String from) throws IOException {
        URL url = new URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + from + "&tl=" + to + "&dt=t&q=" +
                URLEncoder.encode(text, "UTF-8"));
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Something Else");
        //Log.e("INFO=>", String.valueOf(urlConnection));
        BufferedReader br = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String result = br.readLine();
        Log.e("INFO1=>", result);
        result = result.substring(4, result.indexOf("\","));
        Log.e("INFO2=>", result);
        Log.e("INFO3=>", text);
        if (text.toLowerCase(Locale.getDefault()).equals(result.toLowerCase(Locale.getDefault()))) {
            result = mTranslateData(result, from, to);
        }
        br.close();
        return result;
    }


}
