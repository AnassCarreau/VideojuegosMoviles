using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


public class SlotButtonItem : MonoBehaviour
{

    private int slotIndex;
    private string category;
    [SerializeField]private Object scene;
   
   
   
    public void SetCategory(string name)
    {
        category = name;
    }

    public void SetSlot(int slot)
    {
        slotIndex = slot;
    }
    // click event of level button
    public void OnSlotButtonClick()
    {
        GameManager.Instance.SetSlot(slotIndex);
        GameManager.Instance.SetCategory(category);
        GameManager.Instance.LoadScene(scene.name);
    }
}