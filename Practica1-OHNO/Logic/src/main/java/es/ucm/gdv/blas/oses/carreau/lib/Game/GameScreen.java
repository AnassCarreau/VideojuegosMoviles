package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;
import java.util.Deque;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Celda;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.EstadoCelda;
import es.ucm.gdv.blas.oses.carreau.lib.Pair;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;

public class GameScreen implements Screen {
    private Engine engine;
    private Tablero board;
    private int boardDimensions;

    //array que será de dos posiciones para que asi la pista se escriba en dos lineas
    String[] pista;

    //Pila con los últimos movimientos para así poder deshacer
    Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> ultimosMovs;

    private boolean solved = false;

    public GameScreen(Engine eng, int tableroSize, boolean randomBoard) {
        this.engine = eng;
        this.board = new Tablero(tableroSize, randomBoard);
        this.boardDimensions = tableroSize;
        pista = null;
        ultimosMovs = new ArrayDeque<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double deltaTime) {
        if(board.tableroResuelto()) solved = true;
    }

    @Override
    public void render() {
        Graphics g = engine.getGraphics();

        g.setColor(0x000000FF);
        //Juego empezado
        //si la pista es null dibujamos encima del tablero las dimensiones si no dibujaremos la pista
        if(!solved && pista == null){
            g.drawText(Integer.toString(boardDimensions) + "x" + Integer.toString(boardDimensions), Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 7 ,50);
        }
        else if(!solved){
            //La pista esta dividida en dos partes para visualizarla mejor en pantalla en dos lineas
            for(int i = 0; i < pista.length; i++){
                g.drawText(pista[i] ,Assets.josefisans,g.getLogWidth() / 2, g.getLogHeight() / 4 - (50 * 2) + (25 * i),25);
            }
        }
        else{
            //Si llegamos aqui, significa que hemos resuelto el tablero
            g.drawText("GANASTE BRO!", Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 4 - 60 * 2,60);
        }

        int circleSize = g.getLogWidth() / boardDimensions;

        int initialX = (int) (g.getLogWidth() / 2 - circleSize * ((float) boardDimensions / 2));
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                Celda c = board.getCelda(j, i);
                boolean hasNumber = false;
                switch (c.getEstado()) {
                    case Azul: {
                        g.setColor(0x0000FFFF);
                        hasNumber = true;
                        break;
                    }
                    case Rojo: {
                        g.setColor(0xFF0000FF);
                        break;
                    }
                    case Vacia: {
                        g.setColor(0xD3D3D3FF);
                        break;
                    }
                }

                int x = initialX + circleSize / 2 + i * circleSize;
                int y = (g.getLogHeight() / 6) + circleSize / 2 + (j * circleSize);

                g.fillCircle(x, y, circleSize / 2);

                //Si es de las azules fijas, pintamos sus numeros correspondientes
                if (hasNumber && !c.isModifiable()) {
                    g.setColor(0xFFFFFFFF);
                    g.drawText(Integer.toString(c.getValorDefault()), Assets.josefisans, x, y + circleSize/4 ,2*circleSize/3);
                }
            }
        }

        g.drawImage(Assets.close, g.getLogWidth() / 5 - Assets.close.getWidth(), g.getLogHeight() - Assets.close.getHeight(), Assets.close.getWidth() / 2, Assets.close.getHeight() / 2);
        g.drawImage(Assets.history, g.getLogWidth() / 5 * 3 - Assets.history.getWidth(), g.getLogHeight() - Assets.history.getHeight(), Assets.history.getWidth() / 2, Assets.history.getHeight() / 2);
        g.drawImage(Assets.eye, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight() - Assets.eye.getHeight(), Assets.eye.getWidth() / 2, Assets.eye.getHeight() / 2);
    }

    @Override
    public void handleEvents() {
        Graphics g = engine.getGraphics();
        List<TouchEvent> touchEvents = engine.getInput().getTouchEvents();
        //engine.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, g.getLogWidth() / 5 - Assets.close.getWidth(), g.getLogHeight() - Assets.close.getHeight(), Assets.close.getWidth() / 2, Assets.close.getHeight() / 2)) {
                    engine.setScreen(new ChooseLevelScreen(engine));
                    return;
                } else if (inBounds(event, g.getLogWidth() / 5 * 3 - Assets.history.getWidth(), g.getLogHeight() - Assets.history.getHeight(), Assets.history.getWidth() / 2, Assets.history.getHeight() / 2)) {
                    if(ultimosMovs.size() != 0){
                        Pair<EstadoCelda, Pair<Integer, Integer>> pairAux = ultimosMovs.getLast();
                        Celda auxCelda = board.getCelda(pairAux.getRight().getLeft(), pairAux.getRight().getRight());
                        auxCelda.setEstado(pairAux.getLeft());
                        ultimosMovs.removeLast();
                    }
                    else{
                        pista = new String[]{"Nothing to undo."};
                    }
                    return;
                } else if (inBounds(event, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight() - Assets.eye.getHeight(), Assets.eye.getWidth() / 2, Assets.eye.getHeight() / 2)) {
                    //eye
                    pista = board.damePistaAleatoria().split("#");
                    return;
                }
                int circleSize = g.getLogWidth() / boardDimensions;
                int initialX = (int) (g.getLogWidth() / 2 - circleSize * ((float) boardDimensions / 2));
                for (int j = 0; j < boardDimensions; j++) {
                    for (int k = 0; k < boardDimensions; k++) {
                        Celda c = board.getCelda(k, j);
                        int x = initialX + circleSize / 2 + j * circleSize;
                        int y = (g.getLogHeight() / 6) + circleSize / 2 + (k * circleSize);
                        if (c.isModifiable() && inBoundsCircle(event, x, y , circleSize/2)) {
                            //ponemos la pista a null si se ha pulsado ya en algun circulo del tablero
                            pista = null;
                            //guardado del ultimo movimiento en una pila
                            if(ultimosMovs.size() + 1 > 50){
                                ultimosMovs.remove();
                            }
                            ultimosMovs.addLast(new Pair(c.getEstado(), new Pair(k, j)));
                            board.cambiaCelda( k, j);
                            return;
                        }
                        else if(!c.isModifiable() && inBoundsCircle(event, x, y , circleSize/2)){
                            pista = new String[]{"This cell cannot be modified"};
                        }
                    }
                }

            }
        }
    }

    @Override
    public int getScreenID() {
        return 3;
    }


    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width && event.y > y && event.y < y + height;
    }


    private boolean inBoundsCircle(TouchEvent event, int cx, int cy, int radius) {
        int rx = event.x - cx;
        int ry = event.y - cy;
        float dis= (float)Math.sqrt(Math.pow(ry,2) +  Math.pow(rx,2));
        return dis <= radius;
    }

    public Tablero getTablero(){
        return board;
    }

    public Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> getUltimosMovs(){
        return ultimosMovs;
    }

}



