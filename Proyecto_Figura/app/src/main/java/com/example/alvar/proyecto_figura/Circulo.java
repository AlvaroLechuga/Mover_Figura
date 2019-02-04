package com.example.alvar.proyecto_figura;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circulo extends Figura {

    private Float x;
    private Float y;
    private Float radio;

    public Circulo() {
    }

    public Circulo(Float x, Float y, Float radio) {
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getRadio() {
        return radio;
    }

    public void setRadio(Float radio) {
        this.radio = radio;
    }

    @Override
    public void dibujar(Canvas canvas) {
        Paint p= new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getX(),getY(),getRadio(),p);
    }

    @Override
    public boolean comprobarSiTocoDentro(Float x, Float y) {
        boolean devolver = false;

        if (Math.pow(getRadio(), 2) > (Math.pow(x - getX(), 2) + Math.pow(y - getY(), 2))) {

            devolver = true;

        }

        return devolver;
    }

    @Override
    public void actualizar(Float x, Float y) {
        setX(getX()+x);
        setY(getY()+y);
    }

    @Override
    public boolean comprobarColisionMoneda(Circulo ref) {
        return false;
    }
}
