using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class Tile : MonoBehaviour
    {
        private Color tileColor;
        private Touch theTouch;
        private bool empty;
        private Vector3 objectPosPixels;
        private Vector3 offset;
        private bool pulsado;
        
        void Start()
        {
            pulsado = false;
        }

        void Update()
        {
            //if(Input.touchCount > 0)
            //{
            //    theTouch = Input.GetTouch(0);
            //    if(theTouch.phase == TouchPhase.Moved)
            //    {
            //        Debug.Log("toque arrastrando");
            //    }
            //}
            if (Input.GetMouseButtonDown(0))
            {
                MouseDown();
            }
            if (Input.GetMouseButtonUp(0))
            {
                pulsado = false;
            }
            if (pulsado)
            {
                //MouseDrag();
            }
        }

        private void MouseDrag()
        {
            //Debug.Log("drag");
            //Vector3 cursorScreenPoint = new Vector3(Input.mousePosition.x, Input.mousePosition.y, screenPoint.z);
            //Vector3 cursorPosition = Camera.main.ScreenToWorldPoint(cursorScreenPoint) + offset;
            //transform.position = cursorPosition;
        }

        private void MouseDown()
        {
            
            Debug.Log("down");
            objectPosPixels = Camera.main.WorldToScreenPoint(gameObject.transform.position);

            float offsetW = ((float)Screen.width/ (float)Camera.main.pixelWidth) * 50f;
            float offsetH = ((float)Screen.height / (float)Camera.main.pixelHeight) * 50f;

            Debug.Log("OffsetW: " + offsetW);
            Debug.Log("OffsetH: " + offsetH);
            Debug.Log("Objeto en: " + objectPosPixels);
            Debug.Log("Mouse en: " + Input.mousePosition);
            if(Input.mousePosition.x >= objectPosPixels.x - offsetW &&
               Input.mousePosition.x <= objectPosPixels.x + offsetW &&
               Input.mousePosition.y >= objectPosPixels.y - offsetH &&
               Input.mousePosition.y <= objectPosPixels.y + offsetH)
            {
                pulsado = true;
                Debug.Log("Estoy pulsado " + pulsado);
            }
        }

        void setColor(Color color)
        {
            tileColor = color;
        }

        void setEmpty(bool e)
        {
            empty = e;
        }
    }
}
