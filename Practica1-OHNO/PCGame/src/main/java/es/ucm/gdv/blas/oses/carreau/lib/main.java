package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Scanner;


import es.ucm.gdv.blas.oses.carreau.lib.Tablero;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;


public class main {
    public static void main(String[] args){
        Tablero t = new Tablero(4);


        t.drawConsole();

        System.out.println("Resolvemos");

        t.setPos(0,2);
        t.cambiaCelda();

        t.setPos(1,3);
        t.cambiaCelda();

        t.setPos(2,3);
        t.cambiaCelda();

        t.setPos(3,1);
        t.cambiaCelda();

        t.drawConsole();

        System.out.println("Da correcto el resuelve tablero: " + t.tableroResuelto());
    }
}