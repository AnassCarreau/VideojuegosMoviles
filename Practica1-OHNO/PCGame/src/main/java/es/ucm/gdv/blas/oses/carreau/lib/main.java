package es.ucm.gdv.blas.oses.carreau.lib;

import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;
import es.ucm.gdv.blas.oses.carreau.lib.Window;
import es.ucm.gdv.blas.oses.carreau.lib.Game.LoadingScreen;
import es.ucm.gdv.blas.oses.carreau.lib.PCGame;

public class main {

    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 400;
    private static final boolean FULLSCREEN = false;
    private static final int NUM_BUFFERS = 2;

    public static void main(String[] args){
        //Creacion de la ventana
        Window ventana = new Window("OhNo!");
        boolean ventanaCreada = ventana.initWindow(WINDOW_WIDTH, WINDOW_HEIGHT, FULLSCREEN, NUM_BUFFERS);

        if(!ventanaCreada) return;

        PCGame pcGame = new PCGame(ventana, 400, 600);
        LoadingScreen loadScreen = new LoadingScreen(pcGame);
        //pcGame.setScreen(loadScreen);
        pcGame.setScreen(new MainMenuScreen(pcGame));

        pcGame.run();
    }
}