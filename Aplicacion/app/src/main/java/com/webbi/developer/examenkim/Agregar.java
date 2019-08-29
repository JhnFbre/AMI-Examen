package com.webbi.developer.examenkim;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Agregar extends AppCompatActivity {
    EditText txtCedula, txtNombre, txtApellido, txtTelefono, txtDireccion, txtCorreo;
    Button btnGuardar;
    ServicioWebGuardar servicio;
    String idVendedor;
    RadioGroup radioGroup;
    RadioButton radio_sale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        idVendedor= ""+getIntent().getStringExtra("idVendedor");
        txtCedula = findViewById(R.id.txtCedula);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtCorreo = findViewById(R.id.txtCorreo);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radio_sale = (RadioButton) findViewById(selectedId);
                if (radio_sale!=null){
                    btnGuardar.setEnabled(false);
                    servicio=(ServicioWebGuardar)new ServicioWebGuardar().execute();
                }else{
                    Toast.makeText(getApplicationContext(),"¡Te faltan campos por completar!",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed (){
        Intent itemintent = new Intent(Agregar.this, Inicio.class);
        Agregar.this.startActivity(itemintent);

    }

    private class ServicioWebGuardar extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected String getWebServiceResponseData() {
            String path = "http://test-ami-jcxdd.run.goorm.io/api/customers";
            HttpURLConnection urlConnection = null;
            Map<String, String> stringMap = new HashMap<>();

            stringMap.put("name", ""+txtNombre.getText());
            stringMap.put("lastname", ""+txtApellido.getText());
            stringMap.put("dni", ""+txtCedula.getText());
            stringMap.put("dir", ""+txtDireccion.getText());
            stringMap.put("email", ""+txtCorreo.getText());
            stringMap.put("phone", ""+txtTelefono.getText());
            stringMap.put("sale", ""+radio_sale.getTag());

            stringMap.put("employee", String.valueOf(""+idVendedor));
            String requestBody = Utils.buildPostParameters(stringMap);
            try {
                urlConnection = (HttpURLConnection) Utils.makeRequest("POST", path, null, "application/x-www-form-urlencoded", requestBody);
                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                // parse stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }
            } catch (Exception e) {

                e.printStackTrace();
                btnGuardar.setEnabled(true);

                servicio.cancel(true);
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return "gg";

        }

        @Override
        protected void onPostExecute(String gatito) {

            super.onPostExecute(gatito);
            txtNombre.setText("");
            txtApellido.setText("");
            txtCorreo.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");

            Intent itemintent = new Intent(Agregar.this, Inicio.class);
            Agregar.this.startActivity(itemintent);

            Toast.makeText(getApplicationContext(),"Cliente guardado",Toast.LENGTH_LONG).show();

        }


    }


    public static class Utils{
        public static String buildPostParameters(Object content) {
            String output = null;
            if ((content instanceof String) ||
                    (content instanceof JSONObject) ||
                    (content instanceof JSONArray)) {
                output = content.toString();
            } else if (content instanceof Map) {
                Uri.Builder builder = new Uri.Builder();
                HashMap hashMap = (HashMap) content;
                if (hashMap != null) {
                    Iterator entries = hashMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                        entries.remove(); // avoids a ConcurrentModificationException
                    }
                    output = builder.build().getEncodedQuery();
                }
            }

            return output;
        }

        public static URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiAddress);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(!method.equals("GET"));
                urlConnection.setRequestMethod(method);

                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                urlConnection.setRequestProperty("Content-Type", mimeType);
                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                writer.write(requestBody);
                writer.flush();
                writer.close();
                outputStream.close();

                urlConnection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return urlConnection;
        }
    }
}
