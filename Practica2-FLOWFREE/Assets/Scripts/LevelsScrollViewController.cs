using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelsScrollViewController : MonoBehaviour
{
    private int slotIndex;
    private int numberOfLevels;
    [SerializeField] private GameObject levelBtnPref;
    [SerializeField] private GameObject levelBtnParent;
    [SerializeField] private Color[] pipesColor;

    private void Start()
    {
        LoadScrollButtons();
    }

    // load level buttons on game start
    private void LoadScrollButtons()
    {
        
        numberOfLevels = LectutaLote.Instance.getDictionaryCategories()[GameManager.Instance.getActualPlay().category][slotIndex].levels.Length;
        bool blocked = LectutaLote.Instance.getDictionaryCategories()[GameManager.Instance.getActualPlay().category][slotIndex].lvlblocked;
        bool nextLvlsBlockeds = false;
        int conAct = -1;
        GameObject levelBtnParentAux = new GameObject();
        for (int i = 0; i < numberOfLevels; i++)
        {
            if (blocked)
            {
                nextLvlsBlockeds =  LectutaLote.Instance.getDictionaryCategories()[GameManager.Instance.getActualPlay().category][slotIndex].minFlow[i] == 0 && i != 0;
            }

            if (i / 30 > conAct)
            {
                conAct++;
                levelBtnParentAux = Instantiate(levelBtnParent, transform) as GameObject;
            }
            
            GameObject levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform) as GameObject;
            levelBtnObj.GetComponent<LevelButtonItem>().SetLvl(i);
            if(!blocked || !nextLvlsBlockeds)levelBtnObj.GetComponent<LevelButtonItem>().SetColor(pipesColor[i / 30]);
            else { levelBtnObj.GetComponent<LevelButtonItem>().SetColor(Color.black); }
        }
    }


    

    public void SetLvl(int slot)
    {
        slotIndex = slot;
    }
}
