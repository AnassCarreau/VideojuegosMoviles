using System.Collections.Generic;
using UnityEngine;

namespace FlowFreeGame.Menu
{
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
            numberOfLevels = GameManager.Instance.GetLevels()[act.category][act.slotIndex].Length;
            bool blocked = GameManager.Instance.GetCategories()[act.category].lotes[slotIndex].levelblocked;

            bool nextLvlsBlockeds = false;
            int conAct = -1;
            GameObject levelBtnParentAux = new GameObject();


            for (int i = 0; i < numberOfLevels; i++)
            {
                act.levelIndex = i;
                LvlActual prevLevel = act;

                if (i - 1 >= 0) prevLevel.levelIndex = i - 1;

                //Si el lote en el que estamos tiene niveles bloqueados miramos que esté sin jugar y que no sea el primero del lote 
                if (blocked)
                {
                    if (i - 1 >= 0 && GameManager.Instance.GetLevelBestMoves(act) == 0 && GameManager.Instance.GetLevelBestMoves(prevLevel) == 0)
                        nextLvlsBlockeds = true;
                }

                if (i / 30 > conAct)
                {
                    conAct++;
                    levelBtnParentAux = Instantiate(levelBtnParent, transform);
                    listIndicator.Add(Instantiate(IndicatorPrefab, IndicatorParent.transform));
                }

                LevelButtonItem levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.transform);
                levelBtnObj.SetLvl(i);
                levelBtnObj.SetColor(pipesColor[i / 30]);
                if (!blocked || !nextLvlsBlockeds)
                {
                    levelBtnObj.SetButtonInteractable(true);
                }
                else
                {
                    levelBtnObj.SetButtonInteractable(false);
                }

                //TO DO CAMBIAR A SCRIPT DEL PREFAB Y ASIGNARLE SUS DOS HIJOS
                if (GameManager.Instance.GetIsLevelPerfect(act))
                {
                    levelBtnObj.transform.GetChild(1).transform.gameObject.SetActive(true);
                }
                else if (GameManager.Instance.GetLevelBestMoves(act) > 0)
                {
                    levelBtnObj.transform.GetChild(2).transform.gameObject.SetActive(true);
                }

            }
            //TO DO  QUITAR GET
            IndicatorParent.transform.position = new Vector2(IndicatorParent.transform.position.x - IndicatorPrefab.GetComponent<RectTransform>().rect.width * (float)conAct, IndicatorParent.transform.position.y);
        }
    }
}
