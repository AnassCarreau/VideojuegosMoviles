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
    private void Awake()
    {
        listIndicator = new List<GameObject>();
        LoadScrollButtons();
    }

    // load level buttons on game start
    private void LoadScrollButtons()
    {
        LvlActual act = GameManager.Instance.GetLvlActual();

        numberOfLevels = GameManager.Instance.GetLevels()[act.category][act.slotIndex].Length;
        bool blocked = GameManager.Instance.GetCategories()[GameManager.Instance.getActualPlay().category].lotes[slotIndex].levelblocked;
        bool nextLvlsBlockeds = false;
        int conAct = -1;
        GameObject levelBtnParentAux = new GameObject();
        for (int i = 0; i < numberOfLevels; i++)
        {
            if (blocked)
            {
                nextLvlsBlockeds = GameManager.Instance.GetLevels()[act.category][act.slotIndex][i].bestMoves == 0 && i != 0;
            }

            if (i / 30 > conAct)
            {
                conAct++;
                levelBtnParentAux = Instantiate(levelBtnParent, transform);
                listIndicator.Add(Instantiate(IndicatorPrefab, IndicatorParent.transform));
            }

            LevelButtonItem levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform);
            ///to do quitar get component
            levelBtnObj.SetLvl(i);
            if (!blocked || !nextLvlsBlockeds) levelBtnObj.SetColor(pipesColor[i / 30]);
            else { levelBtnObj.SetColor(Color.black); }
        }

        IndicatorParent.transform.position = new Vector2(IndicatorParent.transform.position.x - IndicatorPrefab.GetComponent<RectTransform>().rect.width * (float)conAct, IndicatorParent.transform.position.y);
    }

    public void SetLvl(int slot)
    {
        slotIndex = slot;
    }


}
