using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


public class SlotButtonItem : MonoBehaviour
{

    private int slotIndex;
    private int category;
    [SerializeField]private  UnityEngine.Object scene;
    [SerializeField] private Text text;
    [SerializeField] private Text textRight;
   
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
        LvlActual act = GameManager.Instance.GetLvlActual();
        int index = 0;
        if (GameManager.Instance.GetCategories()[category].lotes[slotIndex].levelblocked)
        {
            //TO DO: HACER
             //index = Array.IndexOf(GameManager.Instance.GetLevels()[act.category][act.slotIndex].bestScoresInLevels, 0);
        }
        else 
        {
            //TO DO: HACER
            //index = Array.FindAll(GameManager.Instance.GetCategories()[category].lotes[slotIndex].bestScoresInLevels, i => i > 0).Length;
        }
        int total = GameManager.Instance.GetLevels()[category][slotIndex].Length;
        textRight.text = index + " / " +total;
    }
    // click event of level button
    public void OnSlotButtonClick()
    {
        GameManager.Instance.SetSlot(slotIndex);
        GameManager.Instance.SetCategory(category);
        GameManager.Instance.LoadScene(scene.name);
    }
}