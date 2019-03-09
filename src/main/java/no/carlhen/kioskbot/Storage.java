package no.carlhen.kioskbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    // Basic Storage Section

    private static File BasePath = new File(System.getProperty("user.home") + "/.KantineBot");

    @SuppressWarnings("unchecked")
    public static void SaveFile(String content, String fileName) {
        if (!BasePath.exists()) {
            BasePath.mkdir();
        }
        try (FileWriter file = new FileWriter(BasePath.getPath() + "/" + fileName)) {
            file.write(content);
        }
        catch(Exception e) {
            Main.logger.error(e);
        }
    }

    public static String ReadFile(String fileName){
        File file = new File(BasePath.getPath() + "/" + fileName);
        if (!file.exists()){
            SaveFile("", fileName);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String tmp;
            String returnString = "";
            while ((tmp = reader.readLine()) != null){
                returnString += tmp + "\n";
            }
            // Remove last \n
            if (returnString.length() == 0){
                return returnString;
            }
            else{
                return returnString.substring(0, returnString.length() - 1);
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // List Files Section

    public static <T> void SaveList(List<T> currList, String fileName){
        Gson gson = new GsonBuilder().create();
        Storage.SaveFile(gson.toJson(currList), fileName);
    }

    public static <T> List<T> GetList(String fileName, TypeToken<List<T>> typeToken){
        Gson gson = new GsonBuilder().create();
        List<T> ordersList = gson.fromJson(Storage.ReadFile(fileName), typeToken.getType());
        if (ordersList == null){
            return new ArrayList<>();
        }
        else{
            return ordersList;
        }
    }

    public static <T extends GenericModel<T>> List<T> UpdateList(List<T> list, T entry, String fileName){
        for (int i = 0; i < list.size(); i++)
            if (entry.id.equals(list.get(i).id)){
                list.set(i, entry);
                SaveList(list, fileName);
                return list;
            }
        return list;
    }
}
