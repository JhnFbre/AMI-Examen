package com.webbi.developer.examenkim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Inicio extends AppCompatActivity {
    TextView txtUsuario, txtNombre;
    Button btnAgregar, btnListado;
    String idVendedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idVendedor= String.valueOf(leerValor(getApplicationContext(),"idVendedor"));
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtUsuario.setText(String.valueOf(leerValor(getApplicationContext(),"usuario")));
        txtNombre.setText(String.valueOf(leerValor(getApplicationContext(),"nombre")));

        btnAgregar=  findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent itemintent = new Intent(Inicio.this, Agregar.class);
                itemintent.putExtra("idVendedor", ""+idVendedor);
                Inicio.this.startActivity(itemintent);

            }
        });
        btnListado=  findViewById(R.id.btnListado);

        btnListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Inicio.this, Listado.class);
                itemintent.putExtra("idVendedor", ""+idVendedor);
                Inicio.this.startActivity(itemintent);
            }
        });

    }

    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences("PREFS", MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed (){
        Intent itemintent = new Intent(Inicio.this, MainActivity.class);
        Inicio.this.startActivity(itemintent);

    }
}
