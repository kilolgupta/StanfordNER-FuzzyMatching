import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;


public class StanfordNer {

    public static HashSet<String> identifyNER(String text,String model) {

        // replacing multiple spaces with single space
        String processedText = text.trim().replaceAll(" +", " ");

        // identify named entities
        LinkedHashMap <String,ArrayList<String>> map=new <String,ArrayList<String>>LinkedHashMap();
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(model);
        List<List<CoreLabel>> classify = classifier.classify(processedText);
        for (List<CoreLabel> coreLabels : classify) {
            for (CoreLabel coreLabel : coreLabels) {
                String word = coreLabel.word();
                String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if(!"O".equals(category)) {
                    if(map.containsKey(category)) {
                        // key is already their just insert in arraylist
                        map.get(category).add(word);
                    }
                    else {
                        ArrayList<String> temp=new ArrayList<>();
                        temp.add(word);
                        map.put(category,temp);
                    }
                }
            }
        }
        // search for combined names to build combined entities in place of separated words
        ArrayList<String> names = map.get("PERSON");
        // variable to store complete names
        HashSet<String> combinedNames = new HashSet<>();
        String name = names.get(0);
        for(int i=1;i<names.size();i++) {
            String nextWord = names.get(i);
            String combinedWord = name + " " + nextWord;
            if(processedText.contains(combinedWord)){
                name = combinedWord;
            }
            else {
                combinedNames.add(name);
                name = nextWord;
            }
        }
        return combinedNames;
    }

    public static void leadersName(String fileName) throws Exception{
        ArrayList<String> leaders = new ArrayList<>();
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String leaderName = "";
            String[] words = line.split("\t");
            leaderName = leaderName + words[1];
            for(int i=2;i<words.length;i++) {
                if(Character.isDigit(words[i].charAt(0))){
                    break;
                }
                else {
                    leaderName = leaderName + " " + words[i];
                }
            }
            leaders.add(leaderName);
        }
        PrintWriter writer = new PrintWriter("LeadersNames.txt", "UTF-8");
        for(String name: leaders) {
            writer.println(name);
        }
        writer.close();
    }

    public static void main(String args[]) throws Exception
    {
        leadersName("leaders1970s.txt");
        // reading cables one by one
        File folder = new File("leaders/textFiles");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles!=null) {
            for( File file: listOfFiles) {
                if(file.isFile()) {
                    Scanner sc = new Scanner(file);
                    sc.useDelimiter("\\Z");
                    String content = sc.next();
                    HashSet<String> namedEntities = identifyNER(content, "stanford-ner-2014-01-04/classifiers/english.all.3class.distsim.crf.ser.gz");

                    String relativePath = "leaders/nerFiles/"+file.getName();
                    File nerFile = new File(relativePath);
                    if(nerFile.createNewFile()){
                        PrintWriter writer = new PrintWriter("leaders/nerFiles/" + nerFile.getName(), "UTF-8");
                        for(String name:namedEntities) {
                            writer.println(name);
                        }
                        writer.close();
                    }
                    System.out.println("Processed " + file.getName());
                }
            }
        }
    }
}
