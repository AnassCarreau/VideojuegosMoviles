using System;
using System.Collections.Generic;
using UnityEngine;
namespace FreeFlowGame
{
    public class PipeController : MonoBehaviour
    {
        [SerializeField]
        private EachPipe pipe;

        [SerializeField]
        SpriteRenderer pipeRenderer;

        [SerializeField]
        GameObject clueStar;

        //Diccionario para tener un padre donde instanciar los pipes de un color especifico
        private Dictionary<Color, Transform> pipeParent;

        //Set con los colores resueltos
        private HashSet<Color> colorCompleted;

        //Diccionario con los pipes de cada color
        private Dictionary<Color, List<EachPipe>> pipeList;

        private Dictionary<Color, Tile> tilePipesIni;

        //Diccionario donde guardamos si se ha puesto una pista en ese color para controlar que las estrellas salgan o no
        private Dictionary<Color, bool> clueInPipe;
        private Dictionary<Color, List<GameObject>> starsInPipes;

        //Cada tile que rompemos guardamos el anterior con los trozos de tuberia que he roto 
        private Dictionary<Tile, Stack<EachPipe>> brokePipes;

        private BoardManager boardManager;

        private bool draw;
        private bool continueMoving;

        private Vector2 posIni;
        [SerializeField]
        private Vector2 posAct;

        private Vector2 dirAct;
        private Vector2 dirAnt;

        private Tile tileIni;
        private Tile lastTilePainted;
        [SerializeField]
        private Tile tileAct;

        //Lista de listas con las posiciones de las pipes en su solucion
        private List<List<Vector2>> pipeSolution;

        private float anglePipe;


        private int numPipesInBoard;
        private int totalPipesInBoard;
        private int moves;
        private Vector2 lastPipe;
        private bool lastPipeColor;
        float scaleFactor;

        void Start()
        {
            //Pone las variables principales del dibujado como deben empezar
            ResetVariables();

            moves = 0;
            numPipesInBoard = 0;
            pipeRenderer.color = Color.black;
            //iNICIALIZACION DE LAS LISTAS 
            boardManager = BoardManager.Instance;
            pipeParent = new Dictionary<Color, Transform>();
            pipeList = new Dictionary<Color, List<EachPipe>>();

            tilePipesIni = new Dictionary<Color, Tile>();

            pipeSolution = boardManager.GetPipeSolution();
            colorCompleted = new HashSet<Color>();
            lastPipe = new Vector2();
            clueInPipe = new Dictionary<Color, bool>();
            starsInPipes = new Dictionary<Color, List<GameObject>>();
            brokePipes = new Dictionary<Tile, Stack<EachPipe>>();
            Color[] c = boardManager.GetPipesColor();
            for (int i = 0; i < c.Length; i++)
            {
                GameObject par = new GameObject();
                par.transform.SetParent(transform);
                par.transform.localScale = Vector3.one;
                par.name = c[i].ToString();
                pipeParent.Add(c[i], par.transform);
                pipeList.Add(c[i], new List<EachPipe>());
            }
            LevelManager.Instance.SetflowsText(0);
            LevelManager.Instance.SetMovesText(moves);
            LevelManager.Instance.SetBestText();
            Percentage();
        }

        public void SetScaleFactor(float scale) { scaleFactor = scale; }

        void Update()
        {
            Vector2 posInBoard;
            RaycastHit2D ra;
#if UNITY_EDITOR
            posInBoard = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            ra = Physics2D.Raycast(posInBoard, -Vector2.up, 0.1f);
            posInBoard.x /= scaleFactor;
            posInBoard.y /= scaleFactor;
            if (ra.collider != null)
            {
                //Primera pulsacion
                if (Input.GetMouseButtonDown(0))
                {
                    InitDrag(posInBoard);
                }
                //Arrastrar
                else if (draw && Input.GetMouseButton(0))
                {
                    OnDrag(posInBoard);
                }
                //Si soltamos
                else if (Input.GetMouseButtonUp(0))
                {
                    OnRelease();
                }
            }
#elif UNITY_ANDROID
            if (Input.touchCount > 0)
            {
                Touch touch = Input.GetTouch(0);
                posInBoard = Camera.main.ScreenToWorldPoint(touch.position);
                ra = Physics2D.Raycast(posInBoard, -Vector2.up, 0.1f);
                if (ra.collider != null)
                {
                    //Primera pulsacion
                    if (touch.phase == TouchPhase.Began)
                    {
                        InitDrag(posInBoard);
                    }
                    //Arrastrar
                    else if (draw && touch.phase == TouchPhase.Moved)
                    {
                        OnDrag(posInBoard);
                    }
                    //Si soltamos
                    else if (touch.phase == TouchPhase.Ended)
                    {
                       OnRelease();
                    }
                }
            }
#endif

#if UNITY_EDITOR
            if (Input.GetKeyUp(KeyCode.A)) { PaintClue(); }
#endif
        }

        private void InitDrag(Vector2 posInBoard)
        {
            //posAbsBoard son las coordenadas de la pulsación en coordenadas del tablero, es decir, (0, 0), (0, -1) ... en formato
            //(x, y)
            Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
            //Tile correspondiente a la primera pulsación
            tileIni = boardManager.GetTileAtPosition(posAbsBoard);

            if (tileIni != null)
            {
                tileAct = tileIni;
                lastTilePainted = tileIni;

                //Si es un circulo destruimos todos los pipes de su color
                if (tileIni.IsCircle())
                {
                    draw = true;
                    posIni = posAbsBoard;
                    posAct = posAbsBoard;

                    //Gestion para los movimientos y el cambio de color
                    if (pipeRenderer.color != tileIni.GetColor())
                    {
                        pipeRenderer.color = tileIni.GetColor();
                        lastPipeColor = true;
                        if (pipeList[pipeRenderer.color].Count > 0)
                        {
                            lastPipe = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                        }
                    }
                    //Guardamos el tile inicial en un diccionario de colores, para saber en qué tile/circulo empezamos
                    tilePipesIni[pipeRenderer.color] = tileIni;
                    //si hubiese ya tuberia de ese color se destruye con todo lo que eso conlleva
                    if (pipeList[pipeRenderer.color].Count > 0) DestroyChildren();
                }

                //Si es un pipe y tiene index destruimos los hermanos posteriores a ese indice
                else if (tileIni.GetIndex() != -1)
                {
                    draw = true;
                    //control del color y lastpipe para los movimientos
                    if (pipeRenderer.color != tileIni.GetColor())
                    {
                        lastPipeColor = true;
                        pipeRenderer.color = tileIni.GetColor();
                        lastPipe = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                    }
                    posAct = posAbsBoard;
                    DestroyChildrenFromIndex(tileAct.GetColor(), tileIni.GetIndex() + 1);
                }
                //Comprobaci�n de si hay estrellas en ese color
                if (clueInPipe.ContainsKey(pipeRenderer.color))
                {
                    starsInPipes[pipeRenderer.color][0].SetActive(false);
                    starsInPipes[pipeRenderer.color][1].SetActive(false);
                }
            }

        }

        private void OnDrag(Vector2 posInBoard)
        {
            //Posicion del raton en posiciones del tablero, al igual que en InitDrag
            Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
            //Tile correspondiente a la posicion posAbsBoard
            Tile aux = boardManager.GetTileAtPosition(posAbsBoard);

            //Si no nos hemos movido no hacemos nada
            if (aux == tileAct) return;

            //Si estamos en un tile que no sea circulo de un color distinto al que estamos pintando y ese tile
            //y la posicion del ultimo tile en el que hemos pintado son adyacentes
            if (aux != null && !(aux.IsCircle() && aux.GetColor() != pipeRenderer.color) && IsDirValid(posAbsBoard - posAct))
            {
                if (aux != tileAct)
                {
                    lastTilePainted = tileAct;
                    tileAct = aux;
                }
            }

            if (tileAct != null)
            {
                //Si esta vacio pintamos 
                if (tileAct.IsFree() && continueMoving && !tileAct.IsCircle())
                {
                    CreatePipe(posAbsBoard);
                }
                //Si no esta vacio y es de un color diferente al actual rompemos la linea
                else if (continueMoving && tileAct.GetColor() != pipeRenderer.color)
                { 
                    //Si tenia estrellas porque se habia dado una pista sobre ese color se desactivan dichas estrellas de los
                    //extremos del pipe
                    if (clueInPipe.ContainsKey(tileAct.GetColor()))
                    {
                        starsInPipes[tileAct.GetColor()][0].SetActive(false);
                        starsInPipes[tileAct.GetColor()][1].SetActive(false);
                    }

                    DestroyPipeTemporarily();
                    CreatePipe(posAbsBoard);
                }
                //Otro extremo
                else if (tileAct.IsCircle() && continueMoving && tileAct.GetColor() == pipeRenderer.color
                && boardManager.GetTileAtPosition(tilePipesIni[pipeRenderer.color].GetPosTile()) != tileAct)
                {
                    CreatePipe(posAbsBoard);
                    continueMoving = false;
                    colorCompleted.Add(pipeRenderer.color);

                    //Comprobaci�n de si hay estrellas en ese color
                    if (clueInPipe.ContainsKey(pipeRenderer.color))
                    {
                        starsInPipes[pipeRenderer.color][0].SetActive(true);
                        starsInPipes[pipeRenderer.color][1].SetActive(true);
                    }
                }
                else if ((!tileAct.IsFree() || tileAct.IsCircle()) && tileAct.GetColor() == pipeRenderer.color)
                {
                    if (!tileAct.IsCircle())
                        DestroyChildrenFromIndex(tileAct.GetColor(), tileAct.GetIndex() + 1);
                    else if (continueMoving)
                        DestroyChildren();
                    else return;
                    
                    posAct = pipeList[pipeRenderer.color].Count != 0 ? pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard() : posIni;
                    continueMoving = true;
                }

            }
        }

        private void OnRelease()
        {
            DestroyBrokenPipes();

            //¿Hemos hecho un ultimo movimiento?
            if (lastPipeColor && pipeList[pipeRenderer.color].Count > 0 && lastPipe != pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard())
            {
                moves++;
                lastPipeColor = false;
            }

            //Si hemos completado un pipe actualizamos el hud
            if (!continueMoving)
                LevelManager.Instance.SetflowsText(colorCompleted.Count);

            //Aumentamos en uno los movimientos empleados para resolver el tablero
            LevelManager.Instance.SetMovesText(moves);

            if (AllPipesCompleted())
            {
                GameManager.Instance.SetScore(moves);
                LevelManager.Instance.LevelCompleted(moves);
                //Para no actualizar los pipes cuando hemos pasado el nivel
                enabled = false;
                return;
            }

            ResetVariables();
        }

        private void Reconstruction(Tile tile)
        {
            if (brokePipes.Count > 0 && brokePipes.ContainsKey(tile))
            {
                foreach (EachPipe a in brokePipes[tile])
                {
                    Tile t = boardManager.GetTileAtPosition(a.GetPositionInBoard());
                    if (brokePipes.ContainsKey(t)) brokePipes.Remove(t);
                    t.SetColor(a.GetPipeColor());
                    t.SetIndex(a.GetPipeIndex());
                    a.transform.gameObject.SetActive(true);
                    numPipesInBoard++;
                }
                brokePipes.Remove(tile);

            }
        }
        /// <summary>
        /// Destruye todos los hijos de un color
        /// </summary>
        private void DestroyChildren()
        {
            int i = pipeList[pipeRenderer.color].Count;
            while (i != 0)
            {
                DestroyPipe(pipeRenderer.color, i - 1);
                if (brokePipes.Count > 0)
                {
                    Tile re = i == 1 ? tileAct : boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][i - 2].GetPositionInBoard());
                    Reconstruction(re);
                }
                i--;
            }
            colorCompleted.Remove(pipeRenderer.color);
            LevelManager.Instance.SetflowsText(colorCompleted.Count);
            Percentage();
        }

        /// <summary>
        /// Destruye todos los hijos de un color desde un indice especifico
        /// </summary>
        /// <param name="tr">transform del padre</param>
        /// <param name="index">indice desde el cual se remueven todos los siguientes hijos</param>
        private void DestroyChildrenFromIndex(Color c, int index)
        {
            int i = pipeList[c].Count;
            while (i != index)
            {
                DestroyPipe(c, i - 1);
                if (brokePipes.Count > 0)
                {
                    Tile re = boardManager.GetTileAtPosition(pipeList[c][i - 2].GetPositionInBoard());
                    Reconstruction(re);
                }

                i--;
            }
            colorCompleted.Remove(c);
            LevelManager.Instance.SetflowsText(colorCompleted.Count);
            Percentage();
        }

        private void DestroyPipe(Color c, int i)
        {
            EachPipe pipeToRemove = pipeList[c][i];

            if (pipeToRemove == null) return;
            Tile childTile = boardManager.GetTileAtPosition(pipeToRemove.GetPositionInBoard());

            pipeList[c].Remove(pipeToRemove);
            //¿Estoy destruyendo por rotura? No, pues entonces podemos poner el tile a free y el index a -1
            if (brokePipes.Count > 0 && !brokePipes.ContainsKey(childTile) || brokePipes.Count == 0)
            {
                childTile.SetIndex(-1);
            }
            Destroy(pipeToRemove.gameObject);
            numPipesInBoard--;
        }

        public void PaintClue()
        {
            if (colorCompleted.Count < pipeSolution.Count)
            {
                List<List<Vector2>> aux = new List<List<Vector2>>(pipeSolution);
                bool pista = false;
                List<Vector2> l = new List<Vector2>();
                Color color = new Color();
                //Pista aleatoria de las pipes que faltan por resolver
                while (!pista)
                {
                    l = aux[UnityEngine.Random.Range(0, aux.Count)];
                    color = boardManager.GetTileAtPosition(l[0]).GetColor();
                    if (!colorCompleted.Contains(color))
                    {
                        pista = true;
                    }
                    else
                    {
                        aux.Remove(l);
                    }
                }
                pipeRenderer.color = color;
                DestroyChildren();


                //Instanciamos las estrellas en los extremos si estamos en ellos y guardamos que hemos puesto estrella
                //si ya existen las estrellas simplemente las activamos
                if (!clueInPipe.ContainsKey(color))
                {
                    Tile tile_initial = boardManager.GetTileAtPosition(l[0]);
                    tilePipesIni[color] = tile_initial;
                    Tile tile_final = boardManager.GetTileAtPosition(l[l.Count - 1]);

                    List<GameObject> starsAux = new List<GameObject>();
                    starsAux.Add(Instantiate(clueStar, new Vector2(tile_initial.GetPosTile().x * scaleFactor, tile_initial.GetPosTile().y * scaleFactor), Quaternion.identity, pipeParent[pipeRenderer.color]));
                    starsAux.Add(Instantiate(clueStar, new Vector2(tile_final.GetPosTile().x * scaleFactor, tile_final.GetPosTile().y * scaleFactor), Quaternion.identity, pipeParent[pipeRenderer.color]));
                    starsInPipes.Add(color, starsAux);
                    clueInPipe.Add(color, true);
                }
                else
                {
                    starsInPipes[color][0].SetActive(true);
                    starsInPipes[color][1].SetActive(true);
                }

                //Pintado de la pista
                for (int i = 1; i < l.Count; i++)
                {
                    Vector2 dir = l[i] - l[i - 1];
                    Tile act = boardManager.GetTileAtPosition(l[i]);

                    if (act.GetColor() != pipeRenderer.color && !act.IsFree() && !act.IsCircle())
                    {
                        DestroyChildrenFromIndex(act.GetColor(), act.GetIndex());
                    }
                    PaintPipe(act, l[i], centerPipe(l[i - 1], dir), dir, false);
                }
                moves++;
                colorCompleted.Add(color);

                if (AllPipesCompleted())
                {
                    //Llamar a levelManager para que muestre la ventanita del siguiente nivel
                    LevelManager.Instance.LevelCompleted(moves);
                    return;
                }
            }
        }

        private void PaintPipe(Tile act, Vector2 posAct_, Vector2 posPipe, Vector2 dir, bool calculateAngle)
        {
            float angle = Mathf.Atan2(-dir.x, dir.y) * Mathf.Rad2Deg;
            if (calculateAngle)
            {
                anglePipe = GetAngleRotation();
                angle = anglePipe;
            }
            Quaternion rot = Quaternion.Euler(0f, 0f, angle);

            //Añadimos Pipe ->el que pintamos
            pipeList[pipeRenderer.color].Add(Instantiate(pipe, new Vector2(posPipe.x, posPipe.y), rot, pipeParent[pipeRenderer.color]));

            //Setteamos valores al eachpipe
            int index = pipeList[pipeRenderer.color].Count - 1;
            pipeList[pipeRenderer.color][index].SetPositionInBoard(posAct_);
            pipeList[pipeRenderer.color][index].SetPipeColor(pipeRenderer.color);
            pipeList[pipeRenderer.color][index].SetPipeIndex(index);

            //Setteamos valores actualizados al tile actual
            act.SetIndex(index);
            act.SetColor(pipeRenderer.color);

            numPipesInBoard++;
            Percentage();
        }

        private bool IsDirValid(Vector2 dir)
        {
            if (Vector2.SqrMagnitude(dir) == 1) return true;
            else return false;
        }

        private bool IsThereWallInDir(Tile tileAnt, Vector2 dir)
        {
            float angle = 360 + Mathf.Atan2(-dir.x, dir.y) * Mathf.Rad2Deg;
            return tileAnt.GetWalls()[(int)(angle % 360.0f / 90.0f)];
        }

        private float GetAngleRotation()
        {

            if (dirAct.x != dirAnt.x && dirAct.y != dirAnt.y)
            {
                //cambio de der/izq hacia abajo
                if (dirAnt.x != 0 && dirAct.x == 0)
                {
                    if (dirAct.y == -1) return 0f;
                    else if (dirAct.y == 1) return 180f;
                }
                //cambio de subir/bajar hacia der/izq
                else if (dirAnt.y != 0 && dirAct.y == 0)
                {
                    if (dirAct.x == 1) return 90f;
                    else if (dirAct.x == -1) return -90f;
                }
            }
            else if (dirAnt.x == 0 && dirAnt.y == 0)
            {
                if (dirAct.x == 1) return 90f;
                else if (dirAct.x == -1) return -90f;
            }
            return anglePipe;
        }

        private void CreatePipe(Vector2 posAbsBoard)
        {
            //Antes de un posible cambio de direcci�n guardo la dir en la que iba
            if (IsDirValid(dirAct)) dirAnt = dirAct;

            dirAct = posAbsBoard - posAct;

            Vector2 posLastPainted = lastTilePainted.GetPosTile();
            if (IsDirValid(dirAct) && !IsThereWallInDir(tileAct, dirAct) && IsDirValid(posLastPainted - posAbsBoard) && boardManager.GetTileAtPosition(posAbsBoard) != null)
            {
                posAct = posAbsBoard;
                Vector2 posPipe = centerPipe(posLastPainted, dirAct);
                PaintPipe(tileAct, posAct, posPipe, dirAct, true);
            }
        }

        private bool AllPipesCompleted()
        {
            return colorCompleted.Count == boardManager.GetPipeSolution().Count;
        }

        private Vector2 centerPipe(Vector2 posTileAnt, Vector2 dirAct_)
        {
            Vector2 posPipe = new Vector2(0, 0);

            if (dirAct_.x > 0) posPipe = new Vector2(posTileAnt.x + 0.5f, posTileAnt.y);
            else if (dirAct_.x < 0) posPipe = new Vector2(posTileAnt.x - 0.5f, posTileAnt.y);
            else if (dirAct_.y > 0) posPipe = new Vector2(posTileAnt.x, posTileAnt.y + 0.5f);
            else if (dirAct_.y < 0) posPipe = new Vector2(posTileAnt.x, posTileAnt.y - 0.5f);

            return posPipe * scaleFactor;
        }

        private void OnDestroy()
        {
            foreach (Color c in boardManager.GetPipesColor())
            {
                Destroy(pipeParent[c].gameObject);
            }
        }
        public void SetTotalPipesInBoard(int n) { totalPipesInBoard = n; }

        private void Percentage()
        {
            int n = ((int)((float)numPipesInBoard / (float)totalPipesInBoard * 100));
            LevelManager.Instance.SetPercentageText(n);
        }

        public void ResetPipes()
        {

            //Borrar tuberias
            foreach (Color c in pipeParent.Keys)
            {
                pipeRenderer.color = c;
                DestroyChildren();
            }

            //Borrar estrellas (pistas)
            foreach (Color c in starsInPipes.Keys)
            {
                while (starsInPipes[c].Count > 0)
                {
                    Destroy(starsInPipes[c][0].gameObject);
                    starsInPipes[c].RemoveAt(0);
                }

            }

            numPipesInBoard = 0;
            moves = 0;
            starsInPipes.Clear();
            clueInPipe.Clear();
            brokePipes.Clear();

            //Actualizamos HUD
            Percentage();
            LevelManager.Instance.SetflowsText(colorCompleted.Count);
            LevelManager.Instance.SetMovesText(moves);
            ResetVariables();
        }

        private void ResetVariables()
        {
            draw = false;
            continueMoving = true;
            tileIni = null;
            tileAct = null;
            lastTilePainted = null;
        }

        private void DestroyBrokenPipes()
        {
            //Si hemos roto alguna tuberia, destruimos los gameObject que hemos desactivado
            if (brokePipes.Count > 0)
            {
                foreach (Tile tile in brokePipes.Keys)
                {
                    Color pipeColor = brokePipes[tile].Peek().GetPipeColor();

                    if (colorCompleted.Contains(pipeColor)) colorCompleted.Remove(pipeColor);

                    while (brokePipes[tile].Count != 0)
                    {
                        EachPipe pipeToDestroy = brokePipes[tile].Pop();
                        if (pipeToDestroy.gameObject != null)
                        {
                            pipeList[tile.GetColor()].Remove(pipeToDestroy);
                            Destroy(pipeToDestroy.gameObject);
                        }
                    }
                }
                LevelManager.Instance.SetflowsText(colorCompleted.Count);
                brokePipes.Clear();
            }
        }

        private void DestroyPipeTemporarily()
        {
            //Nos guardamos la lista del pipe que vamos a romper
            List<EachPipe> listeach = new List<EachPipe>(pipeList[tileAct.GetColor()]);

            listeach.RemoveRange(0, tileAct.GetIndex());
            //Este bucle recorre los pipes hasta que uno esta desactivado significando que ya se rompio antes luego los siguientes tambien estaran 
            // rotos asi que quitamos ese rango y nos quedamos solo con los que nosotros ponemos a false 
            int j = 0;
            foreach (EachPipe a in listeach)
            {
                Tile t = boardManager.GetTileAtPosition(a.GetPositionInBoard());
                //Si el objeto esta activo lo desactivamos y ponemos el tile como libre
                if (a != null && a.transform.gameObject.activeSelf)
                {
                    //Debug.Log("Trozo temporalmente destruido " + t);
                    t.SetIndex(-1);
                    a.transform.gameObject.SetActive(false);
                    numPipesInBoard--;
                    j++;
                }
                else { listeach.RemoveRange(j, listeach.Count - j); break; }
            }

            //añadimos a la cola un par con el tile anterior al que hemos atravesado (porque posAct se actualiza en 
            //CreatePipe entonces sigue siendo el del anterior tile) y la lista con las tuberías restantes del pipe a romper 
            brokePipes.Add(boardManager.GetTileAtPosition(posAct), new Stack<EachPipe>(listeach));
        }
    }
}


