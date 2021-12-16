using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu(fileName = "category", menuName = "Flow/Category Pack", order = 1)]
public class CategoryPack : ScriptableObject
{
    [Tooltip("Nombre de la categoría")]
    public string categoryName;
    [Tooltip("Color que representa la categoría")]
    public Color categoryColor;
    [Tooltip("Lotes incluidos en la categoría")]
    public Lote[] lotes;
}
