package com.example.alvar.proyecto_figura;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Intermediaria extends AppCompatActivity {

    private String modo;
    private int puntuacion;
    private Context context;

    public Intermediaria(String modo, int contador, Context context) {

        this.modo = modo;
        this.puntuacion = contador;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void PasoaAgregar(){

        try {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }catch (NullPointerException npe){
            Log.e("null", getApplicationContext().toString());
            Log.e("modo", modo);
            Log.e("puntuacion", Integer.toString(puntuacion));
        }
    }
}
