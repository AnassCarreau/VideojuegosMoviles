using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SlotsScrollViewController : MonoBehaviour
{

    [SerializeField] public GameObject CatPref;
    [SerializeField] public GameObject SlotPref;

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
            GameObject cat = Instantiate(CatPref, transform) as GameObject;
            Color c = Categories[i].color;
            cat.GetComponent<CategoryTextItem>().SetColor(c);
            cat.GetComponent<CategoryTextItem>().SetName(Categories[i].name);
            for (int j = 0; j < Categories[i].slots.Length; j++)
            {
                GameObject slotButton = Instantiate(SlotPref, transform) as GameObject;
                slotButton.GetComponent<Text>().text = Categories[i].slots[j].name;
                slotButton.GetComponent<Text>().color = Categories[i].color;
                slotButton.GetComponent<SlotButtonItem>().SetCategory(Categories[i].name);
                slotButton.GetComponent<SlotButtonItem>().SetSlot(i);
            }
        }
    }

}