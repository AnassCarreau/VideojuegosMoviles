package es.ucm.gdv.blas.oses.carreau.lib.Game;


import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;

public class ResourceCharger {
    private Engine engine;

    public ResourceCharger(Engine engine) {
        this.engine = engine;
        Graphics g = engine.getGraphics();
        Assets.q42 = g.newImage("q42.png");
        Assets.lock = g.newImage("lock.png");
        Assets.history = g.newImage("history.png");
        Assets.eye = g.newImage("eye.png");
        Assets.close = g.newImage("close.png");
        Assets.josefisans = g.newFont("JosefinSans-Bold.ttf", 1, true);
        Assets.molleregular = g.newFont("Molle-Regular.ttf", 1, true);
        Assets.click = engine.getAudio().newSound("1.wav");
        Assets.ganar = engine.getAudio().newSound("Ganaste.wav");
    }

}