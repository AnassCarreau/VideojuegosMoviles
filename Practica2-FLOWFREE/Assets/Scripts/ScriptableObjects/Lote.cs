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
    //TO DO: mirar esto y el de categories porque esta 2 veces
    [Tooltip("Tiene niveles bloqueados")]
    public bool levelblocked;
}
