using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace FlowFreeGame.Menu
{
    public class LevelsScrollViewController : MonoBehaviour
    {
        private int contGrids;
        private int slotIndex;
        private int numberOfLevels;
        private Color[] pipesColor;
        private Text dimensionsText;

        [SerializeField] private LevelButtonItem levelBtnPref;
        [SerializeField] private ContentScrollScript contentScroll;

        private void Start()
        {
            dimensionsText = contentScroll.GetDimensionsText();
            contGrids = 0;
            pipesColor = GameManager.Instance.GetColorTheme().colorTheme;
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
            ContentScrollScript levelBtnParentAux = new ContentScrollScript();

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
                    dimensionsText.text = GameManager.Instance.GetCategories()[act.category].lotes[slotIndex].nameEach30levels[conAct];
                    levelBtnParentAux = Instantiate(contentScroll, transform);
                }

                LevelButtonItem levelBtnObj = Instantiate(levelBtnPref, levelBtnParentAux.GetGridObject().transform);
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
                
                //En función de si el nivel está perfecto o no activamos la estrella o el tick
                if (GameManager.Instance.GetIsLevelPerfect(act))
                {
                    levelBtnObj.GetStarObject().SetActive(true);
                }
                else if (GameManager.Instance.GetLevelBestMoves(act) > 0)
                {
                    levelBtnObj.GetTickObject().SetActive(true);
                }
            }
        }
    }
}
