using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
// <summary>
/// LevelsScrollViewController - generate scrollview items
/// handle all things those required for scrollview controller
/// </summary>
public class LevelsScrollViewController : MonoBehaviour
{

   // [SerializeField] Text levelNumberText;
    [SerializeField] int numberOfLevels;
    [SerializeField] GameObject levelBtnPref;
    [SerializeField] Transform levelBtnParent;

    private void Start()
    {
        LoadLevelButtons();
    }

    // load level buttons on game start
    private void LoadLevelButtons()
    {
        for (int i = 0; i < numberOfLevels; i++)
        {
            GameObject levelBtnObj = Instantiate(levelBtnPref, levelBtnParent) as GameObject;
            levelBtnObj.GetComponent<LevelButtonItem>().levelIndex = i;
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
