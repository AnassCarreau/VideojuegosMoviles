package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.image.BufferStrategy;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.FileIO;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.PCInput;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class PCGame implements Engine {
    //Variables
    private PCInput pc_input;
    private PCGraphics pc_graphics;
    //Pantalla actual del juego
    Screen screen;
    Window window;

    public PCGame(Window window, int width, int height){
        this.pc_input = new PCInput();
        this.pc_graphics = new PCGraphics(window, width, height);
        this.window = window;
    }

    public void run(){
        BufferStrategy strategy = this.window.getBufferStrategy();
        while(true)
        {
            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    java.awt.Graphics g = strategy.getDrawGraphics();
                    try {
                        screen.render();
                        //this.screen.update(60);
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

    //TO DO: mirar que es esta wea xD
    @Override
    public FileIO getFileIO() {
        return null;
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
