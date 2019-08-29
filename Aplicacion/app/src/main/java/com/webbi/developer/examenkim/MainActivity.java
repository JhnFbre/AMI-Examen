package com.webbi.developer.examenkim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AsyncTaskActivity";
    private static final String PREFS_KEY = "mispreferencias" ;
    String responseText;
    StringBuffer response;
    ServicioWeb servicio;
    Button Logear;
    EditText txtUser, txtClave;
    String userLogged,passLogged,nombreLogged,apellidoLogged,idLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtClave = (EditText) findViewById(R.id.txtPass);

        Logear = (Button) findViewById(R.id.btnLogin);
        Logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtClave.setText("sadasd");
                if (!txtUser.getText().toString().trim().equalsIgnoreCase("")){
                    if(txtClave.getText().toString().trim().equalsIgnoreCase("")){
                        txtClave.setError("Campo vacío");
                    }else{
                        if(isConnectedToInternet())
                        {
                            servicio = (ServicioWeb) new ServicioWeb().execute();
                        }
                        else
                        {
                            Log.d("Error", "Error Conexion");
                            Bundle args = new Bundle();
                            args.putString("titulo", "Advertencia");
                            args.putString("texto", "No hay conexión de Internet");
                            ProblemaConexion f=new ProblemaConexion();
                            f.setArguments(args);
                            f.show(getSupportFragmentManager(), "ProblemaConexión");
                        }

                    }
                }else{
                    txtUser.setError("Campo vacío");

                }

            }
        });
    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    private class ServicioWeb extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            //Logear.setEnabled(false);
        }
        @Override
        protected String doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected String getWebServiceResponseData() {

            String flag= "no";
            try {
                URL url = new URL("http://test-ami-jcxdd.run.goorm.io/api/employees");
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

            try {
                JSONArray arrayDatos= new JSONArray(responseText);
                for (int i=0;i<arrayDatos.length();i++){
                    JSONObject jsonobject = arrayDatos.getJSONObject(i);
                    if(jsonobject.getString("username").equals(""+txtUser.getText()) && jsonobject.getString("password").equals(""+txtClave.getText())){
                       flag="si";
                       userLogged=""+txtUser.getText();
                       passLogged=""+txtClave.getText();
                       nombreLogged=""+jsonobject.getString("name");
                       apellidoLogged=""+jsonobject.getString("lastname");
                       idLogged=""+jsonobject.getString("_id");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }




            return flag;
        }

        @Override
        protected void onPostExecute(String flag) {
            super.onPostExecute(flag);
            //-Log.d(TAG, "onPostExecute");
            if (flag.equals("si")){
                Intent itemintent = new Intent(MainActivity.this, Inicio.class);
                itemintent.putExtra("nombre",""+nombreLogged);
                itemintent.putExtra("usuario",""+userLogged);
                itemintent.putExtra("apellido",""+apellidoLogged);
                itemintent.putExtra("idVendedor",""+idLogged);
                guardarValor(MainActivity.this,"idVendedor",idLogged);
                guardarValor(MainActivity.this,"nombre",nombreLogged+" "+apellidoLogged);
                guardarValor(MainActivity.this,"usuario",userLogged);

                MainActivity.this.startActivity(itemintent);
            }else{
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Usuario o contraseña incorrecta");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
            }
        }
    }
    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }
}
