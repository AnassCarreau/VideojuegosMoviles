package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Font {
    //Setters
    public void setFontName(String fN);
    public void setPos(int posX, int posY);
    public void setTam(int tam);
    //TO DO: Revisar si esta wea tiene que estar aqui
    public void setText(String text);

    //Getters
    public String getFontName();
    public int getPosX();
    public int getPosY();
    public int getTam();
    //TO DO: Revisar si esta wea tiene que estar aqui
    public String getText();
}
