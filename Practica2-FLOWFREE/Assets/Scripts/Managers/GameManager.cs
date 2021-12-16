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
            InitData();
            DontDestroyOnLoad(_instance);
        }
    }

    

    private void Start()
    {
      //  data = SaveSystem.LoadData();

        // ads.ShowBanner();

        
    }

    private void InitData()
    {
        //Habra que leer cuantas pistas nos quedan cuando se haga el guardado
        clues = 0;
        //if (data == null)
        //{
        //    saveCorrect = false;
        //    data = new DataSystem(0);
        //}
        //else
        //{
        //    saveCorrect = true;
        //    clues = data.clues;
        //}

        //Esto es temporal ya que hay que leerlo del data guardado, es para que funcione el boton de clue
        clues = 2;
        levels = new List<List<Nivel[]>>();
        
        //For de Categorias Intro-manias-rectangles
        for (int i = 0; i < categories.Length; i++)
        {
            levels.Add(new List<Nivel[]>());
            int numLotes = categories[i].lotes.Length;
            //For leyendo los archivos de cada categoría, es decir cada lote
            TextAsset[] slot = new TextAsset[numLotes];
            for (int j = 0; j < numLotes; j++)
            { 
                slot[j] = categories[i].lotes[j].maps;
                //TO DO FULL CERDADA
                char[] c = new char[1] { '\n' };
                string[] lvs = slot[j].text.Split(c, StringSplitOptions.RemoveEmptyEntries);
                levels[i].Add(new Nivel[lvs.Length]);
               
                for(int k = 0; k < lvs.Length; k++)
                {
                    levels[i][j][k].nivel = lvs[k];
                    levels[i][j][k].bestMoves = 0;
                }
                

                //if (saveCorrect)
                //{
                //    categories[i].lotes[j].bestScoresInLevels = data.minFlow[i][j];
                //}
                //else
                //{
                //    if (data.minFlow.ContainsKey(i))
                //    {
                //        data.minFlow[i].Add(categories[i].lotes[j].bestScoresInLevels);
                //    }
                //    else
                //    {
                //        List<int[]> lminflow = new List<int[]>();
                //        lminflow.Add(categories[i].lotes[j].bestScoresInLevels);
                //        data.minFlow.Add(i, lminflow);
                //    }
                //}
            }
        }
    }

    private void OnApplicationQuit()
    {
       // data.clues = clues;
      //  SaveSystem.SaveData(data);
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

    public void Quit()
    {
        SaveSystem.SaveData(data);
       // PlayerPrefs.Save();
        Application.Quit();
    }

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
            //Luego se quita 
            //data.minFlow[act.category][act.slotIndex][act.levelIndex] = categories[act.category].lotes[act.slotIndex].bestScoresInLevels[act.levelIndex];

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