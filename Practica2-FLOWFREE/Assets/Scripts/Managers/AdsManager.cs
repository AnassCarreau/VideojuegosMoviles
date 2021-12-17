using System.Collections;
using UnityEngine;
using UnityEngine.Advertisements;
using System;

public class AdsManager : MonoBehaviour, IUnityAdsListener
{
    [SerializeField] private string GameId;
    [SerializeField] private string nameADPista;
    [SerializeField] private string nameADNivelSuperado;
    [SerializeField] private string nameADBanner;
    Action onRewardedSuccess;

    // Start is called before the first frame update
    void Start()
    {
        Advertisement.Initialize(GameId);
        Advertisement.AddListener(this);
    }

    public void PlayAd()
    {
        if (Advertisement.IsReady(nameADNivelSuperado))
        {
            Advertisement.Show(nameADNivelSuperado);
        }
    }

    public void PlayerRewardedAd(Action onSuccess)
    {
        onRewardedSuccess = onSuccess;
        if (Advertisement.IsReady(nameADPista))
        {
            Advertisement.Show(nameADPista);
        }
        else { Debug.Log("Rewarded no esta lista"); }
    }

    public void ShowBanner()
    {
        if (Advertisement.IsReady(nameADBanner))
        {
            Advertisement.Banner.SetPosition(BannerPosition.BOTTOM_CENTER);
            Advertisement.Banner.Show(nameADBanner);
        }
        else
        {
            StartCoroutine(RepeatShowBanner());
        }
    }

    public void HideBanner()
    {
        Advertisement.Banner.Hide();
    }

    IEnumerator RepeatShowBanner()
    {
        yield return new WaitForSeconds(1);
        ShowBanner();
    }

    public void OnUnityAdsReady(string placementId) { }
    public void OnUnityAdsDidError(string message) { }
    public void OnUnityAdsDidStart(string placementId) { }

    public void OnUnityAdsDidFinish(string placementId, ShowResult showResult)
    {
        if (placementId == nameADPista && showResult == ShowResult.Finished)
        {
            onRewardedSuccess.Invoke();
        }
    }
}
