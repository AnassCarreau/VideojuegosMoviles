using UnityEngine;

namespace SOC
{
    [CreateAssetMenu(fileName = "category", menuName = "Flow/Category Pack", order = 1)]
    public class CategoryPack : ScriptableObject
    {
        [Tooltip("Nombre de la categor�a")]
        public string categoryName;
        [Tooltip("Color que representa la categor�a")]
        public Color categoryColor;
        [Tooltip("Lotes incluidos en la categor�a")]
        public Lote[] lotes;
    }
}
