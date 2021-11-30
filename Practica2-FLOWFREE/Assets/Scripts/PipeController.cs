using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace FreeFlowGame
{
    public class PipeController : MonoBehaviour
    {
        [SerializeField]
        private GameObject pipeObject;
        //Diccionario para tener un padre donde instanciar los pipes de un color especifico
        private Dictionary<Color, Transform> pipeParent;
        //Diccionario para tener para cada color su principio fin y tamaño
        private Dictionary<Color, Circle> pipeCircles;
        //Set con los colores resueltos
        private HashSet<Color> pipeCompleted;
        //Diccionario con los pipes de cada color
        private Dictionary<Color, List<GameObject>> pipeList;

        [SerializeField]
        SpriteRenderer pipeRenderer;

        private BoardManager boardManager;

        bool draw = false;

        Vector2 posIni;
        Vector2 posAct;
        Tile tileIni;
        Tile tileAct;
        List<List<Vector2>> pipeSolution;

        public struct Circle
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
            pipeList = new Dictionary<Color, List<GameObject>>();
            pipeCircles = new Dictionary<Color, Circle>();
            pipeSolution = boardManager.getPipeSolution();
            pipeCompleted = new HashSet<Color>();
            Color[] c = boardManager.getPipesColor();
            for (int i = 0; i < c.Length; i++)
            {
                GameObject par = new GameObject();
                par.name = c[i].ToString();
                pipeParent.Add(c[i], par.transform);
                pipeList.Add(c[i], new List<GameObject>());
                Circle cir =new Circle();
                cir.ini = pipeSolution[i][0];
                cir.fin = pipeSolution[i][pipeSolution[i].Count - 1];
                //El tamaño es menos dos porque falta crear un pipe en el primer circulo
                cir.tam = pipeSolution[i].Count - 2;
                pipeCircles.Add(c[i], cir);
            }


        }
        
        /// <summary>
        /// Destruye todos los hijos de un color
        /// </summary>
        /// <param name="tr"> transform del padre </param>
        private void DestroyChildren(Transform tr)
        {
            foreach (Transform child in tr)
            {
                pipeList[pipeRenderer.color].Remove(child.gameObject);
                boardManager.GetTileAtPosition(child.position).setFree(true);
                boardManager.GetTileAtPosition(child.position).setIndex(-1);
                Destroy(child.gameObject);
            }
            pipeCompleted.Remove(pipeRenderer.color);

        }
        /// <summary>
        /// Destruye todos los hijos de un color desde un indice especifico
        /// </summary>
        /// <param name="tr">transform del padre</param>
        /// <param name="index">indice desde el cual se remueven todos los siguientes hijos</param>
        private void DestroyChildrenFromIndex(Transform tr, int index)
        {
            Color c = tileAct.IsCircle() ? tileAct.GetCircleColor() : tileAct.GetColor();
            for (int i = index; i < tr.childCount; i++)
            {
                pipeList[c].Remove(tr.GetChild(i).gameObject);
                boardManager.GetTileAtPosition(tr.GetChild(i).position).setFree(true);
                boardManager.GetTileAtPosition(tr.GetChild(i).position).setIndex(-1);
                Destroy(tr.GetChild(i).gameObject);

            }
            pipeCompleted.Remove(tileAct.GetColor());
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
                if (tileIni != null)
                {
                    //Si es un circulo destrimos todos los pipes de su calor
                    if (tileIni.IsCircle())
                    {
                        draw = true;
                        posIni = posAbsBoard;
                        posAct = posAbsBoard;
                        pipeRenderer.color = tileIni.GetCircleColor();
                        DestroyChildren(pipeParent[pipeRenderer.color]);
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
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileAct = boardManager.GetTileAtPosition(posAbsBoard);
                if (ra.collider != null && tileAct != null)
                {

                    if (!tileAct.IsCircle())
                    {
                        //Si esta vacio pintamos 
                        if (tileAct.isFree())
                        {
                            Vector2 dir = posAbsBoard - posAct;
                            if (dir.x != 0 && dir.y != 0) { Debug.Log("No me puedo mover en diagonal pai"); }
                            else
                            {
                                if (boardManager.GetTileAtPosition(posAbsBoard) != null)
                                {
                                    posAct = posAbsBoard;
                                    PaintPipe(posAct, dir);
                                }
                            }
                        }
                        //Si no esta vacio y es de un color diferente al actual rompemos la linea
                        else if (tileAct.GetColor() != pipeRenderer.color)
                        {
                            DestroyChildrenFromIndex(pipeParent[tileAct.GetColor()], tileAct.getIndex());
                        }
                    }

                    bool destroy = false;
                    //Si es la primera posicion y nos echamos para atras
                    if (tileAct.IsCircle() && (tileAct == boardManager.GetTileAtPosition(pipeCircles[pipeRenderer.color].fin) || tileAct == boardManager.GetTileAtPosition(pipeCircles[pipeRenderer.color].ini)) && pipeList[pipeRenderer.color].Count == 1)
                    {
                        destroy = true;
                        posAct = posIni;
                    }
                    //Si te echas para atras en un pipe
                    else if (!tileAct.isFree() && pipeList[pipeRenderer.color].Count > 1 && tileAct == boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].transform.position))
                    {
                        destroy = true;
                        posAct = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].transform.position;
                    }
                    //Destruimos el ultimo hijo
                    if (destroy)
                    {
                        GameObject des = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1];
                        boardManager.GetTileAtPosition(des.transform.position).setFree(true);
                        boardManager.GetTileAtPosition(des.transform.position).setIndex(-1);
                        pipeList[pipeRenderer.color].Remove(des);
                        pipeCompleted.Remove(pipeRenderer.color);
                        Destroy(des);
                    }
                    //Si hemos tocado el otro extremo
                    if (tileAct.IsCircle() && tileAct.GetCircleColor() == pipeRenderer.color 
                &&  pipeParent[pipeRenderer.color].childCount>1 && (boardManager.GetTileAtPosition(pipeCircles[pipeRenderer.color].fin)==tileAct || boardManager.GetTileAtPosition(pipeCircles[pipeRenderer.color].ini) == tileAct))
                    {
                        draw = false;
                        pipeCompleted.Add(pipeRenderer.color);
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
            if (pipeCompleted.Count < pipeSolution.Count)
            {
                Debug.Log(pipeCompleted.Count +" "+ pipeSolution.Count);
                
                List<List<Vector2>> aux = new List<List<Vector2>>(pipeSolution);
                bool pista = false;
                List<Vector2> l = new List<Vector2>();
                Color color=new Color();
                //Pista aleatoria de las pipes que faltan por resolver
                while (!pista)
                {
                    l = aux[Random.Range(0, aux.Count )];
                    color = boardManager.GetTileAtPosition(l[0]).GetCircleColor();
                    if (!pipeCompleted.Contains(color))
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
                DestroyChildren(pipeParent[color]);
                //Primer pipe
                Vector2 dir = l[1] - l[0];
                tileAct = boardManager.GetTileAtPosition(l[0]);
                PaintPipe(l[0], dir);
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
                    PaintPipe(l[i], dir);
                }
                pipeCompleted.Add(color);

                Debug.Log(pipeCompleted.Count + " " + pipeSolution.Count);

            }
        }

        public void PaintPipe(Vector2 pos,Vector2 dir )
        {
            float angle = Mathf.Atan2(-dir.x, dir.y) * Mathf.Rad2Deg;
            Quaternion rot = Quaternion.Euler(0f, 0f, angle);
            pipeList[pipeRenderer.color].Add(Instantiate(pipeObject, new Vector2(pos.x, pos.y), rot, pipeParent[pipeRenderer.color]));
            tileAct.setFree(false);
            tileAct.setIndex(pipeList[pipeRenderer.color].Count - 1);
            tileAct.SetColor(pipeRenderer.color);
            Debug.Log(pipeList[pipeRenderer.color].Count - 1);
        }
    }

}
