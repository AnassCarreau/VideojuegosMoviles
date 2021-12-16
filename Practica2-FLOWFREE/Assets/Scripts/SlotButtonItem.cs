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
        int index = 0;
        if (GameManager.Instance.GetCategories()[category].lotes[slotIndex].levelblocked)
        {
            List<List<Nivel[]>> niveles = GameManager.Instance.GetLevels();
            int i = 0;
            while (i < niveles[category][slotIndex].Length && niveles[category][slotIndex][i].bestMoves != 0)
            {
                i++;
                index++;
            }
        }
        else 
        {
            List<List<Nivel[]>> niveles = GameManager.Instance.GetLevels();
            for (int i = 0; i < niveles[category][slotIndex].Length; i++)
            {
                if(niveles[category][slotIndex][i].bestMoves != 0) index++;
            }
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