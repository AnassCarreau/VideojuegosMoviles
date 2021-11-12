package es.ucm.gdv.blas.oses.carreau.lib;

import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.LoadingScreen;

public class main {

    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 400;
    private static final boolean FULLSCREEN = false;
    private static final int NUM_BUFFERS = 2;

    public static void main(String[] args){
        //Creacion de la ventana
        Window ventana = new Window("OhNo!");

        //Si no conseguimos crear la ventana terminamos
        if(!ventana.initWindow(WINDOW_WIDTH, WINDOW_HEIGHT, FULLSCREEN, NUM_BUFFERS)) return;

        //Creamos el engine
        PCGame pcGame = new PCGame(ventana, 400, 600);
        //Pantalla de carga de recursos
        LoadingScreen loadScreen = new LoadingScreen(pcGame);
        //Cargamos pantalla de menu principal
        pcGame.setScreen(new MainMenuScreen(pcGame));
        //Comenzamos juego
        pcGame.run();
    }
}