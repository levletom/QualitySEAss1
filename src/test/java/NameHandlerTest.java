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
        String[] args = new String[]{NameHandler.COUNT_SPECIFIC_STRING_ARG, "As"};
        NameHandler.main(args);

    }
}