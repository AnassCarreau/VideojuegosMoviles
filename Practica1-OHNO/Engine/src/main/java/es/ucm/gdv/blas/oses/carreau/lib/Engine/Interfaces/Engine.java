package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Engine {
    /**
     * Metodo que devuelve el motor que se encarga de gestionar la
     * entrada del juego
     *
     * @return el motor de Input
     */
    Input getInput();

    /**
     * Metodo que devuelve el motor que se encarga de gestionar
     * el audio de juego
     *
     * @return el motor de audio
     */
    Audio getAudio();

    /**
     * Metodo que devuelve el motor que se encarga del pintado
     * del juego
     *
     * @return el motor grafico
     */
    Graphics getGraphics();

    /**
     * Metodo para actualizar cual es la pantalla del juego actual
     * en la que nos encontramos
     *
     * @param screen, pantalla/nivel/estado al que vamos a cambiar
     */
    void setScreen(Screen screen);

    /**
     * Metodo para obtener cual es la pantalla/nivel/estado de juego
     * actual
     *
     * @return
     */
    Screen getCurrentScreen();
}