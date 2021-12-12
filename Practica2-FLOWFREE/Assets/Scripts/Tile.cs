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

        [SerializeField]
        private GameObject wallObject;
        private Vector2 posTile;

        private bool free;

        private int index;
        bool[] walls;
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

        public void SetWalls(bool[]w) 
        {
            walls = w;
            for (int i = 0; i < w.Length; i++)
            {
                if (w[i])
                {
                   
                    GameObject o = Instantiate(wallObject, transform);
                    wallObject.GetComponent<SpriteRenderer>().color = _renderer.color;
                    float x= 0;
                    float y= 0;
                    if (i == 2)
                    {
                        y = -transform.localScale.y / 2;

                    }
                    if (i == 3)
                    {
                        x = -transform.localScale.x / 2;
                    }
                    if (i == 1) 
                    {
                        x = transform.localScale.x / 2;
                    }if (i == 0) 
                    {
                        y = transform.localScale.y / 2;
                    }

                    Vector3 vectorFeo=new  Vector2(x ,  y );
                    o.transform.rotation = Quaternion.Euler(0, 0, (i + 1) * 90);
                    o.transform.position = transform.position + vectorFeo ;
                    o.name = $"Muro {posTile.x} {posTile.y} + {i}";
                }
            }
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
