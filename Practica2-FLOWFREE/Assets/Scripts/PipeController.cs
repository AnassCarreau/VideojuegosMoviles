using System.Collections;
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

        private BoardManager boardManager;

        private bool draw = false;
        private bool continueMoving = false;

        private Vector2 posIni;
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

        public struct Pipe
        {
            public Vector2 ini;
            public Vector2 fin;
            public int tam;
        }

        private int numPipesInBoard;
        private int totalPipesInBoard;
        private int moves;
        private Vector2 lastPipe;
        private bool lastPipeColor;
        float scaleFactor;
        void Start()
        {
            moves = 0;
            numPipesInBoard = 0;
            pipeRenderer.color = Color.black;
            //iNICIALIZACION DE LAS LISTAS 
            boardManager = GameManager.Instance.GetBoardManager();
            pipeParent = new Dictionary<Color, Transform>();
            pipeList = new Dictionary<Color, List<EachPipe>>();

            tilePipesIni = new Dictionary<Color, Tile>();

            pipeSolution = boardManager.getPipeSolution();
            colorCompleted = new HashSet<Color>();
            lastPipe = new Vector2();
            clueInPipe = new Dictionary<Color, bool>();
            starsInPipes = new Dictionary<Color, List<GameObject>>();

            Color[] c = boardManager.getPipesColor();
            for (int i = 0; i < c.Length; i++)
            {
                GameObject par = new GameObject();
                par.transform.SetParent(this.transform);
                par.name = c[i].ToString();
                pipeParent.Add(c[i], par.transform);
                pipeList.Add(c[i], new List<EachPipe>());
            }
            GameManager.Instance.SetflowsText(0);
            GameManager.Instance.SetMovesText(moves);
            GameManager.Instance.SetBestText();
            Percentage();
            scaleFactor = boardManager.getScaleFactor();
            //pipe.transform.localScale = new Vector3(scaleFactor, scaleFactor, scaleFactor);
        }

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
                    if (lastPipeColor && lastPipe != pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard())
                    {
                        Debug.Log(lastPipe);
                        moves++;
                        lastPipeColor = false;
                        //lastPipe = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                    }
                    GameManager.Instance.SetMovesText(moves);

                    if (AllPipesCompleted())
                    {
                        //TODO Llamar a levelManager para que muestre la ventanita del siguiente nivel
                        //Esto es temporal
                        Debug.Log("Ganaste");
                        GameManager.Instance.SetScore(moves);
                        GameManager.Instance.LoadScene("LevelSelector");
                        return;
                    }
                    draw = false;

                    tileIni = null;
                    tileAct = null;
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
                        GameManager.Instance.SetMovesText(moves);

                        if (AllPipesCompleted())
                        {
                            //TODO Llamar a levelManager para que muestre la ventanita del siguiente nivel
                            //Esto es temporal
                            Debug.Log("Ganaste");
                            GameManager.Instance.LoadScene("LevelSelector");
                            return;
                        }
                        draw = false;

                        tileIni = null;
                        tileAct = null;
                    }
                }
            }
#endif
            
#if UNITY_EDITOR
            if (Input.GetKeyUp(KeyCode.Space)) { PaintClue(); }
#endif
        }
        private void InitDrag(Vector2 posInBoard)
        {
            Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
            tileIni = boardManager.GetTileAtPosition(posAbsBoard);

            if (tileIni != null)
            {
                tileAct = tileIni;
                lastTilePainted = tileIni;

                //Si es un circulo destruimos todos los pipes de su color
                if (tileIni.IsCircle())
                {
                    draw = true;
                    continueMoving = true;
                    posIni = posAbsBoard;
                    posAct = posAbsBoard;

                    if (pipeRenderer.color != tileIni.GetCircleColor())
                    {
                        pipeRenderer.color = tileIni.GetCircleColor();
                        lastPipeColor = true;
                        if (pipeList[pipeRenderer.color].Count > 0)
                        {
                            lastPipe = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                            Debug.Log("Init "+ lastPipe);
                        }
                    }


                    tilePipesIni[pipeRenderer.color] = tileIni;
                    DestroyChildren();

                }
                //Si es un pipe y tiene index destruimos los hermanos posteriores a ese indice
                else if (tileIni.getIndex() != -1)
                {
                    draw = true;
                    if (pipeRenderer.color != tileIni.GetColor())
                    {
                        lastPipeColor = true;
                        pipeRenderer.color = tileIni.GetColor();
                        lastPipe = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                    }
                    posAct = posAbsBoard;
                    DestroyChildrenFromIndex(tileIni.getIndex() + 1);

                }
                //Comprobación de si hay estrellas en ese color
                if (clueInPipe.ContainsKey(pipeRenderer.color))
                {
                    starsInPipes[pipeRenderer.color][0].SetActive(false);
                    starsInPipes[pipeRenderer.color][1].SetActive(false);
                }
            }
        }

        /// <summary>
        /// Destruye todos los hijos de un color
        /// </summary>
        /// <param name="tr"> transform del padre </param>
        private void DestroyChildren()
        {
            while (pipeList[pipeRenderer.color].Count != 0)
            { 
                DestroyPipe(pipeRenderer.color, 0);
            }
            colorCompleted.Remove(pipeRenderer.color);
            GameManager.Instance.SetflowsText(colorCompleted.Count);
            Percentage();
        }
        /// <summary>
        /// Destruye todos los hijos de un color desde un indice especifico
        /// </summary>
        /// <param name="tr">transform del padre</param>
        /// <param name="index">indice desde el cual se remueven todos los siguientes hijos</param>
        private void DestroyChildrenFromIndex(int index)
        {
            Color c = tileAct.IsCircle() ? tileAct.GetCircleColor() : tileAct.GetColor();
            int total = pipeList[c].Count;
            int i = total - index;
            while(i!=0)
            {
                DestroyPipe(c, index);
                i--;
            }
            colorCompleted.Remove(c);
            GameManager.Instance.SetflowsText(colorCompleted.Count);
            Percentage();
        }

        private void DestroyPipe(Color c, int i)
        {
            EachPipe pipeToRemove = pipeList[c][i];
            pipeList[c].Remove(pipeToRemove);
            Tile childTile = boardManager.GetTileAtPosition(pipeToRemove.GetPositionInBoard());
            childTile.setFree(true);
            childTile.setIndex(-1);
            Destroy(pipeToRemove.gameObject);
            numPipesInBoard--;
        }

        private void OnDrag(Vector2 posInBoard)
        {
            //Posicion del raton en posiciones del tablero
            Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
            //Tile correspondiente a la posicion posAbsBoard
            Tile aux = boardManager.GetTileAtPosition(posAbsBoard);

            //Si estamos en un tile que no sea circulo de un color distinto al que estamos pintando y ese tile
            //y la posicion del ultimo tile en el que hemos pintado son adyacentes
            if (aux != null && !(aux.IsCircle() && aux.GetCircleColor() != pipeRenderer.color) && IsDirValid(posAbsBoard - posAct))
            {
                if (aux != tileAct)
                {
                    lastTilePainted = tileAct;
                    tileAct = aux;
                }
            }

            if (tileAct != null)
            {
                if (!tileAct.IsCircle())
                {
                    //Si esta vacio pintamos 
                    if (tileAct.isFree() && continueMoving)
                    {
                        CreatePipe(posAbsBoard);
                    }
                    //Si no esta vacio y es de un color diferente al actual rompemos la linea
                    else if (continueMoving && tileAct.GetColor() != pipeRenderer.color)
                    {
                        if (clueInPipe.ContainsKey(tileAct.GetColor()))
                        {
                            starsInPipes[tileAct.GetColor()][0].SetActive(false);
                            starsInPipes[tileAct.GetColor()][1].SetActive(false);
                        }
                        DestroyChildrenFromIndex(tileAct.getIndex());
                    }
                }

                //Si es la primera posicion y nos echamos para atras
                if (tileAct.IsCircle() && pipeList[pipeRenderer.color].Count == 1
                    && boardManager.GetTileAtPosition(tilePipesIni[pipeRenderer.color].GetPosTile()) == tileAct)
                {
                    DestroyPipe(pipeRenderer.color, pipeList[pipeRenderer.color].Count - 1);
                    posAct = posIni;
                }
                //Si te echas para atras en un pipe
                else if (!tileAct.isFree() && pipeList[pipeRenderer.color].Count > 1 && tileAct == boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].GetPositionInBoard()))
                {
                    continueMoving = true;
                    DestroyPipe(pipeRenderer.color, pipeList[pipeRenderer.color].Count - 1);
                    posAct = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();
                }

                //Si hemos tocado el otro extremo
                if (tileAct.IsCircle() && continueMoving && tileAct.GetCircleColor() == pipeRenderer.color
                    && boardManager.GetTileAtPosition(tilePipesIni[pipeRenderer.color].GetPosTile()) != tileAct)
                {
                    CreatePipe(posAbsBoard);
                    continueMoving = false;
                    colorCompleted.Add(pipeRenderer.color);
                    GameManager.Instance.SetflowsText(colorCompleted.Count);
                    Debug.Log("Pipe Completada");
                    //Comprobación de si hay estrellas en ese color
                    if (clueInPipe.ContainsKey(pipeRenderer.color))
                    {
                        starsInPipes[pipeRenderer.color][0].SetActive(true);
                        starsInPipes[pipeRenderer.color][1].SetActive(true);
                    }
                }
            }
        }
      
        public void PaintClue()
        {
            if (colorCompleted.Count < pipeSolution.Count)
            {
                Debug.Log(colorCompleted.Count + " " + pipeSolution.Count);

                List<List<Vector2>> aux = new List<List<Vector2>>(pipeSolution);
                bool pista = false;
                List<Vector2> l = new List<Vector2>();
                Color color = new Color();
                //Pista aleatoria de las pipes que faltan por resolver
                while (!pista)
                {
                    l = aux[Random.Range(0, aux.Count)];
                    color = boardManager.GetTileAtPosition(l[0]).GetCircleColor();
                    if (!colorCompleted.Contains(color))
                    {
                        pista = true;
                        Debug.Log("pIsta");
                    }
                    else
                    {
                        aux.Remove(l);
                    }
                }
                pipeRenderer.color = color;
                DestroyChildren();

                pipeRenderer.color = color;

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

                    if (act.GetColor() != pipeRenderer.color && !act.isFree() && !act.IsCircle())
                    {
                        DestroyChildrenFromIndex(act.getIndex());
                    }
                    PaintPipe(act, l[i], centerPipe(l[i - 1], dir), dir, false);
                }

                colorCompleted.Add(color);

                if (AllPipesCompleted())
                {
                    //TODO Llamar a levelManager para que muestre la ventanita del siguiente nivel
                    //Esto es temporal
                    Debug.Log("Ganaste");
                    GameManager.Instance.LoadScene("LevelSelector");
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

            pipeList[pipeRenderer.color].Add(Instantiate(pipe,new Vector2(posPipe.x, posPipe.y), rot, pipeParent[pipeRenderer.color]));
            pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].SetPositionInBoard(posAct_);
            pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].transform.localScale = new Vector3(scaleFactor, scaleFactor + 0.25f, 1.0f);

            act.setFree(false);
            act.setIndex(pipeList[pipeRenderer.color].Count - 1);
            act.SetColor(pipeRenderer.color);
            numPipesInBoard++;
            Percentage();
        }

        private bool IsDirValid(Vector2 dir)
        {
            if (Vector2.SqrMagnitude(dir) == 1) return true;
            else return false;
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
            //Antes de un posible cambio de dirección guardo la dir en la que iba
            if (IsDirValid(dirAct)) dirAnt = dirAct;

            dirAct = posAbsBoard - posAct;

            if (IsDirValid(dirAct) && boardManager.GetTileAtPosition(posAbsBoard) != null)
            {
                Vector2 posTileAnt = lastTilePainted.GetPosTile();
                posAct = posAbsBoard;

                Vector2 posPipe = centerPipe(posTileAnt, dirAct);

                PaintPipe(tileAct, posAct, posPipe, dirAct, true);
            }
        }

        private bool AllPipesCompleted()
        {
            return colorCompleted.Count == boardManager.getPipeSolution().Count;
        }

        public int numColorCompleted() { return colorCompleted.Count; }

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
            foreach (Color c in boardManager.getPipesColor())
            {
                Destroy(pipeParent[c].gameObject);
            }
        }
        public void SetTotalPipesInBoard(int n) { totalPipesInBoard = n; }

        private void Percentage()
        {
            int n = ((int)((float)numPipesInBoard / (float)totalPipesInBoard * 100));
            GameManager.Instance.SetPercentageText( n );
        }
    }
}
