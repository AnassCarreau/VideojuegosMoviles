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

        [SerializeField] private Object scene;

        private PipeController pipeObject;

        private float scaleFactor = 1.0f;

        [SerializeField]
        RectTransform topHud;

        [SerializeField]
        RectTransform bottomHud;


        private void Start()
        {

            pipes = new List<List<Vector2>>();
            m = new Map();
            Initialize();
        }

        public void Initialize()
        {
            // Debug.Log("Initialize");
            Clear();
            setPipes();
            transform.localScale = Vector3.one;
            GenerateGrid();
            //pipeObject.enabled = true;
            if (pipeObject != null) Destroy(pipeObject.gameObject);
            pipeObject = Instantiate(pipeControllerPrefab, gameObject.transform);
            pipeObject.SetTotalPipesInBoard(m.GetWidth() * m.GetHeight() - m.GetFlownum());
            pipeObject.SetScaleFactor(scaleFactor);

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
            LvlActual lvl = GameManager.Instance.getActualPlay();
            // Debug.Log(lvl.levelIndex);
            m.Parse(GameManager.Instance.GetCategories()[lvl.category].lotes[lvl.slotIndex].levels[lvl.levelIndex]);
            GameManager.Instance.SetLevelText(lvl.levelIndex + 1, m.GetWidth(), m.GetHeight());
            pipes = m.GetPipes();
        }

        private Dictionary<Vector2, int> GenerateDictionary()
        {

            Dictionary<Vector2, int> wallsPos = new Dictionary<Vector2, int>();
            List<int> walls = m.Getwalls();
            int height = m.GetHeight();
            int width = m.GetWidth();
            for (int j = 0; j < walls.Count - 1; j += 2)
            {
                int valor = walls[j];
                int valor2 = walls[j + 1];
                int wall = 0;
                if (valor + 1 == valor2) { wall = 1; }
                else if (valor - 1 == valor2) { wall = 3; }
                else if (valor + height == valor2) { wall = 2; }

                int posX = valor % width;
                int posY = valor / height;
                if (width != height)
                {
                    if (width < height) posY = valor / width;
                }
                Vector2 posCasilla = new Vector2(posX, -posY);
                wallsPos.Add( posCasilla, wall);
            }
            return wallsPos;
        }

        public void GenerateGrid()
        {
            _tiles = new Dictionary<Vector2, Tile>();
            Dictionary<Vector2, int> walls = GenerateDictionary();
            for (int i = 0; i < m.GetFlownum(); i++)
            {
                for (int j = 0; j < pipes[i].Count; j++)
                {
                    Vector2 pos= new Vector2(pipes[i][j].x, pipes[i][j].y);
                    var spawnedTile = Instantiate(_tilePrefab,pos, Quaternion.identity, boardParent);
                    spawnedTile.name = $"Tile {pipes[i][j].x} {pipes[i][j].y}";
                    if (j == 0 || j == pipes[i].Count - 1)
                    {
                        spawnedTile.Init(false);
                        spawnedTile.SetCircleColor(pipesColor[i]);
                    }
                    else spawnedTile.Init(true);

                    spawnedTile.SetPosTile(pos);

                    bool[] w = new bool[4] { pos.y + 1 > 0, pos.x + 1 == m.GetWidth(), pos.y - 1 == -(m.GetHeight()), pos.x - 1 < 0 };
                    if (walls.ContainsKey(pos))
                    { 
                        w[walls[pos]] = true;
                        Debug.Log("diABLO");
                    }
                    spawnedTile.SetWalls(w);
                    _tiles[new Vector2(pipes[i][j].x, pipes[i][j].y)] = spawnedTile;
                }
            }


            float camHeight = Camera.main.orthographicSize * 2.0f;
            float camWidth = camHeight * Camera.main.aspect;

          //  Debug.Log("Altura :" + m.GetHeight());
          //  Debug.Log("Anchura :" + m.GetWidth());

            //Debug.Log("Tamanio topHud: " + topHud.rect.height);
            //Debug.Log("Tamanio bottomHud: " + bottomHud.rect.height);
            float tileSizeY = (camHeight - (topHud.rect.height + bottomHud.rect.height) / 100.0f )/ m.GetHeight();
            float tileSizeX = camWidth / m.GetWidth();

            if (tileSizeY > tileSizeX)
            {
                scaleFactor = tileSizeX;
            }
            else
            {
                scaleFactor = tileSizeY;
            }

            transform.localScale = new Vector3(scaleFactor, scaleFactor, scaleFactor);
            _cam.transform.position = new Vector3((((float)m.GetWidth() / 2) - 0.5f) * scaleFactor, -((float)m.GetHeight() / 2) * scaleFactor, -10);

        }

        /*
        public bool[] GetWalls(Vector2 pos, KeyValuePair<Vector2, int> nextWall)
        {
            bool[]w = new bool[4] { pos.y + 1 > 0, pos.x + 1 == m.GetWidth(), pos.y - 1 == -(m.GetHeight()), pos.x - 1 < 0 };
            if ( pos == nextWall.Key) { w[nextWall.Value] = true; }
            return w;
        }*/
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

        public Map GetMap() 
        {
            return m;
        }
        public float getScaleFactor()
        {
            return scaleFactor;
        }
    }
}
