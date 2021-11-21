using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame {
    public class PipeController : MonoBehaviour
    {
        [SerializeField]
        private GameObject pipeObject;

        [SerializeField]
        private Transform pipeParent;

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
        }


        void Update()
        {
            Vector2 posInBoard = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            RaycastHit2D ra = Physics2D.Raycast(posInBoard, -Vector2.up);

            if (ra.collider != null && Input.GetMouseButtonDown(0))
            {
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileIni = boardManager.GetTileAtPosition(posAbsBoard);
                if (tileIni != null && !tileIni.IsEmpty())
                {
                    draw = true;
                    posIni = posAbsBoard;
                    pipeRenderer.color = tileIni.GetCircleColor();
                }
            }
            if (draw && Input.GetMouseButton(0))
            {
                posInBoard = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                ra = Physics2D.Raycast(posInBoard, -Vector2.up);
                Vector2 posAbsBoard = new Vector2(Mathf.RoundToInt(posInBoard.x), Mathf.RoundToInt(posInBoard.y));
                tileAct = boardManager.GetTileAtPosition(posAbsBoard);

                if (ra.collider != null && tileAct != null && tileAct != tileIni && tileAct.IsEmpty())
                {
                    posAct = posAbsBoard;
                    Vector2 dir = posAct - posIni;
                    
                    if(dir.x != 0 && dir.y != 0) { Debug.Log("No me puedo mover en diagonal pai"); }
                    else
                    {
                        Vector2 move = posAct + dir;
                        Tile tileAux = boardManager.GetTileAtPosition(move);
                        if (tileAux != null)
                        {
                            if (dir.x == 0 && dir.y == 1) pipeObject.transform.Rotate(0f, 0f, 180f);
                            else if(dir.x == 0 && dir.y == -1) pipeObject.transform.Rotate(0f, 0f, 0f);
                            else if(dir.x == 1 && dir.y == 0) pipeObject.transform.Rotate(0f, 0f, -90f);
                            else if(dir.x == -1 && dir.y == 0) pipeObject.transform.Rotate(0f, 0f, 90f);

                            Instantiate(pipeObject, new Vector3(posAct.x, posAct.y), Quaternion.identity, pipeParent);
                        }
                    }
                }
                if (ra.collider != null && tileAct != null && tileAct != tileIni && !tileAct.IsEmpty() && tileAct.GetCircleColor() == pipeRenderer.color)
                {
                    draw = false;
                    Debug.Log("Pipe Completada");
                }
            }
            if (Input.GetMouseButtonUp(0))
            {
                draw = false;
            }
        }
    }
}
