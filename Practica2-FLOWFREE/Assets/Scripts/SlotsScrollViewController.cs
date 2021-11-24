using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

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
        Category[] Categories =  LectutaLote.Instance.getCategories();

        for (int i = 0; i < Categories.Length; i++)
        {
            CategoryTextItem cat = Instantiate(CatPref, transform);
            Color c = Categories[i].color;
            cat.SetColor(c);
            cat.SetName(Categories[i].name);
            for (int j = 0; j < Categories[i].slots.Length; j++)
            {
                SlotButtonItem slotButton = Instantiate(SlotPref, transform) ;
                slotButton.SetCategory(Categories[i].name);
                slotButton.SetSlot(i);
                slotButton.SetText(Categories[i].slots[j].name, Categories[i].color);
            }
        }
    }

}