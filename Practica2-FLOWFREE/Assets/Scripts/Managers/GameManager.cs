using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

[System.Serializable]
public struct LvlActual
{
    public string category;
    public int slotIndex;
    public int levelIndex;

}

public class GameManager : MonoBehaviour
{

    public LevelPack[] levels;

    public AdsManager ads;
    //de momento publico para que podamos darle a las escenas sin que se joda 
    public static LvlActual act;
    [SerializeField]
    private FreeFlowGame.BoardManager boardManager;
  // [SerializeField] private LectutaLote lvlManager;
    DataSystem data;
    private static GameManager _instance;

    public static GameManager Instance { get { return _instance; } }


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
        // ads.ShowBanner();
        //Pequeño chanchullo de momento para poder probar la funcionalidad directamente sin empezar el menu todo el rato
        //Basicamente inicializo boardManager en el start de gameManager si es la escena del juego la primera 
        //To do quitarlo e intentar que lecturalote no dependa de monobehvoiur
        LectutaLote.Instance.Initialize();
        if (SceneManager.GetActiveScene().name == boardManager.getScene().name)
        {
            Debug.Log("Inicio");
            boardManager.Initialize();
        }
    }

    public void LevelSuccess() 
    {
        ads.PlayAd();
    }


    private void OnLevelWasLoaded(int level)
    {
        Debug.Log(level);
        Debug.Log(SceneManager.GetActiveScene().name);
        if (SceneManager.GetActiveScene().name == boardManager.getScene().name)
        {
            boardManager.Initialize();
        }
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

    public LvlActual getActualPlay() 
    {
        return act;
    }

    public void SetCategory(string cat) 
    {
        act.category = cat;
    }
    public void SetSlot(int slot)
    {
        act.slotIndex = slot;
    }
    public void SetLevel(int lvl)
    {
        act.levelIndex = lvl;
    }

    public void Restart() 
    {
        boardManager.Clear();
        boardManager.Initialize();
    }

    public void NextLevel() 
    {
        Dictionary<string, List<Slot>> dicc = LectutaLote.Instance.getDictionaryCategories();
        if (act.levelIndex + 1 < dicc[act.category][act.slotIndex].levels.Length 
            && (dicc[act.category][act.slotIndex].minFlow[act.levelIndex+1]>0 || !dicc[act.category][act.slotIndex].lvlblocked))
        {
            act.levelIndex += 1;
            boardManager.Clear();
            boardManager.Initialize();
        }
    } 
    public void BackLevel() 
    {
        if (act.levelIndex - 1 >= 0)
        {
            act.levelIndex -= 1;
            boardManager.Clear();
            boardManager.Initialize();
        }
    }
}