package jtranslate.parser;

import de.susebox.jtopas.*;
import jtranslate.grammar.GrammarSet;
import jtranslate.grammar.RewriteRule;

import java.util.LinkedList;

public class RewriteParser
{
    public LinkedList<RewriteRule> parse(StandardTokenizer tokenizer) throws TokenizerException
    {
        TokenizerProperties props = tokenizer.getTokenizerProperties();
        props.addString("\"", "\"", "\\");
        tokenizer.setTokenizerProperties(props);

        Token t = tokenizer.currentToken();
        if(!t.getImage().equals("rewrite")) {
            throw new Error("Expected 'rewrite', found '"+t.getImage()+"'");
        }

        t = tokenizer.nextToken();
        if(!t.getImage().equals("{")) {
            throw new Error("Expecting '{', found '"+t.getImage()+"'");
        }

        t = tokenizer.nextToken();
        if(t.getType() != Token.STRING) {
            throw new Error("Expecting a STRING");
        }

        RewriteRuleParser rrp = new RewriteRuleParser();
        LinkedList<RewriteRule> rules = new LinkedList<RewriteRule>();
        while(!tokenizer.currentImage().equals("}") && tokenizer.currentToken().getType() != Token.EOF)
        {
            rules.add(rrp.parse(tokenizer));
        }

        props.removeString("\"");
        tokenizer.setTokenizerProperties(props);

        return rules;
    }


    private class RewriteRuleParser
    {
        public RewriteRule parse(StandardTokenizer tokenizer) throws TokenizerException
        {
            String search = tokenizer.currentImage();
            tokenizer.nextToken(); // consume search string

            if(!tokenizer.currentImage().equals("->")) {
                throw new Error("Expecting '->', found '"+tokenizer.currentImage()+"'");
            }

            tokenizer.nextToken();
            String rewrite = tokenizer.currentImage();

            tokenizer.nextToken();
            if(!tokenizer.currentImage().equals(";")) {
                throw new Error("Expecting ';', found '"+tokenizer.currentImage()+"'");
            }

            tokenizer.nextToken(); // consume ';'

            return new RewriteRule(search.substring(1, search.length()-1), rewrite.substring(1, rewrite.length()-1));
        }
    }
}
