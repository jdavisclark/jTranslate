package jtranslate.grammar;

import jtranslate.grammar.GrammarRule;

import java.util.Hashtable;

public class GrammarManager
{
    protected Hashtable<String, GrammarRule> grammars;

    public GrammarManager(Hashtable<String, GrammarRule> grammars) {
        this.grammars = grammars;
    }

    public GrammarManager() {
        this.grammars = new Hashtable<String, GrammarRule>();
    }

    public void addRule(String key, GrammarRule rule, boolean compile) {
        if(grammars.containsKey(key)) {
            throw new Error("Key: \""+key+"\" has already been registered!");
        }
        else {
            if(compile) {
                rule.compileRule(this.grammars);
            }
            grammars.put(key, rule);
        }
    }

    public GrammarRule getRule(String key) {
        if(grammars.containsKey(key)) {
            return grammars.get(key);
        }
        else {
            throw new Error("No rule registered for key \""+key+"\"");
        }
    }

    public Iterable<GrammarRule> getRules() {
        return grammars.values();
    }

    public void compileRules() {
        for(GrammarRule rule : grammars.values()) {
            rule.compileRule(grammars);
        }
    }
}
