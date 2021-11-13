package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileInputStream;
import java.io.InputStream;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;


public class PCGraphics extends AbstractGraphics implements ComponentListener {

    final Window window;
    final java.awt.image.BufferStrategy strategy;
    Color currentColor = Color.WHITE;
    java.awt.Graphics g;

    /**
     * Constructora del motor grafico para la implementacion especifica de PC
     *
     * @param window,    ventana sobre la que vamos a pintar
     * @param logWidth,  int, ancho logico por defecto de nuestro juego
     * @param logHeight, int, alto lógico por defecto de nuestro juego
     */
    public PCGraphics(Window window, int logWidth, int logHeight) {
        this.window = window;
        this.strategy = window.getBufferStrategy();
        this.logWidth = logWidth;
        this.logHeight = logHeight;
        this.winWidth = window.getWidth();
        this.winHeight = window.getHeight();
    }

    /**
     * Metodo para trasladar el punto desde el cual se realizan las operaciones de pintado
     *
     * @param x, int, coordenada en x que representa el nuevo origen de dibujado
     * @param y, int, coordenada en y que representa el nuevo origen de dibujado
     */
    @Override
    public final void translate(float x, float y) {
        g.translate((int) x, (int) y);
    }

    /**
     * Metodo para actualizar el factor de escala del dibujado
     *
     * @param x, factor de escalado a lo ancho
     * @param y, factor de escalado a lo alto
     */
    @Override
    public final void scale(float x, float y) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(x, y);
    }

    /**
     * Metodo para reestablecer los valores por defecto del contexto grafico
     */
    @Override
    public final void restore() {
        g.dispose();
    }


    /**
     * Metodo que crea una nueva imagen a partir de una ruta dada
     *
     * @param name, ruta de la imagen a cargar
     * @return Image, objeto que representa una imagen en el juego, en su
     * implementacion especifica
     */
    @Override
    public final Image newImage(String name) {
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

    /**
     * Metodo que crea una nueva fuente
     *
     * @param filename, String, ruta del archivo con la fuente a cargar
     * @param size,     int, tamaño de la fuente que queremos que tenga por defecto
     * @param isBold,   boolean, si dicha fuente esta en negrita
     * @return el nuevo objeto fuente creado, en su implementacione especifica
     */
    @Override
    public final Font newFont(String filename, float size, boolean isBold) {
        // Cargamos la fuente del fichero .ttf.
        java.awt.Font baseFont;
        try (InputStream is = new FileInputStream("assets/" + filename)) {
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
            if (isBold) baseFont = baseFont.deriveFont(java.awt.Font.BOLD, size);
            else baseFont = baseFont.deriveFont(size);

            return new PCFont(baseFont);
        } catch (Exception e) {
            System.err.println("Error cargando la fuente: " + e);
            return null;
        }

    }

    /**
     * Metodo que limpia la pantalla con el color que le pasas como parametro
     *
     * @param color, int, color en hexadecimal con el que quieres limpiar la ventana
     */
    @Override
    public final void clear(int color) {
        setColor(color);
        g.fillRect(0, 0, getWindowWidth(), getWindowHeight());

    }

    /**
     * Metodo para pintar una imagen en una posicion logica (x,y) con un tamaño logico (w,h)
     *
     * @param img, Image, imagen que queremos dibujar
     * @param x,   int, coordenada x, en dimensiones logicas, en la que queremos pintar
     * @param y,   int, coordenada y, en dimensiones logicas, en la que queremos pintar
     * @param w,   int, ancho, en dimensiones logicas, que queremos que tenga nuestra imagen
     * @param h,   int, alto, en dimensiones logicas, que queremos que tenga nuestra imagen
     */
    @Override
    public final void drawImage(Image img, int x, int y, int w, int h) {
        g.drawImage(((PCImage) img)._image, x, y, w, h, null);
    }

    /**
     * Metodo para settear como color por defecto para las operaciones de
     * pintado aquel que se pasa por parametros
     *
     * @param color, int, color, en hexadecimal, que queremos utilizar
     */
    @Override
    public final void setColor(int color) {
        int red = (int) ((color & 0xffffffffL) >> 24);
        int green = (color & 0x00ff0000) >> 16;
        int blue = (color & 0x0000ff00) >> 8;
        int alpha = color & 0x000000ff;
        currentColor = new Color(red, green, blue, alpha);
        g.setColor(currentColor);
    }

    /**
     * Metodo para dibujar un circulo completamente relleno
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    @Override
    public final void fillCircle(float cx, float cy, int r) {
        int diameter = r * 2;

        g.fillOval((int) cx - r, (int) cy - r, diameter, diameter);
    }

    /**
     * Metodo para dibujar una circunferencia (circulo no relleno)
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    @Override
    public final void drawCircle(float cx, float cy, int r) {
        int diameter = r * 2;

        g.drawOval((int) cx - r, (int) cy - r, diameter, diameter);
    }

    /**
     * Metodo para dibujar un texto centrado en las coordenadas x y
     *
     * @param text, String, texto que queremos dibujar
     * @param font, Font, fuente que vamos a usar para renderizar el texto
     * @param x,    int, coordenada x, en el espacio de coordenadas lógicas en la que queremos
     *              que este centrado el texto
     * @param y,    int, coordenada y, en el espacio de coordenadas lógicas en la que queremos
     *              que este centrado el texto
     * @param tam,  float, tamaño que queremos emplear al pintar la fuente
     */
    @Override
    public final void drawText(String text, Font font, int x, int y, float tam) {
        g.setFont(((PCFont) font)._font.deriveFont(tam));
        int len = g.getFontMetrics().stringWidth(text) / 2;
        g.drawString(text, x - len, y);
    }

    /**
     * Metodo para dibujar un rectangulo/cuadrado sin rellenar
     *
     * @param x,      int, en el espacio de coordenadas logicas, coordenada x de la esquina superior
     *                izquierda del rectangulo
     * @param y,      int, en el espacio de coordenadas logicas, coordenada y de la esquina superior
     *                izquierda del rectangulo
     * @param width,  int, ancho, en coordenadas logicas, del rectangulo
     * @param height, int, alto, en coordenadas logicas, del rectangulo
     * @param color,  int, color en hexadecimal de la linea con la que se pinta el rectangulo
     */
    @Override
    public final void drawRect(int x, int y, int width, int height, int color) {
        g.setColor(new Color(color));
        g.drawRect(x, y /*+ yBorder*/, width, height);
    }


    /**
     * Metodo abstracto de ComponentListener, cuando la ventana
     * cambia sus dimensiones se llama a este metodo
     *
     * @param componentEvent
     */
    @Override
    public final void componentResized(ComponentEvent componentEvent) {
        winWidth = window.getWidth();
        winHeight = window.getHeight();
    }

    /**
     * Metodo que llamamos para tener un nuevo contexto grafico
     * en cada iteracion del bucle
     */
    public final void updateContext() {
        g = strategy.getDrawGraphics();
    }

    /**
     * Metodo abstracto de ComponentListener, no es necesaria su
     * implementacion
     *
     * @param componentEvent
     */
    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    /**
     * Metodo abstracto de ComponentListener, no es necesaria su
     * implementacion
     *
     * @param componentEvent
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }

    /**
     * Metodo abstracto de ComponentListener, no es necesaria su
     * implementacion
     *
     * @param componentEvent
     */
    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }
}