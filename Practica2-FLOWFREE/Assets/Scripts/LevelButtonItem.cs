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

    private void Start()
    {
       // levelButtonText.text = (levelIndex + 1).ToString();
    }
    public void SetColor(Color color) 
    {
        transform.GetComponent<Image>().color = color;
    }
   

    public void SetLvl(int lvl)
    {
       levelIndex = lvl;
       transform.GetChild(0).GetComponent<Text>().text = (levelIndex + 1).ToString();

    }
    // click event of level button
    public void OnLevelButtonClick()
    {
        GameManager.Instance.SetLevel(levelIndex);
        GameManager.Instance.LoadScene(scene.name);

    }


}