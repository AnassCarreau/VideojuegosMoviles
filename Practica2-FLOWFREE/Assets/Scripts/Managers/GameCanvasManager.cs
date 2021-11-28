using UnityEngine;

public class GameCanvasManager : MonoBehaviour
{
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
}
