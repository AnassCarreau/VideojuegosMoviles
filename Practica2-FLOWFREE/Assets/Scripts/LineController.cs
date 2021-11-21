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
    private List<Collider2D> _tiles;

    // Start is called before the first frame update
    void Awake()
    {
        lr =GetComponent<LineRenderer>();
        points = new List<Vector2>();
        _tiles = new List<Collider2D>();
       
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
            //_tiles.Add(ra.collider.gameObject);

        }

        if (pintar)
        {
            if (Input.GetMouseButton(0))
            {

                //Cerrar

                ra = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), -Vector2.up);

                if (ra.collider != null && ra.collider != inicioCircle && ra.collider.CompareTag("Inicio") && ra.collider.transform.GetComponent<SpriteRenderer>().color == lr.startColor)
                {
                    pintar = false;
                }

                //
                Vector2 actual = Camera.main.ScreenToWorldPoint(Input.mousePosition);
                //     Vector2 vec = Vector2.MoveTowards(points[points.Count - 2], actual,10000).normalized;
                //float dis = Vector2.Distance(points[points.Count - 2], actual);

                if (ra.collider != null && !_tiles.Contains(ra.collider) && ra.collider.CompareTag("Casilla") /*&& (Mathf.Approximately(90.0f,ang) || Mathf.Approximately(0.0f,ang)*/ )
                {
                    _tiles.Add(ra.collider);
                    points.Add(ra.collider.bounds.center);
                    lr.positionCount++;
                    Debug.Log(lr.positionCount);
                }
                else if (ra.collider != null && _tiles.Contains(ra.collider) && ra.collider.CompareTag("Casilla") && _tiles[_tiles.Count-1] != ra.collider )
                {
                    points.Remove(_tiles[_tiles.Count - 1].bounds.center);
                    _tiles.RemoveAt(_tiles.Count-1);
                    _tiles.Remove(ra.collider);
                    lr.positionCount--;
                    Debug.Log(lr.positionCount);
                }
                else 
                {
                    if (points.Count > 1)
                    {
                        points[points.Count - 2] = actual;
                    }
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
