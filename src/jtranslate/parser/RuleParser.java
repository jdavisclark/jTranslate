package jtranslate.parser;

import de.susebox.jtopas.Token;
import de.susebox.jtopas.Tokenizer;
import de.susebox.jtopas.TokenizerException;

public class RuleParser extends Parser
{
    public RuleParser(Parser parent) {
        super(parent);
    }

    public String parse() throws TokenizerException {
        int count = 1;
        Token lastToken = currentToken();
        Token t = nextToken(); // consume opening {

        StringBuilder sb = new StringBuilder();

        while (count > 0 && t.getType() != Token.EOF) {
            String img = currentImage();
            if (img.equals("{") && !isDelimiter(lastToken)) {
                count++;
            }
            else if (img.equals("}") && !isDelimiter(lastToken)) {
                count--;
            }

            if (count > 0) {
                sb.append(img);
                lastToken = currentToken();
                t = nextToken();
            }
        }

        return sb.toString();
    }

    public boolean isDelimiter(Token t) {
        return t.getImage().equals("\\");
    }
}
