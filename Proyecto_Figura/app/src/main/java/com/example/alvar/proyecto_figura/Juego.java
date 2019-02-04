package com.example.alvar.proyecto_figura;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;

public class Juego extends SurfaceView implements SurfaceHolder.Callback {

    String modo = "Arrastra";
    String nombre = "Null";

    private HiloPantalla hiloPantalla;
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

    Tiempo tiempo;

    MediaPlayer gameOverSound;

    boolean mostrarDialogo = false;


    public Juego(Context context) {
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
            for(Figura f: figuras){
                f.dibujar(canvas);
            }
            circulo.dibujar(canvas);

            paintTexto.setColor(Color.WHITE);
            paintTexto.setTextSize(60);

            segundos = tiempo.getSegundos();

            canvas.drawText("Puntuación: = "+contador, 100,100, paintTexto);
            canvas.drawText("Tiempo: "+segundos, 600, 100, paintTexto);
        }else{

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
                        a = 3;
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // move

                for(Figura f : figuras){
                    if(f.comprobarSiTocoDentro(event.getX(),event.getY())){
                        figActiva=f;
                    }
                }
                if(figActiva != null){
                    figuras.remove(figActiva);
                    figuras.add(figActiva);
                }
                iniX=event.getX();
                iniY=event.getY();

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

                if(figActiva !=null){
                    float ofiX=event.getX()-iniX;
                    float ofiY=event.getY()-iniY;

                    figActiva.actualizar(ofiX,ofiY);
                }
                iniX=event.getX();
                iniY=event.getY();

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:

                figActiva=null;

                if(figuras.get(0).comprobarColisionMoneda(circulo)){
                    contador++;

                    posX = (float) (Math.random()*(10)+1);
                    posY = (float) (Math.random()*(10)+1);

                    circulo = circulo = new Circulo(anchoPantalla/posX, altoPantalla/posY , 100f);

                }

                break;
        }
        return true;
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
    public void surfaceCreated(SurfaceHolder holder) {

        figActiva = null;
        anchoPantalla=getWidth();
        altoPantalla=getHeight();

        inicializar(anchoPantalla,altoPantalla);

        hiloPantalla = new HiloPantalla(getHolder(), this);
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
        rectangulo = new Rectangulo(anchoPantalla/3f,altoPantalla/3f, 400f, 250f);
    }
}
