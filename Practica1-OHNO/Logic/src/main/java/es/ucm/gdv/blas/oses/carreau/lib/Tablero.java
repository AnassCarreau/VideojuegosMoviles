package es.ucm.gdv.blas.oses.carreau.lib;

import com.sun.org.apache.bcel.internal.generic.LUSHR;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

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

        //generamos un tablero aleatorio con una unica solucion


        //TO DO: HACER BIEN
        if(N == 6) tableroPrueba6x6();
        else if(N == 4)tableroPrueba4x4();
        else generaTablero(N);

        //Inicializacion de las pistas
        pistas = compruebaPistas();
    }

    private void generaTablero(int N){
        Celda [][] _tableroAux = new Celda[N][N];

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                _tableroAux[i][j] = new Celda();
            }
        }

        Random random = new Random();

        boolean [][] tableroBool = new boolean[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                tableroBool[i][j] = false;
            }
        }
        int numCeldasRellenas = 0;
        while(numCeldasRellenas < N * N) {
            int x = random.nextInt(N);
            int y = random.nextInt(N);
            while (tableroBool[x][y]) {
                x = random.nextInt(N);
                y = random.nextInt(N);
            }
            Celda celda = getCelda(x, y);

            int numberDefault = random.nextInt(N) + 1;

            celda.setEstado(EstadoCelda.Azul);
            celda.setModificable(false);
            celda.setValorDefault(numberDefault);
            tableroBool[x][y] = true;
            numCeldasRellenas++;

            int dirRand = random.nextInt(4);
            int i = 0;
            while (i < 4 && celda.getCurrentVisibles() != celda.getValorDefault()) {
                int auxX = x + _dirs.get((dirRand + i) % 4).getLeft();
                int auxY = y + _dirs.get((dirRand + i) % 4).getRight();
                while (posCorrecta(auxX, auxY) && (_tableroAux[auxX][auxY].getEstado() == EstadoCelda.Vacia
                        || _tableroAux[auxX][auxY].getEstado() == EstadoCelda.Azul)) {
                    auxX += _dirs.get((dirRand + i) % 4).getLeft();
                    auxY += _dirs.get((dirRand + i) % 4).getRight();

                    if (posCorrecta(auxX, auxY) && _tableroAux[auxX][auxY].getEstado() == EstadoCelda.Vacia) {
                        _tableroAux[auxX][auxY].setEstado(EstadoCelda.Azul);
                        tableroBool[auxX][auxY] = true;
                        numCeldasRellenas++;
                    }

                    if (celda.getCurrentVisibles() + 1 <= celda.getValorDefault()) {
                        celda.setCurrentVisibles(celda.getCurrentVisibles() + 1);
                        continue;
                    } else {
                        break;
                    }
                }
                i++;
            }
            for (int k = 0; k < _dirs.size(); k++){
                int auxX = x + _dirs.get(k).getLeft();
                int auxY = y + _dirs.get(k).getRight();
                while (posCorrecta(auxX, auxY) && _tableroAux[auxX][auxY].getEstado() == EstadoCelda.Azul) {
                    auxX += _dirs.get(k).getLeft();
                    auxY += _dirs.get(k).getRight();
                }
                if(posCorrecta(auxX, auxY) && _tableroAux[auxX][auxY].getEstado() == EstadoCelda.Vacia) {
                    _tableroAux[auxX][auxY].setEstado(EstadoCelda.Rojo);
                    tableroBool[auxX][auxY] = true;
                    numCeldasRellenas++;
                }
            }
            //System.out.println(numCeldasRellenas);
        }

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                _tablero[i][j] = _tableroAux[i][j];
            }
        }
    }

    private void tableroPrueba6x6(){
        _tablero[1][2].setEstado(EstadoCelda.Rojo);
        _tablero[1][2].setModificable(false);
        _tablero[2][4].setEstado(EstadoCelda.Rojo);
        _tablero[2][4].setModificable(false);
        _tablero[2][5].setEstado(EstadoCelda.Rojo);
        _tablero[2][5].setModificable(false);
        _tablero[5][1].setEstado(EstadoCelda.Rojo);
        _tablero[5][1].setModificable(false);

        _tablero[0][0].setEstado(EstadoCelda.Azul);
        _tablero[0][0].setModificable(false);
        _tablero[0][0].setValorDefault(5);

        _tablero[0][5].setEstado(EstadoCelda.Azul);
        _tablero[0][5].setModificable(false);
        _tablero[0][5].setValorDefault(1);

        _tablero[1][0].setEstado(EstadoCelda.Azul);
        _tablero[1][0].setModificable(false);
        _tablero[1][0].setValorDefault(3);

        _tablero[1][3].setEstado(EstadoCelda.Azul);
        _tablero[1][3].setModificable(false);
        _tablero[1][3].setValorDefault(4);

        _tablero[2][3].setEstado(EstadoCelda.Azul);
        _tablero[2][3].setModificable(false);
        _tablero[2][3].setValorDefault(5);

        _tablero[3][4].setEstado(EstadoCelda.Azul);
        _tablero[3][4].setModificable(false);
        _tablero[3][4].setValorDefault(3);

        _tablero[3][2].setEstado(EstadoCelda.Azul);
        _tablero[3][2].setModificable(false);
        _tablero[3][2].setValorDefault(1);

        _tablero[4][0].setEstado(EstadoCelda.Azul);
        _tablero[4][0].setModificable(false);
        _tablero[4][0].setValorDefault(2);

        _tablero[4][3].setEstado(EstadoCelda.Azul);
        _tablero[4][3].setModificable(false);
        _tablero[4][3].setValorDefault(3);

        _tablero[5][4].setEstado(EstadoCelda.Azul);
        _tablero[5][4].setModificable(false);
        _tablero[5][4].setValorDefault(5);
    }
    private void tableroPrueba4x4(){


        _tablero[0][1].setEstado(EstadoCelda.Rojo);
        _tablero[0][1].setModificable(false);
        _tablero[1][0].setEstado(EstadoCelda.Rojo);
        _tablero[1][0].setModificable(false);

        //_tablero[1][1].setEstado(EstadoCelda.Rojo);
        //_tablero[1][1].setModificable(false);

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
     * Metodo que comprueba si la posicion x,y es una posicion valida del tablero
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
            if(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo) encerrada =false;
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

        //if(!pistas.isEmpty())
        /*int i = 0;
        while( i < _azulesFijas.size()){
            Pair p = _azulesFijas.get(i);
            Celda c = _tablero[(int)(p.getLeft())][(int)(p.getRight())];

            if(c.getValorDefault() != c.getCurrentVisibles()) return false;
            i++;
        }*/

        return pistas.isEmpty();
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
                    }

                    else if( !ady.getRight() && actual.getCurrentVisibles() != actual.getValorDefault()){
                        //PISTA 2-----------------------------------------------------------
                        if(checkHint2(i,j,nuevasPistas))continue; //TO DO:REVISAR ¿PUEDE DARSE 2 PISTAS POR CELDA?

                        //PISTA 3-----------------------------------------------------------
                        List<Pair<Integer,Integer>> celdaInfo = new ArrayList<>();

                        for(int k = 0; k <_dirs.size(); k++){
                            celdaInfo.add(calculaMaxColocablesYVisibles(i,j,_dirs.get(k)));
                        }


                        for(int k = 0; k < _dirs.size(); k++) {
                            int adyInmediatas = calculaAdyInmediatas(i, j, _dirs.get(k));
                            int maxVisiblesOtrasDir = 0;

                            for (int g = 0; g < _dirs.size(); g++) {
                                if (_dirs.get(g) != _dirs.get(k))
                                    maxVisiblesOtrasDir += celdaInfo.get(g).getRight();
                            }

                            if (adyInmediatas + maxVisiblesOtrasDir < actual.getValorDefault()){
                                nuevasPistas.addPista(TipoPista.OneDirectionRequired, i, j);
                                break;
                            }
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
                           maxColocables +=  calculaMaxColocablesYVisibles(i,j,_dirs.get(k)).getLeft();
                        }

                        if(maxColocables < actual.getValorDefault() - actual.getCurrentVisibles()){
                            nuevasPistas.addPista(TipoPista.ImposibleVision, i,j);
                        }
                    }


                }
                //PISTA 6.1
                else if(actual.getEstado() == EstadoCelda.Azul && actual.isModifiable() && ady.getRight() && ady.getLeft() == 0){
                    nuevasPistas.addPista(TipoPista.LockedIn, i,j);
                }
                //PISTA 6.2
                else if(actual.getEstado() == EstadoCelda.Vacia && compruebaVaciaEncerrada(i,j)){
                    nuevasPistas.addPista(TipoPista.MustBeWall, i,j);
                }
            }
        }
        return nuevasPistas;
    }

    //i es el numero de fila es decir la posicion en Y
    //j es eñ numero de la columna es decir la posicion en X
    private boolean checkHint2(int i, int j, Pistas nuevasPistas){
        for(int k = 0 ; k < _dirs.size(); k++){
            Pair<Integer, Integer>dir = _dirs.get(k);
            int auxX = i + dir.getLeft(), auxY = j + dir.getRight();
            if(posCorrecta(auxX,auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Vacia){
                auxX += dir.getLeft();
                auxY += dir.getRight();
                int azules = 0;
                while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
                    auxX += dir.getLeft();
                    auxY += dir.getRight();
                    azules++;
                }

                if(azules + 1 + _tablero[i][j].getCurrentVisibles() > _tablero[i][j].getValorDefault() ){
                    nuevasPistas.addPista(TipoPista.WouldExceed, i,j);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Metodo que cambia el estado de la celda si esta es modificable y actualiza el numero de
     * casillas que ven las celdas azules predefinidas utilizando el metodo de compruebaAdyacentes()
     */
    public boolean cambiaCelda(int _posX, int _posY){
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
    private boolean compruebaVaciaEncerrada(int x, int y){

        for(int i = 0; i < _dirs.size(); i++){
            int auxX = x + _dirs.get(i).getLeft();
            int auxY = y + _dirs.get(i).getRight();

            if(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo) return false;
        }

        return true;
    }

    private Pair<Integer,Integer> calculaMaxColocablesYVisibles(int x, int y, Pair<Integer, Integer> dir){
        int auxX = x + dir.getLeft(), auxY = y+ dir.getRight(), colocables = 0,adyInDir = 0;
        int visibles = _tablero[x][y].getCurrentVisibles();

        //Nos saltamos las adyacentes azules directas
        while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
            auxX += dir.getLeft();
            auxY += dir.getRight();
            adyInDir++;
        }
        //caluculamos cuantas podemos colocar
        int diferenciaVision = _tablero[x][y].getValorDefault() - visibles;
        int azulesEncontradas = 0;

        while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() != EstadoCelda.Rojo
                && diferenciaVision - azulesEncontradas >=  colocables + 1){
            if(_tablero[auxX][auxY].getEstado() == EstadoCelda.Vacia)colocables++;
            else azulesEncontradas++;
            auxX += dir.getLeft();
            auxY += dir.getRight();

        }

        if(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul)colocables--;

        int visiblesEnDir = colocables + adyInDir + azulesEncontradas;
        return new Pair<Integer,Integer>(colocables, visiblesEnDir);
    }

    private int calculaAdyInmediatas(int x, int y, Pair<Integer, Integer> dir){
        int auxX = x + dir.getLeft(), auxY = y+ dir.getRight(),adyInDir = 0;

        //Nos saltamos las adyacentes azules directas
        while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul){
            auxX += dir.getLeft();
            auxY += dir.getRight();
            adyInDir++;
        }

        return adyInDir;
    }

    public Celda getCelda(int x, int y){
        return _tablero[x][y];
    }

    public int getDimensions(){
        return _tablero.length;
    }

}
