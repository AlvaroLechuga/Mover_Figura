package com.example.alvar.proyecto_figura;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PuntuacionHelper extends SQLiteOpenHelper{

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE puntuacion (Nombre VARCHAR, Puntuacion INTEGER, ModoJuego VARCHAR)";

    public PuntuacionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS puntuacion");
        db.execSQL(sqlCreate);
    }
}
