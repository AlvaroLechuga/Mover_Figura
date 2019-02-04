package com.example.alvar.proyecto_figura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeleccionarConsulta extends AppCompatActivity {

    Button btnConsultaDesplaza;
    Button btnConsultaClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_consulta);

        btnConsultaDesplaza = findViewById(R.id.btnConsultaDesplaza);
        btnConsultaClick = findViewById(R.id.btnConsultaClick);

        btnConsultaDesplaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Consulta.class);
                i.putExtra("modo", "Arrastra");
                startActivity(i);
            }
        });

        btnConsultaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Consulta.class);
                i.putExtra("modo", "Click");
                startActivity(i);
            }
        });
    }
}
