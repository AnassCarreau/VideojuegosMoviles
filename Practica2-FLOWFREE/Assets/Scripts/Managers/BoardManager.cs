using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class BoardManager : MonoBehaviour
    {

        [SerializeField] 
        private Tile _tilePrefab;

        [SerializeField]
        private PipeController pipeControllerPrefab;

        [SerializeField] 
        private Transform _cam;

        [SerializeField]
        private Color[] pipesColor;

        [SerializeField]
        private Transform boardParent;

        List<List<Vector2>> pipes;

        private Dictionary<Vector2, Tile> _tiles;

        private Map m;

        [SerializeField]private Object scene;

        private PipeController pipeObject;

        private float scaleFactor = 1.0f;

        private void Start()
        {
            pipes = new List<List<Vector2>>();
            m = new Map();
            Initialize();
        }

        public void Initialize()
        {
            Debug.Log("Initialize");
            Clear();
            setPipes();
            GenerateGrid();
            pipeObject = Instantiate(pipeControllerPrefab, gameObject.transform);
            pipeObject.SetTotalPipesInBoard(m.GetWidth() * m.GetHeight() - m.GetFlownum() );
        }

        private void Clear() 
        {
            foreach (Transform child in boardParent)
            {
                Destroy(child.gameObject);
            }
            if (pipeObject != null) 
            {
                Destroy(pipeObject);
            }
        }
        private void setPipes()
        {
            LvlActual lvl= GameManager.Instance.getActualPlay();
            Debug.Log(lvl.levelIndex);
            m.Parse(GameManager.Instance.GetCategories()[lvl.category].lotes[lvl.slotIndex].levels[lvl.levelIndex]);
            pipes = m.GetPipes();
        }

        public void GenerateGrid()
        {
            _tiles = new Dictionary<Vector2, Tile>();
            
            for (int i = 0; i < m.GetFlownum(); i++)
            {
                for (int j = 0; j < pipes[i].Count; j++)
                {   
                    var spawnedTile = Instantiate(_tilePrefab, new Vector3(pipes[i][j].x, pipes[i][j].y), Quaternion.identity, boardParent);
                    spawnedTile.name = $"Tile {pipes[i][j].x} {pipes[i][j].y}";
                    if(j == 0 || j == pipes[i].Count - 1)
                    {
                        spawnedTile.Init(false);
                        spawnedTile.SetCircleColor(pipesColor[i]);
                    }
                    else spawnedTile.Init(true);

                    spawnedTile.SetPosTile(new Vector2(pipes[i][j].x, pipes[i][j].y));

                    _tiles[new Vector2(pipes[i][j].x, pipes[i][j].y)] = spawnedTile;
                }
            }


            float camHeight = Camera.main.orthographicSize * 2.0f;
            float camWidth = camHeight * Camera.main.aspect;

            float tileSizeY = camHeight / m.GetHeight();
            float tileSizeX = camWidth / m.GetWidth();

            if(tileSizeY > tileSizeX)
            {
                scaleFactor = tileSizeX;
            }
            else
            {
                scaleFactor = tileSizeY;
                //TO DO: Landscape si eso xDDD
                //Restarle en la y lo que tenemos de canvas
            }

            transform.localScale = new Vector3(scaleFactor, scaleFactor, scaleFactor);
            _cam.transform.position = new Vector3((((float)m.GetWidth() / 2) - 0.5f)* scaleFactor, -((float)m.GetHeight() / 2)* scaleFactor, -10);

        }

       

        public Tile GetTileAtPosition(Vector2 pos)
        {
            if (_tiles.TryGetValue(pos, out var tile)) return tile;
            return null;
        }

        public Object getScene() 
        {
            return scene;
        } 
        public Color[] getPipesColor() 
        {
            return pipesColor;
        }
        public List<List<Vector2>> getPipeSolution() { return pipes; }

        public PipeController GetPipeController()
        {
            return pipeObject;
        }

        public float getScaleFactor()
        {
            return scaleFactor;
        }
    }
}
