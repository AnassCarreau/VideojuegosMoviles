using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections.Generic;
using System.Security.Cryptography;
using System.Text;

[System.Serializable]
public class DataSystem
{
    public string hash=string.Empty;
    public int clues;
    public List<Cat> bestScores;

}

[System.Serializable]
public class Cat 
{
     public List<Lot> cat = new List<Lot>();
}

[System.Serializable]
public class Lot
{
    public Lvl[] lvls;
}

[System.Serializable]
public class Lvl
{
    public int bestMoves = 0 ;
    public bool perfect = false;
}

public class SaveSystem
{
    public static void SaveData(DataSystem data)
    {
        data.hash = string.Empty;
        data.hash= Hash(JsonUtility.ToJson(data));

        string json = JsonUtility.ToJson(data);
        string path = Application.persistentDataPath + "/save.json";
        if (File.Exists(path)) 
        {
            File.Delete(path);
        }
        File.WriteAllText(path, json);
    }

    public static DataSystem LoadData()
    {
        string path = Application.persistentDataPath + "/save.json";
        if (File.Exists(path))
        {
            string json = File.ReadAllText(path);
            DataSystem data = JsonUtility.FromJson<DataSystem>(json);

            string hash = data.hash;
            data.hash = string.Empty;
            if (Hash(JsonUtility.ToJson(data)).Equals(hash))
            {
                return data;
            }
            else return null; 
        }
        else { return null; }
    }


    public static string Hash(string data)
    {
        SHA256Managed mySha256 = new SHA256Managed();
        byte[] textToBytes = Encoding.UTF8.GetBytes(data);
        byte[] hashValue = mySha256.ComputeHash(textToBytes);
        return GetHexStringFromHash(hashValue);
    }

    private static string GetHexStringFromHash(byte[] hash)
    {
        string hexString = string.Empty;
        foreach (byte b in hash)
        {
            hexString += b.ToString("x2");
        }
        return hexString;
    }
}
