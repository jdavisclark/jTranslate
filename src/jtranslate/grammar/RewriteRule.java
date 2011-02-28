package jtranslate.grammar;

public class RewriteRule
{
    private String search;
    private String replace;

    public RewriteRule(String search, String replace) {
        this.search = search;
        this.replace = replace;
    }

    public String getSearch() {
        return search;
    }

    public String getReplace() {
        return replace;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
