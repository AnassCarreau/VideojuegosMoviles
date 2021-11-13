package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Animacion;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Celda;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.EstadoCelda;
import es.ucm.gdv.blas.oses.carreau.lib.Fade;
import es.ucm.gdv.blas.oses.carreau.lib.Pair;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;
import es.ucm.gdv.blas.oses.carreau.lib.Vector;

public class GameScreen implements Screen {
  
    //array que será de dos posiciones para que asi la pista se escriba en dos lineas
    String[] pista;
    Vector pos;
    //Pila con los últimos movimientos para así poder deshacer
    final Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> ultimosMovs;
    final HashMap<Celda, Animacion> animaTime;
    final List<Celda> quitaAnima;
    final HashMap<Celda, Fade> fadeTime;
    final List<Celda> quitafade;
    private final Tablero board;
    private final int boardDimensions;
    private boolean botonPista = false;
    private boolean cerrado = false;
    private boolean solved = false;

    int porcentaje=0;
    public GameScreen( int tableroSize, boolean randomBoard) {
        this.board = new Tablero(tableroSize, randomBoard);
        this.boardDimensions = tableroSize;
        pista = null;
        ultimosMovs = new ArrayDeque<>();

        animaTime = new HashMap<>();
        quitaAnima = new ArrayList<>();
        fadeTime = new HashMap<>();
        quitafade = new ArrayList<>();
    }

    @Override
    public void update(double deltaTime) {
        if (board.tableroResuelto() && !solved) {
            solved = true;
            //Si llegamos aqui, significa que hemos resuelto el tablero
            Assets.ganar.play(1);

            for (int i = 0; i < boardDimensions; i++) {
                for (int j = 0; j < boardDimensions; j++) {
                    Celda c = board.getCelda(j, i);
                    fadeTime.put(c, new Fade(255, 0, -300));

                }
            }
        }

        if (!animaTime.isEmpty()) {
            for (Celda key : animaTime.keySet()) {
                double time = animaTime.get(key).actTime;
                double res = time - deltaTime;
                if (res > 0) {
                    Double ot = animaTime.get(key).lstTime;
                    boolean vibrate = animaTime.get(key).vibrate;
                    if (Math.abs(res - ot) >= 0.1) {
                        ot = res;
                        vibrate = !vibrate;
                    }
                    animaTime.put(key, new Animacion(res, ot, vibrate));
                } else {

                    quitaAnima.add(key);

                }
            }

            for (Celda c : quitaAnima) {
                animaTime.remove(c);
            }
            quitaAnima.clear();
        }


        if (!fadeTime.isEmpty()) {
            for (Celda key : fadeTime.keySet()) {
                Fade f = fadeTime.get(key);
                int var = (int) (deltaTime * f.vel);
                if (f.colorIni + var > f.colorFin && f.vel > 0 || f.vel < 0 && f.colorIni + var < f.colorFin) {
                    f.colorIni = f.colorFin;
                } else f.colorIni += var;

                if ((f.vel > 0 && f.colorIni >= f.colorFin) || (f.vel < 0 && f.colorIni <= f.colorFin)) {
                    quitafade.add(key);
                }
            }
            for (Celda c : quitafade) {
                fadeTime.remove(c);
            }
            quitafade.clear();
        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(0x000000FF);
        //Juego empezado
        //dibujamos el texto que tiene que ir en la zona superior
        drawUIText(g);
        //dibujado del tablero
        drawBoard(g);
        //dibujado de las imagenes/botones de la parte inferior
        drawUIButtons(g);
    }

    @Override
    public void handleEvents(Engine engine) {
        List<TouchEvent> touchEvents = engine.getInput().getTouchEvents();

        int len = touchEvents.size();
        System.out.println("---------------------");
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                System.out.println("EVENTO DE TOUCH DOWN");
                System.out.println("Pointer: " + event.pointer);
                System.out.println("Pos x : " + event.x + " , y:" + event.y);
                botonPista = false;
                cerrado = false;
                if (checkUIButtons(event, engine)){
                    System.out.println("Pulsacion");
                    continue; //TODO ¿?continue
                }

                if (checkCirclePressed(event, engine.getGraphics())){
                    System.out.println("Pulsacion");
                    continue; //TODO ¿?continue
                }
            }
        }

        if (solved &&  fadeTime.isEmpty() ) {
            engine.setScreen(new ChooseLevelScreen());
            //TODO return??
        }
    }

    @Override
    public int getScreenID() {
        return 3;
    }

    public Tablero getTablero() {
        return board;
    }

    public Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> getUltimosMovs() {
        return ultimosMovs;
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width && event.y > y && event.y < y + height;
    }

    private boolean inBoundsCircle(TouchEvent event, int cx, int cy, int radius) {
        int rx = event.x - cx;
        int ry = event.y - cy;
        float dis = (float) Math.sqrt(Math.pow(ry, 2) + Math.pow(rx, 2));
        return dis <= radius;
    }

    /**
     * Método que dibuja el texto de la zona superior, es decir las dimensiones del tablero, una pista
     * o si se ha ganado
     *
     * @param g
     */
    private void drawUIText(Graphics g) {
        //si la pista es null dibujamos encima del tablero las dimensiones si no dibujaremos la pista
        if (!solved && pista == null) {
            g.drawText(boardDimensions + "x" + boardDimensions, Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 7, 50);
        } else if (!solved) {
            //La pista esta dividida en dos partes para visualizarla mejor en pantalla en dos lineas
            for (int i = 0; i < pista.length; i++) {
                g.drawText(pista[i], Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 4 - 100 + (25 * i), 25);
            }
        } else if (solved) {

            g.drawText("GANASTE BRO!", Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 4 - 60, 60);
        }
        g.setColor(0xCCCCCCFF);
        g.drawText(Integer.toString(porcentaje) + "%", Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() - (Assets.history.getHeight()+ 15)   , 30);
    }

    /**
     * Método que dibuja el estado actual del tablero
     *
     * @param g
     */
    private void drawBoard(Graphics g) {
        int circleSize = g.getLogWidth() / boardDimensions;
        int initialX = (int) (g.getLogWidth() / 2 - circleSize * ((float) boardDimensions / 2));

        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                Celda c = board.getCelda(j, i);

                int color = 0;

                switch (c.getEstado()) {
                    case Azul: {
                        color = 0x00BFFFFF;
                        break;
                    }
                    case Rojo: {
                        color = 0xFF3D53FF;
                        break;
                    }
                    case Vacia: {
                        color = 0xD3D3D3FF;
                        break;
                    }
                }


                if (fadeTime.containsKey(c)) {

                    Fade f= fadeTime.get(c);
                    color = modifyAlphaColor(color,f.colorIni);
                }
                g.setColor(color);


                int x = initialX + circleSize / 2 + j * circleSize;
                int y = (g.getLogHeight() / 6) + circleSize / 2 + (i * circleSize);

                if (animaTime.containsKey(c)) {

                    g.fillCircle(x, y, circleSize / 2 + 1 * (animaTime.get(c).vibrate ? -1 : 1));
                } else {
                    g.fillCircle(x, y, circleSize / 2);
                }

                //Si es de las azules fijas, pintamos sus numeros correspondientes
                if (c.getValorDefault() != 0 && !c.isModifiable()) {
                    g.setColor(0xFFFFFFFF);
                    g.drawText(Integer.toString(c.getValorDefault()), Assets.josefisans, x, y + circleSize / 4, 2 * circleSize / 3.0f);
                } else if (!c.isModifiable() && cerrado) {
                    g.drawImage(Assets.lock, x - Assets.lock.getWidth() / 4, y - Assets.lock.getHeight() / 4, Assets.lock.getWidth() / 2, Assets.lock.getHeight() / 2);

                }
            }
        }
        //Si se ha pulsado el boton de pista dibujamos un reborde negro sobre la casilla que da dicha pista
        if (botonPista) {
            int x = initialX + circleSize / 2 + pos.x * circleSize;
            int y = (g.getLogHeight() / 6) + circleSize / 2 + (pos.y * circleSize);
            g.setColor(0x000000FF);
            g.drawCircle(x, y, (circleSize) / 2);
        }


    }

    /**
     * Método que dibuja los botones de la parte inferior, cerrar, deshacer y pista
     *
     * @param g
     */
    private void drawUIButtons(Graphics g) {
        g.drawImage(Assets.close, g.getLogWidth() / 5 - Assets.close.getWidth(), g.getLogHeight() - Assets.close.getHeight(), Assets.close.getWidth() / 2, Assets.close.getHeight() / 2);
        g.drawImage(Assets.history, g.getLogWidth() / 5 * 3 - Assets.history.getWidth(), g.getLogHeight() - Assets.history.getHeight(), Assets.history.getWidth() / 2, Assets.history.getHeight() / 2);
        g.drawImage(Assets.eye, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight() - Assets.eye.getHeight(), Assets.eye.getWidth() / 2, Assets.eye.getHeight() / 2);
    }

    /**
     * Método que comprueba si un circulo del tablero ha sido pulsado
     *
     * @param event
     * @param g
     * @return boolean
     */
    private boolean checkCirclePressed(TouchEvent event, Graphics g) {
        int circleSize = g.getLogWidth() / boardDimensions;
        int initialX = (int) (g.getLogWidth() / 2 - circleSize * ((float) boardDimensions / 2));

        for (int j = 0; j < boardDimensions; j++) {
            for (int k = 0; k < boardDimensions; k++) {
                Celda c = board.getCelda(k, j);
                int x = initialX + circleSize / 2 + k * circleSize;
                int y = (g.getLogHeight() / 6) + circleSize / 2 + (j * circleSize);
                if (inBoundsCircle(event, x, y, circleSize / 2)) {
                    Assets.click.play(1);
                    pista = null;
                    if (c.isModifiable()) {
                        //guardado del ultimo movimiento en una pila
                        if (ultimosMovs.size() + 1 > 50) {
                            ultimosMovs.remove();
                        }
                        ultimosMovs.addLast(new Pair(c.getEstado(), new Pair(j, k)));
                        board.cambiaCelda(k, j);
                        porcentaje=board.porcentajeCeldas();
                        fadeTime.put(c, new Fade(125, 254, 1000));
                        return true;
                    } else if (!c.isModifiable()) {
                        if (c.getEstado() == EstadoCelda.Rojo) cerrado = true;

                        animaTime.put(c, new Animacion(0.80, 0.00, true));
                        pista = new String[]{"This cell cannot be modified"};
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Métodos que chequea si hemos pulsado uno de los botones inferiores de las UI
     *
     * @param event
     * @return boolean
     */
    private boolean checkUIButtons(TouchEvent event, Engine engine) {
        Graphics g = engine.getGraphics();
        if (inBounds(event, g.getLogWidth() / 5 - Assets.close.getWidth(), g.getLogHeight() - Assets.close.getHeight(), Assets.close.getWidth() / 2, Assets.close.getHeight() / 2)) {
            engine.setScreen(new ChooseLevelScreen());
            return true;
        } else if (inBounds(event, g.getLogWidth() / 5 * 3 - Assets.history.getWidth(), g.getLogHeight() - Assets.history.getHeight(), Assets.history.getWidth() / 2, Assets.history.getHeight() / 2)) {
            if (ultimosMovs.size() != 0) {
                Pair<EstadoCelda, Pair<Integer, Integer>> pairAux = ultimosMovs.getLast();
                board.cambiaCeldaInversa(pairAux.getRight().getRight(), pairAux.getRight().getLeft());
                porcentaje=board.porcentajeCeldas();
                ultimosMovs.removeLast();
            } else {
                pista = new String[]{"Nothing to undo."};
            }
            return true;
        } else if (inBounds(event, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight() - Assets.eye.getHeight(), Assets.eye.getWidth() / 2, Assets.eye.getHeight() / 2)) {
            //eye
            botonPista = true;
            System.out.println("---");
            Pair<String, Vector> p = board.damePistaAleatoria();
            pista = p.getLeft().split("#");
            pos = p.getRight();
            return true;
        }
        return false;
    }

    private int modifyAlphaColor(int colorToChange, int newAlpha){
        colorToChange =  colorToChange & 0xFFFFFF00 | newAlpha & 0X000000FF;
        return colorToChange;
    }
}



