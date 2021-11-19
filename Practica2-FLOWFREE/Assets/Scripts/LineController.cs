using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LineController : MonoBehaviour
{
    private LineRenderer lr;
    List<Vector2> points;
    Vector2 start;
    public Transform inicio;
    bool pintar = false;

    Collider2D inicioCircle;
    // Start is called before the first frame update
    void Awake()
    {
        lr =GetComponent<LineRenderer>();
        points = new List<Vector2>();
       
    }

    private void Update()
    {


        RaycastHit2D ra = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), -Vector2.up);


        // Debug.Log(inicio.position + " " + Camera.main.ScreenToWorldPoint(Input.mousePosition));
        if (ra.collider != null && ra.collider.CompareTag("Inicio") && Input.GetMouseButtonDown(0))
        {
                pintar = true;
            inicioCircle = ra.collider;
            start = Camera.main.ScreenToWorldPoint(Input.mousePosition);
            points.Clear();
            points.Add(start);
            points.Add(start);
            lr.startColor = ra.collider.GetComponent<SpriteRenderer>().color;
            lr.endColor = ra.collider.GetComponent<SpriteRenderer>().color;
            lr.positionCount = 2;
        }

        if (pintar)
        {
           
            if (Input.GetMouseButton(0))
            {

                //Cerrar

                 ra = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), -Vector2.up);

                // Debug.Log(inicio.position + " " + Camera.main.ScreenToWorldPoint(Input.mousePosition));
                if (ra.collider != null && ra.collider!= inicioCircle && ra.collider.CompareTag("Inicio"))
                {
                    pintar = false;
                }

                //
                Vector2 actual = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                Vector2 vec = Vector2.MoveTowards(points[points.Count - 2], actual,10000).normalized;
                Debug.Log(vec.x + " " + vec.y);
                float dis = Vector2.Distance(points[points.Count - 2], actual);
              
                if (dis > 10 /*&& (Mathf.Approximately(90.0f,ang) || Mathf.Approximately(0.0f,ang)*/ )
                {
                    Debug.Log("hola");
                    points.Add(actual);
                    lr.positionCount++;
                }
                else
                {
                    points[points.Count - 1] = actual;
                }
            }
            if (Input.GetMouseButtonUp(0))
            {
                pintar = false;
            }


            for (int i = 0; i < points.Count; i++)
            {
                lr.SetPosition(i, points[i]);
            }
        }
    }
   


}
