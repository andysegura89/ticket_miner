import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ReadCSV {
    public ReadCSV(){
    }

    // enumerate titles in CSV file
    public static void indexTitleOrder(Hashtable <String, Integer>titleOrderMap, String titles, Hashtable <Integer, String> titleIndex){
        String[] titlesArray = titles.split(",");
        for(int i =0; i < titlesArray.length; i++){
            // enumerate values
            titleOrderMap.put(titlesArray[i], i);
            titleIndex.put(i, titlesArray[i]);
        }
    }

    public static String readCSV(String path, ArrayList<String[]> myObjects){
        String line = "";
        String titlesString = "";
        // read csv lines through a bufferedreader
        try{ 
            BufferedReader br = new BufferedReader(new FileReader(path));
            titlesString = br.readLine(); // read the title line 
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                // continue if no values
                if(values.length <1)
                    continue;
                // read event values and add to ArrayList
                // inputs will be read at a later time
                myObjects.add(values);
            }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return titlesString;
    }
}
