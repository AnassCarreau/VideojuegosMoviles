package es.ucm.gdv.blas.oses.carreau.lib.Engine;


import org.w3c.dom.css.Rect;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;

public abstract class AbstractGraphics implements Graphics {

    //Dimensiones lógicas
    protected int logWidth;
    protected int logHeight;

    //Dimensiones de ventana fisicas
    protected float winWidth;
    protected float winHeight;

    //Métodos getter para las dimensiones lógicas y físicas
    @Override
    public int getLogWidth(){
        return logWidth;
    }
    @Override
    public int getLogHeight(){
        return logHeight;
    }
    //Estos dos métodos se usan solo en el clear de PCGraphics
    @Override
    public int getWindowWidth(){
        return (int)winWidth;
    }
    @Override
    public final int getWindowHeight(){
        return (int)winHeight;
    }

    //Método que nos devuelve el factor de escalado a aplicar
    private float getScaleFactor(){
        float factorWidth = winWidth / logWidth;
        float factorHeight = winHeight / logHeight;

        if(factorWidth < factorHeight) return factorWidth;
        else return factorHeight;
    }

    /**
     * Método que nos devuelve un array de tamaño 4 con la posicion en x, y real, al igual que el tamaño
     * @param x
     * @param y
     * @param w
     * @param h
     * @return int[4]
     */
    private int[] getRealDestRect(int x, int y, int w, int h){
        float scale_factor = getScaleFactor();

        //trasladamos las coordenadas haciendo uso del scale_factor
        int[] posTranslate = translate(x, y, scale_factor);

        //escalamos el tamaño haciendo uso del scale_factor
        int[] tamScale = scale(x, y, scale_factor);

        return new int[]{posTranslate[0], posTranslate[1], tamScale[0], tamScale[1]};
    }

    @Override
    public Image newImage(String name) {
        return null;
    }

    @Override
    public Font newFont(String filename, float size, boolean isBold) {
        return null;
    }

    @Override
    public void drawPixel(int x, int y, int color) {

    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {

    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {

    }

    @Override
    public void clear(int color) {

    }

    @Override
    public int[] translate(int x, int y, float scale_factor) {
        int newX1 = (int)(winWidth/2 - ((logWidth * scale_factor) / 2));
        int newY1 = (int)(winHeight/2 - ((logHeight * scale_factor) / 2));

        int xDest = (int)(newX1 + (x * scale_factor));
        int yDest = (int)(newY1 + (y * scale_factor));

        return new int[]{xDest, yDest};
    }

    @Override
    public int[] scale(int w, int h, float scale_factor) {
        int wDest = (int)(w * scale_factor);
        int hDest = (int)(h * scale_factor);

        return new int[]{wDest, hDest};
    }

    @Override
    public int save() {
        return 0;
    }

    @Override
    public void restore() {

    }

    @Override
    public void drawImage(Image image, int x, int y) {
        int[] rectDest = getRealDestRect(x, y, 0, 0);
        drawRealImage(image, rectDest[0], rectDest[1]);
    }

    @Override
    public void drawImage(Image image, int x, int y, int w, int h) {
        int[] rectDest = getRealDestRect(x, y, w, h);
        drawRealImage(image, rectDest[0], rectDest[1], rectDest[2], rectDest[3]);
    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public void fillCircle(float cx, float cy, int r) {

    }

    @Override
    public void drawText(String text, Font font, int x, int y) {

    }

    //Método implementado por cada módulo en PCGraphics y en AndroidGraphics para dibujar la imagen
    //con las coordenadas y tamaño ya habiendo pasado por el factor de escalado
    public abstract void drawRealImage(Image image, int x, int y, int w, int h);

    public abstract void drawRealImage(Image image, int x, int y);
}
