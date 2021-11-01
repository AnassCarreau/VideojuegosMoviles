package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;

public interface Graphics {
    public Image newImage(String name);

    public Font newFont(String filename, float size, boolean isBold);

    public void drawRect(int x, int y, int width, int height, int color);

    //getters para las dimensiones logicas, deberian estar en la interfaz¿?
    public int getLogWidth();

    public int getLogHeight();

    //getters para las dimensiones fisicas, deberian estar en la interfaz¿?
    public int getWindowWidth();

    public int getWindowHeight();

    //practica
    public void clear(int color);

    public int[] translate(int x, int y, float scale_factor);

    public int[] scale(int w, int h, float scale_factor);

    public int save();

    public void restore();

    public void drawImage(Image image, int x, int y);

    public void drawImage(Image image, int x, int y,int w,int h);

    public void setColor(int color);

    public void fillCircle(float cx, float cy, int r);

    public void drawText(String text,Font font, int x, int y);
};
