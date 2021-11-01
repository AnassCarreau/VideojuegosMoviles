package es.ucm.gdv.blas.oses.carreau.lib;

//TO DO: SON INTERFACES, cambiarlos cuando esten por la implementacion de la plataforma
import java.awt.Color;
import java.io.FileInputStream;
import java.io.InputStream;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import  es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;
import  es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;


public class PCGraphics extends AbstractGraphics {

    Window window;
    java.awt.image.BufferStrategy strategy;

    //Dimensiones lógicas
    int Width;
    int Height;
    public PCGraphics(Window window, int Width, int Height) {
        this.window = window;
        this.strategy = window.getBufferStrategy();
        /*this.Width = Width;
        this.Height = Height;*/
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
    public Font newFont(String filename, float size, boolean isBold) {
        // Cargamos la fuente del fichero .ttf.
        java.awt.Font baseFont;
        try (InputStream is = new FileInputStream("assets/" + filename)) {
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
            if (isBold) baseFont = baseFont.deriveFont(java.awt.Font.BOLD, size);
            else baseFont = baseFont.deriveFont(size);


            return new PCFont(baseFont, size);
        } catch (Exception e) {
            // Ouch. No está.
            System.err.println("Error cargando la fuente: " + e);
            return null;
        }

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

    //practica
    @Override
    public void clear(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

    }

    /*@Override
    public void translate(float x, float y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.translate((int) x, (int) y);
    }

    @Override
    public void scale(float x, float y) {
        window.setSize((int) x, (int) y);
    }
    */

    @Override
    public int save() {
        return 0; //TO DO : IMPLEMENTAR
    }

    @Override
    public void restore() {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.dispose();
    }

    @Override
    public void drawRealImage(Image image, int x, int y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.drawImage(((PCImage) image)._image, x, y, null);
    }

    @Override
    public void drawRealImage(Image img, int x, int y, int w, int h) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.drawImage(((PCImage) img)._image, x, y, w, h, null);
    }


    @Override
    public void setColor(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(Color.GRAY);
    }

    @Override
    public void fillCircle(float cx, float cy, int r) {
        int diameter = r * 2;
        java.awt.Graphics g = strategy.getDrawGraphics();
        //shift x and y by the radius of the circle in order to correctly center it
        g.fillOval((int) cx - r, (int) cy - r, diameter, diameter);
    }

    @Override
    public void drawText(String text, Font font, int x, int y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setFont(((PCFont) font)._font);
        int len = g.getFontMetrics().stringWidth(text) / 2;

        g.drawString(text, (int) x - len, y);
    }

}