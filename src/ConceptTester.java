import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.susebox.jtopas.TokenizerException;
import jtranslate.parser.GrammarParser;

public class ConceptTester
{
    public static void main(String ... args) throws Exception
    {
        File g = new File("test/testGrammars/parseTest.jtg");
        GrammarParser parser = new GrammarParser();
        parser.parse(g);
    }
}
