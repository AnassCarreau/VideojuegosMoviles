package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.image.BufferStrategy;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Audio;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;

public class PCGame implements Engine {

    private final PCInput pc_input;
    private final PCGraphics pc_graphics;
    private final PCAudio pc_audio;
    private Screen screen;
    private final Window window;

    /**
     * Constructora del Engine de PC
     *
     * @param window,    ventana sobre la que vamos a pintar el juego
     * @param logWidth,  int, ancho logico del juego
     * @param logHeight, int, alto logico del juego
     */
    public PCGame(Window window, int logWidth, int logHeight) {
        this.pc_input = new PCInput(this);
        this.pc_graphics = new PCGraphics(window, logWidth, logHeight);
        this.pc_audio = new PCAudio();
        this.window = window;

        window.addComponentListener(pc_graphics);
        window.addMouseListener(pc_input);
        window.addMouseMotionListener(pc_input);
    }

    /**
     * Bucle principal del juego, en cada iteracion del mismo calculamos el deltaTime,
     * actualizamos la Screen actual llamando a los metodos update & handleEvents y pintamos
     * el estado de juego
     */
    public final void run() {
        BufferStrategy strategy = this.window.getBufferStrategy();
        long lastFrameTime = System.nanoTime();
        while (true) {
            //calculo del DeltaTime
            long currentTime = System.nanoTime();
            long nanoDeltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            double deltaTime = (double) nanoDeltaTime / 1.0e9; //nanosegundos a segundos

            screen.handleEvents(this);
            screen.update(deltaTime);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    pc_graphics.updateContext();
                    pc_graphics.clear(0xFFFFFFFF);
                    pc_graphics.prepareFrame();
                    try {
                        screen.render(pc_graphics);
                    }
                    finally {
                        //Llamada a dispose
                        pc_graphics.restore();
                    }
                } while (strategy.contentsRestored());
                strategy.show();
            } while (strategy.contentsLost());
        }
    }

    /**
     * Metodo que devuelve el motor que se encarga de gestionar la
     * entrada del juego
     *
     * @return el motor de InputPC
     */
    @Override
    public final Input getInput() {
        return this.pc_input;
    }

    /**
     * Metodo que devuelve el motor que se encarga de gestionar
     * el audio de juego
     *
     * @return el motor de audioPC
     */
    @Override
    public final Audio getAudio() {
        return this.pc_audio;
    }

    /**
     * Metodo que devuelve el motor que se encarga del pintado
     * del juego
     *
     * @return el motor grafico de PC
     */
    @Override
    public final Graphics getGraphics() {
        return this.pc_graphics;
    }

    /**
     * Metodo para actualizar cual es la pantalla del juego actual
     * en la que nos encontramos
     *
     * @param sc, pantalla/nivel/estado al que vamos a cambiar
     */
    @Override
    public final void setScreen(Screen sc) {
        this.screen = sc;
    }

    /**
     * Metodo para obtener cual es la pantalla/nivel/estado de juego
     * actual
     *
     * @return Screen, pantalla actual
     */
    @Override
    public final Screen getCurrentScreen() {
        return this.screen;
    }
}
