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
            LvlActual act = GameManager.Instance.GetLvlActual();
            List<List<Nivel[]>> levels = GameManager.Instance.GetLevels();

            if (act.levelIndex  == levels[act.category][act.slotIndex].Length - 1) canvasManager.isNextLevelButtonInteractuable(false);
            else canvasManager.isNextLevelButtonInteractuable(true);

            if (act.levelIndex == 0) canvasManager.isPrevLevelButtonInteractuable(false);
            else canvasManager.isPrevLevelButtonInteractuable(true);
        }

        public void Restart()
        {
            BoardManager.Instance.GetPipeController().ResetPipes();
        }

        public void NextLevel()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            CategoryPack[] categories = GameManager.Instance.GetCategories();
            List<List<Nivel[]>> levels = GameManager.Instance.GetLevels();

            //Si podemos seguir avanzando en el lote nivel a nivel
            if (act.levelIndex + 1 < levels[act.category][act.slotIndex].Length
                 && (levels[act.category][act.slotIndex][act.levelIndex].bestMoves > 0
                || !categories[act.category].lotes[act.slotIndex].levelblocked))
            {
                Debug.Log("Cambio de nivel");
                //Desactivamos el panel que sale cuando completas un nivel
                canvasManager.SetPanelActive(false);

                //Hacemos que sea interactuable o no el boton de pasar nivel
                if (act.levelIndex + 1 == levels[act.category][act.slotIndex].Length - 1) canvasManager.isNextLevelButtonInteractuable(false);
                else canvasManager.isNextLevelButtonInteractuable(true);
                
                //Actualizamos nuestro nivel actual
                GameManager.Instance.SetLevel(act.levelIndex + 1);
                //Iniciamos el tablero de la siguiente partida
                BoardManager.Instance.Initialize();
            }
            //Hemos acabado lote
            else if(act.levelIndex + 1 == levels[act.category][act.slotIndex].Length)
            {
                GameManager.Instance.LoadScene("MainMenuFlowFree");
            }
        }
        public void BackLevel()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            if (act.levelIndex - 1 >= 0)
            {
                Debug.Log("Cambio de nivel");
                if (act.levelIndex - 1 == 0) canvasManager.isPrevLevelButtonInteractuable(false);
                else canvasManager.isPrevLevelButtonInteractuable(true);
                GameManager.Instance.SetLevel(act.levelIndex - 1);
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

        public void SetflowsText(int n)
        {
            canvasManager.SetflowsText(n, BoardManager.Instance.GetPipeSolution().Count);
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

        public void LevelCompleted(int moves)
        {
            //TO DO: guardar cosos supongo
            
            LvlActual act = GameManager.Instance.GetLvlActual();
            List<List<Nivel[]>> levels = GameManager.Instance.GetLevels();
            levels[act.category][act.slotIndex][act.levelIndex].bestMoves = moves;

            //Setteamos los textos del panel segun si hemos completado el slot o no
            if (act.levelIndex + 1 < levels[act.category][act.slotIndex].Length)
            {
                canvasManager.SetPannelText("Completaste el nivel en " + moves + " pasos.");
                canvasManager.SetPannelButtonText("próximo nivel");
            }
            //Hemos acabado todos los del slot
            else
            {
                canvasManager.SetPannelText("Has llegado al final del " + GameManager.Instance.GetCategories()[act.category].categoryName);
                //TO DO: aqui este boton tiene una funcionalidad distinta
                canvasManager.SetPannelButtonText("elige próximo paquete");
            }
            canvasManager.SetPanelActive(true);
        }
    }
}
