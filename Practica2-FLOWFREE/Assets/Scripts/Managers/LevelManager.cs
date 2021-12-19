using System.Collections.Generic;
using UnityEngine;
using SOC;

namespace FlowFreeGame
{
    public class LevelManager : MonoBehaviour
    {
        [SerializeField]
        GameCanvasManager canvasManager;
        [SerializeField]
        private Animation boardAnimation;
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
            ActivateButtons();
        }

        public void Restart()
        {
            BoardManager.Instance.GetPipeController().ResetPipes();
        }

        public void NextLevel()
        {
            if (!BoardManager.Instance.IsPlayingAnimation())
            {
                LvlActual act = GameManager.Instance.GetLvlActual();
                CategoryPack[] categories = GameManager.Instance.GetCategories();
                int levels = GameManager.Instance.GetLevels()[act.category][act.slotIndex].Length;

                //Si podemos seguir avanzando en el lote nivel a nivel
                if (act.levelIndex + 1 < levels
                     && (GameManager.Instance.GetLevelBestMoves(act) > 0
                    || !categories[act.category].lotes[act.slotIndex].levelblocked))
                {
                    //Desactivamos el panel que sale cuando completas un nivel
                    canvasManager.SetPanelActive(false);

                    //Actualizamos nuestro nivel actual
                    GameManager.Instance.SetLevel(act.levelIndex + 1);
                    //Habilitamos/Desabilitamos los botones de pasar nivel o no
                    ActivateButtons();
                    //Iniciamos el tablero de la siguiente partida
                    BoardManager.Instance.PlayAnimation();

                }
                //Hemos acabado lote
                else if (act.levelIndex + 1 == levels)
                {
                    GameManager.Instance.LoadScene("MainMenuFlowFree");
                }
            }
        }

      
       
       
        public void BackLevel()
        {
            if (!BoardManager.Instance.IsPlayingAnimation())
            {
                LvlActual act = GameManager.Instance.GetLvlActual();
                if (act.levelIndex - 1 >= 0)
                {
                    Debug.Log("Cambio de nivel");
                    GameManager.Instance.SetLevel(act.levelIndex - 1);
                    ActivateButtons();
                    BoardManager.Instance.PlayAnimation();
                }
            }
        }

        public void UseClue()
        {
            if (GameManager.Instance.UseClue())
            {
                BoardManager.Instance.GetPipeController().PaintClue();
                canvasManager.SetClueText(GameManager.Instance.GetNumClues());
            }
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

            canvasManager.SetBestText(GameManager.Instance.GetLevelBestMoves(GameManager.Instance.GetLvlActual()));
        }

        public void SetClueText()
        {
            canvasManager.SetClueText(GameManager.Instance.GetNumClues());
        }

        public void LevelCompleted(int moves)
        {            
            LvlActual act = GameManager.Instance.GetLvlActual();
            List<List<string[]>> levels = GameManager.Instance.GetLevels();
            
            //Guardamos que hemos conseguido superar el nivel en ese numero de movimientos
            GameManager.Instance.ActualizeCurrentLevelBestScore(moves);
            GameManager.Instance.SaveState();

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

        private void ActivateButtons()
        {
            LvlActual act = GameManager.Instance.GetLvlActual();
            List<List<string[]>> levels = GameManager.Instance.GetLevels();

            if (act.levelIndex == levels[act.category][act.slotIndex].Length - 1 || 
                (GameManager.Instance.GetCategories()[act.category].lotes[act.slotIndex].levelblocked && GameManager.Instance.GetLevelBestMoves(act) == 0))
                canvasManager.IsNextLevelButtonInteractuable(false);
            else
                canvasManager.IsNextLevelButtonInteractuable(true);
            

            if (act.levelIndex == 0) canvasManager.IsPrevLevelButtonInteractuable(false);
            else canvasManager.IsPrevLevelButtonInteractuable(true);
        }
    }
}
