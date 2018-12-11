import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FindNouns {
    public static void main(String[] args) throws Exception{
        String filePath = new File("").getAbsolutePath();
        String fileSeparator = "/";

        File folder = new File("leaders/pos_tagged_cables");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles!=null) {
            for (File file : listOfFiles) {
                Set<String> nouns = new HashSet<>();
                    FileReader fileReader = new FileReader(filePath + fileSeparator + "leaders/pos_tagged_cables/" + file.getName());
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] words = line.split(" ");
                        for (int i = 0; i < words.length; i++) {
                            String[] splits = words[i].split("/");
                            if (splits.length == 2) {
                                String word = splits[0];
                                String pos = splits[1];
                                if(pos.startsWith("NN")) {
                                    nouns.add(word);
                                }
                            }
                        }
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName() + "nouns.txt"));
                    for(String noun: nouns) {
                       writer.write(noun+"\n");
                    }
                    writer.close();
            }
        }
    }
}
