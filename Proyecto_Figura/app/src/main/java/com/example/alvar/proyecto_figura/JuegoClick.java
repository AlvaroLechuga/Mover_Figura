package com.example.alvar.proyecto_figura;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class JuegoClick extends SurfaceView implements SurfaceHolder.Callback {
    String modo = "Click";
    String nombre = "Null";

    private HiloPantalla2 hiloPantalla;
    PuntuacionHelper dbHelper;
    ArrayList<Figura> figuras;
    Figura figActiva;
    Circulo circulo;

    Rectangulo rectangulo;

    Paint paintTexto;
    Paint gameOver;

    Float iniX;
    Float iniY;

    int contador = 0;
    int segundos = 0;

    int anchoPantalla;
    int altoPantalla;

    float posX;
    float posY;

    int a = 0;

    boolean fin = false;

    Tiempo tiempo;

    MediaPlayer gameOverSound;

    public JuegoClick(Context context) {
        super(context);
        dbHelper = new PuntuacionHelper(context, "puntuaciones", null, 1);
        setBackgroundColor(Color.BLACK);
        getHolder().addCallback(this);
        paintTexto = new Paint();
        gameOver = new Paint();

        gameOverSound = MediaPlayer.create(context, R.raw.gameover);

        tiempo = new Tiempo();

    }

    @Override
    public void onDraw(final Canvas canvas){
        super.onDraw(canvas);

        if(segundos != 10){

            circulo.dibujar(canvas);
            paintTexto.setColor(Color.WHITE);
            paintTexto.setTextSize(60);

            segundos = tiempo.getSegundos();

            canvas.drawText("Puntuación: = "+contador, 100,100, paintTexto);
            canvas.drawText("Tiempo: "+segundos, 600, 100, paintTexto);
        }else{
            fin = true;
            tiempo.Detener();
            gameOver.setColor(Color.RED);
            gameOver.setTextAlign(Paint.Align.CENTER);
            gameOver.setTextSize(100);
            canvas.drawText("Puntuación: = "+contador, 100,100, paintTexto);
            canvas.drawText("GAME OVER", anchoPantalla/2,altoPantalla/2, gameOver);

            if(a == 1){
                mostrarDialogo();
                a = 5;
            }

            if(a == 10){
                if(Insertar(nombre, contador, modo)){
                    a = 2;
                }else{
                    a = 3;
                }
            }

            if(a == 2){
                canvas.drawText("Insertado: "+nombre, 600,100, paintTexto);
                rectangulo.dibujar(canvas);
            }

            if(a == 3){
                canvas.drawText("Null", 600,100, paintTexto);
                rectangulo.dibujar(canvas);
            }

            try {
                if(a == 0){
                    gameOverSound.start();
                    TimeUnit.SECONDS.sleep(5);
                    gameOverSound.stop();
                    a = 1;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarDialogo() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Guardar Puntuacion");
        alertDialog.setMessage("Introduce tu nombre");

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        nombre = input.getText().toString();
                        a = 10;
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private boolean Insertar(String nombre, int contador, String modo) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{
            String consulta ="INSERT INTO `puntuacion` (`Nombre`, `Puntuacion`, `ModoJuego`) VALUES ('" + nombre + "', '" + contador + "', '" + modo + "')";
            db.execSQL(consulta);
            return true;
        }catch (SQLException sqle){
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // move
                iniX=event.getX();
                iniY=event.getY();
                if(circulo.comprobarSiTocoDentro(iniX, iniY)){
                    if(!fin){
                        contador++;
                        posX = (float) (Math.random()*(10)+1);
                        posY = (float) (Math.random()*(10)+1);

                        circulo = circulo = new Circulo(anchoPantalla/posX, altoPantalla/posY , 100f);
                    }
                }

                if(a == 2 || a == 3){
                    if(rectangulo.comprobarSiTocoDentro(iniX, iniY)){
                        contador = 0;
                        segundos = 0;
                        a = 1;
                        tiempo.Contar();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:

                figActiva=null;

                if(figuras.get(0).comprobarColisionMoneda(circulo)){

                }

                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        figActiva = null;
        anchoPantalla=getWidth();
        altoPantalla=getHeight();

        inicializar(anchoPantalla,altoPantalla);

        hiloPantalla = new HiloPantalla2(getHolder(), this);
        hiloPantalla.setRunning(true);
        hiloPantalla.start(); //Ejecuta el metodo run del hilo

        circulo = new Circulo(anchoPantalla/5f, altoPantalla/5f , 100f);

        tiempo.Contar();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        hiloPantalla.setRunning(false); //Paramos, ya que si está pintando no podemos destruirlo

        while(retry){
            try{
                hiloPantalla.join();
                retry = false;
            }
            catch(InterruptedException e){

            }
        }
    }

    public void inicializar(int anchoPantalla, int altoPantalla){

        figuras=new ArrayList<>();
        figuras.add(new Rectangulo(anchoPantalla/3f,altoPantalla/3f, 100f,100f));

    }
}
