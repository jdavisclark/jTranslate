import de.susebox.jtopas.*;
import jtranslate.JTranslateEnvironment;
import jtranslate.grammar.GrammarManager;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarSet;
import jtranslate.parser.GrammarParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class jTopasTester
{
    public static void main(String[] args) throws IOException, TokenizerException
    {
        GrammarParser parser = new GrammarParser(new ReaderSource(new File("test/grammars/exJava.jtg")));
        GrammarSet set = parser.parse();
        GrammarManager grammarManager = new GrammarManager();
        for(GrammarRule r : set.getGrammarRules()) {
            grammarManager.addRule(r.getKey(), r, false);
        }
        grammarManager.compileRules();
        String testPattern = grammarManager.getRule("implicit_assignment").getRule();


        File f = new File("test/src/test.java");
        StandardTokenizerProperties tps = new StandardTokenizerProperties();
        tps.addBlockComment("/*", "*/");
        tps.addLineComment("//");
        tps.addSpecialSequence("->");
        tps.addSpecialSequence("@");
        tps.addPattern(testPattern);
        tps.addString("\"", "\"", "\\");

        tps.setParseFlags(Flags.F_COUNT_LINES);




        ReaderSource src = new ReaderSource(new FileReader(f));
        StandardTokenizer tokenizer = new StandardTokenizer(tps);
        tokenizer.setSource(src);
        while(tokenizer.hasMoreToken()) {
            Token t = tokenizer.nextToken();
            System.out.println(t);
        }
    }
}
