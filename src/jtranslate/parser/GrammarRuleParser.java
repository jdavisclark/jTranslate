package jtranslate.parser;

import de.susebox.jtopas.Flags;
import de.susebox.jtopas.Token;
import de.susebox.jtopas.TokenizerException;
import de.susebox.jtopas.TokenizerProperties;
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

        BlockParser rp = new BlockParser(this);
        rule = rp.parse();
        tkn = currentToken();

        String script = null;
        if(currentCompanion() == TokenType.MAP) {
//            TokenizerProperties props = getProperties();
//            props.addString("{{", "}}", "\\");
//            setProperties(props);
//            nextToken(); // consume ->
//            script = currentImage().trim();
//            script = script.substring(2, script.length()-2).trim();
//
//            TokenizerProperties props2 = getProperties();
//            props2.removeString("{{");
//            setProperties(props2);
//
//            nextToken(); // consume script

            nextToken(); // consume ->

            TokenizerProperties props = getProperties();
            props.removeWhitespaces(" ");
            setProperties(props);

            BlockParser blockParser = new BlockParser(this);
            script = blockParser.parse().trim();

            props.addWhitespaces(" ");
            setProperties(props);
        }

        GrammarType type;
        if(trans && script == null) {
            type = GrammarType.Translation;
        }
        else if(script != null) {
            type = GrammarType.TranslationScript;
        }
        else {
            type = GrammarType.Reference;
        }

        GrammarRule g = new GrammarRule(ruleName, rule, type);
        if(g.getType() == GrammarType.Translation) {
            g.setTranslatorName(transName);
        }
        if(g.getType() == GrammarType.TranslationScript) {
            g.setScript(script);
        }


        return g;
    }
}
