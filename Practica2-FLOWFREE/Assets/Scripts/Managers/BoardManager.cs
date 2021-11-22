using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class BoardManager : MonoBehaviour
    {
        private bool boardComplete;

        [SerializeField] 
        private Tile _tilePrefab;

        [SerializeField] 
        private Transform _cam;

        [SerializeField]
        private Color[] pipesColor;

        [SerializeField]
        private Transform boardParent;

        List<List<int>> pipes;

        private Dictionary<Vector2, Tile> _tiles;

        private Map m;

        private void Start()
        {
            pipes = new List<List<int>>();
            m = new Map();

            setPipes();
            GenerateGrid();
        }

        //este va a ser el único Update del juego¿?
        private void Update()
        {
            
        }

        private void setPipes()
        {
            m.Parse(LectutaLote.Instance.getDictionaryCategories()["Intro"][0].levels[1]);
            pipes = m.GetPipes();
        }

        void GenerateGrid()
        {
            int _width = m.GetWidth();
            int _height = m.GetHeight();
            int flownum = m.GetFlownum();
            _tiles = new Dictionary<Vector2, Tile>();
            for(int i = 0; i < flownum; i++)
            {
                for (int j = 0; j < pipes[i].Count; j++)
                {
                    int posX = pipes[i][j] % _width;
                    int posY = pipes[i][j] / _height;

                    if(_width != _height)
                    {
                        if (_width < _height) posY = pipes[i][j] / _width;
                    }
                    
                    var spawnedTile = Instantiate(_tilePrefab, new Vector3(posX, -posY), Quaternion.identity, boardParent);
                    spawnedTile.name = $"Tile {posX} {-posY}";
                    if(j == 0 || j == pipes[i].Count - 1)
                    {
                        spawnedTile.Init(false);
                        spawnedTile.SetCircleColor(pipesColor[i]);
                    }
                    else spawnedTile.Init(true);

                    spawnedTile.SetPosTile(new Vector2(posX, -posY));

                    _tiles[new Vector2(posX, -posY)] = spawnedTile;
                }
            }

            _cam.transform.position = new Vector3((float)_width / 2 - 0.5f, (float)_height / 2 - 5f, -10);
        }

        public Tile GetTileAtPosition(Vector2 pos)
        {
            if (_tiles.TryGetValue(pos, out var tile)) return tile;
            return null;
        }
    }
}
