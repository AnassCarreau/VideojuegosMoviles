using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelsScrollViewController : MonoBehaviour
{
    private int slotIndex;
    private int numberOfLevels;
    [SerializeField] private LevelButtonItem levelBtnPref;
    [SerializeField] private GameObject levelBtnParent;
    [SerializeField] private Color[] pipesColor;

    private void Start()
    {
        LoadScrollButtons();
    }

    // load level buttons on game start
    private void LoadScrollButtons()
    {
        
        numberOfLevels = GameManager.Instance.GetCategories()[GameManager.Instance.getActualPlay().category].lotes[slotIndex].levels.Length;
        bool blocked = GameManager.Instance.GetCategories()[GameManager.Instance.getActualPlay().category].lotes[slotIndex].levelblocked;
        bool nextLvlsBlockeds = false;
        int conAct = -1;
        GameObject levelBtnParentAux = new GameObject();
        for (int i = 0; i < numberOfLevels; i++)
        {
            if (blocked)
            {
                nextLvlsBlockeds =  GameManager.Instance.GetCategories()[GameManager.Instance.getActualPlay().category].lotes[slotIndex].bestScoresInLevels[i] == 0 && i != 0;
            }

            if (i / 30 > conAct)
            {
                conAct++;
                levelBtnParentAux = Instantiate(levelBtnParent, transform) as GameObject;
            }

            LevelButtonItem levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform);
            ///to do quitar get component
            levelBtnObj.SetLvl(i);
            if(!blocked || !nextLvlsBlockeds)levelBtnObj.SetColor(pipesColor[i / 30]);
            else { levelBtnObj.SetColor(Color.black); }
        }
    }

    public void SetLvl(int slot)
    {
        slotIndex = slot;
    }
}
