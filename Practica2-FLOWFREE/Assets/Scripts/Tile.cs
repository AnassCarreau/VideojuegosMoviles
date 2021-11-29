using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
namespace FreeFlowGame
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

        private Vector2 posTile;

        private bool free;


        public void Init(bool emptyTile)
        {
            _renderer.color = _color;
            free = true;
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

        public Vector2 GetPosTile()
        {
            return posTile;
        }

        public void SetPosTile(Vector2 pos)
        {
            posTile = pos;
        }

        public bool IsEmpty()
        {
            return !circleObject.activeSelf && free;
        }

        public void setFree(bool active) 
        {
            free = active;
        }
    }
}
