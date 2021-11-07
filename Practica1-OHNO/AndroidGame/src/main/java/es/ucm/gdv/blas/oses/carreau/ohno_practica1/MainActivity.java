package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;


//Imports del juego
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.gdv.blas.oses.carreau.lib.Celda;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.androidengine.Android.AndroidGame;
import es.ucm.gdv.blas.oses.carreau.lib.EstadoCelda;
import es.ucm.gdv.blas.oses.carreau.lib.Game.ChooseLevelScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.GameScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.LoadingScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;


public class MainActivity extends AppCompatActivity {

    //Referencia al juego
    private AndroidGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.game = new AndroidGame(this, 400, 600);
        Screen newScreen = null;
        if (savedInstanceState != null) {

            int screenId = savedInstanceState.getInt("Screen");

            switch (screenId){
                case 0:
                    break;
                case 1:
                    newScreen = new MainMenuScreen(this.game);
                    break;
                case 2:
                    newScreen = new ChooseLevelScreen(this.game);
                    break;
                case 3:
                {
                    int dimensions = savedInstanceState.getInt("dimensiones");
                    newScreen = new GameScreen(this.game, dimensions, false);

                    Tablero t =((GameScreen)newScreen).getTablero();

                    //TO DO: REVISAR CREACION DEL TABLERO
                    for(int i = 0; i < dimensions; i++){
                        for(int j = 0; j < dimensions; j++){
                            int state = savedInstanceState.getInt("CellState:" + j + "," + i);
                            Celda c = t.getCelda(j,i);
                            if(state == EstadoCelda.Azul.ordinal()) {
                                c.setEstado(EstadoCelda.Azul);
                                c.setValorDefault(savedInstanceState.getInt("CellDef:" + j + "," + i));
                                c.setCurrentVisibles(savedInstanceState.getInt("CellAct:" + j + "," + i));
                            }
                            else if (state == EstadoCelda.Rojo.ordinal()){
                                c.setEstado(EstadoCelda.Rojo);
                            }
                            else if(state == EstadoCelda.Vacia.ordinal()){
                                c.setEstado(EstadoCelda.Vacia);
                            }
                            c.setModificable(savedInstanceState.getBoolean("CellMod:"+ j + "," + i));

                            if(!c.isModifiable())t.addToListaNoModificables(j,i);
                        }
                    }
                    //Actualizamos las pistas que deberia de tener el tablero
                    t.compruebaPistas();

                    break;
                }

            }

        }

        //Carga de recursos
        LoadingScreen loadScreen = new LoadingScreen(game);
        game.setScreen(loadScreen);
        loadScreen.init();

        //si hemos cargado un estado desde el savedInstance
        if(newScreen != null){
            game.setScreen(newScreen);
        }
        // en caso contrario, main menu
        else{
            game.setScreen(new MainMenuScreen(this.game));
        }

        setContentView(game.getView());
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int screenID = game.getCurrentScreen().getScreenID();
        outState.putInt("Screen", screenID);

        //Si es la pantalla del juego
        if(screenID == 3){
            //Serializamos tablero
            outState = saveBoardState(outState);
            //TO DO:Serializar stack de deshacer
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

    private Bundle saveBoardState(Bundle outState){

        GameScreen  gameScreen = (GameScreen) game.getCurrentScreen();
        Tablero t = gameScreen.getTablero();

        outState.putInt("dimensiones",t.getDimensions());

        for(int i = 0; i < t.getDimensions(); i++){
            for(int j = 0; j < t.getDimensions(); j++){
                Celda c = t.getCelda(j,i);
                EstadoCelda state = c.getEstado();
                boolean mod =  c.isModifiable();
                outState.putInt("CellState:" + j + "," + i, state.ordinal());
                outState.putBoolean("CellMod:"+ j + "," + i,mod);

                if(state == EstadoCelda.Azul){
                    outState.putInt("CellDef:" + j + "," + i, c.getValorDefault());
                    outState.putInt("CellAct:"+ j + "," + i, c.getCurrentVisibles());
                }

            }
        }
        return outState;
    }


}