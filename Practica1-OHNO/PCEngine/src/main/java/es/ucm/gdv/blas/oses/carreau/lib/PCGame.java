package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.image.BufferStrategy;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class PCGame implements Engine {
    //Variables
    private PCInput pc_input;
    private PCGraphics pc_graphics;
    //Pantalla actual del juego
    Screen screen;
    Window window;

    public PCGame(Window window, int width, int height){
        this.pc_input = new PCInput(this);
        this.pc_graphics = new PCGraphics(window, width, height);
        this.window = window;
        window.addComponentListener(pc_graphics);
        window.addMouseListener(pc_input);
        window.addMouseMotionListener(pc_input);
    }

    public void run(){
        BufferStrategy strategy = this.window.getBufferStrategy();
        long lastFrameTime = System.nanoTime();
        while(true)
        {
            //calculo del DeltaTime
            long currentTime = System.nanoTime();
            long nanoDeltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            double deltaTime = (double) nanoDeltaTime / 1.0e9; //nanosegundos a segundos

            screen.update(deltaTime);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    java.awt.Graphics g = strategy.getDrawGraphics();
                    try {
                        screen.render();
                    }
                    finally {
                        g.dispose();
                    }
                } while(strategy.contentsRestored());
                strategy.show();
            } while(strategy.contentsLost());
        }
    }

    @Override
    public Input getInput() {
        return this.pc_input;
    }

    @Override
    public Graphics getGraphics() {
        return this.pc_graphics;
    }

    @Override
    public void setScreen(Screen sc) {
        this.screen = sc;
    }

    @Override
    public Screen getCurrentScreen() {
        return this.screen;
    }
}
