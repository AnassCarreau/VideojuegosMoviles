using UnityEngine;
using UnityEngine.UI;

namespace FlowFreeGame.Menu
{
    public class CategoryTextItem : MonoBehaviour
    {
        [SerializeField] Image background;
        [SerializeField] Image line;
        [SerializeField] Text text;

        public void SetName(string name)
        {
            text.text = name;
        }
        public void SetColor(Color color)
        {
            line.color = color;
            background.color = new Color(color.r, color.g, color.b, 0.2f);
        }

    }
}
