package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
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

    //Para avisar al compilador que el valor de este atributo
    //puede ser cambiado en una hebra
    volatile boolean running_ = false;
    Thread thread_;

    public AndroidGame(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        boolean isLandscape = activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
      /*  float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();*/
        renderView = new SurfaceView(activity.getApplicationContext());//AndroidFastRenderView(this, frameBuffer);
        Point p=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        graphics = new AndroidGraphics(activity.getAssets(), frameBuffer,400,600,p.x,p.y );

        input = new AndroidInput(this, renderView);
        activity.setContentView(renderView);
    }

    public Input getInput() {
        return input;
    }

    public Graphics getGraphics() {
        return graphics;
    }

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

            screen.update(deltaTime);

            //Bloqueamos hasta que conseguimos la superficies
            while (!holder.getSurface().isValid()) ;

            //Pedimos el canvas para poder pintar
            Canvas canvas = holder.lockCanvas();
            //Setteamos el canvas para usarlo al pintar
            this.graphics.setCanvas(canvas);
            //Pintamos
            screen.render();

            //Hacemos el swap de los buffers de pintado
            holder.unlockCanvasAndPost(canvas);
        }
    }
}