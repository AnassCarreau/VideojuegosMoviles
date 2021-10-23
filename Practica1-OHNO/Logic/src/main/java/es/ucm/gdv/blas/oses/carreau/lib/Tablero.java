package es.ucm.gdv.blas.oses.carreau.lib;

import java.util.ArrayList;
import java.util.List;

import sun.jvm.hotspot.opto.Block_Array;

public class Tablero {
    private Celda [][] _tablero;
    private List<Pair<Integer, Integer>> _dirs;
    private List<Pair<Integer, Integer>> _azulesFijas;
    private int _posX = 0, _posY = 0;
    public  Tablero(int N){

        //Inicializacion vector direcciones
        _dirs =  new ArrayList<>();
        _dirs.add(new Pair<Integer, Integer>(0,1));
        _dirs.add(new Pair<Integer, Integer>(1,0));
        _dirs.add(new Pair<Integer, Integer>(-1,0));
        _dirs.add(new Pair<Integer, Integer>(0,-1));


        //Inicializacion tablero
        _tablero = new Celda[N][N];

        //Inicializar lista fijas azules
        _azulesFijas = new ArrayList<>();

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                _tablero[i][j] = new Celda();
            }
        }

        //TO DO: HACER BIEN
        _tablero[0][1].setEstado(EstadoCelda.Rojo);
        _tablero[0][1].setModificable(false);
        _tablero[1][0].setEstado(EstadoCelda.Rojo);
        _tablero[1][0].setModificable(false);

        _tablero[1][2].setEstado(EstadoCelda.Azul);
        _tablero[1][2].setModificable(false);
        _tablero[1][2].setValorDefault(2);
        _azulesFijas.add(new Pair<Integer, Integer>(1,2));
        _tablero[2][1].setEstado(EstadoCelda.Azul);
        _tablero[2][1].setModificable(false);
        _tablero[2][1].setValorDefault(1);
        _azulesFijas.add(new Pair<Integer, Integer>(2,1));
        _tablero[3][2].setEstado(EstadoCelda.Azul);
        _tablero[3][2].setModificable(false);
        _tablero[3][2].setValorDefault(2);
        _azulesFijas.add(new Pair<Integer, Integer>(3,2));
        _tablero[3][3].setEstado(EstadoCelda.Azul);
        _tablero[3][3].setModificable(false);
        _tablero[3][3].setValorDefault(4);
        _azulesFijas.add(new Pair<Integer, Integer>(3,3));

    }

    /**
     * Metodo que dibuja el tablero actual en la consola
     */
    public void drawConsole(){
        for(int i = 0; i < _tablero.length; i++){

            for(int j = 0; j < _tablero.length; j++){

                System.out.print("| ");
                if(_tablero[i][j].getEstado().equals(EstadoCelda.Vacia)){
                    System.out.print("  ");
                }
                else if(_tablero[i][j].getEstado().equals(EstadoCelda.Rojo)){
                    System.out.print("R ");
                }
                else{
                    if(_tablero[i][j].isModifiable()) System.out.print("A ");

                    else System.out.print(_tablero[i][j].getValorDefault()+ " ");
                }
            }
            System.out.println("|");
        }
    }

    /**
     * @param x, fila del tablero correspondiente a la casilla que
     *           queramos comprobar si esta dentro de los limites del tablero
     * @param y, columna del tablero correspondiente a la casilla que
     *           queramos comprobar si esta dentro de los limites del tablero
     *
     * Metodo que comprueba si la posicion x,y se encuentra en el tablero
     */
    private boolean posCorrecta(int x, int y){
        return (x >= 0 && x < _tablero.length) && (y >= 0 && y < _tablero.length);
    }

    /**
     * @param x, fila del tablero correspondiente a la casilla que
     *           queramos comprobar sus adyacentes
     * @param y, columna del tablero correspondiente a la casilla que
     *           queramos comprobar sus adyacentes
     *
     * Metodo que comprueba el numero de casillas visibles desde
     * la casilla correspondiente a la posicion x, y
     */
    private int compruebaAdyacentes(int x, int y){

        int visibles = 0;
        for(int i = 0; i < _dirs.size(); i++){
            int auxX = x + _dirs.get(i).getLeft();
            int auxY = y + _dirs.get(i).getRight();

            while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
                visibles++;
                auxX += _dirs.get(i).getLeft();
                auxY += _dirs.get(i).getRight();
            }
        }
        return visibles;
    }

    public void setPos(int x, int y){
        if(posCorrecta(x,y)){
            _posX = x;
            _posY = y;
        }
    }

    /**
     * Metodo que comprueba si la situacion actual del tablero corresponde a una solucion
     */
    public boolean tableroResuelto(){
        int i = 0;
        while( i < _azulesFijas.size()){
            Pair p = _azulesFijas.get(i);
            Celda c = _tablero[(int)(p.getLeft())][(int)(p.getRight())];

            if(c.getValorDefault() != c.getCurrentVisibles()) return false;
            i++;
        }

        return true;
    }
    /**
     * Metodo que cambia el estado de la celda si esta es modificable y actualiza el numero de
     * casillas que ven las celdas azules predefinidas utilizando el metodo de compruebaAdyacentes()
     */
    public boolean cambiaCelda(){
        if (_tablero[_posX][_posY].isModifiable()){
            //EstadoCelda sig = EstadoCelda.values()[(_tablero[_posX][_posY].getEstado().ordinal() + 1) % EstadoCelda.Default.ordinal()];
            EstadoCelda sig = EstadoCelda.Vacia;

            switch (_tablero[_posX][_posY].getEstado()){
                case Azul:{
                    sig = EstadoCelda.Rojo;
                    break; }
                case Rojo:{
                    sig = EstadoCelda.Vacia;
                    break;}
                case Vacia:{
                    sig = EstadoCelda.Azul;
                    break;
                }
                case Default:
                    break;
            }

            _tablero[_posX][_posY].setEstado(sig);

            //TO DO: HACER COMPROBACIONES PARA ACTUALIZAR PISTAS

            for( int i = 0; i < _azulesFijas.size(); i++){
                Pair p = _azulesFijas.get(i);
                int x = (int)(p.getLeft());
                int y = (int)(p.getRight());
                _tablero[x][y].setCurrentVisibles((compruebaAdyacentes(x, y)));
            }

            return true;
        }
        else{
            //TO DO: PONER QUE ERES UN PEAZO GILIPOLLAS :D
            return false;
        }
    }
}
