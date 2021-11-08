package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class AndroidGame  implements Engine, Runnable {
    SurfaceView renderView;
    AndroidGraphics graphics;
    AndroidInput input;
    Screen screen;
    AndroidAudio audio;

    //Para avisar al compilador que el valor de este atributo
    //puede ser cambiado en una hebra
    volatile boolean running_ = false;
    Thread thread_;

    public AndroidGame(AppCompatActivity activity, int logicalWidth, int logicalHeight) {
        //activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        boolean isLandscape = activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? logicalHeight : logicalWidth;
        int frameBufferHeight = isLandscape ? logicalWidth : logicalHeight;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);

        renderView = new SurfaceView(activity.getApplicationContext());

        graphics = new AndroidGraphics(activity, frameBuffer,logicalWidth,logicalHeight);
        audio = new AndroidAudio(activity);
        input = new AndroidInput(this, renderView);
        activity.setContentView(renderView);
    }

    public Input getInput() {
        return input;
    }

    public Graphics getGraphics() { return graphics;
    }
    public AndroidAudio getAudio() { return audio; }

    public Screen getCurrentScreen() {
        return screen;
    }

    public void onResume() {
        //Lanzar hebra
        if (!running_) {
            running_ = true;
            thread_ = new Thread(this);
            thread_.start();
        }
    }

    public void onPause() {
        //Parar hebra
        running_ = false;
        while (true) {
            try {
                thread_.join();
                break;
            }
            //Suelta excepcion si despues de hacer el join,
            // la hebra recibe un mensaje y la reactiva.
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        this.screen = screen;
    }

    public SurfaceView getView(){
        return renderView;
    }

    @Override
    public void run() {
        //Aqui iria el bucle principal
        long _lastFrameTime = System.nanoTime();
        //Guarda una superficie donde se puede pintar
        SurfaceHolder holder = renderView.getHolder();

        while (running_) {
            //update & render
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - _lastFrameTime;
            _lastFrameTime = currentTime;

            double deltaTime = (double) nanoElapsedTime / 10E09;
            screen.handleEvents();
            screen.update(deltaTime);

            //Bloqueamos hasta que conseguimos la superficie
            while (!holder.getSurface().isValid());

            //Pedimos el canvas para poder pintar
            Canvas canvas = holder.lockCanvas();
            //Setteamos el canvas para usarlo al pintar
            this.graphics.setCanvas(canvas);
            //Clear pantalla
            this.graphics.clear(0xFFFFFFFF);
            //Nos preparamos para pintar (transladar y escalar)
            this.graphics.prepareFrame();
            //Pintamos
            screen.render();

            //Hacemos el swap de los buffers de pintado
            holder.unlockCanvasAndPost(canvas);
        }
    }
}