using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class PipeController : MonoBehaviour
    {
        [SerializeField]
        private GameObject pipeObject;

        private Dictionary<Color, Transform> pipeParent;
        private Dictionary<Color, List<GameObject>>  pipeList;

        [SerializeField]
        SpriteRenderer pipeRenderer;

        private BoardManager boardManager;

        bool draw = false;

        Vector2 posIni;
        Vector2 posAct;
        Tile tileIni;
        Tile tileAct;

        void Start()
        {
            boardManager = GameManager.Instance.GetBoardManager();
            pipeParent = new Dictionary<Color, Transform>();
            pipeList = new Dictionary<Color, List<GameObject>>();

            Color[] c = boardManager.getPipesColor();
            for (int i = 0; i < c.Length;i++) 
            {
                GameObject par = new GameObject();
                par.name = c[i].ToString();
                pipeParent.Add(c[i], par.transform);
                pipeList.Add(c[i], new List<GameObject>());
            }

            
        }

        private void DestroyChildren(Transform tr) 
        {
            foreach (Transform child in tr)
            {
                Destroy(child.gameObject);
                boardManager.GetTileAtPosition(child.position).setFree(true);
            }
        }

        private void DestroyChildrenFromIndex(Transform tr,int index)
        {
            for (int i = index; i< tr.childCount;i++)
            {
                Destroy(tr.GetChild(i).gameObject);
                boardManager.GetTileAtPosition(tr.GetChild(i).position).setFree(true);
            }
        }

        void Update()
        {
            Vector2 posInBoard = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            RaycastHit2D ra = Physics2D.Raycast(posInBoard, -Vector2.up);

            if (ra.collider != null && Input.GetMouseButtonDown(0))
            {
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileIni = boardManager.GetTileAtPosition(posAbsBoard);
                if (tileIni != null)
                {
                    if (tileIni.IsCircle())
                    {
                        draw = true;
                        posIni = posAbsBoard;
                        posAct = posAbsBoard;
                        pipeRenderer.color = tileIni.GetCircleColor();
                        DestroyChildren(pipeParent[pipeRenderer.color]);
                    }
                    else
                    {
                        //draw = true;
                        //pipeRenderer.color = tileIni.GetCircleColor();
                       // DestroyChildrenFromIndex(pipeParent[pipeRenderer.color], pipeList[pipeRenderer.color]);
                    }
                }

            }
            else if (draw && Input.GetMouseButton(0))
            {
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileAct = boardManager.GetTileAtPosition(posAbsBoard);
                if (ra.collider != null && tileAct != null) {
                    if (!tileAct.IsCircle() && tileAct.isFree() && tileAct != tileIni)
                    {

                        Vector2 dir = posAbsBoard - posAct;
                        if (dir.x != 0 && dir.y != 0) { Debug.Log("No me puedo mover en diagonal pai"); }
                        else
                        {
                            if (boardManager.GetTileAtPosition(posAbsBoard) != null)
                            {
                                float angle = Mathf.Atan2(-dir.x, dir.y) * Mathf.Rad2Deg;
                                Quaternion rot = Quaternion.Euler(0f, 0f, angle);
                                posAct = posAbsBoard;
                                pipeList[pipeRenderer.color].Add(Instantiate(pipeObject, new Vector3(posAct.x, posAct.y), rot, pipeParent[pipeRenderer.color]));
                                tileAct.setFree(false);
                            }
                        }
                    }


                    if (tileAct.IsCircle() && pipeList[pipeRenderer.color].Count == 1)
                    {
                        boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].transform.position).setFree(true);
                        GameObject des = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1];
                        pipeList[pipeRenderer.color].Remove(des);
                        posAct = posIni;
                        Destroy(des);
                    }
                    else if (!tileAct.isFree() && pipeList[pipeRenderer.color].Count > 1 && tileAct == boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].transform.position)) 
                    {
                        posAct = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 2].transform.position;
                        boardManager.GetTileAtPosition(pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1].transform.position).setFree(true);
                        GameObject des = pipeList[pipeRenderer.color][pipeList[pipeRenderer.color].Count - 1];
                        pipeList[pipeRenderer.color].Remove(des);
                        Destroy(des);
                    }


                    if (tileAct.IsCircle() && tileAct.GetCircleColor() == pipeRenderer.color && tileAct != tileIni)
                    {
                        draw = false;
                        Debug.Log("Pipe Completada");
                    }
                }
            }
            else if (Input.GetMouseButtonUp(0))
            {
                draw = false;
            }
        }
    }
}
