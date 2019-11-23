import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class NameHandler {

    private static final String INVALID_USAGE = "Invalid Usage";
    public static final String COUNT_SPECIFIC_STRING_ARG = "CountSpecificString";
    public static final String COUNT_ALL_STRINGS_ARG = "CountAllStrings";
    public static final String COUNT_MAX_STRING_ARG = "CountMaxString";
    public static final String ALL_INCLUDES_STRING_ARG = "AllIncludesString";
    public static final String GENERATE_NAME_ARG = "GenerateName";
    private static final String URL_NAMES_BASE = "https://www.behindthename.com/names/usage/english";
    private static final int UNEXPECTED_EXIT_STATUS = 1;
    private List<String> names;
    private static final String NAMES_FILE = "names_list.txt";
    private static final int METHOD_ARG_POS = 0;
    private static final int ARGUMENT_ARG_POS = 1;


//./Script.jar CountSpecificString STRING
//./Script.jar CountAllStrings LENGTH
//./Script.jar CountMaxString LENGTH
//./Script.jar AllIncludesString STRING
//./Script.jar GenerateName
    public static void main(String[] args){
        NameHandler nameHandler = new NameHandler();
        if(!nameHandler.initNames())
            exit(UNEXPECTED_EXIT_STATUS);
        switch (args[METHOD_ARG_POS]){
            case COUNT_SPECIFIC_STRING_ARG:
                nameHandler.countSpecificString(args[ARGUMENT_ARG_POS]);
                break;
            case COUNT_ALL_STRINGS_ARG:
                nameHandler.countAllStrings(Integer.parseInt(args[ARGUMENT_ARG_POS]));
                break;
            case COUNT_MAX_STRING_ARG:
                nameHandler.countMaxString(Integer.parseInt(args[ARGUMENT_ARG_POS]));
                break;
            case ALL_INCLUDES_STRING_ARG:
                nameHandler.allIncludesString(args[ARGUMENT_ARG_POS]);
                break;
            case GENERATE_NAME_ARG:
                nameHandler.generateName();
                break;
            default:
                System.out.println(INVALID_USAGE);

        }
    }

    private void generateName() {

    }

    private void allIncludesString(String substring) {

    }

    private void countMaxString(int length) {

    }

    private void countAllStrings(int length) {
    }

    private void countSpecificString(String subString) {
        System.out.println(names.size());
    }

    private boolean initNames() {
        try {
            this.names = deserializeStringList(NAMES_FILE);
        } catch (IOException e) {
            if (parseAndSave(URL_NAMES_BASE,NAMES_FILE)){
                try {
                    this.names = deserializeStringList(NAMES_FILE);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
            else {
                e.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean parseAndSave(String pathToNames, String pathToFileSave){
        List<String> names = null;
        try {
            names = parseNames(pathToNames);
            serializeStringList(pathToFileSave,names);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private List<String> parseNames(String path) throws IOException {
        List<String > stringNames = new LinkedList<>();
        for (int i = 1; i < 15; i++) {
            String indexedPath = path + "/" + i;
            Document doc = Jsoup.connect(indexedPath).get();
            doc.select("span[class=\"tiny\"]").remove();
            Elements elementNames = doc.select("span[class=\"listname\"]");
            stringNames.addAll(elementNames.eachText());
        }
        return stringNames;
    }

    private void serializeStringList(String fileName, List<String> obj) throws IOException {
        FileOutputStream file = new FileOutputStream(fileName);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(obj);
        out.close();
        file.close();
    }

    private List<String> deserializeStringList(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(file);

        List<String> names = (List<String>)in.readObject();
        in.close();
        file.close();
        return names;
    }

    // <span class="listname"><a href="/name/abbey">ABBEY</a></span>
}
