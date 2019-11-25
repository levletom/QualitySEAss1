import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;

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
    private static final int RANDOM_NAME_LENGTH = 20;
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
        HashMap<Character,List<Character>> letterToNextLetterList = new HashMap<>();
        List<Character> aToz = getAllCharsInRange('a','z');
        List<Character> AToZ = getAllCharsInRange('A','Z');
        List<Character> aTozAndAtoZ = new LinkedList<>();
        aTozAndAtoZ.addAll(aToz);
        aTozAndAtoZ.addAll(AToZ);
        for (char letter :
                aTozAndAtoZ) {
            int max = -1;
            for (char seconLetter :
                aToz) {
                String subString = letter +""+ seconLetter;
                int occur = countAllOccurs(subString);
                if (max<occur) {
                    max = occur;
                    LinkedList<Character> list = new LinkedList<>();
                    list.add(seconLetter);
                    letterToNextLetterList.put(letter,list);
                }
                else if(max==occur){
                    List<Character> list =  letterToNextLetterList.getOrDefault(letter,new LinkedList<>());
                    list.add(seconLetter);
                    letterToNextLetterList.put(letter,list);
                }
            }
        }
        int max =-1;
        List<Character> firstLetterList=new LinkedList<>();
        for (char letter :
                AToZ) {
            int occur = countAllOccurs(""+letter);
            if (max<occur) {
                firstLetterList.clear();
                firstLetterList.add(letter);
                max=occur;
            }
            else if (max==occur)
                firstLetterList.add(letter);
        }
        StringBuilder generated = new StringBuilder();
        char firstLetter = getRandomItemFromCharList(firstLetterList);
        generated.append(firstLetter);
        char prevLetter = firstLetter;
        for (int i = 1; i < RANDOM_NAME_LENGTH; i++) {
            List<Character> currLetterList = letterToNextLetterList.get(prevLetter);
            char currLetter = getRandomItemFromCharList(currLetterList);
            generated.append(currLetter);
            prevLetter = currLetter;
        }
        System.out.println(generated.toString());


    }

    public Character getRandomItemFromCharList(List<Character> list) {
        Random rand = new Random();
        return  list.get(rand.nextInt(list.size()));
    }

    private List<Character> getAllCharsInRange(char startIndex, char endIndex) {
        List<Character> result = new LinkedList<>();
        for (int i = startIndex; i <= endIndex ; i++) {
            result.add((char)i);
        }
        return result;
    }

    private void allIncludesString(String stringToCheck) {
        String toCheck = stringToCheck.toLowerCase();
        for (String name :
                this.names) {
            if (toCheck.contains(name.toLowerCase()))
                System.out.println(name.toLowerCase());
        }
    }

    private void countMaxString(int length) {
        HashMap<String, Integer> result = getSubstringStringIntegerHashMap(length,false);
        int max =-1;
        List<String> maxList = new LinkedList<>();
        for (Map.Entry<String,Integer> entry:
                result.entrySet()) {
            int val = entry.getValue();
            String subString = entry.getKey();
            if(val > max){
                maxList.clear();
                max = val;
                maxList.add(subString);
            }
            else if (val==max)
                maxList.add(subString);
        }
        for (String name :
                maxList) {
            System.out.println(name);
        }

    }

    private void countAllStrings(int length) {
        HashMap<String, Integer> result = getSubstringStringIntegerHashMap(length,true);
        for (Map.Entry<String,Integer> entry:
                result.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue().toString());
        }
    }

    private HashMap<String, Integer> getSubstringStringIntegerHashMap(int length,boolean caseSensitive) {
        HashMap<String ,Integer> result = new HashMap<>();
        for (String name :
                this.names) {
            for (int i = 0; i <= name.length() - length; i++) {
                String subName = name.substring(i,i+length);
                if(!caseSensitive)
                    subName = subName.toLowerCase();
                Integer curr = result.getOrDefault(subName,0);
                result.put(subName,curr+1);
            }
        }
        return result;
    }

    private void countSpecificString(String subString) {
        int result = countAllOccurs(subString);
        System.out.println(result);
    }

    private int countAllOccurs(String subString) {
        int result = 0;
        for (String name :
                this.names) {
            if (name.contains(subString))
                result++;
        }
        return result;
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
        List<String> ans = new LinkedList<>();
        for (String name :
                stringNames) {
            StringBuilder sbName = new StringBuilder();
            sbName.append(name.charAt(0));
            sbName.append(name.substring(1).toLowerCase());
            if(isValidName(sbName.toString()))
                ans.add(sbName.toString().strip());
        }
        return ans;
    }

    private boolean isValidName(String name) {
        String validPattern = "[a-zA-Z -]+";
        return name.matches(validPattern);
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
