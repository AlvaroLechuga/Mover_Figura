package com.example.alvar.proyecto_figura;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    Button btnJugar;
    Button btnMostrarRecords;
    Button btnSalir;

    MediaPlayer sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sonido = MediaPlayer.create(this, R.raw.musicstart);
        sonido.start();

        btnJugar = findViewById(R.id.btnJugar);
        btnMostrarRecords = findViewById(R.id.btnMostrarRecords);
        btnSalir = findViewById(R.id.btnSalir);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambioActivity = new Intent(getApplicationContext(), ModosJuego.class);
                sonido.stop();
                startActivity(cambioActivity);
            }
        });

        btnMostrarRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambioActivity = new Intent(getApplicationContext(), SeleccionarConsulta.class);
                sonido.stop();
                startActivity(cambioActivity);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Salir")
                        .setMessage("Estás seguro?")
                        .setNegativeButton(android.R.string.cancel, null)// sin listener
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sonido.stop();
                                finish();
                            }
                        }).show();
            }
        });
    }
}
