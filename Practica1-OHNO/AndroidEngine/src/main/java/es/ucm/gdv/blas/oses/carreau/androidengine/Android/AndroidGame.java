package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;

public class AndroidGame implements Engine, Runnable {
    private final SurfaceView renderView;
    private final AndroidGraphics graphics;
    private final AndroidInput input;
    private Screen screen;
    private final AndroidAudio audio;

    //Para avisar al compilador que el valor de este atributo
    //puede ser cambiado en una hebra
    private volatile boolean running_ = false;
    private Thread thread_;

    /**
     * Constructora del engine especifico de Android
     *
     * @param activity,      actividad de android
     * @param logicalWidth,  int, ancho logico del juego
     * @param logicalHeight, int, alto logico del juego
     */
    public AndroidGame(AppCompatActivity activity, int logicalWidth, int logicalHeight) {
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
        graphics = new AndroidGraphics(activity, frameBuffer, logicalWidth, logicalHeight);
        audio = new AndroidAudio(activity);
        input = new AndroidInput(this, renderView);

        activity.setContentView(renderView);
    }

    /**
     * Metodo que devuelve el motor que se encarga de gestionar la
     * entrada del juego
     *
     * @return el motor de AndroidInput
     */
    @Override
    public Input getInput() {
        return input;
    }

    /**
     * Metodo que devuelve el motor que se encarga del pintado
     * del juego
     *
     * @return el motor grafico de PC
     */
    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * Metodo que devuelve el motor que se encarga de gestionar
     * el audio de juego
     *
     * @return el motor de AndroidAudio
     */
    @Override
    public AndroidAudio getAudio() {
        return audio;
    }


    /**
     * Metodo para obtener cual es la pantalla/nivel/estado de juego
     * actual
     *
     * @return Screen, pantalla actual
     */
    public Screen getCurrentScreen() {
        return screen;
    }


    /**
     * Metodo para actualizar cual es la pantalla del juego actual
     * en la que nos encontramos
     *
     * @param screen, pantalla/nivel/estado al que vamos a cambiar
     */
    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");
        this.screen = screen;
    }


    /**
     * Metodo heredado de la clase Runnable
     * Contiene el bucle principal del juego.
     * En cada iteracion se calcula el deltaTime, se actualiza
     * el estado de la Screen llamando a sus metodos update y handleEvents
     * y despues se pinta el estado de dicha pantalla de juego
     */
    @Override
    public void run() {
        long _lastFrameTime = System.nanoTime();
        //Guarda una superficie donde se puede pintar
        SurfaceHolder holder = renderView.getHolder();

        while (running_) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - _lastFrameTime;
            _lastFrameTime = currentTime;

            double deltaTime = (double) nanoElapsedTime / 1.0e9;
            screen.handleEvents(this);
            screen.update(deltaTime);

            //Bloqueamos hasta que conseguimos la superficie
            while (!holder.getSurface().isValid()) ;

            //Pedimos el canvas para poder pintar
            Canvas canvas = holder.lockCanvas();
            //Setteamos el canvas para usarlo al pintar
            this.graphics.setCanvas(canvas);
            //Clear pantalla
            this.graphics.clear(0xFFFFFFFF);
            //Nos preparamos para pintar (transladar y escalar)
            this.graphics.prepareFrame();
            //Pintamos
            screen.render(graphics);

            //Hacemos el swap de los buffers de pintado
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Metodo que devuelve la surfaceView
     *
     * @return SurfaceView
     */
    public SurfaceView getView() {
        return renderView;
    }

    /**
     * Metodo al que llamaremos cuando desde el Main activity pasemos
     * al estado onResume.
     * Este estado es al que se le llama siempre cuando por ejemplo hay
     * un giro de pantalla, por lo que es en el que creamos la hebra
     */
    public void onResume() {
        if (!running_) {
            running_ = true;
            thread_ = new Thread(this);
            thread_.start();
        }
    }

    /**
     * Metodo al que llamaremos cuando la aplicacion pase al estado de pausa desde
     * la actividad
     * Pausamos el juego y hacemos que la hebra termine
     */
    public void onPause() {
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
}
