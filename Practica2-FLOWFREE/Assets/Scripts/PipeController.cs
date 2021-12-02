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

        //Diccionario para tener un padre donde instanciar los pipes de un color especifico
        private Dictionary<Color, Transform> pipeParent;
        
        //Diccionario para tener el color, el principio, el fin y el tamaño de cada pipe
        private Dictionary<Color, Pipe> pipeInfo;
        
        //Set con los colores resueltos
        private HashSet<Color> colorCompleted;

        //Diccionario con los pipes de cada color
        private Dictionary<Color, List<EachPipe>> pipeList;

        private Dictionary<Color, Tile> tilePipesIni;
        
        private BoardManager boardManager;

        private bool draw = false;
        private bool continueMoving = false;

        private Vector2 posIni;
        private Vector2 posAct;

        private Vector2 dirAct;
        private Vector2 dirAnt;

        [SerializeField]
        private Tile tileIni;
        private Tile tileAnt;
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

        void Start()
        {
            //iNICIALIZACION DE LAS LISTAS 
            boardManager = GameManager.Instance.GetBoardManager();
            pipeParent = new Dictionary<Color, Transform>();
            pipeList = new Dictionary<Color, List<EachPipe>>();
            pipeInfo = new Dictionary<Color, Pipe>();

            tilePipesIni = new Dictionary<Color, Tile>();

            pipeSolution = boardManager.getPipeSolution();
            colorCompleted = new HashSet<Color>();
            Color[] c = boardManager.getPipesColor();
            for (int i = 0; i < c.Length; i++)
            {
                GameObject par = new GameObject();
                par.name = c[i].ToString();
                pipeParent.Add(c[i], par.transform);
                pipeList.Add(c[i], new List<EachPipe>());

                Pipe pipe =new Pipe();
                pipe.ini = pipeSolution[i][0];
                pipe.fin = pipeSolution[i][pipeSolution[i].Count - 1];
                //El tamaño es menos dos porque falta crear un pipe en el primer circulo
                pipe.tam = pipeSolution[i].Count - 2;

                pipeInfo.Add(c[i], pipe);
            }
        }
        
        /// <summary>
        /// Destruye todos los hijos de un color
        /// </summary>
        /// <param name="tr"> transform del padre </param>
        private void DestroyChildren()
        {            
            while(pipeList[pipeRenderer.color].Count != 0)
            {
                EachPipe pipeToRemove = pipeList[pipeRenderer.color][0];
                pipeList[pipeRenderer.color].Remove(pipeToRemove);
                
                Tile childTile = boardManager.GetTileAtPosition(pipeToRemove.GetPositionInBoard());
                childTile.setFree(true);
                childTile.setIndex(-1);

                Destroy(pipeToRemove.gameObject);
            }
            colorCompleted.Remove(pipeRenderer.color);
        }
        /// <summary>
        /// Destruye todos los hijos de un color desde un indice especifico
        /// </summary>
        /// <param name="tr">transform del padre</param>
        /// <param name="index">indice desde el cual se remueven todos los siguientes hijos</param>
        private void DestroyChildrenFromIndex(Transform tr, int index)
        {
            Debug.Log("DestroyChildren papaaaaaaaa");
            Color c = tileAct.IsCircle() ? tileAct.GetCircleColor() : tileAct.GetColor();
            int k = tr.childCount ;
            for (int i = index; i < k; i++)
            {
                EachPipe pipeToRemove = pipeList[c][index];
                pipeList[c].Remove(pipeToRemove);

                Tile childTile = boardManager.GetTileAtPosition(pipeToRemove.GetPositionInBoard());
                childTile.setFree(true);
                childTile.setIndex(-1);
                Destroy(pipeToRemove.gameObject);
            }
            colorCompleted.Remove(tileAct.GetColor());
        }

        void Update()
        {
            //TO DO LIMPIAR CODIGO EN METODOS
            Vector2 posInBoard = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            RaycastHit2D ra = Physics2D.Raycast(posInBoard, -Vector2.up);
            //Primera pulsacion
            if (ra.collider != null && Input.GetMouseButtonDown(0))
            {
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileIni = boardManager.GetTileAtPosition(posAbsBoard);
                tileAnt = tileIni;
                if (tileIni != null)
                {
                    //Si es un circulo destrimos todos los pipes de su calor
                    if (tileIni.IsCircle())
                    {
                        draw = true;
                        continueMoving = true;
                        posIni = posAbsBoard;
                        posAct = posAbsBoard;
                        pipeRenderer.color = tileIni.GetCircleColor();

                        tilePipesIni[pipeRenderer.color] = tileIni;

                        DestroyChildren();
                    }
                    //Si es un pipe y tiene index destruimos los hermanos posteriores a ese indice
                    else if (tileIni.getIndex() != -1)
                    {
                        draw = true;
                        pipeRenderer.color = tileIni.GetColor();
                        posAct = posAbsBoard;
                        DestroyChildrenFromIndex(pipeParent[pipeRenderer.color], tileIni.getIndex() + 1);
                    }
                }

            }
            //Arrastrar
            else if (draw && Input.GetMouseButton(0))
            {
                if(tileAct != null) tileAnt = tileAct;

                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileAct = boardManager.GetTileAtPosition(posAbsBoard);

                if (ra.collider != null && tileAct != null)
                {
                    if (!tileAct.IsCircle())
                    {
                        //Si esta vacio pintamos 
                        if (tileAct.isFree())
                        {
                            //Antes de un posible cambio de dirección guardo la dir en la que iba
                            if (IsDirValid(dirAct)) dirAnt = dirAct;

                            dirAct = posAbsBoard - posAct;

                            if (IsDirValid(dirAct) && boardManager.GetTileAtPosition(posAbsBoard) != null)
                            {
                                Vector2 posTileAnt = tileAnt.GetPosTile();
                                Vector2 posPipe = posAct;
                                posAct = posAbsBoard;

                                if (dirAct.x > 0) posPipe = new Vector2(posTileAnt.x + 0.5f, posTileAnt.y);
                                else if (dirAct.x < 0) posPipe = new Vector2(posTileAnt.x - 0.5f, posTileAnt.y);
                                else if (dirAct.y > 0) posPipe = new Vector2(posTileAnt.x, posTileAnt.y + 0.5f);
                                else if (dirAct.y < 0) posPipe = new Vector2(posTileAnt.x, posTileAnt.y - 0.5f);

                                PaintPipe(posPipe, dirAct, true);
                            }
                        }
                        //Si no esta vacio y es de un color diferente al actual rompemos la linea
                        else if (tileAct.GetColor() != pipeRenderer.color)
                        {
                            DestroyChildrenFromIndex(pipeParent[tileAct.GetColor()], tileAct.getIndex());
                        }
                    }

                    //Si es la primera posicion y nos echamos para atras
                    if (boardManager.GetTileAtPosition(tilePipesIni[pipeRenderer.color].GetPosTile()) == tileAct && pipeList[pipeRenderer.color].Count == 1)
                    {
                        DestroyChildrenFromIndex(pipeParent[pipeRenderer.color], pipeList[pipeRenderer.color].Count - 1);
                        posAct = posIni;
                    }
                    //Si te echas para atras en un pipe
                    else if (!tileAct.isFree() && pipeList[pipeRenderer.color].Count > 1 && tileAct == boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].GetPositionInBoard()))
                    {
                        posAct = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].GetPositionInBoard();

                        DestroyChildrenFromIndex(pipeParent[pipeRenderer.color], pipeList[pipeRenderer.color].Count - 1);
                    }
                    //Si hemos tocado el otro extremo
                    if (continueMoving && tileAct.IsCircle() && tileAct.GetCircleColor() == pipeRenderer.color
                        && boardManager.GetTileAtPosition(tilePipesIni[pipeRenderer.color].GetPosTile()) != tileAct)
                    {
                        continueMoving = false;
                        colorCompleted.Add(pipeRenderer.color);
                        Debug.Log("Pipe Completada");
                    }
                }
            }
            //Si soltamos
            else if (Input.GetMouseButtonUp(0))
            {
                draw = false;
            }

            if (Input.GetKeyUp(KeyCode.Space)) { PaintClue(); }
        }

        public void PaintClue()
        {
            if (colorCompleted.Count < pipeSolution.Count)
            {
                Debug.Log(colorCompleted.Count +" "+ pipeSolution.Count);
                
                List<List<Vector2>> aux = new List<List<Vector2>>(pipeSolution);
                bool pista = false;
                List<Vector2> l = new List<Vector2>();
                Color color=new Color();
                //Pista aleatoria de las pipes que faltan por resolver
                while (!pista)
                {
                    l = aux[Random.Range(0, aux.Count )];
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
                //Primer pipe
                Vector2 dir = l[1] - l[0];
                tileAct = boardManager.GetTileAtPosition(l[0]);
                PaintPipe(l[0], dir, false);
                //Resto de pipes
                for (int i = 1; i < l.Count; i++)
                {
                    tileAct = boardManager.GetTileAtPosition(l[i]);
                    pipeRenderer.color = color;
                    dir = l[i] - l[i - 1];
                    if (tileAct.GetColor() != pipeRenderer.color && !tileAct.isFree() && !tileAct.IsCircle())
                    {
                        DestroyChildrenFromIndex(pipeParent[tileAct.GetColor()], tileAct.getIndex());
                    }
                    PaintPipe(l[i], dir, false);
                }
                colorCompleted.Add(color);

                Debug.Log(colorCompleted.Count + " " + pipeSolution.Count);
            }
        }

        private void PaintPipe(Vector2 posPipe ,Vector2 dir, bool calculateAngle)
        {
            float angle = Mathf.Atan2(-dir.x, dir.y) * Mathf.Rad2Deg;
            if (calculateAngle)
            {
                anglePipe = GetAngleRotation();
                angle = anglePipe;
            }
            Quaternion rot = Quaternion.Euler(0f, 0f, angle);

            pipeList[pipeRenderer.color].Add(Instantiate<EachPipe>(pipe, new Vector2(posPipe.x, posPipe.y), rot, pipeParent[pipeRenderer.color]));

            pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].SetPositionInBoard(posAct);

            tileAct.setFree(false);
            tileAct.setIndex(pipeList[pipeRenderer.color].Count - 1);
            tileAct.SetColor(pipeRenderer.color);
        }

        private bool IsDirValid(Vector2 dir)
        {
            if (dir.x != 0 || dir.y != 0) return true;
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
            else if(dirAnt.x == 0 && dirAnt.y == 0)
            {
                if (dirAct.x == 1) return 90f;
                else if (dirAct.x == -1) return -90f;
            }
            return anglePipe;
        }
    }

}
