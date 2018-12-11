import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FindNewMatches {

    public static HashMap<String, ArrayList<String>> getLeaders(String folderName) throws Exception{

        HashMap<String, ArrayList<String>> leaders = new HashMap<>();

        String filePath = new File("").getAbsolutePath();
        String fileSeparator = "/";

        File folder = new File("leaders/" + folderName);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                Set<String> leadersInFile = new HashSet<>();
                FileReader fileReader = new FileReader(filePath + fileSeparator + "leaders/" + folderName + "/" + file.getName());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(!line.equalsIgnoreCase("")) {
                        String[] words = line.split("\\|");
                        String leader1 = words[1];
                        leadersInFile.add(leader1);
                    }
                }
                ArrayList<String> listLeaders = new ArrayList<>(leadersInFile);
                leaders.put(file.getName().split("_")[0], listLeaders);
            }
        }
        return leaders;
    }

    public static void main(String[] args) throws Exception {
        File folder = new File("leaders/lowerCase-textFiles");
        File[] listOfFiles = folder.listFiles();

        HashMap<String, ArrayList<String>> leaders1 = getLeaders("matchFilesV2");
        HashMap<String, ArrayList<String>> leaders2 = getLeaders("nouns_matchFiles");

        for(String key: leaders1.keySet()) {
            ArrayList<String> l1 = leaders1.get(key);
            ArrayList<String> l2 = leaders2.get(key);
            l2.removeAll(l1);
            BufferedWriter writer = new BufferedWriter(new FileWriter(key + "_newMatches.txt"));
            for(String match: l2) {
                writer.write(match+"\n");
            }
            writer.close();
        }
    }

}
