package es.ucm.gdv.blas.oses.carreau.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Tablero {
    private final List<Vector> _dirs;
    private Celda[][] _tablero;
    private List<Vector> _celdasFijas;
    //LISTA DE PISTAS
    private Pistas pistas = null;
    private int numCeldasNoVacias = 0;

    /**
     * Constructora de la clase Tablero, construye un tablero
     * de NxN casillas
     * @param N, numero de filas/columnas que va a tener el tablero
     * @param randomBoard, boolean, si queremos generar un tablero aleatorio
     *                     o no
     */
    public Tablero(int N, boolean randomBoard) {
        //Inicializacion vector direcciones
        _dirs = new ArrayList<>();

        _dirs.add(new Vector(0, 1)); //ABAJO
        _dirs.add(new Vector(1, 0)); //DCHA
        _dirs.add(new Vector(-1, 0));//IZQDA
        _dirs.add(new Vector(0, -1));//ARRIBA

        //Inicializar lista fijas
        _celdasFijas = new ArrayList<>();
        pistas = new Pistas();

        //Inicializacion tablero
        _tablero = getTableroVacio(N);

        //generamos un tablero aleatorio con una unica solucion
        if (randomBoard) {
            while (!generaTablero(N)) {
            }
        }
    }

    private Vector addNuevaCeldaFija(int N, Random random) {
        //Tratamos de colocar una nueva celda fija
        int x = random.nextInt(N);
        int y = random.nextInt(N);

        //Buscamos casilla que esté vacia (por si da la casualidad de que con los randoms alguna coincide)
        while (_tablero[y][x].getEstado() != EstadoCelda.Vacia) {
            x = random.nextInt(N);
            y = random.nextInt(N);
        }

        Celda c = _tablero[y][x];

        int redIfOne = random.nextInt(N) + 1;

        if (redIfOne == 1) {
            c.setEstado(EstadoCelda.Rojo);
        } else {
            c.setEstado(EstadoCelda.Azul);
            c.setValorDefault(random.nextInt(N) + 1); //Valor entre las que ve (1 a N)
        }

        c.setModificable(false);
        return new Vector(x, y);
    }

    /**
     * Metodo auxiliar para la construccion del tablero de forma aleatoria. Nos ayuda a
     * aplicar la pista WOULDEXCEED en la casilla que marque dicha pista
     * @param pista, pista recibida, debe de ser del tipo WOULDEXCEED
     * @return, Vector, casilla la cual hemos cambiado su estado
     */
    private Vector recibidaWouldExceed(StructPista pista) {
        //mirar en que direccion te pasas y poner una roja
        Vector dir = pista.getDirPista();
        int newX = pista.getPosPista().x + dir.x, newY = pista.getPosPista().y + dir.y;

        //Nos saltamos los azules adyacentes
        while (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Azul) {
            newX += dir.x;
            newY += dir.y;
        }

        if (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Vacia) {
            _tablero[newY][newX].setEstado(EstadoCelda.Rojo);
            return new Vector(newX, newY);
        }

        return null;
    }

    /**
     * Metodo auxiliar para la construccion del tablero de forma aleatoria. Nos ayuda a
     * aplicar la pista ONEDIRECTIONREQUIRED en la casilla que marque dicha pista
     * @param pista, pista recibida, debe de ser del tipo ONEDIRECTIONREQUIRED
     * @return, Vector, casilla la cual hemos cambiado su estado
     */
    private Vector recibidaOneDirectionRequired(StructPista pista) {
        Vector dir = pista.getDirPista();

        int newX = pista.getPosPista().x + dir.x;
        int newY = pista.getPosPista().y + dir.y;

        //Nos saltamos los azules adyacentes
        while (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Azul) {
            newX += dir.x;
            newY += dir.y;
        }

        //Por seguridad, pero si no nos hemos salido es que esa celda vacia es azul
        //porque es la que debemos poner en esa direccion
        if (posCorrecta(newX, newY)) {
            _tablero[newY][newX].setEstado(EstadoCelda.Azul);
            return new Vector(newX, newY);
        }

        return null;
    }


    /**
     * Metodo que genera un tablero aleatorio con una unica solucion de NxN casillas
     * funciona colocando una celda fija (Azul o Roja) y, usando las pistas que estas casillas
     * fijas nos den, ir rellenando el tablero hasta tener uno totalmente lleno y que cumpla
     * los requisitos del juego. Si en un numero concreto de intentos (entendemos por intentos
     * el añadir una nueva casilla fija) no hemos sido capaces de generar un tablero lo descartamos
     * @param N, numero de filas/columnas del tablero que vamos a crear
     * @return boolean, true si hemos sido capaces de generarlo, false en caso contrario
     */
    private boolean generaTablero(int N) {
        int intentos = 0;

        //Creamos tablero vacio
        _tablero = getTableroVacio(N);

        //Inicializamos lista de celdas fijas
        _celdasFijas = new ArrayList<>();

        //Stack auxiliar de casillas que hemos modificado (es decir, no son vacias)
        Stack<Vector> _celdasNoVacias = new Stack<>();

        //Inicializamos generador de aleatorios
        Random random = new Random();

        int numCasillasMod = 0;

        while (numCasillasMod < N * N && intentos < N * N) {

            Vector v = addNuevaCeldaFija(N, random);
            _celdasNoVacias.add(v);
            _celdasFijas.add(v);
            int lastFixed = numCasillasMod;
            numCasillasMod++;

            //Inicializamos las pistas
            compruebaPistasTablero();

            boolean error = false;
            while (!pistas.isEmpty() && !error) {
                Pistas auxPista = new Pistas();
                //Cogemos la primera de la lista
                StructPista pista = pistas.getFirstPista();

                //Coordenadas correspondientes a la celda
                int auxX = pista.getPosPista().x;
                int auxY = pista.getPosPista().y;

                //cogemos que tipo de pista es
                switch (pista.getTipoPista()) {
                    case WouldExceed: {
                        Vector vector = recibidaWouldExceed(pista);
                        if (vector != null) {
                            _celdasNoVacias.add(vector);
                            auxPista = compruebaPistas(vector.x, vector.y);
                            numCasillasMod++;
                        }
                        break;
                    }
                    case ValueReached: {
                        //cerrar celda
                        List<Vector> lis = encierraCelda(auxX, auxY);

                        for (int i = 0; i < lis.size(); i++) {
                            numCasillasMod++;
                            _celdasNoVacias.add(lis.get(i));
                            int x = lis.get(i).x;
                            int y = lis.get(i).y;
                            Pistas p = compruebaPistas(x, y);
                            while (!p.getListaPistas().isEmpty()) {
                                auxPista.getListaPistas().remove(p.getFirstPista());
                                auxPista.getListaPistas().add(p.getFirstPista());
                                p.getListaPistas().remove(0);
                            }
                        }
                        break;
                    }
                    case OneDirectionRequired: {
                        Vector vector = recibidaOneDirectionRequired(pista);
                        if (vector != null) {
                            _celdasNoVacias.add(vector);
                            auxPista = compruebaPistas(vector.x, vector.y);
                            numCasillasMod++;
                        }
                        break;
                    }
                    case MustBeWall:
                        _tablero[auxY][auxX].setEstado(EstadoCelda.Rojo);
                        numCasillasMod++;
                        _celdasNoVacias.add(new Vector(auxX, auxY));
                        auxPista = compruebaPistas(auxX, auxY);
                        break;
                    //TODOS LOS CASOS A PARTIR DE AQUI SON CASOS DE ERROR, POR LO QUE SI SALE ALGUNO DE ESTOS RETROCEDEMOS
                    case ImposibleVision:
                    case ErrorClosedTooLate:
                    case ErrorClosedTooEarly:
                    case LockedIn:
                        while (_celdasNoVacias.size() > lastFixed) {
                            Vector cell = _celdasNoVacias.pop();
                            _tablero[cell.y][cell.x].resetCelda();
                        }
                        //Quitamos la ultima celda fija que hemos puesto
                        _celdasFijas.remove(_celdasFijas.size() - 1);

                        numCasillasMod = _celdasNoVacias.size();

                        //Comprobamos que esten bien los valores de adyacentes del tablero
                        actualizaVisiblesTablero();

                        error = true;
                        break;
                }

                if (error) continue;

                //Sacamos la pista actual
                pistas.getListaPistas().remove(pista);

                while (!auxPista.isEmpty()) {
                    StructPista pi = auxPista.getFirstPista();
                    pistas.addPista(pi);
                    auxPista.getListaPistas().remove(0);
                }
            }
            intentos++;
        }

        //Si nos pasamos de los intentos returneamos false
        if (intentos >= N * N) {
            return false;
        }

        //SI HEMOS LLEGADO HASTA AQUI HABEMUS TABLERO
        Celda[][] _tableroAux = getTableroVacio(N);

        for (int i = 0; i < _celdasFijas.size(); i++) {
            Vector index = _celdasFijas.get(i);
            Celda c = _tablero[index.y][index.x];
            _tableroAux[index.y][index.x] = c;
        }

        //Tablero vacio con solo las default
        _tablero = _tableroAux;

        //Ponemos las pistas del estado inicial
        compruebaPistasTablero();

        numCeldasNoVacias = _celdasFijas.size();
        return true;
    }

    /**
     * Metodo auxiliar empleado a la hora de crear un tablero aleatorio cuando recibimos la
     * pista VALUEREACHED -> lo que implica que tenemos que cerrar dicha celda
     * @param x,int, coordenada x de la casilla que tenemos que cerrar
     * @param y, int, coordenada y de la casilla que tenemos que cerrar
     * @return, lista de Vectores con las coordenadas de las casillas que hemos puesto como rojas
     */
    private List<Vector> encierraCelda(int x, int y) {
        List<Vector> l = new ArrayList<>();
        for (int i = 0; i < _dirs.size(); i++) {
            Vector dir = _dirs.get(i);
            int auxX = x + dir.x;
            int auxY = y + dir.y;

            while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                auxX += dir.x;
                auxY += dir.y;
            }

            if (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Vacia) {
                l.add(new Vector(auxX, auxY));
                _tablero[auxY][auxX].setEstado(EstadoCelda.Rojo);
            }
        }
        return l;
    }

    /**
     * Metodo auxiliar que crea un tablero vacio de NXN dimensiones
     * @param N, int, numero de filas/columnas que va a tener nuestro tablero
     * @return Celda[][] con un tablero de celdas por defecto
     */
    private Celda[][] getTableroVacio(int N) {
        Celda[][] tablero = new Celda[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tablero[i][j] = new Celda();
            }
        }
        return tablero;
    }

    /**
     * @param x, fila del tablero correspondiente a la casilla que
     *           queramos comprobar si esta dentro de los limites del tablero
     * @param y, columna del tablero correspondiente a la casilla que
     *           queramos comprobar si esta dentro de los limites del tablero
     *           <p>
     *           Metodo que comprueba si la posicion x,y es una posicion valida del tablero
     */
    private boolean posCorrecta(int x, int y) {

        return (x >= 0 && x < _tablero.length) && (y >= 0 && y < _tablero.length);
    }

    /**
     * @param x, fila del tablero correspondiente a la casilla que
     *           queramos comprobar sus adyacentes
     * @param y, columna del tablero correspondiente a la casilla que
     *           queramos comprobar sus adyacentes
     *           <p>
     *           Metodo que comprueba el numero de casillas visibles desde
     *           la casilla correspondiente a la posicion x, y
     */
    private Pair<Integer, Boolean> compruebaAdyacentes(int x, int y) {
        boolean encerrada = true;
        int visibles = 0;
        int auxX = 0;
        int auxY = 0;
        for (int i = 0; i < _dirs.size(); i++) {
            auxX = x + _dirs.get(i).x;
            auxY = y + _dirs.get(i).y;

            while (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                auxX += _dirs.get(i).x;
                auxY += _dirs.get(i).y;
                visibles++;
            }
            if (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo)
                encerrada = false;
        }

        return new Pair<Integer, Boolean>(visibles, encerrada);
    }

    /**
     * Metodo que comprueba si la situacion actual del tablero corresponde a una solucion
     */
    public boolean tableroResuelto() {
        if (pistas != null) return pistas.isEmpty();
        else return false;
    }

    /**
     * Metodo que quita la pista de la lista y la actualiza en la pista de la celda
     * @param pista, pista nueva
     * @param pistaAct, pista a quitar de la lista
     */
    public void quitaPista(StructPista pista, StructPista pistaAct) {
        if (pistaAct != null) {
            _tablero[pistaAct.getPosPista().y][pistaAct.getPosPista().x].setCurrentPista(pista);
            pistas.getListaPistas().remove(pistaAct);
        }
    }

    /**
     * Metodo que, dadas las coordenadas de una celda, calcula si dicha celda en el estado actual del tablero
     * proporciona una pista.
     * @param x, int, coordenada x de la casilla de la que queremos obtener la pista
     * @param y, int, coordenada y de la casilla de la que queremos obtener la pista
     * @return StructPista, pista actual que genera dicha casilla teniendo en cuenta el estado del tablero actual
     */
    public StructPista getPistaInCoordenada(int x, int y) {
        Celda actual = _tablero[y][x];
        Pair<Integer, Boolean> ady = compruebaAdyacentes(x, y);

        //Si la celda actual es azul, actualizamos su valor de visibles
        if(actual.getEstado() == EstadoCelda.Azul) actual.setCurrentVisibles(ady.getLeft());

        StructPista pista = null;
        StructPista pistaAct = actual.getPista();
        if (actual.getEstado() == EstadoCelda.Azul && !actual.isModifiable()) {

            //PISTA 1 -> Veo todas las que tengo que ver pero no estoy cerrada
            if (actual.getCurrentVisibles() == actual.getValorDefault() && !ady.getRight()) {
                pista = new StructPista(TipoPista.ValueReached, new Vector(x, y), new Vector(0, 0));
            }
            else if (!ady.getRight() && actual.getCurrentVisibles() < actual.getValorDefault()) {

                //PISTA 10
                int maxColocables = 0;

                //Calculamos el número maximo de azules que seria posible colocar en todas las
                //Direcciones de la celda
                for (int k = 0; k < _dirs.size(); k++) {
                    maxColocables += calculaMaxPosiblesVisibles(x, y, _dirs.get(k));
                }

                //Si ese numero es menor que el valor que debe alcanzar la celda, la susodicha
                //no se podrá rellenar por completo
                if (maxColocables < actual.getValorDefault()) {
                    pista = new StructPista(TipoPista.ImposibleVision, new Vector(x, y), new Vector(0, 0));
                    quitaPista(pista, pistaAct);
                    return pista;
                }

                //PISTA 2-----------------------------------------------------------
                for (int k = 0; k < _dirs.size(); k++) {
                    Vector dir = _dirs.get(k);
                    int auxX = x + dir.x, auxY = y + dir.y;

                    //Nos saltamos los azules adyacentes
                    while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                        auxX += dir.x;
                        auxY += dir.y;
                    }

                    //Encontramos una celda vacía
                    if (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Vacia) {
                        auxX += dir.x;
                        auxY += dir.y;
                        int azules = 0;
                        //Calculamos las azules inmediatas que hay al otro lado de la celda vacia
                        while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                            auxX += dir.x;
                            auxY += dir.y;
                            azules++;
                        }

                        //Si las visibles actuales de nuestra  celda, mas la vacía que vamos a poner en azul
                        //mas las azules que tiene a su otro lado supera el numero que debemos colocar, damos
                        //la pista would exceed
                        if (azules + 1 + _tablero[y][x].getCurrentVisibles() > _tablero[y][x].getValorDefault()) {
                            pista = new StructPista(TipoPista.WouldExceed, new Vector(x, y), dir);
                            quitaPista(pista, pistaAct);

                            return pista;
                        }
                    }
                }


                //PISTA 3-----------------------------------------------------------
                List<Pair<Integer, Integer>> celdaInfo = new ArrayList<>();

                for (int k = 0; k < _dirs.size(); k++) {
                    celdaInfo.add(calculaMaxColocablesYVisibles(x, y, _dirs.get(k)));
                }

                for (int k = 0; k < _dirs.size(); k++) {
                    int adyInmediatas = calculaAdyInmediatas(x, y, _dirs.get(k));
                    int maxVisiblesOtrasDir = 0;

                    for (int g = 0; g < _dirs.size(); g++) {
                        if (_dirs.get(g) != _dirs.get(k))
                            maxVisiblesOtrasDir += celdaInfo.get(g).getRight();
                    }

                    if (adyInmediatas + maxVisiblesOtrasDir < actual.getValorDefault()) {
                        //Comprobamos en que direccion es en la que podemos colocar más azules
                        //y es esa por tanto la direccion en la que tenemos que aplicar la pista
                        //Si en varias podemos colocar el mismo numero de azules, en la que tenemos
                        //que colocar es en la que potencialmente podamos conseguir un mayor numero de azules

                        int index = 0;
                        int visiblesInMaxCol = celdaInfo.get(index).getRight();
                        int maxCol = celdaInfo.get(index).getLeft();

                        for (int h = 1; h < _dirs.size(); h++) {
                            Pair<Integer, Integer> info = celdaInfo.get(h);
                            if (maxCol < info.getLeft() || (maxCol == info.getLeft() && visiblesInMaxCol < info.getRight())) {
                                index = h;
                                maxCol = info.getLeft();
                                visiblesInMaxCol = info.getRight();
                            }
                        }

                        pista = new StructPista(TipoPista.OneDirectionRequired, new Vector(x, y), _dirs.get(index));
                        break;
                    }
                }
            }
            //PISTA 4
            else if (actual.getCurrentVisibles() > actual.getValorDefault()) {
                pista = new StructPista(TipoPista.ErrorClosedTooLate, new Vector(x, y), new Vector(0, 0));
            }
            //PISTA 5
            else if (ady.getRight() && actual.getCurrentVisibles() < actual.getValorDefault()) {
                pista = new StructPista(TipoPista.ErrorClosedTooEarly, new Vector(x, y), new Vector(0, 0));
            }


        }
        //PISTA 6.1
        else if (actual.getEstado() == EstadoCelda.Azul && actual.isModifiable() && ady.getRight() && ady.getLeft() == 0) {
            pista = new StructPista(TipoPista.LockedIn, new Vector(x, y), new Vector(0, 0));
        }
        //PISTA 6.2
        else if (actual.getEstado() == EstadoCelda.Vacia && compruebaVaciaEncerrada(x, y)) {
            pista = new StructPista(TipoPista.MustBeWall, new Vector(x, y), new Vector(0, 0));
        }

        quitaPista(pista, pistaAct);

        return pista;
    }

    /**
     * Metodo que devuelve una pista aleatoria con su correspondiente mensaje y la posicion de la celda que
     * genera dicha pista
     * @return
     */
    public Pair<String, Vector> damePistaAleatoria() {
        return pistas.getPistaTablero();
    }

    /**
     * Metodo para actualizar las pistas de la celda que se ha modificado en CambiaCelda o CambiaCeldaInversa,
     * además del estado de todas las celdas que se encuentran en la misma fila y en la misma columna
     * @param x, int, coordenada x de la celda que vamos a actualizar su pista
     * @param y, int, coordenada y de la celda que vamos a actualizar su pista
     * @return
     */
    public Pistas compruebaPistas(int x, int y) {
        Pistas nuevasPistas = new Pistas();

        if (_tablero[y][x].getPista() != null) {
            pistas.getListaPistas().remove(_tablero[y][x].getPista());
        }
        StructPista p = getPistaInCoordenada(x, y);

        if (p != null) {
            nuevasPistas.addPista(p);
            _tablero[y][x].setCurrentPista(p);
        }
        for (int i = 0; i < _tablero.length; i++) {
            if (i != x) {
                if (_tablero[y][i].getPista() != null) {
                    pistas.getListaPistas().remove(_tablero[y][i].getPista());
                }
                p = getPistaInCoordenada(i, y);
                if (p != null) {
                    nuevasPistas.addPista(p);
                    _tablero[y][i].setCurrentPista(p);
                }
            }
        }
        for (int j = 0; j < _tablero.length; j++) {
            if (j != y) {
                if (_tablero[j][x].getPista() != null) {
                    pistas.getListaPistas().remove(_tablero[j][x].getPista());
                }
                p = getPistaInCoordenada(x, j);
                if (p != null) {
                    nuevasPistas.addPista(p);
                    _tablero[j][x].setCurrentPista(p);
                }
            }
        }

        return nuevasPistas;
    }


    /**
     * Metodo que, teniendo en cuenta el estado del tablero actual, te actualiza la lista pistas
     * con todas las pistas que cuenta actualmente el tablero
     */
    public void compruebaPistasTablero() {
        pistas = new Pistas();

        for (int i = 0; i < _tablero.length; i++) {
            for (int j = 0; j < _tablero.length; j++) {
                StructPista p = getPistaInCoordenada(i, j);
                if (p != null) {
                    pistas.addPista(p);
                    _tablero[j][i].setCurrentPista(p);
                }
            }
        }

    }

    /**
     * Metodo que cambia el estado de la celda si esta es modificable y actualiza la pista correspondiente a esta celda
     * llamando a compruebasPistas
     */
    public void cambiaCelda(int _posX, int _posY) {
        if (_tablero[_posY][_posX].isModifiable()) {
            EstadoCelda sig = EstadoCelda.Vacia;

            switch (_tablero[_posY][_posX].getEstado()) {
                case Azul: {
                    sig = EstadoCelda.Rojo;
                    break;
                }
                case Rojo: {
                    numCeldasNoVacias--;
                    sig = EstadoCelda.Vacia;
                    break;
                }
                case Vacia: {
                    numCeldasNoVacias++;
                    sig = EstadoCelda.Azul;
                    break;
                }
                case Default:
                    break;
            }

            _tablero[_posY][_posX].setEstado(sig);

            Pistas aux = compruebaPistas(_posX, _posY);
            pistas.getListaPistas().addAll(aux.getListaPistas());
        }

    }

    /**
     * Metodo que cambia el estado de la celda si esta es modificable y actualiza la pista correspondiente a esta celda
     * llamando a compruebasPistas. Empleado a la hora de deshacer movimientos.
     */
    public void cambiaCeldaInversa(int _posX, int _posY) {
        if (_tablero[_posY][_posX].isModifiable()) {
            EstadoCelda sig = EstadoCelda.Vacia;

            switch (_tablero[_posY][_posX].getEstado()) {
                case Azul: {
                    numCeldasNoVacias--;
                    sig = EstadoCelda.Vacia;
                    break;
                }
                case Rojo: {
                    sig = EstadoCelda.Azul;
                    break;
                }
                case Vacia: {
                    numCeldasNoVacias++;
                    sig = EstadoCelda.Rojo;
                    break;
                }
                case Default:
                    break;
            }

            _tablero[_posY][_posX].setEstado(sig);

            Pistas aux = compruebaPistas(_posX, _posY);
            pistas.getListaPistas().addAll(aux.getListaPistas());
        }

    }

    /**
     * Metodo que dada una posicion del tablero de una casilla vacia comprueba si esta
     * debe marcarse como cerrada o no
     * <p>
     * Para ello comprueba en todas las direcciones posibles si se encuentra dicha casilla encerrada
     * sin ninguna conexion con alguna azul
     * <p>
     * <p>
     * Este método se corresponde con la pista descrita en el punto nº6 de la practica
     */
    private boolean compruebaVaciaEncerrada(int x, int y) {

        for (int i = 0; i < _dirs.size(); i++) {
            int auxX = x + _dirs.get(i).x;
            int auxY = y + _dirs.get(i).y;

            if (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo)
                return false;
        }

        return true;
    }

    /**
     * Metodo que calcula el maximo de celdas colocables en la celda x,y en la direccion dir
     * @param x
     * @param y
     * @param dir
     * @return Pair con el maximo de celdas azules colocables (Left) y las celdas visibles(Right) en esa direccion
     */
    private Pair<Integer, Integer> calculaMaxColocablesYVisibles(int x, int y, Vector dir) {
        int auxX = x + dir.x, auxY = y + dir.y, colocables = 0, adyInDir = 0;
        int visibles = _tablero[y][x].getCurrentVisibles();

        //Nos saltamos las adyacentes azules directas
        while (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
            auxX += dir.x;
            auxY += dir.y;
            adyInDir++;
        }
        //caluculamos cuantas podemos colocar
        int diferenciaVision = _tablero[y][x].getValorDefault() - visibles;
        int azulesEncontradas = 0;

        while (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo
                && diferenciaVision - azulesEncontradas >= colocables + 1) {
            if (_tablero[auxY][auxX].getEstado() == EstadoCelda.Vacia) colocables++;
            else azulesEncontradas++;
            auxX += dir.x;
            auxY += dir.y;
        }

        if (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul)
            colocables--;

        int visiblesEnDir = colocables + adyInDir + azulesEncontradas;
        return new Pair<Integer, Integer>(colocables, visiblesEnDir);
    }

    /**
     * Calcula cual es el maximo de casillas potencialmente azules en una direccion
     * para cuando nos salimos de los limites del tablero o cuando nos encontramos con una celda roja
     * @param x, int, coordenada x de la casilla de la que queremos calcular cual es el maximo de azules que podra ver
     * @param y, int, coordenada y de la casilla de la que queremos calcular cual es el maximo de azules que podra ver
     * @param dir
     * @return
     */
    private int calculaMaxPosiblesVisibles(int x, int y, Vector dir) {
        int i = 0;
        int auxX = x + dir.x, auxY = y + dir.y;
        while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo) {
            i++;
            auxX += dir.x;
            auxY += dir.y;
        }
        return i;
    }

    /**
     * Metodo que dadas las coordenadas de una casilla, calcula el numero de casillas azules inmediatas que hay en la direccion dir
     * @param x, int, coordenada x de la casilla
     * @param y, int, coordenada y de la casilla
     * @param dir, Vector, direccion en la que queremos calcular el numero de adyacentes
     * @return
     */
    private int calculaAdyInmediatas(int x, int y, Vector dir) {
        int auxX = x + dir.x, auxY = y + dir.y, adyInDir = 0;

        //Nos saltamos las adyacentes azules directas
        while (posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
            auxX += dir.x;
            auxY += dir.y;
            adyInDir++;
        }

        return adyInDir;
    }

    /**
     * Metodo que dadas unas coordenadas de celda te devuelve la celda pedida
     * @param x, int, coordenada x de la celda
     * @param y, int, coordenada y de la celda
     * @return, la celda en la fila y columna x
     */
    public Celda getCelda(int x, int y) {
        return _tablero[y][x];
    }

    /**
     * Metodo para obtener el numero de filas que tiene nuestro tablero
     * @return int, dimensiones del tablero
     */
    public int getDimensions() {
        return _tablero.length;
    }

    /**
     * Metodo que dadas las coordenadas de una celda, la añade en las lista de celdas
     * no modificables
     * @param x, int, coordenada x de la celda que vamos a añadir a la lista
     * @param y, int, coordenada y de la celda que vamos a añadir a la lista
     */
    public void addToListaNoModificables(int x, int y) {
        _celdasFijas.add(new Vector(x, y));
    }

    /**
     * Metodo para actualizar las celdas visibles que estan viendo las celdas azules del
     * tablero
     */
    private void actualizaVisiblesTablero() {
        for (int i = 0; i < _tablero.length; i++) {
            for (int j = 0; j < _tablero.length; j++) {
                if (_tablero[i][j].getEstado() == EstadoCelda.Azul) {
                    int visibles = 0;
                    for (int k = 0; k < _dirs.size(); k++) {
                        visibles += calculaAdyInmediatas(j, i, _dirs.get(k));
                    }
                    _tablero[i][j].setCurrentVisibles(visibles);
                }
            }
        }
    }

    /**
     * Metodo que calcula el porcentaje de celdas que estan rellenas (No son vacias) sobre
     * las que no son modificables
     * @return
     */
    public int porcentajeCeldas() {
        return (int)((float)(numCeldasNoVacias - _celdasFijas.size()) / (float)( _tablero.length * _tablero.length - _celdasFijas.size()) * 100);
    }

    /**
     * Metodo que devuelve el numero de celdas actuales no vacias
     * @return
     */
    public int getNumCeldasNoVacias(){
        return numCeldasNoVacias;
    }
    /**
     * Metodo para actualizar el numero de celdas no vacias
     * @param newNum
     */
    public void setNumCeldasNoVacias(int newNum){
        numCeldasNoVacias =newNum;
    }
}
