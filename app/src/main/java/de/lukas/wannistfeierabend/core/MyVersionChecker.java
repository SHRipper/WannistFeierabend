package de.lukas.wannistfeierabend.core;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.lukas.wannistfeierabend.util.UpdateManager;

/**
 * Created by Lukas on 12.04.2017.
 */

public class MyVersionChecker extends AsyncTask<String,Void,String>{
    UpdateManager um;

    public MyVersionChecker(UpdateManager um, String url, boolean initial){
        this.um = um;
        if (initial){
            this.execute(url,"true");
        }else{
            this.execute(url,"false");
        }
    }
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            if (strings[1].equals("true")) return "init_" + readStream(in);
            else return readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    private String readStream(InputStream in) {
        char c;
        String s = "";

        try {
            while ((c=(char)in.read()) != ';'){
                s += c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        um.setFetchedVersion(s);
    }
}