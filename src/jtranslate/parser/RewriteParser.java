package jtranslate.parser;

import de.susebox.jtopas.*;
import jtranslate.grammar.GrammarSet;
import jtranslate.grammar.RewriteRule;
import jtranslate.parser.error.GrammarParserError;

import java.util.LinkedList;

public class RewriteParser extends Parser
{
    public RewriteParser(Parser parent) {
        super(parent);
    }


    public LinkedList<RewriteRule> parse() throws TokenizerException
    {
        TokenizerProperties props = getProperties();
        props.addString("\"", "\"", "\\");
        setProperties(props);

        Token t = currentToken();
        if(!t.getImage().equals("rewrite")) {
            throw new GrammarParserError("Expected 'rewrite', found '"+t.getImage()+"'", this);
        }

        t = nextToken();
        if(!t.getImage().equals("{")) {
            throw new GrammarParserError("Expecting '{', found '"+t.getImage()+"'", this);
        }

        t = nextToken();
        if(t.getType() != Token.STRING) {
            throw new GrammarParserError("Expecting a string, found '"+currentImage()+"'", this);
        }

        RewriteRuleParser rrp = new RewriteRuleParser(this);
        LinkedList<RewriteRule> rules = new LinkedList<RewriteRule>();
        while(!currentImage().equals("}") && currentToken().getType() != Token.EOF)
        {
            rules.add(rrp.parse());
        }

        props.removeString("\"");
        setProperties(props);

        nextToken(); // consume closing }
        return rules;
    }


    private class RewriteRuleParser extends Parser
    {
        public RewriteRuleParser(Parser parent) {
            super(parent);
        }

        public RewriteRule parse() throws TokenizerException
        {
            String search = currentImage();
            nextToken(); // consume search string

            if(!currentImage().equals("->")) {
                throw new GrammarParserError("Expecting '->', found '"+currentImage()+"'", this);
            }

            nextToken();
            String rewrite = currentImage();

            nextToken();
            if(!currentImage().equals(";")) {
                throw new GrammarParserError("Expecting ';', found '"+currentImage()+"'", this);
            }

            nextToken(); // consume ';'

            return new RewriteRule(search.substring(1, search.length()-1), rewrite.substring(1, rewrite.length()-1));
        }
    }
}
