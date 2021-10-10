package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces;

import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;

public interface Engine {
    public Input getInput();

    public FileIO getFileIO();
    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

   /*Pseudocodigo createWindowAndUIComponent(); Input input = new Input();
    Graphics graphics = new Graphics(); Audio audio = new Audio();
    Screen currentScreen = new MainMenu(); float lastFrameTime = currentTime();
while( !userQuit() ) {
        float deltaTime = currentTime() – lastFrameTime;
        lastFrameTime = currentTime();
        currentScreen.updateState(input, deltaTime);
        currentScreen.present(graphics, audio, deltaTime);
    }
    cleanupResources();

    */

}