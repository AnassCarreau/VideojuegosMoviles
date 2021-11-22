using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

// <summary>
/// LevelButtonItem - attached to level button
/// handle specific button related actions
/// </summary>
public class LevelButtonItem : MonoBehaviour
{

    [HideInInspector] public int levelIndex;
    [HideInInspector] public LevelsScrollViewController levelsScrollViewController;
    //
    [SerializeField] Text levelButtonText;


    private void Start()
    {
        levelButtonText.text = (levelIndex + 1).ToString();
    }

    // click event of level button
    public void OnLevelButtonClick()
    {
        levelsScrollViewController.OnLevelButtonClick(levelIndex);
    }


}