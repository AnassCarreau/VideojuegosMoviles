using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class Map
{
    private int width;
    private int height;
    private int lvlnum;
    private int flownum;
    private List<int> bridge;
    private List<int> holes;
    private List<int> walls;
    private List<List<Vector2>> pipes;

    public bool Parse(string lvl)
    {
        string[] data = lvl.Split(';');
        string cabecera = data[0].Replace("+B", "");
        string[] comas = cabecera.Split(',');
        List<int> dim = comas[0].Split(':').Select(int.Parse).ToList();
        width = dim[0];
        height = dim.Count == 1 ? width : dim[1];
        lvlnum = int.Parse(comas[2]);
        flownum = int.Parse(comas[3]);

        if (comas.Length > 4 && comas[4].Length > 0)
        {
            Debug.Log("Puentes");
            bridge = comas[4].Split(':').Select(int.Parse).ToList();
        }
        if (comas.Length > 5 && comas[5].Length > 0)
        {
            Debug.Log("Holes");
            holes = comas[5].Split(':').Select(int.Parse).ToList();
        }
        if (comas.Length > 6 && comas[6].Length > 0)
        {
            Debug.Log("Muros");
            char[] sep = { '|', ':' };
            walls = comas[6].Split(sep).Select(int.Parse).ToList();
        }

        pipes = new List<List<Vector2>>();
        for (int i = 1; i < data.Length; i++)
        {
            string[] pipe = data[i].Split(',');
            List<Vector2> aux = new List<Vector2>();
            for (int j = 0; j < pipe.Length; j++)
            {
                int valor = int.Parse(pipe[j]);
                int posX = valor % width;
                int posY = valor / height;
                if (width != height)
                {
                    if (width < height) posY = valor / width;
                }
                Vector2 posCasilla = new Vector2(posX, -posY);
                aux.Add(posCasilla);
            }
            pipes.Add(aux);
        }
       
        return true;
    }


    public List<List<Vector2>> GetPipes() { return pipes; }
    public List<int> Getbridge() { return bridge; }
    public List<int> Getholes() { return holes; }
    public List<int> Getwalls() { return walls; }
    public int GetWidth() { return width; }
    public int GetHeight() { return height; }

    public int GetFlownum() { return flownum; }
    public int GetLvlnum()  { return lvlnum; }

    
}
