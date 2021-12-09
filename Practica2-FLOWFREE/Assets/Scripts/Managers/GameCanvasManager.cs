using UnityEngine;
using UnityEngine.UI;

public class GameCanvasManager : MonoBehaviour
{
    [SerializeField]
    private Text clueText;
    [SerializeField]
    private Text flowsText;
    [SerializeField]
    private Text bestText;
    [SerializeField]
    private Text movesText;
    [SerializeField]
    private Text pipePercentage;
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

    public void SetBestText(int n)
    {
        string s;
        if (n == 0)
        {
            s = "-";
        }
        else { s = n.ToString(); }

        bestText.text = "best : " + s;
    }
    public void SetflowsText(int n, int total)
    {
        flowsText.text = " flows : " + n + " / " + total;
    }
    public void SetMovesText(int n)
    {
        movesText.text = " moves : " + n;
    }
    public void SetPercentageText(int n)
    {
        pipePercentage.text = "pipe : " + n + " %";
    }
}
