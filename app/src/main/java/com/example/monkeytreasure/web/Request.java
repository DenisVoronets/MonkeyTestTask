package com.example.monkeytreasure.web;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.monkeytreasure.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Request extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progressBar = findViewById(R.id.progressBar);
        startDollarTask();

    }

    public void startDollarTask() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected()) {
            new DollarTask().execute();// запускаем в новом потоке
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Нет интернета", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private class DollarTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = "";
        double usd;
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                jsonResult = builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        protected void onPostExecute(String jsonResult) {

            try {
                Intent intent = new Intent(Request.this, myWebView.class);
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONObject jsonArray = jsonObject.getJSONObject("Valute");
                JSONObject usdObject = jsonArray.getJSONObject("USD");
                usd = usdObject.getDouble("Value");
                result  = Double.toString(usd);
                intent.putExtra("value",result);
                startActivity(intent);
                progressBar.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


