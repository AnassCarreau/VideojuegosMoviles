package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;


//Imports del juego
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.androidengine.Android.AndroidGame;
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
                    int dimensions = savedInstanceState.getInt("dimensions");
                    newScreen = new GameScreen(this.game, dimensions);
                    break;
                }

            }

        }

        LoadingScreen loadScreen = new LoadingScreen(game);
        game.setScreen(loadScreen);
        loadScreen.init();
        if(newScreen != null){
            game.setScreen(newScreen);
        }
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
            GameScreen  gameScreen = (GameScreen) game.getCurrentScreen();
            Tablero t = gameScreen.getTablero();
            //TO DO: SERIALIZAR TABLERO Y STACK DE DESHACER
            outState.putInt("dimensiones",t.getDimensions());
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


}