using UnityEngine;
using SOC;

namespace FlowFreeGame.Menu
{
    public class SlotsScrollViewController : MonoBehaviour
    {

        [SerializeField] public CategoryTextItem CatPref;
        [SerializeField] public SlotButtonItem SlotPref;

        private void Start()
        {
            LoadLevelButtons();
        }

        // load level buttons on game start
        private void LoadLevelButtons()
        {
            CategoryPack[] Categories = GameManager.Instance.GetCategories();

            for (int i = 0; i < Categories.Length; i++)
            {
                CategoryTextItem cat = Instantiate(CatPref, transform);
                Color c = Categories[i].categoryColor;
                cat.SetColor(c);
                cat.SetName(Categories[i].name);
                for (int j = 0; j < Categories[i].lotes.Length; j++)
                {
                    SlotButtonItem slotButton = Instantiate(SlotPref, transform);
                    slotButton.SetCategory(i);
                    slotButton.SetSlot(j);
                    slotButton.SetText(Categories[i].lotes[j].packName, Categories[i].categoryColor);
                }
            }
        }

    }
}