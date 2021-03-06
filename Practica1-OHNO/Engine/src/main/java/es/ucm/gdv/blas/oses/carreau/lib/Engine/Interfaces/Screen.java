package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Screen {
    /**
     * Metodo que se encarga de actualizar el estado de los objetos de
     * juego
     *
     * @param deltaTime, double, tiempo transcurrido desde la ultima iteracion
     *                   del bucle principal de juego
     */
    void update(double deltaTime);

    /**
     * Metodo que se encarga de pintar todos los elementos
     * del juego
     */
    void render(Graphics g);

    /**
     * Metodo que se encarga de procesar los eventos de Input
     * y realizar cambios en relacion a los mismos
     */
    void handleEvents(Engine engine) ;

    /**
     * Metodo que devuelve el identificador de la pantalla de juego
     * Añadido para el guardado del estado de juego
     *
     * @return int, numero con el identificador de la pantalla de juego
     */
    int getScreenID();
}