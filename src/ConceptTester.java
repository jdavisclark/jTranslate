import java.io.*;
import java.net.URL;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bsh.EvalError;
import bsh.Interpreter;
import de.susebox.jtopas.TokenizerException;
import jtranslate.parser.GrammarParser;
import org.apache.commons.io.FilenameUtils;
import sun.plugin.com.BeanClass;

public class ConceptTester
{
    public static void main(String ... args) throws Exception
    {
//        File g = new File("test/testGrammars/parseTest.jtg");
//        GrammarParser parser = new GrammarParser();
//        parser.parse(g);
        interpreterTest();
    }

    public static void streamTok() throws FileNotFoundException
    {
        StreamTokenizer tok = new StreamTokenizer(new FileReader(ParserTester.GRAMMAR_NAME));
    }

    public static void interpreterTest() throws EvalError {
        String command = "import java.util.regex.MatchResult;int a = 234;return a+name;";
        Interpreter i = new Interpreter();
        i.set("name", "Davis Clark");
        Object o = i.eval(command);


        System.out.println(o);
    }
}
