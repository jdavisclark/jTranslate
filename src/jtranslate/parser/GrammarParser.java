package jtranslate.parser;

import de.susebox.jtopas.*;
import jtranslate.grammar.*;
import jtranslate.parser.error.GrammarParserError;
import sun.font.StandardTextSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

public class GrammarParser extends Parser
{
    public static final StandardTokenizerProperties PROPERTIES;

    static {
        PROPERTIES = new StandardTokenizerProperties();
        PROPERTIES.addBlockComment("/*", "*/");
        PROPERTIES.addLineComment("//");
        PROPERTIES.addSpecialSequence("->", TokenType.MAP);
        PROPERTIES.addSpecialSequence("@", TokenType.SPECIAL_BLOCK);
        PROPERTIES.setParseFlags(Flags.F_COUNT_LINES);
    }

    public GrammarParser(TokenizerSource src)
    {
        super(PROPERTIES, src);
    }

    public GrammarSet parse() throws FileNotFoundException, TokenizerException {
        GrammarSet gs = new GrammarSet();
        Token t;

        while(hasMoreTokens() && (t = nextToken()).getType() != Token.EOF) {
            if(t.getCompanion() == TokenType.SPECIAL_BLOCK) {
                t = nextToken(); // consume @

                if(currentImage().equals("rewrite")) {
                    RewriteParser parser = new RewriteParser(this);
                    LinkedList<RewriteRule> rules = parser.parse();
                    gs.addRewriteRules(rules);
                }
                else {
                    throw new GrammarParserError("No support for special block '"+currentImage()+"'", this);
                }
            }
            else {
                GrammarRuleParser parser = new GrammarRuleParser(this);
                GrammarRule rule = parser.parse();
                gs.addGrammarRule(rule);
            }
        }

        close();
        if(getSource() instanceof ReaderSource) {
            ((ReaderSource)getSource()).close();
        }

        return gs;
    }
}