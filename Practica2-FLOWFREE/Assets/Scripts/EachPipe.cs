using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EachPipe : MonoBehaviour
{
    private Vector2 posInBoard;

    public void SetPositionInBoard(Vector2 p)
    {
        posInBoard = p;
    }

    public Vector2 GetPositionInBoard()
    {
        return posInBoard;
    }
}
