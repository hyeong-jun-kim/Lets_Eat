package org.techtown.letseat.map;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Background2 extends AsyncTask<String, Void, JSONArray> {
    JSONArray Gun_jsonArray;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    @Override
    protected JSONArray doInBackground(String... strUrl) {
        try {
            URL url = new URL(strUrl[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer builder = new StringBuffer();

            String inputString = null;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            String s = builder.toString();
            Gun_jsonArray = new JSONArray(s);

            conn.disconnect();
            bufferedReader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Gun_jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        super.onPostExecute(result);
    }

    protected void onCancelled() {
        super.onCancelled();
    }
}
