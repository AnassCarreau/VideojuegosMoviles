using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


public class SlotButtonItem : MonoBehaviour
{

    private int slotIndex;
    private string category;
    [SerializeField]private  UnityEngine.Object scene;
    [SerializeField] private Text text;
    [SerializeField] private Text textRight;
   
   
   
    public void SetCategory(string name)
    {
        category = name;
    }

    public void SetSlot(int slot)
    {
        slotIndex = slot;
    }

    public void SetText(string tex, Color c) 
    {
        text.text = tex;
        text.color = c;
        int index;
        if (LectutaLote.Instance.getDictionaryCategories()[category][slotIndex].lvlblocked)
        {
             index = Array.IndexOf(LectutaLote.Instance.getDictionaryCategories()[category][slotIndex].minFlow, 0);
        }
        else 
        {
            index = Array.FindAll(LectutaLote.Instance.getDictionaryCategories()[category][slotIndex].minFlow, i => i > 0).Length;
        }
            int total = LectutaLote.Instance.getDictionaryCategories()[category][slotIndex].levels.Length;
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