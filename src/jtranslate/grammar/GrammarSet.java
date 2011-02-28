package jtranslate.grammar;

import java.util.LinkedList;

public class GrammarSet
{
    private LinkedList<RewriteRule> rewriteRules;
    private LinkedList<GrammarRule> grammarRules;

    public GrammarSet(LinkedList<RewriteRule> rewriteRules, LinkedList<GrammarRule> grammarRules) {
        this.rewriteRules = rewriteRules;
        this.grammarRules = grammarRules;
    }

    public GrammarSet() {
        this.rewriteRules = new LinkedList<RewriteRule>();
        this.grammarRules = new LinkedList<GrammarRule>();
    }

    public LinkedList<RewriteRule> getRewriteRules() {
        return rewriteRules;
    }

    public LinkedList<GrammarRule> getGrammarRules() {
        return grammarRules;
    }

    public void addGrammarRule(GrammarRule rule) {
        this.grammarRules.add(rule);
    }

    public void addRewriteRule(RewriteRule rule) {
        this.rewriteRules.add(rule);
    }

    public void addRewriteRules(LinkedList<RewriteRule> rules) {
        this.rewriteRules.addAll(rules);
    }

    public void addSet(GrammarSet set) {
        this.grammarRules.addAll(set.getGrammarRules());
        this.rewriteRules.addAll(set.getRewriteRules());
    }
}
