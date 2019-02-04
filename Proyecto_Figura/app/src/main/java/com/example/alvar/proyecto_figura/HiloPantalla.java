package com.example.alvar.proyecto_figura;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class HiloPantalla extends Thread {

    private SurfaceHolder sh;
    private Juego view;
    private boolean run;

    public HiloPantalla(SurfaceHolder sh, Juego view){
        this.sh = sh;
        this.view = view;
        this.run = false;
    }

    public void setRunning(boolean run){
        this.run = run;
    }

    public void run(){
        Canvas canvas;
        while(run){ //Habilita la edición multiple
            canvas = null;
            try{
                canvas = sh.lockCanvas(null); //Intentamos bloquear el getHolder()

                if(canvas != null) { //Comprueba que el canvas no sea nulo
                    synchronized (sh) { //Solo para nosotros
                        view.postInvalidate(); //Pinta
                    }
                }
            }
            finally{
                if(canvas != null)
                    sh.unlockCanvasAndPost(canvas); //Desbloqueamos el canvas
            }
        }

    }
}
