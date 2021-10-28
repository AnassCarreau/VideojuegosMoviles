package es.ucm.gdv.blas.oses.carreau.lib;

import java.util.ArrayList;
import java.util.List;

import sun.jvm.hotspot.opto.Block_Array;

public class Tablero {
    private Celda [][] _tablero;
    private List<Pair<Integer, Integer>> _dirs;
    private List<Pair<Integer, Integer>> _azulesFijas;
    private int _posX = 0, _posY = 0;
    //LISTA DE PISTAS
    private Pistas pistas;

    public  Tablero(int N){

        //Inicializacion vector direcciones
        _dirs =  new ArrayList<>();
        _dirs.add(new Pair<Integer, Integer>(0,1));
        _dirs.add(new Pair<Integer, Integer>(1,0));
        _dirs.add(new Pair<Integer, Integer>(-1,0));
        _dirs.add(new Pair<Integer, Integer>(0,-1));


        //Inicializacion tablero
        //Creamos paredes artificiales para facilitar la comprobacion sobre las pistas
        _tablero = new Celda[N][N];

        //Inicializar lista fijas azules
        _azulesFijas = new ArrayList<>();

        for(int i = 0; i < _tablero.length; i++){
            for(int j = 0; j < _tablero.length; j++){
                _tablero[i][j] = new Celda();
            }
        }


        //TO DO: HACER BIEN
        _tablero[0][1].setEstado(EstadoCelda.Rojo);
        _tablero[0][1].setModificable(false);
        _tablero[1][0].setEstado(EstadoCelda.Rojo);
        _tablero[1][0].setModificable(false);
        /*
        _tablero[1][1].setEstado(EstadoCelda.Rojo);
        _tablero[1][1].setModificable(false);*/

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


        //Inicializacion de las pistas
        pistas = compruebaPistas();
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
    private Pair<Integer, Boolean> compruebaAdyacentes(int x, int y){
        boolean encerrada = true;
        int visibles = 0;
        int auxX = 0;
        int auxY = 0;
        for(int i = 0; i < _dirs.size(); i++){
            auxX = x + _dirs.get(i).getLeft();
            auxY = y + _dirs.get(i).getRight();

            while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
                auxX += _dirs.get(i).getLeft();
                auxY += _dirs.get(i).getRight();
                visibles++;
            }
            if(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Vacia) encerrada =false;
        }

        return new Pair<Integer, Boolean>(visibles, encerrada);
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

    public String damePistaAleatoria(){
        return pistas.getPistaTablero();
    }
    public Pistas compruebaPistas(){
        Pistas nuevasPistas = new Pistas();
        for(int i = 0; i < _tablero.length; i++){
            for(int j = 0; j < _tablero.length; j++){
                Celda actual = _tablero[i][j];
                Pair<Integer, Boolean> ady = compruebaAdyacentes(i,j);

                if(actual.getEstado() == EstadoCelda.Azul && !actual.isModifiable()){

                    actual.setCurrentVisibles(ady.getLeft());
                    //PISTA 1
                    if(actual.getCurrentVisibles() == actual.getValorDefault() && !ady.getRight()){
                        nuevasPistas.addPista(TipoPista.ValueReached, i,j);
                        continue;
                    }

                    else if( !ady.getRight() && actual.getCurrentVisibles() != actual.getValorDefault()){
                        //PISTA 2-----------------------------------------------------------

                        for(int k = 0 ; k < _dirs.size(); k++){
                            Pair<Integer, Integer>dir = _dirs.get(k);
                            int auxX = i + dir.getLeft(), auxY = j+ dir.getRight();
                            if(posCorrecta(auxX,auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Vacia){
                                auxX += dir.getLeft();
                                auxY += dir.getRight();
                                int azules = 0;
                                while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
                                    auxX += dir.getLeft();
                                    auxY += dir.getRight();
                                    azules++;
                                }

                                if(azules + 1 + actual.getCurrentVisibles() > actual.getValorDefault() ){
                                    nuevasPistas.addPista(TipoPista.WouldExceed, i,j);
                                }
                            }
                        }

                        //PISTA 3-----------------------------------------------------------
                        int [] maxCeldasRellenables = new int[_dirs.size()];
                        List<Integer> dirsAux  = new ArrayList<>() ;
                        for(int k = 0; k <_dirs.size(); k++){
                            maxCeldasRellenables[k] =  calculaMaxAzulesColocables(i,j,_dirs.get(k));
                            if(maxCeldasRellenables[k] != 0)dirsAux.add(k);
                        }

                        //Si solo puedo colocar en dos direcciones
                        if(dirsAux.size() == 2) {
                            int k = 0;
                            while(k < dirsAux.size() && maxCeldasRellenables[dirsAux.get(k)] <= actual.getValorDefault() - actual.getCurrentVisibles()){
                                k++;
                            }

                            if(k == dirsAux.size()) nuevasPistas.addPista(TipoPista.OneDirectionRequired, i,j);
                        }
                    }
                    //PISTA 4
                    else if(ady.getRight() && actual.getCurrentVisibles() > actual.getValorDefault()){
                        nuevasPistas.addPista(TipoPista.ErrorClosedTooLate, i,j);
                    }
                    //PISTA 5
                    else if(ady.getRight() && actual.getCurrentVisibles() < actual.getValorDefault()){
                        nuevasPistas.addPista(TipoPista.ErrorClosedTooEarly, i,j);
                    }
                    //PISTA 10
                    else if(!ady.getRight()){
                        int maxColocables = 0;

                        for(int k = 0; k <_dirs.size(); k++){
                           maxColocables +=  calculaMaxAzulesColocables(i,j,_dirs.get(k));
                        }

                        if(maxColocables < actual.getValorDefault() - actual.getCurrentVisibles()){
                            nuevasPistas.addPista(TipoPista.ImposibleVision, i,j);
                        }
                    }


                }
                //PISTA 6.1
                else if(actual.getEstado() == EstadoCelda.Azul && actual.isModifiable() && ady.getRight()){
                    nuevasPistas.addPista(TipoPista.LockedIn, i,j);
                }
                //PISTA 6.2
                else if(actual.getEstado() == EstadoCelda.Vacia && ady.getRight()){
                    nuevasPistas.addPista(TipoPista.MustBeWall, i,j);
                }

            }
        }
        return nuevasPistas;
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


            for( int i = 0; i < _azulesFijas.size(); i++){
                Pair p = _azulesFijas.get(i);
                int x = (int)(p.getLeft());
                int y = (int)(p.getRight());
                _tablero[x][y].setCurrentVisibles((compruebaAdyacentes(x, y).getLeft()));
            }

            //TO DO: HACER COMPROBACIONES PARA ACTUALIZAR PISTAS
            pistas = compruebaPistas();

            return true;
        }
        else{
            //TO DO: PONER QUE ERES UN PEAZO GILIPOLLAS :D
            return false;
        }
    }

    /*
    * Metodo que dada una posicion del tablero de una casilla vacia comprueba si esta
    * debe marcarse como cerrada o no
    *
    * Para ello comprueba en todas las direcciones posibles si se encuentra dicha casilla encerrada
    * sin ninguna conexion con alguna azul
    *
    * //TO DO: igual el bucle while no hace falta si se mantiene actualizado cuantas casillas azules ve
    * cada casilla
    *
    * Este método se corresponde con la pista descrita en el punto nº6 de la practica
    *  */

    private boolean casillaCerrada(int x, int y){
        for(int i = 0 ; i < _dirs.size(); i++){
            int auxX  = x + _dirs.get(i).getLeft();
            int auxY  = y + _dirs.get(i).getRight();

            while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo){
                if(_tablero[auxX][auxY].getEstado() != _tablero[x][y].getEstado()) return false;

                auxX += _dirs.get(i).getLeft();
                auxY += _dirs.get(i).getRight();
            }
        }

        return true;
    }
    /*
    * Metodo que dada la posicion en el tablero de una casilla azul y una direccion
    * comprueba cual es el numero maximo de casillas que es posible de cambiar su valor a azul
    * en dicha direccion sin salirse del tablero
    * */
    private int calculaMaxDir(int x, int y, Pair<Integer, Integer> dir){
        int auxX = x + dir.getLeft(), auxY = y+ dir.getRight(), i = 0;

        while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo){
            auxX += dir.getLeft();
            auxY += dir.getRight();
            i++;
        }

        return i;
    }

    private int calculaMaxAzulesColocables(int x, int y, Pair<Integer, Integer> dir){
        int auxX = x + dir.getLeft(), auxY = y+ dir.getRight(), i = 0;

        while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo){
            auxX += dir.getLeft();
            auxY += dir.getRight();
            if(posCorrecta(auxX, auxY) &&_tablero[auxX][auxY].getEstado() == EstadoCelda.Vacia)i++;
        }

        return i;
    }

}
