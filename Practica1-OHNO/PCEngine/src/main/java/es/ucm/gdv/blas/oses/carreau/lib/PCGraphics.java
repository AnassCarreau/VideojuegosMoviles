package es.ucm.gdv.blas.oses.carreau.lib;

//TO DO: SON INTERFACES, cambiarlos cuando esten por la implementacion de la plataforma
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileInputStream;
import java.io.InputStream;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import  es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;


public class PCGraphics extends AbstractGraphics implements ComponentListener {

    Window window;
    java.awt.image.BufferStrategy strategy;
    //int yBorder = 0;


    public PCGraphics(Window window, int Width, int Height) {
        this.window = window;
        this.strategy = window.getBufferStrategy();
        this.logWidth = Width;
        this.logHeight = Height;
        this.winWidth = window.getWidth();
        this.winHeight = window.getHeight();
        //yBorder = window.getInsets().top;

        //System.out.println(yBorder);

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
    public void drawRealRect(int x, int y, int width, int height, int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.drawRect(x, y /*+ yBorder*/, width, height);
    }

    //practica
    @Override
    public void clear(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        int red = (int)((color & 0xffffffffL) >> 24);
        int green = (color & 0x00ff0000)>> 16;
        int blue = (color & 0x0000ff00)>>8;
        int alpha = color & 0x000000ff;
        Color c = new Color(red, green,blue,alpha);
        g.setColor(c);
        g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

    }
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
        g.drawImage(((PCImage) image)._image, x, y/* + yBorder*/, null);
    }


    @Override
    public void drawRealImage(Image img, int x, int y, int w, int h) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.drawImage(((PCImage) img)._image, x, y /*+ yBorder*/, w, h, null);
    }


    @Override
    public void setColor(int color) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
    }

    @Override
    public void fillRealCircle(float cx, float cy, int r) {
        int diameter = r * 2;
        java.awt.Graphics g = strategy.getDrawGraphics();
        //shift x and y by the radius of the circle in order to correctly center it
        g.fillOval((int) cx - r, (int) cy - r, diameter, diameter);
    }

    @Override
    public void drawRealText(String text, Font font, int x, int y) {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setFont( ((PCFont) font)._font.deriveFont((float) (((PCFont) font)._font).getSize() * getScaleFactor()));

        int len = g.getFontMetrics().stringWidth(text) / 2;

        g.drawString(text, (int) x - len, y /*+ yBorder*/);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        winWidth = window.getWidth();
        winHeight = window.getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {}
    @Override
    public void componentShown(ComponentEvent componentEvent) {}
    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
}