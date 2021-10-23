package es.ucm.gdv.blasosescarreau.pcengine;

//TO DO: SON INTERFACES, cambiarlos cuando esten por la implementacion de la plataforma
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import  es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;
import  es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;




import java.lang.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class PCGraphics implements Graphics {

    Window window;
    java.awt.image.BufferStrategy strategy;


    //Dimensiones lógicas
    int Width;
    int Height;

    public PCGraphics(Window window, int Width, int Height) {
        this.window = window;
        this.strategy = window.getBufferStrategy();
        this.Width = Width;
        this.Height = Height;


    }

    @Override
    public Image newImage(String name) {
        name = "assets/" + name;
        PCImage image = null;

        try {
            java.awt.Image img = javax.imageio.ImageIO.read(new java.io.File(name));
            image = new PCImage(img);
        } catch (Exception e) {
            System.err.println(e);
        }

        return image;
    }

    //TO DO: mirar la putisima font :D
    @Override
    public Font newFont(String filename, int size, boolean isBold) {

    }

    @Override
    public void drawPixel(int x, int y, int color) {

    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.drawLine(x, y, x2, y2);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.drawRect(x, y, width, height);
    }

    @Override
    public void drawImage(Image img, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight) {

    }

    //seria para coger el tamaño del canvas¿?
    @Override
    public int getWidth() {
        return this.Width;
    }

    @Override
    public int getHeight() {
        return this.Height;
    }

    //practica
    @Override
    public void clear(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.fillRect(0, 0, getWidth(), getHeight());

    }

    @Override
    public void translate(float x, float y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.translate((int)x,(int) y);
    }

    @Override
    public void scale(float x, float y) {
        window.setSize((int)x,(int)y);
    }

    @Override
    public int save() {

    }

    @Override
    public void restore() {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.dispose();
    }

    @Override
    public void drawImage(Image image, int x, int y) {

    }

    @Override
    public void setColor(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
    }

    @Override
    public void fillCircle(float cx, float cy, int r) {
        int diameter = r * 2;
        java.awt.Graphics g= strategy.getDrawGraphics();
        //shift x and y by the radius of the circle in order to correctly center it
        g.fillOval((int)cx - r, (int)cy - r, diameter, diameter);
    }

    @Override
    public void drawText(String text, int x, int y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.drawString(text, x, y);
    }

}