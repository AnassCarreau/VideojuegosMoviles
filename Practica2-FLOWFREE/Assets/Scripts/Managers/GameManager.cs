using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GameManager : MonoBehaviour
{
    private static GameManager _instance;

    public AdsManager ads;
    public static GameManager Instance { get { return _instance; } }

    [SerializeField]
    private FreeFlowGame.BoardManager boardManager;
    private LectutaLote lvlManager;
    DataSystem data;
    private void Awake()
    {
        if (_instance != null && _instance != this)
        {
            Destroy(this.gameObject);
        }
        else
        {
            _instance = this;
        }
    }

    public void DarPista() {
        ads.PlayerRewardedAd(OnRewardedAdSuccess);
    }

    void OnRewardedAdSuccess() 
    {
        Debug.Log("Pista para tu body ");
    }

    private void Start()
    {
        data = SaveSystem.LoadData();
        ads.ShowBanner();
    }

    public void LevelSuccess() 
    {
        ads.PlayAd();
    }

    public FreeFlowGame.BoardManager GetBoardManager()
    {
        return boardManager;
    }

    public void LoadScene(string name)
    {
        DontDestroyOnLoad(this);
        SceneManager.LoadScene(name);
    }
    public DataSystem getData() { return data; }
    public void setData(DataSystem data) { this.data = data; }

    public void Quit()
    {
        SaveSystem.SaveData(data);
       // PlayerPrefs.Save();
        Application.Quit();
    }
}