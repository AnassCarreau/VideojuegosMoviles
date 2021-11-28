using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

// <summary>
/// LevelButtonItem - attached to level button
/// handle specific button related actions
/// </summary>
public class LevelButtonItem : MonoBehaviour
{

     private int levelIndex;    
     private Color color;
     private Text levelButtonText;
    [SerializeField] private Object scene;
    [SerializeField] private Image img; 
    [SerializeField] private Text text; 
    public void SetColor(Color color) 
    {
        img.color = color;
    }
   

    public void SetLvl(int lvl)
    {
       levelIndex = lvl;
       text.text = (levelIndex + 1).ToString();

    }
    // click event of level button
    public void OnLevelButtonClick()
    {
        GameManager.Instance.SetLevel(levelIndex);
        GameManager.Instance.LoadScene(scene.name);
    }

}