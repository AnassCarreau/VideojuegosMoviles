using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using SOC;

public struct LvlActual
{
    public int category;
    public int slotIndex;
    public int levelIndex;
}

public class GameManager : MonoBehaviour
{
    #region SerializeVariables
    //Level manager de la escena (si tiene)
    [SerializeField] private FlowFreeGame.LevelManager levelManager;

    //Array de los lotes sobre los que vamos a trabajar Intro-Manias-Rectangles
    [SerializeField] private CategoryPack[] categories;

    //Array con todos los temas que hay disponibles en la aplicacion, como no hay menu de
    //tienda en nuestra implementacion y necesitabamos guardar cual tema estabamos usando
    //guardamos como dato serializado el indice en este array que representa al tema
    [SerializeField] private Theme[] colorsThemes = new Theme[3];

    //Variables auxiliares para no tener que empezar siempre desde el menu principal
#if UNITY_EDITOR
    [SerializeField]
    private int nivel;
    [SerializeField]
    private int lote;
    [SerializeField]
    private int categoria;
#endif
    //Controlador de anuncios
    [SerializeField] private AdsManager ads;
    //Indice en el array de temas que representa al que esta en uso
    [SerializeField] private int indexTheme;
    #endregion

    #region PrivateVariables
    //Numero de pistas
    private int clues;
    //Instancia del gameManager
    private static GameManager _instance;
    //Datos de guardado
    private DataSystem data;
    //Lista sobre la que hacemos modificaciones para despues sobreescribir los datos de guardado
    private List<Cat> bestdata;
    //Nivel actual
    private LvlActual act;
    //Lista de categorias con su correspondiente lista de lotes y cada lote con sus niveles (ya separados)
    private List<List<string[]>> levels;
    //Tema usado actualmente
    private Theme themeAct;
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
        ads.ShowBanner();
    }

    private void InitData()
    {
        bool loadCorrect = false;
        clues = 3;
        if (data != null)
        {
            clues = data.clues;
            bestdata = data.bestScores;
            indexTheme = data.theme;
            loadCorrect = true;
        }
        else
        {
            data = new DataSystem();
            bestdata = new List<Cat>();
        }

        themeAct = colorsThemes[indexTheme];
        levels = new List<List<string[]>>();

        //For de Categorias Intro-manias-rectangles
        for (int i = 0; i < categories.Length; i++)
        {
            levels.Add(new List<string[]>());
            if (!loadCorrect) bestdata.Add(new Cat());
            int numLotes = categories[i].lotes.Length;

            //For leyendo los archivos de cada categoría, es decir cada lote
            TextAsset[] slot = new TextAsset[numLotes];
            for (int j = 0; j < numLotes; j++)
            {
                slot[j] = categories[i].lotes[j].maps;
                char[] c = new char[1] { '\n' };
                string[] lvs = slot[j].text.Split(c, StringSplitOptions.RemoveEmptyEntries);
                levels[i].Add(new string[lvs.Length]);

                if (!loadCorrect)
                {
                    Lot l = new Lot();
                    l.lvls = new Lvl[lvs.Length];
                    for(int k = 0; k < l.lvls.Length; k++)
                    {
                        l.lvls[k] = new Lvl();
                    }
                    bestdata[i].cat.Add(l);
                }

                for (int k = 0; k < lvs.Length; k++)
                {
                    levels[i][j][k]= lvs[k];
                }
            }
        }
    }

    public void LevelSuccess()
    {
        ads.PlayAd();
    }

    public FlowFreeGame.LevelManager GetLevelManager()
    {
        return levelManager;
    }

    public void LoadScene(string name)
    {
        SceneManager.LoadScene(name);
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
        int bestCurrentLvlScore = bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].bestMoves;
        if (bestCurrentLvlScore > n || bestCurrentLvlScore == 0)
        {
            bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].bestMoves = n;
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

    public void GetNewClue()
    {
        ads.PlayerRewardedAd(OnRewardedAdSuccess);
    }

    void OnRewardedAdSuccess()
    {
        clues++;
        if (levelManager != null) levelManager.SetClueText();
    }

    public LvlActual GetLvlActual() { return act; }

    public int GetNumClues() { return clues; }

    public string GetCurrentLevel() { return levels[act.category][act.slotIndex][act.levelIndex]; }

    public List<List<string[]>> GetLevels() { return levels; }

    public void SetPerfect() { 
        bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].perfect = true; 
    }

    public void ActualizeCurrentLevelBestScore(int n)
    {
        if(bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].bestMoves > n 
            || bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].bestMoves == 0)
        bestdata[act.category].cat[act.slotIndex].lvls[act.levelIndex].bestMoves = n;
    }

    public int GetLevelBestMoves(LvlActual lvl)
    {
        return bestdata[lvl.category].cat[lvl.slotIndex].lvls[lvl.levelIndex].bestMoves;
    }
    public bool GetIsLevelPerfect(LvlActual lvl)
    {
        return bestdata[lvl.category].cat[lvl.slotIndex].lvls[lvl.levelIndex].perfect;
    }

    public Theme GetColorTheme() { return themeAct; }

    public void SaveState()
    {
        data.clues = clues;
        data.bestScores = bestdata;
        if (themeAct != null) data.theme = themeAct.index;
        else data.theme = 0;
        SaveSystem.SaveData(data);
    }
}