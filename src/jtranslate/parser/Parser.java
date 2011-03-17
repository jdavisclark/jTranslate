package jtranslate.parser;

import de.susebox.jtopas.*;

public abstract class Parser
{
    protected StandardTokenizer tokenizer;

    public Parser(StandardTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Parser(TokenizerProperties props, TokenizerSource source) {
        tokenizer = new StandardTokenizer(props);
        tokenizer.setTokenizerProperties(props);
        tokenizer.setSource(source);
    }

    public Parser(Parser parent) {
        this.tokenizer = parent.tokenizer;
    }

    public Token currentToken() throws TokenizerException {
        return tokenizer.currentToken();
    }

    public String currentImage() throws TokenizerException {
        return tokenizer.currentImage();
    }

    public Token nextToken() throws TokenizerException {
        return tokenizer.nextToken();
    }

    public int getCurrentLine() {
        return tokenizer.getCurrentLine();
    }

    public int getCurrentColumn() {
        return tokenizer.getColumnNumber();
    }

    public TokenizerProperties getProperties() {
        return tokenizer.getTokenizerProperties();
    }

    public void setProperties(TokenizerProperties properties) {
        tokenizer.setTokenizerProperties(properties);
    }

    public boolean hasMoreTokens() {
        return tokenizer.hasMoreToken();
    }

    public void close() {
        tokenizer.close();
    }

    public TokenizerSource getSource() {
        return tokenizer.getSource();
    }
}
