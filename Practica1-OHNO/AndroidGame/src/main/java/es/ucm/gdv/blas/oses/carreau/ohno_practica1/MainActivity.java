package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Deque;

import es.ucm.gdv.blas.oses.carreau.androidengine.Android.AndroidGame;
import es.ucm.gdv.blas.oses.carreau.lib.Celda;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.EstadoCelda;
import es.ucm.gdv.blas.oses.carreau.lib.Game.ChooseLevelScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.GameScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.ResourceCharger;
import es.ucm.gdv.blas.oses.carreau.lib.Pair;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;

public class MainActivity extends AppCompatActivity {

    //Referencia al juego
    private AndroidGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creacion del motor
        this.game = new AndroidGame(this, 400, 600);

        Screen newScreen = null;

        //Comprobamos si tenemos que cargar porque hemos recibido un estado que queremos volver a cargar
        if (savedInstanceState != null) {
            int screenId = savedInstanceState.getInt("Screen");
            switch (screenId) {
                case 0:
                    break;
                case 1:
                    newScreen = new MainMenuScreen();
                    break;
                case 2:
                    newScreen = new ChooseLevelScreen();
                    break;
                case 3: {
                    int dimensions = savedInstanceState.getInt("dimensiones");
                    newScreen = new GameScreen(dimensions, false);

                    Tablero t = ((GameScreen) newScreen).getTablero();
                    Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> ultimosMovs = ((GameScreen) newScreen).getUltimosMovs();

                    for (int i = 0; i < dimensions; i++) {
                        for (int j = 0; j < dimensions; j++) {
                            int state = savedInstanceState.getInt("CellState:" + j + "," + i);
                            Celda c = t.getCelda(j, i);
                            if (state == EstadoCelda.Azul.ordinal()) {
                                c.setEstado(EstadoCelda.Azul);
                                c.setValorDefault(savedInstanceState.getInt("CellDef:" + j + "," + i));
                                c.setCurrentVisibles(savedInstanceState.getInt("CellAct:" + j + "," + i));
                            } else if (state == EstadoCelda.Rojo.ordinal()) {
                                c.setEstado(EstadoCelda.Rojo);
                            } else if (state == EstadoCelda.Vacia.ordinal()) {
                                c.setEstado(EstadoCelda.Vacia);
                            }

                            c.setModificable(savedInstanceState.getBoolean("CellMod:" + j + "," + i));

                            if (!c.isModifiable()) t.addToListaNoModificables(j, i);
                        }
                    }
                    //Actualizamos las pistas que deberia de tener el tablero
                    t.compruebaPistasTablero();

                    //Cogemos la cola doble de movimientos
                    int sizeMov = savedInstanceState.getInt("MovementsSize:");
                    for (int i = 0; i < sizeMov; i++) {
                        int movCeldaState = savedInstanceState.getInt("StateMov:" + i);
                        int movX = savedInstanceState.getInt("MovX:" + i);
                        int movY = savedInstanceState.getInt("MovY:" + i);

                        EstadoCelda celdaState = null;
                        if (movCeldaState == EstadoCelda.Azul.ordinal()) {
                            celdaState = EstadoCelda.Azul;
                        } else if (movCeldaState == EstadoCelda.Rojo.ordinal()) {
                            celdaState = EstadoCelda.Rojo;
                        } else if (movCeldaState == EstadoCelda.Vacia.ordinal()) {
                            celdaState = EstadoCelda.Vacia;
                        }

                        Pair<EstadoCelda, Pair<Integer, Integer>> pairMov = new Pair<>(celdaState, new Pair(movX, movY));
                        ultimosMovs.addLast(pairMov);
                    }

                    t.setNumCeldasNoVacias(savedInstanceState.getInt("numCeldasNoVacias"));
                    break;
                }

            }

        }
        //Carga de recursos
        new ResourceCharger(game);
        //si hemos cargado un estado desde el savedInstance
        if(newScreen != null)game.setScreen(newScreen);
        // en caso contrario, main menu
        else game.setScreen(new MainMenuScreen());


        setContentView(game.getView());
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int screenID = game.getCurrentScreen().getScreenID();
        outState.putInt("Screen", screenID);

        //Si es la pantalla del juego
        if (screenID == 3) {
            //Serializamos tablero
            outState = saveBoardState(outState);
            //Serializar cola doble de deshacer
            outState = saveMovementsDeque(outState);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        game.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        game.onPause();
    }

    /**
     * Metodo para salvar el estado del tablero cuando giramos la pantalla
     *
     * @param outState, el Bundle con los datos guardados hasta el momento
     * @return, Bundle, el bundle actualizado con los datos del tablero en el
     */
    private Bundle saveBoardState(Bundle outState) {

        GameScreen gameScreen = (GameScreen) game.getCurrentScreen();
        Tablero t = gameScreen.getTablero();

        outState.putInt("dimensiones", t.getDimensions());

        for (int i = 0; i < t.getDimensions(); i++) {
            for (int j = 0; j < t.getDimensions(); j++) {
                Celda c = t.getCelda(j, i);
                EstadoCelda state = c.getEstado();
                boolean mod = c.isModifiable();
                outState.putInt("CellState:" + j + "," + i, state.ordinal());
                outState.putBoolean("CellMod:" + j + "," + i, mod);

                if (state == EstadoCelda.Azul && !mod) {
                    outState.putInt("CellDef:" + j + "," + i, c.getValorDefault());
                    outState.putInt("CellAct:" + j + "," + i, c.getCurrentVisibles());
                }

            }
        }
        outState.putInt("numCeldasNoVacias", t.getNumCeldasNoVacias());
        return outState;
    }

    /**
     * Metodo para guardar en el bundle el estado de la doble cola con los movimientos a deshacer
     *
     * @param outState, bundle con el estado que queremos salvar
     * @return Bundle, el bundle actualizado
     */
    private Bundle saveMovementsDeque(Bundle outState) {
        GameScreen gameScreen = (GameScreen) game.getCurrentScreen();
        Deque<Pair<EstadoCelda, Pair<Integer, Integer>>> ultimosMovs = gameScreen.getUltimosMovs();

        outState.putInt("MovementsSize:", ultimosMovs.size());

        int i = 0;
        while (ultimosMovs.size() != 0) {
            outState.putInt("StateMov:" + i, ultimosMovs.getFirst().getLeft().ordinal());
            outState.putInt("MovX:" + i, ultimosMovs.getFirst().getRight().getLeft());
            outState.putInt("MovY:" + i, ultimosMovs.getFirst().getRight().getRight());

            i++;
            ultimosMovs.remove();
        }

        return outState;
    }


}