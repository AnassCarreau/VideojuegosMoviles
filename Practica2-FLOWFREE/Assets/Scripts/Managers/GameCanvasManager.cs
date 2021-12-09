using UnityEngine;
using UnityEngine.UI;

public class GameCanvasManager : MonoBehaviour
{
    [SerializeField]
    private Text clueText;

    private void Awake()
    {
        GameManager.Instance.ImCanvasManager(this);   
    }

    public void ChangeNextLevel()
    {
        GameManager.Instance.NextLevel();
    }
    public void ChangeBackLevel()
    {
        GameManager.Instance.BackLevel();
    }
    public void RestartLevel()
    {
        GameManager.Instance.Restart();
    }

    public void GoToLevelScene()
    {
        GameManager.Instance.LoadScene("LevelSelector");
    }

    public void UseClue()
    {
        GameManager.Instance.UseClue();
    }

    public void GetNewClue()
    {
        GameManager.Instance.GetNewClue();
    }

    public void SetClueText(int clues)
    {
        clueText.text = clues + " x";
    }
}
