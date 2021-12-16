using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public struct LvlActual
{
    public int category;
    public int slotIndex;
    public int levelIndex;
}

public struct Nivel
{
    public string nivel;
    public int bestMoves;
}


public class GameManager : MonoBehaviour
{
    #region SerializeVariables
    [SerializeField]
    private FreeFlowGame.LevelManager levelManager;
    #endregion

    #region PublicVariables
    //Array de los lotes sobre los que vamos a trabajar Intro-Manias-Rectangles
    public CategoryPack[] categories;
    //
    public AdsManager ads;

    //de momento publico para que podamos darle a las escenas sin que se joda 
    private LvlActual act;

    //Lista de categorias con su correspondiente lista de lotes y cada lote con sus niveles (ya separados)
    private List<List<Nivel[]>> levels;

#if UNITY_EDITOR
    [SerializeField]
    private int nivel;
    [SerializeField]
    private int lote;
    [SerializeField]
    private int categoria;
#endif

#endregion

    #region PrivateVariables
    private DataSystem data;
    private static GameManager _instance;

    //private FreeFlowGame.GameCanvasManager canvasManager;

    //Variable que controla el numero de pistas
    private int clues;
    private List<Cat> bestdata;
    #endregion

    public static GameManager Instance { get { return _instance; } }


    private void Awake()
    {
        if (_instance != null)
        {
            _instance.levelManager = levelManager;
            Destroy(this.gameObject);
        }
        else
        {
            _instance = this;
#if UNITY_EDITOR
            act.category = categoria;
            act.slotIndex = lote;
            act.levelIndex = nivel;
#endif
            data = SaveSystem.LoadData();
            InitData();
            DontDestroyOnLoad(_instance);
        }
    }



    private void Start()
    {
        
        // ads.ShowBanner();  
    }

    private void InitData()
    {
        bool loadCorrect=false;
        //Habra que leer cuantas pistas nos quedan cuando se haga el guardado
        clues = 2;
        if (data != null)
        {
            clues = data.clues;
            bestdata = data.bestScores;
            loadCorrect = true;
        }
        else
        {
            data = new DataSystem();
            bestdata = new List<Cat>();
        }

        
        //Esto es temporal ya que hay que leerlo del data guardado, es para que funcione el boton de clue
        levels = new List<List<Nivel[]>>();
        
        //For de Categorias Intro-manias-rectangles
        for (int i = 0; i < categories.Length; i++)
        {
            levels.Add(new List<Nivel[]>());
            if (!loadCorrect)bestdata.Add(new Cat());
            int numLotes = categories[i].lotes.Length;
            //For leyendo los archivos de cada categoría, es decir cada lote
            TextAsset[] slot = new TextAsset[numLotes];
            for (int j = 0; j < numLotes; j++)
            { 
                slot[j] = categories[i].lotes[j].maps;
                char[] c = new char[1] { '\n' };
                string[] lvs = slot[j].text.Split(c, StringSplitOptions.RemoveEmptyEntries);
                levels[i].Add(new Nivel[lvs.Length]);

                if (!loadCorrect)
                {
                    Lot l = new Lot();
                    l.lvl = new int[lvs.Length];
                    bestdata[i].cat.Add(l);

                }
                for(int k = 0; k < lvs.Length; k++)
                {
                    levels[i][j][k].nivel = lvs[k];
                    levels[i][j][k].bestMoves = bestdata[i].cat[j].lvl[k] ;
                }
            }
        }
    }

    private void OnApplicationQuit()
    {
        Debug.Log(data);
        data.clues = clues;
        data.bestScores = bestdata;
        SaveSystem.SaveData(data);
    }
    public void LevelSuccess() 
    {
        ads.PlayAd();
    }

    public FreeFlowGame.LevelManager GetLevelManager()
    {
        return levelManager;
    } 
    
   

    public void LoadScene(string name)
    {
        SceneManager.LoadScene(name);
    }

    public DataSystem getData() { return data; }
    public void setData(DataSystem data) { this.data = data; }

   

    public CategoryPack[] GetCategories()
    {
        return categories;
    }

    public void SetCategory(int cat) 
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

    public void SetScore(int n)
    {
        int bestCurrentLvlScore =  levels[act.category][act.slotIndex][act.levelIndex].bestMoves;
        if (bestCurrentLvlScore > n || bestCurrentLvlScore == 0)
        {
            levels[act.category][act.slotIndex][act.levelIndex].bestMoves = n;
            bestdata[act.category].cat[act.slotIndex].lvl[act.levelIndex] = levels[act.category][act.slotIndex][act.levelIndex].bestMoves;
        }
    }
   

    public bool UseClue()
    {

        if (clues - 1 >= 0)
        {
            clues--;
            return true;
        }
        else return false;
    }
    public void GetNewClue() {
        ads.PlayerRewardedAd(OnRewardedAdSuccess);
    }

    void OnRewardedAdSuccess() 
    {
        clues++;
        if (levelManager != null) levelManager.setClueText();
    }

    public LvlActual GetLvlActual() { return act; }

    public int GetNumClues() { return clues; }

    public string GetCurrentLevel() { return levels[act.category][act.slotIndex][act.levelIndex].nivel; }

    public List<List<Nivel[]>> GetLevels() { return levels; }
}