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
            c.a = 0.2f;
            cat.transform.GetChild(0).GetComponent<Image>().color = c;
            cat.GetComponent<Text>().text = Categories[i].name;
            for (int j = 0; j < Categories[i].slots.Length; j++)
            {
                GameObject slotButton = Instantiate(SlotPref, transform) as GameObject;
                slotButton.GetComponent<Text>().text = Categories[i].slots[j].name;
                slotButton.GetComponent<Text>().color = Categories[i].color;
            }
        }

    }

    // user defined public method to handle something when user press any level button
    // at present we are just changing level number, in future you can do anything that is required at here
    public void OnLevelButtonClick(int levelIndex)
    {
        //levelNumberText.text = "Level " + (levelIndex + 1);
    }


}