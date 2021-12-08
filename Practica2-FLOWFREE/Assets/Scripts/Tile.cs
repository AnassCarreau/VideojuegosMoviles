using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
namespace FreeFlowGame
{
    public class Tile : MonoBehaviour
    {
        private Color _color;

        [SerializeField]
        private SpriteRenderer _renderer;

        [SerializeField]
        private GameObject circleObject;

        [SerializeField]
        private SpriteRenderer circleRenderer;

        private Vector2 posTile;

        private bool free;

        private int index;

        public void Init(bool emptyTile)
        {
            LvlActual lvl = GameManager.Instance.getActualPlay();
            _renderer.color = GameManager.Instance.GetCategories()[lvl.category].categoryColor;

            free = true;
            index = -1;
            if (!emptyTile)
            {
                free = false;
                circleObject.SetActive(true);
            }
        }

        public void SetCircleColor(Color c)
        {
            circleRenderer.color = c;
        }

        public Color GetCircleColor()
        {
            return circleRenderer.color;
        }

        public Color GetColor()
        {
            return _color;
        }

        public void SetColor(Color c)
        {
            _color=c;
        }

        public Vector2 GetPosTile()
        {
            return posTile;
        }

        public void SetPosTile(Vector2 pos)
        {
            posTile = pos;
        }

        public bool IsCircle()
        {
            return circleObject.activeSelf;
        }
        public bool isFree() { return free; }

        public void setFree(bool active) 
        {
            free = active;
        }  
        public void setIndex(int i)
        {
            this.index = i;
        }
        public int getIndex()
        {
            return index ;
        }
    }
}
