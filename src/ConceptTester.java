import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.susebox.jtopas.TokenizerException;
import jtranslate.parser.GrammarParser;
import org.apache.commons.io.FilenameUtils;

public class ConceptTester
{
    public static void main(String ... args) throws Exception
    {
//        File g = new File("test/testGrammars/parseTest.jtg");
//        GrammarParser parser = new GrammarParser();
//        parser.parse(g);

        streamTok();

       int i = 0;
    }

    public static void streamTok() throws FileNotFoundException
    {
        StreamTokenizer tok = new StreamTokenizer(new FileReader(ParserTester.GRAMMAR_NAME));
    }
}
