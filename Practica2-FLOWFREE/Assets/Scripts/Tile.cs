using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class Tile : MonoBehaviour
    {
        private Color tileColor;
        private Touch theTouch;

        void Start()
        {

        }

        void Update()
        {
            if(Input.touchCount > 0)
            {
                theTouch = Input.GetTouch(0);
                if(theTouch.phase == TouchPhase.Moved)
                {
                    Debug.Log("toque arrastrando");
                }
            }
        }

        void setColor(Color color)
        {
            tileColor = color;
        }
    }
}
