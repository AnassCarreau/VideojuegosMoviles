using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EachPipe : MonoBehaviour
{
    private Vector2 posInBoard;
    private Color color;
    
    [SerializeField]
    private int pipeIndex = 0;
    public void SetPositionInBoard(Vector2 p)
    {
        posInBoard = p;
    }

    public Vector2 GetPositionInBoard()
    {
        return posInBoard;
    }
    public void SetPipeColor(Color color)
    {
        this.color = color;
    }

    public Color GetPipeColor()
    {
        return color;
    }

    public int GetPipeIndex()
    {
        return pipeIndex;
    }

    public void SetPipeIndex(int index)
    {
        pipeIndex = index;
    }
}
