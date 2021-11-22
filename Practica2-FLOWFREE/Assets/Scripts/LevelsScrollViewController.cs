using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelsScrollViewController : MonoBehaviour
{
    // [SerializeField] Text levelNumberText;
    [SerializeField] int numberOfLevels;
    [SerializeField] GameObject levelBtnPref;
    [SerializeField] GameObject levelBtnParent;
    [SerializeField]
    private Color[] pipesColor;
    // [SerializeField] Transform levelBtnParent;

    private void Start()
    {
        LoadLevelButtons();
    }

    // load level buttons on game start
    private void LoadLevelButtons()
    {

        int contentnum = numberOfLevels / 30;



        int conAct = -1;
        GameObject levelBtnParentAux = new GameObject();
        for (int i = 0; i < numberOfLevels; i++)
        {
            if (i / 30 > conAct)
            {
                conAct++;
                levelBtnParentAux = Instantiate(levelBtnParent, transform) as GameObject;
            }
            GameObject levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform) as GameObject;
            levelBtnObj.GetComponent<LevelButtonItem>().levelIndex = i;
            levelBtnObj.GetComponent<LevelButtonItem>().SetColor(pipesColor[i / 30]);
            levelBtnObj.GetComponent<LevelButtonItem>().levelsScrollViewController = this;
        }
    }

    // user defined public method to handle something when user press any level button
    // at present we are just changing level number, in future you can do anything that is required at here
    public void OnLevelButtonClick(int levelIndex)
    {
        //levelNumberText.text = "Level " + (levelIndex + 1);
    }
}
