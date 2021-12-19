using System.Collections.Generic;
using UnityEngine;

namespace FlowFreeGame
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
        private Transform boardParent;

        List<List<Vector2>> pipes;

        private Dictionary<Vector2, Tile> _tiles;

        private Map m;

        private PipeController pipeObject;

        private float scaleFactor = 1.0f;

        [SerializeField]
        RectTransform topHud;

        [SerializeField]
        RectTransform bottomHud;

        [SerializeField]
        private Animation boardAnimation;

        private static BoardManager _instance; 

        public static BoardManager Instance { get { return _instance; } }

        private void Awake()
        {
            if (_instance != null)
            {
                Destroy(this.gameObject);
            }
            else
            {
                _instance = this;
            }
        }

        private void Start()
        {
            pipes = new List<List<Vector2>>();
            m = new Map();
            Initialize();
        }

        public void PlayAnimation()
        {
            Debug.Log("play");
            Initialize();
            // create a curve to move the GameObject and assign to the clip
            Keyframe[] keys;
            keys = new Keyframe[3];
            keys[0] = new Keyframe(0.0f, transform.localScale.x);
            // within 12 seconds rotate to 120°
            keys[1] = new Keyframe(0.5f, 0.0f);
            // Whatever you need as 3. keyframe
            keys[2] = new Keyframe(1.0f, transform.localScale.x);

            var curve = new AnimationCurve(keys);
            boardAnimation.clip.SetCurve("", typeof(Transform), "localScale.x", curve);

            curve = new AnimationCurve(new Keyframe(0.0f, transform.localScale.y));
            boardAnimation.clip.SetCurve("", typeof(Transform), "localScale.y", curve);

            boardAnimation.Play();
        }
        public bool IsPlayingAnimation() 
        {
            return boardAnimation.isPlaying;
        }
        public void Initialize()
        {
            Clear();
            SetPipes();
            LvlActual lvl = GameManager.Instance.GetLvlActual();
            LevelManager.Instance.SetLevelText(lvl.levelIndex + 1, m.GetWidth(), m.GetHeight());
            GenerateGrid();
            SetBoardScale();
           
            if (pipeObject != null) Destroy(pipeObject.gameObject);
            
            pipeObject = Instantiate(pipeControllerPrefab, gameObject.transform);
            pipeObject.SetTotalPipesInBoard(m.GetWidth() * m.GetHeight() - m.GetNumPipes());
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

        private void SetPipes()
        {
            m.Parse(GameManager.Instance.GetCurrentLevel());
            pipes = m.GetPipes();
        }

        private void GenerateGrid()
        {
            transform.localScale = Vector3.one;
            _tiles = new Dictionary<Vector2, Tile>();
            Dictionary<Vector2, bool[]> walls = m.GetWallsInBoard();
            Color[] colorTheme = GameManager.Instance.GetColorTheme().colorTheme;

            for (int i = 0; i < m.GetNumPipes(); i++)
            {
                for (int j = 0; j < pipes[i].Count; j++)
                {
                    Vector2 pos= new Vector2(pipes[i][j].x, pipes[i][j].y);
                    var spawnedTile = Instantiate(_tilePrefab,pos, Quaternion.identity, boardParent);
                    spawnedTile.name = $"Tile {pipes[i][j].x} {pipes[i][j].y}";
                    
                    //Si eres la primera o la ultima posicion de las tuberias, significa que
                    //eres un circulo
                    if (j == 0 || j == pipes[i].Count - 1)
                    {
                        spawnedTile.Init(false);
                        spawnedTile.SetColor(colorTheme[i]);
                    }
                    else spawnedTile.Init(true);

                    spawnedTile.SetPosTile(pos);
                    //Ponemos paredes en los bordes del tablero
                    bool[] w = new bool[4] { pos.y - 1 == -(m.GetHeight()), pos.x + 1 == m.GetWidth(), pos.y + 1 > 0 , pos.x - 1 < 0 };
                    
                    if (walls.ContainsKey(pos))
                    {
                        for(int k = 0; k < w.Length; k++)
                        {
                            if (walls[pos][k]) w[k] = true;
                        }
                    }
                    spawnedTile.SetWalls(w);
                    _tiles[new Vector2(pipes[i][j].x, pipes[i][j].y)] = spawnedTile;
                }
            }

        }

        private void SetBoardScale()
        {
            float camHeight = Camera.main.orthographicSize * 2.0f;
            float camWidth = camHeight * Camera.main.aspect;

            float topFactor = topHud.rect.width / topHud.rect.height;
            float bottomFactor = bottomHud.rect.width / bottomHud.rect.height;

            float topHeight = camWidth / topFactor;
            float bottomHeight = camWidth / bottomFactor;

            float tileSizeY = (camHeight - (topHeight + bottomHeight)) / m.GetHeight();
            float tileSizeX = camWidth / m.GetWidth();

            if (tileSizeY > tileSizeX)
                scaleFactor = tileSizeX;
            else
                scaleFactor = tileSizeY;

            transform.localScale = new Vector3(scaleFactor, scaleFactor, scaleFactor);
            
            _cam.transform.position = new Vector3((((float)m.GetWidth() / 2) - 0.5f) * scaleFactor, -((float)m.GetHeight() / 2) * scaleFactor, -10);
        }

        public Tile GetTileAtPosition(Vector2 pos)
        {
            if (_tiles.TryGetValue(pos, out var tile)) return tile;
            return null;
        }

        public List<List<Vector2>> GetPipeSolution() { return pipes; }

        public PipeController GetPipeController()
        {
            return pipeObject;
        }

        public void EnablePipeController()
        {
            pipeObject.enabled = true;
        }



       



        public void FinishAnimation()
        {
            // create a curve to move the GameObject and assign to the clip
            Keyframe[] keys;
            keys = new Keyframe[3];
            keys[0] = new Keyframe(0.0f, 0.0f);
            // within 12 seconds rotate to 120°
            keys[1] = new Keyframe(12.0f, 120f);
            // Whatever you need as 3. keyframe
            keys[2] = new Keyframe(16.0f, 0f);

            var curve = new AnimationCurve(keys);


            boardAnimation.clip.SetCurve("Body/RightShoulder/RightArm", typeof(Transform), "position.x", curve);

        }
    }
}
