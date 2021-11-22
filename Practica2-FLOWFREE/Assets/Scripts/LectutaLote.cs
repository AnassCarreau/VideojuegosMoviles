using System;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

public struct Slot
{
    public string[] levels;
    public int[] minFlow;
    public string Name;
    public bool lvlblocked;
    public Slot(string[] levels, int[] minFlow, string Name, bool lvlblocked)
    {
        this.levels = levels;
        this.minFlow = minFlow;
        this.Name = Name;
        this.lvlblocked = lvlblocked;
    }
}
[Serializable]
public  struct Category
{
    public string name;
    public Color color;
    public bool lvlblocked;
    public TextAsset[] slots;
}



public class LectutaLote : MonoBehaviour
{
   [SerializeField] Category[] cat;
    Dictionary<string, List<Slot>> Categories;
    int clues;
    bool saveCorrect;
    private static LectutaLote _instance;
    public static LectutaLote Instance { get { return _instance; } }

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

  
    private void Start()
    {
        DataSystem data = GameManager.Instance.getData();
        clues = 0;
        Categories = new Dictionary<string, List<Slot>>();
        if (data == null) 
        {
            saveCorrect = false;
            data = new DataSystem(0);
        }
        else
        {
            saveCorrect = true;
            clues = data.clues;
        }
        
        for (int i = 0; i < cat.Length; i++)
        {
            TextAsset[] slot = cat[i].slots;
            for (int j = 0; j < slot.Length; j++)
            {

                //TO DO FULL CERDADA
                char[]c= new char[1]{'\n'};
                string[] lvls = slot[j].text.Split(c,StringSplitOptions.RemoveEmptyEntries);
                
                int[] minflow = new int[lvls.Length];
                if (saveCorrect)
                {
                    minflow = data.minFlow[cat[i].name][j];
                }
                else
                {
                    if (data.minFlow.ContainsKey(cat[i].name))
                    {
                        data.minFlow[cat[i].name].Add(minflow);
                    }
                    else
                    {
                        List<int[]> lminflow = new List<int[]>();
                        lminflow.Add(minflow);
                        data.minFlow.Add(cat[i].name, lminflow);
                    }
                }

                Slot s = new Slot(lvls, minflow, slot[j].name, cat[i].lvlblocked);

                if (!Categories.ContainsKey(cat[i].name))
                {
                    List<Slot> lslot = new List<Slot>();
                    lslot.Add(s);
                    Categories.Add(cat[i].name, lslot);
                }
                else
                {
                    Categories[cat[i].name].Add(s);
                }

            }
        }
        GameManager.Instance.setData(data);
    }

    public Dictionary<string, List<Slot>> getDictionaryCategories() 
    {
        return Categories;
    }

    public Category[] getCategories() { return cat; }
   
    //public Tile GetTileAtPosition(Vector2 pos)
    //{
    //    if (_tiles.TryGetValue(pos, out var tile)) return tile;
    //    return null;
    //}

}
