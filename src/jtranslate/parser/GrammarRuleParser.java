package jtranslate.parser;

import de.susebox.jtopas.Token;
import de.susebox.jtopas.TokenizerException;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarType;
import jtranslate.grammar.ReservedRules;
import jtranslate.parser.error.GrammarParserError;

public class GrammarRuleParser extends Parser
{
    public GrammarRuleParser(Parser parent) {
        super(parent);
    }
    
    public GrammarRule parse() throws TokenizerException
    {
        Token tkn = currentToken();
        String ruleName = tkn.getImage();

        if(ReservedRules.RULES.contains(ruleName)) {
            throw new GrammarParserError("\""+ruleName+"\" is a reserved rule! Choose a different name.", this);
        }
        tkn = nextToken(); // consume rule name

        boolean trans = false;
        String transName = null, rule;

        if(currentImage().equals("->")) {
            trans = true;
            tkn = nextToken(); // consume ->

            transName = currentImage();
            tkn = nextToken(); // consume translator name
        }

        if(!currentImage().equals("{")) {
            throw new GrammarParserError("Unexpected token '"+currentImage()+"'", this);
        }

        RuleParser rp = new RuleParser(this);
        rule = rp.parse();
        tkn = currentToken();

        GrammarRule g = new GrammarRule(ruleName, rule, trans ? GrammarType.Translation : GrammarType.Reference);
        if(g.getType() == GrammarType.Translation) {
            g.setTranslatorName(transName);
        }

        return g;
    }
}
