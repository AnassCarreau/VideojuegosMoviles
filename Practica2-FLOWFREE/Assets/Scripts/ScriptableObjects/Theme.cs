using UnityEngine;

namespace SOC
{
    [CreateAssetMenu(fileName = "theme", menuName = "Flow/Theme", order = 3)]
    public class Theme : ScriptableObject
    {
        [Tooltip("Nombre del Tema")]
        public int index;
        [Tooltip("Array de colores del Tema")]
        public Color[] colorTheme = new Color[16];
    }
}
