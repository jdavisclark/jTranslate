package jtranslate;

import bsh.EvalError;
import bsh.Interpreter;
import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarType;
import jtranslate.grammar.RewriteRule;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationManager
{
	protected Hashtable<String, Translator> map = new Hashtable<String, Translator>();

	public void register(String key, Translator translator)
	{
		if(map.containsKey(key))
			throw new Error("Translator already registered for '"+key+"' !");
		map.put(key, translator);
	}

	public Translator resolve(String key)
	{
		if(!map.containsKey(key))
			throw new Error("No translator registered for '"+key+"' !");
		return map.get(key);
	}

    public void deRegister(String key) {
        if(!map.containsKey(key))
			throw new Error("No Translator has been registered with this key!");
		else
			map.remove(key);
    }

	public String translate(File file, Iterable<RewriteRule> rewriteRules, Iterable<GrammarRule> grammarRules) throws IOException, EvalError {
		Scanner scan = new Scanner(file);
		scan.useDelimiter("\\z");
			String source = scan.next();
		scan.close();

        for(RewriteRule rr : rewriteRules) {
            source = source.replace(rr.getSearch(), rr.getReplace());
        }

		for(GrammarRule gram : grammarRules)
		{
			if(gram.getType() == GrammarType.Reference){
				continue;
			}

			Matcher mat = Pattern.compile(gram.getRule()).matcher(source);
			while(mat.find())
			{
				String group = mat.group();
				if(gram.getType() == GrammarType.Translation) {
                    Translator translator = this.resolve(gram.getKey());
				    source = source.replace(group, translator.translate(mat.toMatchResult()));
                }
                else if(gram.getType() == GrammarType.TranslationScript) {
                    Interpreter i = new Interpreter();
                    i.set("match", mat.toMatchResult());
                    Object result = i.eval(gram.getScript());
                    if(!(result instanceof String))
                        throw new Error("Script for '"+gram.getKey()+"' must return a String!");

                    source = source.replace(group, (String)result);
                }
			}
		}
		return source;
	}

    public String translate(String src, Iterable<RewriteRule> rewriteRules, Iterable<GrammarRule> grammarRules)
    {
        for(RewriteRule rr : rewriteRules) {
            src = src.replace(rr.getSearch(), rr.getReplace());
        }

        for(GrammarRule rule : grammarRules) {
            if(rule.getType() != GrammarType.Translation) {
                continue;
            }

            Matcher mat = rule.getPattern().matcher(src);
            while(mat.find()) {
                String group = mat.group();
                Translator t = this.resolve(rule.getKey());
                src = src.replace(group, t.translate(mat.toMatchResult()));
            }
        }
        return src;
    }

    public boolean hasTranslator(String key) {
        return map.containsKey(key);
    }

    public boolean hasTranslator(Translator trans) {
        return map.containsValue(trans);
    }
}
