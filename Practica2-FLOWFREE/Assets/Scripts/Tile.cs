using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
namespace FreeFlowGame
{

    public class Tile : MonoBehaviour
    {
        [SerializeField]
        private Color _color;

        [SerializeField]
        private SpriteRenderer _renderer;

        public void Init()
        {
            _renderer.color = _color;
        }
    }
}
