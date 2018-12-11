import java.io.*;

public class PrepareFiles {

    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String fileSeparator = "/";

        File folder = new File("leaders/textFiles");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles!=null) {
            for (File file : listOfFiles) {
                try {
                    FileReader fileReader = new FileReader(filePath + fileSeparator + "leaders/textFiles/" + file.getName());
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName()));
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        line = line.toLowerCase();
                        writer.write(line);
                        System.out.println("test");
                    }
                    fileReader.close();
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
