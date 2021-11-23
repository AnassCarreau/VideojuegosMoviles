using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu(fileName = "levelpack", menuName = "Flow/Level pack", order = 1)]
public class LevelPack : ScriptableObject
{
    [Tooltip("Nombre del lote")]
    public string packName;
    [Tooltip("Fichero con los niveles")]
    public TextAsset maps;
}
