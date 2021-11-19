using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class BoardManager : MonoBehaviour
    {
        private bool boardComplete;

        [SerializeField] 
        private int _width, _height, numPipes;

        [SerializeField] 
        private Tile _tilePrefab;

        [SerializeField] 
        private Transform _cam;

        [SerializeField]
        private Color[] pipesColor;

        [SerializeField]
        private GameObject circleGameObject;

        [SerializeField]
        private SpriteRenderer circleSpriteRenderer;

        int[][] pipes;

        private Dictionary<Vector2, Tile> _tiles;

        private void Start()
        {
            pipes = new int[numPipes][];
           
            setPipes();
            GenerateGrid();
        }

        //este va a ser el �nico Update del juego�?
        private void Update()
        {
            
        }

        private void setPipes()
        {
            pipes[0] = new int[6] { 0, 5, 10, 15, 20, 21 };
            pipes[1] = new int[5] { 2, 1, 6, 11, 16 };
            pipes[2] = new int[4] { 7, 12, 17, 22 };
            pipes[3] = new int[5] { 4, 3, 8, 13, 18 };
            pipes[4] = new int[5] { 9, 14, 19, 24, 23 };
        }

        void GenerateGrid()
        {
            _tiles = new Dictionary<Vector2, Tile>();
            for(int i = 0; i < numPipes; i++)
            {
                for (int j = 0; j < pipes[i].Length; j++)
                {
                    int posX = pipes[i][j] % _width;
                    int posY = pipes[i][j] / _height;
                    if(_width != _height)
                    {
                        if (_width < _height) posY = pipes[i][j] / _width;
                    }
                    
                    var spawnedTile = Instantiate(_tilePrefab, new Vector3(posX, -posY), Quaternion.identity);
                    spawnedTile.name = $"Tile {posX} {posY}";
                    if(j == 0 || j == pipes[i].Length - 1)
                    {
                        circleSpriteRenderer.color = pipesColor[i];
                        Instantiate(circleGameObject, new Vector3(posX, -posY), Quaternion.identity);
                    }
                    spawnedTile.Init();

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
