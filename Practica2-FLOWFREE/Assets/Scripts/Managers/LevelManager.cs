using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace FreeFlowGame
{
    public class LevelManager : MonoBehaviour
    {
        [SerializeField]
        GameCanvasManager canvasManager;

        private static LevelManager _instance;

        public static LevelManager Instance { get { return _instance; } }

        private void Awake()
        {
            if (_instance != null)
            {
                Destroy(this.gameObject);
            }
            else
            {
                _instance = this;
            }
        }

        // Start is called before the first frame update
        void Start()
        {

        }

        public void Restart()
        {
            //TO DO: Esto no esta hecho, habría que resetear los pipes
            BoardManager.Instance.Initialize();
        }

        public void NextLevel()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            CategoryPack[] categories = GameManager.Instance.GetCategories();
            List<List<Nivel[]>> levels = GameManager.Instance.GetLevels();
            
            if (act.levelIndex + 1 < levels[act.category][act.slotIndex].Length
                 && (levels[act.category][act.slotIndex][act.levelIndex + 1].bestMoves > 0
                || !categories[act.category].lotes[act.slotIndex].levelblocked))
            {
                act.levelIndex += 1;
                BoardManager.Instance.Initialize();
            }
        }
        public void BackLevel()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            if (act.levelIndex - 1 >= 0)
            {
                act.levelIndex -= 1;
                BoardManager.Instance.Initialize();
            }
        }

        public void UseClue()
        {
            GameManager.Instance.UseClue();
            BoardManager.Instance.GetPipeController().PaintClue();
            canvasManager.SetClueText(GameManager.Instance.GetNumClues());
        }

        public void SetLevelText(int n, int w, int h)
        {
            canvasManager.SetLevelText(n, w, h);
        }

        public void ImCanvasManager(FreeFlowGame.GameCanvasManager canvasManager_)
        {
            canvasManager = canvasManager_;
        }

        public void SetflowsText(int n)
        {
            canvasManager.SetflowsText(n, BoardManager.Instance.getPipeSolution().Count);
        }

        public void SetPercentageText(int n)
        {
            canvasManager.SetPercentageText(n);
        }

        public void SetMovesText(int n)
        {
            canvasManager.SetMovesText(n);
        }

        public void SetBestText()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            List<List<Nivel[]>> levels = GameManager.Instance.GetLevels();
            canvasManager.SetBestText(levels[act.category][act.slotIndex][act.levelIndex].bestMoves);
        }

        public void setClueText()
        {
            canvasManager.SetClueText(GameManager.Instance.GetNumClues());
        }
    }
}
