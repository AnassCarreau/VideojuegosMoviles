package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Graphics {
    /**
     * Metodo que crea una nueva imagen a partir de una ruta dada
     *
     * @param name, ruta de la imagen a cargar
     * @return Image, objeto que representa una imagen en el juego
     */
    public Image newImage(String name);

    /**
     * Metodo que crea una nueva fuente
     *
     * @param filename, String, ruta del archivo con la fuente a cargar
     * @param size,     int, tamaño de la fuente que queremos que tenga por defecto
     * @param isBold,   boolean, si dicha fuente esta en negrita
     * @return
     */
    public Font newFont(String filename, float size, boolean isBold);


    /**
     * Metodo que devuelve el ancho de las dimensiones logicas del juego
     *
     * @return int, ancho logico del juego
     */
    public int getLogWidth();

    /**
     * Metodo que devuelve el alto de las dimensiones logicas del juego
     *
     * @return int, alto logico del juego
     */
    public int getLogHeight();

    /**
     * Metodo que devuelve el ancho de la ventana en la que se ejecuta el juego
     *
     * @return int, ancho de la ventana de juego
     */
    public int getWindowWidth();

    /**
     * Metodo que devuelve el alto de la ventana en la que se ejecuta el juego
     *
     * @return int, alto de la ventana de juego
     */
    public int getWindowHeight();

    /**
     * Metodo que limpia la pantalla con el color que le pasas como parametro
     *
     * @param color, int, color en hexadecimal con el que quieres limpiar la ventana
     */
    public void clear(int color);


    /**
     * Metodo para trasladar el punto desde el cual se realizan las operaciones de pintado
     *
     * @param x, int, coordenada en x que representa el nuevo origen de dibujado
     * @param y, int, coordenada en y que representa el nuevo origen de dibujado
     */
    public void translate(float x, float y);

    /**
     * Metodo para actualizar el factor de escala del dibujado
     *
     * @param w, factor de escalado a lo ancho
     * @param h, factor de escalado a lo alto
     */
    public void scale(float w, float h);

    /**
     * Metodo para reestablecer los valores por defecto del contexto grafico
     */
    public void restore();

    /**
     * Metodo para pintar una imagen en una posicion logica (x,y) con un tamaño logico (w,h)
     *
     * @param image, Image, imagen que queremos dibujar
     * @param x,     int, coordenada x, en dimensiones logicas, en la que queremos pintar
     * @param y,     int, coordenada y, en dimensiones logicas, en la que queremos pintar
     * @param w,     int, ancho, en dimensiones logicas, que queremos que tenga nuestra imagen
     * @param h,     int, alto, en dimensiones logicas, que queremos que tenga nuestra imagen
     */
    public void drawImage(Image image, int x, int y, int w, int h);

    /**
     * Metodo para settear como color por defecto para las operaciones de
     * pintado aquel que se pasa por parametros
     *
     * @param color, int, color, en hexadecimal, que queremos utilizar
     */
    public void setColor(int color);

    /**
     * Metodo para dibujar un circulo completamente relleno
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    public void fillCircle(float cx, float cy, int r);

    /**
     * Metodo para dibujar una circunferencia (circulo no relleno)
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    public void drawCircle(float cx, float cy, int r);

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
    public void drawText(String text, Font font, int x, int y, float tam);

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
    public void drawRect(int x, int y, int width, int height, int color);
};
