package jtranslate.parser;

import de.susebox.jtopas.Token;
import de.susebox.jtopas.Tokenizer;
import de.susebox.jtopas.TokenizerException;

public class RuleParser
{
    private Tokenizer tkn;

    public RuleParser(Tokenizer tkn) {
        this.tkn = tkn;
    }

    public String parse() throws TokenizerException {
        int count = 1;
        Token lastToken = tkn.currentToken();
        Token t = tkn.nextToken(); // consume opening {

        StringBuilder sb = new StringBuilder();

        while (count > 0 && t.getType() != Token.EOF) {
            String img = tkn.currentImage();
            if (img.equals("{") && !isDelimiter(lastToken)) {
                count++;
            } else if (img.equals("}") && !isDelimiter(lastToken)) {
                count--;
            }

            if (count > 0) {
                sb.append(img);
                lastToken = tkn.currentToken();
                t = tkn.nextToken();
            }
        }

        return sb.toString();
    }

    public boolean isDelimiter(Token t) {
        return t.getImage().equals("\\");
    }
}
