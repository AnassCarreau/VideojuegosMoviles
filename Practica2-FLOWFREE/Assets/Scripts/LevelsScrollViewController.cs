using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class LevelsScrollViewController : MonoBehaviour
{

    private int slotIndex;
    private int numberOfLevels;
    [SerializeField] private LevelButtonItem levelBtnPref;
    [SerializeField] private GameObject levelBtnParent;
    [SerializeField] private Color[] pipesColor;
    [SerializeField] private Color[] IndicatorColor;
    [SerializeField] private GameObject IndicatorParent;
    [SerializeField] private GameObject IndicatorPrefab;
    //TO DO PONER PRIVADA CON GET
    private List<GameObject> listIndicator;
    public List<GameObject> listInd
    {
        get { return listIndicator; }
    }
    private void Start()
    {
        listIndicator = new List<GameObject>();
        LoadScrollButtons();
    }

    // load level buttons on game start
    private void LoadScrollButtons()
    {
        LvlActual act = GameManager.Instance.GetLvlActual();
        Debug.Log(act.category + " " + act.slotIndex);
        numberOfLevels = GameManager.Instance.GetLevels()[act.category][act.slotIndex].Length;
        bool blocked = GameManager.Instance.GetCategories()[act.category].lotes[slotIndex].levelblocked;
        bool nextLvlsBlockeds = false;
        int conAct = -1;
        GameObject levelBtnParentAux = new GameObject();
        for (int i = 0; i < numberOfLevels; i++)
        {
            Nivel lvl = GameManager.Instance.GetLevels()[act.category][act.slotIndex][i];
            //Si el lote en el que estamos tiene niveles bloqueados miramos que esté sin jugar y que no sea el primero del lote 
            if (blocked)
            {
                nextLvlsBlockeds = lvl.bestMoves == 0 && i != 0;
            }

            if (i / 30 > conAct)
            {
                conAct++;
                levelBtnParentAux = Instantiate(levelBtnParent, transform);
                listIndicator.Add(Instantiate(IndicatorPrefab, IndicatorParent.transform));
            }

            LevelButtonItem levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform);
            levelBtnObj.SetLvl(i);
            if (!blocked || !nextLvlsBlockeds) levelBtnObj.SetColor(pipesColor[i / 30]);
            else { levelBtnObj.SetColor(Color.black); }

            //TO DO CAMBIAR A SCRIPT DEL PREFAB Y ASIGNARLE SUS DOS HIJOS
            if (lvl.perfect)
            {
                levelBtnObj.transform.GetChild(1).transform.gameObject.SetActive(true);
                levelBtnObj.SetColor(Color.black);
            }
            else if (lvl.bestMoves > 0)
            {
                levelBtnObj.transform.GetChild(2).transform.gameObject.SetActive(true);
                levelBtnObj.SetColor(Color.black);

            }
            else if (!blocked || !nextLvlsBlockeds) levelBtnObj.SetColor(pipesColor[i / 30]);
            else { levelBtnObj.SetColor(Color.black); }

        }
        //TO DO  QUITAR GET
        IndicatorParent.transform.position = new Vector2(IndicatorParent.transform.position.x - IndicatorPrefab.GetComponent<RectTransform>().rect.width * (float)conAct, IndicatorParent.transform.position.y);
    }

    


}
