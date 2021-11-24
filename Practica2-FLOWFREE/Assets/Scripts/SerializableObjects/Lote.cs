using System.Collections;
using System.Collections.Generic;
using UnityEngine;


[CreateAssetMenu(fileName = "lote", menuName = "Flow/Lote", order = 2)]
public class Lote : ScriptableObject
{
    [Tooltip("Nombre del lote")]
    public string packName;
    [Tooltip("Fichero que contiene los niveles del lote")]
    public TextAsset maps;
    [Tooltip("Tiene niveles bloqueados")]
    public bool levelblocked;
    [Tooltip("Array con la mejor puntuacion de cada nivel")]
    public int[] bestScoresInLevels;
    [Tooltip("Array con cada nivel")]
    public string[] levels;
}
