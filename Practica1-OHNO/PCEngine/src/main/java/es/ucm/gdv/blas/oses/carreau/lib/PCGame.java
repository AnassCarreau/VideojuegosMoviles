package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.image.BufferStrategy;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Audio;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class PCGame implements Engine {
    //Variables
    private PCInput pc_input;
    private PCGraphics pc_graphics;
    private  PCAudio pc_audio;
    //Pantalla actual del juego
    Screen screen;
    Window window;
    public PCGame(Window window, int width, int height){
        this.pc_input = new PCInput(this);
        this.pc_graphics = new PCGraphics(window, width, height);
        this.pc_audio= new PCAudio();
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

            screen.handleEvents();
            screen.update(deltaTime);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    pc_graphics.updateContext();
                    pc_graphics.clear(0xFFFFFFFF);
                    pc_graphics.prepareFrame();
                    try {
                        screen.render();
                    }
                    finally {
                        pc_graphics.restore();
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
    public Audio getAudio() {
        return this.pc_audio;
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
