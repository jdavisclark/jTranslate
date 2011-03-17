import de.susebox.jtopas.*;
import jtranslate.grammar.GrammarSet;
import jtranslate.parser.GrammarParser;
import jtranslate.parser.TokenType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class ParserTester
{
    public static final String GRAMMAR_NAME = "test/testGrammars/parseTest.jtg";

    public static void main(String[] args) throws FileNotFoundException, TokenizerException
    {
//        GrammarParser parser = new GrammarParser();
//        GrammarSet gs = parser.parse(new File(GRAMMAR_NAME));
       sourceTest();

        int i = 0;
    }

    public static void sourceTest() throws FileNotFoundException, TokenizerException {
        FileReader reader = new FileReader(GRAMMAR_NAME);
        StandardTokenizer t = new StandardTokenizer();
        t.setSource(reader);

        Token t1 = t.nextToken();

        StandardTokenizer tok2 = new StandardTokenizer();
        tok2.setSource(reader);

        int i = 0;
    }


}
