using UnityEngine;

namespace FlowFreeGame
{
    public class Tile : MonoBehaviour
    {
        [SerializeField]
        private Color _color;

        [SerializeField]
        private SpriteRenderer _renderer;

        [SerializeField]
        private GameObject circleObject;

        [SerializeField]
        private SpriteRenderer circleRenderer;

        [SerializeField]
        private GameObject wallObject;

        [SerializeField]
        private SpriteRenderer wallRenderer;

        private Vector2 posTile;
        private int index;
        bool[] walls;

        public void Init(bool emptyTile)
        {
            LvlActual lvl = GameManager.Instance.GetLvlActual();
            _renderer.color = GameManager.Instance.GetCategories()[lvl.category].categoryColor;
            index = -1;
            if (!emptyTile)
            {
                circleObject.SetActive(true);
            }
        }


        public Color GetColor()
        {
            return _color;
        }

        public void SetColor(Color c)
        {
            _color = c;
            circleRenderer.color = c;
        }

        public Vector2 GetPosTile()
        {
            return posTile;
        }

        public void SetPosTile(Vector2 pos)
        {
            posTile = pos;
        }

        public void SetWalls(bool[] w)
        {
            walls = w;
            wallRenderer.color = _renderer.color;
            
            for (int i = 0; i < w.Length; i++)
            {
                if (!w[i]) continue;

                GameObject o = Instantiate(wallObject, transform);
                float angle = 90 * (i + 1);
                Vector3 v = new Vector2(-transform.localScale.x / 2 * Mathf.Cos(angle * Mathf.Deg2Rad), -transform.localScale.y / 2 * Mathf.Sin(angle * Mathf.Deg2Rad));
                o.transform.rotation = Quaternion.Euler(0, 0, angle);
                o.transform.position = transform.position + v;
                o.name = $"Muro {posTile.x} {posTile.y}";
            }

        }
        public bool IsCircle()
        {
            return circleObject.activeSelf;
        }
        public bool IsFree() { return index == -1; }


        public void SetIndex(int i)
        {
            this.index = i;
        }
        public int GetIndex()
        {
            return index;
        }

        public bool[] GetWalls() { return walls; }
    }
}
