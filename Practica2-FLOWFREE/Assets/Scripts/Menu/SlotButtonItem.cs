using UnityEngine;
using UnityEngine.UI;

namespace FlowFreeGame.Menu
{
    public class SlotButtonItem : MonoBehaviour
    {
        private int slotIndex;
        private int category;
        [SerializeField] private string scene;
        [SerializeField] private Text text;
        [SerializeField] private Text textRight;
        [SerializeField] private Button button;

        private void Start()
        {
            button.onClick.AddListener(() => OnSlotButtonClick());
        }

        public void SetCategory(int cat)
        {
            category = cat;
        }

        public void SetSlot(int slot)
        {
            slotIndex = slot;
        }

        public void SetText(string tex, Color c)
        {
            text.text = tex;
            text.color = c;
            int index = 0;

            LvlActual lvl;
            lvl.category = category;
            lvl.slotIndex = slotIndex;
            lvl.levelIndex = 0;

            int niveles = GameManager.Instance.GetLevels()[category][slotIndex].Length;
            if (GameManager.Instance.GetCategories()[category].lotes[slotIndex].levelblocked)
            {
                int i = 0;
                while (i < niveles && GameManager.Instance.GetLevelBestMoves(lvl) != 0)
                {
                    lvl.levelIndex = i;
                    i++;
                    index++;
                }
                //Cuando salimos del while hemos sumado una de mas siempre
                if (index > 0) index--;
            }
            else
            {
                for (int i = 0; i < niveles; i++)
                {
                    lvl.levelIndex = i;
                    if (GameManager.Instance.GetLevelBestMoves(lvl) != 0) index++;
                }
            }
            int total = GameManager.Instance.GetLevels()[category][slotIndex].Length;
            textRight.text = index + " / " + total;
        }
        // click event of level button
        public void OnSlotButtonClick()
        {
            GameManager.Instance.SetSlot(slotIndex);
            GameManager.Instance.SetCategory(category);
            GameManager.Instance.LoadScene(scene);
        }
    }
}