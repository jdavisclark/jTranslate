package jtranslate.grammar;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarRule
{
	public static final Pattern RefGrammar = Pattern.compile("\\<[\\w\\d_]+\\>");

	String key;
	String rule;
	GrammarType grammarType;
    String translatorName;

	public GrammarRule(String key, String pattern, GrammarType gt)
	{
		this.key = key;
		rule = pattern;
		grammarType = gt;
	}

	@Override
	public String toString()
	{
		return String.format("Key: %s\n\tPattern: %s\n\tType: %s", key, rule, grammarType.toString());
	}

	public void compileRule(Hashtable<String, GrammarRule> grams)
	{
        boolean changed;
		do {
			changed = false;
			Matcher m = GrammarRule.RefGrammar.matcher(this.rule);
			while(m.find())
			{
				String ref = m.group();
				String refKey = ref.substring(1, ref.length()-1);
				if(!grams.containsKey(refKey))
					throw new Error("Can not compile rule: \""+this.key+"\" Dependency rule \""+ref+"\"' is not defined!");
				String repRule = grams.get(refKey).rule;
				this.rule = this.rule.replace(ref, repRule);
				changed = true;
			}
		} while(changed);
	}

    public Pattern getPattern() {
        return Pattern.compile(this.rule);
    }

    public String getKey() {
        return this.key;
    }

    public String getRule() {
        return this.rule;
    }

    public GrammarType getType() {
        return this.grammarType;
    }

    public String getTranslatorName() {
        return this.translatorName;
    }

    public void setTranslatorName(String name) {
        translatorName = name;
    }




}
