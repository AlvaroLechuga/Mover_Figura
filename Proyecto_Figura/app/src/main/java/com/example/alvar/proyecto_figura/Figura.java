package com.example.alvar.proyecto_figura;

import android.graphics.Canvas;

public abstract class Figura {


    public abstract void dibujar(Canvas canvas);

    public abstract boolean comprobarSiTocoDentro(Float x, Float y);

    public abstract void actualizar(Float x, Float y);

    public abstract boolean comprobarColisionMoneda(Circulo ref);


}