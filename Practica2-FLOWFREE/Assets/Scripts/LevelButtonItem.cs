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
    public Color color;
    [SerializeField] Text levelButtonText;


    private void Start()
    {
        levelButtonText.text = (levelIndex + 1).ToString();
    }

    public void SetColor(Color color) 
    {
        transform.GetComponent<Image>().color = color;

    }
    // click event of level button
    public void OnLevelButtonClick()
    {
        levelsScrollViewController.OnLevelButtonClick(levelIndex);
    }


}