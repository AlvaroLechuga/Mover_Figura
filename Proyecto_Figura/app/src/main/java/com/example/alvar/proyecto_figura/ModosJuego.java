package com.example.alvar.proyecto_figura;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModosJuego extends AppCompatActivity {

    Button btnModoCubo;
    Button btnModoClick;

    MediaPlayer sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modos_juego);

        sonido = MediaPlayer.create(this, R.raw.musicgame);
        sonido.start();

        btnModoCubo = findViewById(R.id.btnModoCubo);
        btnModoClick = findViewById(R.id.btnModoClick);

        btnModoCubo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambioActivity = new Intent(getApplicationContext(), Enlace.class);
                sonido.stop();
                startActivity(cambioActivity);
            }
        });

        btnModoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambioActivity = new Intent(getApplicationContext(), Enlace2.class);
                sonido.stop();
                startActivity(cambioActivity);
            }
        });
    }
}
