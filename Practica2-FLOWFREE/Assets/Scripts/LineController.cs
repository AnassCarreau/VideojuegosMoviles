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
    // Start is called before the first frame update
    void Awake()
    {
        lr = GetComponent<LineRenderer>();
        points = new List<Vector2>();
        lr.startColor = Color.red;
        lr.endColor = Color.red;
    }

   public void SetUpLine(Transform[] points) 
    {
      //  lr.positionCount = points.Length;
       // this.points = points;
    }

    private void Update()
    {


        RaycastHit2D ra = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), -Vector2.up);


        // Debug.Log(inicio.position + " " + Camera.main.ScreenToWorldPoint(Input.mousePosition));
        if (ra.collider != null && ra.collider.CompareTag("Inicio"))
        {

            if (Input.GetMouseButtonDown(0))
            {
                pintar = true;
            }
        }

        if (pintar)
        {
            if (Input.GetMouseButtonDown(0))
            {
                start = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                points.Clear();
                points.Add(start);
                points.Add(start);
                lr.positionCount = 2;

            }
            if (Input.GetMouseButton(0))
            {
                Vector2 actual = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                //  Debug.Log(Vector2.Dot(actual, points[points.Count - 1]));
                int ang = (int)Vector2.Angle(points[points.Count - 2], actual);
                float dis = Vector2.Distance(points[points.Count - 2], actual);
                // Debug.Log(dis);
                // Debug.DrawLine(points[points.Count - 1], Vector2.up*100);
                //  Debug.DrawLine(points[points.Count - 1], Vector2.left*100);
                //  Debug.Log("Dis : " + dis +  "  Ang : " + ang);
                if (dis > 1 /*&& (Mathf.Approximately(90.0f,ang) || Mathf.Approximately(0.0f,ang)*/ )
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
