package jtranslate.parser;

import de.susebox.jtopas.StandardTokenizer;
import de.susebox.jtopas.Token;
import de.susebox.jtopas.TokenizerException;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarType;
import jtranslate.grammar.ReservedRules;

public class GrammarRuleParser
{
    public GrammarRule parse(StandardTokenizer tokenizer) throws TokenizerException
    {
        Token tkn = tokenizer.currentToken();
        String ruleName = tkn.getImage();

        if(ReservedRules.RULES.contains(ruleName)) {
            throw new Error("\""+ruleName+"\" is a reserved rule! Choose a different name.");
        }
        tkn = tokenizer.nextToken(); // consume rule name

        boolean trans = false;
        String transName = null, rule;

        if(tokenizer.currentImage().equals("->")) {
            trans = true;
            tkn = tokenizer.nextToken(); // consume ->

            transName = tokenizer.currentImage();
            tkn = tokenizer.nextToken(); // consume translator name
        }

        if(!tokenizer.currentImage().equals("{")) {
            throw new Error("Unexpexted token at line: "+tokenizer.getCurrentLine() +", column: "+tokenizer.getCurrentColumn());
        }

        RuleParser rp = new RuleParser(tokenizer);
        rule = rp.parse();
        tkn = tokenizer.currentToken();

        GrammarRule g = new GrammarRule(ruleName, rule, trans ? GrammarType.Translation : GrammarType.Natural);
        if(g.getType() == GrammarType.Translation) {
            g.setTranslatorName(transName
            );
        }

        return g;
    }
}
