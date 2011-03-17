package jtranslate.parser.error;

import jtranslate.parser.Parser;

public class GrammarParserError extends Error
{
    private Parser parser;
    private String message;

    public GrammarParserError(String message, Parser parser) {
        super(message);
        this.message = message;
        this.parser = parser;
    }

    public String getMessage() {
        return this.message+": Line "+(parser.getCurrentLine()+1)+", Column "+ parser.getCurrentColumn();
    }
}
