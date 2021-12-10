using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections.Generic;

[System.Serializable]
public class DataSystem
{
    public int clues;
    public Dictionary<int, List<int[]>> minFlow;
    public DataSystem(int clues)
    {
        this.clues = clues;
        minFlow = new Dictionary<int, List<int[]>>();
    }

}
public class SaveSystem
{

  //se puede inicializar la data antes o en el propio metodo 
    public static void SaveData(DataSystem data) 
    {

        BinaryFormatter formatter = new BinaryFormatter();
        string path = Application.persistentDataPath + "/save.fun";
        Debug.Log(path);
        FileStream stream = new FileStream(path, FileMode.Create);
        //Aqui se inicializaria si no esta incializada
        formatter.Serialize(stream, data);
        stream.Close();
    }


    public static DataSystem LoadData()
    {
        string path = Application.persistentDataPath + "/save.fun";
        if (File.Exists(path))
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(path, FileMode.Open);
            DataSystem data = (DataSystem)formatter.Deserialize(stream);
            stream.Close();
            return data;
        }
        else {return null; }
    }
}
