using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

//Cada paquete tiene un array con los tipos y niveles
//Cada nivel pasado se asocia al numero de movimientos . Si es 0 es porque ese nivel no ha sido pasado 
//Si no significa que si lo cual el siguiente es jugable 
public struct package
{
    int [][] movLevels;
}
public class DataUser
{
    public int clues;
    //mat of All levels unblocks
    public package[] p;
}
public class SaveSystem
{

    
  //se puede inicializar la data antes o en el propio metodo 
    public static void SaveData(DataUser data) 
    {
        BinaryFormatter formatter = new BinaryFormatter();
        string path = Application.persistentDataPath + "/save.fun";
        FileStream stream = new FileStream(path, FileMode.Create);
        //Aqui se inicializaria si no esta incializada
        formatter.Serialize(stream, data);
        stream.Close();
    }


    public static DataUser LoadData()
    {
        string path = Application.persistentDataPath + "/save.fun";
        if (File.Exists(path))
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(path, FileMode.Open);
            DataUser data= (DataUser)formatter.Deserialize(stream);
            stream.Close();
            return data;
        }
        else { Debug.LogError("Save file not found " + path);return null; }
    }
}
