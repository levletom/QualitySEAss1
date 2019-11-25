import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class NameHandlerTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void parseNames() {
        String path = "https://www.behindthename.com/names/usage/english";
        NameHandler nameHandler = new NameHandler();
        assertTrue(nameHandler.parseAndSave(path,"names_list.txt"));
    }

    @org.junit.Test
    public void countSpecifStringTest(){
        String[] args = new String[]{NameHandler.COUNT_SPECIFIC_STRING_ARG, "Ale"};
        NameHandler.main(args);

    }

    @org.junit.Test
    public void countAllStringsTest(){
        String[] args = new String[]{NameHandler.COUNT_ALL_STRINGS_ARG, "2"};
        NameHandler.main(args);
    }

    @org.junit.Test
    public void allIncludeStringTest(){
        String[] args = new String[]{NameHandler.ALL_INCLUDES_STRING_ARG, "isabelaAlexCahrly"};
        NameHandler.main(args);
    }

    @org.junit.Test
    public void countMaxStringTest(){
        String[] args = new String[]{NameHandler.COUNT_MAX_STRING_ARG, "3"};
        NameHandler.main(args);
    }
    @org.junit.Test
    public void generateNameTest(){
        for (int i = 0; i < 100; i++) {
            String[] args = new String[]{NameHandler.GENERATE_NAME_ARG};
            NameHandler.main(args);
        }

    }
}