package es.ucm.gdv.blas.oses.carreau.lib;

import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tablero {
    private Celda [][] _tablero;
    private List<Pair<Integer, Integer>> _dirs;
    private List<Pair<Integer, Integer>> _celdasFijas;
    private int _posX = 0, _posY = 0;
    //LISTA DE PISTAS
    private Pistas pistas = null;

    public  Tablero(int N, boolean randomBoard){

        //Inicializacion vector direcciones
        _dirs =  new ArrayList<>();
        _dirs.add(new Pair<Integer, Integer>(0,1));
        _dirs.add(new Pair<Integer, Integer>(1,0));
        _dirs.add(new Pair<Integer, Integer>(-1,0));
        _dirs.add(new Pair<Integer, Integer>(0,-1));

        //Inicializacion tablero
        _tablero = new Celda[N][N];

        //Inicializar lista fijas
        _celdasFijas = new ArrayList<>();
/*
        for(int i = 0; i < _tablero.length; i++){
            for(int j = 0; j < _tablero.length; j++){
                _tablero[i][j] = new Celda();
            }
        }*/

        //generamos un tablero aleatorio con una unica solucion
        while(!generaTablerov2(N));


        /*
        //TO DO: HACER BIEN
        if(randomBoard){
            if(N == 6) tableroPrueba6x6();
            else if(N == 4)tableroPrueba4x4();
            else generaTablero(N);

        //Inicializacion de las pistas
            pistas = compruebaPistas();
        }*/
    }

    private boolean generaTablerov2(int N){

        System.out.println("Creando nuevo tablero");

        //Creamos tablero vacio
        _tablero = getTableroVacio(N);

        //Inicializamos lista de celdas fijas
        _celdasFijas = new ArrayList<>();

        //Inicializamos generador de aleatorios
        Random random = new Random();

        //Calculamos con aleatorios cuantas celdas fijas vamos a poner
        //TO DO: gracias a estos numeros funciona en 4x4, pero habría que
        //ajustarlo para el resto de tableros supongo
        int numCeldasAzules = random.nextInt(N) + 1;
        int numCeldasRojas = random.nextInt(N);

        //Colocamos aleatoriamente numCeldas azules
        for(int i = 0 ; i < numCeldasAzules; i++) {

            int x = random.nextInt(N);
            int y = random.nextInt(N);

            //Buscamos casilla que esté vacia (por si da la casualidad de que con los randoms alguna coincide)
            while (_tablero[x][y].getEstado() != EstadoCelda.Vacia) {
                x = random.nextInt(N);
                y = random.nextInt(N);
            }

            Celda c =  _tablero[x][y];

            c.setEstado(EstadoCelda.Azul);
            c.setModificable(false);
            c.setValorDefault(random.nextInt(N)+ 1); //Valor entre las que ve (1 a N)
            _celdasFijas.add(new Pair<Integer, Integer>(x,y));
        }

        //Colocamos aleatoriamente  numCeldasRojas rojas
        for(int i = 0 ; i <numCeldasRojas; i++){
            int x = random.nextInt(N);
            int y = random.nextInt(N);

            //Buscamos casilla que esté vacia (por si da la casualidad de que con los randoms alguna coincide)
            while (_tablero[x][y].getEstado() != EstadoCelda.Vacia) {
                x = random.nextInt(N);
                y = random.nextInt(N);
            }
            Celda c =  _tablero[x][y];

            c.setEstado(EstadoCelda.Rojo);
            c.setModificable(false);
            _celdasFijas.add(new Pair<Integer, Integer>(x,y));
        }

        //Inicializamos las pistas
        pistas = compruebaPistas();

        while(!pistas.isEmpty()){
            //Cogemos la primera de la lista
            StructPista pista = pistas.getFirstPista();

            //Coordenadas correspondientes a la celda
            int auxX = pista.getPosPista().getLeft();
            int auxY = pista.getPosPista().getRight();

            //cogemos que tipo de pista es
            switch(pista.getTipoPista()){
                case WouldExceed:{
                    //mirar en que direccion te pasas y poner una roja
                    Pair<Integer, Integer> dir = pista.getDirPista();
                    int newX = auxX + dir.getLeft(), newY = auxY + dir.getRight();

                    //Nos saltamos los azules adyacentes
                    while(posCorrecta(newX, newY) && _tablero[newX][newY].getEstado()==EstadoCelda.Azul){
                        newX += dir.getLeft();
                        newY += dir.getRight();
                    }

                    if(posCorrecta(newX, newY) && _tablero[newX][newY].getEstado() == EstadoCelda.Vacia){
                        _tablero[newX][newY].setEstado(EstadoCelda.Rojo);
                    }
                    break;
                }
                case ValueReached:{
                    //cerrar celda
                    encierraCelda(auxX,auxY);
                    break;
                }
                case OneDirectionRequired:{
                    //Cogemos la direccion de la pista y asi no tenemos que recorrer todas las dirs
                    Pair<Integer, Integer> dir = pista.getDirPista();

                    int newX = auxX + dir.getLeft();
                    int newY = auxY + dir.getRight();
                    while(posCorrecta(newX, newY) && _tablero[newX][newY].getEstado() == EstadoCelda.Azul){
                        newX += dir.getLeft();
                        newY += dir.getRight();
                    }
                    //Por seguridad, pero si no nos hemos salido es que esa celda vacia es azul
                    //porque es la que debemos poner en esa direccion
                    if(posCorrecta(newX, newY)) {
                        _tablero[newX][newY].setEstado(EstadoCelda.Azul);
                        break;
                    }
                    break;
                }
                case MustBeWall:
                    _tablero[auxX][auxY].setEstado(EstadoCelda.Rojo);
                    break;
                //TODOS LOS CASOS A PARTIR DE AQUI SON CASOS DE ERROR, POR LO QUE SI SALE ALGUNO DE ESTOS DESCARTAMOS TABLEROS
                case ImposibleVision:
                    System.out.println("Imposible to fill the vision of this tile " + auxX + " " + auxY + "\n");
                    return false;
                case ErrorClosedTooLate:
                    System.out.println("This number sees a bit too much "  + auxX + " " + auxY +  "\n");
                    return false;
                case ErrorClosedTooEarly:
                    System.out.println("This number can't see enough "  + auxX + " " + auxY +  "\n");
                    return false;
                case LockedIn:
                    System.out.println("A blue dot should always #see at least one other " + auxX + " " + auxY + "\n");
                    return false;
            }

            //Volvemos a actualizar las pistas
            pistas = compruebaPistas();
        }

        //Si hay alguna celda vacia el tablero no nos vale
        for (int i = 0; i < N; i++){
            for(int j = 0 ; j < N; j++){
                if(_tablero[i][j].getEstado() == EstadoCelda.Vacia) return false;
            }
        }


        //SI HEMOS LLEGADO HASTA AQUI HABEMUS TABLERO
        Celda [][] _tableroAux = getTableroVacio(N);

        for(int i = 0; i < _celdasFijas.size();i++){
            Pair<Integer, Integer> index = _celdasFijas.get(i);
            Celda c = _tablero[index.getLeft()][index.getRight()];

            _tableroAux[index.getLeft()][index.getRight()] = c;
        }

        //Tablero vacio con solo las default
        _tablero = _tableroAux;

        //Ponemos las pistas del estado inicial
        pistas = compruebaPistas();

        //Devolvemos true si conseguimos crear el tablero
        return true;
    }

    private void encierraCelda(int x, int y){
        for(int i = 0; i < _dirs.size(); i++){
            Pair<Integer, Integer> dir = _dirs.get(i);
            int auxX = x + dir.getLeft();
            int auxY = y + dir.getRight();

            while(posCorrecta(auxX,auxY) && _tablero[auxX][auxY].getEstado() == EstadoCelda.Azul ){
                auxX += dir.getLeft();
                auxY += dir.getRight();
            }

            if(posCorrecta(auxX, auxY))_tablero[auxX][auxY].setEstado(EstadoCelda.Rojo);
        }
    }

    private Celda[][] getTableroVacio(int N){
        Celda[][] tablero = new Celda[N][N];
        for(int i = 0; i <N;i++){
            for(int j = 0; j < N; j++){
                tablero[i][j] = new Celda();
            }
        }
        return tablero;
    }

    private void tableroPrueba6x6(){
        _tablero[1][2].setEstado(EstadoCelda.Rojo);
        _tablero[1][2].setModificable(false);
        _celdasFijas.add(new Pair<Integer, Integer>(1,2));


        _tablero[2][4].setEstado(EstadoCelda.Rojo);
        _tablero[2][4].setModificable(false);
        _celdasFijas.add(new Pair<Integer, Integer>(2,4));

        _tablero[2][5].setEstado(EstadoCelda.Rojo);
        _tablero[2][5].setModificable(false);
        _celdasFijas.add(new Pair<Integer, Integer>(2,5));

        _tablero[5][1].setEstado(EstadoCelda.Rojo);
        _tablero[5][1].setModificable(false);
        _celdasFijas.add(new Pair<Integer, Integer>(5,1));


        _tablero[0][0].setEstado(EstadoCelda.Azul);
        _tablero[0][0].setModificable(false);
        _tablero[0][0].setValorDefault(5);
        _celdasFijas.add(new Pair<Integer, Integer>(0,0));


        _tablero[0][5].setEstado(EstadoCelda.Azul);
        _tablero[0][5].setModificable(false);
        _tablero[0][5].setValorDefault(1);
        _celdasFijas.add(new Pair<Integer, Integer>(0,5));


        _tablero[1][0].setEstado(EstadoCelda.Azul);
        _tablero[1][0].setModificable(false);
        _tablero[1][0].setValorDefault(3);
        _celdasFijas.add(new Pair<Integer, Integer>(1,0));


        _tablero[1][3].setEstado(EstadoCelda.Azul);
        _tablero[1][3].setModificable(false);
        _tablero[1][3].setValorDefault(4);
        _celdasFijas.add(new Pair<Integer, Integer>(1,3));


        _tablero[2][3].setEstado(EstadoCelda.Azul);
        _tablero[2][3].setModificable(false);
        _tablero[2][3].setValorDefault(5);
        _celdasFijas.add(new Pair<Integer, Integer>(2,3));


        _tablero[3][4].setEstado(EstadoCelda.Azul);
        _tablero[3][4].setModificable(false);
        _tablero[3][4].setValorDefault(3);
        _celdasFijas.add(new Pair<Integer, Integer>(3,4));


        _tablero[3][2].setEstado(EstadoCelda.Azul);
        _tablero[3][2].setModificable(false);
        _tablero[3][2].setValorDefault(1);
        _celdasFijas.add(new Pair<Integer, Integer>(3,2));


        _tablero[4][0].setEstado(EstadoCelda.Azul);
        _tablero[4][0].setModificable(false);
        _tablero[4][0].setValorDefault(2);
        _celdasFijas.add(new Pair<Integer, Integer>(4,0));


        _tablero[4][3].setEstado(EstadoCelda.Azul);
        _tablero[4][3].setModificable(false);
        _tablero[4][3].setValorDefault(3);
        _celdasFijas.add(new Pair<Integer, Integer>(4,3));


        _tablero[5][4].setEstado(EstadoCelda.Azul);
        _tablero[5][4].setModificable(false);
        _tablero[5][4].setValorDefault(5);
        _celdasFijas.add(new Pair<Integer, Integer>(5,4));

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

    /**
     * Metodo que comprueba si la situacion actual del tablero corresponde a una solucion
     */
    public boolean tableroResuelto(){
        if(pistas != null) return pistas.isEmpty();
        else return false;
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
                        StructPista pista = new StructPista(TipoPista.ValueReached, new Pair(i, j), new Pair(0,0));
                        nuevasPistas.addPista(pista);
                    }

                    else if( !ady.getRight() && actual.getCurrentVisibles() < actual.getValorDefault()){
                        //PISTA 10-----------------------------------------------------------
                        int maxColocables = 0;

                        for(int k = 0; k <_dirs.size(); k++){
                            maxColocables +=  calculaMaxColocablesYVisibles(i,j,_dirs.get(k)).getLeft();
                        }

                        if(maxColocables < actual.getValorDefault() - actual.getCurrentVisibles()){
                            StructPista pista = new StructPista(TipoPista.ImposibleVision, new Pair(i, j), new Pair(0,0));
                            nuevasPistas.addPista(pista);
                            continue;
                        }
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
                                StructPista pista = new StructPista(TipoPista.OneDirectionRequired, new Pair(i, j), _dirs.get(k));
                                nuevasPistas.addPista(pista);
                                break;
                            }
                        }
                    }
                    //PISTA 4
                    else if(/*ady.getRight() &&*/ actual.getCurrentVisibles() > actual.getValorDefault()){
                        StructPista pista = new StructPista(TipoPista.ErrorClosedTooLate, new Pair(i, j), new Pair(0, 0));
                        nuevasPistas.addPista(pista);
                    }
                    //PISTA 5
                    else if(/*ady.getRight() &&*/ actual.getCurrentVisibles() < actual.getValorDefault()){
                        StructPista pista = new StructPista(TipoPista.ErrorClosedTooEarly, new Pair(i, j), new Pair(0, 0));
                        nuevasPistas.addPista(pista);
                    }

                }
                //PISTA 6.1
                else if(actual.getEstado() == EstadoCelda.Azul && actual.isModifiable() && ady.getRight() && ady.getLeft() == 0){
                    StructPista pista = new StructPista(TipoPista.LockedIn, new Pair(i, j), new Pair(0, 0));
                    nuevasPistas.addPista(pista);
                }
                //PISTA 6.2
                else if(actual.getEstado() == EstadoCelda.Vacia && compruebaVaciaEncerrada(i,j)){
                    StructPista pista = new StructPista(TipoPista.MustBeWall, new Pair(i, j), new Pair(0, 0));
                    nuevasPistas.addPista(pista);
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

            //Nos saltamos los azules adyacentes
            while(posCorrecta(auxX, auxY) && _tablero[auxX][auxY].getEstado()==EstadoCelda.Azul){
                auxX += dir.getLeft();
                auxY += dir.getRight();
            }

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
                    StructPista pista = new StructPista(TipoPista.WouldExceed, new Pair(i, j), dir);
                    nuevasPistas.addPista(pista);
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


            for(int i = 0; i < _celdasFijas.size(); i++){
                Pair p = _celdasFijas.get(i);
                int x = (int)(p.getLeft());
                int y = (int)(p.getRight());
                _tablero[x][y].setCurrentVisibles((compruebaAdyacentes(x, y).getLeft()));
            }

            //TO DO: HACER COMPROBACIONES PARA ACTUALIZAR PISTAS
            pistas = compruebaPistas();

            return true;
        }
        else return false;

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

    public void addToListaNoModificables(int x, int y){
        _celdasFijas.add(new Pair<Integer, Integer>(x,y));
    }


}
