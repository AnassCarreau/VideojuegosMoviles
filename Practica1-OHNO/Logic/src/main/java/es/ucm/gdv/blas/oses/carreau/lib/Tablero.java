package es.ucm.gdv.blas.oses.carreau.lib;

import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;

import org.graalvm.compiler.nodes.calc.IntegerTestNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tablero {
    private Celda [][] _tablero;
    private List<Vector> _dirs;
    private List<Vector> _celdasFijas;
    private int _posX = 0, _posY = 0;
    //LISTA DE PISTAS
    private Pistas pistas = null;



    public  Tablero(int N, boolean randomBoard) {

        //Inicializacion vector direcciones
        _dirs = new ArrayList<>();

        _dirs.add(new Vector(0, 1)); //ABAJO
        _dirs.add(new Vector(1, 0)); //DCHA
        _dirs.add(new Vector(-1, 0));//IZQDA
        _dirs.add(new Vector(0, -1));//ARRIBA

        //Inicializacion tablero
        _tablero = new Celda[N][N];

        //Inicializar lista fijas
        _celdasFijas = new ArrayList<>();

        for (int i = 0; i < _tablero.length; i++) {
            for (int j = 0; j < _tablero.length; j++) {
                _tablero[i][j] = new Celda();
            }
        }

        //generamos un tablero aleatorio con una unica solucion


       for (int i =0 ;i<10; i++) {
          while (!generaTablerov2(N)) ;
           System.out.println("Tablero Bueno");
       }

        //TO DO: HACER BIEN
      /* if(randomBoard){
            if(N == 6) tableroPrueba6x6();
            else if(N == 4)tableroPrueba4x4();
            else generaTablero(N);


        //Inicializacion de las pistas
            pistas = compruebaPistasTablero();*/


    }


    private boolean generaTablerov2(int N) {

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
        for (int i = 0; i < numCeldasAzules; i++) {

            int x = random.nextInt(N);
            int y = random.nextInt(N);

            //Buscamos casilla que esté vacia (por si da la casualidad de que con los randoms alguna coincide)
            while (_tablero[y][x].getEstado() != EstadoCelda.Vacia) {
                x = random.nextInt(N);
                y = random.nextInt(N);
            }

            Celda c = _tablero[y][x];

            c.setEstado(EstadoCelda.Azul);
            c.setModificable(false);
            c.setValorDefault(random.nextInt(N) + 1); //Valor entre las que ve (1 a N)
            _celdasFijas.add(new Vector(x, y));
        }

        //Colocamos aleatoriamente  numCeldasRojas rojas
        for (int i = 0; i < numCeldasRojas; i++) {
            int x = random.nextInt(N);
            int y = random.nextInt(N);

            //Buscamos casilla que esté vacia (por si da la casualidad de que con los randoms alguna coincide)
            while (_tablero[y][x].getEstado() != EstadoCelda.Vacia) {
                x = random.nextInt(N);
                y = random.nextInt(N);
            }
            Celda c = _tablero[y][x];

            c.setEstado(EstadoCelda.Rojo);
            c.setModificable(false);
            _celdasFijas.add(new Vector(x, y));
        }

        System.out.println("---");
        //Inicializamos las pistas
        pistas = compruebaPistasTablero();
        for (StructPista sp : pistas.getListaPistas()) {
            System.out.println(sp.getTipoPista());
            System.out.println(sp.getPosPista().x + " " + sp.getPosPista().y);
        }

        while (!pistas.isEmpty()) {
            System.out.println("BuclePistas");
            Pistas auxPista = new Pistas();
            //Cogemos la primera de la lista
            StructPista pista = pistas.getFirstPista();

            //Coordenadas correspondientes a la celda
            int auxX = pista.getPosPista().x;
            int auxY = pista.getPosPista().y;

            //cogemos que tipo de pista es
            switch (pista.getTipoPista()) {
                case WouldExceed: {
                    System.out.println("PISTA_WOULDESCEED");
                    //mirar en que direccion te pasas y poner una roja
                    Vector dir = pista.getDirPista();
                    int newX = auxX + dir.x, newY = auxY + dir.y;

                    //Nos saltamos los azules adyacentes
                    while (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Azul) {
                        newX += dir.x;
                        newY += dir.y;
                        System.out.println(1);
                    }

                    if (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Vacia) {
                        _tablero[newY][newX].setEstado(EstadoCelda.Rojo);
                        auxPista = compruebaPistas(newX, newY);
                    }
                    break;
                }
                case ValueReached: {
                    //cerrar celda
                    System.out.println("PISTA_VALUEREACHED");

                    List<Vector> lis = encierraCelda(auxX, auxY);

                    for (int i = 0; i < lis.size(); i++) {
                        int x = lis.get(i).x;
                        int y = lis.get(i).y;
                        Pistas p = compruebaPistas(x, y);
                        while (!p.getListaPistas().isEmpty()) {

                            if (!auxPista.getListaPistas().contains(p.getFirstPista())) {
                                auxPista.getListaPistas().add(p.getFirstPista());
                            }
                            p.getListaPistas().remove(0);
                            System.out.println(2);

                        }

                    }
                    break;
                }
                case OneDirectionRequired: {
                    System.out.println("PISTA_OneDirectionRequired");

                    //Cogemos la direccion de la pista y asi no tenemos que recorrer todas las dirs
                    Vector dir = pista.getDirPista();

                    int newX = auxX + dir.x;
                    int newY = auxY + dir.y;
                    while (posCorrecta(newX, newY) && _tablero[newY][newX].getEstado() == EstadoCelda.Azul) {
                        newX += dir.x;
                        newY += dir.y;
                        System.out.println("pRINT DE TU MADRE");
                        System.out.println(" NEWX: " + newX + ", NEWY: " + newY);
                        // System.out.println("dIRX "+ dir.getLeft());
                        // System.out.println( "dIRY " +dir.getRight());
                        // System.out.println("---");


                        System.out.println(3);
                    }
                    //Por seguridad, pero si no nos hemos salido es que esa celda vacia es azul
                    //porque es la que debemos poner en esa direccion
                    if (posCorrecta(newX, newY)) {
                        System.out.println("vamos a llamar al compruebaPista webon");
                        _tablero[newY][newX].setEstado(EstadoCelda.Azul);
                        auxPista = compruebaPistas(newX, newY);
                    }
                    else{
                        System.out.println("EMMM PROGRAMA DE MIERDA");
                    }
                    break;
                }
                case MustBeWall:
                    System.out.println("PISTA_MustBeWall");

                    _tablero[auxY][auxX].setEstado(EstadoCelda.Rojo);
                    auxPista = compruebaPistas(auxX, auxY);
                    break;
                //TODOS LOS CASOS A PARTIR DE AQUI SON CASOS DE ERROR, POR LO QUE SI SALE ALGUNO DE ESTOS DESCARTAMOS TABLEROS
                case ImposibleVision:
                    //   System.out.println("Imposible to fill the vision of this tile " + auxX + " " + auxY + "\n");
                    return false;
                case ErrorClosedTooLate:
                    //  System.out.println("This number sees a bit too much "  + auxX + " " + auxY +  "\n");
                    return false;
                case ErrorClosedTooEarly:
                    //  System.out.println("This number can't see enough "  + auxX + " " + auxY +  "\n");
                    return false;
                case LockedIn:
                    //   System.out.println("A blue dot should always #see at least one other " + auxX + " " + auxY + "\n");
                    return false;
            }

            while (!auxPista.isEmpty()) {
                StructPista pi = auxPista.getFirstPista();
                pistas.addPista(pi);
                auxPista.getListaPistas().remove(0);
            }

        }

        //Si hay alguna celda vacia el tablero no nos vale
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (_tablero[i][j].getEstado() == EstadoCelda.Vacia) return false;
            }
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
        pistas = compruebaPistasTablero();

        //Devolvemos true si conseguimos crear el tablero
        return true;
    }

    private List<Vector> encierraCelda(int x, int y){
        List<Vector>l=new ArrayList<>();
        for(int i = 0; i < _dirs.size(); i++) {
            Vector dir = _dirs.get(i);
            int auxX = x + dir.x;
            int auxY = y + dir.y;

            while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                auxX += dir.x;
                auxY += dir.y;
            }

            if (posCorrecta(auxX, auxY)) {
                 l.add(new Vector(auxX,auxY));
                _tablero[auxY][auxX].setEstado(EstadoCelda.Rojo);
            }
        }
        return  l;
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
        _tablero[2][1].setEstado(EstadoCelda.Rojo);
        _tablero[2][1].setModificable(false);
        _celdasFijas.add(new Vector(1,2));


        _tablero[4][2].setEstado(EstadoCelda.Rojo);
        _tablero[4][2].setModificable(false);
        _celdasFijas.add(new Vector(2,4));

        _tablero[5][2].setEstado(EstadoCelda.Rojo);
        _tablero[5][2].setModificable(false);
        _celdasFijas.add(new Vector(2,5));

        _tablero[1][5].setEstado(EstadoCelda.Rojo);
        _tablero[1][5].setModificable(false);
        _celdasFijas.add(new Vector(5,1));


        _tablero[0][0].setEstado(EstadoCelda.Azul);
        _tablero[0][0].setModificable(false);
        _tablero[0][0].setValorDefault(5);
        _celdasFijas.add(new Vector(0,0));


        _tablero[5][0].setEstado(EstadoCelda.Azul);
        _tablero[5][0].setModificable(false);
        _tablero[5][0].setValorDefault(1);
        _celdasFijas.add(new Vector(0,5));


        _tablero[0][1].setEstado(EstadoCelda.Azul);
        _tablero[0][1].setModificable(false);
        _tablero[0][1].setValorDefault(3);
        _celdasFijas.add(new Vector(1,0));


        _tablero[3][1].setEstado(EstadoCelda.Azul);
        _tablero[3][1].setModificable(false);
        _tablero[3][1].setValorDefault(4);
        _celdasFijas.add(new Vector(1,3));


        _tablero[3][2].setEstado(EstadoCelda.Azul);
        _tablero[3][2].setModificable(false);
        _tablero[3][2].setValorDefault(5);
        _celdasFijas.add(new Vector(2,3));


        _tablero[4][3].setEstado(EstadoCelda.Azul);
        _tablero[4][3].setModificable(false);
        _tablero[4][3].setValorDefault(3);
        _celdasFijas.add(new Vector(3,4));


        _tablero[2][3].setEstado(EstadoCelda.Azul);
        _tablero[2][3].setModificable(false);
        _tablero[2][3].setValorDefault(1);
        _celdasFijas.add(new Vector(3,2));


        _tablero[0][4].setEstado(EstadoCelda.Azul);
        _tablero[0][4].setModificable(false);
        _tablero[0][4].setValorDefault(2);
        _celdasFijas.add(new Vector(4,0));


        _tablero[3][4] .setEstado(EstadoCelda.Azul);
        _tablero[3][4] .setModificable(false);
        _tablero[3][4] .setValorDefault(3);
        _celdasFijas.add(new Vector(4,3));


        _tablero[4][5] .setEstado(EstadoCelda.Azul);
        _tablero[4][5].setModificable(false);
        _tablero[4][5].setValorDefault(5);
        _celdasFijas.add(new Vector(5,4));

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

        //System.out.println("POSX "+x);
        //System.out.println("POSY "+y);
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
            auxX = x + _dirs.get(i).x;
            auxY = y + _dirs.get(i).y;

            while(posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul){
                auxX += _dirs.get(i).x;
                auxY += _dirs.get(i).y;
                visibles++;
            }
            if(posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo) encerrada =false;
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

  public  void quitaPista(StructPista pista, StructPista pistaAct) {
        if(pistas != null) {
            if (pistaAct != null) {

                pistas.getListaPistas().remove(pistaAct);
            }
            if (pista != null && pistaAct != null) {
                _tablero[pistaAct.getPosPista().y][pistaAct.getPosPista().x].setCurrentPista(pista);

            }
        }
  }

    public  StructPista getPistaInCoordenada(int x ,int y) {
        Celda actual = _tablero[y][x];
        Pair<Integer, Boolean> ady = compruebaAdyacentes(x, y);
        StructPista pista = null;
        StructPista pistaAct = actual.getPista();
        if (actual.getEstado() == EstadoCelda.Azul && !actual.isModifiable()) {

            actual.setCurrentVisibles(ady.getLeft());
            //PISTA 1
            if (actual.getCurrentVisibles() == actual.getValorDefault() && !ady.getRight()) {
                pista = new StructPista(TipoPista.ValueReached, new Vector(x, y), new Vector(0, 0));
            }
            else if (!ady.getRight() && actual.getCurrentVisibles() < actual.getValorDefault()) {

                //PISTA 10
                int maxColocables = 0;

                for (int k = 0; k < _dirs.size(); k++) {
                    maxColocables += calculaMaxColocablesYVisibles(x, y, _dirs.get(k)).getLeft();
                }

                if (maxColocables < actual.getValorDefault() - actual.getCurrentVisibles()) {
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
                        System.out.println(1);
                    }

                    if (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Vacia) {
                        auxX += dir.x;
                        auxY += dir.y;
                        int azules = 0;
                        while (posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul) {
                            auxX += dir.x;
                            auxY += dir.y;
                            azules++;
                            System.out.println(2);

                        }

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

                        int index = 0;
                        int maxCol = celdaInfo.get(index).getLeft();

                        for(int h = 0; h < _dirs.size(); h++){
                            if(maxCol < celdaInfo.get(h).getLeft()){
                                index = h;
                                maxCol = celdaInfo.get(h).getLeft();
                            }
                        }

                        pista = new StructPista(TipoPista.OneDirectionRequired, new Vector(x, y), _dirs.get(index));
                        break;
                    }
                }
            }
            //PISTA 4
            else if (/*ady.getRight() &&*/ actual.getCurrentVisibles() > actual.getValorDefault()) {
                pista = new StructPista(TipoPista.ErrorClosedTooLate, new Vector(x, y), new Vector(0, 0));
            }
            //PISTA 5
            else if (/*ady.getRight() &&*/ actual.getCurrentVisibles() < actual.getValorDefault()) {
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

    public  Pair<String, Vector> damePistaAleatoria() {
        System.out.println(pistas.getListaPistas().size());
        for (int i =0; i<pistas.getListaPistas().size();i++)
        {
            System.out.println(pistas.getListaPistas().get(i).getTipoPista());
            System.out.println(pistas.getListaPistas().get(i).getPosPista().x);
            System.out.println(pistas.getListaPistas().get(i).getPosPista().y);

        }
        return pistas.getPistaTablero();
    }

    public Pistas compruebaPistas(int x,int y) {
        Pistas nuevasPistas = new Pistas();


        for (int i = 0; i < _tablero.length; i++) {

            StructPista p = getPistaInCoordenada(i, y);
            if (p != null) {
                nuevasPistas.addPista(p);
                _tablero[y][i].setCurrentPista(p);
            }
        }
        for (int j = 0; j < _tablero.length; j++) {
            if (y != j) {
                StructPista p = getPistaInCoordenada(x, j);
                if (p != null) {
                    nuevasPistas.addPista(p);
                    _tablero[j][x].setCurrentPista(p);
                }
            }
        }

        return nuevasPistas;
    }



    public Pistas compruebaPistasTablero() {
        Pistas nuevasPistas = new Pistas();

        for (int i = 0; i < _tablero.length; i++) {
            for (int j = 0; j < _tablero.length; j++) {
                StructPista p= getPistaInCoordenada(i, j);
                if(p!=null) {
                    nuevasPistas.addPista(p);
                    _tablero[i][j].setCurrentPista(p);
                }
            }
        }

        return nuevasPistas;
    }

    //i es el numero de fila es decir la posicion en Y
    //j es eñ numero de la columna es decir la posicion en X
   /* private boolean checkHint2(int i, int j, Pistas nuevasPistas){

    }*/
    /**
     * Metodo que cambia el estado de la celda si esta es modificable y actualiza el numero de
     * casillas que ven las celdas azules predefinidas utilizando el metodo de compruebaAdyacentes()
     */
    public boolean cambiaCelda(int _posX, int _posY){
        if (_tablero[_posX][_posY].isModifiable()) {
            //EstadoCelda sig = EstadoCelda.values()[(_tablero[_posX][_posY].getEstado().ordinal() + 1) % EstadoCelda.Default.ordinal()];
            EstadoCelda sig = EstadoCelda.Vacia;

            switch (_tablero[_posX][_posY].getEstado()) {
                case Azul: {
                    sig = EstadoCelda.Rojo;
                    break;
                }
                case Rojo: {
                    sig = EstadoCelda.Vacia;
                    break;
                }
                case Vacia: {
                    sig = EstadoCelda.Azul;
                    break;
                }
                case Default:
                    break;
            }

            _tablero[_posX][_posY].setEstado(sig);


            for (int i = 0; i < _celdasFijas.size(); i++) {
                Vector p = _celdasFijas.get(i);
                int x = (p.x);
                int y = (p.y);
                _tablero[y][x].setCurrentVisibles((compruebaAdyacentes(x, y).getLeft()));
            }


            Pistas aux = compruebaPistas(_posX, _posY);
            while (!aux.isEmpty()) {
                StructPista pi = aux.getFirstPista();
                pistas.addPista(pi);
                aux.getListaPistas().remove(0);
            }
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
            int auxX = x + _dirs.get(i).x;
            int auxY = y + _dirs.get(i).y;

            if(posCorrecta(auxX, auxY) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo) return false;
        }

        return true;
    }

    private Pair<Integer,Integer> calculaMaxColocablesYVisibles(int x, int y, Vector dir){
        int auxX = x + dir.x, auxY = y+ dir.y, colocables = 0,adyInDir = 0;
        int visibles = _tablero[y][x].getCurrentVisibles();

        //Nos saltamos las adyacentes azules directas
        while(posCorrecta( auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul){
            auxX += dir.x;
            auxY += dir.y;
            adyInDir++;
        }
        //caluculamos cuantas podemos colocar
        int diferenciaVision = _tablero[y][x].getValorDefault() - visibles;
        int azulesEncontradas = 0;

        while(posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() != EstadoCelda.Rojo
                && diferenciaVision - azulesEncontradas >=  colocables + 1){
            if(_tablero[auxY][auxX].getEstado() == EstadoCelda.Vacia)colocables++;
            else azulesEncontradas++;
            auxX += dir.x;
            auxY += dir.y;

        }

        if(posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul)colocables--;

        int visiblesEnDir = colocables + adyInDir + azulesEncontradas;
        return new Pair<Integer,Integer>(colocables, visiblesEnDir);
    }

    private int calculaAdyInmediatas(int x, int y, Vector dir){
        int auxX = x + dir.x, auxY = y+ dir.y,adyInDir = 0;

        //Nos saltamos las adyacentes azules directas
        while(posCorrecta(auxY, auxX) && _tablero[auxY][auxX].getEstado() == EstadoCelda.Azul){
            auxX += dir.x;
            auxY += dir.y;
            adyInDir++;
        }

        return adyInDir;
    }

    public Celda getCelda(int x, int y){
        return _tablero[y][x];
    }

    public int getDimensions(){
        return _tablero.length;
    }

    public void addToListaNoModificables(int x, int y){
        _celdasFijas.add(new Vector(x,y));
    }


}
