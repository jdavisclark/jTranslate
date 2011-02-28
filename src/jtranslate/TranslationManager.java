package jtranslate;

import jtranslate.grammar.GrammarRule;
import jtranslate.grammar.GrammarSet;
import jtranslate.grammar.GrammarType;
import jtranslate.grammar.RewriteRule;

import java.io.File;
import java.io.IOException;
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

	public String translate(File file, Iterable<RewriteRule> rewriteRules, Iterable<GrammarRule> grammarRules) throws IOException
	{
		Scanner scan = new Scanner(file);
		scan.useDelimiter("\\z");
			String source = scan.next();
		scan.close();

        for(RewriteRule rr : rewriteRules) {
            source = source.replace(rr.getSearch(), rr.getReplace());
        }

		for(GrammarRule gram : grammarRules)
		{
			if(gram.getType() != GrammarType.Translation){
				continue;
			}

			Matcher mat = Pattern.compile(gram.getRule()).matcher(source);
			while(mat.find())
			{
				String group = mat.group();
				Translator translator = this.resolve(gram.getKey());
				source = source.replace(group, translator.translate(mat.toMatchResult()));
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
