package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


//Imports del juego
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.androidengine.Android.AndroidGame;
import es.ucm.gdv.blas.oses.carreau.lib.Game.LoadingScreen;



public class MainActivity extends AppCompatActivity {

    //Referencia al juego
    private AndroidGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.game = new AndroidGame(this);

        LoadingScreen loadScreen = new LoadingScreen(game);
        game.setScreen(loadScreen);
        loadScreen.init();


        //button = new Button(this);
        //screen = getStartScreen();
        //button.setText("Touch me!");
        //button.setOnClickListener(this);
        setContentView(game.getView());
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