using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace FlowFreeGame.Menu
{
    public class ContentScrollScript : MonoBehaviour
    {
        [SerializeField] private Text dimensionsText;
        [SerializeField] private GameObject gridObject;

        public Text GetDimensionsText()
        {
            return dimensionsText;
        }

        public GameObject GetGridObject()
        {
            return gridObject;
        }
    }
}