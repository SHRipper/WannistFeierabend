package de.lukas.wannistfeierabend.core;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lukas on 12.04.2017.
 */

public class MyVersionChecker extends AsyncTask<String, Void, String[]> {
    UpdateManager um;

    public MyVersionChecker(UpdateManager um, String url) {
        this.um = um;
        this.execute(url);

    }

    @Override
    protected String[] doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    private String[] readStream(InputStream in) {
        char c;
        String[] s = new String[2];
        s[0] = "";
        s[1] = "";

        try {
            while ((c = (char) in.read()) != ';') {
                s[0] += c;
            }
            while ((c = (char) in.read()) != ';') {
                s[1] += c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
        um.compareVersions(s[0], s[1]);

    }
}
