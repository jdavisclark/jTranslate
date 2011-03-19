package jtranslate;

import bsh.EvalError;
import de.susebox.jtopas.TokenizerException;
import jtranslate.grammar.GrammarManager;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarSet;
import jtranslate.grammar.GrammarType;

import java.io.File;
import java.io.IOException;

public class JTranslateEnvironment
{
    protected TranslationManager transManager;
	protected GrammarManager grammarManager;
    protected GrammarSet set;

    public JTranslateEnvironment(GrammarSet set)
    {
        transManager = new TranslationManager();
        grammarManager = new GrammarManager();
        for(GrammarRule g : set.getGrammarRules()) {
            grammarManager.addRule(g.getKey(), g, false);
        }

        grammarManager.compileRules();
        this.set = set;
    }

	public void registerTranslator(String key, Translator translator)
	{
        transManager.register(key, translator);
	}

    public void registerTranslator(GrammarRule rule, ClassLoader loader) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        Translator trans = (Translator)loader.loadClass(rule.getTranslatorName()).newInstance();
        registerTranslator(rule.getKey(), trans);
    }

    public void registerTranslators(ClassLoader loader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for(GrammarRule rule : grammarManager.getRules()) {
            if(rule.getType() == GrammarType.Translation) {
                registerTranslator(rule, loader);
            }
        }
    }

	public void deRegisterTranslator(String key) {
		transManager.deRegister(key);
	}

	public String translate(File file) throws IOException, EvalError {
		return transManager.translate(file, set.getRewriteRules(), grammarManager.getRules());
	}

    public String translate(String src) {
        return transManager.translate(src, set.getRewriteRules(), grammarManager.getRules());
    }
}
