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
        pcGame.setScreen(loadScreen);
        loadScreen.init();
        pcGame.setScreen(new MainMenuScreen(pcGame));

        pcGame.run();

        /*Tablero t = new Tablero(4);
/*
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());


        t.drawConsole();

        System.out.println("Resolvemos");
        System.out.println(t.damePistaAleatoria());

        t.setPos(0,0);
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());

        t.setPos(0,2);
        t.cambiaCelda();
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());

        t.setPos(1,3);
        t.cambiaCelda();
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());

        t.setPos(2,3);
        t.cambiaCelda();
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());

        t.setPos(3,1);
        t.cambiaCelda();
        t.drawConsole();
        System.out.println(t.damePistaAleatoria());

        System.out.println("Da correcto el resuelve tablero: " + t.tableroResuelto());
        */
    }
}