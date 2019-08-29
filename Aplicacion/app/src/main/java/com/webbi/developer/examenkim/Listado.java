package com.webbi.developer.examenkim;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Listado extends AppCompatActivity {
    private static final String TAG = "AsyncTaskActivity";
    private static final String PREFS_KEY = "mispreferencias" ;
    String responseText;
    StringBuffer response;
    ServicioWeb servicio;
    String idVendedor;
    LinearLayout tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabla = (LinearLayout) findViewById(R.id.tabla);

        idVendedor= ""+getIntent().getStringExtra("idVendedor");
        servicio= (ServicioWeb) new ServicioWeb().execute();
    }

    private class ServicioWeb extends AsyncTask<Integer, Integer, JSONArray> {

        @Override
        protected void onPreExecute() {
            //Logear.setEnabled(false);
        }
        @Override
        protected JSONArray doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected JSONArray getWebServiceResponseData() {

            String flag= "no";
            try {
                URL url = new URL("http://test-ami-jcxdd.run.goorm.io/api/customers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                Log.d(TAG, "Response code: " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();

                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    in.close();
                }
                responseText = response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                servicio.cancel(true);

            }
            JSONArray arrayDatos = null;
            try {
                arrayDatos= new JSONArray(responseText);

            } catch (JSONException e) {
                e.printStackTrace();
                servicio.cancel(true);
            }




            return arrayDatos;
        }

        @Override
        protected void onPostExecute(JSONArray arrayDatos) {
            super.onPostExecute(arrayDatos);
            //-Log.d(TAG, "onPostExecute");
            if(arrayDatos!=null){
                for (int i=0;i<arrayDatos.length();i++){

                    try {
                        JSONObject jsonobject = arrayDatos.getJSONObject(i);
                        if(jsonobject.getString("employee").equals(idVendedor)){

                            TextView t1v = new TextView(getApplicationContext());
                            t1v.setText(jsonobject.getString("name"));
                            t1v.setTextColor(Color.BLACK);
                            t1v.setTextSize(16);
                            t1v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t1v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                            ((LinearLayout) tabla).addView(t1v);

                            TextView t2v = new TextView(getApplicationContext());
                            t2v.setText(jsonobject.getString("lastname"));
                            t2v.setTextColor(Color.BLACK);
                            t2v.setTextSize(16);
                            t2v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t2v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                            tabla.addView(t2v);

                            TextView t3v = new TextView(getApplicationContext());
                            t3v.setText(jsonobject.getString("email"));
                            t3v.setTextColor(Color.BLACK);
                            t3v.setTextSize(16);
                            t3v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t3v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                            tabla.addView(t3v);

                            TextView t4v = new TextView(getApplicationContext());
                            t4v.setText(jsonobject.getString("dir"));
                            t4v.setTextColor(Color.BLACK);
                            t4v.setTextSize(16);
                            t4v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t4v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            tabla.addView(t4v);

                            TextView t5v = new TextView(getApplicationContext());
                            t5v.setText(jsonobject.getString("dni"));
                            t5v.setTextColor(Color.BLACK);
                            t5v.setTextSize(16);
                            t5v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t5v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            tabla.addView(t5v);

                            TextView t6v = new TextView(getApplicationContext());
                            t6v.setText(jsonobject.getString("phone"));
                            t6v.setTextColor(Color.BLACK);
                            t6v.setTextSize(16);
                            t6v.setGravity(Gravity.CENTER_HORIZONTAL);
                            t6v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            tabla.addView(t6v);

                            TextView testado = new TextView(getApplicationContext());
                            testado.setText(jsonobject.getString("sale"));
                            testado.setTextColor(Color.RED);
                            testado.setTextSize(20);
                            testado.setGravity(Gravity.CENTER_HORIZONTAL);
                            tabla.addView(testado);

                            View linea= new View(getApplicationContext());
                            linea.setBackgroundColor(Color.parseColor("#000000"));
                            linea.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            linea.getLayoutParams().height=2;
                            tabla.addView(linea);



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{

            }

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed (){
        Intent itemintent = new Intent(Listado.this, Inicio.class);
        Listado.this.startActivity(itemintent);

    }
}
