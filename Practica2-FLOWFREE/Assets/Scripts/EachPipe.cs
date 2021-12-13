using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EachPipe : MonoBehaviour
{
    private Vector2 posInBoard;
    private Color c;
    public void SetPositionInBoard(Vector2 p)
    {
        posInBoard = p;
    }

    public Vector2 GetPositionInBoard()
    {
        return posInBoard;
    }
    public void SetColorInBoard(Color color)
    {
        c = color;
    }

    public Color GetColorInBoard()
    {
        return c;
    }
}
